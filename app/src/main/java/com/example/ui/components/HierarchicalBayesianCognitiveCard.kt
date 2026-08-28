package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.service.AssociativeThoughtGraphMetrics
import com.example.service.CognitiveLatencyMetrics
import com.example.service.DecisionFatigueMetrics
import com.example.service.EmfSpatialContextMetrics
import com.example.service.FacsMicroExpressionMetrics
import com.example.service.HierarchicalBayesianState
import com.example.service.PpgHrvMetrics
import com.example.service.PsychomotorHesitationMetrics
import com.example.service.PupillometryMetrics
import com.example.service.RespiratoryPatternMetrics
import com.example.service.SubvocalSpeechMetrics
import com.example.service.ThoughtHypothesis
import com.example.service.UltradianBioRhythmMetrics
import com.example.service.VisualSaliencyMetrics
import com.example.ui.theme.AppIcons
import com.example.ui.theme.NeuralAccent
import com.example.ui.theme.NeuralCardPurple
import com.example.ui.theme.NeuralDeepPurple
import com.example.ui.theme.NeuralSurface
import com.example.ui.theme.NeuralTextPrimary
import com.example.ui.theme.NeuralTextSecondary
import java.util.Locale
import kotlin.math.sin

@Composable
fun HierarchicalBayesianCognitiveCard(
    bayesianState: HierarchicalBayesianState,
    onMeasurePpgPulse: () -> Unit,
    onTriggerPupilAha: () -> Unit,
    onRecomputeBayesian: () -> Unit,
    onApplyHypothesis: (ThoughtHypothesis) -> Unit,
    onTouchTap: (Float, Float) -> Unit,
    onTriggerCognitiveApnea: () -> Unit = {},
    onStepNextSubvocal: () -> Unit = {},
    onCycleSaliencyTarget: () -> Unit = {},
    onStepNextAssociativeConcept: () -> Unit = {},
    onStepNextFacs: () -> Unit = {},
    onStepNextEmfSpatial: () -> Unit = {},
    onStepNextLatency: () -> Unit = {},
    onStepNextFatigue: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var activeEngineSection by remember { mutableStateOf("BAYESIAN") }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(NeuralSurface)
            .border(1.dp, NeuralAccent.copy(alpha = 0.4f), RoundedCornerShape(24.dp))
            .padding(16.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Brush.linearGradient(listOf(NeuralAccent, Color(0xFF00B4D8)))),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = AppIcons.Psychology,
                            contentDescription = "Cognitive Brain",
                            tint = NeuralDeepPurple,
                            modifier = Modifier.size(22.dp)
                        )
                    }

                    Column {
                        Text(
                            text = "აზრის გამოთვლის 13-ძრავიანი სისტემა",
                            color = NeuralTextPrimary,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "FACS • EMF • Latency • Fatigue • Bayesian",
                            color = NeuralAccent,
                            fontSize = 10.sp,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(NeuralDeepPurple)
                        .border(1.dp, NeuralAccent.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "${String.format(Locale.US, "%.1f", bayesianState.overallCertaintyPct)}% სიზუსტე",
                        color = NeuralAccent,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Top Tab Selector for the Cognitive Engines
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                EngineTabChip(
                    label = "🧠 ბაიესური ტვინი",
                    isSelected = activeEngineSection == "BAYESIAN",
                    onClick = { activeEngineSection = "BAYESIAN" }
                )
                EngineTabChip(
                    label = "⚡ FACS მიმიკა",
                    isSelected = activeEngineSection == "FACS",
                    onClick = { activeEngineSection = "FACS" }
                )
                EngineTabChip(
                    label = "🧲 EMF & სივრცე",
                    isSelected = activeEngineSection == "EMF",
                    onClick = { activeEngineSection = "EMF" }
                )
                EngineTabChip(
                    label = "⏳ ლატენტობა & Dwell",
                    isSelected = activeEngineSection == "LATENCY",
                    onClick = { activeEngineSection = "LATENCY" }
                )
                EngineTabChip(
                    label = "🧠 დაღლილობა & ენტროპია",
                    isSelected = activeEngineSection == "FATIGUE",
                    onClick = { activeEngineSection = "FATIGUE" }
                )
                EngineTabChip(
                    label = "🫁 სუნთქვა & აპნოე",
                    isSelected = activeEngineSection == "RESPIRATION",
                    onClick = { activeEngineSection = "RESPIRATION" }
                )
                EngineTabChip(
                    label = "🤫 სუბვოკალური ენა",
                    isSelected = activeEngineSection == "SUBVOCAL",
                    onClick = { activeEngineSection = "SUBVOCAL" }
                )
                EngineTabChip(
                    label = "🎯 Saliency & Heatmap",
                    isSelected = activeEngineSection == "SALIENCY",
                    onClick = { activeEngineSection = "SALIENCY" }
                )
                EngineTabChip(
                    label = "🧬 ასოციაციური გრაფი",
                    isSelected = activeEngineSection == "ASSOCIATIVE",
                    onClick = { activeEngineSection = "ASSOCIATIVE" }
                )
                EngineTabChip(
                    label = "💓 PPG & HRV",
                    isSelected = activeEngineSection == "PPG",
                    onClick = { activeEngineSection = "PPG" }
                )
                EngineTabChip(
                    label = "👁️ გუგა & Aha!",
                    isSelected = activeEngineSection == "PUPILLOMETRY",
                    onClick = { activeEngineSection = "PUPILLOMETRY" }
                )
                EngineTabChip(
                    label = "⏱️ თაჩ-ყოყმანი",
                    isSelected = activeEngineSection == "HESITATION",
                    onClick = { activeEngineSection = "HESITATION" }
                )
                EngineTabChip(
                    label = "🌙 90წთ BRAC რიტმი",
                    isSelected = activeEngineSection == "BRAC",
                    onClick = { activeEngineSection = "BRAC" }
                )
            }

            // Active Engine Section Switcher
            when (activeEngineSection) {
                "BAYESIAN" -> BayesianIntentEngineView(
                    bayesianState = bayesianState,
                    onRecompute = onRecomputeBayesian,
                    onApplyHypothesis = onApplyHypothesis
                )
                "FACS" -> FacsMicroExpressionEngineView(
                    facs = bayesianState.facsMetrics,
                    onStepNext = onStepNextFacs
                )
                "EMF" -> EmfSpatialContextEngineView(
                    emf = bayesianState.emfMetrics,
                    onStepNext = onStepNextEmfSpatial
                )
                "LATENCY" -> CognitiveLatencyDwellEngineView(
                    latency = bayesianState.latencyMetrics,
                    onStepNext = onStepNextLatency
                )
                "FATIGUE" -> DecisionFatigueDepletionEngineView(
                    fatigue = bayesianState.fatigueMetrics,
                    onStepNext = onStepNextFatigue
                )
                "RESPIRATION" -> RespiratoryPatternEngineView(
                    respiration = bayesianState.respiratoryMetrics,
                    onTriggerApnea = onTriggerCognitiveApnea
                )
                "SUBVOCAL" -> SubvocalSpeechEngineView(
                    subvocal = bayesianState.subvocalMetrics,
                    onStepNext = onStepNextSubvocal
                )
                "SALIENCY" -> VisualSaliencyEngineView(
                    saliency = bayesianState.saliencyMetrics,
                    onCycleTarget = onCycleSaliencyTarget
                )
                "ASSOCIATIVE" -> AssociativeThoughtGraphEngineView(
                    associative = bayesianState.associativeGraphMetrics,
                    onStepNext = onStepNextAssociativeConcept
                )
                "PPG" -> PpgHrvEngineView(
                    ppg = bayesianState.ppgMetrics,
                    onMeasure = onMeasurePpgPulse
                )
                "PUPILLOMETRY" -> PupillometryEngineView(
                    pupil = bayesianState.pupillometryMetrics,
                    onTriggerAha = onTriggerPupilAha
                )
                "HESITATION" -> PsychomotorHesitationView(
                    hesitation = bayesianState.hesitationMetrics,
                    onTouchTap = onTouchTap
                )
                "BRAC" -> UltradianBioRhythmView(
                    bioRhythm = bayesianState.bioRhythmMetrics
                )
            }
        }
    }
}

