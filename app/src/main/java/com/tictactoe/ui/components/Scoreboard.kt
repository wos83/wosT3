package com.tictactoe.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tictactoe.ui.theme.TicTacToeShapes
import com.tictactoe.viewmodel.GameState
import com.tictactoe.viewmodel.Player

@Composable
fun Scoreboard(
    gameState: GameState,
    xColor: Color,
    oColor: Color,
    onResetClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        ScoreCard(
            player = Player.X,
            score = gameState.scoreX,
            color = xColor,
            isActive = gameState.currentPlayer == Player.X,
            modifier = Modifier.weight(1f)
        )

        Spacer(modifier = Modifier.width(16.dp))

        ResetButton(
            onClick = onResetClick,
            modifier = Modifier.weight(0.4f)
        )

        Spacer(modifier = Modifier.width(16.dp))

        ScoreCard(
            player = Player.O,
            score = gameState.scoreO,
            color = oColor,
            isActive = gameState.currentPlayer == Player.O,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun ScoreCard(
    player: Player,
    score: Int,
    color: Color,
    isActive: Boolean,
    modifier: Modifier = Modifier
) {
    val scale by animateFloatAsState(
        targetValue = if (isActive) 1.05f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "scale"
    )

    val glowColor by animateColorAsState(
        targetValue = if (isActive) color.copy(alpha = 0.3f) else Color.Transparent,
        label = "glow"
    )

    val cardGradient = Brush.verticalGradient(
        colors = listOf(
            if (isActive) color.copy(alpha = 0.15f) else Color(0xFFFAFAFA),
            if (isActive) color.copy(alpha = 0.1f) else Color(0xFFF5F5F5),
            Color(0xFFEEEEEE)
        )
    )

    Box(
        modifier = modifier
            .scale(scale)
            .shadow(
                elevation = if (isActive) 12.dp else 6.dp,
                shape = TicTacToeShapes.scoreCardShape,
                ambientColor = if (isActive) color.copy(alpha = 0.3f) else Color.Black.copy(alpha = 0.1f),
                spotColor = if (isActive) color.copy(alpha = 0.4f) else Color.Black.copy(alpha = 0.15f)
            )
            .clip(TicTacToeShapes.scoreCardShape)
            .background(cardGradient)
            .border(
                width = if (isActive) 2.dp else 1.dp,
                brush = Brush.linearGradient(
                    colors = if (isActive) {
                        listOf(color.copy(alpha = 0.6f), color.copy(alpha = 0.2f))
                    } else {
                        listOf(Color.White, Color.Gray.copy(alpha = 0.2f))
                    }
                ),
                shape = TicTacToeShapes.scoreCardShape
            )
            .padding(vertical = 16.dp, horizontal = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .shadow(4.dp, CircleShape)
                    .clip(CircleShape)
                    .background(
                        Brush.radialGradient(
                            colors = listOf(
                                color.copy(alpha = 0.2f),
                                color.copy(alpha = 0.1f)
                            )
                        )
                    )
                    .border(2.dp, color.copy(alpha = 0.5f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = player.symbol.toString(),
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp
                    ),
                    color = color
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = score.toString(),
                style = MaterialTheme.typography.displaySmall.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = if (isActive) color else MaterialTheme.colorScheme.onSurface
            )

            Text(
                text = player.displayName.replace("Player ", ""),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun ResetButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val buttonGradient = Brush.verticalGradient(
        colors = listOf(
            MaterialTheme.colorScheme.primaryContainer,
            MaterialTheme.colorScheme.primary.copy(alpha = 0.8f)
        )
    )

    IconButton(
        onClick = onClick,
        modifier = modifier
            .height(56.dp)
            .shadow(8.dp, RoundedCornerShape(16.dp))
            .clip(RoundedCornerShape(16.dp))
            .background(buttonGradient)
            .border(
                width = 1.dp,
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color.White.copy(alpha = 0.5f),
                        Color.Transparent
                    )
                ),
                shape = RoundedCornerShape(16.dp)
            ),
        colors = IconButtonDefaults.iconButtonColors(
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
        )
    ) {
        Icon(
            imageVector = Icons.Default.Refresh,
            contentDescription = "Reset Game",
            modifier = Modifier.size(28.dp)
        )
    }
}