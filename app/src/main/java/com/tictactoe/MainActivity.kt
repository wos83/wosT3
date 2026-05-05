package com.tictactoe

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tictactoe.ui.components.Board
import com.tictactoe.ui.components.Scoreboard
import com.tictactoe.ui.theme.TicTacToeTheme
import com.tictactoe.viewmodel.TicTacToeViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
        setContent {
            TicTacToeTheme {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0xFF0F0F1A))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .statusBarsPadding()
                    ) {
                        val viewModel: TicTacToeViewModel = viewModel()
                        Scoreboard(
                            player1Score = viewModel.player1Score,
                            player2Score = viewModel.player2Score,
                            currentPlayer = viewModel.currentPlayer,
                            onRestart = { viewModel.resetGame() }
                        )
                        Board(
                            board = viewModel.board,
                            onCellClick = { index -> viewModel.makeMove(index) },
                            winningLine = viewModel.winningLine,
                            isGameOver = viewModel.isGameOver,
                            winner = viewModel.winner
                        )
                    }
                }
            }
        }
    }
}