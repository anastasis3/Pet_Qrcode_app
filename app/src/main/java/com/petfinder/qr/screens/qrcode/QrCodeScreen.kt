package com.petfinder.qr.screens.qrcode

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.petfinder.qr.components.BottomNavDestination
import com.petfinder.qr.components.BottomNavigation
import com.petfinder.qr.components.QrCodeImage
import com.petfinder.qr.components.PrimaryButton
import com.petfinder.qr.components.SecondaryButton
import com.petfinder.qr.components.TopBar
import com.petfinder.qr.components.TopBarAccountAction
import com.petfinder.qr.components.TopBarIconAction
import com.petfinder.qr.components.VGap
import com.petfinder.qr.theme.AppIcons
import com.petfinder.qr.theme.ComponentShapes
import com.petfinder.qr.theme.Dimensions
import com.petfinder.qr.theme.PetFinderTheme
import com.petfinder.qr.theme.QrPanelBg
import com.petfinder.qr.theme.Spacing
import com.petfinder.qr.theme.softShadow

@Composable
fun QrCodeScreen(
    qrContent: String = "https://petfinder.app/pet/sample",
    onDownload: () -> Unit = {},
    onShare: () -> Unit = {},
    onPrint: () -> Unit = {},
    onHelp: () -> Unit = {},
    onNavigate: (BottomNavDestination) -> Unit = {},
) {
    Scaffold(
        topBar = {
            TopBar(
                actions = {
                    TopBarIconAction(
                        icon = AppIcons.Notifications,
                        contentDescription = "Notifications",
                        onClick = {},
                    )
                    TopBarAccountAction(onClick = {})
                },
            )
        },
        bottomBar = {
            BottomNavigation(
                selectedDestination = BottomNavDestination.AddPet,
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
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            VGap(Spacing.lg)

            // QR card
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(ComponentShapes.large)
                    .softShadow()
                    .background(MaterialTheme.colorScheme.surfaceContainerLowest)
                    .padding(Spacing.xl),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(ComponentShapes.card)
                        .background(QrPanelBg)
                        .padding(Spacing.xl),
                    contentAlignment = Alignment.Center,
                ) {
                    Box(
                        modifier = Modifier
                            .clip(ComponentShapes.medium)
                            .softShadow()
                            .background(MaterialTheme.colorScheme.surfaceContainerLowest)
                            .padding(Spacing.md),
                    ) {
                        QrCodeImage(content = qrContent, size = Dimensions.qrCodeSize)
                    }
                }

                VGap(Spacing.lg)
                Text(
                    text = "Ready to Secure",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Bold,
                )
                VGap(Spacing.xs)
                Text(
                    text = "Attach this QR code to your pet's collar to help them find " +
                        "their way home.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                )
            }

            VGap(Spacing.xl)
            PrimaryButton(
                text = "Download QR",
                onClick = onDownload,
                icon = AppIcons.Download,
                useGradient = false,
            )

            VGap(Spacing.md)
            Row(horizontalArrangement = Arrangement.spacedBy(Spacing.md)) {
                SecondaryButton(
                    text = "Share QR",
                    onClick = onShare,
                    icon = AppIcons.Share,
                    modifier = Modifier.weight(1f),
                )
                SecondaryButton(
                    text = "Print QR",
                    onClick = onPrint,
                    icon = AppIcons.Print,
                    modifier = Modifier.weight(1f),
                )
            }

            VGap(Spacing.lg)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clip(ComponentShapes.extraSmall)
                    .clickable(onClick = onHelp)
                    .padding(Spacing.xxs),
            ) {
                Icon(
                    imageVector = AppIcons.Help,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(Dimensions.iconSizeSmall),
                )
                Spacer(modifier = Modifier.size(Spacing.xs))
                Text(
                    text = "How do I attach this?",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }

            VGap(Spacing.xl)
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun QrCodeScreenPreview() {
    PetFinderTheme {
        QrCodeScreen()
    }
}
