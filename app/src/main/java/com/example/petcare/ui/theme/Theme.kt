package com.example.petcare.ui.theme

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

// Paleta de cores para o MODO CLARO
private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF26B6C4),            // --primary
    onPrimary = Color(0xFFFFFFFF),          // --primary-foreground
    secondary = Color(0xFF91D045),          // --secondary
    onSecondary = Color(0xFFFFFFFF),        // --secondary-foreground
    background = Color(0xFFEAF7F8),         // --background
    onBackground = Color(0xFF1B2D3D),       // --foreground
    surface = Color(0xFFFFFFFF),            // --card
    onSurface = Color(0xFF1B2D3D),          // --card-foreground
    error = Color(0xFFD4183D),              // --destructive
    onError = Color(0xFFFFFFFF)             // --destructive-foreground
)

// Paleta de cores para o MODO ESCURO
private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF26B6C4),            // --primary
    onPrimary = Color(0xFFFFFFFF),          // --primary-foreground
    secondary = Color(0xFF91D045),          // --secondary
    onSecondary = Color(0xFFFFFFFF),        // --secondary-foreground
    background = Color(0xFF1B2D3D),         // --background
    onBackground = Color(0xFFEAF7F8),       // --foreground
    surface = Color(0xFF2A3F52),            // --card
    onSurface = Color(0xFFEAF7F8),          // --card-foreground
    error = Color(0xFFD4183D),              // --destructive
    onError = Color(0xFFFFFFFF)             // --destructive-foreground
)

@Composable
fun PetCareTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
