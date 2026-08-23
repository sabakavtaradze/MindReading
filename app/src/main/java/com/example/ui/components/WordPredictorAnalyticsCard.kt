package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.NeuralAccent
import com.example.ui.theme.NeuralBorder
import com.example.ui.theme.NeuralDeepPurple
import com.example.ui.theme.NeuralSurface
import com.example.ui.theme.NeuralTextPrimary
import com.example.ui.theme.NeuralTextSecondary
import com.example.viewmodel.CognitiveFatigueHeatmapItem
import com.example.viewmodel.MarkovLearnedTransition
import com.example.viewmodel.WordBranchPrediction
import com.example.viewmodel.WordPredictionAnalyticsState

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun WordPredictorAnalyticsCard(
    predictionState: WordPredictionAnalyticsState,
    onTogglePreMotorPredictor: () -> Unit,
    onApplyBranch: (String) -> Unit,
    onRegenerateBranches: () -> Unit,
    onToggleMarkovContext: (() -> Unit)? = null,
    onToggleGazeDwell: (() -> Unit)? = null,
    onToggleBilingual: (() -> Unit)? = null,
    onLearnMarkovPair: ((String, String) -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    var showDetailedAnalytics by remember { mutableStateOf(false) }
    var showMarkovTrainer by remember { mutableStateOf(false) }
    var markovPrevInput by remember { mutableStateOf("") }
    var markovNextInput by remember { mutableStateOf("") }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        NeuralSurface,
                        Color(0xFF130E29)
                    )
                )
            )
            .border(
                1.5.dp,
                Brush.horizontalGradient(
                    colors = listOf(
                        NeuralAccent.copy(alpha = 0.8f),
                        Color(0xFF8A2BE2).copy(alpha = 0.6f)
                    )
                ),
                RoundedCornerShape(24.dp)
            )
            .padding(18.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
            // Header: Title & Master Readiness Switch
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(if (predictionState.isPreMotorPredictorActive) Color(0xFF00FFB2) else Color.Gray)
                    )
                    Column {
                        Text(
                            text = "პრემოტორული სიტყვების განჭვრეტა (Pre-Motor Readiness)",
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Bereitschaftspotential: -${predictionState.readinessPotentialLeadTimeMs}ms წინასწარი ამოცნობა",
                            color = NeuralAccent,
                            fontSize = 9.5.sp,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                }

                Switch(
                    checked = predictionState.isPreMotorPredictorActive,
                    onCheckedChange = { onTogglePreMotorPredictor() },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = NeuralAccent,
                        checkedTrackColor = NeuralDeepPurple
                    )
                )
            }

            // Real-time Readiness Potential Waveform Canvas
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(68.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color.Black.copy(alpha = 0.4f))
                    .border(1.dp, Color.White.copy(alpha = 0.08f), RoundedCornerShape(14.dp))
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Column(verticalArrangement = Arrangement.SpaceBetween) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "ნეირონული მზაობის ტალღა (-350ms ➔ 0ms აკრეფა)",
                            color = NeuralTextSecondary,
                            fontSize = 9.sp
                        )
                        Text(
                            text = "${predictionState.currentReadinessSpikeMicroVolts} µV (პიკი)",
                            color = Color(0xFF00FFB2),
                            fontSize = 9.sp,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Canvas(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(34.dp)
                    ) {
                        val width = size.width
                        val height = size.height
                        val midY = height * 0.7f

                        // Draw baseline
                        drawLine(
                            color = Color.White.copy(alpha = 0.15f),
                            start = Offset(0f, midY),
                            end = Offset(width, midY),
                            strokeWidth = 1.dp.toPx()
                        )

                        // Draw negative readiness potential curve (Bereitschaftspotential)
                        val path = Path()
                        path.moveTo(0f, midY)
                        path.cubicTo(
                            width * 0.35f, midY,
                            width * 0.65f, midY - height * 0.55f,
                            width * 0.88f, midY - height * 0.85f
                        )
                        path.lineTo(width, midY + height * 0.1f) // Discharge point at 0ms

                        drawPath(
                            path = path,
                            color = Color(0xFF00FFB2),
                            style = Stroke(width = 2.dp.toPx(), cap = StrokeCap.Round)
                        )

                        // Trigger point marker
                        drawCircle(
                            color = Color(0xFFD4B2FF),
                            radius = 4.dp.toPx(),
                            center = Offset(width * 0.88f, midY - height * 0.85f)
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "-350ms (სუბვოკალი)", color = NeuralTextSecondary, fontSize = 8.sp)
                        Text(text = "⚡ -${predictionState.readinessPotentialLeadTimeMs}ms (განჭვრეტა)", color = Color(0xFF00FFB2), fontSize = 8.5.sp, fontWeight = FontWeight.Bold)
                        Text(text = "0ms (წარმოთქმა)", color = NeuralTextSecondary, fontSize = 8.sp)
                    }
                }
            }

            // Quick Control Pills for Advanced Features (Markov, Gaze Dwell, Bilingual)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ControlPill(
                    title = "🧠 მარკოვის მეხსიერება",
                    isActive = predictionState.isMarkovContextLearningActive,
                    onClick = { onToggleMarkovContext?.invoke() }
                )
                ControlPill(
                    title = "👁️ Gaze-Dwell (200ms)",
                    isActive = predictionState.isGazeDwellSelectionActive,
                    onClick = { onToggleGazeDwell?.invoke() }
                )
                ControlPill(
                    title = "🌐 ორენოვანი (GE/EN)",
                    isActive = predictionState.isBilingualAutoFlipActive,
                    onClick = { onToggleBilingual?.invoke() }
                )
                ControlPill(
                    title = "🎓 ჯაჭვის დასწავლა",
                    isActive = showMarkovTrainer,
                    onClick = { showMarkovTrainer = !showMarkovTrainer }
                )
            }

            // Interactive Markov Trainer Dialog / Inline Input
            AnimatedVisibility(visible = showMarkovTrainer) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.White.copy(alpha = 0.05f))
                        .border(1.dp, NeuralAccent.copy(alpha = 0.4f), RoundedCornerShape(12.dp))
                        .padding(10.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text(
                            text = "🔗 ახალი სემანტიკური გადასვლის დასწავლა (Semantic Chain Trainer):",
                            color = Color.White,
                            fontSize = 10.5.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            OutlinedTextField(
                                value = markovPrevInput,
                                onValueChange = { markovPrevInput = it },
                                placeholder = { Text("წინა სიტყვა (მაგ. კოდის)", fontSize = 10.sp, color = NeuralTextSecondary) },
                                modifier = Modifier.weight(1f).height(46.dp),
                                textStyle = androidx.compose.ui.text.TextStyle(fontSize = 11.sp, color = Color.White),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = NeuralAccent,
                                    unfocusedBorderColor = Color.White.copy(alpha = 0.2f)
                                ),
                                singleLine = true
                            )
                            Text("➔", color = NeuralAccent, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                            OutlinedTextField(
                                value = markovNextInput,
                                onValueChange = { markovNextInput = it },
                                placeholder = { Text("შემდეგი (მაგ. ანალიზი)", fontSize = 10.sp, color = NeuralTextSecondary) },
                                modifier = Modifier.weight(1f).height(46.dp),
                                textStyle = androidx.compose.ui.text.TextStyle(fontSize = 11.sp, color = Color.White),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = NeuralAccent,
                                    unfocusedBorderColor = Color.White.copy(alpha = 0.2f)
                                ),
                                singleLine = true
                            )
                            Button(
                                onClick = {
                                    if (markovPrevInput.isNotBlank() && markovNextInput.isNotBlank()) {
                                        onLearnMarkovPair?.invoke(markovPrevInput.trim(), markovNextInput.trim())
                                        markovPrevInput = ""
                                        markovNextInput = ""
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = NeuralAccent),
                                modifier = Modifier.height(44.dp),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text("დამახსოვრება", color = NeuralDeepPurple, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }

            // Multi-Branch Next-Word Probability Graph (Interactive Tree)
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "🌳 განზრახვის მრავალალტერნატიული ხე (Multi-Branch Intent Tree):",
                        color = Color.White,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "🔄 განახლება",
                        color = NeuralAccent,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.clickable { onRegenerateBranches() }
                    )
                }

                // Horizontal Flow of High-Probability Word Branches
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    predictionState.branches.forEachIndexed { index, branch ->
                        val isTopChoice = index == 0
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(14.dp))
                                .background(
                                    if (isTopChoice) {
                                        Brush.verticalGradient(
                                            listOf(
                                                NeuralAccent.copy(alpha = 0.25f),
                                                Color(0xFF8A2BE2).copy(alpha = 0.35f)
                                            )
                                        )
                                    } else {
                                        Brush.verticalGradient(
                                            listOf(
                                                Color.White.copy(alpha = 0.06f),
                                                Color.White.copy(alpha = 0.02f)
                                            )
                                        )
                                    }
                                )
                                .border(
                                    1.dp,
                                    if (isTopChoice) NeuralAccent else Color.White.copy(alpha = 0.15f),
                                    RoundedCornerShape(14.dp)
                                )
                                .clickable { onApplyBranch(branch.id) }
                                .padding(12.dp)
                        ) {
                            Column(
                                verticalArrangement = Arrangement.spacedBy(4.dp),
                                modifier = Modifier.width(135.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "${branch.probabilityPct}%",
                                        color = if (isTopChoice) Color(0xFF00FFB2) else NeuralAccent,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        fontFamily = FontFamily.Monospace
                                    )
                                    Text(
                                        text = "${branch.phonemeLookaheadMs}ms",
                                        color = NeuralTextSecondary,
                                        fontSize = 9.sp,
                                        fontFamily = FontFamily.Monospace
                                    )
                                }

                                Text(
                                    text = branch.word,
                                    color = Color.White,
                                    fontSize = 13.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    maxLines = 1
                                )

                                Text(
                                    text = branch.linguisticGrammarRole,
                                    color = NeuralTextSecondary,
                                    fontSize = 8.5.sp,
                                    maxLines = 1
                                )

                                Spacer(modifier = Modifier.height(2.dp))

                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(if (isTopChoice) NeuralAccent else Color.White.copy(alpha = 0.1f))
                                        .padding(vertical = 4.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = if (isTopChoice) "⚡ ავტო-დამატება" else "არჩევა",
                                        color = if (isTopChoice) NeuralDeepPurple else Color.White,
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Learned Markov Context Chain Carousel
            if (predictionState.markovMemoryChain.isNotEmpty()) {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(
                        text = "🧠 დასწავლილი მარკოვის ასოციაციები (Learned Thinking Chains):",
                        color = NeuralTextSecondary,
                        fontSize = 10.sp
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        predictionState.markovMemoryChain.forEach { item ->
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Color.White.copy(alpha = 0.05f))
                                    .border(1.dp, Color.White.copy(alpha = 0.1f), RoundedCornerShape(8.dp))
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Text(text = item.previousWord, color = Color.White, fontSize = 9.5.sp, fontWeight = FontWeight.Bold)
                                    Text(text = "➔", color = NeuralAccent, fontSize = 9.sp)
                                    Text(text = item.predictedNextWord, color = Color(0xFF00FFB2), fontSize = 9.5.sp, fontWeight = FontWeight.Bold)
                                    Text(text = "(${item.confidencePct}%)", color = NeuralTextSecondary, fontSize = 8.sp)
                                }
                            }
                        }
                    }
                }
            }

            // Predictive Data Analytics HUD & Trajectory
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color.Black.copy(alpha = 0.35f))
                    .padding(12.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "📊 ნეირო-პრედიქტიური ანალიტიკა:",
                            color = NeuralTextSecondary,
                            fontSize = 10.sp
                        )
                        Text(
                            text = if (showDetailedAnalytics) "შეკუმშვა ▲" else "ღრმა ანალიზი & Heatmap ▼",
                            color = NeuralAccent,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.clickable { showDetailedAnalytics = !showDetailedAnalytics }
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        AnalyticsMetricBadge("კოგნიტური აჩქარება", "+${predictionState.cognitiveSpeedupGainWpm} WPM", Color(0xFF00FFB2))
                        AnalyticsMetricBadge("დაყოვნების ინდექსი", "${predictionState.subconsciousHesitationLatencyMs}ms", Color(0xFFFFD54F))
                        AnalyticsMetricBadge("დაღლილობა", "${predictionState.cognitiveFatiguePct}%", if (predictionState.cognitiveFatiguePct < 40) Color(0xFF00FFB2) else Color(0xFFFF7043))
                        AnalyticsMetricBadge("წინასწარ ამოცნობილი", "${predictionState.totalWordsPredictedAhead} სიტყვა", Color.White)
                    }

                    // Expandable Deep Metrics & Fatigue Heatmap
                    AnimatedVisibility(visible = showDetailedAnalytics) {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.padding(top = 6.dp)
                        ) {
                            // Fatigue Heatmap Table
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Color.White.copy(alpha = 0.04f))
                                    .padding(8.dp)
                            ) {
                                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                    Text(
                                        text = "📉 კოგნიტური დაღლილობისა და სიზუსტის დროითი პროფილი (Fatigue Profile):",
                                        color = Color.White,
                                        fontSize = 9.5.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    predictionState.fatigueHeatmap.forEach { slot ->
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(text = slot.timeSlot, color = NeuralTextSecondary, fontSize = 9.sp)
                                            Text(text = "სიზუსტე: ${slot.accuracyPct}%", color = Color(0xFF00FFB2), fontSize = 9.sp, fontFamily = FontFamily.Monospace)
                                            Text(text = "+${slot.predictedSpeedGainWpm} WPM", color = NeuralAccent, fontSize = 9.sp, fontFamily = FontFamily.Monospace)
                                            Text(
                                                text = slot.fatigueLevel,
                                                color = if (slot.fatigueLevel == "OPTIMAL") Color(0xFF00FFB2) else Color(0xFFFFB74D),
                                                fontSize = 8.5.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                    }
                                }
                            }

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Color.White.copy(alpha = 0.04f))
                                    .padding(8.dp)
                            ) {
                                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                    Text(
                                        text = "🎯 ბოლო შესრულებული წინასწარმეტყველება:",
                                        color = NeuralTextSecondary,
                                        fontSize = 9.sp
                                    )
                                    Text(
                                        text = predictionState.lastAppliedPrediction,
                                        color = Color(0xFF00FFB2),
                                        fontSize = 10.5.sp,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ControlPill(
    title: String,
    isActive: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(if (isActive) NeuralAccent.copy(alpha = 0.2f) else Color.White.copy(alpha = 0.05f))
            .border(1.dp, if (isActive) NeuralAccent else Color.White.copy(alpha = 0.15f), RoundedCornerShape(20.dp))
            .clickable { onClick() }
            .padding(horizontal = 10.dp, vertical = 5.dp)
    ) {
        Text(
            text = title,
            color = if (isActive) NeuralAccent else NeuralTextSecondary,
            fontSize = 9.5.sp,
            fontWeight = if (isActive) FontWeight.Bold else FontWeight.Normal
        )
    }
}

@Composable
private fun AnalyticsMetricBadge(title: String, value: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value,
            color = color,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Monospace
        )
        Text(
            text = title,
            color = NeuralTextSecondary,
            fontSize = 8.sp
        )
    }
}
