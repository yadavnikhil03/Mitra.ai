package com.mitra.app.ui.chat;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.Toast;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import com.mitra.app.data.model.ChatMessage;
import com.mitra.app.data.model.MessageItem;
import com.mitra.app.data.model.Role;
import com.mitra.app.databinding.ItemMessageMitraBinding;
import com.mitra.app.databinding.ItemMessageUserBinding;
import com.mitra.app.databinding.ItemSupportCardBinding;
import com.mitra.app.databinding.ItemTypingIndicatorBinding;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000B\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0006\u0018\u0000 \u001b2\u000e\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u00030\u0001:\u0005\u001b\u001c\u001d\u001e\u001fB\u001d\u0012\u0016\b\u0002\u0010\u0004\u001a\u0010\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\u0007\u0018\u00010\u0005\u00a2\u0006\u0002\u0010\bJ\u0018\u0010\t\u001a\u00020\u00072\u0006\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\u0006H\u0002J\u0010\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u000eH\u0016J\u0018\u0010\u0010\u001a\u00020\u00072\u0006\u0010\u0011\u001a\u00020\u00032\u0006\u0010\u0012\u001a\u00020\u000eH\u0016J\u0018\u0010\u0013\u001a\u00020\u00032\u0006\u0010\u0014\u001a\u00020\u00152\u0006\u0010\u0016\u001a\u00020\u000eH\u0016J\u0010\u0010\u0017\u001a\u00020\u00072\u0006\u0010\u0011\u001a\u00020\u0003H\u0016J\u0010\u0010\u0018\u001a\u00020\u00072\u0006\u0010\u0019\u001a\u00020\u001aH\u0002R\u001c\u0010\u0004\u001a\u0010\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\u0007\u0018\u00010\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006 "}, d2 = {"Lcom/mitra/app/ui/chat/MessageAdapter;", "Landroidx/recyclerview/widget/ListAdapter;", "Lcom/mitra/app/data/model/MessageItem;", "Landroidx/recyclerview/widget/RecyclerView$ViewHolder;", "onSpeak", "Lkotlin/Function1;", "", "", "(Lkotlin/jvm/functions/Function1;)V", "copyToClipboard", "context", "Landroid/content/Context;", "text", "getItemViewType", "", "pos", "onBindViewHolder", "holder", "position", "onCreateViewHolder", "parent", "Landroid/view/ViewGroup;", "viewType", "onViewRecycled", "popIn", "view", "Landroid/view/View;", "Companion", "MitraVH", "SupportVH", "TypingVH", "UserVH", "app_debug"})
public final class MessageAdapter extends androidx.recyclerview.widget.ListAdapter<com.mitra.app.data.model.MessageItem, androidx.recyclerview.widget.RecyclerView.ViewHolder> {
    @org.jetbrains.annotations.Nullable()
    private final kotlin.jvm.functions.Function1<java.lang.String, kotlin.Unit> onSpeak = null;
    @org.jetbrains.annotations.NotNull()
    private static final androidx.recyclerview.widget.DiffUtil.ItemCallback<com.mitra.app.data.model.MessageItem> DIFF = null;
    @org.jetbrains.annotations.NotNull()
    public static final com.mitra.app.ui.chat.MessageAdapter.Companion Companion = null;
    
    public MessageAdapter(@org.jetbrains.annotations.Nullable()
    kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> onSpeak) {
        super(null);
    }
    
    @java.lang.Override()
    public int getItemViewType(int pos) {
        return 0;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public androidx.recyclerview.widget.RecyclerView.ViewHolder onCreateViewHolder(@org.jetbrains.annotations.NotNull()
    android.view.ViewGroup parent, int viewType) {
        return null;
    }
    
    @java.lang.Override()
    public void onBindViewHolder(@org.jetbrains.annotations.NotNull()
    androidx.recyclerview.widget.RecyclerView.ViewHolder holder, int position) {
    }
    
    @java.lang.Override()
    public void onViewRecycled(@org.jetbrains.annotations.NotNull()
    androidx.recyclerview.widget.RecyclerView.ViewHolder holder) {
    }
    
    private final void copyToClipboard(android.content.Context context, java.lang.String text) {
    }
    
    private final void popIn(android.view.View view) {
    }
    
    public MessageAdapter() {
        super(null);
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0016\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u0014\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0006"}, d2 = {"Lcom/mitra/app/ui/chat/MessageAdapter$Companion;", "", "()V", "DIFF", "Landroidx/recyclerview/widget/DiffUtil$ItemCallback;", "Lcom/mitra/app/data/model/MessageItem;", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0086\u0004\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u000e\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\t"}, d2 = {"Lcom/mitra/app/ui/chat/MessageAdapter$MitraVH;", "Landroidx/recyclerview/widget/RecyclerView$ViewHolder;", "b", "Lcom/mitra/app/databinding/ItemMessageMitraBinding;", "(Lcom/mitra/app/ui/chat/MessageAdapter;Lcom/mitra/app/databinding/ItemMessageMitraBinding;)V", "bind", "", "msg", "Lcom/mitra/app/data/model/ChatMessage;", "app_debug"})
    public final class MitraVH extends androidx.recyclerview.widget.RecyclerView.ViewHolder {
        @org.jetbrains.annotations.NotNull()
        private final com.mitra.app.databinding.ItemMessageMitraBinding b = null;
        
