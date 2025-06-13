package com.example.scribble_app.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Light theme colors
private val LightColors = lightColorScheme(
    primary = Color(0xFF667eea),
    secondary = Color(0xFF764ba2),
    tertiary = Color(0xFF4ECDC4),
    background = Color(0xFFFAFAFA),
    surface = Color.White,
    surfaceVariant = Color(0xFFF5F5F5),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    onSurfaceVariant = Color(0xFF49454F),
    outline = Color(0xFF79747E),
    outlineVariant = Color(0xFFCAC4D0),
    scrim = Color(0xFF000000),
    inverseSurface = Color(0xFF313033),
    inverseOnSurface = Color(0xFFF4EFF4),
    inversePrimary = Color(0xFFBEC2FF),
    surfaceDim = Color(0xFFDDD8E0),
    surfaceBright = Color(0xFFFDF8FF),
    surfaceContainerLowest = Color.White,
    surfaceContainerLow = Color(0xFFF7F2FA),
    surfaceContainer = Color(0xFFF1ECF4),
    surfaceContainerHigh = Color(0xFFEBE6EE),
    surfaceContainerHighest = Color(0xFFE6E0E9)
)

// Dark theme colors
private val DarkColors = darkColorScheme(
    primary = Color(0xFF8B9FFF),
    secondary = Color(0xFF9B8FFF),
    tertiary = Color(0xFF7FDBDA),
    background = Color(0xFF0F0F23),
    surface = Color(0xFF1A1A2E),
    surfaceVariant = Color(0xFF2A2A3E),
    onPrimary = Color(0xFF1A1A2E),
    onSecondary = Color(0xFF1A1A2E),
    onTertiary = Color(0xFF0F0F23),
    onBackground = Color(0xFFE6E1E5),
    onSurface = Color(0xFFE6E1E5),
    onSurfaceVariant = Color(0xFFCAC4D0),
    outline = Color(0xFF938F99),
    outlineVariant = Color(0xFF49454F),
    scrim = Color(0xFF000000),
    inverseSurface = Color(0xFFE6E1E5),
    inverseOnSurface = Color(0xFF313033),
    inversePrimary = Color(0xFF667eea),
    surfaceDim = Color(0xFF16213E),
    surfaceBright = Color(0xFF2A2A3E),
    surfaceContainerLowest = Color(0xFF0A0A1E),
    surfaceContainerLow = Color(0xFF1A1A2E),
    surfaceContainer = Color(0xFF1E1E33),
    surfaceContainerHigh = Color(0xFF28283D),
    surfaceContainerHighest = Color(0xFF333348)
)

@Composable
fun DrawingInJetpackComposeTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColors else LightColors

    MaterialTheme(
        colorScheme = colorScheme,
        typography = androidx.compose.material3.Typography(),
        content = content
    )
}