@Composable
private fun EngineTabChip(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(if (isSelected) NeuralAccent else NeuralCardPurple)
            .border(
                1.dp,
                if (isSelected) NeuralAccent else NeuralAccent.copy(alpha = 0.2f),
                RoundedCornerShape(12.dp)
            )
            .clickable { onClick() }
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Text(
            text = label,
            color = if (isSelected) NeuralDeepPurple else NeuralTextPrimary,
            fontSize = 11.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
        )
    }
}

// 1. 🧠 HIERARCHICAL BAYESIAN INTENT ENGINE VIEW
@Composable
private fun BayesianIntentEngineView(
    bayesianState: HierarchicalBayesianState,
    onRecompute: () -> Unit,
    onApplyHypothesis: (ThoughtHypothesis) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(NeuralDeepPurple)
                .border(1.dp, NeuralAccent.copy(alpha = 0.3f), RoundedCornerShape(16.dp))
                .padding(12.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "დომინანტური განზრახვა (9-ძრავიანი სინთეზი)",
                        color = NeuralAccent,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "${String.format(Locale.US, "%.1f", bayesianState.overallCertaintyPct)}% სიზუსტე",
                        color = Color(0xFF00F5D4),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Text(
                    text = bayesianState.dominantThought,
                    color = NeuralTextPrimary,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "კოგნიტური მდგომარეობა: ${bayesianState.cognitiveStateSummary}",
                    color = NeuralTextSecondary,
                    fontSize = 10.sp
                )
            }
        }

        Text(
            text = "აპოსტერიორული ალბათობები P(Intent | Biometrics, Respiration, Subvocal, Saliency):",
            color = NeuralTextSecondary,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold
        )

        bayesianState.topHypotheses.take(4).forEachIndexed { index, hyp ->
            val isTop = index == 0
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(if (isTop) NeuralCardPurple.copy(alpha = 0.8f) else NeuralDeepPurple.copy(alpha = 0.4f))
                    .border(
                        1.dp,
                        if (isTop) NeuralAccent else Color.White.copy(alpha = 0.05f),
                        RoundedCornerShape(12.dp)
                    )
                    .clickable { onApplyHypothesis(hyp) }
                    .padding(10.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "${if (isTop) "⭐ " else ""}${hyp.thoughtSummary}",
                            color = if (isTop) NeuralAccent else NeuralTextPrimary,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.weight(1f)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "${String.format(Locale.US, "%.1f", hyp.probabilityScore * 100)}%",
                            color = if (isTop) Color(0xFF00F5D4) else NeuralTextSecondary,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace
                        )
                    }

                    LinearProgressIndicator(
                        progress = { hyp.probabilityScore.coerceIn(0f, 1f) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(5.dp)
                            .clip(CircleShape),
                        color = if (isTop) NeuralAccent else Color(0xFF7B2CBF),
                        trackColor = Color.White.copy(alpha = 0.08f)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "კატეგორია: ${hyp.primaryIntentCategory}",
                            color = NeuralTextSecondary,
                            fontSize = 9.sp
                        )
                        Text(
                            text = "ქმედება: ${hyp.predictedNextAction}",
                            color = Color(0xFFFFBE0B),
                            fontSize = 9.sp
                        )
                    }
                }
            }
        }

        Button(
            onClick = onRecompute,
            colors = ButtonDefaults.buttonColors(
                containerColor = NeuralAccent,
                contentColor = NeuralDeepPurple
            ),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                imageVector = AppIcons.RefreshIcon,
                contentDescription = "Recompute",
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "ბაიესური ინფერენციის გადათვლა",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

// 🌟 2. 🫁 RESPIRATORY PATTERN & COGNITIVE APNEA VIEW
@Composable
private fun RespiratoryPatternEngineView(
    respiration: RespiratoryPatternMetrics,
    onTriggerApnea: () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "resp")
    val respPhase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 6.28f,
        animationSpec = infiniteRepeatable(
            animation = tween(2400, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "respPhase"
    )

    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(65.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(NeuralDeepPurple)
                .border(
                    1.dp,
                    if (respiration.isCognitiveApneaActive) Color(0xFFFFBE0B) else Color(0xFF00B4D8).copy(alpha = 0.5f),
                    RoundedCornerShape(16.dp)
                )
                .padding(8.dp)
        ) {
            Canvas(modifier = Modifier.fillMaxWidth().height(50.dp)) {
                val width = size.width
                val height = size.height
                val midY = height / 2f
                val path = Path()

                val steps = 50
                val stepWidth = width / steps
                for (i in 0..steps) {
                    val x = i * stepWidth
                    val wave = if (respiration.isCognitiveApneaActive) {
                        -(height * 0.3f) + (sin((i * 0.1f) + respPhase) * (height * 0.05f))
                    } else {
                        sin((i * 0.2f) + respPhase) * (height * 0.38f)
                    }
                    val y = (midY + wave).coerceIn(4f, height - 4f)
                    if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
                }

                drawPath(
                    path = path,
                    color = if (respiration.isCognitiveApneaActive) Color(0xFFFFBE0B) else Color(0xFF00B4D8),
                    style = Stroke(width = 2.dp.toPx())
                )
            }

            Text(
                text = if (respiration.isCognitiveApneaActive) "⚠️ კოგნიტური აპნოე (Breath Hold)" else "რესპირატორული სინუსური არითმია (RSA)",
                color = if (respiration.isCognitiveApneaActive) Color(0xFFFFBE0B) else Color(0xFF00B4D8),
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.TopStart)
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            MetricPill("სუნთქვა (BPM)", "${String.format(Locale.US, "%.1f", respiration.respirationRateBpm)}", Color(0xFF00B4D8), Modifier.weight(1f))
            MetricPill("ვაგუსური ინდექსი", String.format(Locale.US, "%.2f", respiration.vagalRespiratoryIndex), NeuralAccent, Modifier.weight(1f))
            MetricPill("აპნოე (წმ)", "${String.format(Locale.US, "%.1f", respiration.apneaHoldDurationSec)}s", Color(0xFFFFBE0B), Modifier.weight(1f))
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(NeuralCardPurple)
                .padding(10.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(text = respiration.thoughtPacingState, color = NeuralTextPrimary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                Text(text = respiration.georgianInsight, color = NeuralTextSecondary, fontSize = 9.sp)
            }
        }

        Button(
            onClick = onTriggerApnea,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00B4D8), contentColor = NeuralDeepPurple),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(imageVector = AppIcons.Psychology, contentDescription = "Apnea", modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "🫁 კოგნიტური აპნოეს სიმულაცია (ღრმა ფიქრი)", fontSize = 12.sp, fontWeight = FontWeight.Bold)
        }
    }
}

