package com.tictactoe.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

enum class Player {
    X, O
}

enum class CellState {
    EMPTY, X, O
}

data class TicTacToeState(
    val board: List<CellState> = List(9) { CellState.EMPTY },
    val currentPlayer: Player = Player.X,
    val winner: Player? = null,
    val winningLine: List<Int>? = null,
    val isGameOver: Boolean = false
)

class TicTacToeViewModel : ViewModel() {
    private val _gameState = MutableStateFlow(TicTacToeState())
    val gameState: StateFlow<TicTacToeState> = _gameState.asStateFlow()

    private val _player1Score = MutableStateFlow(0)
    val player1Score: Int get() = _player1Score.value

    private val _player2Score = MutableStateFlow(0)
    val player2Score: Int get() = _player2Score.value

    val currentPlayer: Player get() = _gameState.value.currentPlayer
    val board: List<CellState> get() = _gameState.value.board
    val winningLine: List<Int>? get() = _gameState.value.winningLine
    val isGameOver: Boolean get() = _gameState.value.isGameOver
    val winner: Player? get() = _gameState.value.winner

    private val winningCombinations = listOf(
        listOf(0, 1, 2), listOf(3, 4, 5), listOf(6, 7, 8),
        listOf(0, 3, 6), listOf(1, 4, 7), listOf(2, 5, 8),
        listOf(0, 4, 8), listOf(2, 4, 6)
    )

    fun makeMove(index: Int) {
        viewModelScope.launch {
            val currentState = _gameState.value
            if (currentState.board[index] != CellState.EMPTY || currentState.isGameOver) {
                return@launch
            }

            val newBoard = currentState.board.toMutableList()
            val playerSymbol = if (currentState.currentPlayer == Player.X) CellState.X else CellState.O
            newBoard[index] = playerSymbol

            val (winner, winningLine) = checkWinner(newBoard)
            val isGameOver = winner != null || newBoard.none { it == CellState.EMPTY }

            _gameState.value = currentState.copy(
                board = newBoard,
                currentPlayer = if (!isGameOver) {
                    if (currentState.currentPlayer == Player.X) Player.O else Player.X
                } else currentState.currentPlayer,
                winner = winner,
                winningLine = winningLine,
                isGameOver = isGameOver
            )

            when (winner) {
                Player.X -> _player1Score.value++
                Player.O -> _player2Score.value++
                null -> {}
            }
        }
    }

    fun resetGame() {
        viewModelScope.launch {
            _gameState.value = TicTacToeState()
        }
    }

    private fun checkWinner(board: List<CellState>): Pair<Player?, List<Int>?> {
        for (combo in winningCombinations) {
            val (a, b, c) = combo
            if (board[a] != CellState.EMPTY &&
                board[a] == board[b] &&
                board[b] == board[c]
            ) {
                val winner = when (board[a]) {
                    CellState.X -> Player.X
                    CellState.O -> Player.O
                    else -> null
                }
                return winner to combo
            }
        }
        return null to null
    }
}