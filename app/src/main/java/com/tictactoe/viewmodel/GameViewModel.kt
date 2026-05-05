package com.tictactoe.viewmodel

import android.content.Context
import android.media.ToneGenerator
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tictactoe.model.Board
import com.tictactoe.model.GameState
import com.tictactoe.model.Player
import com.tictactoe.util.AIDifficulty
import com.tictactoe.util.GameLogic
import com.tictactoe.util.Minimax
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File

class GameViewModel : ViewModel() {

    var board = Board()
    var currentPlayer = Player.X
    var gameState = GameState.ONGOING
    var winningLine: List<Pair<Int, Int>>? = null

    var player1Score = 0
    var player2Score = 0

    var isPvPMode = true
    var aiDifficulty = AIDifficulty.MEDIUM
    var aiPlayer = Player.O

    var vibrator: Vibrator? = null
    var storage: ScoreStorage? = null
    var toneGenerator: ToneGenerator? = null
    var localView: android.view.View? = null

    init { loadScores() }

    fun onCellClicked(row: Int, col: Int) {
        if (gameState != GameState.ONGOING) return
        if (!isPvPMode && currentPlayer == aiPlayer) return

        val success = GameLogic.makeMove(board, row, col, currentPlayer)
        if (!success) return

        playMoveSound()
        vibrate()

        updateGameState()

        if (gameState == GameState.ONGOING && !isPvPMode && currentPlayer == aiPlayer) {
            makeAIMove()
        }
    }

    private fun makeAIMove() {
        viewModelScope.launch {
            delay(300)
            val move = Minimax.getBestMove(board, aiPlayer, aiDifficulty)
            if (move != null) {
                GameLogic.makeMove(board, move.first, move.second, aiPlayer)
                playMoveSound()
                vibrate()
                updateGameState()
            }
        }
    }

    private fun updateGameState() {
        val winner = GameLogic.checkWinner(board)
        winningLine = GameLogic.getWinningLine(board)

        gameState = when {
            winner == Player.X -> {
                player1Score++
                playWinSound()
                VibrateWin()
                GameState.WIN_X
            }
            winner == Player.O -> {
                player2Score++
                playWinSound()
                VibrateWin()
                GameState.WIN_O
            }
            GameLogic.isDraw(board) -> {
                playDrawSound()
                GameState.DRAW
            }
            else -> GameState.ONGOING
        }

        if (gameState != GameState.ONGOING) {
            storage?.save(player1Score, player2Score)
        }

        if (gameState == GameState.ONGOING) {
            currentPlayer = if (currentPlayer == Player.X) Player.O else Player.X
        }
    }

    fun resetGame() {
        board = Board()
        currentPlayer = Player.X
        gameState = GameState.ONGOING
        winningLine = null
    }

    private fun vibrate() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator?.vibrate(VibrationEffect.createOneShot(30, VibrationEffect.DEFAULT_AMPLITUDE))
            } else {
                @Suppress("DEPRECATION")
                vibrator?.vibrate(30)
            }
            localView?.performHapticFeedback(android.view.HapticFeedbackConstants.KEYBOARD_TAP)
        } catch (e: Exception) { }
    }

    private fun VibrateWin() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator?.vibrate(VibrationEffect.createWaveform(longArrayOf(0, 50, 50, 50), -1))
            } else {
                @Suppress("DEPRECATION")
                vibrator?.vibrate(longArrayOf(0, 50, 50, 50), -1)
            }
        } catch (e: Exception) { }
    }

    private fun playMoveSound() {
        try {
            toneGenerator?.startTone(ToneGenerator.TONE_PROP_BEEP, 30)
        } catch (e: Exception) { }
    }

    private fun playWinSound() {
        try {
            toneGenerator?.startTone(ToneGenerator.TONE_PROP_ACK, 100)
        } catch (e: Exception) { }
    }

    private fun playDrawSound() {
        try {
            toneGenerator?.startTone(ToneGenerator.TONE_PROP_BEEP2, 80)
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

class ScoreStorage(private val context: Context) {
    private val file: File by lazy { File(context.filesDir, "scores.txt") }

    fun save(p1: Int, p2: Int) = file.writeText("$p1,$p2")

    fun load(): Pair<Int, Int> = try {
        if (file.exists()) file.readText().split(",").let {
            if (it.size == 2) Pair(it[0].toInt(), it[1].toInt()) else Pair(0, 0)
        } else Pair(0, 0)
    } catch (e: Exception) { Pair(0, 0) }
}