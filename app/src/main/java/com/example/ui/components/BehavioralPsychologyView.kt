package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.AppIcons
import com.example.ui.theme.NeuralAccent
import com.example.ui.theme.NeuralBackground
import com.example.ui.theme.NeuralCardPurple
import com.example.ui.theme.NeuralCyanAccent
import com.example.ui.theme.NeuralDeepPurple
import com.example.ui.theme.NeuralGreenActive
import com.example.ui.theme.NeuralSurface
import com.example.ui.theme.NeuralTextPrimary
import com.example.ui.theme.NeuralTextSecondary
import com.example.viewmodel.BehavioralPsychologyState
import com.example.viewmodel.CantabPsychState
import com.example.viewmodel.LslExportState
import com.example.viewmodel.PsychologicalTestState
import com.example.viewmodel.VoiceBiomarkersState
import com.example.viewmodel.WearableDeviceItem
import com.example.viewmodel.WearablesSuiteState

@Composable
fun BehavioralPsychologyView(
    psychologyState: BehavioralPsychologyState,
    voiceState: VoiceBiomarkersState,
    cantabState: CantabPsychState,
    wearablesState: WearablesSuiteState,
    testState: PsychologicalTestState,
    lslState: LslExportState,
    onToggleDevice: (String) -> Unit,
    onScanBle: () -> Unit,
    onSwitchTestType: (String) -> Unit,
    onAnswerStroop: (String) -> Unit,
    onTriggerGoNoGo: (Boolean) -> Unit,
    onAnswerIat: (String) -> Unit,
    onClickCantabBox: (Int) -> Unit,
    onToggleVoiceAnalysis: () -> Unit,
    onTriggerAcousticStress: () -> Unit,
    onToggleLsl: () -> Unit,
    onSetLslFormat: (String) -> Unit,
    onExportLslData: () -> Unit,
    onTriggerGsrPeak: () -> Unit,
    onSimulateSystem1Or2: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 1. Hero Overview Header Card
        PsychologyHeroBanner(psychologyState, voiceState, cantabState)

        // 2. System 1 vs System 2 (Kahneman Cognitive Dual-Process Engine)
        System1VsSystem2Card(
            psychologyState = psychologyState,
            onSimulate = onSimulateSystem1Or2
        )

        // 3. Acoustic Voice Biomarkers Engine (Sonde / Sonar Model)
        VoiceBiomarkersCard(
            voiceState = voiceState,
            onToggle = onToggleVoiceAnalysis,
            onTriggerSample = onTriggerAcousticStress
        )

        // 4. CANTAB Cognitive Architecture & Pupillometry (Cambridge Cognition + TEPR)
        CantabPupillometryCard(cantabState = cantabState)

        // 5. Interactive Psychological Test Sandbox (Stroop, Go/No-Go, IAT, CANTAB SWM)
        InteractivePsychTestCard(
            testState = testState,
            onSwitchTestType = onSwitchTestType,
            onAnswerStroop = onAnswerStroop,
            onTriggerGoNoGo = onTriggerGoNoGo,
            onAnswerIat = onAnswerIat,
            onClickCantabBox = onClickCantabBox
        )

        // 6. Ego Depletion & Decision Fatigue Tracker
        EgoDepletionCard(psychologyState)

        // 7. Keystroke & Touch Dynamics (Beiwe / Mindstrong Model)
        KeystrokeTouchDynamicsCard(psychologyState)

        // 8. Emotional Valence & Galvanic Skin Response (GSR / EDA)
        EmotionalValenceGsrCard(
            psychologyState = psychologyState,
            onTriggerGsrPeak = onTriggerGsrPeak
        )

        // 9. Circadian & Sleep Architecture Phenotyping
        CircadianSleepCard(psychologyState)

        // 10. External BLE Wearables Suite (Muse, Empatica, Oura, BioAmp, Tobii)
        WearablesSuiteCard(
            wearablesState = wearablesState,
            onToggleDevice = onToggleDevice,
            onScanBle = onScanBle
        )

        // 11. Lab Streaming Layer (LSL) & Scientific Data Export (Python / NeuroKit2 / MATLAB)
        LslScientificExportCard(
            lslState = lslState,
            onToggleLsl = onToggleLsl,
            onSetLslFormat = onSetLslFormat,
            onExportLslData = onExportLslData
        )
    }
}

