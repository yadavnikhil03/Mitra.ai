package com.mitra.app.ui.chat

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mitra.app.data.api.ApiMessage
import com.mitra.app.data.api.ChatRequest
import com.mitra.app.data.api.MitraApiService
import com.mitra.app.data.api.StreamingChatSource
import com.mitra.app.data.model.Chat
import com.mitra.app.data.model.ChatMessage
import com.mitra.app.data.model.MessageItem
import com.mitra.app.data.model.Role
import com.mitra.app.data.repository.AuthRepository
import com.mitra.app.data.repository.ChatRepository
import com.mitra.app.utils.CrisisDetector
import com.mitra.app.utils.CryptoUtils
import com.mitra.app.utils.GreetingUtils
import com.mitra.app.utils.isOnline
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.UUID
import javax.inject.Inject

// ── UI state ─────────────────────────────────────────────────────────────────

data class ChatUiState(
    val items: List<MessageItem>  = emptyList(),
    val isBusy: Boolean           = false,
    val isIncognito: Boolean      = false,
    val activeChatId: String      = "",
    val allChats: List<Chat>      = emptyList()
)

sealed class ChatEvent {
    object ScrollToBottom : ChatEvent()
    object NavigateToLogin : ChatEvent()
    data class ShowToast(val msg: String) : ChatEvent()
    data class ShowGlowThinking(val on: Boolean) : ChatEvent()
}

// ── ViewModel ─────────────────────────────────────────────────────────────────

