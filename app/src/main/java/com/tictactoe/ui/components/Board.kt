package com.tictactoe.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tictactoe.ui.theme.TicTacToeShapes
import com.tictactoe.viewmodel.GameState
import com.tictactoe.viewmodel.Player

@Composable
fun Board(
    gameState: GameState,
    xColor: Color,
    oColor: Color,
    onCellClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val boardGradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFFE8E8E8),
            Color(0xFFD0D0D0),
            Color(0xFFBEBEBE)
        )
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .shadow(
                elevation = 16.dp,
                shape = TicTacToeShapes.boardShape,
                ambientColor = Color.Black.copy(alpha = 0.2f),
                spotColor = Color.Black.copy(alpha = 0.3f)
            )
            .clip(TicTacToeShapes.boardShape)
            .background(boardGradient)
            .border(
                width = 2.dp,
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color.White.copy(alpha = 0.9f),
                        Color.Gray.copy(alpha = 0.3f),
                        Color.Gray.copy(alpha = 0.3f),
                        Color.White.copy(alpha = 0.5f)
                    )
                ),
                shape = TicTacToeShapes.boardShape
            )
            .padding(16.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            for (row in 0 until 3) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    for (col in 0 until 3) {
                        val position = row * 3 + col
                        val isWinningCell = position in gameState.winningLine

                        GameCell(
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
            initialScale = 0.8f,
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessLow
            )
        )
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        val message = when {
            gameState.isDraw -> "Draw!"
            gameState.winner != null -> "${gameState.winner!!.displayName} Wins!"
            else -> ""
        }

        val winnerColor = when (gameState.winner) {
            Player.X -> xColor
            Player.O -> oColor
            else -> MaterialTheme.colorScheme.primary
        }

        Text(
            text = message,
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 28.sp
            ),
            color = winnerColor,
            modifier = Modifier.padding(16.dp)
        )
    }
}