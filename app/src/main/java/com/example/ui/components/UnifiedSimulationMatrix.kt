package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.AppIcons
import com.example.ui.theme.NeuralAccent
import com.example.ui.theme.NeuralCardPurple
import com.example.ui.theme.NeuralDeepPurple
import com.example.ui.theme.NeuralSurface
import com.example.ui.theme.NeuralTextPrimary
import com.example.ui.theme.NeuralTextSecondary
import com.example.viewmodel.ActionSandboxState
import com.example.viewmodel.CircadianEnvironment
import com.example.viewmodel.CognitiveDecisionTreeState
import com.example.viewmodel.EmotionalFrictionState
import com.example.viewmodel.GhostTypingState
import com.example.viewmodel.MentalImageryState
import com.example.viewmodel.MicroHesitationMetrics
import com.example.viewmodel.NeuroEntrainmentState
import com.example.viewmodel.NeuroFatigueState
import com.example.viewmodel.PreErrorDetectionState
import com.example.viewmodel.SemanticMindGraphState
import com.example.viewmodel.StreamCalibrationWeights
import com.example.viewmodel.SubvocalSpeechState
import com.example.viewmodel.ThoughtTimelineState
import com.example.viewmodel.TimeHorizonPredictions
import com.example.viewmodel.NeuroSyncUiState
import kotlin.math.cos
import kotlin.math.sin

data class UnifiedSimulationActions(
    val onRunUnifiedInference: () -> Unit = {},
    val onToggleContinuousThought: () -> Unit = {},
    val onSetUpdateInterval: (Int) -> Unit = {},
    val onInjectStimulus: (String) -> Unit = {},
    val onAppContextChanged: (String) -> Unit = {},
    val onTouchTap: (Float, Float) -> Unit = { _, _ -> },
    val onDecodeCustomThought: (String) -> Unit = {},
    val onApplyFeedback: (Boolean) -> Unit = {},
    val onToggleSandboxAction: (String) -> Unit = {},
    val onTriggerSubvocal: () -> Unit = {},
    val onSynthesizeImagery: (String) -> Unit = {},
    val onCheckPreError: () -> Unit = {},
    val onSelectGraphNode: (String) -> Unit = {},
    val onModulateMood: (Float, Float) -> Unit = { _, _ -> },
    val onSelectDecisionBranch: (String) -> Unit = {},
    val onAcceptGhostTyping: () -> Unit = {},
    val onCycleGhostSuggestion: () -> Unit = {},
    val onRefreshFatigue: () -> Unit = {},
    val onSearchThoughtHistory: (String) -> Unit = {},
    val onToggleEntrainment: () -> Unit = {},
    val onSetEntrainmentMode: (String) -> Unit = {},
    val onToggleCamera: () -> Unit = {},
    val onToggleEarbuds: () -> Unit = {},
    val onRecalibrateEarbuds: () -> Unit = {},
    val onToggleWordDecoding: () -> Unit = {},
    val onSetWordDecoderCategory: (String) -> Unit = {},
    val onInjectWordDecoderItem: (String, String) -> Unit = { _, _ -> },
    val onClearWordDecoderSentence: () -> Unit = {},
    val onCycleNextWordDecoderItem: () -> Unit = {},
    val onSelectActiveSubject: (String) -> Unit = {},
    val onToggleTargetLock: () -> Unit = {},
    val onToggleContaminationShield: () -> Unit = {},
    val onToggleAutoSwitch: () -> Unit = {},
    val onSimulateDetectedChange: (String) -> Unit = {},
    val onAddNewSubject: (String, String) -> Unit = { _, _ -> },
    val onTogglePreMotorPredictor: () -> Unit = {},
    val onApplyBranchPrediction: (String) -> Unit = {},
    val onRegeneratePredictionBranches: () -> Unit = {},
    val onToggleMarkovContext: () -> Unit = {},
    val onToggleGazeDwell: () -> Unit = {},
    val onToggleBilingual: () -> Unit = {},
    val onLearnMarkovPair: (String, String) -> Unit = { _, _ -> },
    val onCycleScreenContext: () -> Unit = {},
    val onToggleHrvCompensation: () -> Unit = {},
    val onTogglePhoneticSnap: () -> Unit = {},
    val onToggleMicroSaccade: () -> Unit = {},
    val onToggleNeuroGrammar: () -> Unit = {},
    val onToggleEnergyPreserver: () -> Unit = {},
    val onTogglePhonemeCompression: () -> Unit = {},
    val onToggle3DNeuroSpatial: () -> Unit = {},
    val onToggleAffectiveTone: () -> Unit = {},
    val onCycleAffectiveTone: () -> Unit = {},
    val onToggleUnifiedEngine: () -> Unit = {},
    val onSynthesizeUnifiedThought: () -> Unit = {},
    val onSimulateBioStress: () -> Unit = {},
    val onUpdateWeights: (Float, Float, Float, Float) -> Unit = { _, _, _, _ -> }
)

