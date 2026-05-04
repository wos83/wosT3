package com.tictactoe.ui.theme

import android.os.Build
import android.view.View
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

object PixelColors {
    // NES-inspired palette
    val Background = Color(0xFF0F0F23)
    val BackgroundLight = Color(0xFF1A1A2E)
    val Surface = Color(0xFF16213E)
    val SurfaceLight = Color(0xFF1F4068)

    val XColor = Color(0xFFE94560)
    val OColor = Color(0xFF00D9FF)

    val XColorDark = Color(0xFFFF6B6B)
    val OColorDark = Color(0xFF64B5F6)

    val Primary = Color(0xFFE94560)
    val PrimaryVariant = Color(0xFFFF6B8A)
    val Secondary = Color(0xFF00D9FF)
    val Accent = Color(0xFFFFD93D)

    val White = Color(0xFFFFFFFF)
    val Black = Color(0xFF000000)
    val Gray = Color(0xFF6B7280)

    val WinHighlight = Color(0xFFFFD93D)
    val DrawColor = Color(0xFF9CA3AF)
}

private val PixelDarkColorScheme = darkColorScheme(
    primary = PixelColors.Primary,
    onPrimary = PixelColors.White,
    primaryContainer = PixelColors.PrimaryVariant,
    onPrimaryContainer = PixelColors.Black,
    secondary = PixelColors.Secondary,
    onSecondary = PixelColors.Black,
    secondaryContainer = PixelColors.SurfaceLight,
    onSecondaryContainer = PixelColors.White,
    tertiary = PixelColors.Accent,
    onTertiary = PixelColors.Black,
    background = PixelColors.Background,
    onBackground = PixelColors.White,
    surface = PixelColors.Surface,
    onSurface = PixelColors.White,
    surfaceVariant = PixelColors.SurfaceLight,
    onSurfaceVariant = PixelColors.Gray,
    outline = PixelColors.Gray,
    outlineVariant = PixelColors.SurfaceLight
)

private val PixelLightColorScheme = lightColorScheme(
    primary = PixelColors.Primary,
    onPrimary = PixelColors.White,
    primaryContainer = PixelColors.PrimaryVariant,
    onPrimaryContainer = PixelColors.White,
    secondary = PixelColors.Secondary,
    onSecondary = PixelColors.White,
    secondaryContainer = PixelColors.SurfaceLight,
    onSecondaryContainer = PixelColors.Black,
    tertiary = PixelColors.Accent,
    onTertiary = PixelColors.Black,
    background = Color(0xFFF5F5F5),
    onBackground = PixelColors.Black,
    surface = Color(0xFFFFFFFF),
    onSurface = PixelColors.Black,
    surfaceVariant = Color(0xFFE5E5E5),
    onSurfaceVariant = Color(0xFF4B5563),
    outline = Color(0xFF9CA3AF),
    outlineVariant = Color(0xFFD1D5DB)
)

data class PixelGameColors(
    val xColor: Color,
    val oColor: Color,
    val background: Color,
    val surface: Color,
    val gridColor: Color
)

val PixelDarkColors = PixelGameColors(
    xColor = PixelColors.XColorDark,
    oColor = PixelColors.OColorDark,
    background = PixelColors.Background,
    surface = PixelColors.Surface,
    gridColor = PixelColors.Gray
)

val PixelLightColors = PixelGameColors(
    xColor = PixelColors.XColor,
    oColor = PixelColors.OColor,
    background = Color(0xFFF0F0F0),
    surface = Color(0xFFFFFFFF),
    gridColor = Color(0xFF9CA3AF)
)

@Composable
fun PixelTicTacToeTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) PixelDarkColorScheme else PixelLightColorScheme

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as? android.app.Activity)?.window
            window?.let {
                WindowCompat.getInsetsController(it, view).isAppearanceLightStatusBars = !darkTheme
                WindowCompat.getInsetsController(it, view).isAppearanceLightNavigationBars = !darkTheme
            }
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = PixelTypography,
        content = content
    )
}