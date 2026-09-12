package com.mitra.app.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.mitra.app.data.model.Chat
import com.mitra.app.data.model.ChatDto
import com.mitra.app.data.model.MessageDto
import com.mitra.app.data.model.Role
import com.mitra.app.data.model.toDto
import com.mitra.app.data.model.toDomain
import com.mitra.app.utils.CryptoUtils
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChatRepository @Inject constructor(
    private val db: FirebaseFirestore
) {
    // ── Firestore path: users/{uid}/chats/{chatId} ────────────────────────────
    private fun chatsRef(uid: String) = db.collection("users").document(uid).collection("chats")

    /**
     * Load all chats for [uid]. Decrypts message contents if a [cryptoUtils]
     * session key is available, otherwise returns ciphertext as-is (graceful
     * degradation matching web app behaviour).
     */
    suspend fun loadChats(uid: String, cryptoUtils: CryptoUtils?): Map<String, Chat> {
        return try {
            val snap = chatsRef(uid).get().await()
            val result = mutableMapOf<String, Chat>()
            for (doc in snap.documents) {
                val dto = doc.toObject(ChatDto::class.java) ?: continue
                val chat = dto.toDomain()
                // Decrypt each message if we have a session key
                if (cryptoUtils != null) {
                    val decrypted = chat.messages.map { msg ->
                        msg.copy(content = cryptoUtils.decryptOrPlaceholder(msg.content, uid))
                    }
                    chat.messages.clear()
                    chat.messages.addAll(decrypted)
                }
                result[chat.id] = chat
            }
            result
        } catch (e: Exception) {
            emptyMap()
        }
    }

    /**
     * Persist [chat] to Firestore, encrypting every message with [cryptoUtils]
     * when available. Incognito chats must NEVER reach this method.
     */
    suspend fun saveChat(uid: String, chat: Chat, cryptoUtils: CryptoUtils?) {
        require(!chat.isIncognito) { "Incognito chats must not be persisted." }
        if (cryptoUtils == null) return
        try {
            val dto = chat.toDto().let { raw ->
                raw.copy(
                    messages = raw.messages.mapNotNull { m ->
                        val encrypted = cryptoUtils.encrypt(m.content, uid) ?: return@mapNotNull null
                        m.copy(content = encrypted)
                    }
                )
            }
            chatsRef(uid).document(chat.id).set(dto, SetOptions.merge()).await()
        } catch (_: Exception) { /* non-fatal — local state stays intact */ }
    }

    /** Hard-delete a single chat document. */
    suspend fun deleteChat(uid: String, chatId: String) {
        try {
            chatsRef(uid).document(chatId).delete().await()
        } catch (_: Exception) {}
    }

    /** Delete every chat for [uid] — used by "wipe all" action. */
    suspend fun deleteAllChats(uid: String) {
        try {
            val snap = chatsRef(uid).get().await()
            val batch = db.batch()
            snap.documents.forEach { batch.delete(it.reference) }
            batch.commit().await()
        } catch (_: Exception) {}
    }

    /** Submit feedback to the top-level `feedback` collection. */
    suspend fun sendFeedback(uid: String?, text: String): Boolean {
        return try {
            val data = mapOf(
                "uid"       to (uid ?: "anonymous"),
                "text"      to text,
                "createdAt" to com.google.firebase.Timestamp.now()
            )
            db.collection("feedback").add(data).await()
            true
        } catch (_: Exception) {
            false
        }
    }

    /** Log a lightweight analytics event (mirrors __mitraEvent in web app). */
    suspend fun logEvent(uid: String?, deviceId: String, sessionId: String, type: String, extra: Map<String, Any> = emptyMap()) {
        try {
            val data = buildMap<String, Any> {
                put("type", type)
                put("deviceId", deviceId)
                put("sessionId", sessionId)
                if (uid != null) put("uid", uid)
                put("tz", java.util.TimeZone.getDefault().id)
                putAll(extra)
            }
            db.collection("mitra_events").add(data).await()
        } catch (_: Exception) {}
    }
}