@Composable
fun UnifiedSimulationMatrix(
    uiState: NeuroSyncUiState,
    actions: UnifiedSimulationActions,
    modifier: Modifier = Modifier
) {
    val isSyncing = uiState.isSyncing
    val matchPercentage = uiState.matchPercentage
    val statusText = uiState.statusText
    val alphaBandHz = uiState.alphaBandHz
    val betaBandHz = uiState.betaBandHz
    val thetaBandHz = uiState.thetaBandHz
    val gammaBandHz = uiState.gammaBandHz
    val cognitiveLoadPct = uiState.thoughtCognitiveLoadPct
    val subconsciousFocusLevel = uiState.subconsciousFocusLevel
    val touchTapsCount = uiState.touchTapsCount
    val lastTouchCoords = uiState.lastTouchCoords
    val audioDb = uiState.audioDb
    val speakerOutputDb = uiState.speakerOutputDb
    val cameraGazeX = uiState.cameraGazeX
    val cameraGazeY = uiState.cameraGazeY
    val motionTremor = uiState.motionTremor
    val heartRateBpm = uiState.heartRateBpm
    val activeAppContext = uiState.activeAppContext
    val currentPredictionTitle = uiState.currentPredictionTitle
    val currentPredictionText = uiState.currentPredictionText
    val currentActionPlan = uiState.currentActionPlan
    val timeHorizons = uiState.timeHorizons
    val hesitationMetrics = uiState.hesitationMetrics
    val circadian = uiState.circadian
    val calibrationWeights = uiState.calibrationWeights
    val sandboxActions = uiState.sandboxActions
    val subvocalSpeech = uiState.subvocalSpeech
    val mentalImagery = uiState.mentalImagery
    val preErrorState = uiState.preErrorState
    val mindGraph = uiState.mindGraph
    val emotionalFriction = uiState.emotionalFriction
    val decisionTree = uiState.decisionTree
    val ghostTyping = uiState.ghostTyping
    val neuroFatigue = uiState.neuroFatigue
    val thoughtTimeline = uiState.thoughtTimeline
    val entrainment = uiState.entrainment
    val realSensors = uiState.realSensors
    val cameraGaze = uiState.cameraGaze
    val earbudSensor = uiState.earbudSensor
    val wordDecoder = uiState.wordDecoder
    val enhancedMetrics = uiState.enhancedMetrics
    val isContinuousThoughtActive = uiState.isContinuousThoughtStreamActive
    val lastThoughtUpdated = uiState.lastThoughtUpdatedTimestamp
    val isGeneratingPrediction = uiState.isGeneratingPrediction

    val onRunUnifiedInference = actions.onRunUnifiedInference
    val onToggleContinuousThought = actions.onToggleContinuousThought
    val onSetUpdateInterval = actions.onSetUpdateInterval
    val onInjectStimulus = actions.onInjectStimulus
    val onAppContextChanged = actions.onAppContextChanged
    val onTouchTap = actions.onTouchTap
    val onDecodeCustomThought = actions.onDecodeCustomThought
    val onApplyFeedback = actions.onApplyFeedback
    val onToggleSandboxAction = actions.onToggleSandboxAction
    val onTriggerSubvocal = actions.onTriggerSubvocal
    val onSynthesizeImagery = actions.onSynthesizeImagery
    val onCheckPreError = actions.onCheckPreError
    val onSelectGraphNode = actions.onSelectGraphNode
    val onModulateMood = actions.onModulateMood
    val onSelectDecisionBranch = actions.onSelectDecisionBranch
    val onAcceptGhostTyping = actions.onAcceptGhostTyping
    val onCycleGhostSuggestion = actions.onCycleGhostSuggestion
    val onRefreshFatigue = actions.onRefreshFatigue
    val onSearchThoughtHistory = actions.onSearchThoughtHistory
    val onToggleEntrainment = actions.onToggleEntrainment
    val onSetEntrainmentMode = actions.onSetEntrainmentMode
    val onToggleCamera = actions.onToggleCamera
    val onToggleEarbuds = actions.onToggleEarbuds
    val onRecalibrateEarbuds = actions.onRecalibrateEarbuds
    val onToggleWordDecoding = actions.onToggleWordDecoding
    val onSetWordDecoderCategory = actions.onSetWordDecoderCategory
    val onInjectWordDecoderItem = actions.onInjectWordDecoderItem
    val onClearWordDecoderSentence = actions.onClearWordDecoderSentence
    val onCycleNextWordDecoderItem = actions.onCycleNextWordDecoderItem
    val onSelectActiveSubject = actions.onSelectActiveSubject
    val onToggleTargetLock = actions.onToggleTargetLock
    val onToggleContaminationShield = actions.onToggleContaminationShield
    val onToggleAutoSwitch = actions.onToggleAutoSwitch
    val onSimulateDetectedChange = actions.onSimulateDetectedChange
    val onAddNewSubject = actions.onAddNewSubject
    val onTogglePreMotorPredictor = actions.onTogglePreMotorPredictor
    val onApplyBranchPrediction = actions.onApplyBranchPrediction
    val onRegeneratePredictionBranches = actions.onRegeneratePredictionBranches
    val onToggleMarkovContext = actions.onToggleMarkovContext
    val onToggleGazeDwell = actions.onToggleGazeDwell
    val onToggleBilingual = actions.onToggleBilingual
    val onLearnMarkovPair = actions.onLearnMarkovPair
    val onCycleScreenContext = actions.onCycleScreenContext
    val onToggleHrvCompensation = actions.onToggleHrvCompensation
    val onTogglePhoneticSnap = actions.onTogglePhoneticSnap
    val onToggleMicroSaccade = actions.onToggleMicroSaccade
    val onToggleNeuroGrammar = actions.onToggleNeuroGrammar
    val onToggleEnergyPreserver = actions.onToggleEnergyPreserver
    val onTogglePhonemeCompression = actions.onTogglePhonemeCompression
    val onToggle3DNeuroSpatial = actions.onToggle3DNeuroSpatial
    val onToggleAffectiveTone = actions.onToggleAffectiveTone
    val onCycleAffectiveTone = actions.onCycleAffectiveTone
    val onToggleUnifiedEngine = actions.onToggleUnifiedEngine
    val onSynthesizeUnifiedThought = actions.onSynthesizeUnifiedThought
    val onSimulateBioStress = actions.onSimulateBioStress
    val onUpdateWeights = actions.onUpdateWeights
    val subjectState = uiState.subjectRecognition
    val predictionState = uiState.wordPrediction

    var customThoughtPrompt by remember { mutableStateOf("") }
    var isActionExecuted by remember { mutableStateOf(false) }
    var selectedFeedback by remember { mutableStateOf<Boolean?>(null) }
    var activeHorizonTab by remember { mutableStateOf("30s") }

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Master Unified Simulation Title Card
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp))
                .background(NeuralSurface)
                .border(1.dp, NeuralAccent.copy(alpha = 0.3f), RoundedCornerShape(24.dp))
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .background(NeuralAccent),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = AppIcons.Psychology,
                            contentDescription = "Unified Core",
                            tint = NeuralDeepPurple,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    Column {
                        Text(
                            text = "UNIFIED SIMULATION MATRIX",
                            color = NeuralAccent,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                        Text(
                            text = "11 Multimodal & BCI Cognitive Streams",
                            color = NeuralTextSecondary,
                            fontSize = 11.sp
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (isSyncing) NeuralAccent.copy(alpha = 0.15f) else Color.Red.copy(alpha = 0.15f))
                        .border(1.dp, if (isSyncing) NeuralAccent else Color.Red, RoundedCornerShape(12.dp))
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = if (isSyncing) "● LIVE SYNC" else "○ IDLE",
                        color = if (isSyncing) NeuralAccent else Color.Red,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        // =========================================================================
        // 🌟 ULTRA-PROMINENT LIVE DECODED THOUGHT MONITOR (რას ფიქრობთ ახლა / LIVE STREAM)
        // =========================================================================
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(26.dp))
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF1E1038),
                            NeuralCardPurple,
                            Color(0xFF0F071D)
                        )
                    )
                )
                .border(
                    width = 2.dp,
                    brush = Brush.horizontalGradient(
                        listOf(
                            NeuralAccent,
                            Color(0xFF00E5FF),
                            Color(0xFF9D4EDD),
                            NeuralAccent
                        )
                    ),
                    shape = RoundedCornerShape(26.dp)
                )
                .padding(20.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                // Header badge with pulsing live dot & stream status
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(10.dp)
                                .clip(CircleShape)
                                .background(if (isContinuousThoughtActive) NeuralAccent else Color.Gray)
                        )
                        Text(
                            text = "დეკოდირებული აზრები • LIVE STREAM",
                            color = NeuralAccent,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = 1.sp
                        )
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .background(NeuralAccent.copy(alpha = 0.2f))
                                .clickable { onToggleContinuousThought() }
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = if (isContinuousThoughtActive) "⚡ განახლება: ყოველ 4 წამში" else "⏸ შეჩერებულია",
                                color = NeuralAccent,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .background(Color(0xFF00E5FF).copy(alpha = 0.18f))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "${String.format(java.util.Locale.US, "%.1f", matchPercentage)}%",
                                color = Color(0xFF00E5FF),
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                // Primary Decoded Thought Title Banner with Smooth Crossfade Animation
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(18.dp))
                        .background(NeuralDeepPurple.copy(alpha = 0.85f))
                        .border(1.dp, Color(0xFF00E5FF).copy(alpha = 0.35f), RoundedCornerShape(18.dp))
                        .padding(16.dp)
                ) {
                    androidx.compose.animation.Crossfade(
                        targetState = currentPredictionTitle to currentPredictionText,
                        animationSpec = tween(600),
                        label = "thought_transition"
                    ) { (title, text) ->
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "🧠 მიმდინარე მენტალური განზრახვა & აზრი:",
                                    color = Color(0xFF00E5FF),
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "🕒 $lastThoughtUpdated",
                                    color = NeuralTextSecondary,
                                    fontSize = 10.sp
                                )
                            }
                            Text(
                                text = title,
                                color = Color.White,
                                fontSize = 17.sp,
                                fontWeight = FontWeight.ExtraBold,
                                lineHeight = 23.sp
                            )
                            Text(
                                text = text,
                                color = NeuralTextPrimary,
                                fontSize = 13.sp,
                                lineHeight = 19.sp
                            )
                        }
                    }
                }

                // Enhanced Neural Biomarker Highlights (Pupil mm, Fixation zone, Subvocal VPU)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFF00E5FF).copy(alpha = 0.12f))
                            .border(1.dp, Color(0xFF00E5FF).copy(alpha = 0.3f), RoundedCornerShape(12.dp))
                            .padding(10.dp)
                    ) {
                        Column {
                            Text("👁 გუგა & ფოკუსი", color = Color(0xFF00E5FF), fontSize = 9.sp, fontWeight = FontWeight.Bold)
                            Text("${String.format(java.util.Locale.US, "%.2f", enhancedMetrics.pupilDiameterMm)}მმ • ${cameraGaze.gazeConfidencePct}%", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.SemiBold, maxLines = 1)
                        }
                    }

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFF9D4EDD).copy(alpha = 0.12f))
                            .border(1.dp, Color(0xFF9D4EDD).copy(alpha = 0.3f), RoundedCornerShape(12.dp))
                            .padding(10.dp)
                    ) {
                        Column {
                            Text("🦴 Buds 2 VPU ხმა", color = Color(0xFF9D4EDD), fontSize = 9.sp, fontWeight = FontWeight.Bold)
                            Text("${String.format(java.util.Locale.US, "%.1f", earbudSensor.vpuBoneConductionHz)} Hz ძვლის ვიბრაცია", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.SemiBold, maxLines = 1)
                        }
                    }

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(12.dp))
                            .background(NeuralAccent.copy(alpha = 0.12f))
                            .border(1.dp, NeuralAccent.copy(alpha = 0.3f), RoundedCornerShape(12.dp))
                            .padding(10.dp)
                    ) {
                        Column {
                            Text("⚡ ERN შეცდომის შეგრძნება", color = NeuralAccent, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                            Text(if (preErrorState.isImminentError) "⚠️ ERN -180ms" else "✓ სუფთა ნაკადი", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.SemiBold, maxLines = 1)
                        }
                    }
                }

                // Quick Thought Rate Selector Controls
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "განახლების სიჩქარე:",
                        color = NeuralTextSecondary,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium
                    )

                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        listOf(2 to "2წმ", 4 to "4წმ", 6 to "6წმ").forEach { (sec, label) ->
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(NeuralDeepPurple)
                                    .clickable { onSetUpdateInterval(sec) }
                                    .border(1.dp, Color(0xFF00E5FF).copy(alpha = 0.4f), RoundedCornerShape(8.dp))
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = label,
                                    color = Color.White,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }

        // Live Synaptic Simulation Convergence Canvas
        UnifiedMatrixVisualizer(
            isSyncing = isSyncing,
            matchPercentage = matchPercentage,
            alphaHz = alphaBandHz,
            touchCount = touchTapsCount,
            audioDb = audioDb,
            gazeX = cameraGazeX,
            heartRate = heartRateBpm,
            onCoreClicked = onRunUnifiedInference
        )

        // Stream Category Filter Selector
        var selectedCategory by remember { mutableStateOf("WORDS") }
        val categoryChips = listOf(
            "WORDS" to "🗣️ სიტყვების გამოცნობა",
            "ALL" to "⚡ ყველა (12 ნაკადი)",
            "BCI" to "🧠 BCI & სუბვოკალი",
            "SENSORS" to "📡 სენსორები & კამერა",
            "COGNITION" to "🧬 სემანტიკური გრაფი",
            "ACTIONS" to "🔮 პროგნოზები & SandBox"
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            categoryChips.forEach { (catKey, catLabel) ->
                val isSelected = selectedCategory == catKey
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (isSelected) NeuralAccent else NeuralSurface)
                        .border(
                            1.dp,
                            if (isSelected) NeuralAccent else Color.White.copy(alpha = 0.12f),
                            RoundedCornerShape(12.dp)
                        )
                        .clickable { selectedCategory = catKey }
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = catLabel,
                        color = if (isSelected) NeuralDeepPurple else NeuralTextPrimary,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        if (selectedCategory == "ALL" || selectedCategory == "WORDS" || selectedCategory == "BCI") {
            // -------------------------------------------------------------
            // 0. Direct Word Decoder & Subvocal Lexicon Engine
            // -------------------------------------------------------------
            DirectWordDecoderView(
                wordDecoderState = wordDecoder,
                subjectState = subjectState,
                predictionState = predictionState,
                onToggleLiveDecoding = onToggleWordDecoding,
                onSetCategory = onSetWordDecoderCategory,
                onInjectWord = onInjectWordDecoderItem,
                onClearSentence = onClearWordDecoderSentence,
                onCycleNextWord = onCycleNextWordDecoderItem,
                onSelectActiveSubject = onSelectActiveSubject,
                onToggleTargetLock = onToggleTargetLock,
                onToggleContaminationShield = onToggleContaminationShield,
                onToggleAutoSwitch = onToggleAutoSwitch,
                onSimulateDetectedChange = onSimulateDetectedChange,
                onAddNewSubject = onAddNewSubject,
                onTogglePreMotorPredictor = onTogglePreMotorPredictor,
                onApplyBranchPrediction = onApplyBranchPrediction,
                onRegeneratePredictionBranches = onRegeneratePredictionBranches,
                onToggleMarkovContext = onToggleMarkovContext,
                onToggleGazeDwell = onToggleGazeDwell,
                onToggleBilingual = onToggleBilingual,
                onLearnMarkovPair = onLearnMarkovPair,
                onCycleScreenContext = onCycleScreenContext,
                onToggleHrvCompensation = onToggleHrvCompensation,
                onTogglePhoneticSnap = onTogglePhoneticSnap,
                onToggleMicroSaccade = onToggleMicroSaccade,
                onToggleNeuroGrammar = onToggleNeuroGrammar,
                onToggleEnergyPreserver = onToggleEnergyPreserver,
                onTogglePhonemeCompression = onTogglePhonemeCompression,
                onToggle3DNeuroSpatial = onToggle3DNeuroSpatial,
                onToggleAffectiveTone = onToggleAffectiveTone,
                onCycleAffectiveTone = onCycleAffectiveTone,
                onToggleUnifiedEngine = onToggleUnifiedEngine,
                onSynthesizeUnifiedThought = onSynthesizeUnifiedThought,
                onSimulateBioStress = onSimulateBioStress,
                onUpdateWeights = onUpdateWeights
            )
        }

        if (selectedCategory == "ALL" || selectedCategory == "BCI") {
            // -------------------------------------------------------------
            // 1. Silent Subvocal Speech Stream Card (Inner Monologue Decoder)
            // -------------------------------------------------------------
            SubvocalSpeechCard(
                subvocal = subvocalSpeech,
                onTriggerNewWord = onTriggerSubvocal
            )

            // -------------------------------------------------------------
            // 1.1 Georgian Subvocal Phoneme Matrix & EMG Resonance
            // -------------------------------------------------------------
            GeorgianPhonemeMatrixCard()

            // -------------------------------------------------------------
            // 1.2 Samsung Galaxy Buds 2 & Ear-EEG Telemetry Card
            // -------------------------------------------------------------
            GalaxyBuds2EarEegCard(
                earbud = earbudSensor,
                onRecalibrate = onRecalibrateEarbuds,
                onToggle = onToggleEarbuds
            )

            // -------------------------------------------------------------
            // 2. Pre-Error ERN (Error-Related Negativity) Wave Detector Card
            // -------------------------------------------------------------
            PreErrorErnDetectorCard(
                preError = preErrorState,
                onSimulateCheck = onCheckPreError
            )
        }

        if (selectedCategory == "ALL" || selectedCategory == "COGNITION") {
            // -------------------------------------------------------------
            // 3. Semantic Mind Graph Card (Associative Thought Map)
            // -------------------------------------------------------------
            SemanticMindGraphCard(
                mindGraph = mindGraph,
                onSelectNode = onSelectGraphNode
            )

            // -------------------------------------------------------------
            // 4. Mental Imagery Synthesis Card (Mind's Eye Visualizer)
            // -------------------------------------------------------------
            MentalImageryCard(
                imagery = mentalImagery,
                onSynthesize = onSynthesizeImagery
            )

            // -------------------------------------------------------------
            // 5. Emotional Resonance & Cognitive Friction Vector Card
            // -------------------------------------------------------------
            EmotionalFrictionCard(
                friction = emotionalFriction,
                onModulate = onModulateMood
            )

            // -------------------------------------------------------------
            // 6. Branching Cognitive Decision Tree Card
            // -------------------------------------------------------------
            CognitiveDecisionTreeCard(
                decisionTree = decisionTree,
                onSelectBranch = onSelectDecisionBranch
            )

            // -------------------------------------------------------------
            // 7. Subconscious Ghost-Typing Engine Card
            // -------------------------------------------------------------
            GhostTypingEngineCard(
                ghostTyping = ghostTyping,
                onAccept = onAcceptGhostTyping,
                onCycle = onCycleGhostSuggestion
            )

            // -------------------------------------------------------------
            // 9. Thought Stream Timeline & Semantic Search History Card
            // -------------------------------------------------------------
            ThoughtStreamTimelineCard(
                timeline = thoughtTimeline,
                onSearch = onSearchThoughtHistory
            )
        }

        if (selectedCategory == "ALL" || selectedCategory == "BCI") {
            // -------------------------------------------------------------
            // 8. Neuro-Fatigue & Clarity Spectrum Card (Brain Fog Early Warning)
            // -------------------------------------------------------------
            NeuroFatigueClarityCard(
                fatigue = neuroFatigue,
                onRefresh = onRefreshFatigue
            )

            // -------------------------------------------------------------
            // 10. Audio Neuro-Entrainment Beats Generator Card
            // -------------------------------------------------------------
            AudioNeuroEntrainmentCard(
                entrainment = entrainment,
                onTogglePlay = onToggleEntrainment,
                onSelectMode = onSetEntrainmentMode
            )
        }

        if (selectedCategory == "ALL" || selectedCategory == "SENSORS") {
            // -------------------------------------------------------------
            // 11. LIVE HARDWARE SENSORS & ACCELEROMETER CARD (Real Tremor & Stability)
            // -------------------------------------------------------------
            RealHardwareSensorsCard(
                sensors = realSensors
            )

            // -------------------------------------------------------------
            // 12. LIVE CAMERA-X GAZE & FACIAL RADIANCE HUD (Real Gaze Tracking)
            // -------------------------------------------------------------
            RealCameraGazeHUDCard(
                gaze = cameraGaze,
                onToggleCamera = onToggleCamera
            )

            // Micro-Hesitation & Motor Latency Analysis Card
            MicroHesitationCard(
                metrics = hesitationMetrics,
                touchCount = touchTapsCount,
                lastTouchCoords = lastTouchCoords
            )

            // Circadian & Environmental Dynamics Card
            CircadianDynamicsCard(
                circadian = circadian,
                heartRate = heartRateBpm,
                activeAppContext = activeAppContext
            )
        }

        if (selectedCategory == "ALL" || selectedCategory == "ACTIONS") {
            // Time Horizon Predictions Card (+30s / +5m / +30m)
            TimeHorizonsPredictionCard(
                horizons = timeHorizons,
                activeTab = activeHorizonTab,
                onTabSelected = { activeHorizonTab = it }
            )

        // Master Multimodal Inference Action Card (PREDICT Section)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(26.dp))
                .background(
                    Brush.verticalGradient(
                        listOf(
                            Color(0xFF261245),
                            NeuralCardPurple,
                            Color(0xFF130924)
                        )
                    )
                )
                .border(
                    width = 2.dp,
                    brush = Brush.horizontalGradient(
                        listOf(
                            Color(0xFF00E5FF),
                            NeuralAccent,
                            Color(0xFFFF52A2),
                            Color(0xFF00E5FF)
                        )
                    ),
                    shape = RoundedCornerShape(26.dp)
                )
                .padding(18.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "🧠 მულტიმოდალური ნეირო-პროგნოზი & განზრახვა",
                            color = Color(0xFF00E5FF),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = 0.5.sp
                        )
                        Text(
                            text = currentPredictionTitle,
                            color = Color.White,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .background(
                                Brush.horizontalGradient(
                                    listOf(
                                        NeuralAccent,
                                        Color(0xFF00E5FF)
                                    )
                                )
                            )
                            .clickable { onRunUnifiedInference() }
                            .padding(horizontal = 16.dp, vertical = 10.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            if (isGeneratingPrediction) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(16.dp),
                                    color = NeuralDeepPurple,
                                    strokeWidth = 2.dp
                                )
                            } else {
                                Icon(
                                    imageVector = AppIcons.AutoAwesome,
                                    contentDescription = "პროგნოზირება",
                                    tint = NeuralDeepPurple,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                            Text(
                                text = if (isGeneratingPrediction) "ანალიზი..." else "⚡ პროგნოზი",
                                color = NeuralDeepPurple,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.ExtraBold
                            )
                        }
                    }
                }

                // Intent Explanation and Synthesis
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(NeuralDeepPurple.copy(alpha = 0.6f))
                        .padding(12.dp)
                ) {
                    Text(
                        text = currentPredictionText,
                        color = Color.White,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        lineHeight = 20.sp
                    )
                }

                // Multimodal Adaptive Calibration Weights Display
                CalibrationWeightsSection(
                    weights = calibrationWeights
                )

                // Proactive Action Plan
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(NeuralDeepPurple.copy(alpha = 0.85f))
                        .border(1.dp, Color(0xFF00E5FF).copy(alpha = 0.25f), RoundedCornerShape(16.dp))
                        .padding(12.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "ავტომატური პროაქტიული მოქმედებების გეგმა",
                                color = NeuralAccent,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "სიზუსტე: ${String.format(java.util.Locale.US, "%.1f", matchPercentage)}%",
                                color = Color(0xFF00FF66),
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Text(
                            text = currentActionPlan,
                            color = NeuralTextPrimary,
                            fontSize = 12.sp,
                            lineHeight = 18.sp
                        )

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (isActionExecuted) NeuralAccent.copy(alpha = 0.2f) else NeuralAccent)
                                .clickable { isActionExecuted = true }
                                .padding(vertical = 10.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = if (isActionExecuted) "✓ პროაქტიული გეგმა შესრულებულია სისტემაში" else "⚡ პროაქტიული მოქმედების გეგმის შესრულება",
                                color = if (isActionExecuted) NeuralAccent else NeuralDeepPurple,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                // Real-time Adaptive Reinforcement Calibration Feedback
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(10.dp))
                            .background(if (selectedFeedback == true) NeuralAccent else Color.White.copy(alpha = 0.08f))
                            .clickable {
                                selectedFeedback = true
                                onApplyFeedback(true)
                            }
                            .padding(vertical = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "✓ ზუსტია (+განმტკიცება)",
                            color = if (selectedFeedback == true) NeuralDeepPurple else NeuralTextPrimary,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(10.dp))
                            .background(if (selectedFeedback == false) NeuralAccent else Color.White.copy(alpha = 0.08f))
                            .clickable {
                                selectedFeedback = false
                                onApplyFeedback(false)
                            }
                            .padding(vertical = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "⚡ გადაკალიბრება",
                            color = if (selectedFeedback == false) NeuralDeepPurple else NeuralTextPrimary,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }

        // Interactive Proactive OS Sandbox (Action Sandbox)
            ProactiveActionSandboxCard(
                sandbox = sandboxActions,
                onToggle = onToggleSandboxAction
            )

            // Direct Subconscious Thought Decoder Input
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(NeuralSurface)
                    .border(1.dp, NeuralAccent.copy(alpha = 0.25f), RoundedCornerShape(20.dp))
                    .padding(14.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = "DIRECT MIND THOUGHT INJECTION & DECODER",
                        color = NeuralAccent,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            value = customThoughtPrompt,
                            onValueChange = { customThoughtPrompt = it },
                            placeholder = { Text("შეიყვანეთ აზრი (e.g. კოდის რეფაქტორინგი)...", color = NeuralTextSecondary, fontSize = 11.sp) },
                            modifier = Modifier.weight(1f),
                            singleLine = true,
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = NeuralDeepPurple,
                                unfocusedContainerColor = NeuralDeepPurple,
                                focusedTextColor = NeuralTextPrimary,
                                unfocusedTextColor = NeuralTextPrimary,
                                focusedIndicatorColor = NeuralAccent,
                                unfocusedIndicatorColor = Color.White.copy(alpha = 0.1f)
                            )
                        )

                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(NeuralAccent)
                                .clickable {
                                    if (customThoughtPrompt.isNotBlank()) {
                                        onDecodeCustomThought(customThoughtPrompt)
                                    }
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = AppIcons.SendIcon,
                                contentDescription = "Decode",
                                tint = NeuralDeepPurple,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }

                    // Quick Thought Presets
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        PresetThoughtChip("⚡ UI ოპტიმიზაცია", "UI Optimization and Refactor", onDecodeCustomThought, Modifier.weight(1f))
                        PresetThoughtChip("☕ შესვენება", "Take a short break and relax", onDecodeCustomThought, Modifier.weight(1f))
                        PresetThoughtChip("🧠 ღრმა ფოკუსი", "Deep Focus Architecture Session", onDecodeCustomThought, Modifier.weight(1f))
                    }
                }
            }

            // Multimodal Stream Controls Matrix
            StreamControlMatrixSection(
                alphaBandHz = alphaBandHz,
                betaBandHz = betaBandHz,
                thetaBandHz = thetaBandHz,
                gammaBandHz = gammaBandHz,
                cognitiveLoadPct = cognitiveLoadPct,
                subconsciousFocusLevel = subconsciousFocusLevel,
                touchTapsCount = touchTapsCount,
                lastTouchCoords = lastTouchCoords,
                audioDb = audioDb,
                speakerOutputDb = speakerOutputDb,
                cameraGazeX = cameraGazeX,
                cameraGazeY = cameraGazeY,
                motionTremor = motionTremor,
                heartRateBpm = heartRateBpm,
                activeAppContext = activeAppContext,
                onInjectStimulus = onInjectStimulus,
                onAppContextChanged = onAppContextChanged,
                onTouchTap = onTouchTap
            )
        }
    }
}

