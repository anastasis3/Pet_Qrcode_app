package com.petfinder.qr.qr

import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage

/**
 * CameraX [ImageAnalysis.Analyzer] that decodes QR codes with ML Kit. Fires
 * [onQrDetected] exactly once with the first decoded value, then ignores further
 * frames (the host stops the camera on the first hit).
 */
@ExperimentalGetImage
class QrCodeAnalyzer(
    private val onQrDetected: (String) -> Unit,
) : ImageAnalysis.Analyzer {

    private val scanner = BarcodeScanning.getClient(
        BarcodeScannerOptions.Builder()
            .setBarcodeFormats(Barcode.FORMAT_QR_CODE)
            .build(),
    )

    @Volatile
    private var handled = false

    override fun analyze(imageProxy: ImageProxy) {
        val mediaImage = imageProxy.image
        if (mediaImage == null || handled) {
            imageProxy.close()
            return
        }

        val input = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
        scanner.process(input)
            .addOnSuccessListener { barcodes ->
                val value = barcodes.firstOrNull { !it.rawValue.isNullOrBlank() }?.rawValue
                if (value != null && !handled) {
                    handled = true
                    onQrDetected(value)
                }
            }
            .addOnCompleteListener { imageProxy.close() }
    }
}
