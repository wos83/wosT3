package com.tictactoe.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tictactoe.viewmodel.Player

@Composable
fun Scoreboard(
    player1Score: Int,
    player2Score: Int,
    currentPlayer: Player,
    onRestart: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(Color(0xFF1A1A2E))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "JOGO DA VELHA",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFFFFD700),
            letterSpacing = 4.sp
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(0.dp))
                .background(Color(0xFF16213E))
                .border(2.dp, Color(0xFF4A4A6A))
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            PlayerScoreRetro(
                player = "X",
                score = player1Score,
                isActive = currentPlayer == Player.X,
                color = Color(0xFF6366F1)
            )
            Text(
                text = "VS",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF4A4A6A)
            )
            PlayerScoreRetro(
                player = "O",
                score = player2Score,
                isActive = currentPlayer == Player.O,
                color = Color(0xFFEC4899)
            )
        }

        Button(
            onClick = onRestart,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF4A4A6A)
            ),
            shape = RoundedCornerShape(0.dp)
        ) {
            Text(
                text = "NOVO JOGO",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
        }
    }
}

@Composable
private fun PlayerScoreRetro(
    player: String,
    score: Int,
    isActive: Boolean,
    color: Color
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "PLAYER $player",
            fontSize = 14.sp,
            color = if (isActive) color else Color(0xFF6A6A8A),
            fontWeight = if (isActive) FontWeight.Bold else FontWeight.Normal
        )
        Text(
            text = score.toString(),
            fontSize = 32.sp,
            color = color,
            fontWeight = FontWeight.Bold
        )
    }
}