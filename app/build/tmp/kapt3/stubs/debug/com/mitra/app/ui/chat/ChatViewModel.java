package com.mitra.app.ui.chat;

import android.content.Context;
import androidx.lifecycle.ViewModel;
import com.mitra.app.data.api.ApiMessage;
import com.mitra.app.data.api.ChatRequest;
import com.mitra.app.data.api.MitraApiService;
import com.mitra.app.data.api.StreamingChatSource;
import com.mitra.app.data.model.Chat;
import com.mitra.app.data.model.ChatMessage;
import com.mitra.app.data.model.MessageItem;
import com.mitra.app.data.model.Role;
import com.mitra.app.data.repository.AuthRepository;
import com.mitra.app.data.repository.ChatRepository;
import com.mitra.app.utils.CrisisDetector;
import com.mitra.app.utils.CryptoUtils;
import com.mitra.app.utils.GreetingUtils;
import dagger.hilt.android.lifecycle.HiltViewModel;
import dagger.hilt.android.qualifiers.ApplicationContext;
import kotlinx.coroutines.flow.SharedFlow;
import kotlinx.coroutines.flow.StateFlow;
import java.util.UUID;
import javax.inject.Inject;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000t\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010%\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u001b\b\u0007\u0018\u00002\u00020\u0001B1\b\u0007\u0012\b\b\u0001\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t\u0012\u0006\u0010\n\u001a\u00020\u000b\u00a2\u0006\u0002\u0010\fJ\u0010\u0010%\u001a\u00020&2\u0006\u0010\'\u001a\u00020\u0014H\u0002J\u0006\u0010(\u001a\u00020&J\u000e\u0010)\u001a\u00020&2\u0006\u0010*\u001a\u00020\u0017J\b\u0010+\u001a\u00020&H\u0002J\u0010\u0010,\u001a\u00020&2\u0006\u0010-\u001a\u00020\u000fH\u0002J\u0006\u0010.\u001a\u00020&J\u0006\u0010/\u001a\u00020&J\u0006\u00100\u001a\u00020&J\u0018\u00101\u001a\u00020&2\u0006\u00102\u001a\u00020\u00172\b\u00103\u001a\u0004\u0018\u00010\u0017J\b\u00104\u001a\u00020&H\u0002J\b\u00105\u001a\u00020&H\u0002J\b\u00106\u001a\u00020&H\u0002J\b\u00107\u001a\u00020&H\u0002J\b\u00108\u001a\u00020&H\u0002J\u0016\u00109\u001a\u00020#2\u0006\u0010:\u001a\u00020\u0017H\u0086@\u00a2\u0006\u0002\u0010;J\u000e\u0010<\u001a\u00020&2\u0006\u0010:\u001a\u00020\u0017J\u0010\u0010=\u001a\u00020&2\u0006\u0010>\u001a\u00020#H\u0002J\u000e\u0010?\u001a\u00020&2\u0006\u0010*\u001a\u00020\u0017J\u0010\u0010@\u001a\u00020&2\u0006\u0010:\u001a\u00020\u0017H\u0002R\u0014\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u000f0\u000eX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0010\u001a\b\u0012\u0004\u0012\u00020\u00120\u0011X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0013\u001a\u0004\u0018\u00010\u0014X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010\u0015\u001a\u000e\u0012\u0004\u0012\u00020\u0017\u0012\u0004\u0012\u00020\u00140\u0016X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\u0018\u001a\b\u0012\u0004\u0012\u00020\u000f0\u0019\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001a\u0010\u001bR\u000e\u0010\u001c\u001a\u00020\u001dX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0017\u0010\u001e\u001a\b\u0012\u0004\u0012\u00020\u00120\u001f\u00a2\u0006\b\n\u0000\u001a\u0004\b \u0010!R\u000e\u0010\"\u001a\u00020#X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010$\u001a\u0004\u0018\u00010\u0017X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006A"}, d2 = {"Lcom/mitra/app/ui/chat/ChatViewModel;", "Landroidx/lifecycle/ViewModel;", "appContext", "Landroid/content/Context;", "apiService", "Lcom/mitra/app/data/api/MitraApiService;", "authRepo", "Lcom/mitra/app/data/repository/AuthRepository;", "chatRepo", "Lcom/mitra/app/data/repository/ChatRepository;", "cryptoUtils", "Lcom/mitra/app/utils/CryptoUtils;", "(Landroid/content/Context;Lcom/mitra/app/data/api/MitraApiService;Lcom/mitra/app/data/repository/AuthRepository;Lcom/mitra/app/data/repository/ChatRepository;Lcom/mitra/app/utils/CryptoUtils;)V", "_events", "Lkotlinx/coroutines/flow/MutableSharedFlow;", "Lcom/mitra/app/ui/chat/ChatEvent;", "_state", "Lkotlinx/coroutines/flow/MutableStateFlow;", "Lcom/mitra/app/ui/chat/ChatUiState;", "activeChat", "Lcom/mitra/app/data/model/Chat;", "chatsMap", "", "", "events", "Lkotlinx/coroutines/flow/SharedFlow;", "getEvents", "()Lkotlinx/coroutines/flow/SharedFlow;", "sessionMsgCount", "", "state", "Lkotlinx/coroutines/flow/StateFlow;", "getState", "()Lkotlinx/coroutines/flow/StateFlow;", "supportShown", "", "uid", "checkReturnGreeting", "", "chat", "deleteAllChats", "deleteChat", "chatId", "discardIncognitoIfActive", "emit", "event", "logout", "newChat", "newIncognitoChat", "onSignedIn", "userId", "idToken", "persistActive", "refreshUi", "refreshUiWithSupportCard", "refreshUiWithTyping", "removeTypingIndicator", "sendFeedback", "text", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "sendMessage", "setBusy", "busy", "switchChat", "updateStreamingBubble", "app_debug"})
@dagger.hilt.android.lifecycle.HiltViewModel()
public final class ChatViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull()
    private final android.content.Context appContext = null;
    @org.jetbrains.annotations.NotNull()
    private final com.mitra.app.data.api.MitraApiService apiService = null;
    @org.jetbrains.annotations.NotNull()
    private final com.mitra.app.data.repository.AuthRepository authRepo = null;
    @org.jetbrains.annotations.NotNull()
    private final com.mitra.app.data.repository.ChatRepository chatRepo = null;
    @org.jetbrains.annotations.NotNull()
    private final com.mitra.app.utils.CryptoUtils cryptoUtils = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<com.mitra.app.ui.chat.ChatUiState> _state = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.mitra.app.ui.chat.ChatUiState> state = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableSharedFlow<com.mitra.app.ui.chat.ChatEvent> _events = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.SharedFlow<com.mitra.app.ui.chat.ChatEvent> events = null;
    @org.jetbrains.annotations.NotNull()
    private final java.util.Map<java.lang.String, com.mitra.app.data.model.Chat> chatsMap = null;
    @org.jetbrains.annotations.Nullable()
    private com.mitra.app.data.model.Chat activeChat;
    @org.jetbrains.annotations.Nullable()
    private java.lang.String uid;
    private boolean supportShown = false;
    private int sessionMsgCount = 0;
    
    @javax.inject.Inject()
    public ChatViewModel(@dagger.hilt.android.qualifiers.ApplicationContext()
    @org.jetbrains.annotations.NotNull()
    android.content.Context appContext, @org.jetbrains.annotations.NotNull()
    com.mitra.app.data.api.MitraApiService apiService, @org.jetbrains.annotations.NotNull()
    com.mitra.app.data.repository.AuthRepository authRepo, @org.jetbrains.annotations.NotNull()
    com.mitra.app.data.repository.ChatRepository chatRepo, @org.jetbrains.annotations.NotNull()
    com.mitra.app.utils.CryptoUtils cryptoUtils) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.mitra.app.ui.chat.ChatUiState> getState() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.SharedFlow<com.mitra.app.ui.chat.ChatEvent> getEvents() {
        return null;
    }
    
    public final void onSignedIn(@org.jetbrains.annotations.NotNull()
    java.lang.String userId, @org.jetbrains.annotations.Nullable()
    java.lang.String idToken) {
    }
    
    public final void newChat() {
    }
    
    public final void newIncognitoChat() {
    }
    
    public final void switchChat(@org.jetbrains.annotations.NotNull()
    java.lang.String chatId) {
    }
    
    public final void deleteChat(@org.jetbrains.annotations.NotNull()
    java.lang.String chatId) {
    }
    
    public final void deleteAllChats() {
    }
    
    public final void sendMessage(@org.jetbrains.annotations.NotNull()
    java.lang.String text) {
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object sendFeedback(@org.jetbrains.annotations.NotNull()
    java.lang.String text, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Boolean> $completion) {
        return null;
    }
    
    public final void logout() {
    }
    
    private final void checkReturnGreeting(com.mitra.app.data.model.Chat chat) {
    }
    
    private final void discardIncognitoIfActive() {
    }
    
    private final void persistActive() {
    }
    
    private final void refreshUi() {
    }
    
    private final void refreshUiWithTyping() {
    }
    
    private final void refreshUiWithSupportCard() {
    }
    
    private final void removeTypingIndicator() {
    }
    
    /**
     * Live-update the last Mitra bubble as chunks arrive
     */
    private final void updateStreamingBubble(java.lang.String text) {
    }
    
    private final void setBusy(boolean busy) {
    }
    
    private final void emit(com.mitra.app.ui.chat.ChatEvent event) {
    }
}