package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val KabadiWalaLightColorScheme = lightColorScheme(
    primary = GreenPrimary,
    onPrimary = Color.White,
    primaryContainer = GreenContainer,
    onPrimaryContainer = GreenOnContainer,
    secondary = GreenPrimaryLight,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFC8E6C9),
    onSecondaryContainer = GreenPrimaryDark,
    tertiary = SaffronAccent,
    onTertiary = Color.White,
    tertiaryContainer = AmberContainer,
    onTertiaryContainer = Color(0xFF4E2600),
    background = Color.White,
    onBackground = TextDark,
    surface = Color.White,
    onSurface = TextDark,
    surfaceVariant = Color(0xFFF9FAF9),
    onSurfaceVariant = TextMedium,
    outline = OutlineWarm
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = false, // Always keep bright clean white background as requested
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = KabadiWalaLightColorScheme,
        typography = Typography,
        content = content
    )
}