// -------------------------------------------------------------------------------------------------
// 1. Silent Subvocal Speech Stream Composable
// -------------------------------------------------------------------------------------------------
@Composable
private fun SubvocalSpeechCard(
    subvocal: SubvocalSpeechState,
    onTriggerNewWord: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(NeuralSurface)
            .border(1.dp, Color(0xFF00E5FF).copy(alpha = 0.4f), RoundedCornerShape(20.dp))
            .padding(14.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Icon(imageVector = AppIcons.RecordVoiceOver, contentDescription = "სუბვოკალური", tint = Color(0xFF00E5FF), modifier = Modifier.size(16.dp))
                    Text("სუბვოკალური შინაგანი ხმის ნაკადი", color = Color(0xFF00E5FF), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFF00E5FF).copy(alpha = 0.15f))
                        .clickable { onTriggerNewWord() }
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text("⚡ ფონემა", color = Color(0xFF00E5FF), fontSize = 9.sp, fontWeight = FontWeight.Bold)
                }
            }

            Text(
                text = "დეკოდირებული შინაგანი ხმა: \"${subvocal.decodedPhrase}\"",
                color = NeuralTextPrimary,
                fontSize = 12.sp,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.SemiBold
            )

            // Token-level live stream chips
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                subvocal.activeTokens.forEach { token ->
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(8.dp))
                            .background(NeuralDeepPurple)
                            .border(1.dp, Color(0xFF00E5FF).copy(alpha = token.probability), RoundedCornerShape(8.dp))
                            .padding(6.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(token.word.take(14), color = Color(0xFF00E5FF), fontSize = 9.sp, fontWeight = FontWeight.Bold, maxLines = 1)
                            Text("${(token.probability * 100).toInt()}% (${token.latencyOffsetMs}ms)", color = NeuralTextSecondary, fontSize = 8.sp)
                        }
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------------------------------------------
// 1.2 Samsung Galaxy Buds 2 & Ear-EEG Telemetry Composable
// -------------------------------------------------------------------------------------------------
@Composable
private fun GalaxyBuds2EarEegCard(
    earbud: com.example.viewmodel.EarbudSensorState,
    onRecalibrate: () -> Unit,
    onToggle: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(NeuralSurface)
            .border(1.dp, Color(0xFF9D4EDD).copy(alpha = 0.5f), RoundedCornerShape(20.dp))
            .padding(14.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Icon(
                        imageVector = AppIcons.Headphones,
                        contentDescription = "Galaxy Buds 2",
                        tint = Color(0xFF9D4EDD),
                        modifier = Modifier.size(20.dp)
                    )
                    Column {
                        Text(
                            text = "🎧 Samsung Galaxy Buds 2 & Ear-EEG",
                            color = Color(0xFF9D4EDD),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = if (earbud.isConnected) "BLE სინქრონიზებული • VPU + IMU ნაკადი" else "გათიშულია",
                            color = NeuralTextSecondary,
                            fontSize = 9.5.sp
                        )
                    }
                }

                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFF9D4EDD).copy(alpha = 0.15f))
                            .clickable { onRecalibrate() }
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text("⚡ რეკალიბრაცია", color = Color(0xFF9D4EDD), fontSize = 9.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            // Real-time telemetry grid: VPU, IMU, Ear Canal Occlusion, Simulated Bio-Potential
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(10.dp))
                        .background(NeuralDeepPurple)
                        .padding(8.dp)
                ) {
                    Column {
                        Text("VPU ძვლის ვიბრაცია", color = NeuralTextSecondary, fontSize = 8.sp)
                        Text(
                            "${String.format(java.util.Locale.US, "%.1f", earbud.vpuBoneConductionHz)} Hz",
                            color = Color(0xFF00E5FF),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(10.dp))
                        .background(NeuralDeepPurple)
                        .padding(8.dp)
                ) {
                    Column {
                        Text("თავის დახრა (IMU)", color = NeuralTextSecondary, fontSize = 8.sp)
                        Text(
                            "${String.format(java.util.Locale.US, "%.1f", earbud.headImuPitchDeg)}° Pitch",
                            color = Color(0xFF00FF66),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(10.dp))
                        .background(NeuralDeepPurple)
                        .padding(8.dp)
                ) {
                    Column {
                        Text("Ear-EEG სიგნალი", color = NeuralTextSecondary, fontSize = 8.sp)
                        Text(
                            "${String.format(java.util.Locale.US, "%.1f", earbud.earEegSimulatedMicrovolts)} μV",
                            color = Color(0xFFFF52A2),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                }
            }

            Text(
                text = "💡 Galaxy Buds 2 აფიქსირებს ყბის სუბვოკალურ მოძრაობებს (VPU), თავის დახრის კუთხეს და ყურის არხის აკუსტიკას, რაც აძლიერებს აზრების პროგნოზირების სიზუსტეს.",
                color = NeuralTextPrimary.copy(alpha = 0.85f),
                fontSize = 10.5.sp,
                lineHeight = 14.sp
            )
        }
    }
}