@Composable
private fun PsychologyHeroBanner(
    state: BehavioralPsychologyState,
    voice: VoiceBiomarkersState,
    cantab: CantabPsychState
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = NeuralSurface),
        border = androidx.compose.foundation.BorderStroke(1.dp, NeuralAccent.copy(alpha = 0.35f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
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
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(NeuralDeepPurple),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("🧬", fontSize = 18.sp)
                    }
                    Column {
                        Text(
                            text = "ქცევითი & ფსიქოლოგიური ლაბორატორია",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "CANTAB • Acoustic Sonar • IAT • Kahneman S1/S2 • Beiwe",
                            fontSize = 11.sp,
                            color = NeuralTextSecondary
                        )
                    }
                }
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(NeuralGreenActive.copy(alpha = 0.15f))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "LIVE • MULTIMODAL",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = NeuralGreenActive
                    )
                }
            }

            Text(
                text = "ადამიანის ქცევისა და ფსიქიკის ყოველმხრივი ანალიზი: ხმის მიკრო-აკუსტიკა, ქვეცნობიერი ასოციაციები (IAT), სამუშაო მეხსიერება (CANTAB), კლავიატურული დინამიკა (Beiwe) და გუგის კოგნიტური დატვირთვა.",
                fontSize = 12.sp,
                color = NeuralTextPrimary,
                lineHeight = 17.sp
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                MetricChip("System 1/2", "${state.system1RatioPct}% / ${state.system2RatioPct}%", NeuralCyanAccent, Modifier.weight(1.2f))
                MetricChip("ხმის Jitter", "${voice.pitchJitterPct}%", Color(0xFFFFD54F), Modifier.weight(1f))
                MetricChip("CANTAB Span", "${cantab.spatialMemorySpan} ერთეული", NeuralAccent, Modifier.weight(1.1f))
                MetricChip("მზაობა", "${state.morningReadinessScore}/100", NeuralGreenActive, Modifier.weight(1f))
            }
        }
    }
}

@Composable
private fun System1VsSystem2Card(
    psychologyState: BehavioralPsychologyState,
    onSimulate: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = NeuralSurface),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.08f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("⚖️", fontSize = 18.sp)
                    Text(
                        text = "System 1 vs System 2 (კანემანის მოდელი)",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
                Text(
                    text = "${psychologyState.averageDecisionLatencyMs} ms",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace,
                    color = NeuralCyanAccent
                )
            }

            Text(
                text = psychologyState.cognitiveModeDescription,
                fontSize = 12.sp,
                color = NeuralAccent,
                fontWeight = FontWeight.Medium
            )

            // Progress balance bar
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("⚡ System 1 (იმპულსი <250ms): ${psychologyState.system1RatioPct}%", fontSize = 11.sp, color = NeuralCyanAccent)
                    Text("🧠 System 2 (ანალიზი >600ms): ${psychologyState.system2RatioPct}%", fontSize = 11.sp, color = NeuralAccent)
                }
                LinearProgressIndicator(
                    progress = { psychologyState.system1RatioPct / 100f },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp)),
                    color = NeuralCyanAccent,
                    trackColor = NeuralDeepPurple
                )
            }

            Button(
                onClick = onSimulate,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = NeuralDeepPurple),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text("🎲 კოგნიტური სტიმულის სიმულაცია (System 1 ➔ System 2)", fontSize = 12.sp, color = NeuralAccent)
            }
        }
    }
}