// 🌟 3. 🤫 SUBVOCAL SPEECH & INNER MONOLOGUE VIEW
@Composable
private fun SubvocalSpeechEngineView(
    subvocal: SubvocalSpeechMetrics,
    onStepNext: () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(NeuralDeepPurple)
                .border(1.dp, Color(0xFF7209B7).copy(alpha = 0.6f), RoundedCornerShape(16.dp))
                .padding(12.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "🤫 შინაგანი ხმოვანი მონოლოგი", color = Color(0xFFF72585), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    Text(text = "${String.format(Locale.US, "%.1f", subvocal.innerSpeechConfidencePct)}% სიზუსტე", color = Color(0xFF00F5D4), fontSize = 10.sp, fontWeight = FontWeight.Bold)
                }

                Text(
                    text = "„${subvocal.decodedInnerPhraseSnippet}“",
                    color = NeuralTextPrimary,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "აქტიური ფონემა: [${subvocal.silentPhonemeCandidate}] • ${subvocal.laryngealActivityCategory}",
                    color = NeuralTextSecondary,
                    fontSize = 10.sp
                )
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            MetricPill("ფიქრის ტემპი", "${subvocal.innerMonologueVelocityWpm} WPM", Color(0xFFF72585), Modifier.weight(1f))
            MetricPill("F1/F2 ფორმანტი", "${subvocal.formantF1Hz.toInt()}/${subvocal.formantF2Hz.toInt()}Hz", Color(0xFF00B4D8), Modifier.weight(1f))
            MetricPill("კუნთის დაძაბულობა", "${subvocal.subvocalMuscleTensionPct}%", Color(0xFFFFBE0B), Modifier.weight(1f))
        }

        Button(
            onClick = onStepNext,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF72585), contentColor = Color.White),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(imageVector = AppIcons.RecordVoiceOver, contentDescription = "Subvocal", modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "🤫 შემდეგი შინაგანი აზრის დეკოდირება", fontSize = 12.sp, fontWeight = FontWeight.Bold)
        }
    }
}

