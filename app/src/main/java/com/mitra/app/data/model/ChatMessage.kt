package com.mitra.app.data.model

import java.util.UUID

/**
 * Roles that mirror the web app: "user" | "mitra"
 * (backend also accepts "mitra" directly — no translation needed)
 */
enum class Role { USER, MITRA }

/** View-types for the RecyclerView adapter */
enum class MessageType { USER, MITRA, TYPING, SUPPORT }

data class ChatMessage(
    val id: String = UUID.randomUUID().toString(),
    val role: Role,
    val content: String,
    val type: MessageType = if (role == Role.USER) MessageType.USER else MessageType.MITRA,
    val timestamp: Long = System.currentTimeMillis()
)

/** Typing indicator — a sentinel item with no real content */
object TypingMessage

/** Crisis-support card — static, shown once per session */
data class SupportCard(val text: String)

/** Unified sealed class for the adapter */
sealed class MessageItem {
    data class Regular(val msg: ChatMessage) : MessageItem()
    object Typing : MessageItem()
    data class Support(val card: SupportCard) : MessageItem()
}
