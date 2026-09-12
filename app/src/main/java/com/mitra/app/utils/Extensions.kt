package com.mitra.app.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/** Network connectivity check. */
fun Context.isOnline(): Boolean {
    val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val cap = cm.getNetworkCapabilities(cm.activeNetwork) ?: return false
    return cap.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
        cap.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
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