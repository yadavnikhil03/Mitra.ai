package com.mitra.app.data.api

import com.google.gson.annotations.SerializedName
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Streaming

// ── Request / Response DTOs ──────────────────────────────────────────────────

data class ApiMessage(
    @SerializedName("role")    val role: String,
    @SerializedName("content") val content: String
)

data class ChatRequest(
    @SerializedName("messages") val messages: List<ApiMessage>
)

data class SessionKeyResponse(
    @SerializedName("key") val key: String
)

data class HealthResponse(
    @SerializedName("status") val status: String = "ok"
)

// ── Retrofit interface ───────────────────────────────────────────────────────

interface MitraApiService {

    /**
     * Streaming chat endpoint — returns a raw body that is read chunk-by-chunk
     * using OkHttp's buffered source (ResponseBody.source()).
     * Must be called on an IO dispatcher.
     */
    @Streaming
    @POST("chat")
    suspend fun chat(
        @Body request: ChatRequest
    ): Response<ResponseBody>

    /**
     * Fetches the per-session AES-256-GCM derived key for this user.
     * Requires a valid Firebase ID token in the Authorization header.
     */
    @GET("session-key")
    suspend fun getSessionKey(
        @Header("Authorization") bearerToken: String
    ): Response<SessionKeyResponse>

    /** Simple liveness probe — used to warm the Render instance on app start */
    @GET("health")
    suspend fun health(): Response<HealthResponse>
}
