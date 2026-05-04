package com.tictactoe.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.compose.foundation.shape.RoundedCornerShape

private val XColor = Color(0xFFE53935)
private val OColor = Color(0xFF1E88E5)
private val XColorDark = Color(0xFFFF6B6B)
private val OColorDark = Color(0xFF64B5F6)

private val Purple80 = Color(0xFFD0BCFF)
private val PurpleGrey80 = Color(0xFFCCC2DC)
private val Pink80 = Color(0xFFEFB8C8)

private val Purple40 = Color(0xFF6750A4)
private val PurpleGrey40 = Color(0xFF625B71)
private val Pink40 = Color(0xFF7D5260)

private val DarkSurface = Color(0xFF1C1B1F)
private val DarkBackground = Color(0xFF121212)
private val LightSurface = Color(0xFFFFFBFE)
private val LightBackground = Color(0xFFF5F5F5)

private val GlassEffectDark = Color(0x40FFFFFF)
private val GlassEffectLight = Color(0x20000000)

private val MetallicGradientDark = Brush.linearGradient(
    colors = listOf(
        Color(0xFF2D2D2D),
        Color(0xFF1A1A1A),
        Color(0xFF3D3D3D)
    )
)

private val MetallicGradientLight = Brush.linearGradient(
    colors = listOf(
        Color(0xFFF5F5F5),
        Color(0xFFE8E8E8),
        Color(0xFFFAFAFA)
    )
)

private val BrushedMetalGradient = Brush.sweepGradient(
    colors = listOf(
        Color(0xFFBDBDBD),
        Color(0xFFE0E0E0),
        Color(0xFFBDBDBD),
        Color(0xFF9E9E9E),
        Color(0xFFBDBDBD)
    )
)

val LightColorScheme = lightColorScheme(
    primary = Purple40,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFEADDFF),
    onPrimaryContainer = Color(0xFF21005D),
    secondary = PurpleGrey40,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFE8DEF8),
    onSecondaryContainer = Color(0xFF1D192B),
    tertiary = Pink40,
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFFFFD8E4),
    onTertiaryContainer = Color(0xFF31111D),
    error = Color(0xFFB3261E),
    onError = Color.White,
    errorContainer = Color(0xFFF9DEDC),
    onErrorContainer = Color(0xFF410E0B),
    background = LightBackground,
    onBackground = Color(0xFF1C1B1F),
    surface = LightSurface,
    onSurface = Color(0xFF1C1B1F),
    surfaceVariant = Color(0xFFE7E0EC),
    onSurfaceVariant = Color(0xFF49454F),
    outline = Color(0xFF79747E),
    outlineVariant = Color(0xFFCAC4D0),
    inverseSurface = Color(0xFF313033),
    inverseOnSurface = Color(0xFFF4EFF4),
    inversePrimary = Color(0xFFD0BCFF)
)

private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    onPrimary = Color(0xFF381E72),
    primaryContainer = Color(0xFF4F378B),
    onPrimaryContainer = Color(0xFFEADDFF),
    secondary = PurpleGrey80,
    onSecondary = Color(0xFF332D41),
    secondaryContainer = Color(0xFF4A4458),
    onSecondaryContainer = Color(0xFFE8DEF8),
    tertiary = Pink80,
    onTertiary = Color(0xFF492532),
    tertiaryContainer = Color(0xFF633B48),
    onTertiaryContainer = Color(0xFFFFD8E4),
    error = Color(0xFFF2B8B5),
    onError = Color(0xFF601410),
    errorContainer = Color(0xFF8C1D18),
    onErrorContainer = Color(0xFFF9DEDC),
    background = DarkBackground,
    onBackground = Color(0xFFE6E1E5),
    surface = DarkSurface,
    onSurface = Color(0xFFE6E1E5),
    surfaceVariant = Color(0xFF49454F),
    onSurfaceVariant = Color(0xFFCAC4D0),
    outline = Color(0xFF938F99),
    outlineVariant = Color(0xFF49454F),
    inverseSurface = Color(0xFFE6E1E5),
    inverseOnSurface = Color(0xFF313033),
    inversePrimary = Purple40
)

data class TicTacToeColors(
    val xColor: Color,
    val oColor: Color,
    val boardBackground: Brush,
    val cellBackground: Brush,
    val glassEffect: Color,
    val metallicHighlight: Brush
)

val LightTicTacToeColors = TicTacToeColors(
    xColor = XColor,
    oColor = OColor,
    boardBackground = MetallicGradientLight,
    cellBackground = Brush.verticalGradient(
        colors = listOf(
            Color(0xFFFFFFFF),
            Color(0xFFF0F0F0),
            Color(0xFFE5E5E5)
        )
    ),
    glassEffect = GlassEffectLight,
    metallicHighlight = BrushedMetalGradient
)

val DarkTicTacToeColors = TicTacToeColors(
    xColor = XColorDark,
    oColor = OColorDark,
    boardBackground = MetallicGradientDark,
    cellBackground = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF2D2D2D),
            Color(0xFF252525),
            Color(0xFF1F1F1F)
        )
    ),
    glassEffect = GlassEffectDark,
    metallicHighlight = BrushedMetalGradient
)

@Composable
fun TicTacToeTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

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
        typography = Typography,
        content = content
    )
}

object TicTacToeShapes {
    val boardShape = RoundedCornerShape(24.dp)
    val cellShape = RoundedCornerShape(16.dp)
    val buttonShape = RoundedCornerShape(12.dp)
    val scoreCardShape = RoundedCornerShape(20.dp)
}