// -------------------------------------------------------------------------------------------------
// 2. Pre-Error ERN Wave Detector Composable
// -------------------------------------------------------------------------------------------------
@Composable
private fun PreErrorErnDetectorCard(
    preError: PreErrorDetectionState,
    onSimulateCheck: () -> Unit
) {
    val isAlert = preError.isImminentError || preError.preErrorProbabilityPct > 50
    val borderColor = if (isAlert) Color(0xFFFF5252) else Color(0xFF00FF66)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(NeuralSurface)
            .border(1.dp, borderColor.copy(alpha = 0.45f), RoundedCornerShape(20.dp))
            .padding(14.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Icon(
                        imageVector = if (isAlert) AppIcons.WarningAmber else AppIcons.CheckCircle,
                        contentDescription = "ERN",
                        tint = borderColor,
                        modifier = Modifier.size(16.dp)
                    )
                    Text("შეცდომის წინასწარი ERN ტალღის დეტექტორი", color = borderColor, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(borderColor.copy(alpha = 0.15f))
                        .clickable { onSimulateCheck() }
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text("სკანირება (-300ms)", color = borderColor, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                }
            }

            Text(
                text = preError.suggestedIntervention,
                color = NeuralTextPrimary,
                fontSize = 12.sp,
                lineHeight = 16.sp
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                HesitationMetricPill("ERN პიკი (μV)", "${String.format(java.util.Locale.US, "%.1f", preError.ernWaveMagnitudeUv)} μV", Modifier.weight(1f))
                HesitationMetricPill("შეცდომის რისკი", "${preError.preErrorProbabilityPct}%", Modifier.weight(1f))
                HesitationMetricPill("ინტერვალი", "-${preError.timeToImpactMs} ms", Modifier.weight(1f))
                HesitationMetricPill("პრევენცია", "${preError.preventedMistakesCount}", Modifier.weight(1f))
            }
        }
    }
}

