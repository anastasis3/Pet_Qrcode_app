package com.petfinder.qr.model

/** The authenticated owner, derived from the JWT/login response. */
data class AuthUser(
    val id: String,
    val name: String,
    val email: String,
)

/** Result of an authentication attempt (login / register). */
sealed interface AuthResult {
    data class Success(val user: AuthUser) : AuthResult
    data class Error(val message: String) : AuthResult
}
