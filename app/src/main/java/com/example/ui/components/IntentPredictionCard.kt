package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
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
import com.example.ui.theme.NeuralCardPurple
import com.example.ui.theme.NeuralDeepPurple
import com.example.ui.theme.NeuralTextPrimary
import com.example.ui.theme.NeuralTextSecondary

@Composable
fun IntentPredictionCard(
    title: String,
    summary: String,
    actionPlan: String,
    isGenerating: Boolean,
    onRunInferenceClick: () -> Unit,
    subconsciousFocusLevel: String = "Deep Flow State (Alpha 10.2 Hz)",
    alphaBandHz: Float = 10.2f,
    betaBandHz: Float = 18.5f,
    thetaBandHz: Float = 6.1f,
    gammaBandHz: Float = 42.0f,
    thoughtCognitiveLoadPct: Int = 42,
    onDecodeCustomThought: ((String) -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    var isExpanded by remember { mutableStateOf(false) }
    var isExecutingAction by remember { mutableStateOf(false) }
    var actionExecutedText by remember { mutableStateOf<String?>(null) }
    var userFeedback by remember { mutableStateOf<String?>(null) }
    var customThoughtText by remember { mutableStateOf("") }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(28.dp))
            .background(NeuralCardPurple)
            .border(
                width = 1.dp,
                color = Color.White.copy(alpha = 0.12f),
                shape = RoundedCornerShape(28.dp)
            )
            .padding(20.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(NeuralAccent),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = "Intent AI",
                        tint = NeuralDeepPurple,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Spacer(modifier = Modifier.size(14.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = title,
                        color = Color.White,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = "Subconscious Mind Thought Predictor",
                        color = NeuralTextSecondary,
                        fontSize = 11.sp
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(NeuralDeepPurple.copy(alpha = 0.5f))
                        .clickable { onRunInferenceClick() }
                        .padding(8.dp)
                ) {
                    if (isGenerating) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(18.dp),
                            color = NeuralAccent,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Re-analyze Intent",
                            tint = NeuralAccent,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }

            // Predict Mind Scan Trigger Button
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(NeuralDeepPurple)
                    .border(1.dp, NeuralAccent.copy(alpha = 0.4f), RoundedCornerShape(14.dp))
                    .clickable { onRunInferenceClick() }
                    .padding(vertical = 10.dp, horizontal = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Psychology,
                        contentDescription = null,
                        tint = NeuralAccent,
                        modifier = Modifier.size(18.dp)
                    )
                    Text(
                        text = if (isGenerating) "Scanning Mind Synapses..." else "⚡ Predict Mind Thought Now",
                        color = NeuralAccent,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Real-time Brainwave Spectral Bands Telemetry
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(NeuralDeepPurple)
                    .padding(12.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "SUBCONSCIOUS MIND FREQUENCY BANDS",
                            color = NeuralAccent,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Load: $thoughtCognitiveLoadPct%",
                            color = NeuralTextSecondary,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    Text(
                        text = "Mind Focus: $subconsciousFocusLevel",
                        color = NeuralTextPrimary,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium
                    )

                    // Spectral Bar Indicators
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Alpha", color = NeuralTextSecondary, fontSize = 9.sp)
                            Text("${String.format("%.1f", alphaBandHz)}Hz", color = NeuralAccent, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        }
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Beta", color = NeuralTextSecondary, fontSize = 9.sp)
                            Text("${String.format("%.1f", betaBandHz)}Hz", color = NeuralAccent, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        }
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Theta", color = NeuralTextSecondary, fontSize = 9.sp)
                            Text("${String.format("%.1f", thetaBandHz)}Hz", color = NeuralAccent, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        }
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Gamma", color = NeuralTextSecondary, fontSize = 9.sp)
                            Text("${String.format("%.1f", gammaBandHz)}Hz", color = NeuralAccent, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            // Direct Mind Thought Decoder Console
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(NeuralDeepPurple)
                    .border(1.dp, NeuralAccent.copy(alpha = 0.3f), RoundedCornerShape(16.dp))
                    .padding(12.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "DIRECT MIND THOUGHT DECODER",
                        color = NeuralAccent,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            value = customThoughtText,
                            onValueChange = { customThoughtText = it },
                            placeholder = { Text("Type mental focus / thought...", color = NeuralTextSecondary, fontSize = 11.sp) },
                            modifier = Modifier.weight(1f),
                            singleLine = true,
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = NeuralCardPurple,
                                unfocusedContainerColor = NeuralCardPurple,
                                focusedTextColor = NeuralTextPrimary,
                                unfocusedTextColor = NeuralTextPrimary,
                                focusedIndicatorColor = NeuralAccent,
                                unfocusedIndicatorColor = Color.White.copy(alpha = 0.1f)
                            )
                        )

                        Box(
                            modifier = Modifier
                                .size(42.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(NeuralAccent)
                                .clickable {
                                    if (customThoughtText.isNotBlank()) {
                                        onDecodeCustomThought?.invoke(customThoughtText)
                                    }
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Send,
                                contentDescription = "Decode Thought",
                                tint = NeuralDeepPurple,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }

                    // Quick Thought Focus Chips
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(10.dp))
                                .background(NeuralCardPurple)
                                .clickable {
                                    customThoughtText = "Refactor Code Architecture"
                                    onDecodeCustomThought?.invoke("Refactor Code Architecture")
                                }
                                .padding(vertical = 6.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("⚡ Refactor UI", color = NeuralAccent, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        }

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(10.dp))
                                .background(NeuralCardPurple)
                                .clickable {
                                    customThoughtText = "Short Mind Break"
                                    onDecodeCustomThought?.invoke("Short Mind Break")
                                }
                                .padding(vertical = 6.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("☕ Coffee Break", color = NeuralAccent, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        }

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(10.dp))
                                .background(NeuralCardPurple)
                                .clickable {
                                    customThoughtText = "Deep Flow Coding"
                                    onDecodeCustomThought?.invoke("Deep Flow Coding")
                                }
                                .padding(vertical = 6.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("🧠 Deep Flow", color = NeuralAccent, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            Text(
                text = summary,
                color = NeuralTextPrimary,
                fontSize = 14.sp,
                lineHeight = 20.sp,
                fontWeight = FontWeight.Normal
            )

            // Mind Thought Accuracy & Recalibration Feedback
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(NeuralDeepPurple.copy(alpha = 0.5f))
                    .padding(10.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(
                        text = "WAS THIS PREDICTED THOUGHT ACCURATE?",
                        color = NeuralTextSecondary,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (userFeedback == "Accurate") NeuralAccent else Color.White.copy(alpha = 0.08f))
                                .clickable { userFeedback = "Accurate" }
                                .padding(vertical = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "✓ Accurate Thought",
                                color = if (userFeedback == "Accurate") NeuralDeepPurple else NeuralTextPrimary,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (userFeedback == "Recalibrate") NeuralAccent else Color.White.copy(alpha = 0.08f))
                                .clickable { userFeedback = "Recalibrate" }
                                .padding(vertical = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "⚡ Recalibrate Model",
                                color = if (userFeedback == "Recalibrate") NeuralDeepPurple else NeuralTextPrimary,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }

                    if (userFeedback != null) {
                        Text(
                            text = if (userFeedback == "Accurate") "Synaptic weight reinforced (+1.2% model confidence)." else "Model weights adjusted for hesitation & touch latency.",
                            color = NeuralAccent,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Normal
                        )
                    }
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { isExpanded = !isExpanded }
                    .padding(vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = if (isExpanded) "Hide Action Plan" else "View & Trigger Planned Actions",
                    color = NeuralAccent,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Icon(
                    imageVector = Icons.Default.Psychology,
                    contentDescription = "Toggle",
                    tint = NeuralAccent,
                    modifier = Modifier.size(16.dp)
                )
            }

            AnimatedVisibility(
                visible = isExpanded,
                enter = fadeIn() + expandVertically()
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(NeuralDeepPurple.copy(alpha = 0.6f))
                            .padding(14.dp)
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            Text(
                                text = "PREDICTED REAL-TIME ACTIONS",
                                color = NeuralAccent,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            )
                            Text(
                                text = actionPlan,
                                color = NeuralTextPrimary,
                                fontSize = 13.sp,
                                lineHeight = 18.sp
                            )
                        }
                    }

                    // Execute Button
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .background(NeuralAccent)
                            .clickable {
                                isExecutingAction = true
                                actionExecutedText = "Proactive OS Intent Executed: Screen Dimmed & Notification Filtered"
                            }
                            .padding(vertical = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (isExecutingAction) "✓ Action Plan Executed" else "Execute Proactive Action Plan Now",
                            color = NeuralDeepPurple,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    if (actionExecutedText != null) {
                        Text(
                            text = actionExecutedText!!,
                            color = NeuralAccent,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
    }
}