// 🌟 4. 🎯 VISUAL SALIENCY & HEATMAP VIEW
@Composable
private fun VisualSaliencyEngineView(
    saliency: VisualSaliencyMetrics,
    onCycleTarget: () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(NeuralDeepPurple)
                .border(1.dp, Color(0xFFFF5400).copy(alpha = 0.5f), RoundedCornerShape(16.dp))
                .padding(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // 4x4 Heatmap Visualizer Box
                Column(
                    verticalArrangement = Arrangement.spacedBy(3.dp),
                    modifier = Modifier
                        .size(54.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color.Black.copy(alpha = 0.5f))
                        .padding(3.dp)
                ) {
                    for (r in 0 until 4) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(3.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            for (c in 0 until 4) {
                                val idx = r * 4 + c
                                val intensity = saliency.heatmapGrid.getOrNull(idx) ?: 0.2f
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(9.dp)
                                        .clip(RoundedCornerShape(2.dp))
                                        .background(Color(0xFFFF5400).copy(alpha = intensity.coerceIn(0.15f, 1f)))
                                )
                            }
                        }
                    }
                }

                Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(3.dp)) {
                    Text(
                        text = "ობიექტი: ${saliency.targetedVisualElement}",
                        color = NeuralTextPrimary,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "განზრახვა: ${saliency.predictedVisualIntent}",
                        color = Color(0xFFFFBE0B),
                        fontSize = 10.sp
                    )
                    Text(
                        text = "ფიქსაცია: ${saliency.fixationDwellDurationMs}ms • დისპერსია: ${String.format(Locale.US, "%.1f", saliency.gazeDispersionRadiusPx)}px",
                        color = NeuralTextSecondary,
                        fontSize = 9.sp
                    )
                }
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            MetricPill("Saliency სიზუსტე", "${String.format(Locale.US, "%.1f", saliency.saliencyConfidencePct)}%", Color(0xFFFF5400), Modifier.weight(1f))
            MetricPill("Focal X/Y", "${(saliency.focalPointX * 100).toInt()}% / ${(saliency.focalPointY * 100).toInt()}%", NeuralAccent, Modifier.weight(1f))
            MetricPill("Dwell Time", "${saliency.fixationDwellDurationMs} ms", Color(0xFF00F5D4), Modifier.weight(1f))
        }

        Button(
            onClick = onCycleTarget,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF5400), contentColor = Color.White),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(imageVector = AppIcons.Visibility, contentDescription = "Target", modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "🎯 ფოკუსის ობიექტის შეცვლა (Eye Saliency)", fontSize = 12.sp, fontWeight = FontWeight.Bold)
        }
    }
}

