package com.mitra.app.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import java.util.Calendar

class ProactiveCheckInWorker(
    appContext: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)

        val messages = when (hour) {
            in 5..11 -> listOf(
                "Good morning! Got any big plans for today?",
                "Morning! Hope you have a calm, productive day ahead.",
                "Fresh start today. How are you feeling as you begin your day?"
            )
            in 12..16 -> listOf(
                "Midday check-in: How is your day going so far?",
                "Hope your day is treating you well! Remember to take a quick break.",
                "Hey, taking a pause? I'm right here if you want to chat."
            )
            in 17..21 -> listOf(
                "Evening check-in! How did your day go?",
                "Day is winding down. Anything on your mind tonight?",
                "Hope you're unwinding well this evening. I'm here if you want to talk."
            )
            else -> listOf(
                "Late night thoughts? I'm around if you want company.",
                "Still awake? Just a gentle reminder to rest well tonight.",
                "Night is quiet. Here if you need a safe space to share."
            )
        }

        val selectedMessage = messages.random()

        NotificationHelper.showNotification(
            context = applicationContext,
            title = "Mitra AI",
            message = selectedMessage
        )

        return Result.success()
    }
}
