package com.tictactoe.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tictactoe.model.Board
import com.tictactoe.model.Cell
import com.tictactoe.model.GameState
import com.tictactoe.model.Player

@Composable
fun GameBoard(
    board: Board,
    currentPlayer: Player,
    gameState: GameState,
    winningLine: List<Pair<Int, Int>>?,
    onCellClick: (Int, Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        GameStatusText(currentPlayer, gameState)

        BoardGrid(board, winningLine, onCellClick)
    }
}

@Composable
private fun GameStatusText(currentPlayer: Player, gameState: GameState) {
    val statusText = when (gameState) {
        GameState.ONGOING -> "PLAYER $currentPlayer TURN"
        GameState.WIN_X -> "PLAYER X WINS!"
        GameState.WIN_O -> "PLAYER O WINS!"
        GameState.DRAW -> "DRAW!"
    }

    val statusColor = when (gameState) {
        GameState.ONGOING -> if (currentPlayer == Player.X) Color(0xFF6366F1) else Color(0xFFEC4899)
        GameState.WIN_X -> Color(0xFF6366F1)
        GameState.WIN_O -> Color(0xFFEC4899)
        GameState.DRAW -> Color(0xFFFFD700)
    }

    val infiniteTransition = rememberInfiniteTransition(label = "blink")
    val alpha by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = if (gameState == GameState.ONGOING) 1f else 0.3f,
        animationSpec = infiniteRepeatable(
            animation = tween(500, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "blink_alpha"
    )

    Text(
        text = statusText,
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
        color = statusColor.copy(alpha = alpha),
        letterSpacing = 2.sp
    )
}

@Composable
private fun BoardGrid(
    board: Board,
    winningLine: List<Pair<Int, Int>>?,
    onCellClick: (Int, Int) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .shadow(8.dp, RoundedCornerShape(0.dp))
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFF1A1A2E), Color(0xFF16213E))
                )
            )
            .clip(RoundedCornerShape(0.dp))
            .padding(8.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            for (row in 0..2) {
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    for (col in 0..2) {
                        val cell = board.grid[row][col]
                        val isWinningCell = winningLine?.any { it.first == row && it.second == col } == true
                        val cellModifier = Modifier
                            .weight(1f)
                            .aspectRatio(1f)
                            .clickable { onCellClick(row, col) }

                        GameCell(
                            cell = cell,
                            isWinningCell = isWinningCell,
                            modifier = cellModifier
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun GameCell(
    cell: Cell,
    isWinningCell: Boolean,
    modifier: Modifier = Modifier
) {
    val cellColor = when {
        isWinningCell -> Color(0xFF2D2D4A)
        else -> Color(0xFF16213E)
    }

    val borderColor = if (isWinningCell) Color(0xFFFFD700) else Color(0xFF4A4A6A)

    Box(
        modifier = modifier
            .background(
                Brush.verticalGradient(
                    colors = listOf(cellColor, Color(0xFF0F172A))
                )
            )
            .background(Color(0xFF1A1A2E)),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize().padding(4.dp)) {
            drawCell(cell.player)
        }

        Canvas(modifier = Modifier.fillMaxSize()) {
            drawRect(
                color = borderColor,
                style = Stroke(width = 2f)
            )
        }
    }
}

private fun DrawScope.drawCell(player: Player?) {
    when (player) {
        Player.X -> {
            val color = Color(0xFF6366F1)
            val centerX = size.width / 2
            val centerY = size.height / 2
            val radius = minOf(size.width, size.height) * 0.35f

            drawLine(
                color = color,
                start = Offset(centerX - radius, centerY - radius),
                end = Offset(centerX + radius, centerY + radius),
                strokeWidth = 8f
            )
            drawLine(
                color = color,
                start = Offset(centerX + radius, centerY - radius),
                end = Offset(centerX - radius, centerY + radius),
                strokeWidth = 8f
            )
        }
        Player.O -> {
            val color = Color(0xFFEC4899)
            val centerX = size.width / 2
            val centerY = size.height / 2
            val radius = minOf(size.width, size.height) * 0.35f

            drawCircle(
                color = color,
                radius = radius,
                center = Offset(centerX, centerY),
                style = Stroke(width = 8f)
            )
        }
        null -> {}
    }
}