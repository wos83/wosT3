package com.tictactoe.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color

@Composable
fun PixelText(
    text: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    val charHeight = 8f  // 8 pixels
    val charWidth = 5f   // 5 pixels
    val charSpacing = 1f // 1 pixel
    
    Canvas(modifier = modifier) {
        val densityValue = density
        val charHeightPx = charHeight / densityValue
        val charWidthPx = charWidth / densityValue
        val charSpacingPx = charSpacing / densityValue
        
        var xOffset = 0f
        
        for (char in text.uppercase()) {
            val charMatrix = getPixelCharMatrix(char)
            
            for (row in charMatrix.indices) {
                for (col in charMatrix[row].indices) {
                    if (charMatrix[row][col] == 1) {
                        drawRect(
                            color = color,
                            topLeft = Offset(
                                xOffset + col * charWidthPx,
                                row * charHeightPx
                            ),
                            size = androidx.compose.ui.geometry.Size(charWidthPx, charHeightPx)
                        )
                    }
                }
            }
            
            xOffset += charWidthPx + charSpacingPx
        }
    }
}

private fun getPixelCharMatrix(char: Char): List<List<Int>> {
    return when (char) {
        'A' -> listOf(
            listOf(0,1,1,1,0),
            listOf(1,0,0,0,1),
            listOf(1,0,0,0,1),
            listOf(1,1,1,1,1),
            listOf(1,0,0,0,1),
            listOf(1,0,0,0,1)
        )
        'B' -> listOf(
            listOf(1,1,1,1,0),
            listOf(1,0,0,0,1),
            listOf(1,0,0,0,1),
            listOf(1,1,1,1,0),
            listOf(1,0,0,0,1),
            listOf(1,1,1,1,0)
        )
        'C' -> listOf(
            listOf(0,1,1,1,0),
            listOf(1,0,0,0,1),
            listOf(1,0,0,0,0),
            listOf(1,0,0,0,0),
            listOf(1,0,0,0,1),
            listOf(0,1,1,1,0)
        )
        'D' -> listOf(
            listOf(1,1,1,1,0),
            listOf(1,0,0,0,1),
            listOf(1,0,0,0,1),
            listOf(1,0,0,0,1),
            listOf(1,0,0,0,1),
            listOf(1,1,1,1,0)
        )
        'E' -> listOf(
            listOf(1,1,1,1,1),
            listOf(1,0,0,0,0),
            listOf(1,0,0,0,0),
            listOf(1,1,1,1,0),
            listOf(1,0,0,0,0),
            listOf(1,1,1,1,1)
        )
        'F' -> listOf(
            listOf(1,1,1,1,1),
            listOf(1,0,0,0,0),
            listOf(1,0,0,0,0),
            listOf(1,1,1,1,0),
            listOf(1,0,0,0,0),
            listOf(1,0,0,0,0)
        )
        'G' -> listOf(
            listOf(0,1,1,1,0),
            listOf(1,0,0,0,1),
            listOf(1,0,0,0,0),
            listOf(1,0,1,1,1),
            listOf(1,0,0,0,1),
            listOf(0,1,1,1,0)
        )
        'H' -> listOf(
            listOf(1,0,0,0,1),
            listOf(1,0,0,0,1),
            listOf(1,1,1,1,1),
            listOf(1,0,0,0,1),
            listOf(1,0,0,0,1),
            listOf(1,0,0,0,1)
        )
        'I' -> listOf(
            listOf(1,1,1,1,1),
            listOf(0,0,1,0,0),
            listOf(0,0,1,0,0),
            listOf(0,0,1,0,0),
            listOf(0,0,1,0,0),
            listOf(1,1,1,1,1)
        )
        'J' -> listOf(
            listOf(0,0,0,0,1),
            listOf(0,0,0,0,1),
            listOf(0,0,0,0,1),
            listOf(1,0,0,0,1),
            listOf(1,0,0,0,1),
            listOf(0,1,1,1,0)
        )
        'L' -> listOf(
            listOf(1,0,0,0,0),
            listOf(1,0,0,0,0),
            listOf(1,0,0,0,0),
            listOf(1,0,0,0,0),
            listOf(1,0,0,0,0),
            listOf(1,1,1,1,1)
        )
        'N' -> listOf(
            listOf(1,0,0,0,1),
            listOf(1,1,0,0,1),
            listOf(1,0,1,0,1),
            listOf(1,0,0,1,1),
            listOf(1,0,0,0,1),
            listOf(1,0,0,0,1)
        )
        'O' -> listOf(
            listOf(0,1,1,1,0),
            listOf(1,0,0,0,1),
            listOf(1,0,0,0,1),
            listOf(1,0,0,0,1),
            listOf(1,0,0,0,1),
            listOf(0,1,1,1,0)
        )
        'P' -> listOf(
            listOf(1,1,1,1,0),
            listOf(1,0,0,0,1),
            listOf(1,0,0,0,1),
            listOf(1,1,1,1,0),
            listOf(1,0,0,0,0),
            listOf(1,0,0,0,0)
        )
        'R' -> listOf(
            listOf(1,1,1,1,0),
            listOf(1,0,0,0,1),
            listOf(1,0,0,0,1),
            listOf(1,1,1,1,0),
            listOf(1,0,1,0,0),
            listOf(1,0,0,1,1)
        )
        'S' -> listOf(
            listOf(0,1,1,1,0),
            listOf(1,0,0,0,1),
            listOf(0,1,1,1,0),
            listOf(0,0,0,0,1),
            listOf(1,0,0,0,1),
            listOf(0,1,1,1,0)
        )
        'T' -> listOf(
            listOf(1,1,1,1,1),
            listOf(0,0,1,0,0),
            listOf(0,0,1,0,0),
            listOf(0,0,1,0,0),
            listOf(0,0,1,0,0),
            listOf(0,0,1,0,0)
        )
        'U' -> listOf(
            listOf(1,0,0,0,1),
            listOf(1,0,0,0,1),
            listOf(1,0,0,0,1),
            listOf(1,0,0,0,1),
            listOf(1,0,0,0,1),
            listOf(0,1,1,1,0)
        )
        'V' -> listOf(
            listOf(1,0,0,0,1),
            listOf(1,0,0,0,1),
            listOf(1,0,0,0,1),
            listOf(1,0,0,0,1),
            listOf(0,1,0,1,0),
            listOf(0,0,1,0,0)
        )
        'X' -> listOf(
            listOf(1,0,0,0,1),
            listOf(1,0,0,0,1),
            listOf(0,1,0,1,0),
            listOf(0,1,0,1,0),
            listOf(1,0,0,0,1),
            listOf(1,0,0,0,1)
        )
        'Y' -> listOf(
            listOf(1,0,0,0,1),
            listOf(1,0,0,0,1),
            listOf(0,1,0,1,0),
            listOf(0,0,1,0,0),
            listOf(0,0,1,0,0),
            listOf(0,0,1,0,0)
        )
        'Z' -> listOf(
            listOf(1,1,1,1,1),
            listOf(0,0,0,0,1),
            listOf(0,0,1,1,0),
            listOf(0,1,0,0,0),
            listOf(1,0,0,0,0),
            listOf(1,1,1,1,1)
        )
        '0' -> listOf(
            listOf(0,1,1,1,0),
            listOf(1,0,0,1,1),
            listOf(1,0,1,0,1),
            listOf(1,1,0,0,1),
            listOf(1,0,0,0,1),
            listOf(0,1,1,1,0)
        )
        '1' -> listOf(
            listOf(0,0,1,0,0),
            listOf(0,1,1,0,0),
            listOf(0,0,1,0,0),
            listOf(0,0,1,0,0),
            listOf(0,0,1,0,0),
            listOf(0,1,1,1,0)
        )
        '2' -> listOf(
            listOf(0,1,1,1,0),
            listOf(1,0,0,0,1),
            listOf(0,0,0,1,0),
            listOf(0,0,1,0,0),
            listOf(0,1,0,0,0),
            listOf(1,1,1,1,1)
        )
        '3' -> listOf(
            listOf(0,1,1,1,0),
            listOf(1,0,0,0,1),
            listOf(0,0,1,1,0),
            listOf(0,0,0,0,1),
            listOf(1,0,0,0,1),
            listOf(0,1,1,1,0)
        )
        '4' -> listOf(
            listOf(0,0,0,1,0),
            listOf(0,0,1,1,0),
            listOf(0,1,0,1,0),
            listOf(1,1,1,1,1),
            listOf(0,0,0,1,0),
            listOf(0,0,0,1,0)
        )
        '5' -> listOf(
            listOf(1,1,1,1,1),
            listOf(1,0,0,0,0),
            listOf(1,1,1,1,0),
            listOf(0,0,0,0,1),
            listOf(1,0,0,0,1),
            listOf(0,1,1,1,0)
        )
        '6' -> listOf(
            listOf(0,1,1,1,0),
            listOf(1,0,0,0,0),
            listOf(1,1,1,1,0),
            listOf(1,0,0,0,1),
            listOf(1,0,0,0,1),
            listOf(0,1,1,1,0)
        )
        '7' -> listOf(
            listOf(1,1,1,1,1),
            listOf(0,0,0,0,1),
            listOf(0,0,0,1,0),
            listOf(0,0,1,0,0),
            listOf(0,0,1,0,0),
            listOf(0,0,1,0,0)
        )
        '8' -> listOf(
            listOf(0,1,1,1,0),
            listOf(1,0,0,0,1),
            listOf(0,1,1,1,0),
            listOf(1,0,0,0,1),
            listOf(1,0,0,0,1),
            listOf(0,1,1,1,0)
        )
        '9' -> listOf(
            listOf(0,1,1,1,0),
            listOf(1,0,0,0,1),
            listOf(1,0,0,0,1),
            listOf(0,1,1,1,1),
            listOf(0,0,0,0,1),
            listOf(0,1,1,1,0)
        )
        ' ' -> listOf(
            listOf(0,0,0,0,0),
            listOf(0,0,0,0,0),
            listOf(0,0,0,0,0),
            listOf(0,0,0,0,0),
            listOf(0,0,0,0,0),
            listOf(0,0,0,0,0)
        )
        '★' -> listOf(
            listOf(0,0,1,0,0),
            listOf(0,1,1,1,0),
            listOf(1,1,1,1,1),
            listOf(0,1,1,1,0),
            listOf(0,1,0,1,0),
            listOf(1,0,0,0,1)
        )
        '▶' -> listOf(
            listOf(0,0,0,1,0),
            listOf(0,0,1,1,0),
            listOf(0,1,1,1,0),
            listOf(0,0,1,1,0),
            listOf(0,0,0,1,0),
            listOf(0,0,0,1,0)
        )
        '◀' -> listOf(
            listOf(0,1,0,0,0),
            listOf(0,1,1,0,0),
            listOf(0,1,1,1,0),
            listOf(0,1,1,0,0),
            listOf(0,1,0,0,0),
            listOf(0,1,0,0,0)
        )
        '=' -> listOf(
            listOf(0,0,0,0,0),
            listOf(1,1,1,1,1),
            listOf(0,0,0,0,0),
            listOf(1,1,1,1,1),
            listOf(0,0,0,0,0),
            listOf(0,0,0,0,0)
        )
        '<' -> listOf(
            listOf(0,0,0,1,0),
            listOf(0,0,1,1,0),
            listOf(0,1,1,0,0),
            listOf(0,0,1,1,0),
            listOf(0,0,0,1,0),
            listOf(0,0,0,0,0)
        )
        '>' -> listOf(
            listOf(0,1,0,0,0),
            listOf(0,1,1,0,0),
            listOf(0,0,1,1,0),
            listOf(0,1,1,0,0),
            listOf(0,1,0,0,0),
            listOf(0,0,0,0,0)
        )
        else -> listOf(
            listOf(0,1,0,1,0),
            listOf(1,0,1,0,1),
            listOf(0,1,1,1,0),
            listOf(0,0,1,0,0),
            listOf(0,1,0,1,0),
            listOf(0,0,1,0,0)
        )
    }
}