@Composable
private fun VoiceBiomarkersCard(
    voiceState: VoiceBiomarkersState,
    onToggle: () -> Unit,
    onTriggerSample: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = NeuralSurface),
        border = androidx.compose.foundation.BorderStroke(1.dp, NeuralCyanAccent.copy(alpha = 0.3f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("🎙️", fontSize = 18.sp)
                    Text(
                        text = "ხმის აკუსტიკური ბიომარკერები (Sonde/Sonar)",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
                Button(
                    onClick = onToggle,
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (voiceState.isVoiceAnalyzing) NeuralGreenActive else NeuralCardPurple
                    ),
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 8.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = if (voiceState.isVoiceAnalyzing) "აქტიურია" else "გათიშულია",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (voiceState.isVoiceAnalyzing) Color.Black else Color.White
                    )
                }
            }

            Text(
                text = voiceState.vocalAcousticState,
                fontSize = 12.sp,
                color = NeuralGreenActive
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                MetricChip("Pitch (F0)", "${voiceState.fundamentalFrequencyF0Hz} Hz", NeuralCyanAccent, Modifier.weight(1f))
                MetricChip("Jitter", "${voiceState.pitchJitterPct}%", if (voiceState.pitchJitterPct > 1.04f) Color(0xFFFF5252) else NeuralGreenActive, Modifier.weight(1f))
                MetricChip("Shimmer", "${voiceState.amplitudeShimmerPct}%", if (voiceState.amplitudeShimmerPct > 3.8f) Color(0xFFFF5252) else NeuralAccent, Modifier.weight(1f))
                MetricChip("გადაწვა (Burnout)", "${voiceState.vocalDepressionBurnoutRiskPct}%", if (voiceState.vocalDepressionBurnoutRiskPct > 25) Color(0xFFFF5252) else NeuralGreenActive, Modifier.weight(1.2f))
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("ტემპი: ${voiceState.speechCadenceSyllablesPerSec} მარცვალი/წმ", fontSize = 11.sp, color = NeuralTextSecondary)
                Text("HNR სისუფთავე: ${voiceState.harmonicToNoiseRatioDb} dB", fontSize = 11.sp, color = NeuralCyanAccent)
                Text("პაუზის წილი: ${voiceState.pauseToSpeechRatioPct}%", fontSize = 11.sp, color = NeuralTextSecondary)
            }

            OutlinedButton(
                onClick = onTriggerSample,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, NeuralCyanAccent.copy(alpha = 0.4f))
            ) {
                Text("🎙️ ხმის აკუსტიკური სტრესის ნიმუშის ტესტირება", fontSize = 11.sp, color = NeuralCyanAccent)
            }
        }
    }
}

@Composable
private fun CantabPupillometryCard(cantabState: CantabPsychState) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = NeuralSurface),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.08f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("🧠", fontSize = 18.sp)
                    Text(
                        text = "CANTAB კოგნიტური არქიტექტურა & გუგის ტრეკინგი",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
                Text(
                    text = "TEPR: ${cantabState.pupilDilationTeprMm} mm",
                    fontSize = 12.sp,
                    fontFamily = FontFamily.Monospace,
                    color = NeuralAccent
                )
            }

            Text(
                text = "ქვეცნობიერი ასოციაციები (IAT): ${cantabState.implicitBiasStatus}",
                fontSize = 12.sp,
                color = NeuralCyanAccent
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                MetricChip("SWM მეხსიერება", "${cantabState.spatialMemorySpan} ერთეული", NeuralGreenActive, Modifier.weight(1.1f))
                MetricChip("PAL ასოციაცია", "${cantabState.pairedAssociatesScorePct}%", NeuralAccent, Modifier.weight(1f))
                MetricChip("მოქნილობა (WCST)", "${cantabState.cognitiveFlexibilityScorePct}%", NeuralCyanAccent, Modifier.weight(1.1f))
                MetricChip("IAT D-Score", "+${cantabState.iatDScore}", Color(0xFFFFD54F), Modifier.weight(1f))
            }

            Text(
                text = "გუგის კოგნიტური დატვირთვა (Task-Evoked Pupillary Response): ${cantabState.pupilDilationTeprMm} მმ • კოგნიტური ფრიქცია: ${cantabState.cognitiveFrictionIndex} (ოპტიმალური)",
                fontSize = 11.sp,
                color = NeuralTextSecondary
            )
        }
    }
}

