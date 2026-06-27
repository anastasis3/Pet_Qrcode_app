package com.petfinder.qr.repository

import com.petfinder.qr.auth.TokenManager
import com.petfinder.qr.model.AuthResult
import com.petfinder.qr.model.AuthUser
import com.petfinder.qr.network.AuthApiService
import com.petfinder.qr.network.dto.AuthResponse
import com.petfinder.qr.network.dto.LoginRequest
import com.petfinder.qr.network.dto.RefreshTokenRequest
import com.petfinder.qr.network.dto.RegisterRequest
import kotlinx.coroutines.flow.StateFlow
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Coordinates authentication: talks to [AuthApiService] (mock today, Spring Boot
 * tomorrow) and persists the JWT session via [TokenManager]. ViewModels depend
 * only on this class.
 */
@Singleton
class AuthRepository @Inject constructor(
    private val authApi: AuthApiService,
    private val tokenManager: TokenManager,
) {
    /** Reactive session state for navigation gating. */
    val isLoggedIn: StateFlow<Boolean> = tokenManager.authState

    fun isLoggedInNow(): Boolean = tokenManager.isLoggedIn()

    fun currentUser(): AuthUser? = tokenManager.currentUser()

    suspend fun login(email: String, password: String): AuthResult =
        authenticate { authApi.login(LoginRequest(email, password)) }

    suspend fun register(name: String, email: String, password: String): AuthResult =
        authenticate { authApi.register(RegisterRequest(name, email, password)) }

    /** Best-effort server logout, then always clears local tokens. */
    suspend fun logout() {
        runCatching { authApi.logout() }
        tokenManager.clear()
    }

    /** Exchanges the refresh token for a fresh access token. Returns success. */
    suspend fun refreshToken(): Boolean {
        val refreshToken = tokenManager.refreshToken ?: return false
        return runCatching {
            val response = authApi.refresh(RefreshTokenRequest(refreshToken))
            val body = response.body()
            if (response.isSuccessful && body != null) {
                tokenManager.updateTokens(body.accessToken, body.refreshToken, body.expiresIn)
                true
            } else {
                false
            }
        }.getOrDefault(false)
    }

    private suspend fun authenticate(call: suspend () -> Response<AuthResponse>): AuthResult =
        try {
            val response = call()
            val body = response.body()
            if (response.isSuccessful && body != null) {
                val user = AuthUser(body.user.id, body.user.name, body.user.email)
                tokenManager.saveSession(body.accessToken, body.refreshToken, body.expiresIn, user)
                AuthResult.Success(user)
            } else {
                AuthResult.Error("Authentication failed (${response.code()}). Please try again.")
            }
        } catch (e: IOException) {
            AuthResult.Error("No connection. Please check your network and try again.")
        } catch (e: Exception) {
            AuthResult.Error(e.message ?: "Something went wrong. Please try again.")
        }
}
