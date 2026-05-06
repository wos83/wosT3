package com.tictactoe

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.HapticFeedbackConstants
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tictactoe.model.GameState
import com.tictactoe.model.Player
import com.tictactoe.ui.components.GameBoard
import com.tictactoe.ui.components.Scoreboard
import com.tictactoe.ui.theme.TicTacToeTheme
import com.tictactoe.util.AIDifficulty
import kotlinx.coroutines.launch
import java.io.File

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )

        setContent {
            val context = LocalContext.current
            val view = LocalView.current
            val viewModel: GameViewModel = viewModel()

            viewModel.vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val vibratorManager = getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as android.os.VibratorManager
                vibratorManager.defaultVibrator
            } else {
                @Suppress("DEPRECATION")
                getSystemService(Context.VIBRATOR_SERVICE) as android.os.Vibrator
            }

            viewModel.storage = ScoreStorage(this@MainActivity)
            viewModel.localView = view

            try {
                viewModel.toneGenerator = android.media.ToneGenerator(
                    android.media.AudioManager.STREAM_MUSIC,
                    80
                )
            } catch (e: Exception) {
                // Ignore
            }

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
                        Scoreboard(
                            player1Score = viewModel.player1Score,
                            player2Score = viewModel.player2Score,
                            currentPlayer = viewModel.currentPlayer,
                            isPvPMode = viewModel.isPvPMode,
                            aiDifficulty = viewModel.aiDifficulty,
                            onRestart = { viewModel.resetGame() },
                            onModeChange = { viewModel.isPvPMode = !viewModel.isPvPMode },
                            onDifficultyChange = {
                                viewModel.aiDifficulty = when (viewModel.aiDifficulty) {
                                    AIDifficulty.EASY -> AIDifficulty.MEDIUM
                                    AIDifficulty.MEDIUM -> AIDifficulty.HARD
                                    AIDifficulty.HARD -> AIDifficulty.EASY
                                }
                            }
                        )
                        GameBoard(
                            board = viewModel.board,
                            currentPlayer = viewModel.currentPlayer,
                            gameState = viewModel.gameState,
                            winningLine = viewModel.winningLine,
                            onCellClick = { row, col -> viewModel.onCellClicked(row, col) }
                        )
                    }
                }
            }
        }
    }
}

class ScoreStorage(private val context: Context) {
    private val file: File by lazy { File(context.filesDir, "scores.txt") }

    fun save(p1: Int, p2: Int) = file.writeText("$p1,$p2")

    fun load(): Pair<Int, Int> = try {
        if (file.exists()) file.readText().split(",").let {
            if (it.size == 2) Pair(it[0].toInt(), it[1].toInt()) else Pair(0, 0)
        } else Pair(0, 0)
    } catch (e: Exception) { Pair(0, 0) }
}

enum class Player { X, O }
enum class Cell { EMPTY, X, O }

class GameViewModel : ViewModel() {
    var board by mutableStateOf(com.tictactoe.model.Board())
    var currentPlayer by mutableStateOf(Player.X)
    var gameState by mutableStateOf(com.tictactoe.model.GameState.ONGOING)
    var winningLine: List<Pair<Int, Int>>? by mutableStateOf(null)

    var player1Score by mutableStateOf(0)
    var player2Score by mutableStateOf(0)

    var isPvPMode by mutableStateOf(true)
    var aiDifficulty by mutableStateOf(AIDifficulty.MEDIUM)
    var aiPlayer = Player.O

    var vibrator: android.os.Vibrator? = null
    var storage: ScoreStorage? = null
    var toneGenerator: android.media.ToneGenerator? = null
    var localView: android.view.View? = null

    init { loadScores() }

    fun onCellClicked(row: Int, col: Int) {
        if (gameState != com.tictactoe.model.GameState.ONGOING) return
        if (!isPvPMode && currentPlayer == aiPlayer) return

        val success = com.tictactoe.util.GameLogic.makeMove(board, row, col, currentPlayer)
        if (!success) return

        playMoveSound()
        vibrate()

        updateGameState()

        if (gameState == com.tictactoe.model.GameState.ONGOING && !isPvPMode && currentPlayer == aiPlayer) {
            makeAIMove()
        }
    }

    private fun makeAIMove() {
        viewModelScope.launch {
            kotlinx.coroutines.delay(300)
            val move = com.tictactoe.util.Minimax.getBestMove(board, aiPlayer, aiDifficulty)
            if (move != null) {
                com.tictactoe.util.GameLogic.makeMove(board, move.first, move.second, aiPlayer)
                board = board.copy()
                playMoveSound()
                vibrate()
                updateGameState()
            }
        }
    }

    private fun updateGameState() {
        val winner = com.tictactoe.util.GameLogic.checkWinner(board)
        winningLine = com.tictactoe.util.GameLogic.getWinningLine(board)

        gameState = when {
            winner == Player.X -> {
                player1Score++
                playWinSound()
                vibrateWin()
                com.tictactoe.model.GameState.WIN_X
            }
            winner == Player.O -> {
                player2Score++
                playWinSound()
                vibrateWin()
                com.tictactoe.model.GameState.WIN_O
            }
            com.tictactoe.util.GameLogic.isDraw(board) -> {
                playDrawSound()
                com.tictactoe.model.GameState.DRAW
            }
            else -> com.tictactoe.model.GameState.ONGOING
        }

        if (gameState != com.tictactoe.model.GameState.ONGOING) {
            storage?.save(player1Score, player2Score)
        }

        if (gameState == com.tictactoe.model.GameState.ONGOING) {
            currentPlayer = if (currentPlayer == Player.X) Player.O else Player.X
        }
    }

    fun resetGame() {
        board = com.tictactoe.model.Board()
        currentPlayer = Player.X
        gameState = com.tictactoe.model.GameState.ONGOING
        winningLine = null
    }

    private fun vibrate() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator?.vibrate(android.os.VibrationEffect.createOneShot(30, android.os.VibrationEffect.DEFAULT_AMPLITUDE))
            } else {
                @Suppress("DEPRECATION")
                vibrator?.vibrate(30)
            }
            localView?.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP)
        } catch (e: Exception) { }
    }

    private fun vibrateWin() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator?.vibrate(android.os.VibrationEffect.createWaveform(longArrayOf(0, 50, 50, 50), -1))
            } else {
                @Suppress("DEPRECATION")
                vibrator?.vibrate(longArrayOf(0, 50, 50, 50), -1)
            }
        } catch (e: Exception) { }
    }

    private fun playMoveSound() {
        try {
            toneGenerator?.startTone(android.media.ToneGenerator.TONE_PROP_BEEP, 30)
        } catch (e: Exception) { }
    }

    private fun playWinSound() {
        try {
            toneGenerator?.startTone(android.media.ToneGenerator.TONE_PROP_ACK, 100)
        } catch (e: Exception) { }
    }

    private fun playDrawSound() {
        try {
            toneGenerator?.startTone(android.media.ToneGenerator.TONE_PROP_BEEP2, 80)
        } catch (e: Exception) { }
    }

    private fun loadScores() {
        viewModelScope.launch {
            val scores = storage?.load() ?: Pair(0, 0)
            player1Score = scores.first
            player2Score = scores.second
        }
    }

    override fun onCleared() {
        super.onCleared()
        storage?.save(player1Score, player2Score)
    }
}