// 🌟 5. 🧬 ASSOCIATIVE THOUGHT GRAPH & HOPFIELD VIEW
@Composable
private fun AssociativeThoughtGraphEngineView(
    associative: AssociativeThoughtGraphMetrics,
    onStepNext: () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(NeuralDeepPurple)
                .border(1.dp, Color(0xFF4CC9F0).copy(alpha = 0.5f), RoundedCornerShape(16.dp))
                .padding(12.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "🧬 ჰოპფილდის ასოციაციური ატრაქტორი", color = Color(0xFF4CC9F0), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    Text(text = "ენერგია: ${String.format(Locale.US, "%.1f", associative.hopfieldEnergy)} eV", color = NeuralAccent, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                }

                Text(
                    text = "საწყისი აზრი: „${associative.activeSeedConcept}“",
                    color = NeuralTextPrimary,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = associative.associativeInsightGeorgian,
                    color = NeuralTextSecondary,
                    fontSize = 10.sp
                )
            }
        }

        Text(
            text = "მომდევნო 3 ნავარაუდევი ასოციაციური აზრი (Spreading Activation):",
            color = NeuralTextSecondary,
            fontSize = 10.sp,
            fontWeight = FontWeight.SemiBold
        )

        associative.upcomingAssociativePredictions.take(3).forEach { candidate ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(NeuralCardPurple.copy(alpha = 0.6f))
                    .border(1.dp, Color(0xFF4CC9F0).copy(alpha = 0.3f), RoundedCornerShape(10.dp))
                    .padding(8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = "➔ ${candidate.thoughtText}", color = NeuralTextPrimary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        Text(text = "ტიპი: ${candidate.reasoningCategory} • Hop: ${candidate.semanticHopDistance}", color = NeuralTextSecondary, fontSize = 9.sp)
                    }
                    Text(text = "${String.format(Locale.US, "%.1f", candidate.associativeStrengthPct)}%", color = Color(0xFF00F5D4), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        Button(
            onClick = onStepNext,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CC9F0), contentColor = NeuralDeepPurple),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(imageVector = AppIcons.AutoGraph, contentDescription = "Graph", modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "🧬 ასოციაციური გრაფის გადართვა", fontSize = 12.sp, fontWeight = FontWeight.Bold)
        }
    }
}

// 6. 💓 PPG & HRV METRICS VIEW
@Composable
private fun PpgHrvEngineView(
    ppg: PpgHrvMetrics,
    onMeasure: () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "ppg")
    val pulsePhase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 6.28f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "pulse"
    )

    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(65.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(NeuralDeepPurple)
                .border(1.dp, Color(0xFFFF006E).copy(alpha = 0.4f), RoundedCornerShape(16.dp))
                .padding(8.dp)
        ) {
            Canvas(modifier = Modifier.fillMaxWidth().height(50.dp)) {
                val width = size.width
                val height = size.height
                val midY = height / 2f
                val path = Path()

                val steps = 50
                val stepWidth = width / steps
                for (i in 0..steps) {
                    val x = i * stepWidth
                    val wave = sin((i * 0.35f) + pulsePhase) * (height * 0.35f)
                    val ppgSpike = if (i % 15 == 7) -(height * 0.4f) else 0f
                    val y = (midY + wave + ppgSpike).coerceIn(4f, height - 4f)
                    if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
                }

                drawPath(
                    path = path,
                    color = Color(0xFFFF006E),
                    style = Stroke(width = 2.dp.toPx())
                )
            }

            Text(
                text = "PPG ოპტიკური პულსის ტალღა",
                color = Color(0xFFFF006E),
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.TopStart)
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            MetricPill("პულსი (BPM)", "${ppg.heartRateBpm.toInt()} BPM", Color(0xFFFF006E), Modifier.weight(1f))
            MetricPill("HRV RMSSD", "${String.format(Locale.US, "%.1f", ppg.rmssdMs)} ms", NeuralAccent, Modifier.weight(1f))
            MetricPill("LF/HF ბალანსი", String.format(Locale.US, "%.2f", ppg.lfHfRatio), Color(0xFF00B4D8), Modifier.weight(1f))
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(NeuralCardPurple)
                .padding(10.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "სტრესის ინდექსი: ${ppg.baevskyStressIndex.toInt()}", color = NeuralTextPrimary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    Text(text = ppg.emotionalArousalState, color = Color(0xFFFFBE0B), fontSize = 10.sp, fontWeight = FontWeight.Bold)
                }
                Text(text = "სისხლძარღვთა მიკრო-პულსაცია კალიბრირებულია სმარტფონის სენსორით.", color = NeuralTextSecondary, fontSize = 9.sp)
            }
        }

        Button(
            onClick = onMeasure,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF006E), contentColor = Color.White),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(imageVector = AppIcons.WaterDrop, contentDescription = "Measure", modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "🔬 PPG ოპტიკური პულსის გაზომვა", fontSize = 12.sp, fontWeight = FontWeight.Bold)
        }
    }
}

