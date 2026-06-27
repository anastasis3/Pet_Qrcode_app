package com.petfinder.qr.viewmodel.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.petfinder.qr.model.AuthResult
import com.petfinder.qr.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AuthUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isAuthenticated: Boolean = false,
)

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    fun login(email: String, password: String) =
        authenticate { authRepository.login(email.trim(), password) }

    fun register(name: String, email: String, password: String) =
        authenticate { authRepository.register(name.trim(), email.trim(), password) }

    fun logout() {
        viewModelScope.launch { authRepository.logout() }
    }

    fun isLoggedIn(): Boolean = authRepository.isLoggedInNow()

    fun consumeError() {
        _uiState.update { it.copy(errorMessage = null) }
    }

    private fun authenticate(block: suspend () -> AuthResult) {
        if (_uiState.value.isLoading) return
        _uiState.update { it.copy(isLoading = true, errorMessage = null) }
        viewModelScope.launch {
            when (val result = block()) {
                is AuthResult.Success ->
                    _uiState.update { it.copy(isLoading = false, isAuthenticated = true) }

                is AuthResult.Error ->
                    _uiState.update { it.copy(isLoading = false, errorMessage = result.message) }
            }
        }
    }
}
