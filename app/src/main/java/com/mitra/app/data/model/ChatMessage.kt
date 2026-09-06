package com.mitra.app.data.model

import java.util.UUID

enum class Role { USER, MITRA }
enum class MessageType { USER, MITRA, TYPING, SUPPORT }

data class ChatMessage(
    val id: String = UUID.randomUUID().toString(),
    val role: Role,
    val content: String,
    val type: MessageType = if (role == Role.USER) MessageType.USER else MessageType.MITRA,
    val timestamp: Long = System.currentTimeMillis()
)

object TypingMessage
data class SupportCard(val text: String)
sealed class MessageItem {
    data class Regular(val msg: ChatMessage) : MessageItem()
    object Typing : MessageItem()
    data class Support(val card: SupportCard) : MessageItem()
}
