package com.petfinder.qr.network

import com.petfinder.qr.auth.TokenManager
import com.petfinder.qr.network.dto.RefreshTokenRequest
import dagger.Lazy
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Standard JWT refresh flow: when an authenticated request comes back 401, try
 * to mint a new access token from the stored refresh token and replay the
 * request once. This is dormant in mock mode (no real 401s) but is the exact
 * mechanism Spring Boot will rely on.
 *
 * [Lazy] breaks the DI cycle OkHttp → Authenticator → AuthApiService → Retrofit
 * → OkHttp.
 */
@Singleton
class TokenAuthenticator @Inject constructor(
    private val tokenManager: TokenManager,
    private val authApi: Lazy<AuthApiService>,
) : Authenticator {

    override fun authenticate(route: Route?, response: Response): Request? {
        // Never try to refresh an auth call, and give up after a couple of tries.
        if (response.request.url.encodedPath.contains("/auth/")) return null
        if (responseCount(response) >= 2) return null

        val refreshToken = tokenManager.refreshToken ?: return null

        val refreshed = runBlocking {
            runCatching {
                val result = authApi.get().refresh(RefreshTokenRequest(refreshToken))
                if (result.isSuccessful) result.body() else null
            }.getOrNull()
        } ?: run {
            tokenManager.clear()
            return null
        }

        tokenManager.updateTokens(refreshed.accessToken, refreshed.refreshToken, refreshed.expiresIn)

        return response.request.newBuilder()
            .header("Authorization", "Bearer ${refreshed.accessToken}")
            .build()
    }

    private fun responseCount(response: Response): Int {
        var prior = response.priorResponse
        var count = 1
        while (prior != null) {
            count++
            prior = prior.priorResponse
        }
        return count
    }
}
