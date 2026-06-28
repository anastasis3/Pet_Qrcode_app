package com.petfinder.qr.image

import androidx.core.net.toFile
import androidx.core.net.toUri
import com.petfinder.qr.network.PetApiService
import com.petfinder.qr.network.retryIO
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import javax.inject.Inject

/**
 * Uploads the locally-compressed image to the backend (multipart) and returns
 * the remote URL to persist on the pet. Active when
 * [com.petfinder.qr.utils.Constants.USE_REMOTE_BACKEND] is on.
 */
class ApiImageUploader @Inject constructor(
    private val petApi: PetApiService,
) : ImageUploader {

    override suspend fun upload(localUri: String): Result<String> = try {
        val file = localUri.toUri().toFile()
        val body = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
        val part = MultipartBody.Part.createFormData("file", file.name, body)
        val response = retryIO { petApi.uploadImage(part) }
        Result.success(response.url)
    } catch (e: Exception) {
        // On failure, keep the local URI so the pet still shows its photo offline.
        Result.failure(e)
    }
}
