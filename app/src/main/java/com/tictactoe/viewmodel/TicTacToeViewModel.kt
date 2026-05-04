package com.tictactoe.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

sealed class Player(val symbol: Char, val displayName: String) {
    data object X : Player('X', "Player X")
    data object O : Player('O', "Player O")
}

data class GameState(
    val board: List<Char?> = List(9) { null },
    val currentPlayer: Player = Player.X,
    val winner: Player? = null,
    val isDraw: Boolean = false,
    val winningLine: List<Int> = emptyList(),
    val scoreX: Int = 0,
    val scoreO: Int = 0,
    val isProcessing: Boolean = false,
    val lastMovePosition: Int? = null
)

class TicTacToeViewModel : ViewModel() {

    private val _gameState = MutableStateFlow(GameState())
    val gameState: StateFlow<GameState> = _gameState.asStateFlow()

    private val winPatterns = listOf(
        listOf(0, 1, 2),
        listOf(3, 4, 5),
        listOf(6, 7, 8),
        listOf(0, 3, 6),
        listOf(1, 4, 7),
        listOf(2, 5, 8),
        listOf(0, 4, 8),
        listOf(2, 4, 6)
    )

    /**
     * Makes a move at the specified position.
     * Uses async/await pattern to process game logic off the main thread.
     */
    fun makeMove(position: Int) {
        if (_gameState.value.isProcessing) return

        val currentState = _gameState.value
        if (currentState.board[position] != null || currentState.winner != null || currentState.isDraw) {
            return
        }

        viewModelScope.launch {
            _gameState.update { it.copy(isProcessing = true) }

            val newBoard = currentState.board.toMutableList()
            newBoard[position] = currentState.currentPlayer.symbol

            val result = withContext(Dispatchers.Default) {
                calculateGameResult(newBoard, currentState.currentPlayer)
            }

            _gameState.update { state ->
                state.copy(
                    board = newBoard,
                    winner = result.winner,
                    winningLine = result.winningLine,
                    isDraw = result.isDraw,
                    scoreX = if (result.winner == Player.X) state.scoreX + 1 else state.scoreX,
                    scoreO = if (result.winner == Player.O) state.scoreO + 1 else state.scoreO,
                    currentPlayer = if (result.winner == null && !result.isDraw) {
                        if (state.currentPlayer == Player.X) Player.O else Player.X
                    } else {
                        state.currentPlayer
                    },
                    isProcessing = false,
                    lastMovePosition = position
                )
            }
        }
    }

    /**
     * Asynchronously calculates the game result.
     * Runs on Default dispatcher to avoid blocking the main thread.
     */
    private suspend fun calculateGameResult(
        board: List<Char?>,
        currentPlayer: Player
    ): GameResult = withContext(Dispatchers.Default) {
        val currentSymbol = currentPlayer.symbol

        for (pattern in winPatterns) {
            if (checkWinPattern(board, pattern, currentSymbol)) {
                return@withContext GameResult(
                    winner = currentPlayer,
                    winningLine = pattern,
                    isDraw = false
                )
            }
        }

        if (board.none { it == null }) {
            return@withContext GameResult(
                winner = null,
                winningLine = emptyList(),
                isDraw = true
            )
        }

        GameResult(winner = null, winningLine = emptyList(), isDraw = false)
    }

    /**
     * Checks if a specific pattern constitutes a win.
     * Optimized algorithm with early exit on first match.
     */
    private fun checkWinPattern(board: List<Char?>, pattern: List<Int>, symbol: Char): Boolean {
        return pattern.all { board[it] == symbol }
    }

    /**
     * Resets the current game board while preserving scores.
     */
    fun resetGame() {
        _gameState.update { state ->
            state.copy(
                board = List(9) { null },
                currentPlayer = Player.X,
                winner = null,
                isDraw = false,
                winningLine = emptyList(),
                isProcessing = false,
                lastMovePosition = null
            )
        }
    }

    /**
     * Resets both the game and the score.
     */
    fun resetAll() {
        viewModelScope.launch {
            delay(50)
            _gameState.value = GameState()
        }
    }

    override fun onCleared() {
        super.onCleared()
    }
}

private data class GameResult(
    val winner: Player?,
    val winningLine: List<Int>,
    val isDraw: Boolean
)