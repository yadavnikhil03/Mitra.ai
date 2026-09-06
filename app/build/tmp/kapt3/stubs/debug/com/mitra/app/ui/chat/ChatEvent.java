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
import com.mitra.app.data.model.MessageType;
import com.mitra.app.data.model.Role;
import com.mitra.app.data.model.SupportCard;
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

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\b6\u0018\u00002\u00020\u0001:\u0004\u0003\u0004\u0005\u0006B\u0007\b\u0004\u00a2\u0006\u0002\u0010\u0002\u0082\u0001\u0004\u0007\b\t\n\u00a8\u0006\u000b"}, d2 = {"Lcom/mitra/app/ui/chat/ChatEvent;", "", "()V", "NavigateToLogin", "ScrollToBottom", "ShowGlowThinking", "ShowToast", "Lcom/mitra/app/ui/chat/ChatEvent$NavigateToLogin;", "Lcom/mitra/app/ui/chat/ChatEvent$ScrollToBottom;", "Lcom/mitra/app/ui/chat/ChatEvent$ShowGlowThinking;", "Lcom/mitra/app/ui/chat/ChatEvent$ShowToast;", "app_debug"})
public abstract class ChatEvent {
    
    private ChatEvent() {
        super();
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002\u00a8\u0006\u0003"}, d2 = {"Lcom/mitra/app/ui/chat/ChatEvent$NavigateToLogin;", "Lcom/mitra/app/ui/chat/ChatEvent;", "()V", "app_debug"})
    public static final class NavigateToLogin extends com.mitra.app.ui.chat.ChatEvent {
        @org.jetbrains.annotations.NotNull()
        public static final com.mitra.app.ui.chat.ChatEvent.NavigateToLogin INSTANCE = null;
        
        private NavigateToLogin() {
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002\u00a8\u0006\u0003"}, d2 = {"Lcom/mitra/app/ui/chat/ChatEvent$ScrollToBottom;", "Lcom/mitra/app/ui/chat/ChatEvent;", "()V", "app_debug"})
    public static final class ScrollToBottom extends com.mitra.app.ui.chat.ChatEvent {
        @org.jetbrains.annotations.NotNull()
        public static final com.mitra.app.ui.chat.ChatEvent.ScrollToBottom INSTANCE = null;
        
        private ScrollToBottom() {
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0007\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0086\b\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\t\u0010\u0007\u001a\u00020\u0003H\u00c6\u0003J\u0013\u0010\b\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u0003H\u00c6\u0001J\u0013\u0010\t\u001a\u00020\u00032\b\u0010\n\u001a\u0004\u0018\u00010\u000bH\u00d6\u0003J\t\u0010\f\u001a\u00020\rH\u00d6\u0001J\t\u0010\u000e\u001a\u00020\u000fH\u00d6\u0001R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006\u00a8\u0006\u0010"}, d2 = {"Lcom/mitra/app/ui/chat/ChatEvent$ShowGlowThinking;", "Lcom/mitra/app/ui/chat/ChatEvent;", "on", "", "(Z)V", "getOn", "()Z", "component1", "copy", "equals", "other", "", "hashCode", "", "toString", "", "app_debug"})
    public static final class ShowGlowThinking extends com.mitra.app.ui.chat.ChatEvent {
        private final boolean on = false;
        
        public ShowGlowThinking(boolean on) {
        }
        
        public final boolean getOn() {
            return false;
        }
        
        public final boolean component1() {
            return false;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.mitra.app.ui.chat.ChatEvent.ShowGlowThinking copy(boolean on) {
            return null;
        }
        
        @java.lang.Override()
        public boolean equals(@org.jetbrains.annotations.Nullable()
        java.lang.Object other) {
            return false;
        }
        
        @java.lang.Override()
        public int hashCode() {
            return 0;
        }
        
        @java.lang.Override()
        @org.jetbrains.annotations.NotNull()
        public java.lang.String toString() {
            return null;
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0006\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\b\u0086\b\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\t\u0010\u0007\u001a\u00020\u0003H\u00c6\u0003J\u0013\u0010\b\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u0003H\u00c6\u0001J\u0013\u0010\t\u001a\u00020\n2\b\u0010\u000b\u001a\u0004\u0018\u00010\fH\u00d6\u0003J\t\u0010\r\u001a\u00020\u000eH\u00d6\u0001J\t\u0010\u000f\u001a\u00020\u0003H\u00d6\u0001R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006\u00a8\u0006\u0010"}, d2 = {"Lcom/mitra/app/ui/chat/ChatEvent$ShowToast;", "Lcom/mitra/app/ui/chat/ChatEvent;", "msg", "", "(Ljava/lang/String;)V", "getMsg", "()Ljava/lang/String;", "component1", "copy", "equals", "", "other", "", "hashCode", "", "toString", "app_debug"})
    public static final class ShowToast extends com.mitra.app.ui.chat.ChatEvent {
        @org.jetbrains.annotations.NotNull()
        private final java.lang.String msg = null;
        
        public ShowToast(@org.jetbrains.annotations.NotNull()
        java.lang.String msg) {
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.lang.String getMsg() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.lang.String component1() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.mitra.app.ui.chat.ChatEvent.ShowToast copy(@org.jetbrains.annotations.NotNull()
        java.lang.String msg) {
            return null;
        }
        
        @java.lang.Override()
        public boolean equals(@org.jetbrains.annotations.Nullable()
        java.lang.Object other) {
            return false;
        }
        
        @java.lang.Override()
        public int hashCode() {
            return 0;
        }
        
        @java.lang.Override()
        @org.jetbrains.annotations.NotNull()
        public java.lang.String toString() {
            return null;
        }
    }
}