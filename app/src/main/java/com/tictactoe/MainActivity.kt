package com.tictactoe

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tictactoe.ui.components.PixelBoard
import com.tictactoe.ui.components.PixelScoreboard
import com.tictactoe.ui.components.PixelText
import com.tictactoe.viewmodel.TicTacToeViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        setContent {
            val isDarkTheme = isSystemInDarkTheme()
            val pixelColors = if (isDarkTheme) PixelThemeColors.dark else PixelThemeColors.light

            PixelTicTacToeApp(
                viewModel = viewModel(),
                pixelColors = pixelColors,
                isDarkTheme = isDarkTheme
            )
        }
    }
}

@Composable
fun PixelTicTacToeApp(
    viewModel: TicTacToeViewModel,
    pixelColors: PixelColors,
    isDarkTheme: Boolean
) {
    val gameState by viewModel.gameState.collectAsState()

    val bgColor1 = if (isDarkTheme) Color(0xFF1a1a2e) else Color(0xFF87CEEB)
    val bgColor2 = if (isDarkTheme) Color(0xFF16213e) else Color(0xFFADD8E6)

    Box(modifier = Modifier.fillMaxSize()) {
        PixelBackground(
            color1 = bgColor1,
            color2 = bgColor2,
            modifier = Modifier.fillMaxSize()
        )

        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
                .padding(horizontal = 8.dp),
            contentAlignment = Alignment.Center
        ) {
            val screenHeight = maxHeight

            val titleHeight: Dp = (screenHeight * 0.15f).coerceAtMost(140.dp)
            val middleSpacer: Dp = (screenHeight * 0.02f).coerceIn(8.dp, 20.dp)

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(middleSpacer)
            ) {
                PixelTitle(
                    primaryColor = pixelColors.xColor,
                    secondaryColor = pixelColors.oColor,
                    tertiaryColor = pixelColors.oColor.copy(green = 0.8f),
                    modifier = Modifier.height(titleHeight)
                )

                PixelScoreboard(
                    gameState = gameState,
                    xColor = pixelColors.xColor,
                    oColor = pixelColors.oColor,
                    onResetClick = { viewModel.resetGame() },
                    onResetAllClick = { viewModel.resetAll() }
                )

                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    PixelBoard(
                        gameState = gameState,
                        xColor = pixelColors.xColor,
                        oColor = pixelColors.oColor,
                        onCellClick = { position ->
                            viewModel.makeMove(position)
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

@Composable
private fun PixelTitle(
    primaryColor: Color,
    secondaryColor: Color,
    tertiaryColor: Color,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(horizontalArrangement = Arrangement.Center) {
            PixelText(text = "T", color = primaryColor, modifier = Modifier.height(40.dp))
            PixelText(text = "I", color = secondaryColor, modifier = Modifier.height(40.dp))
            PixelText(text = "C", color = tertiaryColor, modifier = Modifier.height(40.dp))
            PixelText(text = " ", color = Color.Transparent, modifier = Modifier.height(40.dp))
            PixelText(text = "T", color = primaryColor, modifier = Modifier.height(40.dp))
            PixelText(text = "A", color = secondaryColor, modifier = Modifier.height(40.dp))
            PixelText(text = "C", color = tertiaryColor, modifier = Modifier.height(40.dp))
            PixelText(text = " ", color = Color.Transparent, modifier = Modifier.height(40.dp))
            PixelText(text = "T", color = primaryColor, modifier = Modifier.height(40.dp))
            PixelText(text = "O", color = secondaryColor, modifier = Modifier.height(40.dp))
            PixelText(text = "E", color = tertiaryColor, modifier = Modifier.height(40.dp))
        }

        Spacer(modifier = Modifier.height(4.dp))

        PixelDivider(
            width = 200.dp,
            color1 = primaryColor,
            color2 = secondaryColor,
            color3 = tertiaryColor
        )

        Spacer(modifier = Modifier.height(2.dp))

        Row(horizontalArrangement = Arrangement.Center) {
            PixelText(text = "J", color = tertiaryColor, modifier = Modifier.height(24.dp))
            PixelText(text = "O", color = secondaryColor, modifier = Modifier.height(24.dp))
            PixelText(text = "G", color = primaryColor, modifier = Modifier.height(24.dp))
            PixelText(text = "O", color = tertiaryColor, modifier = Modifier.height(24.dp))
            PixelText(text = " ", color = Color.Transparent, modifier = Modifier.height(24.dp))
            PixelText(text = "D", color = secondaryColor, modifier = Modifier.height(24.dp))
            PixelText(text = "A", color = primaryColor, modifier = Modifier.height(24.dp))
            PixelText(text = " ", color = Color.Transparent, modifier = Modifier.height(24.dp))
            PixelText(text = "V", color = tertiaryColor, modifier = Modifier.height(24.dp))
            PixelText(text = "E", color = secondaryColor, modifier = Modifier.height(24.dp))
            PixelText(text = "L", color = primaryColor, modifier = Modifier.height(24.dp))
            PixelText(text = "H", color = tertiaryColor, modifier = Modifier.height(24.dp))
            PixelText(text = "A", color = secondaryColor, modifier = Modifier.height(24.dp))
        }

        Spacer(modifier = Modifier.height(4.dp))

        PixelDivider(
            width = 160.dp,
            color1 = tertiaryColor,
            color2 = primaryColor,
            color3 = secondaryColor
        )
    }
}

@Composable
private fun PixelDivider(
    width: Dp,
    color1: Color,
    color2: Color,
    color3: Color
) {
    Canvas(
        modifier = Modifier
            .width(width)
            .height(6.dp)
    ) {
        val pixelSize = 6f
        val totalPixels = (size.width / pixelSize).toInt()

        for (i in 0 until totalPixels) {
            val x = i * pixelSize
            val color = when {
                i < totalPixels / 3 -> color1
                i < 2 * totalPixels / 3 -> color2
                else -> color3
            }
            drawRect(
                color = color,
                topLeft = Offset(x, 0f),
                size = androidx.compose.ui.geometry.Size(pixelSize, pixelSize)
            )
        }
    }
}

@Composable
private fun PixelBackground(
    color1: Color,
    color2: Color,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier) {
        val pixelSize = 16f

        var pixelIndex = 0
        for (y in 0..(size.height / pixelSize).toInt()) {
            for (x in 0..(size.width / pixelSize).toInt()) {
                val color = if ((pixelIndex + x + y) % 2 == 0) color1 else color2
                drawRect(
                    color = color,
                    topLeft = Offset(x * pixelSize, y * pixelSize),
                    size = androidx.compose.ui.geometry.Size(pixelSize, pixelSize)
                )
                pixelIndex++
            }
        }
    }
}

data class PixelColors(
    val xColor: Color,
    val oColor: Color,
    val background: Color
)

object PixelThemeColors {
    val dark = PixelColors(
        xColor = Color(0xFFFF6B6B),
        oColor = Color(0xFF64B5F6),
        background = Color(0xFF1a1a2e)
    )

    val light = PixelColors(
        xColor = Color(0xFFE94560),
        oColor = Color(0xFF00D9FF),
        background = Color(0xFF87CEEB)
    )
}