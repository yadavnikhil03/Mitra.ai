# Keep Firebase model classes
-keep class com.google.firebase.** { *; }
-keep class com.google.android.gms.** { *; }

# Keep Retrofit / OkHttp
-keepattributes Signature
-keepattributes *Annotation*
-keep class retrofit2.** { *; }
-keep class okhttp3.** { *; }
-keep class okio.** { *; }

# Keep Mitra API DTOs (Gson serialization)
-keep class com.mitra.app.data.api.** { *; }
-keep class com.mitra.app.data.model.** { *; }
-keep class com.mitra.app.data.repository.ChatDto { *; }
-keep class com.mitra.app.data.repository.MessageDto { *; }

# Hilt
-keep class dagger.hilt.** { *; }
-keep class javax.inject.** { *; }

# Room
-keep class androidx.room.** { *; }

# Suppress missing class warnings for optional deps
-dontwarn com.google.errorprone.**
-dontwarn javax.annotation.**
