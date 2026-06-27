package com.petfinder.qr.auth

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.petfinder.qr.model.AuthUser
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Persists the JWT session in [EncryptedSharedPreferences] (AES-256). The access
 * token, refresh token, expiry and the current user all live encrypted on-device,
 * so a session survives app restarts and works fully offline.
 */
@Singleton
class TokenManager @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    private val prefs: SharedPreferences = createEncryptedPrefs(context)

    private val _authState = MutableStateFlow(isLoggedIn())
    /** Emits true while a session is stored, false after logout. */
    val authState: StateFlow<Boolean> = _authState.asStateFlow()

    val accessToken: String? get() = prefs.getString(KEY_ACCESS, null)
    val refreshToken: String? get() = prefs.getString(KEY_REFRESH, null)

    fun isLoggedIn(): Boolean = accessToken != null

    fun isAccessTokenExpired(): Boolean {
        val expiresAt = prefs.getLong(KEY_EXPIRES_AT, 0L)
        return expiresAt != 0L && System.currentTimeMillis() >= expiresAt
    }

    fun currentUser(): AuthUser? {
        val id = prefs.getString(KEY_USER_ID, null) ?: return null
        return AuthUser(
            id = id,
            name = prefs.getString(KEY_USER_NAME, "").orEmpty(),
            email = prefs.getString(KEY_USER_EMAIL, "").orEmpty(),
        )
    }

    /** Stores a brand-new session (after login / register). */
    fun saveSession(
        accessToken: String,
        refreshToken: String,
        expiresInSeconds: Long,
        user: AuthUser,
    ) {
        prefs.edit()
            .putString(KEY_ACCESS, accessToken)
            .putString(KEY_REFRESH, refreshToken)
            .putLong(KEY_EXPIRES_AT, System.currentTimeMillis() + expiresInSeconds * 1000)
            .putString(KEY_USER_ID, user.id)
            .putString(KEY_USER_NAME, user.name)
            .putString(KEY_USER_EMAIL, user.email)
            .apply()
        _authState.value = true
    }

    /** Replaces just the tokens (after a refresh) — keeps the stored user. */
    fun updateTokens(accessToken: String, refreshToken: String, expiresInSeconds: Long) {
        prefs.edit()
            .putString(KEY_ACCESS, accessToken)
            .putString(KEY_REFRESH, refreshToken)
            .putLong(KEY_EXPIRES_AT, System.currentTimeMillis() + expiresInSeconds * 1000)
            .apply()
        _authState.value = true
    }

    fun clear() {
        prefs.edit().clear().apply()
        _authState.value = false
    }

    private fun createEncryptedPrefs(context: Context): SharedPreferences = try {
        buildPrefs(context)
    } catch (e: Exception) {
        // A corrupted keystore/file would otherwise crash on every launch — reset once.
        context.deleteSharedPreferences(PREFS_NAME)
        buildPrefs(context)
    }

    private fun buildPrefs(context: Context): SharedPreferences {
        val masterKey = MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()
        return EncryptedSharedPreferences.create(
            context,
            PREFS_NAME,
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM,
        )
    }

    private companion object {
        const val PREFS_NAME = "petfinder_secure_prefs"
        const val KEY_ACCESS = "access_token"
        const val KEY_REFRESH = "refresh_token"
        const val KEY_EXPIRES_AT = "expires_at"
        const val KEY_USER_ID = "user_id"
        const val KEY_USER_NAME = "user_name"
        const val KEY_USER_EMAIL = "user_email"
    }
}