// -------------------------------------------------------------------------------------------------
// 3. Semantic Mind Graph Composable
// -------------------------------------------------------------------------------------------------
@Composable
private fun SemanticMindGraphCard(
    mindGraph: SemanticMindGraphState,
    onSelectNode: (String) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(NeuralSurface)
            .border(1.dp, Color(0xFF9D4EDD).copy(alpha = 0.4f), RoundedCornerShape(20.dp))
            .padding(14.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Icon(imageVector = AppIcons.Hub, contentDescription = "ასოციაციური გრაფი", tint = Color(0xFF9D4EDD), modifier = Modifier.size(16.dp))
                    Text("სემანტიკური აზროვნების ასოციაციური გრაფი", color = Color(0xFF9D4EDD), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
                Text("ცენტრი: ${mindGraph.centralTopic}", color = NeuralTextSecondary, fontSize = 10.sp)
            }

            // Interactive Node Graph View
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(NeuralDeepPurple)
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val w = size.width
                    val h = size.height
                    val center = Offset(w / 2f, h / 2f)

                    // Draw connecting synaptic edges
                    mindGraph.nodes.forEach { node ->
                        val nodeOffset = Offset(node.xOffset * w, node.yOffset * h)
                        drawLine(
                            color = Color(0xFF9D4EDD).copy(alpha = node.weight * 0.6f),
                            start = center,
                            end = nodeOffset,
                            strokeWidth = 2f,
                            pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
                        )
                    }

                    // Center Hub Node
                    drawCircle(
                        color = Color(0xFF9D4EDD),
                        radius = 16f,
                        center = center
                    )
                }

                // Interactive Overlay Nodes
                mindGraph.nodes.forEach { node ->
                    val isSelected = node.id == mindGraph.activeNodeId
                    Box(
                        modifier = Modifier
                            .align(
                                when {
                                    node.yOffset < 0.35f -> Alignment.TopCenter
                                    node.xOffset < 0.3f && node.yOffset < 0.7f -> Alignment.CenterStart
                                    node.xOffset > 0.7f && node.yOffset < 0.7f -> Alignment.CenterEnd
                                    node.xOffset < 0.5f -> Alignment.BottomStart
                                    else -> Alignment.BottomEnd
                                }
                            )
                            .padding(8.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (isSelected) Color(0xFF9D4EDD) else NeuralSurface)
                            .border(1.dp, Color(0xFF9D4EDD), RoundedCornerShape(8.dp))
                            .clickable { onSelectNode(node.id) }
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "${node.label} (${(node.weight * 100).toInt()}%)",
                            color = if (isSelected) Color.White else NeuralTextPrimary,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------------------------------------------
// 4. Mental Imagery Synthesis Composable
// -------------------------------------------------------------------------------------------------
@Composable
private fun MentalImageryCard(
    imagery: MentalImageryState,
    onSynthesize: (String) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(NeuralSurface)
            .border(1.dp, Color(0xFFFFD166).copy(alpha = 0.4f), RoundedCornerShape(20.dp))
            .padding(14.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Icon(imageVector = AppIcons.Image, contentDescription = "ვიზუალიზაცია", tint = Color(0xFFFFD166), modifier = Modifier.size(16.dp))
                    Text("მენტალური ვიზუალიზაციის რეკონსტრუქცია", color = Color(0xFFFFD166), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFFFFD166).copy(alpha = 0.15f))
                        .clickable { onSynthesize("") }
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(if (imagery.isSynthesizing) "გენერაცია..." else "⚡ სინთეზი", color = Color(0xFFFFD166), fontSize = 9.sp, fontWeight = FontWeight.Bold)
                }
            }

            Text(
                text = "ვიზუალიზებული კონცეფცია: \"${imagery.activeConcept}\"",
                color = NeuralTextPrimary,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold
            )

            // Dynamic Generative Visualizer Canvas
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(90.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(
                        Brush.horizontalGradient(
                            listOf(NeuralDeepPurple, Color(0xFF1E1035), NeuralDeepPurple)
                        )
                    )
                    .border(1.dp, Color(0xFFFFD166).copy(alpha = 0.2f), RoundedCornerShape(12.dp))
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val w = size.width
                    val h = size.height
                    val step = w / 16f

                    // Draw reconstructed wave spectrum of mental imagery
                    for (i in 0..15) {
                        val barHeight = (sin(i * 0.6f + imagery.thetaGammaCoherence * 3) * 0.4f + 0.5f) * h * 0.8f
                        drawRect(
                            brush = Brush.verticalGradient(listOf(Color(0xFFFFD166), Color(0xFF9D4EDD))),
                            topLeft = Offset(i * step + 4f, h - barHeight),
                            size = androidx.compose.ui.geometry.Size(step - 8f, barHeight)
                        )
                    }
                }

                Row(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text("სიზუსტე: ${imagery.visualFidelityPct}%", color = Color(0xFFFFD166), fontSize = 9.sp, fontWeight = FontWeight.Bold)
                    Text("θ-γ კოჰერენტულობა: ${String.format(java.util.Locale.US, "%.2f", imagery.thetaGammaCoherence)}", color = NeuralTextSecondary, fontSize = 9.sp)
                }
            }

            // Tags
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                imagery.imageryTags.forEach { tag ->
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(NeuralDeepPurple)
                            .padding(horizontal = 6.dp, vertical = 3.dp)
                    ) {
                        Text("#$tag", color = Color(0xFFFFD166), fontSize = 8.sp)
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------------------------------------------
// 5. Emotional Resonance & Cognitive Friction Vector Composable
// -------------------------------------------------------------------------------------------------
@Composable
private fun EmotionalFrictionCard(
    friction: EmotionalFrictionState,
    onModulate: (Float, Float) -> Unit
) {
    val valenceColor = when {
        friction.valence > 0.3f -> Color(0xFF00FF66)
        friction.valence < -0.2f -> Color(0xFFFF5252)
        else -> Color(0xFFFFD166)
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(NeuralSurface)
            .border(1.dp, valenceColor.copy(alpha = 0.4f), RoundedCornerShape(20.dp))
            .padding(14.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Icon(imageVector = AppIcons.Mood, contentDescription = "ემოციური რეზონანსი", tint = valenceColor, modifier = Modifier.size(16.dp))
                    Text("ემოციური რეზონანსი & კოგნიტური ხახუნი", color = valenceColor, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
                Text("${friction.cognitiveFrictionPct}% ხახუნი", color = valenceColor, fontSize = 10.sp, fontWeight = FontWeight.Bold)
            }

            Text(
                text = "რეზონანსული განწყობა: ${friction.dominantMood}",
                color = NeuralTextPrimary,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold
            )

            Text(
                text = friction.recommendedAdaptation,
                color = NeuralTextSecondary,
                fontSize = 11.sp,
                lineHeight = 15.sp
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFF00FF66).copy(alpha = 0.15f))
                        .clickable { onModulate(0.2f, 0.1f) }
                        .padding(vertical = 6.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("🧘 ფოკუსის გაუმჯობესება", color = Color(0xFF00FF66), fontSize = 9.sp, fontWeight = FontWeight.Bold)
                }

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFFFF5252).copy(alpha = 0.15f))
                        .clickable { onModulate(-0.3f, 0.2f) }
                        .padding(vertical = 6.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("⚠️ ხახუნის დამატება", color = Color(0xFFFF5252), fontSize = 9.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

// -------------------------------------------------------------------------------------------------
// 6. Branching Cognitive Decision Tree Composable
// -------------------------------------------------------------------------------------------------
@Composable
private fun CognitiveDecisionTreeCard(
    decisionTree: CognitiveDecisionTreeState,
    onSelectBranch: (String) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(NeuralSurface)
            .border(1.dp, Color(0xFF9D4EDD).copy(alpha = 0.45f), RoundedCornerShape(20.dp))
            .padding(14.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Icon(imageVector = AppIcons.AccountTree, contentDescription = "გადაწყვეტილების ხე", tint = Color(0xFF9D4EDD), modifier = Modifier.size(16.dp))
                    Text("გადაწყვეტილების განშტოებადი ხე", color = Color(0xFF9D4EDD), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
                Text("3 აქტიური გზა", color = NeuralAccent, fontSize = 9.sp, fontWeight = FontWeight.Bold)
            }

            Text(
                text = "მრავალვარიანტული კოგნიტური განშტოება: გონება აფასებს პარალელურ ვარიანტებს მოქმედებამდე.",
                color = NeuralTextSecondary,
                fontSize = 11.sp,
                lineHeight = 15.sp
            )

            // Decision Branches List
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                decisionTree.branches.forEach { branch ->
                    val isSelected = branch.id == decisionTree.activeBranchId
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (isSelected) Color(0xFF9D4EDD).copy(alpha = 0.2f) else NeuralDeepPurple)
                            .border(
                                width = if (isSelected) 1.5.dp else 1.dp,
                                color = if (isSelected) NeuralAccent else Color.White.copy(alpha = 0.08f),
                                shape = RoundedCornerShape(12.dp)
                            )
                            .clickable { onSelectBranch(branch.id) }
                            .padding(12.dp)
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = branch.title,
                                    color = if (isSelected) NeuralAccent else Color.White,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "${branch.probabilityPct}% ალბათობა",
                                    color = if (isSelected) Color(0xFF00FF66) else NeuralTextSecondary,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Text(
                                text = branch.description,
                                color = NeuralTextPrimary,
                                fontSize = 11.sp,
                                lineHeight = 15.sp
                            )
                            Text(
                                text = "შემდეგი პროაქტიული ნაბიჯი: ${branch.nextAction}",
                                color = Color(0xFF00E5FF),
                                fontSize = 10.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------------------------------------------
// 7. Subconscious Ghost-Typing Engine Composable
// -------------------------------------------------------------------------------------------------
@Composable
private fun GhostTypingEngineCard(
    ghostTyping: GhostTypingState,
    onAccept: () -> Unit,
    onCycle: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(NeuralSurface)
            .border(1.dp, Color(0xFF00E5FF).copy(alpha = 0.45f), RoundedCornerShape(20.dp))
            .padding(14.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Icon(imageVector = AppIcons.Keyboard, contentDescription = "Ghost Typing", tint = Color(0xFF00E5FF), modifier = Modifier.size(16.dp))
                    Text("ქვეცნობიერი წინასწარ-აკრეფის ძრავა", color = Color(0xFF00E5FF), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFF00E5FF).copy(alpha = 0.15f))
                        .clickable { onCycle() }
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text("⚡ აზრის შეცვლა", color = Color(0xFF00E5FF), fontSize = 9.sp, fontWeight = FontWeight.Bold)
                }
            }

            Text(
                text = "ტელეპათიური წინასწარ-შევსება: აყალიბებს სრულ წინადადებას განზრახვის ბუფერიდან კლავიშის დაჭერამდე.",
                color = NeuralTextSecondary,
                fontSize = 11.sp
            )

            // Ghost Typing Visual Editor Box
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFF0D0618))
                    .border(1.dp, Color(0xFF00E5FF).copy(alpha = 0.25f), RoundedCornerShape(12.dp))
                    .padding(14.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("კოდის / ტექსტის ნაკადი", color = NeuralAccent, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                        Text("${ghostTyping.confidencePct}% სიზუსტე", color = Color(0xFF00FF66), fontSize = 9.sp, fontWeight = FontWeight.Bold)
                    }

                    // Simulated Code Line with Ghost completion
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = ghostTyping.typedPrefix,
                            color = Color.White,
                            fontSize = 12.sp,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold
                        )
                        if (!ghostTyping.isAccepted) {
                            Text(
                                text = ghostTyping.ghostSuggestion.removePrefix(ghostTyping.typedPrefix),
                                color = Color(0xFF00E5FF).copy(alpha = 0.45f),
                                fontSize = 12.sp,
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.Normal
                            )
                        }
                    }
                }
            }

            // Accept Button
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(if (ghostTyping.isAccepted) Color(0xFF00FF66).copy(alpha = 0.2f) else NeuralAccent)
                    .clickable { onAccept() }
                    .padding(vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (ghostTyping.isAccepted) "✓ აზრი მიღებულია და შეყვანილია" else "⚡ აზრის დადასტურება (Tab)",
                    color = if (ghostTyping.isAccepted) Color(0xFF00FF66) else NeuralDeepPurple,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

// -------------------------------------------------------------------------------------------------
// 8. Neuro-Fatigue & Clarity Spectrum Composable
// -------------------------------------------------------------------------------------------------
@Composable
private fun NeuroFatigueClarityCard(
    fatigue: NeuroFatigueState,
    onRefresh: () -> Unit
) {
    val energyColor = when {
        fatigue.mentalEnergyPct > 70 -> Color(0xFF00FF66)
        fatigue.mentalEnergyPct > 40 -> Color(0xFFFFD166)
        else -> Color(0xFFFF5252)
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(NeuralSurface)
            .border(1.dp, energyColor.copy(alpha = 0.45f), RoundedCornerShape(20.dp))
            .padding(14.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Icon(imageVector = AppIcons.BatteryChargingFull, contentDescription = "მენტალური ენერგია", tint = energyColor, modifier = Modifier.size(16.dp))
                    Text("ნეირო-დაღლილობა & სიცხადის სპექტრი", color = energyColor, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(energyColor.copy(alpha = 0.15f))
                        .clickable { onRefresh() }
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text("⚡ სკანირება θ/β", color = energyColor, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                }
            }

            Text(
                text = "სიცხადის სტატუსი: ${fatigue.clarityStatus}",
                color = NeuralTextPrimary,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold
            )

            // Mental Battery Indicator Bar
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("მენტალური ენერგიის რეზერვი", color = NeuralTextSecondary, fontSize = 10.sp)
                    Text("${fatigue.mentalEnergyPct}% (${fatigue.cognitiveEnduranceMinutes} წთ დარჩენილი)", color = energyColor, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(CircleShape)
                        .background(NeuralDeepPurple)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(fatigue.mentalEnergyPct / 100f)
                            .fillMaxHeight()
                            .clip(CircleShape)
                            .background(Brush.horizontalGradient(listOf(Color(0xFF9D4EDD), energyColor)))
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(10.dp))
                        .background(NeuralDeepPurple)
                        .padding(8.dp)
                ) {
                    Column {
                        Text("θ/β თანაფარდობა", color = NeuralTextSecondary, fontSize = 8.sp)
                        Text("${String.format(java.util.Locale.US, "%.2f", fatigue.thetaBetaRatio)} (ოპტიმ. < 2.0)", color = NeuralTextPrimary, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }
                }
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(10.dp))
                        .background(NeuralDeepPurple)
                        .padding(8.dp)
                ) {
                    Column {
                        Text("რეკომენდაცია", color = NeuralTextSecondary, fontSize = 8.sp)
                        Text(fatigue.recoveryRecommendation.take(28) + "...", color = Color(0xFF00E5FF), fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------------------------------------------
// 9. Thought Stream Timeline & Semantic Search Composable
// -------------------------------------------------------------------------------------------------
@Composable
private fun ThoughtStreamTimelineCard(
    timeline: ThoughtTimelineState,
    onSearch: (String) -> Unit
) {
    var searchInput by remember { mutableStateOf(timeline.searchQuery) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(NeuralSurface)
            .border(1.dp, Color(0xFFFFD166).copy(alpha = 0.4f), RoundedCornerShape(20.dp))
            .padding(14.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Icon(imageVector = AppIcons.Timeline, contentDescription = "ქრონოლოგია", tint = Color(0xFFFFD166), modifier = Modifier.size(16.dp))
                    Text("აზრების ნაკადის ქრონოლოგია & ლოგები", color = Color(0xFFFFD166), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
                Text("${timeline.historyLogs.size} ჩაწერილი აზრი", color = NeuralAccent, fontSize = 9.sp, fontWeight = FontWeight.Bold)
            }

            // Search Bar
            TextField(
                value = searchInput,
                onValueChange = {
                    searchInput = it
                    onSearch(it)
                },
                placeholder = { Text("მოძებნეთ დეკოდირებული აზრები...", color = NeuralTextSecondary, fontSize = 11.sp) },
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp)),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = NeuralDeepPurple,
                    unfocusedContainerColor = NeuralDeepPurple,
                    focusedTextColor = NeuralTextPrimary,
                    unfocusedTextColor = NeuralTextPrimary,
                    focusedIndicatorColor = Color(0xFFFFD166),
                    unfocusedIndicatorColor = Color.Transparent
                ),
                singleLine = true
            )

            // Timeline Items
            val filteredLogs = timeline.historyLogs.filter {
                searchInput.isBlank() || it.title.contains(searchInput, ignoreCase = true) || it.detail.contains(searchInput, ignoreCase = true)
            }

            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                filteredLogs.forEach { item ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(NeuralDeepPurple)
                            .padding(10.dp)
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = item.title,
                                    color = Color(0xFFFFD166),
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = item.timestamp,
                                    color = NeuralTextSecondary,
                                    fontSize = 9.sp,
                                    fontFamily = FontFamily.Monospace
                                )
                            }
                            Text(
                                text = item.detail,
                                color = NeuralTextPrimary,
                                fontSize = 11.sp,
                                lineHeight = 15.sp
                            )
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "#${item.category}",
                                    color = NeuralAccent,
                                    fontSize = 9.sp
                                )
                                Text(
                                    text = "${item.confidencePct}% სიზუსტე",
                                    color = Color(0xFF00FF66),
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
}

// -------------------------------------------------------------------------------------------------
// 10. Audio Neuro-Entrainment Beats Generator Composable
// -------------------------------------------------------------------------------------------------
@Composable
private fun AudioNeuroEntrainmentCard(
    entrainment: NeuroEntrainmentState,
    onTogglePlay: () -> Unit,
    onSelectMode: (String) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(NeuralSurface)
            .border(1.dp, Color(0xFF00FF66).copy(alpha = 0.45f), RoundedCornerShape(20.dp))
            .padding(14.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Icon(imageVector = AppIcons.Waves, contentDescription = "ნეირო-სინქრონიზაცია", tint = Color(0xFF00FF66), modifier = Modifier.size(16.dp))
                    Text("აუდიო ნეირო-სინქრონიზაციის ბინორალური ტალღები", color = Color(0xFF00FF66), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (entrainment.isPlaying) Color(0xFF00FF66).copy(alpha = 0.2f) else NeuralDeepPurple)
                        .clickable { onTogglePlay() }
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = if (entrainment.isPlaying) "🔊 აქტიურია" else "🔇 შეჩერებულია",
                        color = if (entrainment.isPlaying) Color(0xFF00FF66) else NeuralTextSecondary,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Text(
                text = "ბინორალური სინაფსური სინქრონიზაცია: მართავს აუდიო ტალღებს (${entrainment.carrierFrequencyHz} Hz) ტვინის სასურველი სიხშირის სტიმულაციისთვის (${entrainment.targetWaveHz} Hz).",
                color = NeuralTextSecondary,
                fontSize = 11.sp,
                lineHeight = 15.sp
            )

            Text(
                text = "ეფექტი: ${entrainment.entrainmentBenefit}",
                color = Color(0xFF00E5FF),
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold
            )

            // Frequency Mode Selector Pills
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                listOf(
                    "Alpha ნაკადი (10 Hz)",
                    "Theta შემოქმედება (6 Hz)",
                    "Gamma ფოკუსი (40 Hz)"
                ).forEach { mode ->
                    val isSelected = entrainment.activeFrequencyMode.startsWith(mode.take(5))
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (isSelected) Color(0xFF00FF66) else NeuralDeepPurple)
                            .clickable { onSelectMode(mode) }
                            .padding(vertical = 6.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = mode.split(" ").first(),
                            color = if (isSelected) NeuralDeepPurple else NeuralTextPrimary,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------------------------------------------
// Standard Simulation Matrix Helper Composables
// -------------------------------------------------------------------------------------------------
@Composable
private fun TimeHorizonsPredictionCard(
    horizons: TimeHorizonPredictions,
    activeTab: String,
    onTabSelected: (String) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(NeuralSurface)
            .border(1.dp, Color(0xFF00E5FF).copy(alpha = 0.35f), RoundedCornerShape(20.dp))
            .padding(14.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Icon(imageVector = AppIcons.AccessTime, contentDescription = "დროის ჰორიზონტი", tint = Color(0xFF00E5FF), modifier = Modifier.size(16.dp))
                    Text("დროის ჰორიზონტის პროგნოზები", color = Color(0xFF00E5FF), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
                Text("3 მასშტაბი", color = NeuralTextSecondary, fontSize = 10.sp)
            }

            // Tabs for +30s / +5m / +30m
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                TimeHorizonPill("+30 წმ (მოტორული)", activeTab == "30s", Modifier.weight(1f)) { onTabSelected("30s") }
                TimeHorizonPill("+5 წთ (დავალება)", activeTab == "5m", Modifier.weight(1f)) { onTabSelected("5m") }
                TimeHorizonPill("+30 წთ (ენერგია)", activeTab == "30m", Modifier.weight(1f)) { onTabSelected("30m") }
            }

            // Horizon Content Box
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(NeuralDeepPurple)
                    .padding(12.dp)
            ) {
                val (badgeColor, title, desc) = when (activeTab) {
                    "30s" -> Triple(Color(0xFF00FF66), "დაუყოვნებელი მოტორული რეფლექსი (+30 წმ)", horizons.horizon30Sec)
                    "5m" -> Triple(Color(0xFF00E5FF), "მიმდინარე სამუშაო მიზანი (+5 წთ)", horizons.horizon5Min)
                    else -> Triple(Color(0xFFFFD166), "ქვეცნობიერი ენერგია & ტრაექტორია (+30 წთ)", horizons.horizon30Min)
                }

                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(title, color = badgeColor, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    Text(desc, color = NeuralTextPrimary, fontSize = 12.sp, lineHeight = 17.sp)
                }
            }
        }
    }
}

@Composable
private fun TimeHorizonPill(label: String, isSelected: Boolean, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(if (isSelected) Color(0xFF00E5FF) else NeuralDeepPurple)
            .clickable { onClick() }
            .padding(vertical = 6.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            color = if (isSelected) NeuralDeepPurple else NeuralTextPrimary,
            fontSize = 9.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun CalibrationWeightsSection(weights: StreamCalibrationWeights) {
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("მულტიმოდალური კალიბრაციის წონები (#${weights.reinforcedIterations})", color = NeuralTextSecondary, fontSize = 9.sp, fontWeight = FontWeight.Bold)
            Text("${weights.calibrationConfidence}% სიზუსტე", color = NeuralAccent, fontSize = 9.sp, fontWeight = FontWeight.Bold)
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(3.dp))
        ) {
            Box(modifier = Modifier.weight(weights.neuralWeight).fillMaxSize().background(Color(0xFF9D4EDD)))
            Box(modifier = Modifier.weight(weights.touchWeight).fillMaxSize().background(Color(0xFF00E5FF)))
            Box(modifier = Modifier.weight(weights.audioWeight).fillMaxSize().background(Color(0xFF00FF66)))
            Box(modifier = Modifier.weight(weights.visionWeight).fillMaxSize().background(Color(0xFFFFD166)))
            Box(modifier = Modifier.weight(weights.bioWeight).fillMaxSize().background(Color(0xFFFF5252)))
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("EEG ${(weights.neuralWeight * 100).toInt()}%", color = Color(0xFF9D4EDD), fontSize = 9.sp)
            Text("შეხება ${(weights.touchWeight * 100).toInt()}%", color = Color(0xFF00E5FF), fontSize = 9.sp)
            Text("ხმა ${(weights.audioWeight * 100).toInt()}%", color = Color(0xFF00FF66), fontSize = 9.sp)
            Text("მზერა ${(weights.visionWeight * 100).toInt()}%", color = Color(0xFFFFD166), fontSize = 9.sp)
            Text("ბიო ${(weights.bioWeight * 100).toInt()}%", color = Color(0xFFFF5252), fontSize = 9.sp)
        }
    }
}

@Composable
private fun MicroHesitationCard(
    metrics: MicroHesitationMetrics,
    touchCount: Int,
    lastTouchCoords: String
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(NeuralSurface)
            .border(1.dp, Color(0xFF00FF66).copy(alpha = 0.35f), RoundedCornerShape(20.dp))
            .padding(14.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Icon(imageVector = AppIcons.Speed, contentDescription = "მიკრო-დაყოვნება", tint = Color(0xFF00FF66), modifier = Modifier.size(16.dp))
                    Text("მიკრო-დაყოვნება & კინეტიკური ლატენტობა", color = Color(0xFF00FF66), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
                Text("${metrics.interTapLatencyMs} ms ლატენტობა", color = Color(0xFF00FF66), fontSize = 10.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
            }

            Text(
                text = "აკრეფის რიტმის სტატუსი: ${metrics.typingRhythmState}",
                color = NeuralTextPrimary,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                HesitationMetricPill("დაყოვნების ინდექსი", String.format(java.util.Locale.US, "%.2f", metrics.hesitationIndex), Modifier.weight(1f))
                HesitationMetricPill("მოტორული რყევა", "${metrics.motorJitterPct}%", Modifier.weight(1f))
                HesitationMetricPill("სულ შეხებები", "$touchCount", Modifier.weight(1f))
            }
        }
    }
}

@Composable
private fun HesitationMetricPill(label: String, value: String, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(NeuralDeepPurple)
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(label, color = NeuralTextSecondary, fontSize = 8.sp)
            Text(value, color = NeuralTextPrimary, fontSize = 11.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
        }
    }
}

@Composable
private fun CircadianDynamicsCard(
    circadian: CircadianEnvironment,
    heartRate: Int,
    activeAppContext: String
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(NeuralSurface)
            .border(1.dp, Color(0xFFFFD166).copy(alpha = 0.35f), RoundedCornerShape(20.dp))
            .padding(14.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Icon(imageVector = AppIcons.WbSunny, contentDescription = "ცირკადული", tint = Color(0xFFFFD166), modifier = Modifier.size(16.dp))
                    Text("ცირკადული & გარემო კონტექსტი", color = Color(0xFFFFD166), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
                Text("ელემენტი: ${circadian.batteryPct}%", color = Color(0xFFFFD166), fontSize = 10.sp, fontWeight = FontWeight.Bold)
            }

            Text("ფაზა: ${circadian.timeOfDayPeriod} | თერმული: ${circadian.thermalState}", color = NeuralTextPrimary, fontSize = 11.sp)

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                HesitationMetricPill("გარემო სინათლე", "${circadian.ambientLux} Lux", Modifier.weight(1f))
                HesitationMetricPill("ბიოლოგიური რიტმი", "$heartRate BPM", Modifier.weight(1f))
                HesitationMetricPill("სამუშაო კონტექსტი", activeAppContext.take(12), Modifier.weight(1f))
            }
        }
    }
}

