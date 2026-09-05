package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.service.AdaptivePersonalProfileEngine
import com.example.service.GlobalCognitiveWorkspaceEngine
import com.example.service.HybridCognitiveEngine
import com.example.service.LocalAdversarialSelfPlayEngine
import com.example.service.LocalConsensusArbitrator
import com.example.service.LocalEpisodicMemoryGraph
import com.example.service.LocalEvolutionaryBrain
import com.example.service.LocalNeuralTransformerAi
import com.example.service.MultiNeuralNetworkEcosystem
import com.example.service.SpikingNeuralNetworkEngine
import com.example.ui.theme.AppIcons
import com.example.ui.theme.NeuralAccent
import com.example.ui.theme.NeuralBackground
import com.example.ui.theme.NeuralDeepPurple
import com.example.ui.theme.NeuralSurface
import com.example.ui.theme.NeuralTextPrimary
import com.example.ui.theme.NeuralTextSecondary
import java.util.Locale
import kotlin.math.roundToInt

@Composable
fun LocalEvolutionaryBrainCard(localBrain: LocalEvolutionaryBrain.LocalBrainTelemetry) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF0D1B2A),
                        Color(0xFF1B263B),
                        Color(0xFF243B55)
                    )
                )
            )
            .border(1.5.dp, Color(0xFF00E5FF).copy(alpha = 0.55f), RoundedCornerShape(14.dp))
            .padding(14.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF00E5FF).copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "🧬", fontSize = 14.sp)
                    }
                    Column {
                        Text(
                            text = "ლოკალური ევოლუციური ტვინი (Local Brain)",
                            color = Color(0xFF80DEEA),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "ალგორითმული მართვა & უწყვეტი დახვეწა",
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(Color(0xFF00E5FF).copy(alpha = 0.2f))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "Gen #${localBrain.evolutionGeneration}",
                            color = Color(0xFFE0F7FA),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(Color(0xFFFFD54F).copy(alpha = 0.25f))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "${localBrain.experiencePoints} XP",
                            color = Color(0xFFFFECB3),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // Active Strategy Card
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color.Black.copy(alpha = 0.45f))
                    .border(1.dp, Color(0xFF00E5FF).copy(alpha = 0.25f), RoundedCornerShape(10.dp))
                    .padding(10.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "არჩეული მიდგომა: ${localBrain.activeStrategy.titleKa}",
                            color = Color(0xFFE0F7FA),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.weight(1f)
                        )
                        Text(
                            text = "${(localBrain.activeStrategy.efficacyScore * 100).toInt()}% ეფექტურობა",
                            color = Color(0xFF69F0AE),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Text(
                        text = localBrain.activeStrategy.descriptionKa,
                        color = Color(0xFFB0BEC5),
                        fontSize = 11.sp,
                        lineHeight = 15.sp
                    )

                    // Efficacy Progress Bar
                    LinearProgressIndicator(
                        progress = { localBrain.activeStrategy.efficacyScore },
                        modifier = Modifier.fillMaxWidth().height(4.dp).clip(CircleShape),
                        color = Color(0xFF00E5FF),
                        trackColor = Color.White.copy(alpha = 0.1f)
                    )

                    // Adaptation Status Message
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = localBrain.adaptationStatusKa,
                            color = Color(0xFFFFD54F),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = "STDP სინაფსი: ${(localBrain.activeStrategy.synapticWeight * 100).toInt()}%",
                            color = Color(0xFF80CBC4),
                            fontSize = 10.sp
                        )
                    }
                }
            }

            // Algorithmic Plan Steps
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = "⚙️ ალგორითმული ორგანიზების გეგმა (Gemini AI + On-Device):",
                    color = Color(0xFF80DEEA),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold
                )
                localBrain.algorithmicPlanSteps.forEach { step ->
                    Row(
                        modifier = Modifier.padding(start = 4.dp),
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Text(text = "▸", color = Color(0xFF00E5FF), fontSize = 11.sp)
                        Text(
                            text = step,
                            color = Color(0xFFECEFF1),
                            fontSize = 10.5.sp,
                            lineHeight = 15.sp
                        )
                    }
                }
            }

            // Ranked Strategies (Evolutionary Leaderboard)
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = "🏆 სტრატეგიების ევოლუციური რეიტინგი (დაგროვილი გამოცდილება):",
                    color = Color(0xFFB0BEC5),
                    fontSize = 10.5.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Row(
                    modifier = Modifier.horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    localBrain.topStrategiesRanked.forEach { rank ->
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color.Black.copy(alpha = 0.35f))
                                .border(1.dp, if (rank.id == localBrain.activeStrategy.id) Color(0xFF00E5FF) else Color.White.copy(alpha = 0.1f), RoundedCornerShape(8.dp))
                                .padding(horizontal = 8.dp, vertical = 6.dp)
                        ) {
                            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                                Text(
                                    text = rank.titleKa.take(22) + if (rank.titleKa.length > 22) "…" else "",
                                    color = if (rank.id == localBrain.activeStrategy.id) Color(0xFF00E5FF) else Color.White,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                    Text(
                                        text = "${rank.efficacyPct}% მოგება",
                                        color = Color(0xFF69F0AE),
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = "გამოყენება: ${rank.usageCount}",
                                        color = Color(0xFF90A4AE),
                                        fontSize = 9.sp
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
fun LocalNeuralTransformerAiCard(transformer: LocalNeuralTransformerAi.LocalTransformerTelemetry) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF2A0845),
                        Color(0xFF1B0A2A),
                        Color(0xFF0F172A)
                    )
                )
            )
            .border(1.5.dp, Color(0xFFC084FC).copy(alpha = 0.6f), RoundedCornerShape(14.dp))
            .padding(14.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFC084FC).copy(alpha = 0.25f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "⚡", fontSize = 14.sp)
                    }
                    Column {
                        Text(
                            text = "ლოკალური ნეირო-ტრანსფორმერი (Local AI)",
                            color = Color(0xFFE9D5FF),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "4-Head Self-Attention • 8-არხიანი Fusion",
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(Color(0xFFA855F7).copy(alpha = 0.3f))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = if (transformer.isOnlineDistilled) "Distill #${transformer.distillationStepCount}" else "On-Device",
                            color = Color(0xFFF3E8FF),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(Color(0xFF06B6D4).copy(alpha = 0.25f))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "Norm ${String.format(Locale.US, "%.1f", transformer.latentEmbeddingNorm)}",
                            color = Color(0xFFCFFAFE),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // Synthesized Thought Box
            if (transformer.synthesizedThought.isNotBlank()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color.Black.copy(alpha = 0.4f))
                        .border(1.dp, Color(0xFFA855F7).copy(alpha = 0.35f), RoundedCornerShape(10.dp))
                        .padding(10.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(text = "💡", fontSize = 12.sp)
                            Text(
                                text = "ტრანსფორმერის მიერ სინთეზირებული აზრი:",
                                color = Color(0xFFD8B4FE),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                        Text(
                            text = transformer.synthesizedThought,
                            color = Color(0xFFFAF5FF),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            lineHeight = 17.sp
                        )
                    }
                }
            }

            // Attention Heatmap across 8 channels
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "🎯 Self-Attention ყურადღების განაწილება (8 არხი):",
                        color = Color(0xFFD8B4FE),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = "დომინანტი: ${transformer.dominantAttentionModality}",
                        color = Color(0xFFF472B6),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                transformer.attentionDistribution.entries.chunked(2).forEach { rowEntries ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        rowEntries.forEach { entry ->
                            val isDominant = entry.key == transformer.dominantAttentionModality
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (isDominant) Color(0xFFA855F7).copy(alpha = 0.2f) else Color.Black.copy(alpha = 0.3f))
                                    .border(
                                        1.dp,
                                        if (isDominant) Color(0xFFEC4899).copy(alpha = 0.5f) else Color.White.copy(alpha = 0.08f),
                                        RoundedCornerShape(8.dp)
                                    )
                                    .padding(horizontal = 8.dp, vertical = 6.dp)
                            ) {
                                Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(
                                            text = entry.key,
                                            color = if (isDominant) Color(0xFFF472B6) else Color(0xFFCBD5E1),
                                            fontSize = 10.sp,
                                            fontWeight = if (isDominant) FontWeight.Bold else FontWeight.Normal
                                        )
                                        Text(
                                            text = "${(entry.value * 100).roundToInt()}%",
                                            color = if (isDominant) Color(0xFFF472B6) else Color(0xFF94A3B8),
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                    LinearProgressIndicator(
                                        progress = { entry.value.coerceIn(0f, 1f) },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(3.dp)
                                            .clip(CircleShape),
                                        color = if (isDominant) Color(0xFFEC4899) else Color(0xFFA855F7),
                                        trackColor = Color.White.copy(alpha = 0.1f)
                                    )
                                }
                            }
                        }
                        if (rowEntries.size == 1) {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }
            }

            // Next-Token Predictions from Transformer Decoder
            if (transformer.predictedTokens.isNotEmpty()) {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text = "🔮 დეკოდერის პროგნოზირებული ტოკენები (Cross-Entropy):",
                        color = Color(0xFFCBD5E1),
                        fontSize = 10.5.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Row(
                        modifier = Modifier.horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        transformer.predictedTokens.forEachIndexed { idx, pair ->
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Color(0xFF3B0764).copy(alpha = 0.45f))
                                    .border(1.dp, Color(0xFFC084FC).copy(alpha = 0.3f), RoundedCornerShape(8.dp))
                                    .padding(horizontal = 8.dp, vertical = 5.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                                ) {
                                    Text(
                                        text = "#${idx + 1} ${pair.first}",
                                        color = Color.White,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Box(
                                        modifier = Modifier
                                            .clip(CircleShape)
                                            .background(Color(0xFF10B981).copy(alpha = 0.25f))
                                            .padding(horizontal = 4.dp, vertical = 1.dp)
                                    ) {
                                        Text(
                                            text = "${(pair.second * 100).roundToInt()}%",
                                            color = Color(0xFF6EE7B7),
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Knowledge Distillation Status
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(6.dp))
                    .background(Color.White.copy(alpha = 0.05f))
                    .padding(horizontal = 8.dp, vertical = 5.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (transformer.isOnlineDistilled)
                        "✨ Cloud AI ცოდნის დისტილაცია (Online Calibration)"
                    else
                        "🔒 100% On-Device ავტონომიური Self-Attention რეჟიმი",
                    color = if (transformer.isOnlineDistilled) Color(0xFFFDE047) else Color(0xFF94A3B8),
                    fontSize = 9.5.sp,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = "${transformer.activeMultiModalChannelsCount}/8 არხი",
                    color = Color(0xFFA78BFA),
                    fontSize = 9.5.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun AdaptivePersonalProfileCard(
    profile: AdaptivePersonalProfileEngine.AdaptivePersonalProfile,
    guidance: String = ""
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF064E3B),
                        Color(0xFF065F46),
                        Color(0xFF0F172A)
                    )
                )
            )
            .border(1.5.dp, Color(0xFF10B981).copy(alpha = 0.6f), RoundedCornerShape(14.dp))
            .padding(14.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF10B981).copy(alpha = 0.25f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "🎯", fontSize = 14.sp)
                    }
                    Column {
                        Text(
                            text = "ინდივიდუალური ადაპტაციური პროფილი",
                            color = Color(0xFFA7F3D0),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "ავტონომიური თვით-კალიბრაცია & ბაზისები",
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                }
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFF10B981).copy(alpha = 0.25f))
                        .border(1.dp, Color(0xFF10B981), RoundedCornerShape(8.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "${profile.personalAdaptationScorePct}% მორგება",
                        color = Color(0xFF6EE7B7),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Baselines Grid
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color.Black.copy(alpha = 0.35f))
                        .padding(8.dp)
                ) {
                    Text(
                        text = "ბაზისური HR (პულსი)",
                        color = Color(0xFF94A3B8),
                        fontSize = 9.sp
                    )
                    Text(
                        text = "${profile.baselineHeartRateBpm.toInt()} BPM",
                        color = Color.White,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Δ = ${String.format(Locale.US, "%+.1f", profile.currentHrDeviationFromBaseline)} BPM",
                        color = if (profile.currentHrDeviationFromBaseline > 3f) Color(0xFFF87171) else Color(0xFF6EE7B7),
                        fontSize = 9.5.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color.Black.copy(alpha = 0.35f))
                        .padding(8.dp)
                ) {
                    Text(
                        text = "მოსვენების გუგა",
                        color = Color(0xFF94A3B8),
                        fontSize = 9.sp
                    )
                    Text(
                        text = "${String.format(Locale.US, "%.2f", profile.baselinePupilDiameterMm)} მმ",
                        color = Color.White,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Δ = ${String.format(Locale.US, "%+.2f", profile.currentPupilDeviationFromBaselineMm)} მმ",
                        color = Color(0xFF6EE7B7),
                        fontSize = 9.5.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color.Black.copy(alpha = 0.35f))
                        .padding(8.dp)
                ) {
                    Text(
                        text = "რეაქციის ლატენტობა",
                        color = Color(0xFF94A3B8),
                        fontSize = 9.sp
                    )
                    Text(
                        text = "${profile.baselineReactionLatencyMs} ms",
                        color = Color.White,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = profile.circadianChronotype.take(12),
                        color = Color(0xFF38BDF8),
                        fontSize = 9.5.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            // Adaptation State Footer
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(6.dp))
                    .background(Color.White.copy(alpha = 0.05f))
                    .padding(horizontal = 8.dp, vertical = 5.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "🌱 ${profile.adaptationStateDescription}",
                    color = Color(0xFFA7F3D0),
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = "სულ: ${profile.totalLifetimeInferences} ციკლი • XP: ${profile.totalExperiencePoints}",
                    color = Color(0xFFFDE047),
                    fontSize = 9.5.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            // Behavioral Guidance Banner if present
            if (guidance.isNotBlank()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFF0284C7).copy(alpha = 0.2f))
                        .border(1.dp, Color(0xFF38BDF8).copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                        .padding(8.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(text = "🧭", fontSize = 13.sp)
                        Text(
                            text = guidance,
                            color = Color(0xFFE0F2FE),
                            fontSize = 11.sp,
                            lineHeight = 15.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CognitiveInsightsTab(result: HybridCognitiveEngine.CognitiveResult?) {
    // 0. Adaptive Personal Profile & Baselines Card
    result?.adaptiveProfile?.let { profile ->
        AdaptivePersonalProfileCard(
            profile = profile,
            guidance = result.behavioralGuidance
        )
    }

    // 1. Active Cognitive Task & Solution Card
    if (!result?.activeCognitiveTask.isNullOrBlank()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF1E1B4B),
                            Color(0xFF0F172A)
                        )
                    )
                )
                .border(1.5.dp, Color(0xFF6366F1).copy(alpha = 0.6f), RoundedCornerShape(14.dp))
                .padding(14.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF6366F1).copy(alpha = 0.3f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "🎯", fontSize = 13.sp)
                    }
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "კოგნიტური ამოცანის ამოხსნა (AI Task Solver)",
                            color = Color(0xFFA5B4FC),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = result?.activeCognitiveTask.orEmpty(),
                            color = Color.White,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(Color(0xFF6366F1).copy(alpha = 0.25f))
                            .padding(horizontal = 6.dp, vertical = 3.dp)
                    ) {
                        Text(
                            text = result?.taskCategory.orEmpty(),
                            color = Color(0xFFC7D2FE),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                if (!result?.cognitiveTaskSolution.isNullOrBlank()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color.Black.copy(alpha = 0.4f))
                            .border(1.dp, Color(0xFF818CF8).copy(alpha = 0.2f), RoundedCornerShape(10.dp))
                            .padding(10.dp)
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text(
                                text = "💡 AI გადაწყვეტა & აზროვნების დახმარება:",
                                color = Color(0xFFFDE047),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = result?.cognitiveTaskSolution.orEmpty(),
                                color = Color(0xFFF1F5F9),
                                fontSize = 12.sp,
                                lineHeight = 17.sp
                            )
                        }
                    }
                }

                if (result?.taskReasoningSteps?.isNotEmpty() == true) {
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(
                            text = "🪜 ეტაპობრივი ამოხსნის საფეხურები:",
                            color = Color(0xFF94A3B8),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        result.taskReasoningSteps.forEach { step ->
                            Row(
                                modifier = Modifier.padding(start = 4.dp),
                                horizontalArrangement = Arrangement.spacedBy(6.dp),
                                verticalAlignment = Alignment.Top
                            ) {
                                Text(text = "•", color = Color(0xFF60A5FA), fontSize = 12.sp)
                                Text(
                                    text = step,
                                    color = Color(0xFFE2E8F0),
                                    fontSize = 11.sp,
                                    lineHeight = 16.sp
                                )
                            }
                        }
                    }
                }

                if (!result?.thinkingAidAdvice.isNullOrBlank()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFF0284C7).copy(alpha = 0.15f))
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "⚡", fontSize = 12.sp)
                        Text(
                            text = "რჩევა აზროვნებისთვის: ${result.thinkingAidAdvice}",
                            color = Color(0xFFBAE6FD),
                            fontSize = 11.sp
                        )
                    }
                }
            }
        }
    }

    // 2. Self-Evolving Local Brain (Reinforcement & Evolutionary Meta-Learning)
    result?.localBrainTelemetry?.let { localBrain ->
        LocalEvolutionaryBrainCard(localBrain = localBrain)
    }

    // 3. On-Device Neural Transformer AI (Self-Attention & Latent Space Synthesis)
    result?.localTransformerTelemetry?.let { transformer ->
        LocalNeuralTransformerAiCard(transformer = transformer)
    }

    val synthesisText = result?.deepSynthesisText
        ?: "სააზროვნო ბირთვი ამუშავებს მზა ანალიტიკას (კამერა, მიკროფონი, სენსორები)..."

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(NeuralBackground.copy(alpha = 0.85f))
            .border(1.dp, NeuralAccent.copy(alpha = 0.25f), RoundedCornerShape(14.dp))
            .padding(14.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(
                    imageVector = AppIcons.Lightbulb,
                    contentDescription = null,
                    tint = NeuralAccent,
                    modifier = Modifier.size(15.dp)
                )
                Text(
                    text = "აზრობრივი სინთეზი & შეფასება:",
                    color = NeuralAccent,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Text(
                text = synthesisText,
                color = Color.White,
                fontSize = 13.sp,
                lineHeight = 18.sp
            )
        }
    }

    result?.insights?.let { list ->
        if (list.isNotEmpty()) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "ნეირო-ანალიტიკის სინთეზური შრეები:",
                    color = NeuralTextSecondary,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold
                )
                list.forEach { insight ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(NeuralSurface.copy(alpha = 0.6f))
                            .border(1.dp, Color.White.copy(alpha = 0.05f), RoundedCornerShape(10.dp))
                            .padding(horizontal = 10.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .clip(CircleShape)
                                .background(NeuralAccent)
                        )
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = insight.title,
                                color = NeuralTextPrimary,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = insight.description,
                                color = NeuralTextSecondary,
                                fontSize = 11.sp
                            )
                        }
                        Text(
                            text = "${(insight.confidence * 100).toInt()}%",
                            color = NeuralAccent,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CognitiveLogicTab(result: HybridCognitiveEngine.CognitiveResult?) {
    val logicChain = result?.logicalDeductionChain ?: emptyList()
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Icon(
                imageVector = AppIcons.Psychology,
                contentDescription = null,
                tint = Color(0xFF00E5FF),
                modifier = Modifier.size(16.dp)
            )
            Text(
                text = "🔗 ლოგიკური დედუქციის ჯაჭვი (Chain of Thought):",
                color = NeuralTextPrimary,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
        }

        if (logicChain.isEmpty()) {
            Text(
                text = "ლოგიკური ჯაჭვი ფორმირდება...",
                color = NeuralTextSecondary,
                fontSize = 12.sp
            )
        } else {
            logicChain.forEachIndexed { index, step ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(NeuralSurface.copy(alpha = 0.6f))
                        .border(1.dp, Color(0xFF00E5FF).copy(alpha = 0.2f), RoundedCornerShape(10.dp))
                        .padding(10.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Box(
                        modifier = Modifier
                            .size(20.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF00E5FF).copy(alpha = 0.15f))
                            .border(1.dp, Color(0xFF00E5FF), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "${index + 1}",
                            color = Color(0xFF00E5FF),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Text(
                        text = step,
                        color = Color.White,
                        fontSize = 12.sp,
                        lineHeight = 16.sp,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
fun CognitiveAlgorithmsTab(result: HybridCognitiveEngine.CognitiveResult?) {
    val steps = result?.algorithmicSteps ?: emptyList()
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Icon(
                imageVector = AppIcons.Memory,
                contentDescription = null,
                tint = Color(0xFF00E676),
                modifier = Modifier.size(16.dp)
            )
            Text(
                text = "⚡ ალგორითმული გადაწყვეტილების ხე:",
                color = NeuralTextPrimary,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
        }

        if (steps.isEmpty()) {
            Text(
                text = "ალგორითმული საფეხურები მუშავდება...",
                color = NeuralTextSecondary,
                fontSize = 12.sp
            )
        } else {
            steps.forEach { node ->
                val statusColor = when (node.status) {
                    "OPTIMIZED" -> Color(0xFF00E676)
                    "ACTIVE" -> Color(0xFF00E5FF)
                    "COMPLETED" -> Color(0xFFFFD600)
                    else -> NeuralAccent
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(NeuralSurface.copy(alpha = 0.6f))
                        .border(1.dp, statusColor.copy(alpha = 0.25f), RoundedCornerShape(10.dp))
                        .padding(10.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "ნაბიჯი ${node.step}: ${node.stageName}",
                                color = statusColor,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(statusColor.copy(alpha = 0.15f))
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = node.status,
                                    color = statusColor,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                        Text(
                            text = node.description,
                            color = NeuralTextPrimary,
                            fontSize = 11.sp
                        )
                        Text(
                            text = "კოდი: ${node.conditionOrAction}",
                            color = NeuralTextSecondary,
                            fontSize = 10.sp,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CognitiveSnnTab(result: HybridCognitiveEngine.CognitiveResult?) {
    val snn = result?.snnTelemetry
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(NeuralBackground.copy(alpha = 0.85f))
                .border(1.dp, Color(0xFF00E5FF).copy(alpha = 0.4f), RoundedCornerShape(12.dp))
                .padding(12.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = AppIcons.Hub,
                            contentDescription = null,
                            tint = Color(0xFF00E5FF),
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = "SNN ბირთვი (80 LIF ნეირონი • 5 კლასტერი):",
                            color = Color.White,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Text(
                        text = "${String.format(Locale.US, "%.1f", snn?.totalSpikesPerSec ?: 24.5f)} Hz",
                        color = Color(0xFF00E5FF),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(8.dp))
                            .background(NeuralSurface.copy(alpha = 0.8f))
                            .padding(6.dp)
                    ) {
                        Column {
                            Text("დოფამინი DA", color = NeuralTextSecondary, fontSize = 9.sp)
                            Text(
                                "${String.format(Locale.US, "%.2f", snn?.neuromodulation?.dopamineLevel ?: 1.0f)}x",
                                color = Color(0xFF00E676),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(8.dp))
                            .background(NeuralSurface.copy(alpha = 0.8f))
                            .padding(6.dp)
                    ) {
                        Column {
                            Text("სეროტონინი 5-HT", color = NeuralTextSecondary, fontSize = 9.sp)
                            Text(
                                "${String.format(Locale.US, "%.2f", snn?.neuromodulation?.serotoninLevel ?: 1.0f)}x",
                                color = Color(0xFF00E5FF),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(8.dp))
                            .background(NeuralSurface.copy(alpha = 0.8f))
                            .padding(6.dp)
                    ) {
                        Column {
                            Text("ნორადრენალინი NA", color = NeuralTextSecondary, fontSize = 9.sp)
                            Text(
                                "${String.format(Locale.US, "%.2f", snn?.neuromodulation?.noradrenalineLevel ?: 1.0f)}x",
                                color = Color(0xFFFF9100),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }

        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text(
                text = "ნეირონული კლასტერების აქტივაცია (STDP პლასტიკურობა):",
                color = NeuralTextSecondary,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold
            )
            snn?.clusterSpikeFrequencies?.forEach { (cluster, freq) ->
                val freqPct = ((freq / 60.0f) * 100).coerceIn(5f, 100f).roundToInt()
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = cluster.labelKa,
                        color = NeuralTextPrimary,
                        fontSize = 10.sp,
                        modifier = Modifier.width(130.dp)
                    )
                    LinearProgressIndicator(
                        progress = { freqPct / 100f },
                        modifier = Modifier.weight(1f).height(4.dp).clip(CircleShape),
                        color = Color(0xFF00E5FF),
                        trackColor = Color.White.copy(alpha = 0.1f)
                    )
                    Text(
                        text = "$freqPct%",
                        color = Color(0xFF00E5FF),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.width(32.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun CognitiveHtmTab(result: HybridCognitiveEngine.CognitiveResult?) {
    val htm = result?.ecosystemTelemetry?.htmTelemetry
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(NeuralBackground.copy(alpha = 0.85f))
                .border(1.dp, Color(0xFF00E676).copy(alpha = 0.4f), RoundedCornerShape(12.dp))
                .padding(12.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = AppIcons.Psychology,
                            contentDescription = null,
                            tint = Color(0xFF00E676),
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = "HTM კორტიკალური სვეტები (${htm?.activeColumnsCount ?: 40} სვეტი):",
                            color = Color.White,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Text(
                        text = "${String.format(Locale.US, "%.1f", htm?.sdrSparsityPercentage ?: 10f)}% SDR",
                        color = Color(0xFF00E676),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(8.dp))
                            .background(NeuralSurface.copy(alpha = 0.8f))
                            .padding(6.dp)
                    ) {
                        Column {
                            Text("პროგნოზირებული უჯრედები", color = NeuralTextSecondary, fontSize = 9.sp)
                            Text(
                                "${htm?.predictiveCellsCount ?: 12} უჯრედი",
                                color = Color(0xFF00E676),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(8.dp))
                            .background(NeuralSurface.copy(alpha = 0.8f))
                            .padding(6.dp)
                    ) {
                        Column {
                            Text("ანომალიის ინდექსი", color = NeuralTextSecondary, fontSize = 9.sp)
                            Text(
                                String.format(Locale.US, "%.2f", htm?.anomalyScore ?: 0.05f),
                                color = if ((htm?.anomalyScore ?: 0f) > 0.4f) Color(0xFFFF9100) else Color(0xFF00E5FF),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(8.dp))
                            .background(NeuralSurface.copy(alpha = 0.8f))
                            .padding(6.dp)
                    ) {
                        Column {
                            Text("მიმდევრობის კოჰერენტულობა", color = NeuralTextSecondary, fontSize = 9.sp)
                            Text(
                                "${String.format(Locale.US, "%.2f", htm?.sequenceCoherence ?: 0.85f)}",
                                color = Color(0xFFB388FF),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp))
                .background(NeuralSurface.copy(alpha = 0.6f))
                .padding(10.dp)
        ) {
            Text(
                text = "Hierarchical Temporal Memory (HTM) მოდელი უწყვეტად სწავლობს დროით თანმიმდევრობებს და აკეთებს პროგნოზებს SDR (Sparse Distributed Representations) პრინციპით.",
                color = NeuralTextSecondary,
                fontSize = 11.sp,
                lineHeight = 15.sp
            )
        }
    }
}

@Composable
fun CognitiveHopfieldTab(result: HybridCognitiveEngine.CognitiveResult?) {
    val hopfield = result?.ecosystemTelemetry?.hopfieldTelemetry
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(10.dp))
                    .background(NeuralSurface.copy(alpha = 0.7f))
                    .border(1.dp, Color(0xFFFFD600).copy(alpha = 0.3f), RoundedCornerShape(10.dp))
                    .padding(8.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    Text("ქსელის ენერგია E", color = NeuralTextSecondary, fontSize = 9.sp)
                    Text(
                        "${String.format(Locale.US, "%.1f", hopfield?.energy ?: -12.4f)}",
                        color = Color(0xFFFFD600),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(10.dp))
                    .background(NeuralSurface.copy(alpha = 0.7f))
                    .border(1.dp, Color(0xFF00E5FF).copy(alpha = 0.3f), RoundedCornerShape(10.dp))
                    .padding(8.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    Text("ენერგიის ΔE", color = NeuralTextSecondary, fontSize = 9.sp)
                    Text(
                        "${String.format(Locale.US, "%.3f", hopfield?.energyDelta ?: -0.012f)}",
                        color = Color(0xFF00E5FF),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(10.dp))
                    .background(NeuralSurface.copy(alpha = 0.7f))
                    .border(1.dp, Color(0xFFB388FF).copy(alpha = 0.3f), RoundedCornerShape(10.dp))
                    .padding(8.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    Text("მსგავსება", color = NeuralTextSecondary, fontSize = 9.sp)
                    Text(
                        "${((hopfield?.similarityScore ?: 0.88f) * 100).roundToInt()}%",
                        color = Color(0xFFB388FF),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(
                    Brush.horizontalGradient(
                        listOf(
                            Color(0xFFFFD600).copy(alpha = 0.15f),
                            NeuralDeepPurple.copy(alpha = 0.5f)
                        )
                    )
                )
                .border(1.dp, Color(0xFFFFD600).copy(alpha = 0.4f), RoundedCornerShape(12.dp))
                .padding(12.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "ამოცნობილი ასოციაციური მეხსიერება:",
                        color = NeuralTextSecondary,
                        fontSize = 10.sp
                    )
                    Text(
                        text = "${hopfield?.storedMemoriesCount ?: 8} შენახული პატერნი",
                        color = Color(0xFFFFD600),
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Text(
                    text = "„${hopfield?.recalledPatternLabel ?: "ღრმა კონცენტრაცია და ალგორითმული Flow"}“",
                    color = Color.White,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "Modern Hopfield ქსელი უწყვეტი განახლებით პოულობს უახლოეს ენერგეტიკულ მინიმუმს და ასოციაციურ მეხსიერებას გადასცემს Gemini AI-სა და HTM-ს.",
                    color = NeuralTextSecondary,
                    fontSize = 10.sp,
                    lineHeight = 14.sp
                )
            }
        }
    }
}

@Composable
fun CognitiveConceptsTab(result: HybridCognitiveEngine.CognitiveResult?) {
    val concepts = result?.conceptHierarchy ?: emptyList()
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Icon(
                imageVector = AppIcons.AccountTree,
                contentDescription = null,
                tint = Color(0xFFFF9100),
                modifier = Modifier.size(16.dp)
            )
            Text(
                text = "📊 კონცეპტუალური იერარქია & პრიორიტეტები:",
                color = NeuralTextPrimary,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
        }

        if (concepts.isEmpty()) {
            Text(
                text = "კონცეფციების იერარქია მუშავდება...",
                color = NeuralTextSecondary,
                fontSize = 12.sp
            )
        } else {
            concepts.forEach { conceptNode ->
                val prioColor = when (conceptNode.priority) {
                    "HIGH" -> Color(0xFFFF5252)
                    "MEDIUM" -> Color(0xFFFF9100)
                    else -> Color(0xFF00E5FF)
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(NeuralSurface.copy(alpha = 0.6f))
                        .border(1.dp, prioColor.copy(alpha = 0.3f), RoundedCornerShape(10.dp))
                        .padding(10.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(prioColor.copy(alpha = 0.15f))
                                        .border(1.dp, prioColor.copy(alpha = 0.5f), RoundedCornerShape(6.dp))
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = conceptNode.category,
                                        color = prioColor,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                                Text(
                                    text = conceptNode.concept,
                                    color = Color.White,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            Text(
                                text = "${conceptNode.weightPct}%",
                                color = prioColor,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        LinearProgressIndicator(
                            progress = { conceptNode.weightPct / 100f },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(3.dp)
                                .clip(CircleShape),
                            color = prioColor,
                            trackColor = Color.White.copy(alpha = 0.08f)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CognitiveMeshBusTab(result: HybridCognitiveEngine.CognitiveResult?) {
    val ecosystem = result?.ecosystemTelemetry
    val liveSignals: List<MultiNeuralNetworkEcosystem.InterNeuralSignal> = ecosystem?.liveSignals ?: emptyList()
    val synergy = ecosystem?.globalSynergyScore ?: 94.8f
    val activePipes = ecosystem?.totalActiveSynapticPipes ?: 152

    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .background(
                    Brush.horizontalGradient(
                        listOf(
                            Color(0xFF00E5FF).copy(alpha = 0.15f),
                            NeuralDeepPurple.copy(alpha = 0.7f),
                            Color(0xFF00E676).copy(alpha = 0.15f)
                        )
                    )
                )
                .border(1.dp, Color(0xFF00E5FF).copy(alpha = 0.4f), RoundedCornerShape(14.dp))
                .padding(12.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = AppIcons.Hub,
                            contentDescription = null,
                            tint = Color(0xFF00E5FF),
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            text = "მულტი-ნეირონული სინაფსური ხიდი:",
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFF00E676).copy(alpha = 0.18f))
                            .border(1.dp, Color(0xFF00E676).copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                        Text(
                            text = "100% ორმხრივი სინქრონი",
                            color = Color(0xFF00E676),
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "გლობალური სინერგია: ${String.format(Locale.US, "%.1f", synergy)}%",
                            color = Color(0xFF00E5FF),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(modifier = Modifier.height(3.dp))
                        LinearProgressIndicator(
                            progress = { synergy / 100f },
                            modifier = Modifier.fillMaxWidth().height(4.dp).clip(CircleShape),
                            color = Color(0xFF00E5FF),
                            trackColor = Color.White.copy(alpha = 0.1f)
                        )
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "აქტიური მილები: $activePipes არხი",
                            color = Color(0xFF00E676),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(modifier = Modifier.height(3.dp))
                        LinearProgressIndicator(
                            progress = { 0.96f },
                            modifier = Modifier.fillMaxWidth().height(4.dp).clip(CircleShape),
                            color = Color(0xFF00E676),
                            trackColor = Color.White.copy(alpha = 0.1f)
                        )
                    }
                }

                Text(
                    text = ecosystem?.interNetworkCrossTalkSummary ?: "SNN (80 LIF) ⇄ HTM (40 სვეტი) ⇄ Hopfield (32D) ⇄ Gemini Cloud AI",
                    color = NeuralTextSecondary,
                    fontSize = 10.sp,
                    lineHeight = 14.sp
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            val nodes = listOf(
                "⚡ SNN (იმპულსები)" to Color(0xFF00E5FF),
                "🧬 HTM (კორტიკალური სვეტები)" to Color(0xFF00E676),
                "🔮 Hopfield (ასოციაციური მეხსიერება)" to Color(0xFFFFD600),
                "🧠 Polyvagal (ქცევა & სტრესი)" to Color(0xFFFF9100),
                "📊 Bayesian (ალბათური ინფერენცია)" to Color(0xFFB388FF),
                "🌐 Gemini (ღრუბლოვანი AI & დაუნლინკი)" to Color(0xFF00E5FF)
            )
            nodes.forEach { (name, color) ->
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(color.copy(alpha = 0.12f))
                        .border(1.dp, color.copy(alpha = 0.4f), RoundedCornerShape(8.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = name,
                        color = color,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }

        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "ცოცხალი პაკეტების & სიგნალების ნაკადი (Cross-Talk Bus):",
                    color = NeuralTextPrimary,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "LIVE <1ms",
                    color = Color(0xFF00E676),
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            if (liveSignals.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(NeuralSurface.copy(alpha = 0.6f))
                        .padding(10.dp)
                ) {
                    Text(
                        text = "⚡ სიგნალები უწყვეტად მიმოიცვლება SNN, HTM, Hopfield და Cloud AI მოდულებს შორის...",
                        color = NeuralTextSecondary,
                        fontSize = 11.sp
                    )
                }
            } else {
                liveSignals.take(6).forEach { signal ->
                    val sigColor = when {
                        signal.source.contains("SNN", ignoreCase = true) -> Color(0xFF00E5FF)
                        signal.source.contains("HTM", ignoreCase = true) -> Color(0xFF00E676)
                        signal.source.contains("Hopfield", ignoreCase = true) -> Color(0xFFFFD600)
                        signal.source.contains("Gemini", ignoreCase = true) -> Color(0xFFB388FF)
                        else -> NeuralAccent
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(NeuralSurface.copy(alpha = 0.7f))
                            .border(1.dp, sigColor.copy(alpha = 0.3f), RoundedCornerShape(10.dp))
                            .padding(8.dp)
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(6.dp)
                                            .clip(CircleShape)
                                            .background(sigColor)
                                    )
                                    Text(
                                        text = "${signal.source} ➔ ${signal.target}",
                                        color = sigColor,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                                Text(
                                    text = signal.signalType,
                                    color = NeuralTextSecondary,
                                    fontSize = 8.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                            Text(
                                text = signal.descriptionKa,
                                color = Color.White,
                                fontSize = 10.sp,
                                lineHeight = 13.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

// ==========================================
// ⚖️ SYSTEM 2 ARBITRATOR & GLOBAL WORKSPACE TAB
// ==========================================
@Composable
fun CognitiveArbitratorWorkspaceTab(result: HybridCognitiveEngine.CognitiveResult?) {
    val workspace = result?.globalWorkspaceTelemetry
    val arbitrator = result?.consensusVerdict
    val broadcast = workspace?.activeBroadcast

    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        // 1. Conscious Broadcast Banner (Global Attention Winner)
        if (broadcast != null) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(
                        Brush.horizontalGradient(
                            listOf(
                                Color(0xFF00E5FF).copy(alpha = 0.18f),
                                Color(0xFF7C4DFF).copy(alpha = 0.25f)
                            )
                        )
                    )
                    .border(1.2.dp, Color(0xFF00E5FF).copy(alpha = 0.5f), RoundedCornerShape(14.dp))
                    .padding(12.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(text = "🌐", fontSize = 14.sp)
                            Text(
                                text = "ცნობიერების გლობალური მაუწყებლობა",
                                color = Color(0xFF00E5FF),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(Color(0xFF00E5FF).copy(alpha = 0.15f))
                                .border(1.dp, Color(0xFF00E5FF).copy(alpha = 0.4f), RoundedCornerShape(6.dp))
                                .padding(horizontal = 7.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "კოჰერენტულობა: ${broadcast.globalCoherencePct}%",
                                color = Color(0xFF00E5FF),
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Text(
                        text = "„${broadcast.winningHypothesis}“",
                        color = Color.White,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        lineHeight = 17.sp
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "გამარჯვებული აგენტი: ${broadcast.winningAgentNameKa}",
                            color = Color(0xFFE0F7FA),
                            fontSize = 10.5.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = "${workspace.registeredAgentsCount} ლოკალური ქსელი",
                            color = NeuralTextSecondary,
                            fontSize = 10.sp
                        )
                    }

                    // Feedback directive across networks
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color.Black.copy(alpha = 0.35f))
                            .padding(8.dp)
                    ) {
                        Text(
                            text = "⚡ ${broadcast.broadcastFeedbackDirectiveKa}",
                            color = Color(0xFFA7F3D0),
                            fontSize = 10.sp,
                            lineHeight = 13.sp
                        )
                    }
                }
            }
        }

        // 2. System 2 Deliberation Arbitrator & Verifier
        if (arbitrator != null) {
            val badgeColor = Color(arbitrator.verdictType.badgeColorHex)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(NeuralSurface.copy(alpha = 0.7f))
                    .border(1.dp, badgeColor.copy(alpha = 0.45f), RoundedCornerShape(12.dp))
                    .padding(12.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(text = "⚖️", fontSize = 13.sp)
                            Text(
                                text = "System 2 არბიტრი & კრიტიკოსი",
                                color = Color.White,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(badgeColor.copy(alpha = 0.15f))
                                .border(1.dp, badgeColor.copy(alpha = 0.5f), RoundedCornerShape(6.dp))
                                .padding(horizontal = 7.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = arbitrator.verdictType.labelKa,
                                color = badgeColor,
                                fontSize = 9.5.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    // Progress Meters: Consensus & Somatic Congruence
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(text = "კონსენსუსი:", color = NeuralTextSecondary, fontSize = 10.sp)
                                Text(
                                    text = "${arbitrator.consensusScorePct}%",
                                    color = badgeColor,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Spacer(modifier = Modifier.height(3.dp))
                            LinearProgressIndicator(
                                progress = { arbitrator.consensusScorePct / 100f },
                                modifier = Modifier.fillMaxWidth().height(4.dp).clip(CircleShape),
                                color = badgeColor,
                                trackColor = Color.White.copy(alpha = 0.1f)
                            )
                        }
                        Column(modifier = Modifier.weight(1f)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(text = "🫀 სომატური თანხვედრა:", color = NeuralTextSecondary, fontSize = 10.sp)
                                Text(
                                    text = "${arbitrator.somaticCongruencePct}%",
                                    color = Color(0xFF38BDF8),
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Spacer(modifier = Modifier.height(3.dp))
                            LinearProgressIndicator(
                                progress = { arbitrator.somaticCongruencePct / 100f },
                                modifier = Modifier.fillMaxWidth().height(4.dp).clip(CircleShape),
                                color = Color(0xFF38BDF8),
                                trackColor = Color.White.copy(alpha = 0.1f)
                            )
                        }
                    }

                    Text(
                        text = arbitrator.arbitrationExplanationKa,
                        color = Color(0xFFE2E8F0),
                        fontSize = 11.sp,
                        lineHeight = 15.sp
                    )

                    // Deliberation Trace Steps
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(
                            text = "დელიბერაციის ლოგიკური ნაბიჯები:",
                            color = NeuralTextSecondary,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        arbitrator.deliberationSteps.take(4).forEach { step ->
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(Color.Black.copy(alpha = 0.25f))
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = step,
                                    color = Color(0xFFF1F5F9),
                                    fontSize = 10.sp,
                                    lineHeight = 13.sp
                                )
                            }
                        }
                    }
                }
            }
        }

        // 3. The Global Blackboard: Active Competing Proposals
        if (broadcast != null && broadcast.proposals.isNotEmpty()) {
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(
                    text = "📋 გლობალური სამუშაო დაფა (Blackboard) — შეჯიბრი:",
                    color = NeuralTextSecondary,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
                broadcast.proposals.forEach { prop ->
                    val isWinning = prop.agentId == broadcast.winningAgentId
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(if (isWinning) NeuralAccent.copy(alpha = 0.15f) else NeuralSurface.copy(alpha = 0.6f))
                            .border(
                                width = 1.dp,
                                color = if (isWinning) NeuralAccent else Color.White.copy(alpha = 0.1f),
                                shape = RoundedCornerShape(10.dp)
                            )
                            .padding(8.dp)
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                                ) {
                                    Text(text = prop.icon, fontSize = 12.sp)
                                    Text(
                                        text = prop.agentNameKa,
                                        color = if (isWinning) NeuralAccent else Color.White,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    if (isWinning) {
                                        Box(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(4.dp))
                                                .background(NeuralAccent.copy(alpha = 0.2f))
                                                .padding(horizontal = 4.dp, vertical = 1.dp)
                                        ) {
                                            Text(
                                                text = "გამარჯვებული",
                                                color = NeuralAccent,
                                                fontSize = 8.5.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                    }
                                }
                                Text(
                                    text = "${prop.confidenceScorePct}% რწმენა",
                                    color = if (isWinning) NeuralAccent else NeuralTextSecondary,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Text(
                                text = "ჰიპოთეზა: ${prop.proposedHypothesis}",
                                color = Color(0xFFF1F5F9),
                                fontSize = 10.5.sp,
                                lineHeight = 14.sp
                            )
                            Text(
                                text = prop.telemetryDetail,
                                color = NeuralTextSecondary,
                                fontSize = 9.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

// ==========================================
// 🔮 LOCAL EPISODIC MEMORY GRAPH TAB
// ==========================================
@Composable
fun CognitiveEpisodicMemoryTab(result: HybridCognitiveEngine.CognitiveResult?) {
    val recall = result?.episodicMemoryRecall
    val topMatch = recall?.topMatch

    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        // Nearest Recall Banner
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(
                    Brush.horizontalGradient(
                        listOf(
                            Color(0xFFFFD600).copy(alpha = 0.15f),
                            Color(0xFF00E5FF).copy(alpha = 0.12f)
                        )
                    )
                )
                .border(1.dp, Color(0xFFFFD600).copy(alpha = 0.45f), RoundedCornerShape(12.dp))
                .padding(12.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(text = "🔮", fontSize = 13.sp)
                        Text(
                            text = "ასოციაციური ეპიზოდური მეხსიერება",
                            color = Color(0xFFFFD600),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(Color(0xFFFFD600).copy(alpha = 0.15f))
                            .border(1.dp, Color(0xFFFFD600).copy(alpha = 0.4f), RoundedCornerShape(6.dp))
                            .padding(horizontal = 7.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "${recall?.similarityScorePct ?: 0}% Cos-Sim",
                            color = Color(0xFFFFD600),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Text(
                    text = recall?.associativeExplanationKa ?: "ეპიზოდური მეხსიერების ანალიზი...",
                    color = Color.White,
                    fontSize = 11.sp,
                    lineHeight = 15.sp
                )

                if (topMatch != null) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color.Black.copy(alpha = 0.35f))
                            .padding(8.dp)
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
                            Text(
                                text = "ანალოგიური გამოცდილება: „${topMatch.decodedThought}“",
                                color = Color(0xFFFFF9C4),
                                fontSize = 10.5.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "კატეგორია: ${topMatch.contextCategory}",
                                    color = NeuralTextSecondary,
                                    fontSize = 9.sp
                                )
                                Text(
                                    text = "პულსი: ${topMatch.hrBpm.toInt()} BPM • Flow: ${(topMatch.flowIndex * 100).toInt()}%",
                                    color = NeuralTextSecondary,
                                    fontSize = 9.sp
                                )
                            }
                        }
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "ინდექსირებული ეპიზოდები: ${recall?.totalIndexedEpisodes ?: 0}",
                        color = NeuralTextSecondary,
                        fontSize = 9.5.sp
                    )
                    Text(
                        text = "16-განზომილებიანი Cos-Sim ინფერენცია",
                        color = Color(0xFF00E5FF),
                        fontSize = 9.5.sp
                    )
                }
            }
        }

        // Nearest Neighbors List
        if (recall?.nearestNeighbors?.isNotEmpty() == true) {
            Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
                Text(
                    text = "უახლოესი ასოციაციური მეზობლები:",
                    color = NeuralTextSecondary,
                    fontSize = 10.5.sp,
                    fontWeight = FontWeight.SemiBold
                )
                recall.nearestNeighbors.forEach { (thoughtExcerpt, simScore) ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(NeuralSurface.copy(alpha = 0.6f))
                            .border(1.dp, Color.White.copy(alpha = 0.08f), RoundedCornerShape(8.dp))
                            .padding(horizontal = 8.dp, vertical = 6.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = thoughtExcerpt,
                                color = Color(0xFFF1F5F9),
                                fontSize = 10.sp
                            )
                            Text(
                                text = "$simScore% მსგავსება",
                                color = Color(0xFFFFD600),
                                fontSize = 9.5.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 * 🥊 Local Adversarial Self-Play Arena Composable (ორმხრივი შეჯიბრი და თვით-სწავლება)
 * Visualizes the on-device sparring arena between Proposer (Generator) and Skeptic (Adversary Critic).
 */
@Composable
fun CognitiveAdversarialSelfPlayTab(result: HybridCognitiveEngine.CognitiveResult?) {
    val selfPlay = result?.adversarialSelfPlayTelemetry

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // Tournament Header Banner
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(
                    Brush.horizontalGradient(
                        listOf(Color(0xFF31103F), Color(0xFF1E1B4B), Color(0xFF140D26))
                    )
                )
                .border(1.dp, Color(0xFFD946EF).copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                .padding(12.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(text = "🥊", fontSize = 16.sp)
                        Text(
                            text = "ორმხრივი შეჯიბრი და თვით-სწავლება",
                            color = Color(0xFFF5D0FE),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(Color(0xFFD946EF).copy(alpha = 0.25f))
                            .border(1.dp, Color(0xFFD946EF).copy(alpha = 0.5f), RoundedCornerShape(6.dp))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "რაუნდი #${selfPlay?.tournamentRound ?: 25}",
                            color = Color(0xFFF0ABFC),
                            fontSize = 9.5.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                Text(
                    text = "100% On-Device Reinforcement Learning through Self-Play (RLSP): პროპოზერი (Generator) და სკეპტიკოსი (Adversary Critic) ეჯიბრებიან ერთმანეთს ჰიპოთეზების გამოსაწრთობად და ჰალუცინაციების აღმოსაფხვრელად.",
                    color = NeuralTextSecondary,
                    fontSize = 10.sp,
                    lineHeight = 14.sp
                )
            }
        }

        // Elo Rating & Battle Cards
        val genElo = selfPlay?.generatorElo ?: 1528
        val advElo = selfPlay?.adversaryElo ?: 1506
        val totalElo = (genElo + advElo).toFloat()
        val genRatio = (genElo / totalElo).coerceIn(0.2f, 0.8f)

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(NeuralSurface.copy(alpha = 0.85f))
                .border(1.dp, Color.White.copy(alpha = 0.08f), RoundedCornerShape(12.dp))
                .padding(12.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Generator Agent
                    Column(horizontalAlignment = Alignment.Start) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text(text = "⚡", fontSize = 12.sp)
                            Text(text = "პროპოზერი (Gen)", color = Color(0xFF38BDF8), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                        Text(text = "Elo: $genElo", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.ExtraBold)
                    }

                    // VS Badge & Last Winner
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Box(
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(Color.White.copy(alpha = 0.1f))
                                .padding(horizontal = 8.dp, vertical = 2.dp)
                        ) {
                            Text(text = "VS", color = Color(0xFFFFD600), fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        }
                        Text(
                            text = "გამარჯვებული: ${selfPlay?.currentWinnerNameKa ?: "⚡ პროპოზერი"} (Δ${selfPlay?.eloDelta ?: 8})",
                            color = Color(0xFFA7F3D0),
                            fontSize = 8.5.sp
                        )
                    }

                    // Adversary Agent
                    Column(horizontalAlignment = Alignment.End) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text(text = "სკეპტიკოსი (Adv)", color = Color(0xFFFB7185), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            Text(text = "🛡️", fontSize = 12.sp)
                        }
                        Text(text = "Elo: $advElo", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.ExtraBold)
                    }
                }

                // Balance of Power Bar
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(text = "გენერატორის უპირატესობა: ${(genRatio * 100).toInt()}%", color = Color(0xFF38BDF8), fontSize = 9.sp)
                        Text(text = "სკეპტიკოსის წნეხი: ${((1f - genRatio) * 100).toInt()}%", color = Color(0xFFFB7185), fontSize = 9.sp)
                    }
                    LinearProgressIndicator(
                        progress = { genRatio },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(RoundedCornerShape(3.dp)),
                        color = Color(0xFF38BDF8),
                        trackColor = Color(0xFFFB7185)
                    )
                }

                // Key Metrics Triple Grid
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    // Robustness Metric
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFF10B981).copy(alpha = 0.15f))
                            .border(1.dp, Color(0xFF10B981).copy(alpha = 0.3f), RoundedCornerShape(8.dp))
                            .padding(8.dp)
                    ) {
                        Column {
                            Text(text = "სიმტკიცე", color = Color(0xFFA7F3D0), fontSize = 9.sp)
                            Text(
                                text = "${selfPlay?.stressTestedRobustnessPct ?: 88}%",
                                color = Color(0xFF34D399),
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    // Tension Metric
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFFF59E0B).copy(alpha = 0.15f))
                            .border(1.dp, Color(0xFFF59E0B).copy(alpha = 0.3f), RoundedCornerShape(8.dp))
                            .padding(8.dp)
                    ) {
                        Column {
                            Text(text = "დაძაბულობა", color = Color(0xFFFDE68A), fontSize = 9.sp)
                            Text(
                                text = "${selfPlay?.adversarialTensionPct ?: 42}%",
                                color = Color(0xFFFBBF24),
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    // Prevented Hallucinations
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFF6366F1).copy(alpha = 0.15f))
                            .border(1.dp, Color(0xFF6366F1).copy(alpha = 0.3f), RoundedCornerShape(8.dp))
                            .padding(8.dp)
                    ) {
                        Column {
                            Text(text = "შეცდომა აცილდა", color = Color(0xFFC7D2FE), fontSize = 9.sp)
                            Text(
                                text = "${selfPlay?.preventedHallucinationsCount ?: 38}",
                                color = Color(0xFF818CF8),
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }

        // Active Duel Arena: Thesis vs Antithesis -> Hardened Synthesis
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text(
                text = "აქტიური დიალექტიკური დუელი (თეზისი vs ანტითეზისი):",
                color = NeuralTextSecondary,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold
            )

            // 1. Thesis (Proposer)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(0xFF0284C7).copy(alpha = 0.12f))
                    .border(1.dp, Color(0xFF0284C7).copy(alpha = 0.4f), RoundedCornerShape(10.dp))
                    .padding(10.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text(text = "⚡", fontSize = 11.sp)
                        Text(text = "თეზისი (პროპოზერის ჰიპოთეზა):", color = Color(0xFF38BDF8), fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }
                    Text(
                        text = selfPlay?.thesisProposition ?: result?.synthesizedThoughtSentence.orEmpty(),
                        color = Color.White,
                        fontSize = 11.sp,
                        lineHeight = 15.sp
                    )
                }
            }

            // 2. Antithesis (Skeptic Adversary)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(0xFFBE123C).copy(alpha = 0.12f))
                    .border(1.dp, Color(0xFFBE123C).copy(alpha = 0.4f), RoundedCornerShape(10.dp))
                    .padding(10.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Text(text = "🛡️", fontSize = 11.sp)
                            Text(text = "ანტითეზისი (სკეპტიკოსის კრიტიკა & სტრეს-ტესტი):", color = Color(0xFFFB7185), fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        }
                        Text(
                            text = selfPlay?.activeDebateCategory ?: "სომატური შემოწმება",
                            color = Color(0xFFFFD600),
                            fontSize = 8.5.sp
                        )
                    }
                    Text(
                        text = selfPlay?.antithesisChallenge ?: "სკეპტიკოსი ამოწმებს ბიომეტრიულ და ნეირონულ შესაბამისობას...",
                        color = Color(0xFFFECDD3),
                        fontSize = 11.sp,
                        lineHeight = 15.sp
                    )
                }
            }

            // 3. Hardened Synthesis
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(
                        Brush.horizontalGradient(
                            listOf(Color(0xFF064E3B).copy(alpha = 0.8f), Color(0xFF042F2E).copy(alpha = 0.8f))
                        )
                    )
                    .border(1.5.dp, Color(0xFF10B981).copy(alpha = 0.6f), RoundedCornerShape(10.dp))
                    .padding(10.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text(text = "💎", fontSize = 11.sp)
                        Text(text = "გამყარებული სინთეზი (Hegelian Hardened Output):", color = Color(0xFF34D399), fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }
                    Text(
                        text = selfPlay?.dialecticalSynthesis ?: result?.synthesizedThoughtSentence.orEmpty(),
                        color = Color(0xFFF0FDF4),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                        lineHeight = 15.sp
                    )
                }
            }
        }

        // Sparring History List
        if (selfPlay?.sparringHistory?.isNotEmpty() == true) {
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(
                    text = "თვით-სწავლების ისტორია & ტურნირის რაუნდები:",
                    color = NeuralTextSecondary,
                    fontSize = 10.5.sp,
                    fontWeight = FontWeight.SemiBold
                )

                selfPlay.sparringHistory.take(4).forEach { exchange ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(NeuralSurface.copy(alpha = 0.6f))
                            .border(1.dp, Color.White.copy(alpha = 0.08f), RoundedCornerShape(8.dp))
                            .padding(horizontal = 10.dp, vertical = 8.dp)
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "რაუნდი #${exchange.roundNumber} • ${exchange.challengeCategoryKa}",
                                    color = Color(0xFFE2E8F0),
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                val statusText = if (exchange.defenseSuccess) "დაცულია ✓" else "კორექტირებულია 🛡️"
                                val statusColor = if (exchange.defenseSuccess) Color(0xFF34D399) else Color(0xFFFB7185)
                                Text(text = statusText, color = statusColor, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                            }
                            Text(
                                text = exchange.hardenedSynthesis,
                                color = NeuralTextSecondary,
                                fontSize = 9.5.sp
                            )
                        }
                    }
                }
            }
        }
    }
}


