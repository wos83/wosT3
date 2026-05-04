package com.tictactoe.ui.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

object PixelUtils {
    @Composable
    fun pixelSizeInDp(): Dp {
        val density = LocalDensity.current.density
        return (1.0 / density).dp
    }

    @Composable
    fun pixelsToDp(pixels: Int): Dp {
        return pixelSizeInDp() * pixels
    }

    @Composable
    fun alignToPixel(dpValue: Dp): Dp {
        val pixelSize = pixelSizeInDp()
        val pixelCount = (dpValue.value / pixelSize.value).roundToInt()
        return (pixelCount * pixelSize.value).dp
    }
}