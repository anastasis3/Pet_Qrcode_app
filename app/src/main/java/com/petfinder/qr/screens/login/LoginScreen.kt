package com.petfinder.qr.screens.login

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
fun LoginScreen(
    isLoading: Boolean = false,
    errorMessage: String? = null,
    onSignIn: (email: String, password: String) -> Unit = { _, _ -> },
    onForgotPassword: () -> Unit = {},
    onCreateAccount: () -> Unit = {},
) {
    var email by remember { mutableStateOf("owner@example.com") }
    var password by remember { mutableStateOf("password") }

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

            // Brand mark
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
                text = "PetFinder QR",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Bold,
            )
            VGap(Spacing.xs)
            Text(
                text = "Reuniting families with a simple scan.\nWelcome back.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
            )

            VGap(Spacing.xl)

            // Form card
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(ComponentShapes.large)
                    .softShadow()
                    .background(MaterialTheme.colorScheme.surfaceContainerLowest)
                    .padding(Spacing.xl),
            ) {
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
                    trailingIcon = AppIcons.Lock,
                    visualTransformation = PasswordVisualTransformation(),
                    shape = ComponentShapes.pill,
                    backgroundColor = MaterialTheme.colorScheme.surfaceContainer,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                )
                VGap(Spacing.xs)
                Text(
                    text = "Forgot Password?",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier
                        .align(Alignment.End)
                        .clip(ComponentShapes.extraSmall)
                        .clickable(onClick = onForgotPassword)
                        .padding(Spacing.xxs),
                )
                if (errorMessage != null) {
                    VGap(Spacing.sm)
                    Text(
                        text = errorMessage,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.error,
                    )
                }
                VGap(Spacing.md)
                PrimaryButton(
                    text = "Sign In",
                    onClick = { onSignIn(email, password) },
                    isLoading = isLoading,
                    enabled = !isLoading,
                )
            }

            VGap(Spacing.xl)
            Text(
                text = "Don't have an account?",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            VGap(Spacing.sm)
            SecondaryButton(text = "Create Account", onClick = onCreateAccount)

            VGap(Spacing.sectionMargin)
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun LoginScreenPreview() {
    PetFinderTheme {
        LoginScreen()
    }
}