@Composable
private fun InteractivePsychTestCard(
    testState: PsychologicalTestState,
    onSwitchTestType: (String) -> Unit,
    onAnswerStroop: (String) -> Unit,
    onTriggerGoNoGo: (Boolean) -> Unit,
    onAnswerIat: (String) -> Unit,
    onClickCantabBox: (Int) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = NeuralSurface),
        border = androidx.compose.foundation.BorderStroke(1.dp, NeuralAccent.copy(alpha = 0.3f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("🎮", fontSize = 18.sp)
                    Text(
                        text = "ინტერაქტიული ფსიქოლოგიური ლაბორატორია",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(NeuralDeepPurple)
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "ქულა: ${testState.testScorePct}%",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = NeuralAccent
                    )
                }
            }

            // 4 Mode Tabs
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                TestTabButton("Stroop", testState.activeTestType == "STROOP", Modifier.weight(1f)) {
                    onSwitchTestType("STROOP")
                }
                TestTabButton("Go/No-Go", testState.activeTestType == "GO_NO_GO", Modifier.weight(1f)) {
                    onSwitchTestType("GO_NO_GO")
                }
                TestTabButton("IAT ასოციაცია", testState.activeTestType == "IAT", Modifier.weight(1.2f)) {
                    onSwitchTestType("IAT")
                }
                TestTabButton("CANTAB SWM", testState.activeTestType == "CANTAB_SWM", Modifier.weight(1.2f)) {
                    onSwitchTestType("CANTAB_SWM")
                }
            }

            Text(
                text = testState.statusMessage,
                fontSize = 12.sp,
                color = NeuralTextPrimary
            )

            // Test Display Arena
            when (testState.activeTestType) {
                "STROOP" -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(NeuralBackground)
                            .padding(vertical = 18.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                text = testState.stroopWord,
                                fontSize = 32.sp,
                                fontWeight = FontWeight.Black,
                                color = Color(testState.stroopInkColorHex)
                            )
                            Text(
                                text = "(აირჩიეთ ტექსტის ფერი და არა სიტყვის მნიშვნელობა!)",
                                fontSize = 11.sp,
                                color = NeuralTextSecondary
                            )
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        testState.stroopOptions.forEach { opt ->
                            Button(
                                onClick = { onAnswerStroop(opt) },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = when (opt) {
                                        "წითელი" -> Color(0xFFD32F2F)
                                        "მწვანე" -> Color(0xFF388E3C)
                                        "ლურჯი" -> Color(0xFF1976D2)
                                        else -> Color(0xFFFBC02D)
                                    }
                                )
                            ) {
                                Text(opt, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            }
                        }
                    }
                }

                "GO_NO_GO" -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(NeuralBackground)
                            .padding(vertical = 18.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(54.dp)
                                    .clip(CircleShape)
                                    .background(if (testState.isGoSignal) NeuralGreenActive else Color(0xFFFF5252)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(if (testState.isGoSignal) "GO" else "STOP", fontWeight = FontWeight.Black, color = Color.Black, fontSize = 14.sp)
                            }
                            Text(
                                text = testState.goNoGoPrompt,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (testState.isGoSignal) NeuralGreenActive else Color(0xFFFF8A80)
                            )
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = { onTriggerGoNoGo(true) },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(containerColor = NeuralGreenActive),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text("დაჭერა (GO!)", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }
                        Button(
                            onClick = { onTriggerGoNoGo(false) },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(containerColor = NeuralCardPurple),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text("შეჩერება (PASS)", color = Color.White, fontSize = 12.sp)
                        }
                    }
                }

                "IAT" -> {
                    // Implicit Association Test
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(NeuralBackground)
                            .padding(vertical = 18.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text = testState.iatStimulusWord,
                                fontSize = 28.sp,
                                fontWeight = FontWeight.Black,
                                color = NeuralCyanAccent
                            )
                            Text(
                                text = "ქვეცნობიერი ასოციაცია: სწრაფად მიუსადაგეთ კატეგორია",
                                fontSize = 11.sp,
                                color = NeuralTextSecondary
                            )
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = { onAnswerIat(testState.iatLeftCategory) },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(containerColor = NeuralDeepPurple),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text("👈 ${testState.iatLeftCategory}", fontSize = 11.sp, color = NeuralAccent, fontWeight = FontWeight.Bold)
                        }
                        Button(
                            onClick = { onAnswerIat(testState.iatRightCategory) },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(containerColor = NeuralCardPurple),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text("${testState.iatRightCategory} 👉", fontSize = 11.sp, color = Color.White, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                "CANTAB_SWM" -> {
                    // CANTAB Spatial Working Memory Grid
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(NeuralBackground)
                            .padding(14.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = "იპოვეთ დამალული სიმბოლო ყუთებში (ნაპოვნია: ${testState.cantabFoundCount} • შეცდომა: ${testState.cantabErrorsCount})",
                                fontSize = 11.sp,
                                color = NeuralTextSecondary
                            )

                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                testState.cantabBoxes.take(3).forEach { boxId ->
                                    Box(
                                        modifier = Modifier
                                            .size(52.dp)
                                            .clip(RoundedCornerShape(10.dp))
                                            .background(NeuralDeepPurple)
                                            .border(1.dp, NeuralAccent.copy(alpha = 0.5f), RoundedCornerShape(10.dp))
                                            .clickable { onClickCantabBox(boxId) },
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text("🎁 #$boxId", fontSize = 11.sp, color = Color.White, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }

                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                testState.cantabBoxes.drop(3).forEach { boxId ->
                                    Box(
                                        modifier = Modifier
                                            .size(52.dp)
                                            .clip(RoundedCornerShape(10.dp))
                                            .background(NeuralDeepPurple)
                                            .border(1.dp, NeuralAccent.copy(alpha = 0.5f), RoundedCornerShape(10.dp))
                                            .clickable { onClickCantabBox(boxId) },
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text("🎁 #$boxId", fontSize = 11.sp, color = Color.White, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("ბოლო ლატენტურობა: ${testState.lastReactionLatencyMs} ms", fontSize = 11.sp, color = NeuralTextSecondary)
                Text(testState.evaluatedMode, fontSize = 11.sp, color = NeuralCyanAccent, fontWeight = FontWeight.Medium)
            }
        }
    }
}

@Composable
private fun EgoDepletionCard(psychologyState: BehavioralPsychologyState) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = NeuralSurface),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.08f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("🔋", fontSize = 18.sp)
                    Text(
                        text = "Ego Depletion & გადაწყვეტილების გადაღლა",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
                Text(
                    text = "${psychologyState.dailyMicroDecisionsCount} / ${psychologyState.maxDailyDecisionsBudget}",
                    fontSize = 12.sp,
                    fontFamily = FontFamily.Monospace,
                    color = NeuralAccent
                )
            }

            Text(
                text = "ნებისყოფის სტატუსი: ${psychologyState.willpowerStatus}",
                fontSize = 12.sp,
                color = NeuralGreenActive
            )

            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("გადაღლის ინდექსი: ${psychologyState.egoDepletionPct}%", fontSize = 11.sp, color = NeuralTextSecondary)
                    Text("შეცდომის რისკი: ${psychologyState.mistakeSusceptibilityPct}%", fontSize = 11.sp, color = if (psychologyState.mistakeSusceptibilityPct > 20) Color(0xFFFF5252) else NeuralCyanAccent)
                }
                LinearProgressIndicator(
                    progress = { psychologyState.egoDepletionPct / 100f },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp)),
                    color = if (psychologyState.egoDepletionPct > 60) Color(0xFFFF5252) else NeuralAccent,
                    trackColor = NeuralDeepPurple
                )
            }
        }
    }
}

