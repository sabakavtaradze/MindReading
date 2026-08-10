package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val ImmersiveDarkColorScheme = darkColorScheme(
    primary = NeuralAccent,
    onPrimary = NeuralDeepPurple,
    primaryContainer = NeuralDeepPurple,
    onPrimaryContainer = NeuralAccent,
    secondary = NeuralCardPurple,
    onSecondary = NeuralTextPrimary,
    background = NeuralBackground,
    onBackground = NeuralTextPrimary,
    surface = NeuralSurface,
    onSurface = NeuralTextPrimary,
    surfaceVariant = NeuralSurface,
    onSurfaceVariant = NeuralTextSecondary,
    outline = NeuralBorder
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = true,
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = ImmersiveDarkColorScheme,
        typography = Typography,
        content = content
    )
}
