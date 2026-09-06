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

@kotlin.Metadata(mv = {1, 9, 0}, k = 2, xi = 48, d1 = {"\u0000\n\n\u0000\n\u0002\u0010\b\n\u0002\b\u0004\"\u000e\u0010\u0000\u001a\u00020\u0001X\u0082T\u00a2\u0006\u0002\n\u0000\"\u000e\u0010\u0002\u001a\u00020\u0001X\u0082T\u00a2\u0006\u0002\n\u0000\"\u000e\u0010\u0003\u001a\u00020\u0001X\u0082T\u00a2\u0006\u0002\n\u0000\"\u000e\u0010\u0004\u001a\u00020\u0001X\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0005"}, d2 = {"VT_MITRA", "", "VT_SUPPORT", "VT_TYPING", "VT_USER", "app_debug"})
public final class MessageAdapterKt {
    private static final int VT_MITRA = 0;
    private static final int VT_USER = 1;
    private static final int VT_TYPING = 2;
    private static final int VT_SUPPORT = 3;
}