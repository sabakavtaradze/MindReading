package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Headphones
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.NeuralAccent
import com.example.ui.theme.NeuralBorder
import com.example.ui.theme.NeuralDeepPurple
import com.example.ui.theme.NeuralSurface
import com.example.ui.theme.NeuralTextPrimary
import com.example.ui.theme.NeuralTextSecondary

@Composable
fun MindLabView(
    alphaBandHz: Float,
    betaBandHz: Float,
    thetaBandHz: Float,
    gammaBandHz: Float,
    cognitiveLoadPct: Int,
    subconsciousFocusLevel: String,
    onInjectStimulus: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var isSoundscapeActive by remember { mutableStateOf(false) }
    var soundscapeFrequency by remember { mutableStateOf(10.5f) }
    var soundscapeMode by remember { mutableStateOf("Deep Alpha Flow") }
    var isochronicVolume by remember { mutableStateOf(0.65f) }

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Subconscious Mind Readiness & Flow Index Card
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp))
                .background(NeuralSurface)
                .border(1.dp, NeuralAccent.copy(alpha = 0.3f), RoundedCornerShape(24.dp))
                .padding(16.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(NeuralDeepPurple),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Psychology,
                                contentDescription = null,
                                tint = NeuralAccent,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        Column {
                            Text(
                                text = "MIND READINESS INDEX",
                                color = NeuralAccent,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Subconscious BCI Neural Lab",
                                color = NeuralTextSecondary,
                                fontSize = 10.sp
                            )
                        }
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(NeuralDeepPurple)
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "${100 - cognitiveLoadPct}% STABILITY",
                            color = NeuralAccent,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                // Readiness Progress Gauge
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Flow Depth: $subconsciousFocusLevel",
                            color = NeuralTextPrimary,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = "Cognitive Load: $cognitiveLoadPct%",
                            color = NeuralTextSecondary,
                            fontSize = 11.sp
                        )
                    }

                    LinearProgressIndicator(
                        progress = { (100 - cognitiveLoadPct) / 100f },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp)),
                        color = NeuralAccent,
                        trackColor = Color.White.copy(alpha = 0.1f)
                    )
                }
            }
        }

        // Isochronic Binaural Soundscape Synthesizer Card
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp))
                .background(NeuralSurface)
                .border(1.dp, Color.White.copy(alpha = 0.1f), RoundedCornerShape(24.dp))
                .padding(16.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Headphones,
                            contentDescription = null,
                            tint = NeuralAccent,
                            modifier = Modifier.size(20.dp)
                        )
                        Text(
                            text = "NEURAL FREQUENCY SOUNDSCAPE",
                            color = NeuralTextPrimary,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(if (isSoundscapeActive) NeuralAccent else NeuralDeepPurple)
                            .clickable { isSoundscapeActive = !isSoundscapeActive },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (isSoundscapeActive) Icons.Default.Pause else Icons.Default.PlayArrow,
                            contentDescription = "Toggle Soundscape",
                            tint = if (isSoundscapeActive) NeuralDeepPurple else NeuralAccent,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                Text(
                    text = if (isSoundscapeActive)
                        "⚡ Synthesizing Isochronic Binaural Pulses @ ${String.format("%.1f", soundscapeFrequency)} Hz ($soundscapeMode)"
                    else
                        "Soundscape offline. Tap Play to stimulate brainwave synchronization.",
                    color = if (isSoundscapeActive) NeuralAccent else NeuralTextSecondary,
                    fontSize = 11.sp,
                    fontWeight = if (isSoundscapeActive) FontWeight.SemiBold else FontWeight.Normal
                )

                // Soundscape Preset Pills
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    PresetPill("Alpha 10Hz", soundscapeMode == "Deep Alpha Flow", modifier = Modifier.weight(1f)) {
                        soundscapeMode = "Deep Alpha Flow"
                        soundscapeFrequency = 10.5f
                        isSoundscapeActive = true
                    }
                    PresetPill("Gamma 40Hz", soundscapeMode == "Gamma Insight", modifier = Modifier.weight(1f)) {
                        soundscapeMode = "Gamma Insight"
                        soundscapeFrequency = 40.0f
                        isSoundscapeActive = true
                    }
                    PresetPill("Theta 6Hz", soundscapeMode == "Theta Intuition", modifier = Modifier.weight(1f)) {
                        soundscapeMode = "Theta Intuition"
                        soundscapeFrequency = 6.2f
                        isSoundscapeActive = true
                    }
                }

                // Volume Slider
                Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Isochronic Intensity", color = NeuralTextSecondary, fontSize = 10.sp)
                        Text("${(isochronicVolume * 100).toInt()}%", color = NeuralAccent, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }

                    Slider(
                        value = isochronicVolume,
                        onValueChange = { isochronicVolume = it },
                        valueRange = 0f..1f,
                        colors = SliderDefaults.colors(
                            thumbColor = NeuralAccent,
                            activeTrackColor = NeuralAccent,
                            inactiveTrackColor = Color.White.copy(alpha = 0.1f)
                        )
                    )
                }
            }
        }

        // Subconscious Stimulus Injection Simulator Card
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp))
                .background(NeuralSurface)
                .border(1.dp, Color.White.copy(alpha = 0.1f), RoundedCornerShape(24.dp))
                .padding(16.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Lightbulb,
                        contentDescription = null,
                        tint = NeuralAccent,
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = "STIMULUS INJECTION SIMULATOR",
                        color = NeuralTextPrimary,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Text(
                    text = "Inject external physical or psychological triggers to observe how subconscious neural predictions shift in real-time:",
                    color = NeuralTextSecondary,
                    fontSize = 11.sp
                )

                // Stimulus Buttons Grid
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        StimulusButton(
                            label = "☕ Caffeine Spike",
                            description = "+Beta Waves & Alertness",
                            modifier = Modifier.weight(1f),
                            onClick = { onInjectStimulus("CAFFEINE_SPIKE") }
                        )
                        StimulusButton(
                            label = "🧘 Deep Breathing",
                            description = "+Alpha Calm & Focus",
                            modifier = Modifier.weight(1f),
                            onClick = { onInjectStimulus("DEEP_BREATHING") }
                        )
                    }

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        StimulusButton(
                            label = "💻 Complex Problem",
                            description = "+Gamma High Synaptic Load",
                            modifier = Modifier.weight(1f),
                            onClick = { onInjectStimulus("COMPLEX_PROBLEM") }
                        )
                        StimulusButton(
                            label = "🎧 Ambient Masking",
                            description = "Reduces Environmental Noise",
                            modifier = Modifier.weight(1f),
                            onClick = { onInjectStimulus("NOISE_MASKING") }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun PresetPill(
    label: String,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(if (isSelected) NeuralAccent else NeuralDeepPurple)
            .clickable { onClick() }
            .padding(vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            color = if (isSelected) NeuralDeepPurple else NeuralTextPrimary,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun StimulusButton(
    label: String,
    description: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .background(NeuralDeepPurple)
            .border(1.dp, NeuralAccent.copy(alpha = 0.25f), RoundedCornerShape(14.dp))
            .clickable { onClick() }
            .padding(10.dp)
    ) {
        Column {
            Text(
                text = label,
                color = NeuralAccent,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = description,
                color = NeuralTextSecondary,
                fontSize = 9.sp
            )
        }
    }
}
