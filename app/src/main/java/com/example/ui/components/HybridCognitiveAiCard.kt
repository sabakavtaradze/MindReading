package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.foundation.layout.width
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
import com.example.service.SpikingNeuralNetworkEngine
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
            var cognitiveSubTab by remember { mutableStateOf(0) } // 0: Insights, 1: Logic Chain, 2: Algorithm Tree, 3: SNN Network, 4: HTM Cortical Columns, 5: Hopfield Memory, 6: Concept Hierarchy

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.Black.copy(alpha = 0.4f))
                    .horizontalScroll(rememberScrollState())
                    .padding(4.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                val tabs = listOf("💡 ინსაითი", "🔗 ლოგიკა", "⚡ ალგორითმი", "🧠 SNN", "🧬 HTM სვეტები", "🔮 Hopfield", "📊 კონცეფცია")
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
                0 -> {
                    // Deep Synthesis Speech Bubble / Reasoning Result
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

                    // Cognitive Insights Checklist
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

                1 -> {
                    // 🔗 Logical Deduction Chain View
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
                                text = "🔗 ლოგიკური მსჯელობის სტრუქტურა (Logical Chain):",
                                color = NeuralTextPrimary,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        if (logicChain.isEmpty()) {
                            Text(
                                text = "ლოგიკური ჯაჭვი ფორმირდება ცოცხალი სენსორებიდან...",
                                color = NeuralTextSecondary,
                                fontSize = 12.sp
                            )
                        } else {
                            logicChain.forEachIndexed { idx, stepText ->
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(NeuralSurface.copy(alpha = 0.7f))
                                        .border(1.dp, Color(0xFF00E5FF).copy(alpha = 0.2f), RoundedCornerShape(10.dp))
                                        .padding(10.dp)
                                ) {
                                    Row(
                                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                                        verticalAlignment = Alignment.Top
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(22.dp)
                                                .clip(CircleShape)
                                                .background(Color(0xFF00E5FF).copy(alpha = 0.2f))
                                                .border(1.dp, Color(0xFF00E5FF), CircleShape),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = "${idx + 1}",
                                                color = Color(0xFF00E5FF),
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                        Text(
                                            text = stepText,
                                            color = Color.White,
                                            fontSize = 12.sp,
                                            lineHeight = 17.sp,
                                            modifier = Modifier.weight(1f)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                2 -> {
                    // ⚡ Algorithmic Action Tree View
                    val algoSteps = result?.algorithmicSteps ?: emptyList()
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = AppIcons.Speed,
                                contentDescription = null,
                                tint = Color(0xFF00E676),
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = "⚡ ალგორითმული საფეხურები & ოპერაციები:",
                                color = NeuralTextPrimary,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        if (algoSteps.isEmpty()) {
                            Text(
                                text = "ალგორითმული სქემა მზადდება...",
                                color = NeuralTextSecondary,
                                fontSize = 12.sp
                            )
                        } else {
                            algoSteps.forEach { step ->
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(Color(0xFF050E18))
                                        .border(1.dp, Color(0xFF00E676).copy(alpha = 0.3f), RoundedCornerShape(12.dp))
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
                                                Text(
                                                    text = "STEP ${step.step}:",
                                                    color = Color(0xFF00E676),
                                                    fontSize = 11.sp,
                                                    fontWeight = FontWeight.Bold
                                                )
                                                Text(
                                                    text = step.stageName,
                                                    color = Color.White,
                                                    fontSize = 12.sp,
                                                    fontWeight = FontWeight.Bold
                                                )
                                            }
                                            Box(
                                                modifier = Modifier
                                                    .clip(RoundedCornerShape(6.dp))
                                                    .background(Color(0xFF00E676).copy(alpha = 0.15f))
                                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                                            ) {
                                                Text(
                                                    text = step.status,
                                                    color = Color(0xFF00E676),
                                                    fontSize = 9.sp,
                                                    fontWeight = FontWeight.Bold
                                                )
                                            }
                                        }

                                        if (step.description.isNotBlank()) {
                                            Text(
                                                text = step.description,
                                                color = NeuralTextSecondary,
                                                fontSize = 11.sp
                                            )
                                        }

                                        if (step.conditionOrAction.isNotBlank()) {
                                            Box(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .clip(RoundedCornerShape(6.dp))
                                                    .background(Color.Black.copy(alpha = 0.5f))
                                                    .padding(horizontal = 8.dp, vertical = 5.dp)
                                            ) {
                                                Text(
                                                    text = "▸ ${step.conditionOrAction}",
                                                    color = Color(0xFF80D8FF),
                                                    fontSize = 11.sp,
                                                    fontWeight = FontWeight.Medium
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                3 -> {
                    // 🧠 Spiking Neural Network (SNN) & Bidirectional Neuromodulation Matrix
                    val snn = result?.snnTelemetry
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        // Title Header
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
                                    tint = Color(0xFF00E5FF),
                                    modifier = Modifier.size(16.dp)
                                )
                                Text(
                                    text = "🧠 SNN & STDP ნეირონული ბადე (80 LIF ნეირონი)",
                                    color = NeuralTextPrimary,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            // Live Firing Badge
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(Color(0xFF00E5FF).copy(alpha = 0.15f))
                                    .border(1.dp, Color(0xFF00E5FF).copy(alpha = 0.5f), RoundedCornerShape(6.dp))
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = "${snn?.totalSpikesPerSec?.roundToInt() ?: 24} Hz Spikes",
                                    color = Color(0xFF00E5FF),
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        // SNN Key Metrics Grid
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            // Membrane Potential
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(NeuralSurface.copy(alpha = 0.7f))
                                    .border(1.dp, Color(0xFFB388FF).copy(alpha = 0.3f), RoundedCornerShape(10.dp))
                                    .padding(8.dp)
                            ) {
                                Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                                    Text(
                                        text = "მემბრანის V_m",
                                        color = NeuralTextSecondary,
                                        fontSize = 9.sp
                                    )
                                    Text(
                                        text = "${String.format(java.util.Locale.US, "%.1f", snn?.averageMembranePotential ?: -64.5f)} mV",
                                        color = Color(0xFFB388FF),
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }

                            // Synaptic Weight
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(NeuralSurface.copy(alpha = 0.7f))
                                    .border(1.dp, Color(0xFF00E676).copy(alpha = 0.3f), RoundedCornerShape(10.dp))
                                    .padding(8.dp)
                            ) {
                                Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                                    Text(
                                        text = "სინაფსური წონა",
                                        color = NeuralTextSecondary,
                                        fontSize = 9.sp
                                    )
                                    Text(
                                        text = "${String.format(java.util.Locale.US, "%.2f", snn?.meanSynapticWeight ?: 0.52f)} W",
                                        color = Color(0xFF00E676),
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }

                            // STDP Plasticity Delta
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(NeuralSurface.copy(alpha = 0.7f))
                                    .border(1.dp, Color(0xFFFF9100).copy(alpha = 0.3f), RoundedCornerShape(10.dp))
                                    .padding(8.dp)
                            ) {
                                Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                                    Text(
                                        text = "STDP ΔW",
                                        color = NeuralTextSecondary,
                                        fontSize = 9.sp
                                    )
                                    Text(
                                        text = "${if ((snn?.stdpPlasticityRateDelta ?: 0f) >= 0) "+" else ""}${String.format(java.util.Locale.US, "%.3f", snn?.stdpPlasticityRateDelta ?: 0.012f)}",
                                        color = Color(0xFFFF9100),
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }

                        // Cortical Clusters Live Spectrum (5 Brain Clusters)
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color.Black.copy(alpha = 0.35f))
                                .border(1.dp, Color.White.copy(alpha = 0.08f), RoundedCornerShape(12.dp))
                                .padding(10.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = "🌐 5 კორტიკალური კლასტერის ცოცხალი აქტივობა:",
                                color = NeuralTextSecondary,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.SemiBold
                            )

                            SpikingNeuralNetworkEngine.CorticalCluster.values().forEach { cluster ->
                                val freqHz = snn?.clusterSpikeFrequencies?.get(cluster) ?: 8.0f
                                val isDominant = snn?.dominantActiveCluster == cluster
                                val clusterColor = when (cluster) {
                                    SpikingNeuralNetworkEngine.CorticalCluster.FRONTAL_EXECUTIVE -> Color(0xFF00E5FF)
                                    SpikingNeuralNetworkEngine.CorticalCluster.TEMPORAL_ACOUSTIC -> Color(0xFFFFD600)
                                    SpikingNeuralNetworkEngine.CorticalCluster.PARIETAL_GAZE -> Color(0xFF00E676)
                                    SpikingNeuralNetworkEngine.CorticalCluster.LIMBIC_POLYVAGAL -> Color(0xFFB388FF)
                                    SpikingNeuralNetworkEngine.CorticalCluster.SOMATOSENSORY_MOTOR -> Color(0xFFFF4081)
                                }

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
                                                    .background(if (isDominant) clusterColor else clusterColor.copy(alpha = 0.4f))
                                            )
                                            Text(
                                                text = cluster.labelKa,
                                                color = if (isDominant) Color.White else NeuralTextSecondary,
                                                fontSize = 10.sp,
                                                fontWeight = if (isDominant) FontWeight.Bold else FontWeight.Normal
                                            )
                                            if (isDominant) {
                                                Text(
                                                    text = "• დომინანტური",
                                                    color = clusterColor,
                                                    fontSize = 8.sp,
                                                    fontWeight = FontWeight.Bold
                                                )
                                            }
                                        }

                                        Text(
                                            text = "${String.format(java.util.Locale.US, "%.1f", freqHz)} Hz",
                                            color = clusterColor,
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }

                                    LinearProgressIndicator(
                                        progress = { (freqHz / 40.0f).coerceIn(0.05f, 1.0f) },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(3.dp)
                                            .clip(CircleShape),
                                        color = clusterColor,
                                        trackColor = Color.White.copy(alpha = 0.05f)
                                    )
                                }
                            }
                        }

                        // Bidirectional Closed-Loop Neuromodulation Box (Gemini AI ↔ On-Device SNN)
                        val neuro = snn?.neuromodulation
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(
                                    Brush.verticalGradient(
                                        listOf(
                                            NeuralDeepPurple.copy(alpha = 0.85f),
                                            NeuralSurface
                                        )
                                    )
                                )
                                .border(
                                    1.dp,
                                    Brush.horizontalGradient(
                                        listOf(
                                            Color(0xFFFFD600).copy(alpha = 0.5f),
                                            Color(0xFF00E5FF).copy(alpha = 0.5f)
                                        )
                                    ),
                                    RoundedCornerShape(12.dp)
                                )
                                .padding(10.dp)
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
                                            imageVector = AppIcons.RefreshIcon,
                                            contentDescription = null,
                                            tint = Color(0xFFFFD600),
                                            modifier = Modifier.size(14.dp)
                                        )
                                        Text(
                                            text = "🔄 ორმხრივი ნეირომოდულაცია (Gemini ↔ SNN)",
                                            color = Color.White,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                    Text(
                                        text = "ციკლი #${neuro?.closedLoopCycles ?: 1}",
                                        color = Color(0xFFFFD600),
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }

                                // 3 Neuromodulators Row
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    // Dopamine
                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(Color.Black.copy(alpha = 0.3f))
                                            .padding(6.dp)
                                    ) {
                                        Column {
                                            Text("დოფამინი (LTP)", color = NeuralTextSecondary, fontSize = 8.sp)
                                            Text(
                                                "${String.format(java.util.Locale.US, "%.2f", neuro?.dopamineLevel ?: 1.0f)}x",
                                                color = Color(0xFFFFD600),
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                    }
                                    // Serotonin
                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(Color.Black.copy(alpha = 0.3f))
                                            .padding(6.dp)
                                    ) {
                                        Column {
                                            Text("სეროტონინი", color = NeuralTextSecondary, fontSize = 8.sp)
                                            Text(
                                                "${String.format(java.util.Locale.US, "%.2f", neuro?.serotoninLevel ?: 1.0f)}x",
                                                color = Color(0xFF00E5FF),
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                    }
                                    // Noradrenaline
                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(Color.Black.copy(alpha = 0.3f))
                                            .padding(6.dp)
                                    ) {
                                        Column {
                                            Text("ნორადრენალინი", color = NeuralTextSecondary, fontSize = 8.sp)
                                            Text(
                                                "${String.format(java.util.Locale.US, "%.2f", neuro?.noradrenalineLevel ?: 1.0f)}x",
                                                color = Color(0xFFFF9100),
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                    }
                                }

                                // Status / Reason from AI
                                Text(
                                    text = "AI დაკალიბრება: ${neuro?.aiFeedbackStatus ?: "ორმხრივი კომუნიკაციის ჰომეოსტაზი აქტიურია"}",
                                    color = NeuralTextSecondary,
                                    fontSize = 10.sp,
                                    lineHeight = 14.sp
                                )
                            }
                        }
                    }
                }

                4 -> {
                    // 🧬 Hierarchical Temporal Memory (HTM Cortical Columns & SDR) View
                    val htm = result?.ecosystemTelemetry?.htmTelemetry
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
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
                                    tint = Color(0xFF00E676),
                                    modifier = Modifier.size(16.dp)
                                )
                                Text(
                                    text = "🧬 HTM კორტიკალური სვეტები (40 მინიკოლონა • 160 უჯრედი)",
                                    color = NeuralTextPrimary,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(Color(0xFF00E676).copy(alpha = 0.15f))
                                    .border(1.dp, Color(0xFF00E676).copy(alpha = 0.5f), RoundedCornerShape(6.dp))
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = "${htm?.activeColumnsCount ?: 7}/40 SDR Sparsity",
                                    color = Color(0xFF00E676),
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        // HTM 3-Card Metrics Row
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            // Active Columns & Sparsity
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(NeuralSurface.copy(alpha = 0.7f))
                                    .border(1.dp, Color(0xFF00E676).copy(alpha = 0.3f), RoundedCornerShape(10.dp))
                                    .padding(8.dp)
                            ) {
                                Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                                    Text("SDR Sparsity", color = NeuralTextSecondary, fontSize = 9.sp)
                                    Text(
                                        "${String.format(java.util.Locale.US, "%.1f", htm?.sdrSparsityPercentage ?: 15.0f)}%",
                                        color = Color(0xFF00E676),
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }

                            // Predictive Cells
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(NeuralSurface.copy(alpha = 0.7f))
                                    .border(1.dp, Color(0xFF00E5FF).copy(alpha = 0.3f), RoundedCornerShape(10.dp))
                                    .padding(8.dp)
                            ) {
                                Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                                    Text("პროგნოზირება", color = NeuralTextSecondary, fontSize = 9.sp)
                                    Text(
                                        "${htm?.predictiveCellsCount ?: 12} უჯრედი",
                                        color = Color(0xFF00E5FF),
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }

                            // Anomaly Score
                            val anomaly = htm?.anomalyScore ?: 0.15f
                            val anomalyColor = if (anomaly > 0.6f) Color(0xFFFF5252) else if (anomaly > 0.3f) Color(0xFFFF9100) else Color(0xFF00E676)
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(NeuralSurface.copy(alpha = 0.7f))
                                    .border(1.dp, anomalyColor.copy(alpha = 0.3f), RoundedCornerShape(10.dp))
                                    .padding(8.dp)
                            ) {
                                Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                                    Text("ანომალია / სიახლე", color = NeuralTextSecondary, fontSize = 9.sp)
                                    Text(
                                        "${(anomaly * 100).roundToInt()}%",
                                        color = anomalyColor,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }

                        // HTM Cortical Grid Visualization (40 columns, highlighted if active)
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color.Black.copy(alpha = 0.35f))
                                .border(1.dp, Color.White.copy(alpha = 0.08f), RoundedCornerShape(12.dp))
                                .padding(10.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "კორტიკალური მინიკოლონების სივრცითი ბადე (SDR):",
                                    color = NeuralTextSecondary,
                                    fontSize = 10.sp
                                )
                                Text(
                                    text = "თანმიმდევრული კოჰერენტულობა: ${((htm?.sequenceCoherence ?: 0.85f) * 100).roundToInt()}%",
                                    color = Color(0xFF00E676),
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            // 4 rows of 10 columns
                            val activeIndices = htm?.activeColumnIndices?.toSet() ?: setOf(2, 5, 12, 19, 28, 33)
                            for (row in 0 until 4) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    for (col in 0 until 10) {
                                        val idx = row * 10 + col
                                        val isActive = activeIndices.contains(idx)
                                        Box(
                                            modifier = Modifier
                                                .weight(1f)
                                                .height(14.dp)
                                                .clip(RoundedCornerShape(3.dp))
                                                .background(
                                                    if (isActive) Color(0xFF00E676)
                                                    else Color(0xFF00E676).copy(alpha = 0.08f)
                                                )
                                                .border(
                                                    0.5.dp,
                                                    if (isActive) Color.White else Color.White.copy(alpha = 0.05f),
                                                    RoundedCornerShape(3.dp)
                                                )
                                        )
                                    }
                                }
                            }

                            Text(
                                text = "დომინანტური კორტიკალური პატერნი: „${htm?.dominantCorticalPattern ?: "სენსორულ-მოტორული სტაბილური ნაკადი"}“",
                                color = Color.White,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }

                5 -> {
                    // 🔮 Modern Continuous Hopfield Memory Network View
                    val hopfield = result?.ecosystemTelemetry?.hopfieldTelemetry
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
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
                                    tint = Color(0xFFFFD600),
                                    modifier = Modifier.size(16.dp)
                                )
                                Text(
                                    text = "🔮 Hopfield ასოციაციური მეხსიერების ბადე",
                                    color = NeuralTextPrimary,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(Color(0xFFFFD600).copy(alpha = 0.15f))
                                    .border(1.dp, Color(0xFFFFD600).copy(alpha = 0.5f), RoundedCornerShape(6.dp))
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = "${((hopfield?.convergenceScore ?: 0.95f) * 100).roundToInt()}% კონვერგენცია",
                                    color = Color(0xFFFFD600),
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        // Hopfield Metrics Row
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            // Lyapunov Energy
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(NeuralSurface.copy(alpha = 0.7f))
                                    .border(1.dp, Color(0xFFFFD600).copy(alpha = 0.3f), RoundedCornerShape(10.dp))
                                    .padding(8.dp)
                            ) {
                                Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                                    Text("ლიაპუნოვის ენერგია E", color = NeuralTextSecondary, fontSize = 9.sp)
                                    Text(
                                        "${String.format(java.util.Locale.US, "%.2f", hopfield?.energy ?: -14.2f)}",
                                        color = Color(0xFFFFD600),
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }

                            // Energy Delta (Minimization)
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
                                        "${String.format(java.util.Locale.US, "%.3f", hopfield?.energyDelta ?: -0.012f)}",
                                        color = Color(0xFF00E5FF),
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }

                            // Similarity Score
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

                        // Hopfield Memory Attractor Card
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

                6 -> {
                    // 📊 Concept Hierarchy View
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
            }
        }
    }
}