// 7. 👁️ PUPILLOMETRY & COGNITIVE LOAD VIEW
@Composable
private fun PupillometryEngineView(
    pupil: PupillometryMetrics,
    onTriggerAha: () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(NeuralDeepPurple)
                .border(1.dp, if (pupil.isAhaDecisionMoment) Color(0xFFFFBE0B) else NeuralAccent.copy(alpha = 0.3f), RoundedCornerShape(16.dp))
                .padding(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.1f))
                        .border(2.dp, NeuralAccent, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    val pupilRadius = (pupil.pupilDiameterMm * 4.2f).coerceIn(8f, 20f)
                    Box(
                        modifier = Modifier
                            .size((pupilRadius * 2).dp)
                            .clip(CircleShape)
                            .background(if (pupil.isAhaDecisionMoment) Color(0xFFFFBE0B) else Color.Black)
                            .border(1.dp, NeuralAccent, CircleShape)
                    )
                }

                Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text = if (pupil.isAhaDecisionMoment) "💡 აღმოჩენილია AHA! MOMENT" else "გუგის დიამეტრი: ${String.format(Locale.US, "%.1f", pupil.pupilDiameterMm)} mm",
                        color = if (pupil.isAhaDecisionMoment) Color(0xFFFFBE0B) else NeuralTextPrimary,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "მიკრო-საკადა: ${String.format(Locale.US, "%.1f", pupil.microSaccadeRateHz)} Hz • მდგომარეობა: ${pupil.attentionStateDescription}",
                        color = NeuralTextSecondary,
                        fontSize = 10.sp
                    )
                }
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            MetricPill("სიჩქარე (mm/s)", String.format(Locale.US, "%.2f", pupil.phasicDilationVelocity), Color(0xFF00F5D4), Modifier.weight(1f))
            MetricPill("ფიქსაცია (ms)", "${pupil.visualFocusFixationDurationMs} ms", NeuralAccent, Modifier.weight(1f))
            MetricPill("ვიზუალური ენტროპია", String.format(Locale.US, "%.2f", pupil.visualEntropyScore), Color(0xFFFFBE0B), Modifier.weight(1f))
        }

        Button(
            onClick = onTriggerAha,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFBE0B), contentColor = NeuralDeepPurple),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(imageVector = AppIcons.Lightbulb, contentDescription = "Aha", modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "💡 AHA! MOMENT ინსაითის სიმულაცია", fontSize = 12.sp, fontWeight = FontWeight.Bold)
        }
    }
}

