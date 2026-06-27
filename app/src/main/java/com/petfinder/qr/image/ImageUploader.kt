package com.petfinder.qr.image

import javax.inject.Inject

/**
 * The seam between "store the image" and "where it actually lives". Today it's
 * local-only; later [R2ImageUploader] will push the file to Cloudflare R2 and
 * return a public URL — without any caller changes.
 *
 * @return the canonical URI/URL to persist on the pet (local `file://` now,
 * remote `https://` once R2 is wired).
 */
interface ImageUploader {
    suspend fun upload(localUri: String): Result<String>
}

/**
 * Current implementation: there is no remote bucket yet, so the locally-stored
 * compressed file *is* the canonical location.
 */
class LocalImageUploader @Inject constructor() : ImageUploader {
    override suspend fun upload(localUri: String): Result<String> = Result.success(localUri)
}

/**
 * Future Cloudflare R2 implementation. R2 is S3-compatible, so the eventual
 * implementation will either:
 *   1. PUT the file bytes to a short-lived presigned R2 URL fetched from our
 *      backend, or
 *   2. use the AWS S3 SDK pointed at the R2 endpoint
 *      (`https://<accountid>.r2.cloudflarestorage.com`).
 *
 * To switch over: implement the body below and bind this in
 * [com.petfinder.qr.di.ImageModule] instead of [LocalImageUploader]. The
 * compressed file produced by [ImageCompressor] is already upload-ready.
 */
class R2ImageUploader @Inject constructor(
    // e.g. okHttpClient: OkHttpClient, r2Config: R2Config
) : ImageUploader {
    override suspend fun upload(localUri: String): Result<String> =
        Result.failure(NotImplementedError("Cloudflare R2 upload is not wired yet"))
}
