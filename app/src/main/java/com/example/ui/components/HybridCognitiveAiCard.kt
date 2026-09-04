package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.service.HybridCognitiveEngine
import com.example.service.PolyvagalBehavioralEngine
import com.example.ui.theme.AppIcons
import com.example.ui.theme.NeuralAccent
import com.example.ui.theme.NeuralBackground
import com.example.ui.theme.NeuralDeepPurple
import com.example.ui.theme.NeuralSurface
import com.example.ui.theme.NeuralTextPrimary
import com.example.ui.theme.NeuralTextSecondary
import kotlin.math.roundToInt

@Composable
fun HybridCognitiveAiCard(
    result: HybridCognitiveEngine.CognitiveResult?,
    modifier: Modifier = Modifier
) {
    val isCloud = result?.isCloudActive == true
    val modeColor = if (isCloud) Color(0xFF00E5FF) else Color(0xFF00E676)
    val statusLabel = result?.modeLabel ?: "ავტონომიური ნეირო-სააზროვნო & ქცევითი ბირთვი (On-Device)"
    val polyvagal = result?.polyvagalResult

    val polyvagalColor = when (polyvagal?.dominantState) {
        PolyvagalBehavioralEngine.PolyvagalState.VENTRAL_VAGAL -> Color(0xFF00E676)
        PolyvagalBehavioralEngine.PolyvagalState.SYMPATHETIC -> Color(0xFFFF9100)
        PolyvagalBehavioralEngine.PolyvagalState.DORSAL_VAGAL -> Color(0xFFB388FF)
        null -> Color(0xFF00E676)
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(22.dp))
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        NeuralDeepPurple.copy(alpha = 0.94f),
                        NeuralSurface
                    )
                )
            )
            .border(
                1.5.dp,
                Brush.horizontalGradient(
                    listOf(
                        modeColor.copy(alpha = 0.6f),
                        NeuralAccent.copy(alpha = 0.35f)
                    )
                ),
                RoundedCornerShape(22.dp)
            )
            .padding(18.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
            // Header Row: Title + Online/Offline Badge
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
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(modeColor.copy(alpha = 0.15f))
                            .border(1.dp, modeColor.copy(alpha = 0.4f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (isCloud) AppIcons.AutoAwesome else AppIcons.Speed,
                            contentDescription = null,
                            tint = modeColor,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Column {
                        Text(
                            text = "კოგნიტური & ქცევითი AI",
                            color = NeuralTextPrimary,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = if (isCloud) "Cloud Reasoning Active" else "100% On-Device Offline Engine",
                            color = modeColor,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                // Mode Pill Badge
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(modeColor.copy(alpha = 0.12f))
                        .border(1.dp, modeColor.copy(alpha = 0.35f), RoundedCornerShape(12.dp))
                        .padding(horizontal = 10.dp, vertical = 5.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(5.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(7.dp)
                                .clip(CircleShape)
                                .background(modeColor)
                        )
                        Text(
                            text = if (isCloud) "ONLINE AI" else "OFFLINE CORE",
                            color = modeColor,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // Mode Label Bar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color.Black.copy(alpha = 0.35f))
                    .padding(horizontal = 10.dp, vertical = 6.dp)
            ) {
                Text(
                    text = statusLabel,
                    color = NeuralTextSecondary,
                    fontSize = 11.sp
                )
            }

            // ==========================================
            // 🧠 POLYVAGAL BEHAVIORAL ENTITY SECTION
            // ==========================================
            if (polyvagal != null) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(NeuralBackground.copy(alpha = 0.7f))
                        .border(1.dp, polyvagalColor.copy(alpha = 0.3f), RoundedCornerShape(14.dp))
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
                                    tint = polyvagalColor,
                                    modifier = Modifier.size(16.dp)
                                )
                                Text(
                                    text = "ქცევითი ნეირო-სტატუსი (Polyvagal):",
                                    color = NeuralTextPrimary,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            // Dominant Pill
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(polyvagalColor.copy(alpha = 0.15f))
                                    .border(1.dp, polyvagalColor.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                                    .padding(horizontal = 8.dp, vertical = 3.dp)
                            ) {
                                Text(
                                    text = polyvagal.dominantState.labelKa.take(16) + "...",
                                    color = polyvagalColor,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        // Tri-Metric Polyvagal Gauge Bars
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Flow ზონა: ${(polyvagal.flowStateIndex * 100).roundToInt()}%",
                                    color = Color(0xFF00E676),
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Spacer(modifier = Modifier.height(3.dp))
                                LinearProgressIndicator(
                                    progress = { polyvagal.flowStateIndex },
                                    modifier = Modifier.fillMaxWidth().height(4.dp).clip(CircleShape),
                                    color = Color(0xFF00E676),
                                    trackColor = Color.White.copy(alpha = 0.1f)
                                )
                            }
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "სტრესი: ${(polyvagal.sympatheticScore * 100).roundToInt()}%",
                                    color = Color(0xFFFF9100),
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Spacer(modifier = Modifier.height(3.dp))
                                LinearProgressIndicator(
                                    progress = { polyvagal.sympatheticScore },
                                    modifier = Modifier.fillMaxWidth().height(4.dp).clip(CircleShape),
                                    color = Color(0xFFFF9100),
                                    trackColor = Color.White.copy(alpha = 0.1f)
                                )
                            }
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "დაღლა: ${(polyvagal.dorsalScore * 100).roundToInt()}%",
                                    color = Color(0xFFB388FF),
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Spacer(modifier = Modifier.height(3.dp))
                                LinearProgressIndicator(
                                    progress = { polyvagal.dorsalScore },
                                    modifier = Modifier.fillMaxWidth().height(4.dp).clip(CircleShape),
                                    color = Color(0xFFB388FF),
                                    trackColor = Color.White.copy(alpha = 0.1f)
                                )
                            }
                        }

                        // Dissonance & Attention Horizon
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "ყურადღების დრიფტი: ~${polyvagal.attentionDriftSeconds} წამი",
                                color = NeuralTextSecondary,
                                fontSize = 11.sp
                            )
                            Text(
                                text = "სომატური დისონანსი: ${(polyvagal.somaticDissonanceIndex * 100).roundToInt()}%",
                                color = NeuralTextSecondary,
                                fontSize = 11.sp
                            )
                        }
                    }
                }
            }

            // ==========================================
            // 📚 AUTONOMOUS DYNAMIC LEXICON DISCOVERY
            // ==========================================
            result?.let { res ->
                val words = res.recentlyDiscoveredWords
                if (words.isNotEmpty()) {
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(5.dp)
                            ) {
                                Icon(
                                    imageVector = AppIcons.Hub,
                                    contentDescription = null,
                                    tint = Color(0xFF00E5FF),
                                    modifier = Modifier.size(14.dp)
                                )
                                Text(
                                    text = "გარედან მოძიებული ახალი ცნებები & სიტყვები:",
                                    color = Color(0xFF00E5FF),
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Text(
                                text = "ბაზა: ${res.totalVocabularySize} ცნება",
                                color = NeuralTextSecondary,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        // Horizontal Scrolling Word Chips
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .horizontalScroll(rememberScrollState()),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            words.forEach { word ->
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(Color(0xFF00E5FF).copy(alpha = 0.12f))
                                        .border(1.dp, Color(0xFF00E5FF).copy(alpha = 0.35f), RoundedCornerShape(8.dp))
                                        .padding(horizontal = 8.dp, vertical = 4.dp)
                                ) {
                                    Text(
                                        text = "+ $word",
                                        color = Color(0xFFE0F7FA),
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // 🌟 Prominent Live Synthesized Thought Banner
            val currentThought = result?.synthesizedThoughtSentence.orEmpty()
            if (currentThought.isNotBlank()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(
                            Brush.horizontalGradient(
                                listOf(
                                    modeColor.copy(alpha = 0.18f),
                                    NeuralDeepPurple.copy(alpha = 0.4f)
                                )
                            )
                        )
                        .border(1.2.dp, modeColor.copy(alpha = 0.5f), RoundedCornerShape(16.dp))
                        .padding(14.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = if (isCloud) AppIcons.AutoAwesome else AppIcons.Speed,
                                contentDescription = null,
                                tint = modeColor,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = if (isCloud) "🌐 ამოცნობილი აზრი (Gemini AI):" else "⚡ ამოცნობილი აზრი (On-Device):",
                                color = modeColor,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Text(
                            text = "„$currentThought“",
                            color = Color.White,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            lineHeight = 20.sp
                        )
                    }
                }
            }

            // ==========================================
            // 🧠 COGNITIVE LOGIC, ALGORITHMS, MULTI-NEURAL ECOSYSTEM & CONCEPTS
            // ==========================================
            var cognitiveSubTab by remember { mutableStateOf(0) } // 0: Insights, 1: Arbitrator & Workspace, 2: Episodic Memory, 3: Logic, 4: Algo, 5: SNN, 6: HTM, 7: Hopfield, 8: Concepts, 9: Mesh Bus

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.Black.copy(alpha = 0.4f))
                    .horizontalScroll(rememberScrollState())
                    .padding(4.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                val tabs = listOf("💡 ინსაითი", "⚖️ არბიტრი & Workspace", "🔮 ეპიზოდები", "🔗 ლოგიკა", "⚡ ალგორითმი", "🧠 SNN", "🧬 HTM სვეტები", "🔮 Hopfield", "📊 კონცეფცია", "🌐 ნეირო-ხიდი (Mesh)")
                tabs.forEachIndexed { index, label ->
                    val selected = cognitiveSubTab == index
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (selected) NeuralAccent.copy(alpha = 0.25f) else Color.Transparent)
                            .border(
                                width = if (selected) 1.dp else 0.dp,
                                color = if (selected) NeuralAccent else Color.Transparent,
                                shape = RoundedCornerShape(8.dp)
                            )
                            .clickable { cognitiveSubTab = index }
                            .padding(horizontal = 10.dp, vertical = 6.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = label,
                            color = if (selected) Color.White else NeuralTextSecondary,
                            fontSize = 10.sp,
                            fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                }
            }

            when (cognitiveSubTab) {
                0 -> CognitiveInsightsTab(result)
                1 -> CognitiveArbitratorWorkspaceTab(result)
                2 -> CognitiveEpisodicMemoryTab(result)
                3 -> CognitiveLogicTab(result)
                4 -> CognitiveAlgorithmsTab(result)
                5 -> CognitiveSnnTab(result)
                6 -> CognitiveHtmTab(result)
                7 -> CognitiveHopfieldTab(result)
                8 -> CognitiveConceptsTab(result)
                9 -> CognitiveMeshBusTab(result)
            }
        }
    }
}
