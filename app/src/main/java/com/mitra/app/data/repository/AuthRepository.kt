package com.mitra.app.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

sealed class AuthResult {
    data class Success(val uid: String) : AuthResult()
    data class Error(val message: String) : AuthResult()
}

@Singleton
class AuthRepository @Inject constructor(
    private val auth: FirebaseAuth
) {
    /** Current signed-in user, or null */
    val currentUser: FirebaseUser? get() = auth.currentUser

    /**
     * Emits the current user (or null) and every subsequent auth-state change.
     * Mirrors web's onAuthStateChanged().
     */
    val authStateFlow: Flow<FirebaseUser?> = callbackFlow {
        val listener = FirebaseAuth.AuthStateListener { fa ->
            trySend(fa.currentUser)
        }
        auth.addAuthStateListener(listener)
        awaitClose { auth.removeAuthStateListener(listener) }
    }

    /** Retrieve a fresh ID token for the currently signed-in user. */
    suspend fun getIdToken(): String? {
        return try {
            auth.currentUser?.getIdToken(false)?.await()?.token
        } catch (e: Exception) {
            null
        }
    }

    suspend fun signIn(email: String, password: String): AuthResult {
        return try {
            val cred = auth.signInWithEmailAndPassword(email, password).await()
            AuthResult.Success(cred.user!!.uid)
        } catch (e: Exception) {
            AuthResult.Error(friendlyError(e))
        }
    }

    suspend fun signUp(email: String, password: String): AuthResult {
        return try {
            val cred = auth.createUserWithEmailAndPassword(email, password).await()
            AuthResult.Success(cred.user!!.uid)
        } catch (e: Exception) {
            AuthResult.Error(friendlyError(e))
        }
    }

    suspend fun signOut() {
        auth.signOut()
    }

    suspend fun sendPasswordReset(email: String): AuthResult {
        return try {
            auth.sendPasswordResetEmail(email).await()
            AuthResult.Success("")
        } catch (e: Exception) {
            AuthResult.Error(friendlyError(e))
        }
    }

    // ── Error message mapping (mirrors web app's friendly() function) ─────────
    private fun friendlyError(e: Exception): String {
        val code = e.message ?: ""
        return when {
            "email-already-in-use"  in code -> "That email is already registered. Try logging in instead."
            "invalid-email"         in code -> "That does not look like a valid email."
            "weak-password"         in code -> "Password should be at least 6 characters."
            "user-not-found"        in code -> "No account found with that email."
            "wrong-password"        in code -> "That password does not match."
            "invalid-credential"    in code -> "Email or password is incorrect."
            "too-many-requests"     in code -> "Too many attempts. Please try again in a few minutes."
            else -> "Something went wrong. Try again?"
        }
    }
}
