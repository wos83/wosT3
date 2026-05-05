package com.tictactoe.util

import com.tictactoe.model.Board
import com.tictactoe.model.GameState
import com.tictactoe.model.Player

object GameLogic {

    private val winLines = listOf(
        // Rows
        listOf(Pair(0, 0), Pair(0, 1), Pair(0, 2)),
        listOf(Pair(1, 0), Pair(1, 1), Pair(1, 2)),
        listOf(Pair(2, 0), Pair(2, 1), Pair(2, 2)),
        // Columns
        listOf(Pair(0, 0), Pair(1, 0), Pair(2, 0)),
        listOf(Pair(0, 1), Pair(1, 1), Pair(2, 1)),
        listOf(Pair(0, 2), Pair(1, 2), Pair(2, 2)),
        // Diagonals
        listOf(Pair(0, 0), Pair(1, 1), Pair(2, 2)),
        listOf(Pair(0, 2), Pair(1, 1), Pair(2, 0))
    )

    fun checkWinner(board: Board): Player? {
        for (line in winLines) {
            val p1 = board.grid[line[0].first][line[0].second].player
            if (p1 != null &&
                p1 == board.grid[line[1].first][line[1].second].player &&
                p1 == board.grid[line[2].first][line[2].second].player
            ) {
                return p1
            }
        }
        return null
    }

    fun getWinningLine(board: Board): List<Pair<Int, Int>>? {
        for (line in winLines) {
            val p1 = board.grid[line[0].first][line[0].second].player
            if (p1 != null &&
                p1 == board.grid[line[1].first][line[1].second].player &&
                p1 == board.grid[line[2].first][line[2].second].player
            ) {
                return line
            }
        }
        return null
    }

    fun isDraw(board: Board): Boolean {
        return board.isFull() && checkWinner(board) == null
    }

    fun updateGameState(board: Board): GameState {
        val winner = checkWinner(board)
        return when {
            winner == Player.X -> GameState.WIN_X
            winner == Player.O -> GameState.WIN_O
            isDraw(board) -> GameState.DRAW
            else -> GameState.ONGOING
        }
    }

    fun makeMove(board: Board, row: Int, col: Int, player: Player): Boolean {
        if (board.grid[row][col].player != null) return false
        board.grid[row][col].player = player
        return true
    }
}

enum class AIDifficulty {
    EASY,
    MEDIUM,
    HARD
}

object Minimax {

    fun getBestMove(board: Board, aiPlayer: Player, difficulty: AIDifficulty): Pair<Int, Int>? {
        val availableMoves = board.getAvailableMoves()
        if (availableMoves.isEmpty()) return null

        return when (difficulty) {
            AIDifficulty.EASY -> availableMoves.random()
            AIDifficulty.MEDIUM -> {
                if ((0..100).random() < 50) {
                    getBestMoveMinimax(board, aiPlayer)
                } else {
                    availableMoves.random()
                }
            }
            AIDifficulty.HARD -> getBestMoveMinimax(board, aiPlayer)
        }
    }

    private fun getBestMoveMinimax(board: Board, aiPlayer: Player): Pair<Int, Int>? {
        val availableMoves = board.getAvailableMoves()
        if (availableMoves.isEmpty()) return null

        var bestScore = Int.MIN_VALUE
        var bestMove: Pair<Int, Int>? = null

        for (move in availableMoves) {
            val newBoard = board.copy()
            GameLogic.makeMove(newBoard, move.first, move.second, aiPlayer)

            val score = minimax(newBoard, false, aiPlayer)
            if (score > bestScore) {
                bestScore = score
                bestMove = move
            }
        }

        return bestMove
    }

    private fun minimax(board: Board, isMaximizing: Boolean, aiPlayer: Player): Int {
        val winner = GameLogic.checkWinner(board)
        val humanPlayer = if (aiPlayer == Player.X) Player.O else Player.X

        when {
            winner == aiPlayer -> return 10
            winner == humanPlayer -> return -10
            GameLogic.isDraw(board) -> return 0
        }

        val availableMoves = board.getAvailableMoves()
        if (availableMoves.isEmpty()) return 0

        if (isMaximizing) {
            var bestScore = Int.MIN_VALUE
            for (move in availableMoves) {
                val newBoard = board.copy()
                GameLogic.makeMove(newBoard, move.first, move.second, aiPlayer)
                val score = minimax(newBoard, false, aiPlayer)
                bestScore = maxOf(score, bestScore)
            }
            return bestScore
        } else {
            var bestScore = Int.MAX_VALUE
            for (move in availableMoves) {
                val newBoard = board.copy()
                GameLogic.makeMove(newBoard, move.first, move.second, humanPlayer)
                val score = minimax(newBoard, true, aiPlayer)
                bestScore = minOf(score, bestScore)
            }
            return bestScore
        }
    }
}