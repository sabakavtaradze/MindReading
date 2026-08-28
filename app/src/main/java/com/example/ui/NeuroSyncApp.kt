package com.example.ui

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.components.BehavioralPsychologyView
import com.example.ui.components.DigitalTwinView
import com.example.ui.components.DirectWordDecoderView
import com.example.ui.components.ExplanationModal
import com.example.ui.components.HeaderView
import com.example.ui.components.HeroNeuralOverlay
import com.example.ui.components.IntentPredictionCard
import com.example.ui.components.MindBandData
import com.example.ui.components.InteractiveTouchPad
import com.example.ui.components.MindLabView
import com.example.ui.components.PermissionsDialog
import com.example.ui.components.SynapticHistoryView
import com.example.ui.components.UnifiedSimulationActions
import com.example.ui.components.UnifiedSimulationMatrix
import com.example.ui.theme.AppIcons
import com.example.ui.theme.NeuralAccent
import com.example.ui.theme.NeuralBackground
import com.example.ui.theme.NeuralBorder
import com.example.ui.theme.NeuralDeepPurple
import com.example.ui.theme.NeuralSurface
import com.example.viewmodel.NeuroSyncViewModel

enum class NeuroTab { WORDS, UNIFIED_MATRIX, BEHAVIOR_PSYCH, DIGITAL_TWIN, MIND_LAB, SENSORS, LOGS }

