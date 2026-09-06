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

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0010\n\u0002\u0010\b\n\u0002\b\u0002\b\u0086\b\u0018\u00002\u00020\u0001BC\u0012\u000e\b\u0002\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003\u0012\b\b\u0002\u0010\u0005\u001a\u00020\u0006\u0012\b\b\u0002\u0010\u0007\u001a\u00020\u0006\u0012\b\b\u0002\u0010\b\u001a\u00020\t\u0012\u000e\b\u0002\u0010\n\u001a\b\u0012\u0004\u0012\u00020\u000b0\u0003\u00a2\u0006\u0002\u0010\fJ\u000f\u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003H\u00c6\u0003J\t\u0010\u0014\u001a\u00020\u0006H\u00c6\u0003J\t\u0010\u0015\u001a\u00020\u0006H\u00c6\u0003J\t\u0010\u0016\u001a\u00020\tH\u00c6\u0003J\u000f\u0010\u0017\u001a\b\u0012\u0004\u0012\u00020\u000b0\u0003H\u00c6\u0003JG\u0010\u0018\u001a\u00020\u00002\u000e\b\u0002\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u00062\b\b\u0002\u0010\u0007\u001a\u00020\u00062\b\b\u0002\u0010\b\u001a\u00020\t2\u000e\b\u0002\u0010\n\u001a\b\u0012\u0004\u0012\u00020\u000b0\u0003H\u00c6\u0001J\u0013\u0010\u0019\u001a\u00020\u00062\b\u0010\u001a\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010\u001b\u001a\u00020\u001cH\u00d6\u0001J\t\u0010\u001d\u001a\u00020\tH\u00d6\u0001R\u0011\u0010\b\u001a\u00020\t\u00a2\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000eR\u0017\u0010\n\u001a\b\u0012\u0004\u0012\u00020\u000b0\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u0010R\u0011\u0010\u0005\u001a\u00020\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0011R\u0011\u0010\u0007\u001a\u00020\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\u0011R\u0017\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u0010\u00a8\u0006\u001e"}, d2 = {"Lcom/mitra/app/ui/chat/ChatUiState;", "", "items", "", "Lcom/mitra/app/data/model/MessageItem;", "isBusy", "", "isIncognito", "activeChatId", "", "allChats", "Lcom/mitra/app/data/model/Chat;", "(Ljava/util/List;ZZLjava/lang/String;Ljava/util/List;)V", "getActiveChatId", "()Ljava/lang/String;", "getAllChats", "()Ljava/util/List;", "()Z", "getItems", "component1", "component2", "component3", "component4", "component5", "copy", "equals", "other", "hashCode", "", "toString", "app_debug"})
public final class ChatUiState {
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<com.mitra.app.data.model.MessageItem> items = null;
    private final boolean isBusy = false;
    private final boolean isIncognito = false;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String activeChatId = null;
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<com.mitra.app.data.model.Chat> allChats = null;
    
    public ChatUiState(@org.jetbrains.annotations.NotNull()
    java.util.List<? extends com.mitra.app.data.model.MessageItem> items, boolean isBusy, boolean isIncognito, @org.jetbrains.annotations.NotNull()
    java.lang.String activeChatId, @org.jetbrains.annotations.NotNull()
    java.util.List<com.mitra.app.data.model.Chat> allChats) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.mitra.app.data.model.MessageItem> getItems() {
        return null;
    }
    
    public final boolean isBusy() {
        return false;
    }
    
    public final boolean isIncognito() {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getActiveChatId() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.mitra.app.data.model.Chat> getAllChats() {
        return null;
    }
    
    public ChatUiState() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.mitra.app.data.model.MessageItem> component1() {
        return null;
    }
    
    public final boolean component2() {
        return false;
    }
    
    public final boolean component3() {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component4() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.mitra.app.data.model.Chat> component5() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.mitra.app.ui.chat.ChatUiState copy(@org.jetbrains.annotations.NotNull()
    java.util.List<? extends com.mitra.app.data.model.MessageItem> items, boolean isBusy, boolean isIncognito, @org.jetbrains.annotations.NotNull()
    java.lang.String activeChatId, @org.jetbrains.annotations.NotNull()
    java.util.List<com.mitra.app.data.model.Chat> allChats) {
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