package com.mitra.app.ui.chat;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.Lifecycle;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.auth.FirebaseAuth;
import com.mitra.app.R;
import com.mitra.app.data.model.Chat;
import com.mitra.app.data.model.MessageItem;
import com.mitra.app.data.model.Role;
import com.mitra.app.databinding.ActivityChatBinding;
import com.mitra.app.databinding.ItemChatRowBinding;
import com.mitra.app.ui.login.LoginActivity;
import com.mitra.app.ui.sheets.ConfirmDeleteSheet;
import com.mitra.app.ui.sheets.FeedbackSheet;
import com.mitra.app.ui.sheets.InfoSheet;
import dagger.hilt.android.AndroidEntryPoint;
import java.util.Locale;
import javax.inject.Inject;

@dagger.hilt.android.AndroidEntryPoint()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0017\b\u0007\u0018\u0000 C2\u00020\u0001:\u0002CDB\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010%\u001a\u00020&H\u0002J\b\u0010\'\u001a\u00020&H\u0002J\b\u0010(\u001a\u00020&H\u0002J\b\u0010)\u001a\u00020&H\u0002J\b\u0010*\u001a\u00020&H\u0002J\b\u0010+\u001a\u00020&H\u0017J\u0012\u0010,\u001a\u00020&2\b\u0010-\u001a\u0004\u0018\u00010.H\u0014J\b\u0010/\u001a\u00020&H\u0014J\b\u00100\u001a\u00020&H\u0002J\b\u00101\u001a\u00020&H\u0014J\b\u00102\u001a\u00020&H\u0014J\b\u00103\u001a\u00020&H\u0002J\u0010\u00104\u001a\u00020&2\u0006\u00105\u001a\u00020\u0014H\u0002J\u0010\u00106\u001a\u00020&2\u0006\u00107\u001a\u00020\u0014H\u0002J\b\u00108\u001a\u00020&H\u0002J\b\u00109\u001a\u00020&H\u0002J\b\u0010:\u001a\u00020&H\u0002J\b\u0010;\u001a\u00020&H\u0002J\b\u0010<\u001a\u00020&H\u0002J\b\u0010=\u001a\u00020&H\u0002J\b\u0010>\u001a\u00020&H\u0002J\u0010\u0010?\u001a\u00020&2\u0006\u0010@\u001a\u00020\fH\u0002J\b\u0010A\u001a\u00020&H\u0002J\b\u0010B\u001a\u00020&H\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082.\u00a2\u0006\u0002\n\u0000R\u001e\u0010\u0005\u001a\u00020\u00068\u0006@\u0006X\u0087.\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0007\u0010\b\"\u0004\b\t\u0010\nR\u000e\u0010\u000b\u001a\u00020\fX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u000eX\u0082.\u00a2\u0006\u0002\n\u0000R\u0012\u0010\u000f\u001a\u00060\u0010R\u00020\u0000X\u0082.\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0011\u001a\u0004\u0018\u00010\u0012X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0013\u001a\u00020\u0014X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0015\u001a\u00020\u0014X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0016\u001a\u00020\u0014X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0017\u001a\u0004\u0018\u00010\fX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0018\u001a\b\u0012\u0004\u0012\u00020\f0\u0019X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u001a\u001a\b\u0012\u0004\u0012\u00020\f0\u0019X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u001b\u001a\u0004\u0018\u00010\u001cX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u001d\u001a\u0004\u0018\u00010\u001eX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001b\u0010\u001f\u001a\u00020 8BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\b#\u0010$\u001a\u0004\b!\u0010\"\u00a8\u0006E"}, d2 = {"Lcom/mitra/app/ui/chat/ChatActivity;", "Landroidx/appcompat/app/AppCompatActivity;", "()V", "adapter", "Lcom/mitra/app/ui/chat/MessageAdapter;", "auth", "Lcom/google/firebase/auth/FirebaseAuth;", "getAuth", "()Lcom/google/firebase/auth/FirebaseAuth;", "setAuth", "(Lcom/google/firebase/auth/FirebaseAuth;)V", "baseText", "", "binding", "Lcom/mitra/app/databinding/ActivityChatBinding;", "drawerAdapter", "Lcom/mitra/app/ui/chat/ChatActivity$DrawerChatAdapter;", "glowBreathAnimator", "Landroid/animation/ValueAnimator;", "isRecording", "", "isThinking", "isTtsEnabled", "lastSpokenMessageId", "micPermissionLauncher", "Landroidx/activity/result/ActivityResultLauncher;", "notificationPermissionLauncher", "speechRecognizer", "Landroid/speech/SpeechRecognizer;", "tts", "Landroid/speech/tts/TextToSpeech;", "vm", "Lcom/mitra/app/ui/chat/ChatViewModel;", "getVm", "()Lcom/mitra/app/ui/chat/ChatViewModel;", "vm$delegate", "Lkotlin/Lazy;", "applyWindowInsets", "", "goToLogin", "initTts", "observeEvents", "observeState", "onBackPressed", "onCreate", "savedInstanceState", "Landroid/os/Bundle;", "onDestroy", "onMicClick", "onPause", "onResume", "requestNotificationPermissionAndSchedule", "setGlowThinking", "thinking", "setMicRecording", "recording", "setupComposer", "setupDrawerRecycler", "setupHeaderButtons", "setupRecycler", "setupSpeechRecognizer", "showFeedbackSheet", "showInfoSheet", "speak", "text", "startGlowBreath", "startVoice", "Companion", "DrawerChatAdapter", "app_debug"})
public final class ChatActivity extends androidx.appcompat.app.AppCompatActivity {
    private com.mitra.app.databinding.ActivityChatBinding binding;
    @org.jetbrains.annotations.NotNull()
    private final kotlin.Lazy vm$delegate = null;
    private com.mitra.app.ui.chat.MessageAdapter adapter;
    private com.mitra.app.ui.chat.ChatActivity.DrawerChatAdapter drawerAdapter;
    @javax.inject.Inject()
    public com.google.firebase.auth.FirebaseAuth auth;
    @org.jetbrains.annotations.Nullable()
    private android.speech.SpeechRecognizer speechRecognizer;
    private boolean isRecording = false;
    @org.jetbrains.annotations.NotNull()
    private java.lang.String baseText = "";
    @org.jetbrains.annotations.Nullable()
    private android.speech.tts.TextToSpeech tts;
    private boolean isTtsEnabled = false;
    @org.jetbrains.annotations.Nullable()
    private java.lang.String lastSpokenMessageId;
    @org.jetbrains.annotations.NotNull()
    private final androidx.activity.result.ActivityResultLauncher<java.lang.String> micPermissionLauncher = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.activity.result.ActivityResultLauncher<java.lang.String> notificationPermissionLauncher = null;
    @org.jetbrains.annotations.Nullable()
    private android.animation.ValueAnimator glowBreathAnimator;
    private boolean isThinking = false;
    @org.jetbrains.annotations.NotNull()
    private static final androidx.recyclerview.widget.DiffUtil.ItemCallback<com.mitra.app.data.model.Chat> DIFF = null;
    @org.jetbrains.annotations.NotNull()
    private static final com.mitra.app.ui.chat.ChatActivity.Companion Companion = null;
    
