package com.tictactoe.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.tictactoe.viewmodel.GameState

@Composable
fun PixelBoard(
    gameState: GameState,
    xColor: Color,
    oColor: Color,
    onCellClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val gridColor = Color(0xFF000000)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
    ) {
        PixelGridLines(
            gridColor = gridColor,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .padding(8.dp)
        ) {
            for (row in 0 until 3) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    for (col in 0 until 3) {
                        val position = row * 3 + col
                        val isWinningCell = position in gameState.winningLine

                        PixelGameCell(
                            symbol = gameState.board[position],
                            isWinningCell = isWinningCell,
                            xColor = xColor,
                            oColor = oColor,
                            onClick = { onCellClick(position) },
                            enabled = gameState.winner == null &&
                                    !gameState.isDraw &&
                                    !gameState.isProcessing,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }
    }

    AnimatedVisibility(
        visible = gameState.winner != null || gameState.isDraw,
        enter = fadeIn() + scaleIn(
            initialScale = 0.7f,
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessLow
            )
        )
    ) {
        Spacer(modifier = Modifier.height(12.dp))
    }
}

@Composable
private fun PixelGridLines(
    gridColor: Color,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier) {
        val cellSize = size.width / 3
        val lineWidth = 8f
        val pixelSize = 8f

        val bgColor = Color(0xFF2d2d2d)
        drawRect(
            color = bgColor,
            topLeft = Offset.Zero,
            size = size
        )

        for (i in 1..2) {
            val x = cellSize * i - lineWidth / 2
            for (dy in 0..(size.height / pixelSize).toInt()) {
                drawRect(
                    color = gridColor,
                    topLeft = Offset(x, dy * pixelSize),
                    size = androidx.compose.ui.geometry.Size(lineWidth, pixelSize)
                )
            }
        }

        for (i in 1..2) {
            val y = cellSize * i - lineWidth / 2
            for (dx in 0..(size.width / pixelSize).toInt()) {
                drawRect(
                    color = gridColor,
                    topLeft = Offset(dx * pixelSize, y),
                    size = androidx.compose.ui.geometry.Size(pixelSize, lineWidth)
                )
            }
        }
    }
}