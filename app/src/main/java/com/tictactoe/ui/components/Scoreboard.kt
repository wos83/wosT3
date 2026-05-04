package com.tictactoe.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.tictactoe.viewmodel.GameState
import com.tictactoe.viewmodel.Player

@Composable
fun PixelScoreboard(
    gameState: GameState,
    xColor: Color,
    oColor: Color,
    onResetClick: () -> Unit,
    onResetAllClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            PixelScoreCard(
                player = Player.X,
                score = gameState.scoreX,
                color = xColor,
                isActive = gameState.currentPlayer == Player.X,
                modifier = Modifier.weight(1f)
            )

            Spacer(modifier = Modifier.width(8.dp))

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                PixelButton(
                    text = "NOVO",
                    onClick = onResetClick,
                    width = 70.dp,
                    height = 28.dp
                )

                PixelButton(
                    text = "ZERA",
                    onClick = onResetAllClick,
                    width = 70.dp,
                    height = 28.dp,
                    color = Color(0xFFff6b6b)
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            PixelScoreCard(
                player = Player.O,
                score = gameState.scoreO,
                color = oColor,
                isActive = gameState.currentPlayer == Player.O,
                modifier = Modifier.weight(1f)
            )
        }

        PixelStatusBar(
            winner = gameState.winner,
            isDraw = gameState.isDraw,
            currentPlayer = gameState.currentPlayer,
            xColor = xColor,
            oColor = oColor
        )
    }
}

@Composable
private fun PixelButton(
    text: String,
    onClick: () -> Unit,
    width: androidx.compose.ui.unit.Dp = 80.dp,
    height: androidx.compose.ui.unit.Dp = 36.dp,
    color: Color = Color(0xFF4a90d9)
) {
    Box(
        modifier = Modifier
            .width(width)
            .height(height)
            .background(color)
            .clickable { onClick() }
            .padding(vertical = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        PixelText(
            text = text,
            color = Color.White,
            modifier = Modifier.height(16.dp)
        )
    }
}

@Composable
private fun PixelStatusBar(
    winner: Player?,
    isDraw: Boolean,
    currentPlayer: Player,
    xColor: Color,
    oColor: Color
) {
    val statusText: String
    val statusColor: Color

    when {
        winner != null -> {
            statusText = "${winner.symbol}VENCEU"
            statusColor = if (winner == Player.X) xColor else oColor
        }
        isDraw -> {
            statusText = "EMPATE"
            statusColor = Color(0xFFffcc00)
        }
        else -> {
            statusText = "VEZ:${currentPlayer.symbol}"
            statusColor = Color(0xFFffffff)
        }
    }

    val infiniteTransition = rememberInfiniteTransition(label = "blink")
    val alpha = if (winner != null || isDraw) {
        infiniteTransition.animateFloat(
            initialValue = 1f,
            targetValue = 0.4f,
            animationSpec = infiniteRepeatable(
                animation = tween(400),
                repeatMode = RepeatMode.Reverse
            ),
            label = "blinkAlpha"
        ).value
    } else {
        1f
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF1a1a1a))
            .padding(vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        PixelText(
            text = " $statusText ",
            color = statusColor.copy(alpha = alpha),
            modifier = Modifier.height(20.dp)
        )
    }
}

@Composable
private fun PixelScoreCard(
    player: Player,
    score: Int,
    color: Color,
    isActive: Boolean,
    modifier: Modifier = Modifier
) {
    val scale by animateFloatAsState(
        targetValue = if (isActive) 1.05f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessHigh
        ),
        label = "scale"
    )

    val containerColor by animateColorAsState(
        targetValue = if (isActive) Color(0xFF3d3d3d) else Color(0xFF2d2d2d),
        label = "containerColor",
        animationSpec = tween(200)
    )

    val infiniteTransition = rememberInfiniteTransition(label = "active")
    val indicatorAlpha by infiniteTransition.animateFloat(
        initialValue = 0.5f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(500),
            repeatMode = RepeatMode.Reverse
        ),
        label = "indicatorAlpha"
    )

    Box(
        modifier = modifier
            .scale(scale)
            .background(containerColor)
            .padding(vertical = 10.dp, horizontal = 14.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                PixelText(
                    text = player.symbol.toString(),
                    color = color,
                    modifier = Modifier.size(28.dp)
                )

                if (isActive) {
                    Spacer(modifier = Modifier.width(4.dp))
                    PixelText(
                        text = "<",
                        color = color.copy(alpha = indicatorAlpha),
                        modifier = Modifier.size(12.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Canvas(modifier = Modifier.size(16.dp)) {
                    val pixelSize = 4f
                    for (i in 0..1) {
                        for (j in 0..1) {
                            drawRect(
                                color = color.copy(alpha = 0.3f),
                                topLeft = Offset(i * pixelSize * 2, j * pixelSize * 2),
                                size = androidx.compose.ui.geometry.Size(pixelSize * 1.5f, pixelSize * 1.5f)
                            )
                        }
                    }
                }

                PixelText(
                    text = score.toString(),
                    color = if (isActive) color else Color(0xFF888888),
                    modifier = Modifier.height(28.dp)
                )
            }

            Spacer(modifier = Modifier.height(2.dp))

            PixelText(
                text = if (player == Player.X) "P1" else "P2",
                color = Color(0xFF666666),
                modifier = Modifier.height(12.dp)
            )
        }
    }
}