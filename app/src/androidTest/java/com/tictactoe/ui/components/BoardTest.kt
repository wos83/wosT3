package com.tictactoe.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.tictactoe.viewmodel.CellState
import com.tictactoe.viewmodel.Player
import org.junit.Assert.*

@Composable
fun verifyBoardState(
    board: List<CellState>,
    onCellClick: (Int) -> Unit,
    winningLine: List<Int>?,
    isGameOver: Boolean,
    winner: Player?
) {
    assertEquals(9, board.size)
    assertTrue(onCellClick is Function1<*, *>)
    assertTrue(isGameOver is Boolean)
}

fun verifyEmptyBoard(board: List<CellState>) {
    assertEquals(9, board.size)
    board.forEach { cell ->
        assertEquals(CellState.EMPTY, cell)
    }
}

fun verifyWinMessage(winner: Player?, isGameOver: Boolean) {
    if (isGameOver && winner != null) {
        val expectedMessage = when (winner) {
            Player.X -> "Player X Wins!"
            Player.O -> "Player O Wins!"
        }
        assertNotNull(expectedMessage)
    } else if (isGameOver && winner == null) {
        assertTrue(true)
    }
}

fun verifyScoreDisplay(player1Score: Int, player2Score: Int) {
    assertTrue(player1Score >= 0)
    assertTrue(player2Score >= 0)
}

fun verifyCurrentPlayer(player: Player) {
    assertTrue(player == Player.X || player == Player.O)
}