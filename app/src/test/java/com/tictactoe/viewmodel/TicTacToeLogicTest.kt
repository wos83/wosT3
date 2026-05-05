package com.tictactoe.viewmodel

import org.junit.Assert.*
import org.junit.Test

class TicTacToeLogicTest {

    private val winningCombinations = listOf(
        listOf(0, 1, 2), listOf(3, 4, 5), listOf(6, 7, 8),
        listOf(0, 3, 6), listOf(1, 4, 7), listOf(2, 5, 8),
        listOf(0, 4, 8), listOf(2, 4, 6)
    )

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

    @Test
    fun `empty board has no winner`() {
        val board = List(9) { CellState.EMPTY }
        val (winner, _) = checkWinner(board)
        assertNull(winner)
    }

    @Test
    fun `X wins horizontally in first row`() {
        val board = listOf(CellState.X, CellState.X, CellState.X) + List(6) { CellState.EMPTY }
        val (winner, winningLine) = checkWinner(board)
        assertEquals(Player.X, winner)
        assertEquals(listOf(0, 1, 2), winningLine)
    }

    @Test
    fun `X wins horizontally in second row`() {
        val board = List(3) { CellState.EMPTY } + listOf(CellState.X, CellState.X, CellState.X) + List(3) { CellState.EMPTY }
        val (winner, winningLine) = checkWinner(board)
        assertEquals(Player.X, winner)
        assertEquals(listOf(3, 4, 5), winningLine)
    }

    @Test
    fun `X wins horizontally in third row`() {
        val board = List(6) { CellState.EMPTY } + listOf(CellState.X, CellState.X, CellState.X)
        val (winner, winningLine) = checkWinner(board)
        assertEquals(Player.X, winner)
        assertEquals(listOf(6, 7, 8), winningLine)
    }

    @Test
    fun `O wins vertically in first column`() {
        val board = listOf(CellState.O, CellState.EMPTY, CellState.EMPTY,
                       CellState.O, CellState.EMPTY, CellState.EMPTY,
                       CellState.O, CellState.EMPTY, CellState.EMPTY)
        val (winner, winningLine) = checkWinner(board)
        assertEquals(Player.O, winner)
        assertEquals(listOf(0, 3, 6), winningLine)
    }

    @Test
    fun `X wins vertically in second column`() {
        val board = listOf(CellState.EMPTY, CellState.X, CellState.EMPTY,
                       CellState.EMPTY, CellState.X, CellState.EMPTY,
                       CellState.EMPTY, CellState.X, CellState.EMPTY)
        val (winner, winningLine) = checkWinner(board)
        assertEquals(Player.X, winner)
        assertEquals(listOf(1, 4, 7), winningLine)
    }

    @Test
    fun `O wins diagonally from top-left`() {
        val board = listOf(CellState.O, CellState.EMPTY, CellState.EMPTY,
                       CellState.EMPTY, CellState.O, CellState.EMPTY,
                       CellState.EMPTY, CellState.EMPTY, CellState.O)
        val (winner, winningLine) = checkWinner(board)
        assertEquals(Player.O, winner)
        assertEquals(listOf(0, 4, 8), winningLine)
    }

    @Test
    fun `X wins diagonally from top-right`() {
        val board = listOf(CellState.EMPTY, CellState.EMPTY, CellState.X,
                       CellState.EMPTY, CellState.X, CellState.EMPTY,
                       CellState.X, CellState.EMPTY, CellState.EMPTY)
        val (winner, winningLine) = checkWinner(board)
        assertEquals(Player.X, winner)
        assertEquals(listOf(2, 4, 6), winningLine)
    }

    @Test
    fun `no winner when board is mixed`() {
        val board = listOf(CellState.X, CellState.O, CellState.EMPTY,
                       CellState.EMPTY, CellState.X, CellState.EMPTY,
                       CellState.EMPTY, CellState.EMPTY, CellState.EMPTY)
        val (winner, _) = checkWinner(board)
        assertNull(winner)
    }
}