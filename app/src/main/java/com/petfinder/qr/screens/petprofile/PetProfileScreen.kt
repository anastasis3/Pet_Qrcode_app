package com.petfinder.qr.screens.petprofile

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.petfinder.qr.components.BottomNavDestination
import com.petfinder.qr.components.BottomNavigation
import com.petfinder.qr.components.ContactInfoRow
import com.petfinder.qr.components.MapPreview
import com.petfinder.qr.components.PrimaryButton
import com.petfinder.qr.components.SafeBadge
import com.petfinder.qr.components.SecondaryButton
import com.petfinder.qr.components.SectionContainer
import com.petfinder.qr.components.SegmentedToggle
import com.petfinder.qr.components.ToggleOption
import com.petfinder.qr.components.TopBar
import com.petfinder.qr.components.TopBarIconAction
import com.petfinder.qr.components.VGap
import com.petfinder.qr.model.PetUiModel
import com.petfinder.qr.preview.SampleData
import com.petfinder.qr.theme.AppIcons
import com.petfinder.qr.theme.ComponentShapes
import com.petfinder.qr.theme.Dimensions
import com.petfinder.qr.theme.IconCircleBlue
import com.petfinder.qr.theme.IconCirclePeach
import com.petfinder.qr.theme.IconCirclePurple
import com.petfinder.qr.theme.PetFinderTheme
import com.petfinder.qr.theme.Spacing
import com.petfinder.qr.theme.softShadow

@Composable
fun PetProfileScreen(
    pet: PetUiModel = SampleData.buddyProfile,
    onBack: () -> Unit = {},
    onViewQr: () -> Unit = {},
    onEditInfo: () -> Unit = {},
    onNavigate: (BottomNavDestination) -> Unit = {},
) {
    var statusIndex by remember { mutableIntStateOf(0) }

    Scaffold(
        topBar = {
            TopBar(
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
            AsyncImage(
                model = pet.imageUrl,
                contentDescription = pet.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(340.dp)
                    .clip(ComponentShapes.large)
                    .background(MaterialTheme.colorScheme.surfaceContainerHigh),
            )

            VGap(Spacing.md)
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = pet.name,
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f),
                )
                SafeBadge(label = "Safe", showIcon = false)
            }
            VGap(Spacing.xxs)
            Text(
                text = "${pet.breed} • ${pet.ageText}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )

            VGap(Spacing.md)
            Text(
                text = pet.description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )

            VGap(Spacing.lg)
            SectionContainer(title = "Pet Status") {
                SegmentedToggle(
                    options = listOf(
                        ToggleOption("Safe", AppIcons.Safe),
                        ToggleOption("Lost", AppIcons.Lost),
                    ),
                    selectedIndex = statusIndex,
                    onSelect = { statusIndex = it },
                    trackColor = androidx.compose.ui.graphics.Color.Transparent,
                )
            }

            VGap(Spacing.lg)
            Row(horizontalArrangement = Arrangement.spacedBy(Spacing.md)) {
                PrimaryButton(
                    text = "View QR",
                    onClick = onViewQr,
                    icon = AppIcons.QrCode,
                    useGradient = false,
                    height = Dimensions.buttonHeightMedium,
                    modifier = Modifier.weight(1f),
                )
                SecondaryButton(
                    text = "Edit Info",
                    onClick = onEditInfo,
                    icon = AppIcons.Edit,
                    modifier = Modifier.weight(1f),
                )
            }

            VGap(Spacing.md)
            LastUpdateRow(text = "Last Update: ${pet.lastUpdate}")

            VGap(Spacing.lg)
            Text(
                text = "Contact Information",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Bold,
            )

            VGap(Spacing.md)
            ContactInfoRow(
                icon = AppIcons.Call,
                label = "Phone",
                value = pet.contact.phone,
                iconCircleColor = IconCircleBlue,
                iconTint = MaterialTheme.colorScheme.primary,
            )
            VGap(Spacing.sm)
            ContactInfoRow(
                icon = AppIcons.Email,
                label = "Email",
                value = pet.contact.email,
                iconCircleColor = IconCirclePurple,
                iconTint = MaterialTheme.colorScheme.secondary,
            )
            VGap(Spacing.sm)
            ContactInfoRow(
                icon = AppIcons.Location,
                label = "City",
                value = pet.contact.city,
                iconCircleColor = IconCirclePeach,
                iconTint = MaterialTheme.colorScheme.tertiary,
            )

            VGap(Spacing.lg)
            LastScanActivityCard(petName = pet.name)

            VGap(Spacing.xl)
        }
    }
}

@Composable
private fun LastUpdateRow(text: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(ComponentShapes.pill)
            .border(Dimensions.borderThin, MaterialTheme.colorScheme.outlineVariant, ComponentShapes.pill)
            .padding(horizontal = Spacing.md, vertical = Spacing.sm),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = AppIcons.Calendar,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(Dimensions.iconSizeSmall),
        )
        Spacer(modifier = Modifier.size(Spacing.xs))
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
        )
    }
}

@Composable
private fun LastScanActivityCard(petName: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(ComponentShapes.card)
            .softShadow()
            .background(MaterialTheme.colorScheme.surfaceContainerLowest)
            .padding(Spacing.md),
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Last Scan Activity",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    text = "Someone scanned $petName's tag recently",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = Spacing.xxs),
                )
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "2 hours ago",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary,
                    textAlign = TextAlign.End,
                )
                Text(
                    text = "Oct 26, 2:45 PM",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.End,
                )
            }
        }
        VGap(Spacing.md)
        MapPreview(
            height = Dimensions.mapPreviewHeightCompact,
            chipText = "Dolores Park Area, Mission District",
            chipIcon = AppIcons.Explore,
            chipAlignment = Alignment.BottomStart,
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun PetProfileScreenPreview() {
    PetFinderTheme {
        PetProfileScreen()
    }
}
