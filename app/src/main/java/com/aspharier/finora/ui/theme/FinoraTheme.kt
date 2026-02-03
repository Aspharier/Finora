package com.aspharier.finora.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme =
        darkColorScheme(
                primary = NeonGreen,
                onPrimary = DarkBackground,
                background = DarkBackground,
                onBackground = PureWhite,
                surface = DarkSurface,
                onSurface = PureWhite,
                surfaceVariant = DarkSurfaceVariant,
                onSurfaceVariant = PureWhite.copy(alpha = 0.7f),
                secondary = OceanGreen,
                tertiary = MintGreen,
                error = ErrorRed
        )

private val LightColorScheme =
        lightColorScheme(
                primary = NeonGreen,
                onPrimary = DarkText,
                background = LightBackground,
                onBackground = DarkText,
                surface = LightSurface,
                onSurface = DarkText,
                surfaceVariant = LightSurfaceVariant,
                onSurfaceVariant = DarkTextSecondary,
                secondary = OceanGreen,
                tertiary = MintGreen,
                error = ErrorRed
        )

@Composable
fun FinTrackTheme(isDarkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colorScheme = if (isDarkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
            colorScheme = colorScheme,
            typography = FinTrackTypography,
            shapes = FinTrackShapes,
            content = content
    )
}
