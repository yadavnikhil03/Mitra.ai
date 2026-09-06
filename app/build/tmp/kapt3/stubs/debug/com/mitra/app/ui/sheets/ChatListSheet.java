package com.mitra.app.ui.sheets;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.mitra.app.R;
import com.mitra.app.data.model.Chat;
import com.mitra.app.databinding.BottomSheetChatListBinding;
import com.mitra.app.databinding.ItemChatRowBinding;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import androidx.core.content.ContextCompat;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000V\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\u0018\u00002\u00020\u0001:\u0001\"Bs\u0012\f\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003\u0012\b\u0010\u0005\u001a\u0004\u0018\u00010\u0006\u0012\u0012\u0010\u0007\u001a\u000e\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020\t0\b\u0012\u0012\u0010\n\u001a\u000e\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020\t0\b\u0012\f\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\t0\f\u0012\f\u0010\r\u001a\b\u0012\u0004\u0012\u00020\t0\f\u0012\u0010\b\u0002\u0010\u000e\u001a\n\u0012\u0004\u0012\u00020\t\u0018\u00010\f\u00a2\u0006\u0002\u0010\u000fJ\b\u0010\u0015\u001a\u00020\u0016H\u0016J$\u0010\u0017\u001a\u00020\u00182\u0006\u0010\u0019\u001a\u00020\u001a2\b\u0010\u001b\u001a\u0004\u0018\u00010\u001c2\b\u0010\u001d\u001a\u0004\u0018\u00010\u001eH\u0016J\b\u0010\u001f\u001a\u00020\tH\u0016J\u001a\u0010 \u001a\u00020\t2\u0006\u0010!\u001a\u00020\u00182\b\u0010\u001d\u001a\u0004\u0018\u00010\u001eH\u0016R\u0010\u0010\u0010\u001a\u0004\u0018\u00010\u0011X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0005\u001a\u0004\u0018\u00010\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0012\u001a\u00020\u00118BX\u0082\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0013\u0010\u0014R\u0014\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010\n\u001a\u000e\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020\t0\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0016\u0010\u000e\u001a\n\u0012\u0004\u0012\u00020\t\u0018\u00010\fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\r\u001a\b\u0012\u0004\u0012\u00020\t0\fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\t0\fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010\u0007\u001a\u000e\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020\t0\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006#"}, d2 = {"Lcom/mitra/app/ui/sheets/ChatListSheet;", "Lcom/google/android/material/bottomsheet/BottomSheetDialogFragment;", "chats", "", "Lcom/mitra/app/data/model/Chat;", "activeChatId", "", "onSelect", "Lkotlin/Function1;", "", "onDelete", "onNewChat", "Lkotlin/Function0;", "onIncognito", "onExitIncognito", "(Ljava/util/List;Ljava/lang/String;Lkotlin/jvm/functions/Function1;Lkotlin/jvm/functions/Function1;Lkotlin/jvm/functions/Function0;Lkotlin/jvm/functions/Function0;Lkotlin/jvm/functions/Function0;)V", "_binding", "Lcom/mitra/app/databinding/BottomSheetChatListBinding;", "binding", "getBinding", "()Lcom/mitra/app/databinding/BottomSheetChatListBinding;", "getTheme", "", "onCreateView", "Landroid/view/View;", "inf", "Landroid/view/LayoutInflater;", "parent", "Landroid/view/ViewGroup;", "state", "Landroid/os/Bundle;", "onDestroyView", "onViewCreated", "view", "ChatRowAdapter", "app_debug"})
public final class ChatListSheet extends com.google.android.material.bottomsheet.BottomSheetDialogFragment {
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<com.mitra.app.data.model.Chat> chats = null;
    @org.jetbrains.annotations.Nullable()
    private final java.lang.String activeChatId = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlin.jvm.functions.Function1<com.mitra.app.data.model.Chat, kotlin.Unit> onSelect = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlin.jvm.functions.Function1<com.mitra.app.data.model.Chat, kotlin.Unit> onDelete = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlin.jvm.functions.Function0<kotlin.Unit> onNewChat = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlin.jvm.functions.Function0<kotlin.Unit> onIncognito = null;
    @org.jetbrains.annotations.Nullable()
    private final kotlin.jvm.functions.Function0<kotlin.Unit> onExitIncognito = null;
    @org.jetbrains.annotations.Nullable()
    private com.mitra.app.databinding.BottomSheetChatListBinding _binding;
    
    public ChatListSheet(@org.jetbrains.annotations.NotNull()
    java.util.List<com.mitra.app.data.model.Chat> chats, @org.jetbrains.annotations.Nullable()
    java.lang.String activeChatId, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super com.mitra.app.data.model.Chat, kotlin.Unit> onSelect, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super com.mitra.app.data.model.Chat, kotlin.Unit> onDelete, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onNewChat, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onIncognito, @org.jetbrains.annotations.Nullable()
    kotlin.jvm.functions.Function0<kotlin.Unit> onExitIncognito) {
        super();
    }
    
    private final com.mitra.app.databinding.BottomSheetChatListBinding getBinding() {
        return null;
    }
    