// 8. ⏱️ PSYCHOMOTOR HESITATION & TOUCH VIEW
@Composable
private fun PsychomotorHesitationView(
    hesitation: PsychomotorHesitationMetrics,
    onTouchTap: (Float, Float) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(75.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(Brush.linearGradient(listOf(NeuralDeepPurple, NeuralCardPurple)))
                .border(1.dp, Color(0xFF7B2CBF).copy(alpha = 0.5f), RoundedCornerShape(16.dp))
                .pointerInput(Unit) {
                    detectTapGestures { offset ->
                        onTouchTap(offset.x, offset.y)
                    }
                },
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(imageVector = AppIcons.TouchApp, contentDescription = "Tap Pad", tint = NeuralAccent, modifier = Modifier.size(22.dp))
                Text(text = "დააჭირეთ აქ ტემპისა და ყოყმანის გასაზომად", color = NeuralTextPrimary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                Text(text = "ფრენის დრო: ${hesitation.flightTimeMs} ms • Hold: ${hesitation.holdDurationMs} ms", color = NeuralAccent, fontSize = 9.sp)
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            MetricPill("სიზუსტე", "${hesitation.hesitationConfidencePct.toInt()}%", Color(0xFFFF5400), Modifier.weight(1f))
            MetricPill("შენონის ენტროპია", String.format(Locale.US, "%.2f", hesitation.shannonEntropyScore), Color(0xFF00B4D8), Modifier.weight(1f))
            MetricPill("მიკრო-ტრემორი", "${String.format(Locale.US, "%.1f", hesitation.spatialJitterPixelVariance)} px", NeuralAccent, Modifier.weight(1f))
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(NeuralDeepPurple)
                .padding(10.dp)
        ) {
            Text(
                text = "მოტორული რეჟიმი: ${hesitation.cognitiveMode}",
                color = NeuralTextPrimary,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

// 9. 🌙 ULTRADIAN BIO-RHYTHM (90-MIN BRAC) VIEW
@Composable
private fun UltradianBioRhythmView(
    bioRhythm: UltradianBioRhythmMetrics
) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(NeuralDeepPurple)
                .border(1.dp, Color(0xFF7209B7).copy(alpha = 0.5f), RoundedCornerShape(16.dp))
                .padding(12.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "🌙 90-წუთიანი BRAC ციკლი (${bioRhythm.currentUltradianPhaseMinutes}წთ)",
                        color = Color(0xFFB5179E),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = if (bioRhythm.isPeakCognitiveWindow) "⭐ პიკური ფანჯარა" else "განტვირთვის ფაზა",
                        color = NeuralAccent,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Text(
                    text = bioRhythm.circadianPhaseName,
                    color = NeuralTextPrimary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "ოპტიმალური დომენი: ${bioRhythm.optimalThoughtDomain}",
                    color = NeuralTextSecondary,
                    fontSize = 10.sp
                )
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            MetricPill("ულტრადიანული ენერგია", "${bioRhythm.ultradianEnergyPercent}%", Color(0xFF00F5D4), Modifier.weight(1f))
            MetricPill("კოგნიტური რეზერვი", "${bioRhythm.cognitiveFuelReservePct}%", Color(0xFFFFBE0B), Modifier.weight(1f))
            MetricPill("რეკომენდებული რელაქსი", "${bioRhythm.recoveryRecommendationMinutes} წთ", Color(0xFFB5179E), Modifier.weight(1f))
        }
    }
}

@Composable
private fun MetricPill(
    label: String,
    value: String,
    accentColor: Color,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(NeuralDeepPurple)
            .border(1.dp, accentColor.copy(alpha = 0.3f), RoundedCornerShape(12.dp))
            .padding(vertical = 6.dp, horizontal = 6.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(text = label, color = NeuralTextSecondary, fontSize = 9.sp)
            Text(text = value, color = accentColor, fontSize = 10.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
        }
    }
}

// 10. ⚡ FACS MICRO-EXPRESSION DYNAMICS VIEW (40-120ms)
@Composable
private fun FacsMicroExpressionEngineView(
    facs: FacsMicroExpressionMetrics,
    onStepNext: () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(NeuralDeepPurple)
                .border(1.dp, Color(0xFFFF5964).copy(alpha = 0.5f), RoundedCornerShape(16.dp))
                .padding(12.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "⚡ FACS მიკრო-მიმიკა (${facs.microExpressionDurationMs}ms)",
                        color = Color(0xFFFF5964),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "${100 - facs.facialFrictionPct}% სანდოობა",
                        color = NeuralAccent,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Text(
                    text = "დომინანტური ემოცია: ${facs.detectedMicroEmotion}",
                    color = NeuralTextPrimary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = facs.georgianMicroInsight,
                    color = NeuralTextSecondary,
                    fontSize = 10.sp
                )
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            MetricPill("AU4 წარბის დაჭიმულობა", String.format(Locale.US, "%.2f", facs.eyebrowFurrowAU4), Color(0xFFFF5964), Modifier.weight(1f))
            MetricPill("AU12 ღიმილი", String.format(Locale.US, "%.2f", facs.smileLipCornerAU12), Color(0xFF00F5D4), Modifier.weight(1f))
            MetricPill("ემოციური ვალენტობა", String.format(Locale.US, "%.2f", facs.emotionalValenceScore), if (facs.emotionalValenceScore >= 0) Color(0xFF00F5D4) else Color(0xFFFF5964), Modifier.weight(1f))
        }

        Button(
            onClick = onStepNext,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF5964).copy(alpha = 0.25f)),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("⚡ შემდეგი მიკრო-მიმიკის სიმულაცია (FACS AU Step)", color = Color(0xFFFF5964), fontSize = 11.sp, fontWeight = FontWeight.Bold)
        }
    }
}

// 11. 🧲 EMF & SPATIAL CONTEXT VIEW
@Composable
private fun EmfSpatialContextEngineView(
    emf: EmfSpatialContextMetrics,
    onStepNext: () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(NeuralDeepPurple)
                .border(1.dp, Color(0xFF4CC9F0).copy(alpha = 0.5f), RoundedCornerShape(16.dp))
                .padding(12.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "🧲 EMF & სივრცითი გარემო",
                        color = Color(0xFF4CC9F0),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = if (emf.proximityToElectronicsScore > 0.5f) "⚡ მოწყობილობასთან ახლოს" else "თავისუფალი ველი",
                        color = if (emf.proximityToElectronicsScore > 0.5f) Color(0xFFFFBE0B) else Color(0xFF00F5D4),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Text(
                    text = "გარემოს დომენი: ${emf.estimatedEnvironmentDomain}",
                    color = NeuralTextPrimary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "კოგნიტური შეზღუდვა: ${emf.spatialCognitiveDomainConstraint}",
                    color = NeuralTextSecondary,
                    fontSize = 10.sp
                )
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            MetricPill("მაგნიტური ველი (EMF)", "${String.format(Locale.US, "%.1f", emf.magneticFluxDensityMicroTesla)} µT", Color(0xFF4CC9F0), Modifier.weight(1f))
            MetricPill("სიმაღლის დელტა", "${String.format(Locale.US, "%.1f", emf.altitudeDeltaMeters)} m", Color(0xFF00F5D4), Modifier.weight(1f))
            MetricPill("RF/BT სიგნალები", "${emf.ambientRssiDensity} აქტიური", Color(0xFFFFBE0B), Modifier.weight(1f))
        }

        Button(
            onClick = onStepNext,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CC9F0).copy(alpha = 0.25f)),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("🧲 გარემოს პროფილის გადართვა (სამუშაო/ტრანზიტი/გარე)", color = Color(0xFF4CC9F0), fontSize = 11.sp, fontWeight = FontWeight.Bold)
        }
    }
}

