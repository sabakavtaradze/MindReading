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
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun UnifiedSimulationMatrix(
    isSyncing: Boolean,
    matchPercentage: Float,
    statusText: String,
    // Neural Streams
    alphaBandHz: Float,
    betaBandHz: Float,
    thetaBandHz: Float,
    gammaBandHz: Float,
    cognitiveLoadPct: Int,
    subconsciousFocusLevel: String,
    // Sensory Streams
    touchTapsCount: Int,
    lastTouchCoords: String,
    audioDb: Float,
    speakerOutputDb: Float,
    cameraGazeX: Float,
    cameraGazeY: Float,
    motionTremor: Float,
    heartRateBpm: Int,
    activeAppContext: String,
    // Prediction Output
    currentPredictionTitle: String,
    currentPredictionText: String,
    currentActionPlan: String,
    timeHorizons: TimeHorizonPredictions,
    hesitationMetrics: MicroHesitationMetrics,
    circadian: CircadianEnvironment,
    calibrationWeights: StreamCalibrationWeights,
    sandboxActions: ActionSandboxState,
    // 10 Advanced Thought Prediction Modules
    subvocalSpeech: SubvocalSpeechState,
    mentalImagery: MentalImageryState,
    preErrorState: PreErrorDetectionState,
    mindGraph: SemanticMindGraphState,
    emotionalFriction: EmotionalFrictionState,
    decisionTree: CognitiveDecisionTreeState,
    ghostTyping: GhostTypingState,
    neuroFatigue: NeuroFatigueState,
    thoughtTimeline: ThoughtTimelineState,
    entrainment: NeuroEntrainmentState,
    realSensors: com.example.sensor.RealHardwareSensorState,
    cameraGaze: com.example.sensor.RealCameraGazeState,
    isGeneratingPrediction: Boolean,
    // Callbacks
    onRunUnifiedInference: () -> Unit,
    onInjectStimulus: (String) -> Unit,
    onAppContextChanged: (String) -> Unit,
    onTouchTap: (Float, Float) -> Unit,
    onDecodeCustomThought: (String) -> Unit,
    onApplyFeedback: (Boolean) -> Unit,
    onToggleSandboxAction: (String) -> Unit,
    onTriggerSubvocal: () -> Unit,
    onSynthesizeImagery: (String) -> Unit,
    onCheckPreError: () -> Unit,
    onSelectGraphNode: (String) -> Unit,
    onModulateMood: (Float, Float) -> Unit,
    onSelectDecisionBranch: (String) -> Unit,
    onAcceptGhostTyping: () -> Unit,
    onCycleGhostSuggestion: () -> Unit,
    onRefreshFatigue: () -> Unit,
    onSearchThoughtHistory: (String) -> Unit,
    onToggleEntrainment: () -> Unit,
    onSetEntrainmentMode: (String) -> Unit,
    onToggleCamera: () -> Unit,
    modifier: Modifier = Modifier
) {
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
                // Header badge with pulsing live dot
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
                                .background(NeuralAccent)
                        )
                        Text(
                            text = "დეკოდირებული აზრები • რას ფიქრობთ ახლა",
                            color = NeuralAccent,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = 1.sp
                        )
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .background(NeuralAccent.copy(alpha = 0.2f))
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "${String.format(java.util.Locale.US, "%.1f", matchPercentage)}% ნეირო-სიზუსტე",
                            color = NeuralAccent,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                // Primary Decoded Thought Title Banner
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(NeuralDeepPurple.copy(alpha = 0.85f))
                        .border(1.dp, Color(0xFF00E5FF).copy(alpha = 0.35f), RoundedCornerShape(16.dp))
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "🧠 მიმდინარე მენტალური განზრახვა & აზრი:",
                        color = Color(0xFF00E5FF),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = currentPredictionTitle,
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.ExtraBold,
                        lineHeight = 24.sp
                    )
                    Text(
                        text = currentPredictionText,
                        color = NeuralTextPrimary,
                        fontSize = 13.sp,
                        lineHeight = 19.sp
                    )
                }

                // Live Inner Monologue Telemetry Bar
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
                            Text("შინაგანი ხმა (Subvocal)", color = Color(0xFF00E5FF), fontSize = 9.sp, fontWeight = FontWeight.Bold)
                            Text(subvocalSpeech.decodedPhrase.take(35) + if (subvocalSpeech.decodedPhrase.length > 35) "..." else "", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.SemiBold, maxLines = 1)
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
                            Text("ემოციური ვექტორი", color = Color(0xFF9D4EDD), fontSize = 9.sp, fontWeight = FontWeight.Bold)
                            Text(emotionalFriction.dominantMood.take(28), color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.SemiBold, maxLines = 1)
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
        // 2. Pre-Error ERN (Error-Related Negativity) Wave Detector Card
        // -------------------------------------------------------------
        PreErrorErnDetectorCard(
            preError = preErrorState,
            onSimulateCheck = onCheckPreError
        )

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
        // 8. Neuro-Fatigue & Clarity Spectrum Card (Brain Fog Early Warning)
        // -------------------------------------------------------------
        NeuroFatigueClarityCard(
            fatigue = neuroFatigue,
            onRefresh = onRefreshFatigue
        )

        // -------------------------------------------------------------
        // 9. Thought Stream Timeline & Semantic Search History Card
        // -------------------------------------------------------------
        ThoughtStreamTimelineCard(
            timeline = thoughtTimeline,
            onSearch = onSearchThoughtHistory
        )

        // -------------------------------------------------------------
        // 10. Audio Neuro-Entrainment Beats Generator Card
        // -------------------------------------------------------------
        AudioNeuroEntrainmentCard(
            entrainment = entrainment,
            onTogglePlay = onToggleEntrainment,
            onSelectMode = onSetEntrainmentMode
        )

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

        // Time Horizon Predictions Card (+30s / +5m / +30m)
        TimeHorizonsPredictionCard(
            horizons = timeHorizons,
            activeTab = activeHorizonTab,
            onTabSelected = { activeHorizonTab = it }
        )

        // Master Multimodal Inference Action Card
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp))
                .background(NeuralCardPurple)
                .border(1.dp, NeuralAccent.copy(alpha = 0.4f), RoundedCornerShape(24.dp))
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
                            text = "PREDICTED INTENT & COGNITIVE STATE",
                            color = NeuralAccent,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.8.sp
                        )
                        Text(
                            text = currentPredictionTitle,
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(NeuralAccent)
                            .clickable { onRunUnifiedInference() }
                            .padding(horizontal = 14.dp, vertical = 8.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            if (isGeneratingPrediction) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(14.dp),
                                    color = NeuralDeepPurple,
                                    strokeWidth = 2.dp
                                )
                            } else {
                                Icon(
                                    imageVector = AppIcons.AutoAwesome,
                                    contentDescription = "Predict",
                                    tint = NeuralDeepPurple,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                            Text(
                                text = if (isGeneratingPrediction) "Fusing..." else "PREDICT",
                                color = NeuralDeepPurple,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                // Intent Explanation and Synthesis
                Text(
                    text = currentPredictionText,
                    color = NeuralTextPrimary,
                    fontSize = 13.sp,
                    lineHeight = 19.sp
                )

                // Multimodal Adaptive Calibration Weights Display
                CalibrationWeightsSection(
                    weights = calibrationWeights
                )

                // Proactive Action Plan
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(NeuralDeepPurple.copy(alpha = 0.7f))
                        .border(1.dp, Color.White.copy(alpha = 0.08f), RoundedCornerShape(16.dp))
                        .padding(12.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "AUTOMATED PROACTIVE OS ACTIONS",
                                color = NeuralAccent,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Confidence: ${String.format(java.util.Locale.US, "%.1f", matchPercentage)}%",
                                color = NeuralTextSecondary,
                                fontSize = 10.sp
                            )
                        }

                        Text(
                            text = currentActionPlan,
                            color = NeuralTextPrimary,
                            fontSize = 12.sp,
                            lineHeight = 17.sp
                        )

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (isActionExecuted) NeuralAccent.copy(alpha = 0.2f) else NeuralAccent)
                                .clickable { isActionExecuted = true }
                                .padding(vertical = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = if (isActionExecuted) "✓ Proactive Plan Executed to System" else "⚡ Execute Proactive Action Plan",
                                color = if (isActionExecuted) NeuralAccent else NeuralDeepPurple,
                                fontSize = 11.sp,
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
                            .padding(vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "✓ Accurate (+Reinforce)",
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
                            .padding(vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "⚡ Recalibrate (Shift Weights)",
                            color = if (selectedFeedback == false) NeuralDeepPurple else NeuralTextPrimary,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }

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
                    Icon(imageVector = AppIcons.RecordVoiceOver, contentDescription = "Subvocal", tint = Color(0xFF00E5FF), modifier = Modifier.size(16.dp))
                    Text("SILENT SUBVOCAL SPEECH STREAM", color = Color(0xFF00E5FF), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFF00E5FF).copy(alpha = 0.15f))
                        .clickable { onTriggerNewWord() }
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text("⚡ Sample Phoneme", color = Color(0xFF00E5FF), fontSize = 9.sp, fontWeight = FontWeight.Bold)
                }
            }

            Text(
                text = "Decoded Inner Voice: \"${subvocal.decodedPhrase}\"",
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
                    Text("PRE-ERROR ERN WAVE DETECTOR", color = borderColor, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(borderColor.copy(alpha = 0.15f))
                        .clickable { onSimulateCheck() }
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text("Scan Wave (-300ms)", color = borderColor, fontSize = 9.sp, fontWeight = FontWeight.Bold)
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
                HesitationMetricPill("ERN Peak (μV)", "${String.format(java.util.Locale.US, "%.1f", preError.ernWaveMagnitudeUv)} μV", Modifier.weight(1f))
                HesitationMetricPill("Pre-Error Risk", "${preError.preErrorProbabilityPct}%", Modifier.weight(1f))
                HesitationMetricPill("Intercept Window", "-${preError.timeToImpactMs} ms", Modifier.weight(1f))
                HesitationMetricPill("Interceptions", "${preError.preventedMistakesCount} Saved", Modifier.weight(1f))
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
                    Icon(imageVector = AppIcons.Hub, contentDescription = "Mind Graph", tint = Color(0xFF9D4EDD), modifier = Modifier.size(16.dp))
                    Text("SEMANTIC MIND ASSOCIATION GRAPH", color = Color(0xFF9D4EDD), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
                Text("Central: ${mindGraph.centralTopic}", color = NeuralTextSecondary, fontSize = 10.sp)
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
                    Icon(imageVector = AppIcons.Image, contentDescription = "Mental Imagery", tint = Color(0xFFFFD166), modifier = Modifier.size(16.dp))
                    Text("MENTAL IMAGERY RECONSTRUCTION", color = Color(0xFFFFD166), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFFFFD166).copy(alpha = 0.15f))
                        .clickable { onSynthesize("") }
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(if (imagery.isSynthesizing) "Rendering..." else "⚡ Synthesize", color = Color(0xFFFFD166), fontSize = 9.sp, fontWeight = FontWeight.Bold)
                }
            }

            Text(
                text = "Visualized Concept: \"${imagery.activeConcept}\"",
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
                    Text("Fidelity: ${imagery.visualFidelityPct}%", color = Color(0xFFFFD166), fontSize = 9.sp, fontWeight = FontWeight.Bold)
                    Text("θ-γ Coherence: ${String.format(java.util.Locale.US, "%.2f", imagery.thetaGammaCoherence)}", color = NeuralTextSecondary, fontSize = 9.sp)
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
                    Icon(imageVector = AppIcons.Mood, contentDescription = "Emotional Resonance", tint = valenceColor, modifier = Modifier.size(16.dp))
                    Text("EMOTIONAL RESONANCE & COGNITIVE FRICTION", color = valenceColor, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
                Text("${friction.cognitiveFrictionPct}% Friction", color = valenceColor, fontSize = 10.sp, fontWeight = FontWeight.Bold)
            }

            Text(
                text = "Resonance Mood: ${friction.dominantMood}",
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
                    Text("🧘 Flow Boost (+Valence)", color = Color(0xFF00FF66), fontSize = 9.sp, fontWeight = FontWeight.Bold)
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
                    Text("⚠️ Inject Friction", color = Color(0xFFFF5252), fontSize = 9.sp, fontWeight = FontWeight.Bold)
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
                    Icon(imageVector = AppIcons.AccountTree, contentDescription = "Decision Tree", tint = Color(0xFF9D4EDD), modifier = Modifier.size(16.dp))
                    Text("BRANCHING COGNITIVE DECISION TREE", color = Color(0xFF9D4EDD), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
                Text("3 Active Pathways", color = NeuralAccent, fontSize = 9.sp, fontWeight = FontWeight.Bold)
            }

            Text(
                text = "Multiverse Cognitive Branching: The mind evaluates parallel intent paths prior to physical motor commitment.",
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
                                    text = "${branch.probabilityPct}% Probability",
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
                                text = "Next Proactive Action: ${branch.nextAction}",
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
                    Text("SUBCONSCIOUS GHOST-TYPING ENGINE", color = Color(0xFF00E5FF), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFF00E5FF).copy(alpha = 0.15f))
                        .clickable { onCycle() }
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text("⚡ Cycle Thought", color = Color(0xFF00E5FF), fontSize = 9.sp, fontWeight = FontWeight.Bold)
                }
            }

            Text(
                text = "Telepathic Pre-Completion: Synthesizes entire expressions from sub-conscious intent buffers before keystrokes.",
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
                        Text("IDE CONVERGENCE STREAM", color = NeuralAccent, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                        Text("${ghostTyping.confidencePct}% Confidence", color = Color(0xFF00FF66), fontSize = 9.sp, fontWeight = FontWeight.Bold)
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
                    text = if (ghostTyping.isAccepted) "✓ Thought Expression Accepted into IDE" else "⚡ Accept Ghost Thought (Tab Key)",
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
                    Icon(imageVector = AppIcons.BatteryChargingFull, contentDescription = "Mental Energy", tint = energyColor, modifier = Modifier.size(16.dp))
                    Text("NEURO-FATIGUE & CLARITY SPECTRUM", color = energyColor, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(energyColor.copy(alpha = 0.15f))
                        .clickable { onRefresh() }
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text("⚡ Re-Scan θ/β", color = energyColor, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                }
            }

            Text(
                text = "Clarity Status: ${fatigue.clarityStatus}",
                color = NeuralTextPrimary,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold
            )

            // Mental Battery Indicator Bar
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Mental Battery Reserve", color = NeuralTextSecondary, fontSize = 10.sp)
                    Text("${fatigue.mentalEnergyPct}% Capacity (${fatigue.cognitiveEnduranceMinutes} mins reserve)", color = energyColor, fontSize = 10.sp, fontWeight = FontWeight.Bold)
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
                        Text("θ/β Power Ratio", color = NeuralTextSecondary, fontSize = 8.sp)
                        Text("${String.format(java.util.Locale.US, "%.2f", fatigue.thetaBetaRatio)} (Optimal < 2.0)", color = NeuralTextPrimary, fontSize = 10.sp, fontWeight = FontWeight.Bold)
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
                        Text("Recovery Directive", color = NeuralTextSecondary, fontSize = 8.sp)
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
                    Icon(imageVector = AppIcons.Timeline, contentDescription = "Timeline", tint = Color(0xFFFFD166), modifier = Modifier.size(16.dp))
                    Text("THOUGHT STREAM TIMELINE & LOGS", color = Color(0xFFFFD166), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
                Text("${timeline.historyLogs.size} Logged Thoughts", color = NeuralAccent, fontSize = 9.sp, fontWeight = FontWeight.Bold)
            }

            // Search Bar
            TextField(
                value = searchInput,
                onValueChange = {
                    searchInput = it
                    onSearch(it)
                },
                placeholder = { Text("Search decoded thoughts & intentions...", color = NeuralTextSecondary, fontSize = 11.sp) },
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
                                    text = "${item.confidencePct}% match",
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
                    Icon(imageVector = AppIcons.Waves, contentDescription = "Neuro-Entrainment", tint = Color(0xFF00FF66), modifier = Modifier.size(16.dp))
                    Text("AUDIO NEURO-ENTRAINMENT BEATS", color = Color(0xFF00FF66), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (entrainment.isPlaying) Color(0xFF00FF66).copy(alpha = 0.2f) else NeuralDeepPurple)
                        .clickable { onTogglePlay() }
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = if (entrainment.isPlaying) "🔊 ACTIVE BEATS" else "🔇 PAUSED",
                        color = if (entrainment.isPlaying) Color(0xFF00FF66) else NeuralTextSecondary,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Text(
                text = "Binaural Synaptic Alignment: Modulates carrier audio waves (${entrainment.carrierFrequencyHz} Hz) to stimulate targeted brainwave resonance (${entrainment.targetWaveHz} Hz).",
                color = NeuralTextSecondary,
                fontSize = 11.sp,
                lineHeight = 15.sp
            )

            Text(
                text = "Mode Effect: ${entrainment.entrainmentBenefit}",
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
                    "Alpha Flow (10 Hz)",
                    "Theta Creative (6 Hz)",
                    "Gamma Hyper-Focus (40 Hz)"
                ).forEach { mode ->
                    val isSelected = entrainment.activeFrequencyMode == mode
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
                    Icon(imageVector = AppIcons.AccessTime, contentDescription = "Time Horizons", tint = Color(0xFF00E5FF), modifier = Modifier.size(16.dp))
                    Text("MULTI-HORIZON INTENT PREDICTIONS", color = Color(0xFF00E5FF), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
                Text("3 Time Scales", color = NeuralTextSecondary, fontSize = 10.sp)
            }

            // Tabs for +30s / +5m / +30m
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                TimeHorizonPill("+30 SEC (Motor)", activeTab == "30s", Modifier.weight(1f)) { onTabSelected("30s") }
                TimeHorizonPill("+5 MIN (Task)", activeTab == "5m", Modifier.weight(1f)) { onTabSelected("5m") }
                TimeHorizonPill("+30 MIN (Energy)", activeTab == "30m", Modifier.weight(1f)) { onTabSelected("30m") }
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
                    "30s" -> Triple(Color(0xFF00FF66), "IMMEDIATE MOTOR REFLEX (+30 SEC)", horizons.horizon30Sec)
                    "5m" -> Triple(Color(0xFF00E5FF), "TASK-LEVEL WORKFLOW OBJECTIVE (+5 MIN)", horizons.horizon5Min)
                    else -> Triple(Color(0xFFFFD166), "SUBCONSCIOUS ENERGY & COGNITIVE TRAJECTORY (+30 MIN)", horizons.horizon30Min)
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
            Text("REINFORCED MULTIMODAL WEIGHTS (ITERATION #${weights.reinforcedIterations})", color = NeuralTextSecondary, fontSize = 9.sp, fontWeight = FontWeight.Bold)
            Text("${weights.calibrationConfidence}% Conf", color = NeuralAccent, fontSize = 9.sp, fontWeight = FontWeight.Bold)
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
            Text("Touch ${(weights.touchWeight * 100).toInt()}%", color = Color(0xFF00E5FF), fontSize = 9.sp)
            Text("Audio ${(weights.audioWeight * 100).toInt()}%", color = Color(0xFF00FF66), fontSize = 9.sp)
            Text("Vision ${(weights.visionWeight * 100).toInt()}%", color = Color(0xFFFFD166), fontSize = 9.sp)
            Text("Bio ${(weights.bioWeight * 100).toInt()}%", color = Color(0xFFFF5252), fontSize = 9.sp)
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
                    Icon(imageVector = AppIcons.Speed, contentDescription = "Hesitation", tint = Color(0xFF00FF66), modifier = Modifier.size(16.dp))
                    Text("MICRO-HESITATION & KINETIC LATENCY", color = Color(0xFF00FF66), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
                Text("${metrics.interTapLatencyMs} ms Latency", color = Color(0xFF00FF66), fontSize = 10.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
            }

            Text(
                text = "Typing Rhythm State: ${metrics.typingRhythmState}",
                color = NeuralTextPrimary,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                HesitationMetricPill("Hesitation Index", String.format(java.util.Locale.US, "%.2f", metrics.hesitationIndex), Modifier.weight(1f))
                HesitationMetricPill("Motor Jitter", "${metrics.motorJitterPct}%", Modifier.weight(1f))
                HesitationMetricPill("Total Taps", "$touchCount", Modifier.weight(1f))
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
                    Icon(imageVector = AppIcons.WbSunny, contentDescription = "Circadian", tint = Color(0xFFFFD166), modifier = Modifier.size(16.dp))
                    Text("CIRCADIAN & ENVIRONMENTAL CONTEXT", color = Color(0xFFFFD166), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
                Text("Battery: ${circadian.batteryPct}%", color = Color(0xFFFFD166), fontSize = 10.sp, fontWeight = FontWeight.Bold)
            }

            Text("Phase: ${circadian.timeOfDayPeriod} | Thermals: ${circadian.thermalState}", color = NeuralTextPrimary, fontSize = 11.sp)

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                HesitationMetricPill("Ambient Light", "${circadian.ambientLux} Lux", Modifier.weight(1f))
                HesitationMetricPill("Biological Rhythm", "$heartRate BPM", Modifier.weight(1f))
                HesitationMetricPill("Work Context", activeAppContext.take(12), Modifier.weight(1f))
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
                    Icon(imageVector = AppIcons.Build, contentDescription = "Sandbox", tint = NeuralAccent, modifier = Modifier.size(16.dp))
                    Text("PROACTIVE OS ACTION SANDBOX", color = NeuralAccent, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
                Text("Live Controls", color = NeuralTextSecondary, fontSize = 10.sp)
            }

            SandboxToggleRow("Do-Not-Disturb Focus Shield", sandbox.isDndActive) { onToggle("DND") }
            SandboxToggleRow("Display Blue-Light / Dimming Filter", sandbox.isDisplayDimmed) { onToggle("DISPLAY_DIM") }
            SandboxToggleRow("Spatial 432Hz Binaural Carrier Sound", sandbox.isBinauralAudioOn) { onToggle("BINAURAL") }
            SandboxToggleRow("Pre-warm Software Keyboard Buffers", sandbox.isImePrewarmed) { onToggle("IME_PREWARM") }
            SandboxToggleRow("Compute & CPU Thermal Governor Lock", sandbox.isThermalOptimized) { onToggle("THERMAL") }
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
                val nodeX = center.x + (radius * 0.85f * cos(angle)).toFloat()
                val nodeY = center.y + (radius * 0.85f * sin(angle)).toFloat()

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
                        contentDescription = "Core",
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
                text = "MULTIMODAL NEXUS CORE",
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
                text = "EEG: ${String.format(java.util.Locale.US, "%.1f", alphaHz)}Hz Alpha",
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
                text = "TOUCH: $touchCount Taps",
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
                text = "AUDIO: ${audioDb.toInt()} dB",
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
                text = "HEART: $heartRate BPM",
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
                text = "LIVE SENSORY & NEURAL STREAMS (INTERACTIVE)",
                color = NeuralAccent,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )
            Icon(
                imageVector = AppIcons.Settings,
                contentDescription = "Toggle Streams",
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
                                Icon(imageVector = AppIcons.TouchApp, contentDescription = "Touch", tint = Color(0xFF00E5FF), modifier = Modifier.size(16.dp))
                                Text("TOUCH KINEMATICS STREAM (TAP HERE)", color = Color(0xFF00E5FF), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                            Text("Taps: $touchTapsCount", color = NeuralTextPrimary, fontSize = 11.sp, fontFamily = FontFamily.Monospace)
                        }

                        Text(
                            text = "Last Touch Coordinates: $lastTouchCoords | Pressure: 0.72 | Velocity: 3.4 Hz",
                            color = NeuralTextSecondary,
                            fontSize = 11.sp,
                            fontFamily = FontFamily.Monospace
                        )

                        Text(
                            text = "Tap on this surface to stream live motor impulses to the prediction matrix.",
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
                                Text("EEG SUBCONSCIOUS BRAINWAVES", color = Color(0xFF9D4EDD), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                            Text("Load: $cognitiveLoadPct%", color = NeuralAccent, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }

                        Text(text = "Focus: $subconsciousFocusLevel", color = NeuralTextPrimary, fontSize = 11.sp)

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
                        Text("SIMULATED ACTIVE APP CONTEXT", color = NeuralTextSecondary, fontSize = 10.sp, fontWeight = FontWeight.Bold)

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
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Icon(imageVector = AppIcons.Speed, contentDescription = "Sensors", tint = Color(0xFF00E5FF), modifier = Modifier.size(16.dp))
                    Text("REAL HARDWARE SENSORS (IMU)", color = Color(0xFF00E5FF), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (sensors.isTracking) Color(0xFF00FF66).copy(alpha = 0.2f) else NeuralDeepPurple)
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = if (sensors.isTracking) "⚡ SENSOR MANAGER ACTIVE" else "STANDBY",
                        color = if (sensors.isTracking) Color(0xFF00FF66) else NeuralTextSecondary,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Text(
                text = "Live On-Device IMU: Measures physical hand micro-tremor (8–12 Hz) and physiological stability directly from your phone's accelerometer and gyroscope.",
                color = NeuralTextSecondary,
                fontSize = 11.sp,
                lineHeight = 15.sp
            )

            // Stability & Hand Tension Status
            Text(
                text = "Motor Status: ${sensors.handTensionLevel}",
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
                        Text("Micro-Tremor", color = NeuralTextSecondary, fontSize = 8.sp)
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
                        Text("Tremor Freq", color = NeuralTextSecondary, fontSize = 8.sp)
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
                        Text("Stability Index", color = NeuralTextSecondary, fontSize = 8.sp)
                        Text("${sensors.neuromuscularStabilityPct}%", color = stabilityColor, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            // Real X/Y/Z Vector telemetry
            Text(
                text = "Accel (X: ${String.format(java.util.Locale.US, "%.2f", sensors.accelX)}, Y: ${String.format(java.util.Locale.US, "%.2f", sensors.accelY)}, Z: ${String.format(java.util.Locale.US, "%.2f", sensors.accelZ)}) • Gyro Z: ${String.format(java.util.Locale.US, "%.2f", sensors.gyroZ)}",
                color = NeuralTextSecondary,
                fontSize = 9.sp,
                fontFamily = FontFamily.Monospace
            )
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
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Icon(imageVector = AppIcons.CameraFront, contentDescription = "Camera Gaze", tint = Color(0xFFFF52A2), modifier = Modifier.size(16.dp))
                    Text("LIVE CAMERA-X GAZE & FACIAL HUD", color = Color(0xFFFF52A2), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (gaze.isCameraActive) Color(0xFFFF52A2).copy(alpha = 0.2f) else NeuralDeepPurple)
                        .clickable { onToggleCamera() }
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = if (gaze.isCameraActive) "📷 ACTIVE CAM" else "📷 START CAMERA",
                        color = if (gaze.isCameraActive) Color(0xFFFF52A2) else NeuralTextSecondary,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Text(
                text = "Selfie-Camera Computer Vision: Evaluates facial radiance (rPPG pulse), blink frequency, pupil dilation score, and visual gaze vectors at 60 FPS.",
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
                        Text("GAZE VECTOR FOCUS", color = Color(0xFFFF52A2), fontSize = 9.sp, fontWeight = FontWeight.Bold)
                        Text("${gaze.gazeConfidencePct}% Gaze Lock", color = Color(0xFF00FF66), fontSize = 9.sp, fontWeight = FontWeight.Bold)
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
                        Text("Blink Rate", color = NeuralTextSecondary, fontSize = 8.sp)
                        Text("${gaze.eyeBlinkRatePerMin} / min", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
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
                        Text("Cognitive Dilation", color = NeuralTextSecondary, fontSize = 8.sp)
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
                        Text("rPPG Pulse", color = NeuralTextSecondary, fontSize = 8.sp)
                        Text("${gaze.opticalRadiancePulseBpm} BPM", color = Color(0xFF00FF66), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

