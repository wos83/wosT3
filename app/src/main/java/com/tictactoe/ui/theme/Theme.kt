package com.tictactoe.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val XColor = Color(0xFF6366F1)
private val OColor = Color(0xFFEC4899)
private val XColorDark = Color(0xFF818CF8)
private val OColorDark = Color(0xFFF472B6)

val PlayerXColor: Color get() = XColor
val PlayerOColor: Color get() = OColor
val PlayerXColorDark: Color get() = XColorDark
val PlayerOColorDark: Color get() = OColorDark

private val RetroDarkColorScheme = darkColorScheme(
    primary = XColor,
    secondary = OColor,
    tertiary = Color(0xFFFFD700),
    background = Color(0xFF0F0F1A),
    surface = Color(0xFF1A1A2E),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.Black,
    onBackground = Color.White,
    onSurface = Color.White
)

private val RetroLightColorScheme = lightColorScheme(
    primary = XColor,
    secondary = OColor,
    tertiary = Color(0xFF06B6D4),
    background = Color(0xFF1A1A2E),
    surface = Color(0xFF16213E),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color.White,
    onSurface = Color.White
)

@Composable
fun TicTacToeTheme(
    darkTheme: Boolean = true,
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else lightColorScheme()
        }
        else -> RetroDarkColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}