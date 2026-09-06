package com.mitra.app

import android.app.Application
import com.mitra.app.worker.NotificationHelper
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MitraApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        NotificationHelper.createNotificationChannel(this)
    }
}
