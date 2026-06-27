package com.petfinder.qr.network.dto

import com.google.gson.annotations.SerializedName

/**
 * Request/response payloads for the auth endpoints. Field names match the
 * expected Spring Boot JSON contract so swapping the mock for the real backend
 * needs no changes here.
 */

data class LoginRequest(
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String,
)

data class RegisterRequest(
    @SerializedName("name") val name: String,
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String,
)

data class RefreshTokenRequest(
    @SerializedName("refreshToken") val refreshToken: String,
)

data class AuthResponse(
    @SerializedName("accessToken") val accessToken: String,
    @SerializedName("refreshToken") val refreshToken: String,
    @SerializedName("tokenType") val tokenType: String = "Bearer",
    @SerializedName("expiresIn") val expiresIn: Long = 3600,
    @SerializedName("user") val user: UserDto,
)

data class UserDto(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("email") val email: String,
)