    public ChatActivity() {
        super();
    }
    
    private final com.mitra.app.ui.chat.ChatViewModel getVm() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.google.firebase.auth.FirebaseAuth getAuth() {
        return null;
    }
    
    public final void setAuth(@org.jetbrains.annotations.NotNull()
    com.google.firebase.auth.FirebaseAuth p0) {
    }
    
    @java.lang.Override()
    protected void onCreate(@org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
    }
    
    private final void initTts() {
    }
    
    private final void speak(java.lang.String text) {
    }
    
    private final void applyWindowInsets() {
    }
    
    private final void setupRecycler() {
    }
    
    private final void setupDrawerRecycler() {
    }
    
    private final void setupComposer() {
    }
    
    private final void setupHeaderButtons() {
    }
    
    private final void observeState() {
    }
    
    private final void observeEvents() {
    }
    
    private final void startGlowBreath() {
    }
    
    private final void setGlowThinking(boolean thinking) {
    }
    
    private final void showInfoSheet() {
    }
    
    private final void showFeedbackSheet() {
    }
    
    private final void setupSpeechRecognizer() {
    }
    
    private final void onMicClick() {
    }
    
    private final void startVoice() {
    }
    
    private final void setMicRecording(boolean recording) {
    }
    
    @java.lang.Override()
    @java.lang.Deprecated()
    public void onBackPressed() {
    }
    
    @java.lang.Override()
    protected void onResume() {
    }
    
    @java.lang.Override()
    protected void onPause() {
    }
    
    @java.lang.Override()
    protected void onDestroy() {
    }
    
    private final void goToLogin() {
    }
    
