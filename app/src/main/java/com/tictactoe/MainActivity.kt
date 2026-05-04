package com.tictactoe

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tictactoe.ui.components.Board
import com.tictactoe.ui.components.Scoreboard
import com.tictactoe.ui.theme.DarkTicTacToeColors
import com.tictactoe.ui.theme.LightTicTacToeColors
import com.tictactoe.ui.theme.TicTacToeTheme
import com.tictactoe.viewmodel.TicTacToeViewModel
import androidx.compose.foundation.isSystemInDarkTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            TicTacToeTheme(
                darkTheme = isSystemInDarkTheme(),
                dynamicColor = true
            ) {
                TicTacToeApp()
            }
        }
    }
}

@Composable
fun TicTacToeApp(
    viewModel: TicTacToeViewModel = viewModel()
) {
    val gameState by viewModel.gameState.collectAsState()
    val isDarkTheme = isSystemInDarkTheme()

    val ticTacToeColors = if (isDarkTheme) DarkTicTacToeColors else LightTicTacToeColors

    val backgroundGradient = Brush.verticalGradient(
        colors = if (isDarkTheme) {
            listOf(
                MaterialTheme.colorScheme.background,
                MaterialTheme.colorScheme.surface.copy(alpha = 0.95f),
                MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
            )
        } else {
            listOf(
                MaterialTheme.colorScheme.background,
                MaterialTheme.colorScheme.surface.copy(alpha = 0.97f),
                MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f)
            )
        }
    )

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundGradient)
                .systemBarsPadding()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .statusBarsPadding()
                    .padding(vertical = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Header()

                Scoreboard(
                    gameState = gameState,
                    xColor = ticTacToeColors.xColor,
                    oColor = ticTacToeColors.oColor,
                    onResetClick = { viewModel.resetGame() }
                )

                Board(
                    gameState = gameState,
                    xColor = ticTacToeColors.xColor,
                    oColor = ticTacToeColors.oColor,
                    onCellClick = { position ->
                        viewModel.makeMove(position)
                    }
                )

                TurnIndicator(
                    currentPlayer = gameState.currentPlayer.displayName,
                    winner = gameState.winner,
                    isDraw = gameState.isDraw,
                    xColor = ticTacToeColors.xColor,
                    oColor = ticTacToeColors.oColor
                )

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
private fun Header() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Tic Tac Toe",
            style = MaterialTheme.typography.headlineLarge.copy(
                fontWeight = FontWeight.Bold,
                fontWeight = FontWeight.ExtraBold
            ),
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "Classic Game",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun TurnIndicator(
    currentPlayer: String,
    winner: com.tictactoe.viewmodel.Player?,
    isDraw: Boolean,
    xColor: androidx.compose.ui.graphics.Color,
    oColor: androidx.compose.ui.graphics.Color
) {
    val text = when {
        winner != null -> "Winner: ${winner.displayName}"
        isDraw -> "It's a Draw!"
        else -> "$currentPlayer's Turn"
    }

    val textColor = when {
        winner == com.tictactoe.viewmodel.Player.X -> xColor
        winner == com.tictactoe.viewmodel.Player.O -> oColor
        isDraw -> MaterialTheme.colorScheme.tertiary
        else -> MaterialTheme.colorScheme.onSurface
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .height(48.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.SemiBold
            ),
            color = textColor
        )
    }
}