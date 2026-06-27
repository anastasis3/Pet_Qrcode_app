package com.petfinder.qr.repository

import android.net.Uri
import com.petfinder.qr.image.ImageCompressor
import com.petfinder.qr.image.ImageUploader
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Single entry point the UI uses for pet photos. It compresses + stores the
 * picked image, then routes it through [ImageUploader] (local today, Cloudflare
 * R2 later). The returned string is what gets persisted on the pet and rendered
 * by Coil.
 */
@Singleton
class ImageRepository @Inject constructor(
    private val imageCompressor: ImageCompressor,
    private val imageUploader: ImageUploader,
) {
    suspend fun processAndStore(uri: Uri): String {
        val localUri = imageCompressor.compressAndStore(uri)
        // LocalImageUploader returns the same local URI; R2 will return a remote URL.
        return imageUploader.upload(localUri).getOrDefault(localUri)
    }
}