@Composable
fun NeuroSyncApp(
    viewModel: NeuroSyncViewModel,
    onRequestMasterPermissions: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val historyList by viewModel.history.collectAsStateWithLifecycle()
    val savedCheckpoints by viewModel.digitalTwinCheckpoints.collectAsStateWithLifecycle()
    var activeTab by remember { mutableStateOf(NeuroTab.UNIFIED_MATRIX) }
    val lifecycleOwner = LocalLifecycleOwner.current

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        viewModel.setCameraPermissionGranted(isGranted)
        if (isGranted) {
            viewModel.startCameraGazeTracking(lifecycleOwner)
        }
    }

    val micPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        viewModel.setMicPermission(isGranted)
    }

    Scaffold(
        containerColor = NeuralBackground,
        bottomBar = {
            FooterBar(
                isSyncing = uiState.isSyncing,
                onPermissionsClick = { viewModel.setPermissionsModalOpen(true) },
                onSyncToggleClick = { viewModel.toggleSyncing() }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .statusBarsPadding()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(bottom = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Top Header with Master 1-Button Toggle
                HeaderView(
                    isSyncing = uiState.isSyncing,
                    subjectState = uiState.subjectRecognition,
                    onMasterToggleClick = { 
                        viewModel.masterActivateAll()
                        onRequestMasterPermissions()
                    }
                )

                // Hero Synaptic Overlay Visualizer
                HeroNeuralOverlay(
                    matchPercentage = uiState.matchPercentage,
                    statusText = uiState.statusText,
                    touchValue = uiState.telemetry.touchValue,
                    audioValue = uiState.telemetry.audioValue,
                    visualValue = uiState.telemetry.visualValue,
                    motionValue = uiState.telemetry.motionValue,
                    biometricsValue = uiState.telemetry.biometricsValue,
                    neuralValue = uiState.telemetry.neuralValue,
                    isSyncing = uiState.isSyncing,
                    onCenterCoreClick = { viewModel.runNeuralPredictionInference() },
                    modifier = Modifier.padding(horizontal = 24.dp)
                )

                // Section Navigation Tabs
                TabSelectorBar(
                    activeTab = activeTab,
                    onTabSelected = { activeTab = it },
                    onInfoClick = { viewModel.setExplanationModalOpen(true) },
                    modifier = Modifier.padding(horizontal = 24.dp)
                )

                // Tab Content Switcher
                Box(modifier = Modifier.padding(horizontal = 24.dp)) {
                    when (activeTab) {
                        NeuroTab.WORDS -> {
                            DirectWordDecoderView(
                                wordDecoderState = uiState.wordDecoder,
                                subjectState = uiState.subjectRecognition,
                                predictionState = uiState.wordPrediction,
                                onToggleLiveDecoding = { viewModel.toggleWordDecoding() },
                                onSetCategory = { viewModel.setWordDecoderLexiconCategory(it) },
                                onInjectWord = { word, cat -> viewModel.injectDecodedWord(word, cat) },
                                onClearSentence = { viewModel.clearDecodedSentence() },
                                onCycleNextWord = { viewModel.cycleNextDecodedWord() },
                                onSelectActiveSubject = { viewModel.setActiveSubject(it) },
                                onToggleTargetLock = { viewModel.toggleTargetLock() },
                                onToggleContaminationShield = { viewModel.toggleContaminationShield() },
                                onToggleAutoSwitch = { viewModel.toggleAutoSwitchSubject() },
                                onSimulateDetectedChange = { viewModel.simulateDetectedSubjectChange(it) },
                                onAddNewSubject = { name, title -> viewModel.addNewSubjectProfile(name, title) },
                                onTogglePreMotorPredictor = { viewModel.togglePreMotorPredictor() },
                                onApplyBranchPrediction = { viewModel.applyBranchPrediction(it) },
                                onRegeneratePredictionBranches = { viewModel.regenerateWordPredictionBranches() },
                                onToggleMarkovContext = { viewModel.toggleMarkovContextLearning() },
                                onToggleGazeDwell = { viewModel.toggleGazeDwellSelection() },
                                onToggleBilingual = { viewModel.toggleBilingualAutoFlip() },
                                onLearnMarkovPair = { prev, next -> viewModel.learnNewMarkovTransition(prev, next) },
                                onCycleScreenContext = { viewModel.cycleAppScreenContext() },
                                onToggleHrvCompensation = { viewModel.toggleHrvStressCompensation() },
                                onTogglePhoneticSnap = { viewModel.togglePhoneticNoiseSnap() },
                                onSimulateBioStress = { viewModel.simulateDynamicBioStress() },
                                onUpdateWeights = { ngram, time, bio, ctx -> viewModel.updateFusionWeights(ngram, time, bio, ctx) }
                            )
                        }
                        NeuroTab.UNIFIED_MATRIX -> {
                            UnifiedSimulationMatrix(
                                uiState = uiState,
                                actions = UnifiedSimulationActions(
                                    onRunUnifiedInference = { viewModel.runNeuralPredictionInference() },
                                    onToggleContinuousThought = { viewModel.toggleContinuousThoughtStream() },
                                    onSetUpdateInterval = { viewModel.setThoughtUpdateInterval(it) },
                                    onInjectStimulus = { viewModel.injectStimulus(it) },
                                    onAppContextChanged = { viewModel.setAppContext(it) },
                                    onTouchTap = { x, y -> viewModel.registerTouchTap(x, y) },
                                    onDecodeCustomThought = { viewModel.decodeCustomThought(it) },
                                    onApplyFeedback = { isAccurate -> viewModel.applyFeedbackCalibration(isAccurate) },
                                    onToggleSandboxAction = { actionKey -> viewModel.toggleSandboxAction(actionKey) },
                                    onTriggerSubvocal = { viewModel.triggerSubvocalSpeechWord() },
                                    onSynthesizeImagery = { prompt -> viewModel.synthesizeMentalImagery(prompt) },
                                    onCheckPreError = { viewModel.simulateErnPreErrorCheck() },
                                    onSelectGraphNode = { nodeId -> viewModel.selectMindGraphNode(nodeId) },
                                    onModulateMood = { dValence, dArousal -> viewModel.modulateEmotionalValence(dValence, dArousal) },
                                    onSelectDecisionBranch = { branchId -> viewModel.selectDecisionBranch(branchId) },
                                    onAcceptGhostTyping = { viewModel.acceptGhostTyping() },
                                    onCycleGhostSuggestion = { viewModel.cycleGhostSuggestion() },
                                    onRefreshFatigue = { viewModel.refreshNeuroFatigueCheck() },
                                    onSearchThoughtHistory = { query -> viewModel.searchThoughtTimeline(query) },
                                    onToggleEntrainment = { viewModel.toggleEntrainmentPlay() },
                                    onSetEntrainmentMode = { mode -> viewModel.setEntrainmentMode(mode) },
                                    onToggleEarbuds = { viewModel.toggleEarbudsConnected() },
                                    onRecalibrateEarbuds = { viewModel.recalibrateEarbuds() },
                                    onToggleWordDecoding = { viewModel.toggleWordDecoding() },
                                    onSetWordDecoderCategory = { viewModel.setWordDecoderLexiconCategory(it) },
                                    onInjectWordDecoderItem = { word, cat -> viewModel.injectDecodedWord(word, cat) },
                                    onClearWordDecoderSentence = { viewModel.clearDecodedSentence() },
                                    onCycleNextWordDecoderItem = { viewModel.cycleNextDecodedWord() },
                                    onSelectActiveSubject = { viewModel.setActiveSubject(it) },
                                    onToggleTargetLock = { viewModel.toggleTargetLock() },
                                    onToggleContaminationShield = { viewModel.toggleContaminationShield() },
                                    onToggleAutoSwitch = { viewModel.toggleAutoSwitchSubject() },
                                    onSimulateDetectedChange = { viewModel.simulateDetectedSubjectChange(it) },
                                    onAddNewSubject = { name, title -> viewModel.addNewSubjectProfile(name, title) },
                                    onTogglePreMotorPredictor = { viewModel.togglePreMotorPredictor() },
                                    onApplyBranchPrediction = { viewModel.applyBranchPrediction(it) },
                                    onRegeneratePredictionBranches = { viewModel.regenerateWordPredictionBranches() },
                                    onToggleMarkovContext = { viewModel.toggleMarkovContextLearning() },
                                    onToggleGazeDwell = { viewModel.toggleGazeDwellSelection() },
                                    onToggleBilingual = { viewModel.toggleBilingualAutoFlip() },
                                    onLearnMarkovPair = { prev, next -> viewModel.learnNewMarkovTransition(prev, next) },
                                    onCycleScreenContext = { viewModel.cycleAppScreenContext() },
                                    onToggleHrvCompensation = { viewModel.toggleHrvStressCompensation() },
                                    onTogglePhoneticSnap = { viewModel.togglePhoneticNoiseSnap() },
                                    onToggleMicroSaccade = { viewModel.toggleMicroSaccadeAnticipation() },
                                    onToggleNeuroGrammar = { viewModel.toggleNeuroGrammarTransformer() },
                                    onToggleEnergyPreserver = { viewModel.toggleCognitiveEnergyPreserver() },
                                    onTogglePhonemeCompression = { viewModel.toggleSubvocalPhonemeCompression() },
                                    onToggle3DNeuroSpatial = { viewModel.toggle3DNeuroSpatialFocusMap() },
                                    onToggleAffectiveTone = { viewModel.toggleAffectiveToneStylizer() },
                                    onCycleAffectiveTone = { viewModel.cycleAffectiveTone() },
                                    onToggleUnifiedEngine = { viewModel.toggleUnifiedIntelligenceEngine() },
                                    onSynthesizeUnifiedThought = { viewModel.synthesizeUnifiedThought() },
                                    onSimulateBioStress = { viewModel.simulateDynamicBioStress() },
                                    onUpdateWeights = { ngram, time, bio, ctx -> viewModel.updateFusionWeights(ngram, time, bio, ctx) },
                                    onMeasurePpgPulse = { viewModel.measurePpgPulseManual() },
                                    onTriggerPupilAha = { viewModel.triggerPupilAhaMoment() },
                                    onRecomputeBayesian = { viewModel.recomputeBayesianThought() },
                                    onApplyBayesianHypothesis = { hypothesis -> viewModel.applyBayesianHypothesis(hypothesis) },
                                    onTriggerCognitiveApnea = { viewModel.triggerCognitiveApnea() },
                                    onStepNextSubvocal = { viewModel.stepNextSubvocalThought() },
                                    onCycleSaliencyTarget = { viewModel.cycleSaliencyTarget() },
                                    onStepNextAssociativeConcept = { viewModel.stepNextAssociativeConcept() },
                                    onStepNextFacs = { viewModel.stepNextFacsMicroExpression() },
                                    onStepNextEmfSpatial = { viewModel.stepNextSpatialProfile() },
                                    onStepNextLatency = { viewModel.stepNextCognitiveLatency() },
                                    onStepNextFatigue = { viewModel.stepNextDecisionFatigue() },
                                    onToggleCamera = {
                                        if (uiState.cameraGaze.isCameraActive) {
                                            viewModel.stopCameraGazeTracking()
                                        } else {
                                            cameraPermissionLauncher.launch(android.Manifest.permission.CAMERA)
                                        }
                                    }
                                )
                            )
                        }
                        NeuroTab.BEHAVIOR_PSYCH -> {
                            BehavioralPsychologyView(
                                psychologyState = uiState.behavioralPsychology,
                                voiceState = uiState.voiceBiomarkers,
                                cantabState = uiState.cantabSpm,
                                wearablesState = uiState.wearablesSuite,
                                testState = uiState.psychTestState,
                                lslState = uiState.lslExportState,
                                onToggleDevice = { viewModel.toggleWearableDevice(it) },
                                onScanBle = { viewModel.triggerBleScan() },
                                onSwitchTestType = { viewModel.switchPsychTestType(it) },
                                onAnswerStroop = { viewModel.answerStroopTest(it) },
                                onTriggerGoNoGo = { viewModel.triggerGoNoGoAction(it) },
                                onAnswerIat = { viewModel.answerIatTest(it) },
                                onClickCantabBox = { viewModel.clickCantabBox(it) },
                                onToggleVoiceAnalysis = { viewModel.toggleVoiceAnalysis() },
                                onTriggerAcousticStress = { viewModel.triggerAcousticStressSample() },
                                onToggleLsl = { viewModel.toggleLslBroadcast() },
                                onSetLslFormat = { viewModel.setLslExportFormat(it) },
                                onExportLslData = { viewModel.exportLslDataPacket() },
                                onTriggerGsrPeak = { viewModel.triggerGsrStressPeak() },
                                onSimulateSystem1Or2 = { viewModel.simulateSystem1Or2Step() }
                            )
                        }
                        NeuroTab.DIGITAL_TWIN -> {
                            DigitalTwinView(
                                twinState = uiState.digitalTwin,
                                savedCheckpoints = savedCheckpoints,
                                onAdvanceDay = { viewModel.advanceDigitalTwinDay(it) },
                                onInjectSample = { viewModel.injectCustomDigitalTwinSample(it) },
                                onSaveCheckpoint = { viewModel.saveDigitalTwinCheckpoint() },
                                onTriggerDeepFineTuning = { viewModel.triggerDeepPersonaFineTuning() },
                                onDeleteCheckpoint = { viewModel.deleteDigitalTwinCheckpoint(it) }
                            )
                        }
                        NeuroTab.MIND_LAB -> {
                            MindLabView(
                                alphaBandHz = uiState.alphaBandHz,
                                betaBandHz = uiState.betaBandHz,
                                thetaBandHz = uiState.thetaBandHz,
                                gammaBandHz = uiState.gammaBandHz,
                                cognitiveLoadPct = uiState.thoughtCognitiveLoadPct,
                                subconsciousFocusLevel = uiState.subconsciousFocusLevel,
                                onInjectStimulus = { viewModel.injectStimulus(it) }
                            )
                        }
                        NeuroTab.SENSORS -> {
                            InteractiveTouchPad(
                                touchTapsCount = uiState.touchTapsCount,
                                lastTouchCoords = uiState.lastTouchCoords,
                                audioDb = uiState.audioDb,
                                activeAppContext = uiState.activeAppContext,
                                cameraGazeX = uiState.cameraGazeX,
                                cameraGazeY = uiState.cameraGazeY,
                                speakerOutputDb = uiState.speakerOutputDb,
                                motionTremor = uiState.motionTremor,
                                heartRateBpm = uiState.heartRateBpm,
                                realSensors = uiState.realSensors,
                                realAudio = uiState.realAudio,
                                cameraGaze = uiState.cameraGaze,
                                onTapRegistered = { x, y -> viewModel.registerTouchTap(x, y) },
                                onContextSelect = { viewModel.setAppContext(it) },
                                onAudioDbChange = { viewModel.updateAudioDb(it) },
                                onHeartRateChange = { viewModel.updateHeartRate(it) },
                                onMotionTremorChange = { viewModel.updateMotionTremor(it) },
                                onToggleCamera = {
                                    if (uiState.cameraGaze.isCameraActive) {
                                        viewModel.stopCameraGazeTracking()
                                    } else {
                                        cameraPermissionLauncher.launch(android.Manifest.permission.CAMERA)
                                    }
                                },
                                onRequestMasterPermissions = {
                                    viewModel.masterActivateAll()
                                    onRequestMasterPermissions()
                                }
                            )
                        }
                        NeuroTab.LOGS -> {
                            SynapticHistoryView(
                                historyList = historyList,
                                onClearClick = { viewModel.clearHistory() }
                            )
                        }
                    }
                }
            }
        }

        // Dialog Modals
        if (uiState.isPermissionsModalOpen) {
            PermissionsDialog(
                micGranted = uiState.micPermissionGranted,
                cameraGranted = uiState.cameraPermissionGranted,
                usageStatsGranted = uiState.usageStatsPermissionGranted,
                accessibilityGranted = uiState.accessibilityPermissionGranted,
                overlayGranted = uiState.overlayPermissionGranted,
                onMicToggle = { 
                    if (it) micPermissionLauncher.launch(android.Manifest.permission.RECORD_AUDIO)
                    else viewModel.setMicPermission(false)
                },
                onCameraToggle = {
                    if (it) cameraPermissionLauncher.launch(android.Manifest.permission.CAMERA)
                    else {
                        viewModel.setCameraPermissionGranted(false)
                        viewModel.stopCameraGazeTracking()
                    }
                },
                onUsageStatsToggle = { viewModel.setUsageStatsPermission(it) },
                onAccessibilityToggle = { viewModel.setAccessibilityPermission(it) },
                onOverlayToggle = { viewModel.setOverlayPermission(it) },
                onOpenExplanationClick = { viewModel.setExplanationModalOpen(true) },
                onDismissRequest = { viewModel.setPermissionsModalOpen(false) }
            )
        }

        if (uiState.isExplanationModalOpen) {
            ExplanationModal(
                onDismissRequest = { viewModel.setExplanationModalOpen(false) }
            )
        }
    }
}

