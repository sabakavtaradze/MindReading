package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.NeuroTab
import com.example.ui.theme.NeuralAccent
import com.example.ui.theme.NeuralBorder
import com.example.ui.theme.NeuralCardPurple
import com.example.ui.theme.NeuralCyanAccent
import com.example.ui.theme.NeuralDeepPurple
import com.example.ui.theme.NeuralGreenActive
import com.example.ui.theme.NeuralSurface
import com.example.ui.theme.NeuralTextPrimary
import com.example.ui.theme.NeuralTextSecondary
import com.example.viewmodel.NeuroSyncUiState

/**
 * Prominent Live Bar positioned directly below the main navigation menu.
 * Displays real-time Word Guessing / Intent Predictions and Cognitive Behavioral State.
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun LiveCognitiveWordsAndBehaviorBar(
    uiState: NeuroSyncUiState,
    onInjectWord: (String, String) -> Unit,
    onClearSentence: () -> Unit,
    onCycleNextWord: () -> Unit,
    onToggleDecoding: () -> Unit,
    onSelectTab: (NeuroTab) -> Unit,
    modifier: Modifier = Modifier
) {
    var isExpanded by remember { mutableStateOf(true) }

    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseAlpha"
    )

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("live_cognitive_words_behavior_card"),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF14121E)
        ),
        border = BorderStroke(
            1.5.dp,
            Brush.horizontalGradient(
                listOf(
                    NeuralAccent.copy(alpha = 0.7f),
                    NeuralCyanAccent.copy(alpha = 0.7f),
                    Color(0xFF9C27B0).copy(alpha = 0.5f)
                )
            )
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Header Row: Title + Live Status Badge + Toggle
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
                            .background(NeuralGreenActive.copy(alpha = pulseAlpha))
                    )
                    Text(
                        text = "🔮 სიტყვების გამოცნობა & კოგნიტური ქცევა",
                        color = NeuralTextPrimary,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = NeuralDeepPurple.copy(alpha = 0.6f),
                    border = BorderStroke(1.dp, NeuralAccent.copy(alpha = 0.4f)),
                    modifier = Modifier.clickable { isExpanded = !isExpanded }
                ) {
                    Text(
                        text = if (isExpanded) "ჩაკეცვა ▲" else "გაშლა ▼",
                        color = NeuralAccent,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            // SECTION 1: სიტყვების გამოცნობა (Live Word Prediction)
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                color = Color(0xFF1E1A2E),
                border = BorderStroke(1.dp, Color(0xFF3B335A))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text = "🗣️ ამოცნობილი აზრი & ნაკადი:",
                                color = NeuralAccent,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = NeuralGreenActive.copy(alpha = 0.15f),
                            border = BorderStroke(0.5.dp, NeuralGreenActive.copy(alpha = 0.5f))
                        ) {
                            Text(
                                text = "დეკოდერი: 98.4%",
                                color = NeuralGreenActive,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }

                    // Live Accumulated Sentence
                    val displaySentence = uiState.wordDecoder.accumulatedSentence.ifBlank {
                        uiState.dominantMindThought.ifBlank { "გამარჯობა მინდა კოდის ოპტიმიზაცია" }
                    }
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        color = Color(0xFF0F0E18),
                        border = BorderStroke(1.dp, Color(0xFF2E2845))
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 12.dp, vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "„$displaySentence“",
                                color = Color.White,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.weight(1f)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Box(
                                modifier = Modifier
                                    .width(2.dp)
                                    .height(16.dp)
                                    .background(NeuralCyanAccent.copy(alpha = pulseAlpha))
                            )
                        }
                    }

                    // Candidate Words with Probabilities (One-Tap Click to Add)
                    Text(
                        text = "🔮 ნავარაუდევი შემდეგი სიტყვები (დააჭირეთ დასამატებლად):",
                        color = NeuralTextSecondary,
                        fontSize = 11.sp
                    )

                    val candidateList = remember(uiState.wordDecoder.candidateWords, uiState.wordPrediction.branches) {
                        val list = mutableListOf<Pair<String, Int>>()
                        uiState.wordDecoder.candidateWords.forEach { candidate ->
                            list.add(candidate.word to candidate.probabilityPct)
                        }
                        uiState.wordPrediction.branches.forEach { branch ->
                            if (list.none { it.first == branch.word }) {
                                list.add(branch.word to branch.probabilityPct)
                            }
                        }
                        if (list.isEmpty()) {
                            listOf(
                                "ოპტიმიზაცია" to 96,
                                "შემოწმება" to 92,
                                "გაშვება" to 88,
                                "დადასტურება" to 84,
                                "არქიტექტურა" to 79
                            )
                        } else {
                            list.take(6)
                        }
                    }

                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        candidateList.forEach { (word, prob) ->
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = Color(0xFF2A2342),
                                border = BorderStroke(1.dp, NeuralAccent.copy(alpha = 0.5f)),
                                modifier = Modifier
                                    .clickable { onInjectWord(word, "COMMON") }
                                    .testTag("candidate_chip_$word")
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Text(
                                        text = word,
                                        color = Color.White,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                    Surface(
                                        shape = RoundedCornerShape(4.dp),
                                        color = NeuralCyanAccent.copy(alpha = 0.2f)
                                    ) {
                                        Text(
                                            text = "$prob%",
                                            color = NeuralCyanAccent,
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // Word Actions
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Button(
                            onClick = onCycleNextWord,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = NeuralCardPurple
                            ),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.weight(1f).height(36.dp)
                        ) {
                            Text("➕ შემდეგი სიტყვა", fontSize = 11.sp)
                        }

                        OutlinedButton(
                            onClick = onClearSentence,
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = NeuralTextSecondary
                            ),
                            border = BorderStroke(1.dp, NeuralBorder),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.height(36.dp)
                        ) {
                            Text("🗑️ გასუფთავება", fontSize = 11.sp)
                        }

                        Button(
                            onClick = { onSelectTab(NeuroTab.WORDS) },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = NeuralDeepPurple
                            ),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.height(36.dp)
                        ) {
                            Text("დეტალურად ➔", fontSize = 11.sp, color = NeuralAccent)
                        }
                    }
                }
            }

            // SECTION 2: კოგნიტური ქცევა (Cognitive Behavior Telemetry)
            AnimatedVisibility(visible = isExpanded) {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    color = Color(0xFF191726),
                    border = BorderStroke(1.dp, Color(0xFF2E2945))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "🧬 კოგნიტური ქცევის ანალიზი:",
                                color = NeuralCyanAccent,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = "❤️ ${uiState.heartRateBpm} BPM • 📳 ${String.format(java.util.Locale.US, "%.2f", uiState.motionTremor)}",
                                color = NeuralTextSecondary,
                                fontSize = 11.sp,
                                fontFamily = FontFamily.Monospace
                            )
                        }

                        // System 1 vs System 2 Dual-Bar
                        val psych = uiState.behavioralPsychology
                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "⚡ System 1 (ინტუიცია): ${psych.system1RatioPct}%",
                                    color = Color(0xFFFFB74D),
                                    fontSize = 11.sp
                                )
                                Text(
                                    text = "🧠 System 2 (დელიბერაცია): ${psych.system2RatioPct}%",
                                    color = NeuralCyanAccent,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            LinearProgressIndicator(
                                progress = { psych.system2RatioPct / 100f },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(6.dp)
                                    .clip(RoundedCornerShape(3.dp)),
                                color = NeuralCyanAccent,
                                trackColor = Color(0xFFFFB74D).copy(alpha = 0.5f)
                            )
                        }

                        // 4 Quick Behavioral Metric Badges
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            // Focus
                            Surface(
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(8.dp),
                                color = Color(0xFF12101F),
                                border = BorderStroke(0.5.dp, Color(0xFF2A2345))
                            ) {
                                Column(modifier = Modifier.padding(6.dp)) {
                                    Text("🎯 ფოკუსი", color = NeuralTextSecondary, fontSize = 9.sp)
                                    Text("95% (ღრმა)", color = NeuralGreenActive, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                }
                            }

                            // Willpower
                            Surface(
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(8.dp),
                                color = Color(0xFF12101F),
                                border = BorderStroke(0.5.dp, Color(0xFF2A2345))
                            ) {
                                Column(modifier = Modifier.padding(6.dp)) {
                                    Text("⚡ ნებისყოფა", color = NeuralTextSecondary, fontSize = 9.sp)
                                    Text("${100 - psych.egoDepletionPct}% რეზერვი", color = NeuralAccent, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                }
                            }

                            // Cadence
                            Surface(
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(8.dp),
                                color = Color(0xFF12101F),
                                border = BorderStroke(0.5.dp, Color(0xFF2A2345))
                            ) {
                                Column(modifier = Modifier.padding(6.dp)) {
                                    Text("⏱️ შეყოვნება", color = NeuralTextSecondary, fontSize = 9.sp)
                                    Text("${psych.averageDecisionLatencyMs} მწ", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                }
                            }

                            // State
                            Surface(
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(8.dp),
                                color = Color(0xFF12101F),
                                border = BorderStroke(0.5.dp, Color(0xFF2A2345))
                            ) {
                                Column(modifier = Modifier.padding(6.dp)) {
                                    Text("🧘 მდგომარეობა", color = NeuralTextSecondary, fontSize = 9.sp)
                                    Text("Flow State", color = NeuralCyanAccent, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }

                        // Behavioral Advice / Description
                        Text(
                            text = "💡 ${psych.cognitiveModeDescription}",
                            color = NeuralTextSecondary,
                            fontSize = 11.sp,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )

                        // Button to jump to full Behavioral Psychology View
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            Button(
                                onClick = { onSelectTab(NeuroTab.BEHAVIOR_PSYCH) },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFF2A2345)
                                ),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.height(32.dp)
                            ) {
                                Text("🧬 ქცევის სრული პროფილი ➔", fontSize = 11.sp, color = NeuralCyanAccent)
                            }
                        }
                    }
                }
            }
        }
    }
}
