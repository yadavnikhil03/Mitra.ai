package com.mitra.app.data.model

import java.util.UUID

/**
 * Represents a full conversation thread, mirroring the web app's chat object.
 * Marked with @Transient fields that should NOT be serialised to Firestore.
 */
data class Chat(
    val id: String = "c_${UUID.randomUUID().toString().replace("-", "")}",
    val title: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    var updatedAt: Long = System.currentTimeMillis(),
    val messages: MutableList<ChatMessage> = mutableListOf(),
    /** True → never persisted, lives only in memory for the session */
    @Transient val isIncognito: Boolean = false
) {
    /** Derives a display title from the first user message, matching web logic */
    fun displayTitle(): String {
        if (title.isNotBlank()) return title
        val first = messages.firstOrNull { it.role == Role.USER && it.content.isNotBlank() }
        return first?.content?.trim()?.take(42) ?: "New chat"
    }
}

/** Firestore-safe DTO (no MutableList, no Role enums — plain strings) */
data class ChatDto(
    val id: String = "",
    val title: String = "",
    val createdAt: Long = 0L,
    val updatedAt: Long = 0L,
    val messages: List<MessageDto> = emptyList()
)

data class MessageDto(
    val role: String = "mitra",   // "user" | "mitra"
    val content: String = ""      // may be base64-encrypted ciphertext
)

fun Chat.toDto() = ChatDto(
    id = id,
    title = title,
    createdAt = createdAt,
    updatedAt = updatedAt,
    messages = messages.map { MessageDto(role = it.role.name.lowercase(), content = it.content) }
)

fun ChatDto.toDomain() = Chat(
    id = id,
    title = title,
    createdAt = createdAt,
    updatedAt = updatedAt,
    messages = messages.map {
        val role = if (it.role == "user") Role.USER else Role.MITRA
        ChatMessage(role = role, content = it.content)
    }.toMutableList()
)
