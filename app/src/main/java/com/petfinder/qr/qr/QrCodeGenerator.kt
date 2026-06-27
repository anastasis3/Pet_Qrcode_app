package com.petfinder.qr.qr

import android.graphics.Bitmap
import android.graphics.Color
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.MultiFormatWriter
import com.petfinder.qr.utils.Constants

/**
 * Turns a pet's deep-link URL into a scannable QR bitmap (ZXing). Pure/offline —
 * no Android context needed.
 */
object QrCodeGenerator {

    /** The unique, scannable URL encoded for a pet: `https://petfinder.app/pet/{petId}`. */
    fun contentFor(petId: String): String = Constants.QR_BASE_URL + petId

    /**
     * Extracts the pet id from a scanned value, e.g.
     * `https://petfinder.app/pet/abc123` -> `abc123`. Returns null if the value
     * isn't one of our pet links.
     */
    fun petIdFrom(content: String): String? {
        val marker = "/pet/"
        val index = content.indexOf(marker)
        if (index == -1) return null
        return content.substring(index + marker.length)
            .substringBefore('/')
            .substringBefore('?')
            .trim()
            .ifBlank { null }
    }

    fun generate(content: String, sizePx: Int): Bitmap? = try {
        val hints = mapOf(EncodeHintType.MARGIN to 1)
        val matrix = MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, sizePx, sizePx, hints)
        val width = matrix.width
        val height = matrix.height
        val pixels = IntArray(width * height)
        for (y in 0 until height) {
            val offset = y * width
            for (x in 0 until width) {
                pixels[offset + x] = if (matrix[x, y]) Color.BLACK else Color.WHITE
            }
        }
        Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888).apply {
            setPixels(pixels, 0, width, 0, 0, width, height)
        }
    } catch (e: Exception) {
        null
    }
}