@Composable
private fun TabSelectorBar(
    activeTab: NeuroTab,
    onTabSelected: (NeuroTab) -> Unit,
    onInfoClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(NeuralSurface)
            .border(1.dp, Color.White.copy(alpha = 0.08f), RoundedCornerShape(20.dp))
            .padding(6.dp)
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TabPillItem("სიტყვები", activeTab == NeuroTab.WORDS) {
            onTabSelected(NeuroTab.WORDS)
        }
        TabPillItem("მატრიცა", activeTab == NeuroTab.UNIFIED_MATRIX) {
            onTabSelected(NeuroTab.UNIFIED_MATRIX)
        }
        TabPillItem("🧬 ქცევა & BLE", activeTab == NeuroTab.BEHAVIOR_PSYCH) {
            onTabSelected(NeuroTab.BEHAVIOR_PSYCH)
        }
        TabPillItem("90 დღე", activeTab == NeuroTab.DIGITAL_TWIN) {
            onTabSelected(NeuroTab.DIGITAL_TWIN)
        }
        TabPillItem("ლაბი", activeTab == NeuroTab.MIND_LAB) {
            onTabSelected(NeuroTab.MIND_LAB)
        }
        TabPillItem("სენსორები", activeTab == NeuroTab.SENSORS) {
            onTabSelected(NeuroTab.SENSORS)
        }
        TabPillItem("ლოგი", activeTab == NeuroTab.LOGS) {
            onTabSelected(NeuroTab.LOGS)
        }

        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(NeuralDeepPurple)
                .clickable { onInfoClick() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = AppIcons.Info,
                contentDescription = "ინფორმაცია",
                tint = NeuralAccent,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}

@Composable
private fun TabPillItem(
    label: String,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .background(if (isSelected) NeuralAccent else Color.Transparent)
            .clickable { onClick() }
            .padding(horizontal = 14.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            fontSize = 12.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            color = if (isSelected) NeuralDeepPurple else Color.White
        )
    }
}

