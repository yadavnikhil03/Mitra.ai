package com.mitra.app.data.api;

import com.google.gson.annotations.SerializedName;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Streaming;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\bf\u0018\u00002\u00020\u0001J\u001e\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u00032\b\b\u0001\u0010\u0005\u001a\u00020\u0006H\u00a7@\u00a2\u0006\u0002\u0010\u0007J\u001e\u0010\b\u001a\b\u0012\u0004\u0012\u00020\t0\u00032\b\b\u0001\u0010\n\u001a\u00020\u000bH\u00a7@\u00a2\u0006\u0002\u0010\fJ\u0014\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u000e0\u0003H\u00a7@\u00a2\u0006\u0002\u0010\u000f\u00a8\u0006\u0010"}, d2 = {"Lcom/mitra/app/data/api/MitraApiService;", "", "chat", "Lretrofit2/Response;", "Lokhttp3/ResponseBody;", "request", "Lcom/mitra/app/data/api/ChatRequest;", "(Lcom/mitra/app/data/api/ChatRequest;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getSessionKey", "Lcom/mitra/app/data/api/SessionKeyResponse;", "bearerToken", "", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "health", "Lcom/mitra/app/data/api/HealthResponse;", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_debug"})
public abstract interface MitraApiService {
    
    /**
     * Streaming chat endpoint — returns a raw body that is read chunk-by-chunk
     * using OkHttp's buffered source (ResponseBody.source()).
     * Must be called on an IO dispatcher.
     */
    @retrofit2.http.Streaming()
    @retrofit2.http.POST(value = "chat")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object chat(@retrofit2.http.Body()
    @org.jetbrains.annotations.NotNull()
    com.mitra.app.data.api.ChatRequest request, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super retrofit2.Response<okhttp3.ResponseBody>> $completion);
    
    /**
     * Fetches the per-session AES-256-GCM derived key for this user.
     * Requires a valid Firebase ID token in the Authorization header.
     */
    @retrofit2.http.GET(value = "session-key")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getSessionKey(@retrofit2.http.Header(value = "Authorization")
    @org.jetbrains.annotations.NotNull()
    java.lang.String bearerToken, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super retrofit2.Response<com.mitra.app.data.api.SessionKeyResponse>> $completion);
    
    /**
     * Simple liveness probe — used to warm the Render instance on app start
     */
    @retrofit2.http.GET(value = "health")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object health(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super retrofit2.Response<com.mitra.app.data.api.HealthResponse>> $completion);
}