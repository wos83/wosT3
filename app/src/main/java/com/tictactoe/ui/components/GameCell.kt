package com.tictactoe.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.graphicsLayer
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp
import com.tictactoe.ui.theme.TicTacToeShapes
import kotlinx.coroutines.launch

@Composable
fun GameCell(
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

    val scale = remember { Animatable(if (isPressed) 0.95f else 1f) }

    LaunchedEffect(isPressed) {
        scale.animateTo(
            targetValue = if (isPressed) 0.92f else 1f,
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessMedium
            )
        )
    }

    val animatedScale by animateFloatAsState(
        targetValue = scale.value,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "scale"
    )

    val cellGradient = Brush.verticalGradient(
        colors = listOf(
            if (enabled) Color(0xFFFAFAFA) else Color(0xFFF0F0F0),
            if (enabled) Color(0xFFE8E8E8) else Color(0xFFE0E0E0),
            if (enabled) Color(0xFFFFFFFF) else Color(0xFFF5F5F5)
        )
    )

    val winningGlow = if (isWinningCell) {
        Brush.radialGradient(
            colors = listOf(
                xColor.copy(alpha = 0.3f),
                oColor.copy(alpha = 0.3f),
                Color.Transparent
            )
        )
    } else null

    Box(
        modifier = modifier
            .aspectRatio(1f)
            .graphicsLayer {
                scaleX = animatedScale
                scaleY = animatedScale
            }
            .shadow(
                elevation = if (isPressed) 2.dp else 6.dp,
                shape = TicTacToeShapes.cellShape,
                ambientColor = Color.Black.copy(alpha = 0.1f),
                spotColor = Color.Black.copy(alpha = 0.15f)
            )
            .clip(TicTacToeShapes.cellShape)
            .background(cellGradient)
            .then(
                if (winningGlow != null) {
                    Modifier.drawBehind {
                        drawRect(brush = winningGlow)
                    }
                } else Modifier
            )
            .border(
                width = 1.dp,
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color.White.copy(alpha = 0.8f),
                        Color.Gray.copy(alpha = 0.2f)
                    )
                ),
                shape = TicTacToeShapes.cellShape
            )
            .clickable(
                interactionSource = interactionSource,
                indication = ripple(bounded = true, color = MaterialTheme.colorScheme.primary),
                enabled = enabled && symbol == null,
                onClick = {
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    onClick()
                }
            ),
        contentAlignment = Alignment.Center
    ) {
        if (symbol != null) {
            AnimatedSymbol(
                symbol = symbol,
                xColor = xColor,
                oColor = oColor
            )
        }
    }
}

@Composable
private fun AnimatedSymbol(
    symbol: Char,
    xColor: Color,
    oColor: Color
) {
    val progress = remember { Animatable(0f) }

    LaunchedEffect(symbol) {
        progress.snapTo(0f)
        progress.animateTo(
            targetValue = 1f,
            animationSpec = tween(
                durationMillis = 400,
                easing = FastOutSlowInEasing
            )
        )
    }

    val color = if (symbol == 'X') xColor else oColor

    Canvas(
        modifier = Modifier
            .fillMaxSize(0.6f)
            .padding(8.dp)
    ) {
        val strokeWidth = size.minDimension * 0.12f
        val progressValue = progress.value

        if (symbol == 'X') {
            val pathSize = size.minDimension * 0.8f
            val offset = (size.width - pathSize) / 2

            val path1 = Path().apply {
                moveTo(offset, offset)
                lineTo(offset + pathSize, offset + pathSize)
            }
            val path2 = Path().apply {
                moveTo(offset + pathSize, offset)
                lineTo(offset, offset + pathSize)
            }

            drawPath(
                path = path1,
                color = color.copy(alpha = progressValue),
                style = Stroke(
                    width = strokeWidth,
                    cap = StrokeCap.Round
                )
            )

            val startProgress = progressValue * 0.5f
            if (startProgress > 0) {
                drawPath(
                    path = path2,
                    color = color.copy(alpha = startProgress),
                    style = Stroke(
                        width = strokeWidth,
                        cap = StrokeCap.Round
                    )
                )
            }
        } else {
            val center = Offset(size.width / 2, size.height / 2)
            val radius = size.minDimension * 0.35f

            drawCircle(
                color = color.copy(alpha = progressValue),
                radius = radius * progressValue,
                center = center,
                style = Stroke(width = strokeWidth)
            )
        }
    }
}

