package com.mitra.app.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.EditText
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/** Animate a view in with the bubble-pop anim. */
fun View.animateBubbleIn(context: Context) {
    startAnimation(AnimationUtils.loadAnimation(context, com.mitra.app.R.anim.bubble_in))
}

/** Show/hide with fade. */
fun View.fadeIn(duration: Long = 250) {
    alpha = 0f
    visibility = View.VISIBLE
    animate().alpha(1f).setDuration(duration).start()
}

fun View.fadeOut(duration: Long = 200, gone: Boolean = true) {
    animate().alpha(0f).setDuration(duration).withEndAction {
        visibility = if (gone) View.GONE else View.INVISIBLE
    }.start()
}

/** Apply window-inset padding so views clear system bars. */
fun View.applyStatusBarPadding() {
    ViewCompat.setOnApplyWindowInsetsListener(this) { v, insets ->
        val bars = insets.getInsets(WindowInsetsCompat.Type.statusBars())
        v.setPadding(v.paddingLeft, bars.top, v.paddingRight, v.paddingBottom)
        insets
    }
}

fun View.applyNavBarPadding() {
    ViewCompat.setOnApplyWindowInsetsListener(this) { v, insets ->
        val bars = insets.getInsets(WindowInsetsCompat.Type.navigationBars())
        v.setPadding(v.paddingLeft, v.paddingTop, v.paddingRight, bars.bottom)
        insets
    }
}

/** Network connectivity check. */
fun Context.isOnline(): Boolean {
    val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val cap = cm.getNetworkCapabilities(cm.activeNetwork) ?: return false
    return cap.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
}

/** Formatted time string (e.g. 10:42 AM). */
fun Long.toFormattedTime(): String {
    return SimpleDateFormat("h:mm a", Locale.getDefault()).format(Date(this))
}

/** Relative date string for chat rows — mirrors web app's relDate(). */
fun Long.toRelativeDate(): String {
    val now    = java.util.Calendar.getInstance()
    val target = java.util.Calendar.getInstance().also { it.timeInMillis = this }

    return when {
        now.get(java.util.Calendar.DATE) == target.get(java.util.Calendar.DATE) &&
        now.get(java.util.Calendar.MONTH) == target.get(java.util.Calendar.MONTH) -> {
            java.text.SimpleDateFormat("h:mm a", java.util.Locale.getDefault()).format(java.util.Date(this))
        }
        now.get(java.util.Calendar.DATE) - target.get(java.util.Calendar.DATE) == 1 -> "Yesterday"
        else -> java.text.SimpleDateFormat("d MMM", java.util.Locale.getDefault()).format(java.util.Date(this))
    }
}

/** Auto-expand an EditText as the user types, up to [maxLines]. */
fun EditText.enableAutoResize(maxHeightPx: Int) {
    addTextChangedListener(object : android.text.TextWatcher {
        override fun afterTextChanged(s: android.text.Editable?) {
            layoutParams = layoutParams.also { lp ->
                lp.height = android.view.ViewGroup.LayoutParams.WRAP_CONTENT
            }
            val measuredH = measuredHeight
            if (measuredH > maxHeightPx) {
                layoutParams = layoutParams.also { it.height = maxHeightPx }
            }
        }
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
    })
}