@Composable
private fun KeystrokeTouchDynamicsCard(psychologyState: BehavioralPsychologyState) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = NeuralSurface),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.08f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("⌨️", fontSize = 18.sp)
                Text(
                    text = "კლავიატურული & შეხების დინამიკა (Beiwe)",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                MetricChip("აკრეფის ტემპი", "${psychologyState.keystrokeCadenceWpm} WPM", NeuralCyanAccent, Modifier.weight(1f))
                MetricChip("Flight Time", "${psychologyState.flightTimeMs} ms", NeuralAccent, Modifier.weight(1f))
                MetricChip("Dwell Time", "${psychologyState.dwellDurationMs} ms", Color.White, Modifier.weight(1f))
                MetricChip("წაშლის წილი", "${psychologyState.backspaceCorrectionRatePct}%", NeuralGreenActive, Modifier.weight(1f))
            }

            Text(
                text = "შეხების წნევის მდგრადობა: ${psychologyState.touchPressureConsistencyPct}% • მოტორული აჟიტაციის ინდექსი: ${psychologyState.microTremorAgitationIndex} (ნორმა: <0.15)",
                fontSize = 11.sp,
                color = NeuralTextSecondary
            )
        }
    }
}

@Composable
private fun EmotionalValenceGsrCard(
    psychologyState: BehavioralPsychologyState,
    onTriggerGsrPeak: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = NeuralSurface),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.08f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("⚡", fontSize = 18.sp)
                    Text(
                        text = "ემოციური ვალენტობა & GSR / EDA",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
                Text(
                    text = "${psychologyState.galvanicSkinConductanceMicroSiemens} µS",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace,
                    color = Color(0xFFFFD54F)
                )
            }

            Text(
                text = psychologyState.innerAffectStatus,
                fontSize = 12.sp,
                color = NeuralGreenActive
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                MetricChip("Russell Valence", "+${String.format(java.util.Locale.US, "%.2f", psychologyState.emotionalValence)}", NeuralGreenActive, Modifier.weight(1f))
                MetricChip("Arousal", "${String.format(java.util.Locale.US, "%.2f", psychologyState.arousalLevel)}", NeuralCyanAccent, Modifier.weight(1f))
                MetricChip("Phasic Peaks", "${psychologyState.phasicSpikesPerMin}/წთ", Color(0xFFFFD54F), Modifier.weight(1f))
                MetricChip("FACS AU4", "${psychologyState.au4FrownTensionIndex}", NeuralAccent, Modifier.weight(1f))
            }

            OutlinedButton(
                onClick = onTriggerGsrPeak,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFFD54F).copy(alpha = 0.4f))
            ) {
                Text("⚡ სიმპათიკური აღგზნების ტრიგერი (GSR Surge)", fontSize = 11.sp, color = Color(0xFFFFD54F))
            }
        }
    }
}

