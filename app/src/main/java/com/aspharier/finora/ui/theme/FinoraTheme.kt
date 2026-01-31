package com.aspharier.finora.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = NeonGreen,
    onPrimary = DarkSurface,

    background = Color(0xFF121212),
    onBackground = PureWhite,

    surface = DarkSurface,
    onSurface = PureWhite,

    secondary = OceanGreen,
    tertiary = MintGreen,

    error = ErrorRed
)

@Composable
fun FinTrackTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = FinTrackTypography,
        shapes = FinTrackShapes,
        content = content
    )
}