package com.mitra.app.utils

import android.util.Base64
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.SecretKeySpec
import javax.inject.Inject
import javax.inject.Singleton

private const val NONCE_SIZE   = 12
private const val TAG_LENGTH   = 128
private const val ALGORITHM    = "AES/GCM/NoPadding"
private const val KEY_ALGO     = "AES"

@Singleton
class CryptoUtils @Inject constructor() {

    @Volatile private var secretKey: SecretKey? = null

    val hasKey: Boolean get() = secretKey != null

    fun initKey(base64KeyBytes: String) {
        val rawBytes = Base64.decode(base64KeyBytes, Base64.DEFAULT)
        secretKey = SecretKeySpec(rawBytes, KEY_ALGO)
    }

    fun clearKey() {
        secretKey = null
    }

    fun encrypt(plaintext: String, uid: String): String {
        val key = secretKey ?: return plaintext

        val nonce = ByteArray(NONCE_SIZE).also { SecureRandom().nextBytes(it) }
        val aad   = uid.toByteArray(Charsets.UTF_8)
        val pt    = plaintext.toByteArray(Charsets.UTF_8)

        val cipher = Cipher.getInstance(ALGORITHM)
        cipher.init(Cipher.ENCRYPT_MODE, key, GCMParameterSpec(TAG_LENGTH, nonce))
        cipher.updateAAD(aad)
        val ct = cipher.doFinal(pt)

        val blob = nonce + ct
        return Base64.encodeToString(blob, Base64.NO_WRAP)
    }

    fun decrypt(ciphertextB64: String, uid: String): String {
        val key = secretKey ?: return ciphertextB64

        if (!isLikelyBase64Blob(ciphertextB64)) {
            return ciphertextB64
        }

        return try {
            val blob  = Base64.decode(ciphertextB64, Base64.DEFAULT)
            if (blob.size < NONCE_SIZE + 16) return ciphertextB64
            val nonce = blob.sliceArray(0 until NONCE_SIZE)
            val ct    = blob.sliceArray(NONCE_SIZE until blob.size)
            val aad   = uid.toByteArray(Charsets.UTF_8)

            val cipher = Cipher.getInstance(ALGORITHM)
            cipher.init(Cipher.DECRYPT_MODE, key, GCMParameterSpec(TAG_LENGTH, nonce))
            cipher.updateAAD(aad)
            cipher.doFinal(ct).toString(Charsets.UTF_8)
        } catch (_: Exception) {
            ciphertextB64
        }
    }

    private fun isLikelyBase64Blob(text: String): Boolean {
        if (text.length < 24 || text.contains(" ")) return false
        return try {
            val decoded = Base64.decode(text, Base64.DEFAULT)
            decoded.size >= NONCE_SIZE + 16
        } catch (_: Exception) {
            false
        }
    }
}
