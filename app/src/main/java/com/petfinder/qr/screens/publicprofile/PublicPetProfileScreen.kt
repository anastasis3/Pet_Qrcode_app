package com.petfinder.qr.screens.publicprofile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.petfinder.qr.components.AlertBanner
import com.petfinder.qr.components.LabeledInfoBox
import com.petfinder.qr.components.MapPreview
import com.petfinder.qr.components.SafeBadge
import com.petfinder.qr.components.SoftActionButton
import com.petfinder.qr.components.VGap
import com.petfinder.qr.model.PetUiModel
import com.petfinder.qr.preview.SampleData
import com.petfinder.qr.theme.AboutMeBg
import com.petfinder.qr.theme.AppIcons
import com.petfinder.qr.theme.ComponentShapes
import com.petfinder.qr.theme.Dimensions
import com.petfinder.qr.theme.PetFinderTheme
import com.petfinder.qr.theme.SoftActionBlue
import com.petfinder.qr.theme.SoftActionPeach
import com.petfinder.qr.theme.SoftActionPurple
import com.petfinder.qr.theme.Spacing
import com.petfinder.qr.components.TopBar
import com.petfinder.qr.components.TopBarIconAction

@Composable
fun PublicPetProfileScreen(
    pet: PetUiModel = SampleData.luna.copy(name = "Rex", ageText = "4 Years"),
    onCallOwner: () -> Unit = {},
    onEmailOwner: () -> Unit = {},
    onSendLocation: () -> Unit = {},
    onShare: () -> Unit = {},
) {
    Scaffold(
        topBar = {
            TopBar(
                actions = {
                    TopBarIconAction(
                        icon = AppIcons.Share,
                        contentDescription = "Share",
                        onClick = onShare,
                    )
                },
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
            AlertBanner(text = "If I'm lost, please contact my owner.")

            VGap(Spacing.md)
            // Hero card
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(ComponentShapes.large)
                    .background(MaterialTheme.colorScheme.surfaceContainerLowest),
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(280.dp),
                ) {
                    AsyncImage(
                        model = pet.imageUrl,
                        contentDescription = pet.name,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(280.dp)
                            .background(MaterialTheme.colorScheme.surfaceContainerHigh),
                    )
                    SafeBadge(
                        label = "Lost Status",
                        showIcon = false,
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(Spacing.md),
                    )
                }

                Column(modifier = Modifier.padding(Spacing.lg)) {
                    Text(
                        text = "Hi! My name is ${pet.name} 🐶",
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.Bold,
                    )
                    VGap(Spacing.sm)
                    Text(
                        text = "I'm a friendly pup who might have wandered a bit too far. " +
                            "Thank you for scanning my tag!",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )

                    VGap(Spacing.md)
                    Row(horizontalArrangement = Arrangement.spacedBy(Spacing.md)) {
                        LabeledInfoBox(
                            label = "Breed",
                            value = pet.breed,
                            modifier = Modifier.weight(1f),
                        )
                        LabeledInfoBox(
                            label = "Age",
                            value = pet.ageText,
                            modifier = Modifier.weight(1f),
                        )
                    }
                    VGap(Spacing.md)
                    LabeledInfoBox(
                        label = "City",
                        value = "San Francisco",
                        modifier = Modifier.fillMaxWidth(),
                    )

                    VGap(Spacing.md)
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(ComponentShapes.card)
                            .background(AboutMeBg)
                            .padding(Spacing.md),
                    ) {
                        Text(
                            text = "ABOUT ME",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                        VGap(Spacing.xs)
                        Text(
                            text = "I am very friendly and love treats! I have a microchip, but " +
                                "my family misses me a lot. I might be a little scared, so please " +
                                "approach me slowly. I love belly rubs.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface,
                        )
                    }
                }
            }

            VGap(Spacing.md)
            SoftActionButton(
                icon = AppIcons.Call,
                label = "Call Owner",
                containerColor = SoftActionBlue,
                contentColor = MaterialTheme.colorScheme.onSurface,
                onClick = onCallOwner,
            )
            VGap(Spacing.sm)
            SoftActionButton(
                icon = AppIcons.Email,
                label = "Email Owner",
                containerColor = SoftActionPurple,
                contentColor = MaterialTheme.colorScheme.onSurface,
                onClick = onEmailOwner,
            )
            VGap(Spacing.sm)
            SoftActionButton(
                icon = AppIcons.Location,
                label = "Send My Location",
                containerColor = SoftActionPeach,
                contentColor = MaterialTheme.colorScheme.onSurface,
                onClick = onSendLocation,
            )

            VGap(Spacing.lg)
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = AppIcons.Explore,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(Dimensions.iconSizeDefault),
                )
                Spacer(modifier = Modifier.size(Spacing.xs))
                Text(
                    text = "Last Known Scan Location",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Bold,
                )
            }

            VGap(Spacing.md)
            MapPreview(
                height = 200.dp,
                chipText = "Scanned near Golden Gate Park",
                chipAlignment = Alignment.BottomStart,
            )

            VGap(Spacing.md)
            Text(
                text = "* Privacy note: Exact home addresses are hidden for safety.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontStyle = FontStyle.Italic,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
            )

            VGap(Spacing.md)
            Row(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .clip(ComponentShapes.pill)
                    .background(MaterialTheme.colorScheme.surfaceContainerHigh)
                    .padding(horizontal = Spacing.md, vertical = Spacing.sm),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    imageVector = AppIcons.Shield,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(Dimensions.iconSizeMedium),
                )
                Spacer(modifier = Modifier.size(Spacing.xs))
                Text(
                    text = "Secure Pet Identification System",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }

            VGap(Spacing.xxl)
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun PublicPetProfileScreenPreview() {
    PetFinderTheme {
        PublicPetProfileScreen()
    }
}
