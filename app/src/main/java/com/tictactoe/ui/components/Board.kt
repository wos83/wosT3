package com.tictactoe.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tictactoe.viewmodel.CellState
import com.tictactoe.viewmodel.Player

@Composable
fun Board(
    board: List<CellState>,
    onCellClick: (Int) -> Unit,
    winningLine: List<Int>?,
    isGameOver: Boolean,
    winner: Player?,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (isGameOver) {
            val winnerText = when (winner) {
                Player.X -> "PLAYER X WINS!"
                Player.O -> "PLAYER O WINS!"
                null -> "DRAW!"
            }
            Text(
                text = winnerText,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = when (winner) {
                    Player.X -> Color(0xFF6366F1)
                    Player.O -> Color(0xFFEC4899)
                    null -> Color.White
                },
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .background(Color(0xFF1A1A2E))
                .border(4.dp, Color(0xFF4A4A6A))
        ) {
            Column(
                modifier = Modifier.fillMaxSize().padding(4.dp),
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                for (row in 0..2) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        for (col in 0..2) {
                            val index = row * 3 + col
                            GameCellRetro(
                                cellState = board[index],
                                isWinningCell = winningLine?.contains(index) == true,
                                onClick = { onCellClick(index) },
                                modifier = Modifier
                                    .weight(1f)
                                    .aspectRatio(1f)
                                    .padding(2.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun GameCellRetro(
    cellState: CellState,
    isWinningCell: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val borderColor = when {
        isWinningCell -> Color(0xFFFFD700)
        else -> Color(0xFF3D3D5C)
    }

    val cellColor = when {
        isWinningCell -> Color(0xFF2D2D4A)
        else -> Color(0xFF16213E)
    }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(0.dp))
            .background(cellColor)
            .border(3.dp, borderColor)
            .pointerInput(Unit) {
                detectTapGestures { onClick() }
            },
        contentAlignment = Alignment.Center
    ) {
        when (cellState) {
            CellState.X -> PixelX()
            CellState.O -> PixelO()
            CellState.EMPTY -> {}
        }
    }
}

@Composable
private fun PixelX() {
    Text(
        text = "X",
        fontSize = 48.sp,
        fontWeight = FontWeight.Bold,
        color = Color(0xFF6366F1)
    )
}

@Composable
private fun PixelO() {
    Text(
        text = "O",
        fontSize = 48.sp,
        fontWeight = FontWeight.Bold,
        color = Color(0xFFEC4899)
    )
}