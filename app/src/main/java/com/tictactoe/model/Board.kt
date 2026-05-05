package com.tictactoe.model

enum class Player {
    X, O
}

enum class GameState {
    ONGOING,
    WIN_X,
    WIN_O,
    DRAW
}

data class Cell(
    var player: Player? = null
)

class Board {
    val grid: Array<Array<Cell>> = Array(3) { Array(3) { Cell() } }

    fun reset() {
        for (row in grid) {
            for (cell in row) {
                cell.player = null
            }
        }
    }

    fun isFull(): Boolean {
        return grid.all { row -> row.all { it.player != null } }
    }

    fun getAvailableMoves(): List<Pair<Int, Int>> {
        val moves = mutableListOf<Pair<Int, Int>>()
        for (i in 0..2) {
            for (j in 0..2) {
                if (grid[i][j].player == null) {
                    moves.add(Pair(i, j))
                }
            }
        }
        return moves
    }

    fun copy(): Board {
        val newBoard = Board()
        for (i in 0..2) {
            for (j in 0..2) {
                newBoard.grid[i][j].player = grid[i][j].player
            }
        }
        return newBoard
    }
}