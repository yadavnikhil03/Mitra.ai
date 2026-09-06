package com.mitra.app.data.repository;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.mitra.app.data.model.Chat;
import com.mitra.app.data.model.ChatDto;
import com.mitra.app.data.model.MessageDto;
import com.mitra.app.data.model.Role;
import com.mitra.app.utils.CryptoUtils;
import javax.inject.Inject;
import javax.inject.Singleton;

@javax.inject.Singleton()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000@\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0005\n\u0002\u0010$\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u000b\n\u0002\u0010\u000b\n\u0002\b\u0002\b\u0007\u0018\u00002\u00020\u0001B\u000f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0010\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bH\u0002J\u0016\u0010\t\u001a\u00020\n2\u0006\u0010\u0007\u001a\u00020\bH\u0086@\u00a2\u0006\u0002\u0010\u000bJ\u001e\u0010\f\u001a\u00020\n2\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\r\u001a\u00020\bH\u0086@\u00a2\u0006\u0002\u0010\u000eJ,\u0010\u000f\u001a\u000e\u0012\u0004\u0012\u00020\b\u0012\u0004\u0012\u00020\u00110\u00102\u0006\u0010\u0007\u001a\u00020\b2\b\u0010\u0012\u001a\u0004\u0018\u00010\u0013H\u0086@\u00a2\u0006\u0002\u0010\u0014JF\u0010\u0015\u001a\u00020\n2\b\u0010\u0007\u001a\u0004\u0018\u00010\b2\u0006\u0010\u0016\u001a\u00020\b2\u0006\u0010\u0017\u001a\u00020\b2\u0006\u0010\u0018\u001a\u00020\b2\u0014\b\u0002\u0010\u0019\u001a\u000e\u0012\u0004\u0012\u00020\b\u0012\u0004\u0012\u00020\u00010\u0010H\u0086@\u00a2\u0006\u0002\u0010\u001aJ(\u0010\u001b\u001a\u00020\n2\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\u001c\u001a\u00020\u00112\b\u0010\u0012\u001a\u0004\u0018\u00010\u0013H\u0086@\u00a2\u0006\u0002\u0010\u001dJ \u0010\u001e\u001a\u00020\u001f2\b\u0010\u0007\u001a\u0004\u0018\u00010\b2\u0006\u0010 \u001a\u00020\bH\u0086@\u00a2\u0006\u0002\u0010\u000eR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006!"}, d2 = {"Lcom/mitra/app/data/repository/ChatRepository;", "", "db", "Lcom/google/firebase/firestore/FirebaseFirestore;", "(Lcom/google/firebase/firestore/FirebaseFirestore;)V", "chatsRef", "Lcom/google/firebase/firestore/CollectionReference;", "uid", "", "deleteAllChats", "", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "deleteChat", "chatId", "(Ljava/lang/String;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "loadChats", "", "Lcom/mitra/app/data/model/Chat;", "cryptoUtils", "Lcom/mitra/app/utils/CryptoUtils;", "(Ljava/lang/String;Lcom/mitra/app/utils/CryptoUtils;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "logEvent", "deviceId", "sessionId", "type", "extra", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/util/Map;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "saveChat", "chat", "(Ljava/lang/String;Lcom/mitra/app/data/model/Chat;Lcom/mitra/app/utils/CryptoUtils;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "sendFeedback", "", "text", "app_debug"})
public final class ChatRepository {
    @org.jetbrains.annotations.NotNull()
    private final com.google.firebase.firestore.FirebaseFirestore db = null;
    
    @javax.inject.Inject()
    public ChatRepository(@org.jetbrains.annotations.NotNull()
    com.google.firebase.firestore.FirebaseFirestore db) {
        super();
    }
    
    private final com.google.firebase.firestore.CollectionReference chatsRef(java.lang.String uid) {
        return null;
    }
    
    /**
     * Load all chats for [uid]. Decrypts message contents if a [cryptoUtils]
     * session key is available, otherwise returns ciphertext as-is (graceful
     * degradation matching web app behaviour).
     */
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object loadChats(@org.jetbrains.annotations.NotNull()
    java.lang.String uid, @org.jetbrains.annotations.Nullable()
    com.mitra.app.utils.CryptoUtils cryptoUtils, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.Map<java.lang.String, com.mitra.app.data.model.Chat>> $completion) {
        return null;
    }
    
    /**
     * Persist [chat] to Firestore, encrypting every message with [cryptoUtils]
     * when available. Incognito chats must NEVER reach this method.
     */
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object saveChat(@org.jetbrains.annotations.NotNull()
    java.lang.String uid, @org.jetbrains.annotations.NotNull()
    com.mitra.app.data.model.Chat chat, @org.jetbrains.annotations.Nullable()
    com.mitra.app.utils.CryptoUtils cryptoUtils, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    /**
     * Hard-delete a single chat document.
     */
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object deleteChat(@org.jetbrains.annotations.NotNull()
    java.lang.String uid, @org.jetbrains.annotations.NotNull()
    java.lang.String chatId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    /**
     * Delete every chat for [uid] — used by "wipe all" action.
     */
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object deleteAllChats(@org.jetbrains.annotations.NotNull()
    java.lang.String uid, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    /**
     * Submit feedback to the top-level `feedback` collection.
     */
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object sendFeedback(@org.jetbrains.annotations.Nullable()
    java.lang.String uid, @org.jetbrains.annotations.NotNull()
    java.lang.String text, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Boolean> $completion) {
        return null;
    }
    
    /**
     * Log a lightweight analytics event (mirrors __mitraEvent in web app).
     */
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object logEvent(@org.jetbrains.annotations.Nullable()
    java.lang.String uid, @org.jetbrains.annotations.NotNull()
    java.lang.String deviceId, @org.jetbrains.annotations.NotNull()
    java.lang.String sessionId, @org.jetbrains.annotations.NotNull()
    java.lang.String type, @org.jetbrains.annotations.NotNull()
    java.util.Map<java.lang.String, ? extends java.lang.Object> extra, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
}