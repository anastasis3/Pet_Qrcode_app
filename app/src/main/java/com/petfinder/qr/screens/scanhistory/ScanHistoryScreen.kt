package com.petfinder.qr.screens.scanhistory

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import coil.compose.AsyncImage
import com.petfinder.qr.components.BottomNavDestination
import com.petfinder.qr.components.BottomNavigation
import com.petfinder.qr.components.LocationMap
import com.petfinder.qr.components.SafeBadge
import com.petfinder.qr.components.TimelineNode
import com.petfinder.qr.components.TopBar
import com.petfinder.qr.components.TopBarIconAction
import com.petfinder.qr.components.VGap
import com.petfinder.qr.model.ScanEvent
import com.petfinder.qr.preview.SampleData
import com.petfinder.qr.theme.AppIcons
import com.petfinder.qr.theme.ComponentShapes
import com.petfinder.qr.theme.Dimensions
import com.petfinder.qr.theme.PetFinderTheme
import com.petfinder.qr.theme.Spacing
import com.petfinder.qr.theme.softShadow

@Composable
fun ScanHistoryScreen(
    events: List<ScanEvent> = SampleData.scanHistory,
    onBack: () -> Unit = {},
    onDownloadReport: () -> Unit = {},
    onNavigate: (BottomNavDestination) -> Unit = {},
) {
    Scaffold(
        topBar = {
            TopBar(
                title = "Scan History",
                showLogo = false,
                showBackButton = true,
                onBackClick = onBack,
                actions = {
                    TopBarIconAction(
                        icon = AppIcons.MoreVert,
                        contentDescription = "More options",
                        onClick = {},
                    )
                },
            )
        },
        bottomBar = {
            BottomNavigation(
                selectedDestination = null,
                onDestinationSelected = onNavigate,
            )
        },
        containerColor = MaterialTheme.colorScheme.background,
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = Spacing.containerPadding),
        ) {
            VGap(Spacing.xs)
            Text(
                text = "Track Activity",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Bold,
            )
            VGap(Spacing.xxs)
            Text(
                text = "See exactly when and where your pet's tag was recently scanned.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )

            VGap(Spacing.xl)
            events.forEachIndexed { index, event ->
                TimelineNode(
                    isActive = event.isActive,
                    isLast = index == events.lastIndex,
                ) {
                    ScanEventCard(event)
                }
            }

            VGap(Spacing.sm)
            EndOfHistory(onDownloadReport = onDownloadReport)

            VGap(Spacing.xl)
        }
    }
}

@Composable
private fun ScanEventCard(event: ScanEvent) {
    val elevated = event.isActive || event.showMap ||
        event.scannedBy != null || event.note != null

    val cardModifier = if (elevated) {
        Modifier
            .fillMaxWidth()
            .clip(ComponentShapes.card)
            .softShadow()
            .background(MaterialTheme.colorScheme.surfaceContainerLowest)
            .padding(Spacing.md)
    } else {
        Modifier
            .fillMaxWidth()
            .padding(Spacing.md)
    }

    Column(modifier = cardModifier) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = event.dateLabel,
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold,
                )
                VGap(Spacing.xxs)
                Text(
                    text = event.place,
                    style = MaterialTheme.typography.headlineSmall,
                    color = if (event.isActive) {
                        MaterialTheme.colorScheme.onSurface
                    } else {
                        MaterialTheme.colorScheme.onSurfaceVariant
                    },
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    text = event.timeDetail,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            if (event.isNew) {
                SafeBadge(label = "NEW", showIcon = false)
            }
        }

        if (event.showMap && event.hasLocation) {
            VGap(Spacing.md)
            LocationMap(
                latitude = event.latitude!!,
                longitude = event.longitude!!,
                height = Dimensions.mapPreviewHeightCompact,
                markerTitle = event.place,
                chipText = "VIEW MAP",
                chipIcon = AppIcons.Location,
                chipAlignment = Alignment.BottomEnd,
            )
        }

        if (event.scannedBy != null) {
            VGap(Spacing.md)
            Row(verticalAlignment = Alignment.CenterVertically) {
                AsyncImage(
                    model = SampleData.luna.imageUrl,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(Dimensions.avatarSizeSmall)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surfaceContainerHigh),
                )
                Spacer(modifier = Modifier.size(Spacing.sm))
                Column {
                    Text(
                        text = "Scanned by: \"${event.scannedBy}\"",
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                    if (event.note != null) {
                        Text(
                            text = event.note,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }
            }
        } else if (event.note != null) {
            VGap(Spacing.sm)
            Text(
                text = event.note,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontStyle = FontStyle.Italic,
            )
        }
    }
}

@Composable
private fun EndOfHistory(onDownloadReport: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            modifier = Modifier
                .size(Dimensions.iconContainerSize)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surfaceContainerHigh),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = AppIcons.History,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(Dimensions.iconSizeDefault),
            )
        }
        VGap(Spacing.sm)
        Text(
            text = "End of recent history",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        VGap(Spacing.xs)
        Text(
            text = "Download Full Activity Report",
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .clip(ComponentShapes.extraSmall)
                .clickable(onClick = onDownloadReport)
                .padding(Spacing.xxs),
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun ScanHistoryScreenPreview() {
    PetFinderTheme {
        ScanHistoryScreen()
    }
}