    private final void requestNotificationPermissionAndSchedule() {
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u0082\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u0017\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0006\u0010\u0007\u00a8\u0006\b"}, d2 = {"Lcom/mitra/app/ui/chat/ChatActivity$Companion;", "", "()V", "DIFF", "Landroidx/recyclerview/widget/DiffUtil$ItemCallback;", "Lcom/mitra/app/data/model/Chat;", "getDIFF", "()Landroidx/recyclerview/widget/DiffUtil$ItemCallback;", "app_debug"})
    static final class Companion {
        
        private Companion() {
            super();
        }
        
        @org.jetbrains.annotations.NotNull()
        public final androidx.recyclerview.widget.DiffUtil.ItemCallback<com.mitra.app.data.model.Chat> getDIFF() {
            return null;
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000:\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\b\u0082\u0004\u0018\u00002\u0016\u0012\u0004\u0012\u00020\u0002\u0012\f\u0012\n0\u0003R\u00060\u0000R\u00020\u00040\u0001:\u0001\u0016B-\u0012\u0012\u0010\u0005\u001a\u000e\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u00070\u0006\u0012\u0012\u0010\b\u001a\u000e\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u00070\u0006\u00a2\u0006\u0002\u0010\tJ \u0010\f\u001a\u00020\u00072\u000e\u0010\r\u001a\n0\u0003R\u00060\u0000R\u00020\u00042\u0006\u0010\u000e\u001a\u00020\u000fH\u0016J \u0010\u0010\u001a\n0\u0003R\u00060\u0000R\u00020\u00042\u0006\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\u000fH\u0016J\u0010\u0010\u0014\u001a\u00020\u00072\b\u0010\u0015\u001a\u0004\u0018\u00010\u000bR\u0010\u0010\n\u001a\u0004\u0018\u00010\u000bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001a\u0010\b\u001a\u000e\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u00070\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010\u0005\u001a\u000e\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u00070\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0017"}, d2 = {"Lcom/mitra/app/ui/chat/ChatActivity$DrawerChatAdapter;", "Landroidx/recyclerview/widget/ListAdapter;", "Lcom/mitra/app/data/model/Chat;", "Lcom/mitra/app/ui/chat/ChatActivity$DrawerChatAdapter$VH;", "Lcom/mitra/app/ui/chat/ChatActivity;", "onSelect", "Lkotlin/Function1;", "", "onDelete", "(Lcom/mitra/app/ui/chat/ChatActivity;Lkotlin/jvm/functions/Function1;Lkotlin/jvm/functions/Function1;)V", "activeId", "", "onBindViewHolder", "vh", "pos", "", "onCreateViewHolder", "parent", "Landroid/view/ViewGroup;", "vt", "setActiveId", "id", "VH", "app_debug"})
    final class DrawerChatAdapter extends androidx.recyclerview.widget.ListAdapter<com.mitra.app.data.model.Chat, com.mitra.app.ui.chat.ChatActivity.DrawerChatAdapter.VH> {
        @org.jetbrains.annotations.NotNull()
        private final kotlin.jvm.functions.Function1<com.mitra.app.data.model.Chat, kotlin.Unit> onSelect = null;
        @org.jetbrains.annotations.NotNull()
        private final kotlin.jvm.functions.Function1<com.mitra.app.data.model.Chat, kotlin.Unit> onDelete = null;
        @org.jetbrains.annotations.Nullable()
        private java.lang.String activeId;
        
        public DrawerChatAdapter(@org.jetbrains.annotations.NotNull()
        kotlin.jvm.functions.Function1<? super com.mitra.app.data.model.Chat, kotlin.Unit> onSelect, @org.jetbrains.annotations.NotNull()
        kotlin.jvm.functions.Function1<? super com.mitra.app.data.model.Chat, kotlin.Unit> onDelete) {
            super(null);
        }
        
        public final void setActiveId(@org.jetbrains.annotations.Nullable()
        java.lang.String id) {
        }
        
        @java.lang.Override()
        @org.jetbrains.annotations.NotNull()
        public com.mitra.app.ui.chat.ChatActivity.DrawerChatAdapter.VH onCreateViewHolder(@org.jetbrains.annotations.NotNull()
        android.view.ViewGroup parent, int vt) {
            return null;
        }
        
        @java.lang.Override()
        public void onBindViewHolder(@org.jetbrains.annotations.NotNull()
        com.mitra.app.ui.chat.ChatActivity.DrawerChatAdapter.VH vh, int pos) {
        }
        
        @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\b\u0086\u0004\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006\u00a8\u0006\u0007"}, d2 = {"Lcom/mitra/app/ui/chat/ChatActivity$DrawerChatAdapter$VH;", "Landroidx/recyclerview/widget/RecyclerView$ViewHolder;", "b", "Lcom/mitra/app/databinding/ItemChatRowBinding;", "(Lcom/mitra/app/ui/chat/ChatActivity$DrawerChatAdapter;Lcom/mitra/app/databinding/ItemChatRowBinding;)V", "getB", "()Lcom/mitra/app/databinding/ItemChatRowBinding;", "app_debug"})
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