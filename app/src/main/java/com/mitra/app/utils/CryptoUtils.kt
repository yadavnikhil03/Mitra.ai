package com.mitra.app.utils

import android.util.Base64
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.SecretKeySpec
import javax.inject.Inject
import javax.inject.Singleton

private const val NONCE_SIZE   = 12          // 96-bit IV — must match backend
private const val TAG_LENGTH   = 128         // GCM tag bits
private const val ALGORITHM    = "AES/GCM/NoPadding"
private const val KEY_ALGO     = "AES"

/**
 * Client-side AES-256-GCM encryption, mirroring crypto-utils.js exactly.
 *
 * Design:
 *  - The master secret never leaves the server.
 *  - The backend derives a per-user key via HKDF and returns its raw bytes
 *    from GET /session-key (authenticated with a Firebase ID token).
 *  - We import those bytes and hold the [SecretKey] in-memory only — never
 *    written to disk or Keystore — exactly like the Web Crypto non-extractable
 *    key object in the browser.
 *  - AAD = UTF-8 bytes of the user's UID, matching the backend binding.
 *
 * Format: Base64( nonce[12] || ciphertext+tag ) — same as Python backend.
 */
@Singleton
class CryptoUtils @Inject constructor() {

    /** Holds the current session key; null until [initKey] is called. */
    @Volatile private var secretKey: SecretKey? = null

    /** True only when a valid session key has been imported. */
    val hasKey: Boolean get() = secretKey != null

    /**
     * Import raw key bytes returned by the /session-key endpoint.
     * The bytes are base64-encoded by the server.
     */
    fun initKey(base64KeyBytes: String) {
        val rawBytes = Base64.decode(base64KeyBytes, Base64.DEFAULT)
        secretKey = SecretKeySpec(rawBytes, KEY_ALGO)
    }

    /** Drop the in-memory key on sign-out. */
    fun clearKey() {
        secretKey = null
    }

    /**
     * Encrypt [plaintext] → Base64(nonce || ciphertext+tag).
     * [uid] is used as AAD, binding the ciphertext to this user.
     */
    fun encrypt(plaintext: String, uid: String): String {
        val key = secretKey ?: return plaintext   // no key → pass-through

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

    /**
     * Decrypt a Base64(nonce || ciphertext+tag) blob back to plain text.
     * Throws [SecurityException] on tamper / wrong key, matching backend posture.
     */
    fun decrypt(ciphertextB64: String, uid: String): String {
        val key = secretKey ?: return ciphertextB64  // no key → pass-through

        val blob  = Base64.decode(ciphertextB64, Base64.DEFAULT)
        val nonce = blob.sliceArray(0 until NONCE_SIZE)
        val ct    = blob.sliceArray(NONCE_SIZE until blob.size)
        val aad   = uid.toByteArray(Charsets.UTF_8)

        return try {
            val cipher = Cipher.getInstance(ALGORITHM)
            cipher.init(Cipher.DECRYPT_MODE, key, GCMParameterSpec(TAG_LENGTH, nonce))
            cipher.updateAAD(aad)
            cipher.doFinal(ct).toString(Charsets.UTF_8)
        } catch (e: Exception) {
            throw SecurityException("decryption failed: invalid ciphertext, key, or tampered data", e)
        }
    }
}
