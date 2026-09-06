package com.mitra.app.data.model;

import java.util.UUID;

/**
 * Unified sealed class for the adapter
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\b6\u0018\u00002\u00020\u0001:\u0003\u0003\u0004\u0005B\u0007\b\u0004\u00a2\u0006\u0002\u0010\u0002\u0082\u0001\u0003\u0006\u0007\b\u00a8\u0006\t"}, d2 = {"Lcom/mitra/app/data/model/MessageItem;", "", "()V", "Regular", "Support", "Typing", "Lcom/mitra/app/data/model/MessageItem$Regular;", "Lcom/mitra/app/data/model/MessageItem$Support;", "Lcom/mitra/app/data/model/MessageItem$Typing;", "app_debug"})
public abstract class MessageItem {
    
    private MessageItem() {
        super();
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0086\b\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\t\u0010\u0007\u001a\u00020\u0003H\u00c6\u0003J\u0013\u0010\b\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u0003H\u00c6\u0001J\u0013\u0010\t\u001a\u00020\n2\b\u0010\u000b\u001a\u0004\u0018\u00010\fH\u00d6\u0003J\t\u0010\r\u001a\u00020\u000eH\u00d6\u0001J\t\u0010\u000f\u001a\u00020\u0010H\u00d6\u0001R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006\u00a8\u0006\u0011"}, d2 = {"Lcom/mitra/app/data/model/MessageItem$Regular;", "Lcom/mitra/app/data/model/MessageItem;", "msg", "Lcom/mitra/app/data/model/ChatMessage;", "(Lcom/mitra/app/data/model/ChatMessage;)V", "getMsg", "()Lcom/mitra/app/data/model/ChatMessage;", "component1", "copy", "equals", "", "other", "", "hashCode", "", "toString", "", "app_debug"})
    public static final class Regular extends com.mitra.app.data.model.MessageItem {
        @org.jetbrains.annotations.NotNull()
        private final com.mitra.app.data.model.ChatMessage msg = null;
        
        public Regular(@org.jetbrains.annotations.NotNull()
        com.mitra.app.data.model.ChatMessage msg) {
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.mitra.app.data.model.ChatMessage getMsg() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.mitra.app.data.model.ChatMessage component1() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.mitra.app.data.model.MessageItem.Regular copy(@org.jetbrains.annotations.NotNull()
        com.mitra.app.data.model.ChatMessage msg) {
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
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0086\b\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\t\u0010\u0007\u001a\u00020\u0003H\u00c6\u0003J\u0013\u0010\b\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u0003H\u00c6\u0001J\u0013\u0010\t\u001a\u00020\n2\b\u0010\u000b\u001a\u0004\u0018\u00010\fH\u00d6\u0003J\t\u0010\r\u001a\u00020\u000eH\u00d6\u0001J\t\u0010\u000f\u001a\u00020\u0010H\u00d6\u0001R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006\u00a8\u0006\u0011"}, d2 = {"Lcom/mitra/app/data/model/MessageItem$Support;", "Lcom/mitra/app/data/model/MessageItem;", "card", "Lcom/mitra/app/data/model/SupportCard;", "(Lcom/mitra/app/data/model/SupportCard;)V", "getCard", "()Lcom/mitra/app/data/model/SupportCard;", "component1", "copy", "equals", "", "other", "", "hashCode", "", "toString", "", "app_debug"})
    public static final class Support extends com.mitra.app.data.model.MessageItem {
        @org.jetbrains.annotations.NotNull()
        private final com.mitra.app.data.model.SupportCard card = null;
        
        public Support(@org.jetbrains.annotations.NotNull()
        com.mitra.app.data.model.SupportCard card) {
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.mitra.app.data.model.SupportCard getCard() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.mitra.app.data.model.SupportCard component1() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.mitra.app.data.model.MessageItem.Support copy(@org.jetbrains.annotations.NotNull()
        com.mitra.app.data.model.SupportCard card) {
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
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002\u00a8\u0006\u0003"}, d2 = {"Lcom/mitra/app/data/model/MessageItem$Typing;", "Lcom/mitra/app/data/model/MessageItem;", "()V", "app_debug"})
    public static final class Typing extends com.mitra.app.data.model.MessageItem {
        @org.jetbrains.annotations.NotNull()
        public static final com.mitra.app.data.model.MessageItem.Typing INSTANCE = null;
        
        private Typing() {
        }
    }
}