// 12. ⏳ COGNITIVE LATENCY & DWELL DYNAMICS VIEW (System 1 vs System 2)
@Composable
private fun CognitiveLatencyDwellEngineView(
    latency: CognitiveLatencyMetrics,
    onStepNext: () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(NeuralDeepPurple)
                .border(1.dp, Color(0xFFF72585).copy(alpha = 0.5f), RoundedCornerShape(16.dp))
                .padding(12.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "⏳ კოგნიტური ლატენტობა (${latency.stimulusResponseLatencyMs}ms)",
                        color = Color(0xFFF72585),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = if (latency.cognitiveFrictionIndex > 0.4f) "🧠 System 2 (ღრმა)" else "⚡ System 1 (სწრაფი)",
                        color = if (latency.cognitiveFrictionIndex > 0.4f) Color(0xFFFFBE0B) else Color(0xFF00F5D4),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Text(
                    text = "აზროვნების რეჟიმი: ${latency.decisionMode}",
                    color = NeuralTextPrimary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = latency.georgianLatencyInsight,
                    color = NeuralTextSecondary,
                    fontSize = 10.sp
                )
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            MetricPill("პასუხის ლატენტობა", "${latency.stimulusResponseLatencyMs} ms", Color(0xFFF72585), Modifier.weight(1f))
            MetricPill("მოქმედების დაყოვნება", "${latency.dwellTimeBeforeActionMs} ms", Color(0xFF00F5D4), Modifier.weight(1f))
            MetricPill("ფრიქციის ინდექსი", "${(latency.cognitiveFrictionIndex * 100).toInt()}%", Color(0xFFFFBE0B), Modifier.weight(1f))
        }

        Button(
            onClick = onStepNext,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF72585).copy(alpha = 0.25f)),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("⏳ ლატენტობის რეჟიმის შეცვლა (System 1/2 პულსი)", color = Color(0xFFF72585), fontSize = 11.sp, fontWeight = FontWeight.Bold)
        }
    }
}

// 13. 🧠 DECISION FATIGUE & EGO DEPLETION VIEW
@Composable
private fun DecisionFatigueDepletionEngineView(
    fatigue: DecisionFatigueMetrics,
    onStepNext: () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(NeuralDeepPurple)
                .border(1.dp, Color(0xFFFFB703).copy(alpha = 0.5f), RoundedCornerShape(16.dp))
                .padding(12.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "🧠 მენტალური რესურსი & ენტროპია",
                        color = Color(0xFFFFB703),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = if (fatigue.mentalEnergyReservePct < 40) "⚠️ Ego Depleted" else "სრულად ენერგიული",
                        color = if (fatigue.mentalEnergyReservePct < 40) Color(0xFFFF5964) else Color(0xFF00F5D4),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Text(
                    text = "გადაღლის სტადია: ${fatigue.decisionDepletionLevel}",
                    color = NeuralTextPrimary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = fatigue.georgianFatigueSummary,
                    color = NeuralTextSecondary,
                    fontSize = 10.sp
                )
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            MetricPill("მენტალური რეზერვი", "${fatigue.mentalEnergyReservePct}%", if (fatigue.mentalEnergyReservePct > 50) Color(0xFF00F5D4) else Color(0xFFFF5964), Modifier.weight(1f))
            MetricPill("გადაწყვეტილებები", "${fatigue.accumulatedDecisionsToday}", Color(0xFFFFB703), Modifier.weight(1f))
            MetricPill("მიდრეკილება", fatigue.heuristicBiasTendency.take(18) + "...", Color(0xFF00B4D8), Modifier.weight(1f))
        }

        Button(
            onClick = onStepNext,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFB703).copy(alpha = 0.25f)),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("🧠 გადაწყვეტილების მიღება (ენერგიის ამოწურვის ციკლი)", color = Color(0xFFFFB703), fontSize = 11.sp, fontWeight = FontWeight.Bold)
        }
    }
}