@Composable
private fun ProactiveActionSandboxCard(
    sandbox: ActionSandboxState,
    onToggle: (String) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(NeuralCardPurple)
            .border(1.dp, NeuralAccent.copy(alpha = 0.35f), RoundedCornerShape(20.dp))
            .padding(14.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Icon(imageVector = AppIcons.Build, contentDescription = "პანელი", tint = NeuralAccent, modifier = Modifier.size(16.dp))
                    Text("პროაქტიული სისტემური მოქმედებების პანელი", color = NeuralAccent, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
                Text("მართვა", color = NeuralTextSecondary, fontSize = 10.sp)
            }

            SandboxToggleRow("არ შემაწუხოთ (ფოკუსის ფარი)", sandbox.isDndActive) { onToggle("DND") }
            SandboxToggleRow("ეკრანის დაბნელება & ლურჯი სინათლის ფილტრი", sandbox.isDisplayDimmed) { onToggle("DISPLAY_DIM") }
            SandboxToggleRow("432Hz ბინორალური დამამშვიდებელი ხმა", sandbox.isBinauralAudioOn) { onToggle("BINAURAL") }
            SandboxToggleRow("კლავიატურის ბუფერის წინასწარი მომზადება", sandbox.isImePrewarmed) { onToggle("IME_PREWARM") }
            SandboxToggleRow("პროცესორის თერმული ოპტიმიზაცია", sandbox.isThermalOptimized) { onToggle("THERMAL") }
        }
    }
}

