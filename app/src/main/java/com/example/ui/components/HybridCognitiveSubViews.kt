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
import com.example.service.HybridCognitiveEngine
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
fun CognitiveInsightsTab(result: HybridCognitiveEngine.CognitiveResult?) {
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
