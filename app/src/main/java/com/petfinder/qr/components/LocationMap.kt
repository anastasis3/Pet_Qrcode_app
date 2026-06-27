package com.petfinder.qr.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.unit.Dp
import com.google.android.gms.maps.GoogleMapOptions
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.petfinder.qr.theme.ComponentShapes
import com.petfinder.qr.theme.Dimensions
import com.petfinder.qr.theme.Spacing
import com.petfinder.qr.theme.softShadow

/**
 * Shows a Google Map centred on [latitude]/[longitude] with a single marker —
 * the pet's last (or a historical) scan location.
 *
 * Google Maps can't render under Layoutlib, so in @Preview / inspection mode this
 * falls back to the stylized [MapPreview] placeholder. Defaults to lite mode
 * (a lightweight static map), which is ideal inside scrolling cards.
 */
@Composable
fun LocationMap(
    latitude: Double,
    longitude: Double,
    modifier: Modifier = Modifier,
    height: Dp = Dimensions.mapPreviewHeightCompact,
    markerTitle: String? = null,
    chipText: String? = null,
    chipIcon: ImageVector? = null,
    chipAlignment: Alignment = Alignment.BottomStart,
    interactive: Boolean = false,
) {
    if (LocalInspectionMode.current) {
        MapPreview(
            modifier = modifier,
            height = height,
            chipText = chipText,
            chipIcon = chipIcon,
            chipAlignment = chipAlignment,
        )
        return
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(height)
            .clip(ComponentShapes.mapPreview),
    ) {
        val location = LatLng(latitude, longitude)
        val cameraPositionState = rememberCameraPositionState {
            position = CameraPosition.fromLatLngZoom(location, 15f)
        }
        val markerState = remember(location) { MarkerState(position = location) }

        GoogleMap(
            modifier = Modifier.matchParentSize(),
            cameraPositionState = cameraPositionState,
            googleMapOptionsFactory = { GoogleMapOptions().liteMode(!interactive) },
            uiSettings = MapUiSettings(
                zoomControlsEnabled = false,
                scrollGesturesEnabled = interactive,
                zoomGesturesEnabled = interactive,
                rotationGesturesEnabled = false,
                tiltGesturesEnabled = false,
                mapToolbarEnabled = false,
                compassEnabled = false,
            ),
        ) {
            Marker(state = markerState, title = markerTitle)
        }

        if (chipText != null) {
            Row(
                modifier = Modifier
                    .align(chipAlignment)
                    .padding(Spacing.sm)
                    .clip(ComponentShapes.pill)
                    .softShadow()
                    .background(MaterialTheme.colorScheme.surfaceContainerLowest)
                    .padding(horizontal = Spacing.sm, vertical = Spacing.xs),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                if (chipIcon != null) {
                    Icon(
                        imageVector = chipIcon,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(Dimensions.iconSizeSmall),
                    )
                    Spacer(modifier = Modifier.size(Spacing.xxs))
                }
                Text(
                    text = chipText,
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                )
            }
        }
    }
}
