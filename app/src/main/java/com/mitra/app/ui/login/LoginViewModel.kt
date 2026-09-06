package com.mitra.app.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mitra.app.data.repository.AuthRepository
import com.mitra.app.data.repository.AuthResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class AuthMode { LOGIN, SIGNUP }

data class LoginUiState(
    val mode: AuthMode       = AuthMode.LOGIN,
    val isLoading: Boolean   = false,
    val errorMessage: String = ""
)

sealed class LoginEvent {
    object NavigateToChat : LoginEvent()
    data class ShowError(val msg: String) : LoginEvent()
    data class ShowToast(val msg: String) : LoginEvent()
}

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepo: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<LoginEvent>()
    val events: SharedFlow<LoginEvent> = _events.asSharedFlow()

    fun toggleMode() {
        val next = if (_uiState.value.mode == AuthMode.LOGIN) AuthMode.SIGNUP else AuthMode.LOGIN
        _uiState.value = _uiState.value.copy(mode = next, errorMessage = "")
    }

    fun submit(email: String, password: String) {
        if (email.isBlank() || password.isBlank()) {
            _uiState.value = _uiState.value.copy(errorMessage = "Enter both email and password.")
            return
        }
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = "")
            val result = when (_uiState.value.mode) {
                AuthMode.LOGIN  -> authRepo.signIn(email, password)
                AuthMode.SIGNUP -> authRepo.signUp(email, password)
            }
            _uiState.value = _uiState.value.copy(isLoading = false)
            when (result) {
                is AuthResult.Success -> _events.emit(LoginEvent.NavigateToChat)
                is AuthResult.Error   -> _uiState.value = _uiState.value.copy(errorMessage = result.message)
            }
        }
    }

    fun forgotPassword(email: String) {
        if (email.isBlank()) {
            _uiState.value = _uiState.value.copy(errorMessage = "Enter your email above first, then tap this again.")
            return
        }
        viewModelScope.launch {
            val result = authRepo.sendPasswordReset(email)
            val msg = when (result) {
                is AuthResult.Success -> "Password reset email sent. Check your inbox."
                is AuthResult.Error   -> result.message
            }
            _uiState.value = _uiState.value.copy(errorMessage = msg)
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = "")
    }
}
