package com.petfinder.qr.network

import com.petfinder.qr.network.dto.ImageUploadResponse
import com.petfinder.qr.network.dto.LogScanRequest
import com.petfinder.qr.network.dto.PetDto
import com.petfinder.qr.network.dto.PetRequest
import com.petfinder.qr.network.dto.ScanDto
import com.petfinder.qr.network.dto.UpdateStatusRequest
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path

/**
 * The Spring Boot pet/scan/image REST contract. Methods throw on non-2xx
 * (handled by [safeApiCall]/[retryIO]); the [Authorization] header is added by
 * [AuthInterceptor].
 */
interface PetApiService {

    // ── Pet CRUD ────────────────────────────────────────────────────────────
    @GET("api/pets")
    suspend fun getPets(): List<PetDto>

    @GET("api/pets/{id}")
    suspend fun getPet(@Path("id") id: String): PetDto

    @POST("api/pets")
    suspend fun createPet(@Body request: PetRequest): PetDto

    @PUT("api/pets/{id}")
    suspend fun updatePet(@Path("id") id: String, @Body request: PetRequest): PetDto

    @PATCH("api/pets/{id}/status")
    suspend fun updateStatus(@Path("id") id: String, @Body request: UpdateStatusRequest): PetDto

    @DELETE("api/pets/{id}")
    suspend fun deletePet(@Path("id") id: String)

    // ── Lost Pets feed ────────────────────────────────────────────────────────
    @GET("api/pets/lost")
    suspend fun getLostPets(): List<PetDto>

    // ── QR / public lookup (no auth needed server-side) ───────────────────────
    @GET("api/pets/public/{id}")
    suspend fun getPublicPet(@Path("id") id: String): PetDto

    // ── Scan History + Location upload ────────────────────────────────────────
    @GET("api/pets/{id}/scans")
    suspend fun getScans(@Path("id") id: String): List<ScanDto>

    @POST("api/pets/{id}/scans")
    suspend fun logScan(@Path("id") id: String, @Body request: LogScanRequest): ScanDto

    // ── Image upload ──────────────────────────────────────────────────────────
    @Multipart
    @POST("api/images")
    suspend fun uploadImage(@Part file: MultipartBody.Part): ImageUploadResponse
}
