package com.petfinder.qr.network

import com.petfinder.qr.network.dto.AuthResponse
import com.petfinder.qr.network.dto.LoginRequest
import com.petfinder.qr.network.dto.RefreshTokenRequest
import com.petfinder.qr.network.dto.RegisterRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * Auth endpoints. The paths mirror a typical Spring Security / JWT controller
 * (`/api/auth/...`). Today these are answered by [MockAuthInterceptor]; pointing
 * [com.petfinder.qr.utils.Constants.BASE_URL] at the real server is all that's
 * needed to go live.
 */
interface AuthApiService {

    @POST("api/auth/login")
    suspend fun login(@Body request: LoginRequest): Response<AuthResponse>

    @POST("api/auth/register")
    suspend fun register(@Body request: RegisterRequest): Response<AuthResponse>

    @POST("api/auth/refresh")
    suspend fun refresh(@Body request: RefreshTokenRequest): Response<AuthResponse>

    @POST("api/auth/logout")
    suspend fun logout(): Response<Unit>
}