@Composable
private fun FooterBar(
    isSyncing: Boolean,
    onPermissionsClick: () -> Unit,
    onSyncToggleClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(horizontal = 24.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Left Permissions Button
        Box(
            modifier = Modifier
                .weight(1f)
                .height(52.dp)
                .clip(CircleShape)
                .background(NeuralSurface)
                .border(1.dp, NeuralBorder, CircleShape)
                .clickable { onPermissionsClick() },
            contentAlignment = Alignment.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = AppIcons.Settings,
                    contentDescription = "ნებართვები",
                    tint = NeuralAccent,
                    modifier = Modifier.size(18.dp)
                )
                Text(
                    text = "ნებართვები",
                    color = NeuralAccent,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        // Right Syncing Button
        Box(
            modifier = Modifier
                .weight(1f)
                .height(52.dp)
                .clip(CircleShape)
                .background(NeuralAccent)
                .clickable { onSyncToggleClick() },
            contentAlignment = Alignment.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = if (isSyncing) AppIcons.StopCircle else AppIcons.PlayCircle,
                    contentDescription = "სინქრონიზაცია",
                    tint = NeuralDeepPurple,
                    modifier = Modifier.size(18.dp)
                )
                Text(
                    text = if (isSyncing) "სინქრონიზაცია" else "დაპაუზებული",
                    color = NeuralDeepPurple,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
