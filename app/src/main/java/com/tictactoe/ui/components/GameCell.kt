package com.tictactoe.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp

@Composable
fun PixelGameCell(
    symbol: Char?,
    isWinningCell: Boolean,
    xColor: Color,
    oColor: Color,
    onClick: () -> Unit,
    enabled: Boolean,
    modifier: Modifier = Modifier
) {
    val haptic = LocalHapticFeedback.current
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val pressScale = remember { Animatable(1f) }

    LaunchedEffect(isPressed) {
        pressScale.animateTo(
            targetValue = if (isPressed) 0.85f else 1f,
            animationSpec = tween(50, easing = LinearEasing)
        )
    }

    val cellColor = when {
        isWinningCell -> Color(0xFF4a4a4a)
        symbol != null -> Color(0xFF3d3d3d)
        enabled -> Color(0xFF2d2d2d)
        else -> Color(0xFF252525)
    }

    val borderColor = when {
        isWinningCell -> Color(0xFFffff00)
        else -> Color(0xFF1a1a1a)
    }

    Box(
        modifier = modifier
            .aspectRatio(1f)
            .padding(2.dp)
            .background(cellColor)
            .then(
                if (isWinningCell) {
                    Modifier.background(Color.Black.copy(alpha = 0.3f))
                } else Modifier
            )
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                enabled = enabled && symbol == null,
                onClick = {
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    onClick()
                }
            ),
        contentAlignment = Alignment.Center
    ) {
        if (symbol != null) {
            PixelSymbol(
                symbol = symbol,
                xColor = xColor,
                oColor = oColor,
                isWinning = isWinningCell
            )
        }

        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .padding(2.dp)
        ) {
            val pixelSize = 4f

            drawLine(
                color = borderColor,
                start = Offset(0f, 0f),
                end = Offset(size.width, 0f),
                strokeWidth = pixelSize
            )
            drawLine(
                color = borderColor,
                start = Offset(0f, size.height - pixelSize),
                end = Offset(size.width, size.height - pixelSize),
                strokeWidth = pixelSize
            )
            drawLine(
                color = borderColor,
                start = Offset(0f, 0f),
                end = Offset(0f, size.height),
                strokeWidth = pixelSize
            )
            drawLine(
                color = borderColor,
                start = Offset(size.width - pixelSize, 0f),
                end = Offset(size.width - pixelSize, size.height),
                strokeWidth = pixelSize
            )
        }
    }
}

@Composable
private fun PixelSymbol(
    symbol: Char,
    xColor: Color,
    oColor: Color,
    isWinning: Boolean
) {
    val progress = remember { Animatable(0f) }

    LaunchedEffect(symbol) {
        progress.snapTo(0f)
        progress.animateTo(
            targetValue = 1f,
            animationSpec = tween(
                durationMillis = 400,
                easing = LinearEasing
            )
        )
    }

    Canvas(
        modifier = Modifier
            .fillMaxSize(0.85f)
            .padding(4.dp)
    ) {
        val color = if (symbol == 'X') xColor else oColor

        if (isWinning) {
            drawRect(
                color = Color.White.copy(alpha = 0.2f),
                topLeft = Offset.Zero,
                size = size
            )
        }

        if (symbol == 'X') {
            drawPixelX(color, progress.value)
        } else {
            drawPixelO(color, progress.value)
        }
    }
}

private fun DrawScope.drawPixelX(color: Color, progress: Float) {
    val centerX = size.width / 2
    val centerY = size.height / 2
    val maxRadius = size.minDimension * 0.4f

    val steps = 8
    val currentStep = (progress * steps).toInt().coerceIn(0, steps)

    val pixelSize = maxRadius / 2.5f

    for (step in 0 until currentStep) {
        val stepProgress = step.toFloat() / steps

        val dist1 = stepProgress * maxRadius
        drawRect(
            color = color,
            topLeft = Offset(centerX - dist1 - pixelSize/2, centerY - pixelSize/2),
            size = androidx.compose.ui.geometry.Size(pixelSize, pixelSize)
        )

        if (progress > 0.4f) {
            val line2Progress = ((progress - 0.4f) / 0.6f * steps).toInt()
            if (step <= line2Progress) {
                val dist2 = stepProgress * maxRadius
                drawRect(
                    color = color,
                    topLeft = Offset(centerX + dist2 - pixelSize/2, centerY - pixelSize/2),
                    size = androidx.compose.ui.geometry.Size(pixelSize, pixelSize)
                )
            }
        }
    }

    if (progress > 0.8f) {
        val centerPixelSize = pixelSize * 0.9f
        drawRect(
            color = color.copy(alpha = 0.3f),
            topLeft = Offset(centerX - centerPixelSize/2, centerY - centerPixelSize/2),
            size = androidx.compose.ui.geometry.Size(centerPixelSize, centerPixelSize)
        )
    }
}

private fun DrawScope.drawPixelO(color: Color, progress: Float) {
    val centerX = size.width / 2
    val centerY = size.height / 2
    val radius = size.minDimension * 0.35f

    val steps = 10
    val currentStep = (progress * steps).toInt().coerceIn(0, steps)

    val pixelSize = radius / 2f

    for (step in 0 until currentStep) {
        val angle = (step.toFloat() / steps) * 360f - 90f
        val angleRad = Math.toRadians(angle.toDouble())

        val x = centerX + (radius * kotlin.math.cos(angleRad)).toFloat()
        val y = centerY + (radius * kotlin.math.sin(angleRad)).toFloat()

        drawRect(
            color = color,
            topLeft = Offset(x - pixelSize/2, y - pixelSize/2),
            size = androidx.compose.ui.geometry.Size(pixelSize, pixelSize)
        )
    }

    if (progress > 0.9f) {
        val fillSize = radius * 0.4f
        drawRect(
            color = color.copy(alpha = 0.15f),
            topLeft = Offset(centerX - fillSize/2, centerY - fillSize/2),
            size = androidx.compose.ui.geometry.Size(fillSize, fillSize)
        )
    }
}