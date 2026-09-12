package com.mitra.app.data.model

import java.util.UUID

data class Chat(
    val id: String = "c_${UUID.randomUUID().toString().replace("-", "")}",
    val title: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    var updatedAt: Long = System.currentTimeMillis(),
    val messages: MutableList<ChatMessage> = mutableListOf(),

    @Transient val isIncognito: Boolean = false
) {

    fun displayTitle(): String {
        if (title.isNotBlank()) return title
        val first = messages.firstOrNull { it.role == Role.USER && it.content.isNotBlank() }
        return first?.content?.trim()?.take(42) ?: "New chat"
    }
}

data class ChatDto(
    val id: String = "",
    val title: String = "",
    val createdAt: Long = 0L,
    val updatedAt: Long = 0L,
    val messages: List<MessageDto> = emptyList()
)

data class MessageDto(
    val role: String = "mitra",
    val content: String = "",
    val id: String = "",
    val timestamp: Long = 0L
)

fun Chat.toDto() = ChatDto(
    id = id,
    title = title,
    createdAt = createdAt,
    updatedAt = updatedAt,
    messages = messages.map {
        MessageDto(
            role = it.role.name.lowercase(),
            content = it.content,
            id = it.id,
            timestamp = it.timestamp
        )
    }
)

fun ChatDto.toDomain() = Chat(
    id = id,
    title = title,
    createdAt = createdAt,
    updatedAt = updatedAt,
    messages = messages.map {
        val role = if (it.role == "user") Role.USER else Role.MITRA
        ChatMessage(
            id = if (it.id.isBlank()) UUID.randomUUID().toString() else it.id,
            role = role,
            content = it.content,
            timestamp = if (it.timestamp > 0L) it.timestamp else System.currentTimeMillis()
        )
    }.toMutableList()
)