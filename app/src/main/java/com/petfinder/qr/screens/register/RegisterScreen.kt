package com.petfinder.qr.screens.register

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.petfinder.qr.components.PrimaryButton
import com.petfinder.qr.components.RoundedTextField
import com.petfinder.qr.components.SecondaryButton
import com.petfinder.qr.components.VGap
import com.petfinder.qr.theme.AppIcons
import com.petfinder.qr.theme.ComponentShapes
import com.petfinder.qr.theme.Dimensions
import com.petfinder.qr.theme.PetFinderTheme
import com.petfinder.qr.theme.Spacing
import com.petfinder.qr.theme.softShadow

@Composable
fun RegisterScreen(
    isLoading: Boolean = false,
    errorMessage: String? = null,
    onCreateAccount: (name: String, email: String, password: String) -> Unit = { _, _, _ -> },
    onSignIn: () -> Unit = {},
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirm by remember { mutableStateOf("") }

    Scaffold(containerColor = MaterialTheme.colorScheme.background) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = Spacing.containerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            VGap(Spacing.sectionMargin)

            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = AppIcons.LogoFilled,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(Dimensions.iconSizeLarge),
                )
            }

            VGap(Spacing.md)
            Text(
                text = "Create Account",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Bold,
            )
            VGap(Spacing.xs)
            Text(
                text = "Join PetFinder QR to keep your\nfurry family safe and sound.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
            )

            VGap(Spacing.xl)

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(ComponentShapes.large)
                    .softShadow()
                    .background(MaterialTheme.colorScheme.surfaceContainerLowest)
                    .padding(Spacing.xl),
            ) {
                RoundedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = "Full Name",
                    placeholder = "Jane Doe",
                    trailingIcon = AppIcons.Account,
                    shape = ComponentShapes.pill,
                    backgroundColor = MaterialTheme.colorScheme.surfaceContainer,
                )
                VGap(Spacing.md)
                RoundedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = "Email Address",
                    placeholder = "hello@petowner.com",
                    trailingIcon = AppIcons.EmailAlt,
                    shape = ComponentShapes.pill,
                    backgroundColor = MaterialTheme.colorScheme.surfaceContainer,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                )
                VGap(Spacing.md)
                RoundedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = "Password",
                    placeholder = "Create a password",
                    trailingIcon = AppIcons.Lock,
                    visualTransformation = PasswordVisualTransformation(),
                    shape = ComponentShapes.pill,
                    backgroundColor = MaterialTheme.colorScheme.surfaceContainer,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                )
                VGap(Spacing.md)
                RoundedTextField(
                    value = confirm,
                    onValueChange = { confirm = it },
                    label = "Confirm Password",
                    placeholder = "Re-enter your password",
                    trailingIcon = AppIcons.Lock,
                    visualTransformation = PasswordVisualTransformation(),
                    shape = ComponentShapes.pill,
                    backgroundColor = MaterialTheme.colorScheme.surfaceContainer,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                )
                if (errorMessage != null) {
                    VGap(Spacing.sm)
                    Text(
                        text = errorMessage,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.error,
                    )
                }
                VGap(Spacing.lg)
                PrimaryButton(
                    text = "Create Account",
                    onClick = { onCreateAccount(name, email, password) },
                    isLoading = isLoading,
                    enabled = !isLoading,
                )
            }

            VGap(Spacing.xl)
            Text(
                text = "Already have an account?",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            VGap(Spacing.sm)
            SecondaryButton(text = "Sign In", onClick = onSignIn)

            VGap(Spacing.sectionMargin)
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun RegisterScreenPreview() {
    PetFinderTheme {
        RegisterScreen()
    }
}