    @java.lang.Override()
    public int getTheme() {
        return 0;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public android.view.View onCreateView(@org.jetbrains.annotations.NotNull()
    android.view.LayoutInflater inf, @org.jetbrains.annotations.Nullable()
    android.view.ViewGroup parent, @org.jetbrains.annotations.Nullable()
    android.os.Bundle state) {
        return null;
    }
    
    @java.lang.Override()
    public void onViewCreated(@org.jetbrains.annotations.NotNull()
    android.view.View view, @org.jetbrains.annotations.Nullable()
    android.os.Bundle state) {
    }
    
    @java.lang.Override()
    public void onDestroyView() {
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000>\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010!\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0004\b\u0082\u0004\u0018\u00002\u0010\u0012\f\u0012\n0\u0002R\u00060\u0000R\u00020\u00030\u0001:\u0001\u0019BE\u0012\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005\u0012\b\u0010\u0007\u001a\u0004\u0018\u00010\b\u0012\u0012\u0010\t\u001a\u000e\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\u000b0\n\u0012\u0012\u0010\f\u001a\u000e\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\u000b0\n\u00a2\u0006\u0002\u0010\rJ\u000e\u0010\u000e\u001a\u00020\u00062\u0006\u0010\u000f\u001a\u00020\u0010J\b\u0010\u0011\u001a\u00020\u0010H\u0016J \u0010\u0012\u001a\u00020\u000b2\u000e\u0010\u0013\u001a\n0\u0002R\u00060\u0000R\u00020\u00032\u0006\u0010\u000f\u001a\u00020\u0010H\u0016J \u0010\u0014\u001a\n0\u0002R\u00060\u0000R\u00020\u00032\u0006\u0010\u0015\u001a\u00020\u00162\u0006\u0010\u0017\u001a\u00020\u0010H\u0016J\u000e\u0010\u0018\u001a\u00020\u000b2\u0006\u0010\u000f\u001a\u00020\u0010R\u0010\u0010\u0007\u001a\u0004\u0018\u00010\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010\f\u001a\u000e\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\u000b0\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010\t\u001a\u000e\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\u000b0\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u001a"}, d2 = {"Lcom/mitra/app/ui/sheets/ChatListSheet$ChatRowAdapter;", "Landroidx/recyclerview/widget/RecyclerView$Adapter;", "Lcom/mitra/app/ui/sheets/ChatListSheet$ChatRowAdapter$VH;", "Lcom/mitra/app/ui/sheets/ChatListSheet;", "chats", "", "Lcom/mitra/app/data/model/Chat;", "activeChatId", "", "onSelect", "Lkotlin/Function1;", "", "onDelete", "(Lcom/mitra/app/ui/sheets/ChatListSheet;Ljava/util/List;Ljava/lang/String;Lkotlin/jvm/functions/Function1;Lkotlin/jvm/functions/Function1;)V", "getItemAt", "pos", "", "getItemCount", "onBindViewHolder", "vh", "onCreateViewHolder", "parent", "Landroid/view/ViewGroup;", "vt", "removeAt", "VH", "app_debug"})
    final class ChatRowAdapter extends androidx.recyclerview.widget.RecyclerView.Adapter<com.mitra.app.ui.sheets.ChatListSheet.ChatRowAdapter.VH> {
        @org.jetbrains.annotations.NotNull()
        private final java.util.List<com.mitra.app.data.model.Chat> chats = null;
        @org.jetbrains.annotations.Nullable()
        private final java.lang.String activeChatId = null;
        @org.jetbrains.annotations.NotNull()
        private final kotlin.jvm.functions.Function1<com.mitra.app.data.model.Chat, kotlin.Unit> onSelect = null;
        @org.jetbrains.annotations.NotNull()
        private final kotlin.jvm.functions.Function1<com.mitra.app.data.model.Chat, kotlin.Unit> onDelete = null;
        
        public ChatRowAdapter(@org.jetbrains.annotations.NotNull()
        java.util.List<com.mitra.app.data.model.Chat> chats, @org.jetbrains.annotations.Nullable()
        java.lang.String activeChatId, @org.jetbrains.annotations.NotNull()
        kotlin.jvm.functions.Function1<? super com.mitra.app.data.model.Chat, kotlin.Unit> onSelect, @org.jetbrains.annotations.NotNull()
        kotlin.jvm.functions.Function1<? super com.mitra.app.data.model.Chat, kotlin.Unit> onDelete) {
            super();
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.mitra.app.data.model.Chat getItemAt(int pos) {
            return null;
        }
        
        public final void removeAt(int pos) {
        }
        
        @java.lang.Override()
        @org.jetbrains.annotations.NotNull()
        public com.mitra.app.ui.sheets.ChatListSheet.ChatRowAdapter.VH onCreateViewHolder(@org.jetbrains.annotations.NotNull()
        android.view.ViewGroup parent, int vt) {
            return null;
        }
        
        @java.lang.Override()
        public int getItemCount() {
            return 0;
        }
        
        @java.lang.Override()
        public void onBindViewHolder(@org.jetbrains.annotations.NotNull()
        com.mitra.app.ui.sheets.ChatListSheet.ChatRowAdapter.VH vh, int pos) {
        }
        
        @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\b\u0086\u0004\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006\u00a8\u0006\u0007"}, d2 = {"Lcom/mitra/app/ui/sheets/ChatListSheet$ChatRowAdapter$VH;", "Landroidx/recyclerview/widget/RecyclerView$ViewHolder;", "b", "Lcom/mitra/app/databinding/ItemChatRowBinding;", "(Lcom/mitra/app/ui/sheets/ChatListSheet$ChatRowAdapter;Lcom/mitra/app/databinding/ItemChatRowBinding;)V", "getB", "()Lcom/mitra/app/databinding/ItemChatRowBinding;", "app_debug"})
        public final class VH extends androidx.recyclerview.widget.RecyclerView.ViewHolder {
            @org.jetbrains.annotations.NotNull()
            private final com.mitra.app.databinding.ItemChatRowBinding b = null;
            
            public VH(@org.jetbrains.annotations.NotNull()
            com.mitra.app.databinding.ItemChatRowBinding b) {
                super(null);
            }
            
            @org.jetbrains.annotations.NotNull()
            public final com.mitra.app.databinding.ItemChatRowBinding getB() {
                return null;
            }
        }
    }
}