        public MitraVH(@org.jetbrains.annotations.NotNull()
        com.mitra.app.databinding.ItemMessageMitraBinding b) {
            super(null);
        }
        
        public final void bind(@org.jetbrains.annotations.NotNull()
        com.mitra.app.data.model.ChatMessage msg) {
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0086\u0004\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u000e\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\t"}, d2 = {"Lcom/mitra/app/ui/chat/MessageAdapter$SupportVH;", "Landroidx/recyclerview/widget/RecyclerView$ViewHolder;", "b", "Lcom/mitra/app/databinding/ItemSupportCardBinding;", "(Lcom/mitra/app/ui/chat/MessageAdapter;Lcom/mitra/app/databinding/ItemSupportCardBinding;)V", "bind", "", "text", "", "app_debug"})
    public final class SupportVH extends androidx.recyclerview.widget.RecyclerView.ViewHolder {
        @org.jetbrains.annotations.NotNull()
        private final com.mitra.app.databinding.ItemSupportCardBinding b = null;
        
        public SupportVH(@org.jetbrains.annotations.NotNull()
        com.mitra.app.databinding.ItemSupportCardBinding b) {
            super(null);
        }
        
        public final void bind(@org.jetbrains.annotations.NotNull()
        java.lang.String text) {
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010!\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\b\u0086\u0004\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0006\u0010\u000b\u001a\u00020\fJ\u0006\u0010\r\u001a\u00020\fR\u0014\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\b\u001a\b\u0012\u0004\u0012\u00020\n0\tX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u000e"}, d2 = {"Lcom/mitra/app/ui/chat/MessageAdapter$TypingVH;", "Landroidx/recyclerview/widget/RecyclerView$ViewHolder;", "b", "Lcom/mitra/app/databinding/ItemTypingIndicatorBinding;", "(Lcom/mitra/app/ui/chat/MessageAdapter;Lcom/mitra/app/databinding/ItemTypingIndicatorBinding;)V", "animators", "", "Landroid/animation/Animator;", "dots", "", "Landroid/view/View;", "startAnim", "", "stopAnim", "app_debug"})
    public final class TypingVH extends androidx.recyclerview.widget.RecyclerView.ViewHolder {
        @org.jetbrains.annotations.NotNull()
        private final com.mitra.app.databinding.ItemTypingIndicatorBinding b = null;
        @org.jetbrains.annotations.NotNull()
        private final java.util.List<android.view.View> dots = null;
        @org.jetbrains.annotations.NotNull()
        private final java.util.List<android.animation.Animator> animators = null;
        
        public TypingVH(@org.jetbrains.annotations.NotNull()
        com.mitra.app.databinding.ItemTypingIndicatorBinding b) {
            super(null);
        }
        
        public final void startAnim() {
        }
        
        public final void stopAnim() {
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0086\u0004\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u000e\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\t"}, d2 = {"Lcom/mitra/app/ui/chat/MessageAdapter$UserVH;", "Landroidx/recyclerview/widget/RecyclerView$ViewHolder;", "b", "Lcom/mitra/app/databinding/ItemMessageUserBinding;", "(Lcom/mitra/app/ui/chat/MessageAdapter;Lcom/mitra/app/databinding/ItemMessageUserBinding;)V", "bind", "", "msg", "Lcom/mitra/app/data/model/ChatMessage;", "app_debug"})
    public final class UserVH extends androidx.recyclerview.widget.RecyclerView.ViewHolder {
        @org.jetbrains.annotations.NotNull()
        private final com.mitra.app.databinding.ItemMessageUserBinding b = null;
        
        public UserVH(@org.jetbrains.annotations.NotNull()
        com.mitra.app.databinding.ItemMessageUserBinding b) {
            super(null);
        }
        
        public final void bind(@org.jetbrains.annotations.NotNull()
        com.mitra.app.data.model.ChatMessage msg) {
        }
    }
}