@Composable
private fun SandboxToggleRow(
    title: String,
    isActive: Boolean,
    onToggle: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(NeuralDeepPurple)
            .clickable { onToggle() }
            .padding(horizontal = 12.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(title, color = NeuralTextPrimary, fontSize = 11.sp)
        Switch(
            checked = isActive,
            onCheckedChange = { onToggle() },
            colors = SwitchDefaults.colors(
                checkedThumbColor = NeuralAccent,
                checkedTrackColor = NeuralAccent.copy(alpha = 0.4f),
                uncheckedThumbColor = Color.Gray,
                uncheckedTrackColor = Color.DarkGray
            )
        )
    }
}

@Composable
private fun PresetThoughtChip(
    label: String,
    prompt: String,
    onSelect: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(NeuralDeepPurple)
            .border(1.dp, NeuralAccent.copy(alpha = 0.3f), RoundedCornerShape(10.dp))
            .clickable { onSelect(prompt) }
            .padding(vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            color = NeuralAccent,
            fontSize = 10.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
private fun UnifiedMatrixVisualizer(
    isSyncing: Boolean,
    matchPercentage: Float,
    alphaHz: Float,
    touchCount: Int,
    audioDb: Float,
    gazeX: Float,
    heartRate: Int,
    onCoreClicked: () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "sim_pulse")
    val coreScale by infiniteTransition.animateFloat(
        initialValue = 1.0f,
        targetValue = 1.06f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "core_scale"
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(290.dp)
            .clip(RoundedCornerShape(28.dp))
            .background(NeuralSurface)
            .border(1.dp, Color.White.copy(alpha = 0.1f), RoundedCornerShape(28.dp))
    ) {
        // Synaptic Background Canvas
        Canvas(modifier = Modifier.fillMaxSize()) {
            val center = Offset(size.width / 2f, size.height / 2f)
            val radius = size.width.coerceAtMost(size.height) * 0.4f

            // Concentric Energy Rings
            drawCircle(
                color = NeuralAccent.copy(alpha = 0.08f),
                radius = radius * 1.05f,
                center = center,
                style = Stroke(width = 2f)
            )
            drawCircle(
                color = NeuralAccent.copy(alpha = 0.15f),
                radius = radius * 0.7f,
                center = center,
                style = Stroke(width = 2f)
            )
            drawCircle(
                color = NeuralAccent.copy(alpha = 0.25f),
                radius = radius * 0.4f,
                center = center,
                style = Stroke(width = 2f)
            )

            // Converging Multimodal Nodes
            val nodeCount = 6
            val nodeColors = listOf(
                Color(0xFF9D4EDD), // Brainwaves (EEG)
                Color(0xFF00E5FF), // Touch Kinematics
                Color(0xFF00FF66), // Acoustic Stream
                Color(0xFFFFD166), // Ocular/Gaze
                Color(0xFFFF5252), // Biometrics (Heart)
                Color(0xFF00E5FF)  // OS Context
            )

            for (i in 0 until nodeCount) {
                val angle = (i.toFloat() / nodeCount) * 2 * Math.PI - (Math.PI / 2)
                val nodeX = center.x + (radius * 0.85f * kotlin.math.cos(angle)).toFloat()
                val nodeY = center.y + (radius * 0.85f * kotlin.math.sin(angle)).toFloat()

                // Connecting synaptic ray
                drawLine(
                    color = nodeColors[i % nodeColors.size].copy(alpha = if (isSyncing) 0.5f else 0.2f),
                    start = Offset(nodeX, nodeY),
                    end = center,
                    strokeWidth = 2f
                )

                // Outer Node Circle
                drawCircle(
                    color = nodeColors[i % nodeColors.size],
                    radius = 8f,
                    center = Offset(nodeX, nodeY)
                )
            }
        }

        // Center Quantum Prediction Core
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .clickable { onCoreClicked() },
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(110.dp)
                    .scale(if (isSyncing) coreScale else 1f)
                    .clip(CircleShape)
                    .background(
                        Brush.radialGradient(
                            colors = listOf(NeuralAccent, NeuralDeepPurple)
                        )
                    )
                    .border(2.dp, NeuralAccent, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = AppIcons.Psychology,
                        contentDescription = "ბირთვი",
                        tint = NeuralDeepPurple,
                        modifier = Modifier.size(32.dp)
                    )
                    Text(
                        text = "${String.format(java.util.Locale.US, "%.1f", matchPercentage)}%",
                        color = NeuralDeepPurple,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "მულტიმოდალური ბირთვი",
                color = NeuralAccent,
                fontSize = 10.sp,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )
        }

        // Corner Telemetry Badges
        Box(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(14.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(NeuralDeepPurple.copy(alpha = 0.85f))
                .padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            Text(
                text = "EEG: ${String.format(java.util.Locale.US, "%.1f", alphaHz)} Hz",
                color = Color(0xFF9D4EDD),
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(14.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(NeuralDeepPurple.copy(alpha = 0.85f))
                .padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            Text(
                text = "შეხება: $touchCount",
                color = Color(0xFF00E5FF),
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Box(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(14.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(NeuralDeepPurple.copy(alpha = 0.85f))
                .padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            Text(
                text = "აუდიო: ${audioDb.toInt()} dB",
                color = Color(0xFF00FF66),
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(14.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(NeuralDeepPurple.copy(alpha = 0.85f))
                .padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            Text(
                text = "პულსი: $heartRate BPM",
                color = Color(0xFFFF5252),
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun StreamControlMatrixSection(
    alphaBandHz: Float,
    betaBandHz: Float,
    thetaBandHz: Float,
    gammaBandHz: Float,
    cognitiveLoadPct: Int,
    subconsciousFocusLevel: String,
    touchTapsCount: Int,
    lastTouchCoords: String,
    audioDb: Float,
    speakerOutputDb: Float,
    cameraGazeX: Float,
    cameraGazeY: Float,
    motionTremor: Float,
    heartRateBpm: Int,
    activeAppContext: String,
    onInjectStimulus: (String) -> Unit,
    onAppContextChanged: (String) -> Unit,
    onTouchTap: (Float, Float) -> Unit
) {
    var isExpanded by remember { mutableStateOf(true) }

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { isExpanded = !isExpanded }
                .padding(vertical = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "სენსორული & ნეირონული ნაკადები (ინტერაქტიული)",
                color = NeuralAccent,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )
            Icon(
                imageVector = AppIcons.Settings,
                contentDescription = "ნაკადების ჩაკეცვა",
                tint = NeuralAccent,
                modifier = Modifier.size(16.dp)
            )
        }

        AnimatedVisibility(visible = isExpanded) {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                // Interactive Touch Kinematics Box
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(130.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .background(NeuralDeepPurple)
                        .border(1.dp, Color(0xFF00E5FF).copy(alpha = 0.4f), RoundedCornerShape(20.dp))
                        .pointerInput(Unit) {
                            detectTapGestures { offset ->
                                onTouchTap(offset.x, offset.y)
                            }
                        }
                        .padding(14.dp)
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                Icon(imageVector = AppIcons.TouchApp, contentDescription = "შეხება", tint = Color(0xFF00E5FF), modifier = Modifier.size(16.dp))
                                Text("შეხების კინემატიკური ნაკადი (დააჭირეთ აქ)", color = Color(0xFF00E5FF), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                            Text("შეხებები: $touchTapsCount", color = NeuralTextPrimary, fontSize = 11.sp, fontFamily = FontFamily.Monospace)
                        }

                        Text(
                            text = "ბოლო შეხების კოორდინატები: $lastTouchCoords | წნევა: 0.72 | სიჩქარე: 3.4 Hz",
                            color = NeuralTextSecondary,
                            fontSize = 11.sp,
                            fontFamily = FontFamily.Monospace
                        )

                        Text(
                            text = "შეეხეთ ამ პანელს მოტორული იმპულსების პროგნოზირების მატრიცაში გადასაცემად.",
                            color = NeuralTextPrimary.copy(alpha = 0.7f),
                            fontSize = 10.sp
                        )
                    }
                }

                // Brainwave Frequency Matrix & Mental Stimuli
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp))
                        .background(NeuralSurface)
                        .border(1.dp, Color(0xFF9D4EDD).copy(alpha = 0.35f), RoundedCornerShape(20.dp))
                        .padding(14.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                Icon(imageVector = AppIcons.Psychology, contentDescription = "EEG", tint = Color(0xFF9D4EDD), modifier = Modifier.size(16.dp))
                                Text("EEG ტვინის ტალღები", color = Color(0xFF9D4EDD), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                            Text("დატვირთვა: $cognitiveLoadPct%", color = NeuralAccent, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }

                        Text(text = "ფოკუსირება: $subconsciousFocusLevel", color = NeuralTextPrimary, fontSize = 11.sp)

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            FrequencyPill("Alpha (8-12Hz)", alphaBandHz, Color(0xFF00FF66), Modifier.weight(1f))
                            FrequencyPill("Beta (13-30Hz)", betaBandHz, Color(0xFF00E5FF), Modifier.weight(1f))
                            FrequencyPill("Theta (4-8Hz)", thetaBandHz, Color(0xFFFFD166), Modifier.weight(1f))
                            FrequencyPill("Gamma (30+Hz)", gammaBandHz, Color(0xFFFF5252), Modifier.weight(1f))
                        }

                        // Mental Stimuli Injectors
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            StimulusButton("☕ კოფეინი", "CAFFEINE_SPIKE", onInjectStimulus, Modifier.weight(1f))
                            StimulusButton("🧘 სუნთქვა", "DEEP_BREATHING", onInjectStimulus, Modifier.weight(1f))
                            StimulusButton("🧩 ამოცანა", "COMPLEX_PROBLEM", onInjectStimulus, Modifier.weight(1f))
                        }
                    }
                }

                // Active App Context Selector
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp))
                        .background(NeuralSurface)
                        .border(1.dp, Color.White.copy(alpha = 0.1f), RoundedCornerShape(20.dp))
                        .padding(14.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("აქტიური აპლიკაციის კონტექსტი", color = NeuralTextSecondary, fontSize = 10.sp, fontWeight = FontWeight.Bold)

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            AppContextChip("💻 IDE", "Developer IDE & Neural Research", activeAppContext, onAppContextChanged, Modifier.weight(1f))
                            AppContextChip("💬 Chat", "Messaging & Fast Communications", activeAppContext, onAppContextChanged, Modifier.weight(1f))
                            AppContextChip("🎨 Canvas", "Creative Vector & UI Design Studio", activeAppContext, onAppContextChanged, Modifier.weight(1f))
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun FrequencyPill(label: String, value: Float, color: Color, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(NeuralDeepPurple)
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(label, color = color, fontSize = 8.sp)
            Text("${String.format(java.util.Locale.US, "%.1f", value)}Hz", color = NeuralTextPrimary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun StimulusButton(label: String, type: String, onInject: (String) -> Unit, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(NeuralDeepPurple)
            .clickable { onInject(type) }
            .padding(vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(label, color = NeuralTextPrimary, fontSize = 10.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun AppContextChip(label: String, fullContext: String, activeContext: String, onSelect: (String) -> Unit, modifier: Modifier = Modifier) {
    val isSelected = activeContext == fullContext
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(if (isSelected) NeuralAccent else NeuralDeepPurple)
            .clickable { onSelect(fullContext) }
            .padding(vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            color = if (isSelected) NeuralDeepPurple else NeuralTextPrimary,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

// -------------------------------------------------------------------------------------------------
// 11. Real Hardware Sensors & Accelerometer Composable
// -------------------------------------------------------------------------------------------------
@Composable
private fun RealHardwareSensorsCard(
    sensors: com.example.sensor.RealHardwareSensorState
) {
    val stabilityColor = when {
        sensors.neuromuscularStabilityPct > 80 -> Color(0xFF00FF66)
        sensors.neuromuscularStabilityPct > 60 -> Color(0xFFFFD166)
        else -> Color(0xFFFF5252)
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(NeuralSurface)
            .border(1.dp, Color(0xFF00E5FF).copy(alpha = 0.45f), RoundedCornerShape(20.dp))
            .padding(14.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically, 
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.weight(1f, fill = false)
                ) {
                    Icon(imageVector = AppIcons.Speed, contentDescription = "სენსორები", tint = Color(0xFF00E5FF), modifier = Modifier.size(16.dp))
                    Text("აპარატურული სენსორები (IMU)", color = Color(0xFF00E5FF), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (sensors.isTracking) Color(0xFF00FF66).copy(alpha = 0.2f) else NeuralDeepPurple)
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = if (sensors.isTracking) "● აქტიურია" else "მზადყოფნა",
                        color = if (sensors.isTracking) Color(0xFF00FF66) else NeuralTextSecondary,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Text(
                text = "მოწყობილობის ფიზიკური IMU სენსორები: ზომავს ხელის მიკრო-ტრემორს (8–12 Hz) და ფიზიოლოგიურ სტაბილურობას აქსელერომეტრიდან და გიროსკოპიდან.",
                color = NeuralTextSecondary,
                fontSize = 11.sp,
                lineHeight = 15.sp
            )

            // Stability & Hand Tension Status
            Text(
                text = "მოტორული სტატუსი: ${sensors.handTensionLevel}",
                color = NeuralTextPrimary,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold
            )

            // 3-Metric Tiles
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(10.dp))
                        .background(NeuralDeepPurple)
                        .padding(8.dp)
                ) {
                    Column {
                        Text("მიკრო-ტრემორი", color = NeuralTextSecondary, fontSize = 8.sp)
                        Text("${String.format(java.util.Locale.US, "%.2f", sensors.microTremorMagnitude)} g", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(10.dp))
                        .background(NeuralDeepPurple)
                        .padding(8.dp)
                ) {
                    Column {
                        Text("სიხშირე (Hz)", color = NeuralTextSecondary, fontSize = 8.sp)
                        Text("${String.format(java.util.Locale.US, "%.1f", sensors.physiologicalTremorHz)} Hz", color = Color(0xFF00E5FF), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(10.dp))
                        .background(NeuralDeepPurple)
                        .padding(8.dp)
                ) {
                    Column {
                        Text("სტაბილურობა", color = NeuralTextSecondary, fontSize = 8.sp)
                        Text("${sensors.neuromuscularStabilityPct}%", color = stabilityColor, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            // Real X/Y/Z Vector telemetry with stable 2-row layout to prevent screen jumping
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(NeuralDeepPurple.copy(alpha = 0.5f))
                    .padding(horizontal = 8.dp, vertical = 6.dp),
                verticalArrangement = Arrangement.spacedBy(3.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "აქსელერომეტრი",
                        color = NeuralTextSecondary,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = "X: ${String.format(java.util.Locale.US, "%+05.2f", sensors.accelX)}  Y: ${String.format(java.util.Locale.US, "%+05.2f", sensors.accelY)}  Z: ${String.format(java.util.Locale.US, "%+05.2f", sensors.accelZ)}",
                        color = Color.White,
                        fontSize = 9.sp,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "გიროსკოპი",
                        color = NeuralTextSecondary,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = "X: ${String.format(java.util.Locale.US, "%+05.2f", sensors.gyroX)}  Y: ${String.format(java.util.Locale.US, "%+05.2f", sensors.gyroY)}  Z: ${String.format(java.util.Locale.US, "%+05.2f", sensors.gyroZ)}",
                        color = Color(0xFF00E5FF),
                        fontSize = 9.sp,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

// -------------------------------------------------------------------------------------------------
// 12. Real Camera-X Gaze & Facial Radiance HUD Composable
// -------------------------------------------------------------------------------------------------
@Composable
private fun RealCameraGazeHUDCard(
    gaze: com.example.sensor.RealCameraGazeState,
    onToggleCamera: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(NeuralSurface)
            .border(1.dp, Color(0xFFFF52A2).copy(alpha = 0.45f), RoundedCornerShape(20.dp))
            .padding(14.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically, 
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.weight(1f, fill = false)
                ) {
                    Icon(imageVector = AppIcons.CameraFront, contentDescription = "კამერის მზერა", tint = Color(0xFFFF52A2), modifier = Modifier.size(16.dp))
                    Text("კამერის მზერა & სახის HUD", color = Color(0xFFFF52A2), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (gaze.isCameraActive) Color(0xFFFF52A2).copy(alpha = 0.2f) else NeuralDeepPurple)
                        .clickable { onToggleCamera() }
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = if (gaze.isCameraActive) "● აქტიურია" else "ჩართვა",
                        color = if (gaze.isCameraActive) Color(0xFFFF52A2) else NeuralTextSecondary,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Text(
                text = "წინა კამერის კომპიუტერული ხედვა: აფასებს სახის ნათებას (rPPG პულსი), თვალის დახამხამებას, გუგის რეაქციასა და მზერის მიმართულებას 60 FPS რეჟიმში.",
                color = NeuralTextSecondary,
                fontSize = 11.sp,
                lineHeight = 15.sp
            )

            // Gaze Orientation Visual Indicator
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFF140826))
                    .border(1.dp, Color(0xFFFF52A2).copy(alpha = 0.25f), RoundedCornerShape(12.dp))
                    .padding(12.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("მზერის ვექტორის ფოკუსი", color = Color(0xFFFF52A2), fontSize = 9.sp, fontWeight = FontWeight.Bold)
                        Text("${gaze.gazeConfidencePct}% ფიქსაცია", color = Color(0xFF00FF66), fontSize = 9.sp, fontWeight = FontWeight.Bold)
                    }
                    Text(
                        text = "👀 ${gaze.gazeDirection}",
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Gaze & Optical Biometrics Grid
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(10.dp))
                        .background(NeuralDeepPurple)
                        .padding(8.dp)
                ) {
                    Column {
                        Text("დახამხამება", color = NeuralTextSecondary, fontSize = 8.sp)
                        Text("${gaze.eyeBlinkRatePerMin} / წთ", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(10.dp))
                        .background(NeuralDeepPurple)
                        .padding(8.dp)
                ) {
                    Column {
                        Text("გუგის რეაქცია", color = NeuralTextSecondary, fontSize = 8.sp)
                        Text("${String.format(java.util.Locale.US, "%.2f", gaze.opticalPupilDilationScore)}", color = Color(0xFF00E5FF), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(10.dp))
                        .background(NeuralDeepPurple)
                        .padding(8.dp)
                ) {
                    Column {
                        Text("rPPG პულსი", color = NeuralTextSecondary, fontSize = 8.sp)
                        Text("${gaze.opticalRadiancePulseBpm} BPM", color = Color(0xFF00FF66), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

