package com.petfinder.qr.screens.scanner

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview as CameraPreviewUseCase
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import com.petfinder.qr.components.PrimaryButton
import com.petfinder.qr.components.TopBar
import com.petfinder.qr.components.VGap
import com.petfinder.qr.qr.QrCodeAnalyzer
import com.petfinder.qr.qr.QrCodeGenerator
import com.petfinder.qr.theme.AppIcons
import com.petfinder.qr.theme.ComponentShapes
import com.petfinder.qr.theme.Dimensions
import com.petfinder.qr.theme.Spacing
import java.util.concurrent.Executors

/**
 * Live CameraX QR scanner. On the first pet link it decodes, it hands the petId
 * back via [onQrScanned] (the host then opens the public recovery profile).
 */
@Composable
fun QrScannerScreen(
    onBack: () -> Unit = {},
    onQrScanned: (petId: String) -> Unit = {},
) {
    val context = LocalContext.current
    var hasCameraPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) ==
                PackageManager.PERMISSION_GRANTED,
        )
    }
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission(),
    ) { granted -> hasCameraPermission = granted }

    LaunchedEffect(Unit) {
        if (!hasCameraPermission) permissionLauncher.launch(Manifest.permission.CAMERA)
    }

    var handled by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopBar(
                title = "Scan QR Code",
                showLogo = false,
                showBackButton = true,
                onBackClick = onBack,
            )
        },
        containerColor = MaterialTheme.colorScheme.background,
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center,
        ) {
            if (hasCameraPermission) {
                CameraPreview(
                    modifier = Modifier.fillMaxSize(),
                    onQrDetected = { raw ->
                        if (!handled) {
                            QrCodeGenerator.petIdFrom(raw)?.let { petId ->
                                handled = true
                                onQrScanned(petId)
                            }
                        }
                    },
                )
                ScannerOverlay()
            } else {
                PermissionRequest(
                    onGrant = { permissionLauncher.launch(Manifest.permission.CAMERA) },
                )
            }
        }
    }
}

@OptIn(ExperimentalGetImage::class)
@Composable
private fun CameraPreview(
    onQrDetected: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val executor = remember { Executors.newSingleThreadExecutor() }
    DisposableEffect(Unit) {
        onDispose { executor.shutdown() }
    }

    AndroidView(
        modifier = modifier,
        factory = { ctx ->
            val previewView = PreviewView(ctx).apply {
                scaleType = PreviewView.ScaleType.FILL_CENTER
            }
            val providerFuture = ProcessCameraProvider.getInstance(ctx)
            providerFuture.addListener({
                val cameraProvider = providerFuture.get()
                val preview = CameraPreviewUseCase.Builder().build().also {
                    it.setSurfaceProvider(previewView.surfaceProvider)
                }
                val analysis = ImageAnalysis.Builder()
                    .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                    .build()
                    .also {
                        it.setAnalyzer(
                            executor,
                            QrCodeAnalyzer { value ->
                                ContextCompat.getMainExecutor(ctx).execute { onQrDetected(value) }
                            },
                        )
                    }
                runCatching {
                    cameraProvider.unbindAll()
                    cameraProvider.bindToLifecycle(
                        lifecycleOwner,
                        CameraSelector.DEFAULT_BACK_CAMERA,
                        preview,
                        analysis,
                    )
                }
            }, ContextCompat.getMainExecutor(ctx))
            previewView
        },
    )
}

@Composable
private fun BoxScope.ScannerOverlay() {
    Box(
        modifier = Modifier
            .align(Alignment.Center)
            .size(260.dp)
            .border(
                width = 3.dp,
                color = Color.White,
                shape = ComponentShapes.large,
            ),
    )
    Box(
        modifier = Modifier
            .align(Alignment.BottomCenter)
            .padding(bottom = 48.dp)
            .clip(ComponentShapes.pill)
            .background(Color.Black.copy(alpha = 0.55f))
            .padding(horizontal = Spacing.lg, vertical = Spacing.sm),
    ) {
        Text(
            text = "Point your camera at a pet's QR tag",
            style = MaterialTheme.typography.titleSmall,
            color = Color.White,
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
private fun PermissionRequest(onGrant: () -> Unit) {
    Column(
        modifier = Modifier.padding(Spacing.xxl),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Icon(
            imageVector = AppIcons.QrScanner,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(Dimensions.iconSizeXLarge),
        )
        VGap(Spacing.md)
        Text(
            text = "Camera access needed",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface,
        )
        VGap(Spacing.xs)
        Text(
            text = "Allow camera access to scan a pet's QR tag and view their recovery profile.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
        )
        VGap(Spacing.lg)
        PrimaryButton(
            text = "Grant Permission",
            onClick = onGrant,
            fillMaxWidth = false,
        )
    }
}