@Composable
private fun CircadianSleepCard(psychologyState: BehavioralPsychologyState) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = NeuralSurface),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.08f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("🌙", fontSize = 18.sp)
                Text(
                    text = "ცირკადული რიტმი & ძილის არქიტექტურა",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            Text(
                text = "ფაზა: ${psychologyState.circadianPhase}",
                fontSize = 12.sp,
                color = NeuralAccent
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                MetricChip("დილის მზაობა", "${psychologyState.morningReadinessScore}/100", NeuralGreenActive, Modifier.weight(1.1f))
                MetricChip("REM ძილი", "${psychologyState.remSleepRatioPct}%", NeuralCyanAccent, Modifier.weight(1f))
                MetricChip("ღრმა ძილი", "${psychologyState.deepSleepRatioPct}%", NeuralDeepPurple, Modifier.weight(1f))
            }
        }
    }
}

@Composable
private fun WearablesSuiteCard(
    wearablesState: WearablesSuiteState,
    onToggleDevice: (String) -> Unit,
    onScanBle: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = NeuralSurface),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.08f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("📡", fontSize = 18.sp)
                    Text(
                        text = "Bluetooth (BLE) ნეირო & ბიო-სენსორები",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
                Button(
                    onClick = onScanBle,
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = NeuralDeepPurple),
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 8.dp, vertical = 2.dp)
                ) {
                    Text(if (wearablesState.isBleScanning) "სკანირება..." else "BLE სკანი", fontSize = 10.sp, color = NeuralAccent)
                }
            }

            wearablesState.devices.forEach { dev ->
                WearableDeviceRow(device = dev, onToggle = { onToggleDevice(dev.id) })
            }
        }
    }
}

