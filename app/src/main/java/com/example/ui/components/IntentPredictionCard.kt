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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.example.ui.theme.AppIcons
import com.example.ui.theme.NeuralAccent
import com.example.ui.theme.NeuralCardPurple
import com.example.ui.theme.NeuralDeepPurple
import com.example.ui.theme.NeuralTextPrimary
import com.example.ui.theme.NeuralTextSecondary

data class MindBandData(
    val subconsciousFocusLevel: String = "Deep Flow State (Alpha 10.2 Hz)",
    val alphaBandHz: Float = 10.2f,
    val betaBandHz: Float = 18.5f,
    val thetaBandHz: Float = 6.1f,
    val gammaBandHz: Float = 42.0f,
    val thoughtCognitiveLoadPct: Int = 42
)

@Composable
fun IntentPredictionCard(
    title: String,
    summary: String,
    actionPlan: String,
    isGenerating: Boolean,
    onRunInferenceClick: () -> Unit,
    mindBands: MindBandData = MindBandData(),
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
            // Header Row
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
                        imageVector = AppIcons.AutoAwesome,
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
                            imageVector = AppIcons.RefreshIcon,
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
                        imageVector = AppIcons.Psychology,
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

            // Spectral Band Telemetry
            MindSpectralCard(mindBands = mindBands)

            // Direct Mind Thought Decoder Console
            DirectThoughtDecoderView(
                customThoughtText = customThoughtText,
                onTextChanged = { customThoughtText = it },
                onDecode = { thought ->
                    onDecodeCustomThought?.invoke(thought)
                }
            )

            Text(
                text = summary,
                color = NeuralTextPrimary,
                fontSize = 14.sp,
                lineHeight = 20.sp,
                fontWeight = FontWeight.Normal
            )

            // Thought Accuracy Feedback
            MindThoughtFeedbackSection(
                userFeedback = userFeedback,
                onFeedbackSelected = { userFeedback = it }
            )

            // Expandable Action Plan
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
                    imageVector = AppIcons.Psychology,
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

@Composable
private fun MindSpectralCard(
    mindBands: MindBandData
) {
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
                    text = "Load: ${mindBands.thoughtCognitiveLoadPct}%",
                    color = NeuralTextSecondary,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Text(
                text = "Mind Focus: ${mindBands.subconsciousFocusLevel}",
                color = NeuralTextPrimary,
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                SpectralBandItem(name = "Alpha", hz = mindBands.alphaBandHz, modifier = Modifier.weight(1f))
                SpectralBandItem(name = "Beta", hz = mindBands.betaBandHz, modifier = Modifier.weight(1f))
                SpectralBandItem(name = "Theta", hz = mindBands.thetaBandHz, modifier = Modifier.weight(1f))
                SpectralBandItem(name = "Gamma", hz = mindBands.gammaBandHz, modifier = Modifier.weight(1f))
            }
        }
    }
}

@Composable
private fun SpectralBandItem(name: String, hz: Float, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Text(name, color = NeuralTextSecondary, fontSize = 9.sp)
        Text(
            text = "${String.format(java.util.Locale.US, "%.1f", hz)}Hz",
            color = NeuralAccent,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun DirectThoughtDecoderView(
    customThoughtText: String,
    onTextChanged: (String) -> Unit,
    onDecode: (String) -> Unit
) {
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
                    onValueChange = onTextChanged,
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
                                onDecode(customThoughtText)
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = AppIcons.SendIcon,
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
                ThoughtChip(title = "⚡ Refactor UI", thought = "Refactor Code Architecture", onSelect = onDecode, modifier = Modifier.weight(1f))
                ThoughtChip(title = "☕ Coffee Break", thought = "Short Mind Break", onSelect = onDecode, modifier = Modifier.weight(1f))
                ThoughtChip(title = "🧠 Deep Flow", thought = "Deep Flow Coding", onSelect = onDecode, modifier = Modifier.weight(1f))
            }
        }
    }
}

@Composable
private fun ThoughtChip(
    title: String,
    thought: String,
    onSelect: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(NeuralCardPurple)
            .clickable { onSelect(thought) }
            .padding(vertical = 6.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(title, color = NeuralAccent, fontSize = 10.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun MindThoughtFeedbackSection(
    userFeedback: String?,
    onFeedbackSelected: (String) -> Unit
) {
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
                        .clickable { onFeedbackSelected("Accurate") }
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
                        .clickable { onFeedbackSelected("Recalibrate") }
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
}
