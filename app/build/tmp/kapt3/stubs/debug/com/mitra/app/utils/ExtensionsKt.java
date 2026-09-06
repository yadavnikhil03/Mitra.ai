package com.mitra.app.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@kotlin.Metadata(mv = {1, 9, 0}, k = 2, xi = 48, d1 = {"\u0000:\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\u001a\u0012\u0010\u0000\u001a\u00020\u0001*\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u0004\u001a\n\u0010\u0005\u001a\u00020\u0001*\u00020\u0002\u001a\n\u0010\u0006\u001a\u00020\u0001*\u00020\u0002\u001a\u0012\u0010\u0007\u001a\u00020\u0001*\u00020\b2\u0006\u0010\t\u001a\u00020\n\u001a\u0014\u0010\u000b\u001a\u00020\u0001*\u00020\u00022\b\b\u0002\u0010\f\u001a\u00020\r\u001a\u001e\u0010\u000e\u001a\u00020\u0001*\u00020\u00022\b\b\u0002\u0010\f\u001a\u00020\r2\b\b\u0002\u0010\u000f\u001a\u00020\u0010\u001a\n\u0010\u0011\u001a\u00020\u0010*\u00020\u0004\u001a\n\u0010\u0012\u001a\u00020\u0013*\u00020\r\u001a\n\u0010\u0014\u001a\u00020\u0013*\u00020\r\u00a8\u0006\u0015"}, d2 = {"animateBubbleIn", "", "Landroid/view/View;", "context", "Landroid/content/Context;", "applyNavBarPadding", "applyStatusBarPadding", "enableAutoResize", "Landroid/widget/EditText;", "maxHeightPx", "", "fadeIn", "duration", "", "fadeOut", "gone", "", "isOnline", "toFormattedTime", "", "toRelativeDate", "app_debug"})
public final class ExtensionsKt {
    
    /**
     * Animate a view in with the bubble-pop anim.
     */
    public static final void animateBubbleIn(@org.jetbrains.annotations.NotNull()
    android.view.View $this$animateBubbleIn, @org.jetbrains.annotations.NotNull()
    android.content.Context context) {
    }
    
    /**
     * Show/hide with fade.
     */
    public static final void fadeIn(@org.jetbrains.annotations.NotNull()
    android.view.View $this$fadeIn, long duration) {
    }
    
    public static final void fadeOut(@org.jetbrains.annotations.NotNull()
    android.view.View $this$fadeOut, long duration, boolean gone) {
    }
    
    /**
     * Apply window-inset padding so views clear system bars.
     */
    public static final void applyStatusBarPadding(@org.jetbrains.annotations.NotNull()
    android.view.View $this$applyStatusBarPadding) {
    }
    
    public static final void applyNavBarPadding(@org.jetbrains.annotations.NotNull()
    android.view.View $this$applyNavBarPadding) {
    }
    
    /**
     * Network connectivity check.
     */
    public static final boolean isOnline(@org.jetbrains.annotations.NotNull()
    android.content.Context $this$isOnline) {
        return false;
    }
    
    /**
     * Formatted time string (e.g. 10:42 AM).
     */
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String toFormattedTime(long $this$toFormattedTime) {
        return null;
    }
    
    /**
     * Relative date string for chat rows — mirrors web app's relDate().
     */
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String toRelativeDate(long $this$toRelativeDate) {
        return null;
    }
    
    /**
     * Auto-expand an EditText as the user types, up to [maxLines].
     */
    public static final void enableAutoResize(@org.jetbrains.annotations.NotNull()
    android.widget.EditText $this$enableAutoResize, int maxHeightPx) {
    }
}