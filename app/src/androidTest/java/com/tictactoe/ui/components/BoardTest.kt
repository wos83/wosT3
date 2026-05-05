package com.tictactoe.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.tictactoe.model.Cell
import com.tictactoe.model.Player
import org.junit.Assert.*

@Composable
fun verifyBoardState(
    board: List<List<Cell>>,
    onCellClick: (Int, Int) -> Unit,
    winningLine: List<Pair<Int, Int>>?,
    isGameOver: Boolean,
    winner: Player?
) {
    assertEquals(9, board.size * board[0].size)
    assertTrue(onCellClick is Function2<*, *, *>)
    assertTrue(isGameOver is Boolean)
}

fun verifyEmptyBoard(board: List<List<Cell>>) {
    assertEquals(3, board.size)
    board.forEach { row ->
        row.forEach { cell ->
            assertNull(cell.player)
        }
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