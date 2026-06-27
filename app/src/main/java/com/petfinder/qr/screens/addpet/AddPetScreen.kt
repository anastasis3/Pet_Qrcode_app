package com.petfinder.qr.screens.addpet

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.petfinder.qr.components.BottomNavDestination
import com.petfinder.qr.components.BottomNavigation
import com.petfinder.qr.components.DropdownField
import com.petfinder.qr.components.InfoNote
import com.petfinder.qr.components.PhotoUploadBox
import com.petfinder.qr.components.PrimaryButton
import com.petfinder.qr.components.RoundedTextField
import com.petfinder.qr.components.SectionContainer
import com.petfinder.qr.components.SegmentedToggle
import com.petfinder.qr.components.ToggleOption
import com.petfinder.qr.components.TopBar
import com.petfinder.qr.components.TopBarAccountAction
import com.petfinder.qr.components.VGap
import com.petfinder.qr.model.PetFormData
import com.petfinder.qr.theme.AppIcons
import com.petfinder.qr.theme.ComponentShapes
import com.petfinder.qr.theme.PetFinderTheme
import com.petfinder.qr.theme.Spacing

@Composable
fun AddPetScreen(
    initial: PetFormData = PetFormData(),
    title: String = "Register Your Pet",
    onSave: (PetFormData) -> Unit = {},
    onNavigate: (BottomNavDestination) -> Unit = {},
) {
    // Field state is re-seeded whenever [initial] arrives (e.g. an edit load completes).
    var name by remember(initial) { mutableStateOf(initial.name) }
    var breed by remember(initial) { mutableStateOf(initial.breed) }
    var description by remember(initial) { mutableStateOf(initial.description) }
    var phone by remember(initial) { mutableStateOf(initial.phone) }
    var email by remember(initial) { mutableStateOf(initial.email) }
    var city by remember(initial) { mutableStateOf(initial.city) }
    var age by remember(initial) { mutableStateOf(initial.age) }
    var species by remember(initial) { mutableIntStateOf(if (initial.species == "Cat") 1 else 0) }

    val ageOptions = listOf("Puppy / Kitten", "Young", "Adult", "Senior")

    Scaffold(
        topBar = {
            TopBar(actions = { TopBarAccountAction(onClick = {}) })
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
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
            )
            VGap(Spacing.xs)
            Text(
                text = "Fill in the details to protect your furry friend. Saving will " +
                    "instantly generate a unique identification QR code.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
            )

            VGap(Spacing.xl)
            PhotoUploadBox()

            VGap(Spacing.lg)
            RoundedTextField(
                value = name,
                onValueChange = { name = it },
                label = "Pet Name",
                placeholder = "e.g. Buddy",
                shape = ComponentShapes.pill,
            )

            VGap(Spacing.lg)
            FieldLabel("Species")
            SegmentedToggle(
                options = listOf(
                    ToggleOption("Dog", AppIcons.Pets),
                    ToggleOption("Cat", AppIcons.Pets),
                ),
                selectedIndex = species,
                onSelect = { species = it },
                trackColor = androidx.compose.ui.graphics.Color.Transparent,
                selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                selectedContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                unselectedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
            )

            VGap(Spacing.lg)
            RoundedTextField(
                value = breed,
                onValueChange = { breed = it },
                label = "Breed",
                placeholder = "e.g. Golden Retriever",
                shape = ComponentShapes.pill,
            )

            VGap(Spacing.lg)
            DropdownField(
                value = age,
                label = "Age",
                onClick = {
                    val next = (ageOptions.indexOf(age).coerceAtLeast(0) + 1) % ageOptions.size
                    age = ageOptions[next]
                },
            )

            VGap(Spacing.lg)
            RoundedTextField(
                value = description,
                onValueChange = { description = it },
                label = "Description & Medical Info",
                placeholder = "Friendly, microchipped, needs daily medication...",
                singleLine = false,
                maxLines = 4,
                shape = ComponentShapes.medium,
            )

            VGap(Spacing.xl)
            SectionContainer(
                title = "Owner Contact",
                titleIcon = AppIcons.ContactEmergency,
            ) {
                RoundedTextField(
                    value = phone,
                    onValueChange = { phone = it },
                    label = "Phone Number",
                    placeholder = "+1 (555) 000-0000",
                    shape = ComponentShapes.pill,
                    useWhiteBackground = true,
                )
                VGap(Spacing.md)
                RoundedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = "Email Address",
                    placeholder = "owner@example.com",
                    shape = ComponentShapes.pill,
                    useWhiteBackground = true,
                )
                VGap(Spacing.md)
                RoundedTextField(
                    value = city,
                    onValueChange = { city = it },
                    label = "City",
                    placeholder = "City, State",
                    shape = ComponentShapes.pill,
                    useWhiteBackground = true,
                )
            }

            VGap(Spacing.md)
            InfoNote(
                text = "Saving this profile will generate a secure QR code for your pet's " +
                    "collar. Good samaritans can scan it to access these details and contact " +
                    "you instantly.",
                icon = AppIcons.QrCode,
            )

            VGap(Spacing.xl)
            PrimaryButton(
                text = "Save Pet & Generate QR",
                onClick = {
                    onSave(
                        PetFormData(
                            name = name,
                            species = if (species == 1) "Cat" else "Dog",
                            breed = breed,
                            age = age,
                            description = description,
                            phone = phone,
                            email = email,
                            city = city,
                            imageUrl = initial.imageUrl,
                        ),
                    )
                },
                icon = AppIcons.Save,
            )

            VGap(Spacing.xl)
        }
    }
}

@Composable
private fun FieldLabel(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.labelLarge,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = Modifier.padding(start = Spacing.xxs, bottom = Spacing.xs),
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun AddPetScreenPreview() {
    PetFinderTheme {
        AddPetScreen()
    }
}
