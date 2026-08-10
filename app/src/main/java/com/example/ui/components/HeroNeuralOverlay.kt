package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.NeuralAccent
import com.example.ui.theme.NeuralDeepPurple
import com.example.ui.theme.NeuralSurface
import com.example.ui.theme.NeuralTextSecondary

@Composable
fun HeroNeuralOverlay(
    matchPercentage: Float,
    statusText: String,
    touchValue: Float,
    audioValue: Float,
    visualValue: Float,
    motionValue: Float,
    biometricsValue: Float,
    neuralValue: Float,
    isSyncing: Boolean,
    onCenterCoreClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val scalePulse by infiniteTransition.animateFloat(
        initialValue = 1.0f,
        targetValue = 1.08f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )

    val alphaPulse by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "alpha"
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(310.dp)
            .clip(RoundedCornerShape(32.dp))
            .background(NeuralSurface)
            .border(
                width = 1.dp,
                color = Color.White.copy(alpha = 0.08f),
                shape = RoundedCornerShape(32.dp)
            )
    ) {
        // Radial Gradient Effect
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            NeuralAccent.copy(alpha = 0.18f),
                            Color.Transparent
                        )
                    )
                )
        )

        // Center Synaptic Circle Stack
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(bottom = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(175.dp)
                    .clip(CircleShape)
                    .background(NeuralAccent.copy(alpha = 0.05f))
                    .border(1.dp, NeuralAccent.copy(alpha = 0.18f), CircleShape)
                    .clickable { onCenterCoreClick() },
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(125.dp)
                        .clip(CircleShape)
                        .background(NeuralAccent.copy(alpha = 0.12f))
                        .border(1.dp, NeuralAccent.copy(alpha = 0.38f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(68.dp)
                            .scale(if (isSyncing) scalePulse else 1.0f)
                            .clip(CircleShape)
                            .background(NeuralAccent),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Psychology,
                            contentDescription = "Synaptic Core",
                            tint = NeuralDeepPurple,
                            modifier = Modifier.size(34.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = statusText,
                color = NeuralTextSecondary,
                fontSize = 11.sp,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 0.8.sp
            )
        }

        // Bottom 6 Telemetry Channel Indicators
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 14.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            TelemetryBarItem(
                label = "Touch",
                value = touchValue,
                isPulse = isSyncing,
                alpha = alphaPulse,
                modifier = Modifier.weight(1f)
            )
            TelemetryBarItem(
                label = "Audio",
                value = audioValue,
                isPulse = isSyncing,
                alpha = alphaPulse,
                modifier = Modifier.weight(1f)
            )
            TelemetryBarItem(
                label = "Camera",
                value = visualValue,
                isPulse = isSyncing,
                alpha = alphaPulse,
                modifier = Modifier.weight(1f)
            )
            TelemetryBarItem(
                label = "Motion",
                value = motionValue,
                isPulse = isSyncing,
                alpha = alphaPulse,
                modifier = Modifier.weight(1f)
            )
            TelemetryBarItem(
                label = "Bio",
                value = biometricsValue,
                isPulse = isSyncing,
                alpha = alphaPulse,
                modifier = Modifier.weight(1f)
            )
            TelemetryBarItem(
                label = "Neural",
                value = neuralValue,
                isPulse = isSyncing,
                alpha = alphaPulse,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun TelemetryBarItem(
    label: String,
    value: Float,
    isPulse: Boolean,
    alpha: Float,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(4.dp)
                .clip(RoundedCornerShape(2.dp))
                .background(NeuralAccent.copy(alpha = 0.15f))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(value.coerceIn(0.1f, 1.0f))
                    .height(4.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(
                        if (isPulse && label == "Neural") NeuralAccent.copy(alpha = alpha)
                        else NeuralAccent
                    )
            )
        }
        Text(
            text = label.uppercase(),
            fontSize = 9.sp,
            color = Color.White.copy(alpha = 0.5f),
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp
        )
    }
}
