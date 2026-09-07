package com.mitra.app.utils

import java.util.Calendar

object GreetingUtils {

    fun greetingLine(): String {
        val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        return when (hour) {
            in 0..4 -> listOf(
                "Hey, couldn't sleep? I am here if you want to talk.",
                "It is pretty quiet right now. What is keeping you up?",
                "Still awake? I am around if you want to chat about whatever is on your mind.",
                "Late night. What is going on?"
            ).random()
            in 5..11 -> listOf(
                "Morning, how are you feeling as you start the day?",
                "Good morning. How are you feeling today?",
                "Morning. Fresh start. How are things looking?",
                "New day. How are you doing today?"
            ).random()
            in 12..16 -> listOf(
                "Hey, what has been going on with you today?",
                "Taking a break? How is your day going so far?",
                "Midday check in. How is everything going?",
                "How is your day treating you so far?"
            ).random()
            in 17..21 -> listOf(
                "Evening. How was your day?",
                "Hey, evening. Things usually quiet down now. What is on your mind?",
                "Evening is here. How are you holding up today?",
                "Day is winding down. How did it go?"
            ).random()
            else -> listOf(
                "Hey, up late tonight? I am around if you want to talk.",
                "Late night thoughts? Feel free to share.",
                "Hey, what is up tonight?",
                "Night is quiet. Here if you want company for a bit."
            ).random()
        }
    }

    fun returnLine(): String = listOf(
        "Hey, welcome back. How have you been since we last talked?",
        "Good to see you again. How has everything been?",
        "Hey, welcome back. What is new since we last spoke?",
        "Welcome back. How have things been going?"
    ).random()

    val safeReplies: List<String> = listOf(
        "I am glad you told me that, and I am right here. You matter, and even when things feel overwhelming, feelings change. I am here with you. What is making things feel so heavy today?",
        "Thank you for saying that out loud instead of keeping it inside. I am here with you. The pain you are feeling is real, but you do not have to handle it alone. I am right here. How long have you felt this way?",
        "I am right here with you. I know things feel unbearable right now, but please do not carry this alone. I am staying right here with you. What happened today?",
        "Thank you for telling me. This is not something you have to carry by yourself. I am here. What happened today?"
    )

    fun randomSafeReply(): String = safeReplies.random()
}