@HiltViewModel
class ChatViewModel @Inject constructor(
    @ApplicationContext private val appContext: Context,
    private val apiService: MitraApiService,
    private val authRepo: AuthRepository,
    private val chatRepo: ChatRepository,
    private val cryptoUtils: CryptoUtils
) : ViewModel() {

    private val _state  = MutableStateFlow(ChatUiState())
    val state: StateFlow<ChatUiState> = _state.asStateFlow()

    private val _events = MutableSharedFlow<ChatEvent>()
    val events: SharedFlow<ChatEvent> = _events.asSharedFlow()

    // In-memory session state (never touches disk for incognito)
    private val chatsMap = mutableMapOf<String, Chat>()
    private var activeChat: Chat? = null
    private var uid: String? = null
    private var supportShown = false
    private var sessionMsgCount = 0

    // ── Init: load chats after sign-in ────────────────────────────────────────

    fun onSignedIn(userId: String, idToken: String?) {
        uid = userId
        viewModelScope.launch {
            // Fetch session key for encryption
            if (idToken != null) {
                try {
                    val resp = apiService.getSessionKey("Bearer $idToken")
                    if (resp.isSuccessful) {
                        resp.body()?.key?.let { cryptoUtils.initKey(it) }
                    }
                } catch (_: Exception) {}
            }

            // Warm the backend
            try { apiService.health() } catch (_: Exception) {}

            // Load persisted chats
            val loaded = chatRepo.loadChats(userId, if (cryptoUtils.hasKey) cryptoUtils else null)
            chatsMap.putAll(loaded)

            // Resume most-recent or create fresh
            val mostRecent = chatsMap.values.maxByOrNull { it.updatedAt }
            if (mostRecent != null) {
                activeChat = mostRecent
                checkReturnGreeting(mostRecent)
            } else {
                newChat()
                return@launch
            }

            refreshUi()
            emit(ChatEvent.ScrollToBottom)
        }
    }

    // ── New chat / Incognito ─────────────────────────────────────────────────

    fun newChat() {
        discardIncognitoIfActive()
        val greeting = GreetingUtils.greetingLine()
        val chat = Chat(messages = mutableListOf(
            ChatMessage(role = Role.MITRA, content = greeting)
        ))
        chatsMap[chat.id] = chat
        activeChat = chat
        supportShown = false
        refreshUi()
        persistActive()
        viewModelScope.launch { emit(ChatEvent.ScrollToBottom) }
    }

    fun newIncognitoChat() {
        discardIncognitoIfActive()
        val greeting = GreetingUtils.greetingLine()
        val chat = Chat(
            messages    = mutableListOf(ChatMessage(role = Role.MITRA, content = greeting)),
            isIncognito = true
        )
        chatsMap[chat.id] = chat
        activeChat = chat
        supportShown = false
        refreshUi()
        viewModelScope.launch {
            emit(ChatEvent.ShowToast("Switched to incognito mode"))
            emit(ChatEvent.ScrollToBottom)
        }
    }

    fun switchChat(chatId: String) {
        val target = chatsMap[chatId] ?: return
        discardIncognitoIfActive()
        activeChat = target
        supportShown = false
        refreshUi()
        viewModelScope.launch { emit(ChatEvent.ScrollToBottom) }
    }

    fun deleteChat(chatId: String) {
        val wasActive = chatId == activeChat?.id
        chatsMap.remove(chatId)
        viewModelScope.launch {
            uid?.let { chatRepo.deleteChat(it, chatId) }
        }
        if (wasActive) {
            val next = chatsMap.values.maxByOrNull { it.updatedAt }
            if (next != null) { activeChat = next; refreshUi() }
            else newChat()
        } else {
            refreshUi()
        }
    }

    fun deleteAllChats() {
        chatsMap.clear()
        activeChat = null
        viewModelScope.launch {
            uid?.let { chatRepo.deleteAllChats(it) }
        }
        newChat()
    }

    // ── Send message ─────────────────────────────────────────────────────────

    fun sendMessage(text: String) {
        val chat = activeChat ?: return
        if (text.isBlank() || _state.value.isBusy) return

        if (!appContext.isOnline()) {
            viewModelScope.launch {
                emit(ChatEvent.ShowToast(
                    "Looks like your internet connection dropped. Send your message again once you are back online."
                ))
            }
            return
        }

        // Append user message
        val userMsg = ChatMessage(role = Role.USER, content = text)
        chat.messages.add(userMsg)
        chat.updatedAt = System.currentTimeMillis()
        sessionMsgCount++

        // Crisis detection — show support card if needed
        if (!supportShown && CrisisDetector.isCrisis(text)) {
            supportShown = true
            refreshUiWithTyping()
            viewModelScope.launch {
                kotlinx.coroutines.delay(600)
                val safeReply = GreetingUtils.randomSafeReply()
                val mitraMsg  = ChatMessage(role = Role.MITRA, content = safeReply)
                chat.messages.add(mitraMsg)
                refreshUiWithSupportCard()
                persistActive()
                emit(ChatEvent.ScrollToBottom)
            }
            return
        }

        refreshUiWithTyping()
        setBusy(true)
        emit(ChatEvent.ShowGlowThinking(true))

        viewModelScope.launch {
            var accumulated = ""
            var success     = false

            try {
                val apiMessages = chat.messages
                    .filter { it.type != com.mitra.app.data.model.MessageType.SUPPORT }
                    .takeLast(20)   // MAX_HISTORY = 20
                    .map { ApiMessage(role = it.role.name.lowercase(), content = it.content) }

                val request  = ChatRequest(apiMessages)
                var response = try {
                    apiService.chat(request)
                } catch (_: Exception) { null }

                // Retry once on failure (mirrors web app behaviour)
                if (response == null || !response.isSuccessful) {
                    kotlinx.coroutines.delay(3500)
                    response = apiService.chat(request)
                }

                if (!response.isSuccessful || response.body() == null) {
                    throw Exception("bad response")
                }

                // Remove typing indicator, show streamed reply
                removeTypingIndicator()

                StreamingChatSource.stream(response).collect { chunk ->
                    accumulated += chunk
                    updateStreamingBubble(accumulated)
                    emit(ChatEvent.ScrollToBottom)
                }
                success = true

            } catch (e: Exception) {
                removeTypingIndicator()
                if (accumulated.isEmpty()) {
                    accumulated = listOf(
                        "Could not connect right now. Please try sending again in a moment.",
                        "Connection interrupted. Please try again in a few seconds.",
                        "Something went wrong on my end. Please give it a moment and try again."
                    ).random()
                }
                updateStreamingBubble(accumulated)
            } finally {
                if (accumulated.isNotEmpty()) {
                    // Commit streamed content as a real ChatMessage
                    val mitraMsg = ChatMessage(role = Role.MITRA, content = accumulated)
                    chat.messages.add(mitraMsg)
                    persistActive()
                    refreshUi()
                }
                setBusy(false)
                emit(ChatEvent.ShowGlowThinking(false))
                emit(ChatEvent.ScrollToBottom)
            }
        }
    }

    // ── Feedback / logout ────────────────────────────────────────────────────

    suspend fun sendFeedback(text: String): Boolean =
        chatRepo.sendFeedback(uid, text)

    fun logout() {
        viewModelScope.launch {
            cryptoUtils.clearKey()
            authRepo.signOut()
            chatsMap.clear()
            activeChat = null
            emit(ChatEvent.NavigateToLogin)
        }
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private fun checkReturnGreeting(chat: Chat) {
        val hasHistory = chat.messages.size > 1
        if (!hasHistory) return
        val gapHrs = (System.currentTimeMillis() - chat.updatedAt) / 3_600_000.0
        if (gapHrs > 3.0) {
            viewModelScope.launch {
                kotlinx.coroutines.delay(500)
                val returnMsg = ChatMessage(role = Role.MITRA, content = GreetingUtils.returnLine())
                chat.messages.add(returnMsg)
                refreshUi()
                persistActive()
                emit(ChatEvent.ScrollToBottom)
            }
        }
    }

    private fun discardIncognitoIfActive() {
        activeChat?.takeIf { it.isIncognito }?.let { chatsMap.remove(it.id) }
    }

    private fun persistActive() {
        val chat = activeChat ?: return
        if (chat.isIncognito) return
        val u = uid ?: return
        viewModelScope.launch {
            chatRepo.saveChat(u, chat, if (cryptoUtils.hasKey) cryptoUtils else null)
        }
    }

    private fun refreshUi() {
        val chat  = activeChat ?: return
        val items = chat.messages.map { MessageItem.Regular(it) }
        _state.update {
            it.copy(
                items       = items,
                isIncognito = chat.isIncognito,
                activeChatId = chat.id,
                allChats    = chatsMap.values
                    .filter { c -> !c.isIncognito }
                    .sortedByDescending { c -> c.updatedAt }
            )
        }
    }

    private fun refreshUiWithTyping() {
        val chat  = activeChat ?: return
        val items = chat.messages.map { MessageItem.Regular(it) } + listOf(MessageItem.Typing)
        _state.update { it.copy(items = items) }
    }

    private fun refreshUiWithSupportCard() {
        val chat  = activeChat ?: return
        val card  = com.mitra.app.data.model.SupportCard(
            "If you are carrying something heavy right now, you do not have to handle it alone. Tele-MANAS is free, 24/7, and confidential:"
        )
        val items = chat.messages.map { MessageItem.Regular(it) } + listOf(MessageItem.Support(card))
        _state.update { it.copy(items = items) }
    }

    private fun removeTypingIndicator() {
        val chat  = activeChat ?: return
        val items = chat.messages.map { MessageItem.Regular(it) }
        _state.update { it.copy(items = items) }
    }

    /** Live-update the last Mitra bubble as chunks arrive */
    private fun updateStreamingBubble(text: String) {
        val chat = activeChat ?: return
        // Build item list: all committed messages + a streaming Mitra bubble at end
        val committed = chat.messages.map { MessageItem.Regular(it) }
        val streaming = MessageItem.Regular(
            ChatMessage(role = Role.MITRA, content = text)
        )
        _state.update { it.copy(items = committed + streaming) }
    }

    private fun setBusy(busy: Boolean) {
        _state.update { it.copy(isBusy = busy) }
    }

    private fun emit(event: ChatEvent) {
        viewModelScope.launch { _events.emit(event) }
    }
}
