package com.petfinder.qr.image

import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.min

/**
 * Decodes a picked image efficiently (sampled to avoid OOM on huge photos),
 * fixes EXIF rotation, downscales it, JPEG-compresses it and writes the result
 * to app-internal storage. Returns a persistent `file://` URI string.
 *
 * Keeping a compressed copy on disk means the picked image survives the
 * temporary content-URI grant, works offline, and is exactly what we'd later
 * hand to the Cloudflare R2 uploader.
 */
@Singleton
class ImageCompressor @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    suspend fun compressAndStore(
        uri: Uri,
        maxDimension: Int = DEFAULT_MAX_DIMENSION,
        quality: Int = DEFAULT_QUALITY,
    ): String = withContext(Dispatchers.IO) {
        val resolver = context.contentResolver

        // 1. Read just the bounds to compute a safe sample size.
        // (decodeStream returns null in inJustDecodeBounds mode — that's expected.)
        val bounds = BitmapFactory.Options().apply { inJustDecodeBounds = true }
        val boundsStream = resolver.openInputStream(uri)
            ?: throw IOException("Unable to open image stream")
        boundsStream.use { BitmapFactory.decodeStream(it, null, bounds) }

        // 2. Decode downsampled.
        val decodeOptions = BitmapFactory.Options().apply {
            inSampleSize = calculateInSampleSize(bounds.outWidth, bounds.outHeight, maxDimension)
        }
        val decoded = resolver.openInputStream(uri)?.use {
            BitmapFactory.decodeStream(it, null, decodeOptions)
        } ?: throw IOException("Unable to decode image")

        // 3. Orient + downscale to the target box.
        val rotated = applyExifRotation(resolver, uri, decoded)
        val scaled = scaleDown(rotated, maxDimension)

        // 4. Write JPEG into internal storage.
        val directory = File(context.filesDir, IMAGE_DIR).apply { mkdirs() }
        val outFile = File(directory, "pet_${UUID.randomUUID()}.jpg")
        FileOutputStream(outFile).use { output ->
            scaled.compress(Bitmap.CompressFormat.JPEG, quality, output)
        }

        Uri.fromFile(outFile).toString()
    }

    private fun calculateInSampleSize(width: Int, height: Int, target: Int): Int {
        if (width <= 0 || height <= 0) return 1
        var sampleSize = 1
        while (width / (sampleSize * 2) >= target && height / (sampleSize * 2) >= target) {
            sampleSize *= 2
        }
        return sampleSize
    }

    private fun scaleDown(bitmap: Bitmap, max: Int): Bitmap {
        val width = bitmap.width
        val height = bitmap.height
        if (width <= max && height <= max) return bitmap
        val ratio = min(max.toFloat() / width, max.toFloat() / height)
        val targetWidth = (width * ratio).toInt().coerceAtLeast(1)
        val targetHeight = (height * ratio).toInt().coerceAtLeast(1)
        return Bitmap.createScaledBitmap(bitmap, targetWidth, targetHeight, true)
    }

    private fun applyExifRotation(resolver: ContentResolver, uri: Uri, bitmap: Bitmap): Bitmap {
        val degrees = runCatching {
            resolver.openInputStream(uri)?.use { input ->
                when (ExifInterface(input).getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL,
                )) {
                    ExifInterface.ORIENTATION_ROTATE_90 -> 90f
                    ExifInterface.ORIENTATION_ROTATE_180 -> 180f
                    ExifInterface.ORIENTATION_ROTATE_270 -> 270f
                    else -> 0f
                }
            } ?: 0f
        }.getOrDefault(0f)

        if (degrees == 0f) return bitmap
        val matrix = Matrix().apply { postRotate(degrees) }
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }

    private companion object {
        const val IMAGE_DIR = "pet_images"
        const val DEFAULT_MAX_DIMENSION = 1280
        const val DEFAULT_QUALITY = 80
    }
}