@Composable
private fun WearableDeviceRow(
    device: WearableDeviceItem,
    onToggle: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(NeuralBackground)
            .border(1.dp, if (device.isConnected) NeuralGreenActive.copy(alpha = 0.3f) else Color.White.copy(alpha = 0.05f), RoundedCornerShape(12.dp))
            .clickable { onToggle() }
            .padding(10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
        ) {
            Text(device.iconEmoji, fontSize = 20.sp)
            Column {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(device.name, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    Text("${device.batteryPct}%", fontSize = 10.sp, color = NeuralTextSecondary)
                }
                Text(device.primaryMetric, fontSize = 11.sp, color = NeuralCyanAccent)
                Text(device.secondaryMetric, fontSize = 10.sp, color = NeuralTextSecondary)
            }
        }

        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(6.dp))
                .background(if (device.isConnected) NeuralGreenActive.copy(alpha = 0.15f) else Color.White.copy(alpha = 0.05f))
                .padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            Text(
                text = if (device.isConnected) "დაკავშირებულია" else "გათიშული",
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = if (device.isConnected) NeuralGreenActive else NeuralTextSecondary
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun LslScientificExportCard(
    lslState: LslExportState,
    onToggleLsl: () -> Unit,
    onSetLslFormat: (String) -> Unit,
    onExportLslData: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = NeuralSurface),
        border = androidx.compose.foundation.BorderStroke(1.dp, NeuralAccent.copy(alpha = 0.35f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("🔬", fontSize = 18.sp)
                    Text(
                        text = "Lab Streaming Layer (LSL) & ექსპორტი",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
                Button(
                    onClick = onToggleLsl,
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (lslState.isLslBroadcastActive) NeuralGreenActive else NeuralCardPurple
                    ),
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 8.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = if (lslState.isLslBroadcastActive) "LSL ON" else "LSL OFF",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (lslState.isLslBroadcastActive) Color.Black else Color.White
                    )
                }
            }

            Text(
                text = lslState.lastExportStatus,
                fontSize = 11.sp,
                color = NeuralCyanAccent
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                MetricChip("სიხშირე", "${lslState.samplingRateHz} Hz", NeuralAccent, Modifier.weight(1f))
                MetricChip("პაკეტები", "${lslState.packetsTransmitted}", Color.White, Modifier.weight(1f))
                MetricChip("თავსებადობა", "Python/NeuroKit2", NeuralGreenActive, Modifier.weight(1.3f))
            }

            // Format selector
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("ფორმატი:", fontSize = 11.sp, color = NeuralTextSecondary)
                lslState.exportFormats.forEach { fmt ->
                    val isSel = lslState.selectedFormat == fmt
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(if (isSel) NeuralAccent else NeuralDeepPurple)
                            .clickable { onSetLslFormat(fmt) }
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(fmt, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = if (isSel) Color.Black else Color.White)
                    }
                }
            }

            Button(
                onClick = onExportLslData,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = NeuralDeepPurple),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text("💾 მონაცემთა ექსპორტი (XDF / EDF ფაილი)", fontSize = 12.sp, color = NeuralAccent)
            }
        }
    }
}

@Composable
private fun MetricChip(
    title: String,
    value: String,
    accentColor: Color,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(NeuralBackground)
            .padding(vertical = 8.dp, horizontal = 6.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        Text(title, fontSize = 9.sp, color = NeuralTextSecondary)
        Text(value, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = accentColor)
    }
}

@Composable
private fun TestTabButton(
    title: String,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(if (isSelected) NeuralAccent else NeuralDeepPurple)
            .clickable { onClick() }
            .padding(vertical = 6.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = title,
            fontSize = 10.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            color = if (isSelected) Color.Black else Color.White
        )
    }
}
