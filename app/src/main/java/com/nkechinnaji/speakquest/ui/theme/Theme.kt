package com.nkechinnaji.speakquest.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = Pink50,
    onPrimary = Color.White,
    primaryContainer = Pink30,
    onPrimaryContainer = Pink90,
    secondary = OliveGray,
    onSecondary = Color.White,
    secondaryContainer = Olive40,
    onSecondaryContainer = Olive80,
    tertiary = Pink60,
    background = DarkSlate,
    onBackground = Pink90,
    surface = Charcoal,
    onSurface = Pink90,
    surfaceVariant = DarkSlate,
    onSurfaceVariant = Pink80,
    error = Error,
    onError = Color.White
)

private val LightColorScheme = lightColorScheme(
    primary = Pink40,
    onPrimary = Color.White,
    primaryContainer = Pink90,
    onPrimaryContainer = Pink30,
    secondary = OliveGray,
    onSecondary = Color.White,
    secondaryContainer = Olive80,
    onSecondaryContainer = Olive40,
    tertiary = Pink50,
    background = PinkCream,
    onBackground = Charcoal,
    surface = Color.White,
    onSurface = Charcoal,
    surfaceVariant = CardPink,
    onSurfaceVariant = Pink40,
    outline = Pink60,
    error = Error,
    onError = Color.White
)

@Composable
fun SpeakQuestTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = GradientStart.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
