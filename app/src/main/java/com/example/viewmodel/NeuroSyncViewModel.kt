package com.example.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.BuildConfig
import com.example.data.AppDatabase
import com.example.data.PredictionEntity
import com.example.data.PredictionRepository
import com.example.util.PermissionHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.util.concurrent.TimeUnit
import kotlin.random.Random

data class Quadruple<out A, out B, out C, out D>(
    val first: A,
    val second: B,
    val third: C,
    val fourth: D
)

data class TelemetryState(
    val touchValue: Float = 0.85f,
    val audioValue: Float = 0.42f,
    val visualValue: Float = 0.91f,
    val motionValue: Float = 0.65f,
    val biometricsValue: Float = 0.78f,
    val neuralValue: Float = 0.98f
)

data class TimeHorizonPredictions(
    val horizon30Sec: String = "+30წმ: კურსორის პოზიციონირება და ტელეპათიური ავტო-შევსება.",
    val horizon5Min: String = "+5წთ: კოდის კომპილაცია და ტესტების გაშვება 0-შეფერხებით.",
    val horizon30Min: String = "+30წთ: უწყვეტი ალფა-ფოკუსის შენარჩუნება გადაღლის გარეშე."
)

data class MicroHesitationMetrics(
    val interTapLatencyMs: Long = 184L,
    val hesitationIndex: Float = 0.18f, // 0.0 to 1.0
    val motorJitterPct: Int = 12,
    val typingRhythmState: String = "მკაფიო და გადამწყვეტი რიტმი"
)

data class CircadianEnvironment(
    val timeOfDayPeriod: String = "დღის ენერგიის პიკი",
    val batteryPct: Int = 86,
    val thermalState: String = "ოპტიმალური (31.2°C)",
    val ambientLux: Int = 340
)

data class StreamCalibrationWeights(
    val neuralWeight: Float = 0.35f,
    val touchWeight: Float = 0.25f,
    val audioWeight: Float = 0.15f,
    val visionWeight: Float = 0.15f,
    val bioWeight: Float = 0.10f,
    val calibrationConfidence: Float = 98.4f,
    val reinforcedIterations: Int = 42
)

data class ActionSandboxState(
    val isDndActive: Boolean = true,
    val isDisplayDimmed: Boolean = false,
    val isBinauralAudioOn: Boolean = true,
    val isImePrewarmed: Boolean = true,
    val isThermalOptimized: Boolean = false
)

// 1. Silent Subvocal Speech Stream
data class SubvocalSpeechToken(
    val word: String,
    val probability: Float,
    val latencyOffsetMs: Int
)

data class SubvocalSpeechState(
    val isStreaming: Boolean = true,
    val decodedPhrase: String = "fun calculateNeuralConvergence(vector: FloatArray)...",
    val activeTokens: List<SubvocalSpeechToken> = listOf(
        SubvocalSpeechToken("fun", 0.98f, -120),
        SubvocalSpeechToken("calculateNeuralConvergence", 0.94f, -75),
        SubvocalSpeechToken("(vector:", 0.89f, -30),
        SubvocalSpeechToken("FloatArray)", 0.96f, 15)
    ),
    val phonemeFrequencyHz: Float = 24.8f
)

// 2. Mental Imagery Synthesis (Mind's Eye Visualizer)
data class MentalImageryState(
    val isSynthesizing: Boolean = false,
    val activeConcept: String = "მრავალშრიანი ნეირონული გრაფის ქსელი სინაფსური ხიდებით",
    val visualFidelityPct: Int = 94,
    val thetaGammaCoherence: Float = 0.88f,
    val imageryTags: List<String> = listOf("გრაფის კვანძები", "მრავალგანზომილებიანი", "ნეონის მატრიცა", "სინაფსური ნაკადი")
)

// 3. Pre-Error ERN (Error-Related Negativity) Wave Detector
data class PreErrorDetectionState(
    val ernWaveMagnitudeUv: Float = 4.2f, // microvolts (negativity peak)
    val preErrorProbabilityPct: Int = 8, // < 25% = safe, > 60% = pre-error imminent
    val timeToImpactMs: Int = 320,
    val suggestedIntervention: String = "შემოწმებულია: მოტორული ტრემორი ნორმაშია",
    val isImminentError: Boolean = false,
    val preventedMistakesCount: Int = 7
)

// 4. Semantic Mind Graph Node & Edge
data class SemanticMindNode(
    val id: String,
    val label: String,
    val category: String,
    val weight: Float,
    val xOffset: Float, // 0.0 to 1.0 relative
    val yOffset: Float  // 0.0 to 1.0 relative
)

data class SemanticMindGraphState(
    val centralTopic: String = "ნეირონული UI რეფაქტორინგი",
    val nodes: List<SemanticMindNode> = listOf(
        SemanticMindNode("1", "არქიტექტურა", "Core", 0.95f, 0.5f, 0.25f),
        SemanticMindNode("2", "წარმადობა", "Engine", 0.88f, 0.2f, 0.55f),
        SemanticMindNode("3", "Compose განლაგება", "UI", 0.92f, 0.8f, 0.55f),
        SemanticMindNode("4", "Room მონაცემთა ბაზა", "Persistence", 0.78f, 0.35f, 0.85f),
        SemanticMindNode("5", "BCI ნაკადი", "Telemetry", 0.96f, 0.65f, 0.85f)
    ),
    val activeNodeId: String = "1"
)

// 5. Emotional Resonance & Cognitive Friction Vector
data class EmotionalFrictionState(
    val valence: Float = 0.76f, // -1.0 (Frustrated) to +1.0 (Joy/Satisfaction)
    val arousal: Float = 0.62f, // 0.0 (Calm) to 1.0 (Excited/High Alert)
    val cognitiveFrictionPct: Int = 14, // Low friction = seamless flow
    val dominantMood: String = "ღრმა ჰარმონია და შემოქმედებითი მუხტი",
    val recommendedAdaptation: String = "შენარჩუნებულია შეუფერხებელი მუქი თემა და ყურადღების ფარი."
)

// 6. Branching Cognitive Decision Tree
data class DecisionBranch(
    val id: String,
    val title: String,
    val probabilityPct: Int,
    val description: String,
    val nextAction: String
)

data class CognitiveDecisionTreeState(
    val branches: List<DecisionBranch> = listOf(
        DecisionBranch("b1", "შტო A: კოდის რეფაქტორინგი და Compose ოპტიმიზაცია", 68, "ქვეცნობიერი განზრახვა მიმართულია Jetpack Compose ინტერფეისის დაჩქარებაზე.", "კომპილატორის ქეშის წინასწარ მომზადება"),
        DecisionBranch("b2", "შტო B: დოკუმენტაცია & ქართული ენის მოდელი", 24, "ქართული მორფოლოგიური ზმნების ნეირონული პარამეტრების შემოწმება.", "ქართული სუბვოკალური მოდელის დაქეშვა"),
        DecisionBranch("b3", "შტო C: გონებრივი განტვირთვა და ალფა-რელაქსაცია", 8, "კოგნიტური ტრაექტორია მიუთითებს ხანმოკლე 2-წუთიან დასვენების მზაობაზე.", "ბინაურალური 10Hz ტალღის ჩართვა")
    ),
    val activeBranchId: String = "b1"
)

// 7. Subconscious Ghost-Typing Engine
data class GhostTypingState(
    val isEnabled: Boolean = true,
    val ghostSuggestion: String = "fun შევამოწმოთკოგნიტურისინქრონიზაცია(ტალღა: FloatArray): Boolean { ... }",
    val typedPrefix: String = "fun შევამოწმოთკოგნიტური",
    val confidencePct: Int = 98,
    val isAccepted: Boolean = false
)

// 8. Neuro-Fatigue & Clarity Spectrum (Brain Fog Early Warning)
data class NeuroFatigueState(
    val mentalEnergyPct: Int = 84, // 0-100%
    val thetaBetaRatio: Float = 1.42f, // < 2.0 = High Clarity, > 3.0 = Brain Fog / Drowsiness
    val cognitiveEnduranceMinutes: Int = 54,
    val clarityStatus: String = "მაღალი მენტალური სისხარტე • 0 გადაღლა",
    val recoveryRecommendation: String = "აქტიურია ოპტიმალური ფოკუსის ზონა. მიკრო-შესვენება რეკომენდებულია 35 წუთში."
)

// 9. Thought Stream Timeline & Semantic Search History
data class ThoughtLogItem(
    val id: String,
    val timestamp: String,
    val title: String,
    val detail: String,
    val confidencePct: Int,
    val category: String
)

data class ThoughtTimelineState(
    val searchQuery: String = "",
    val historyLogs: List<ThoughtLogItem> = listOf(
        ThoughtLogItem("t1", "15:16:12", "UI რეფაქტორინგი და წარმადობის მატრიცა", "ამოცნობილია მომხმარებლის განზრახვა შეამციროს Compose რეკომპოზიციები.", 98, "Coding"),
        ThoughtLogItem("t2", "15:14:40", "სუბვოკალური ფონემა დეკოდირებულია", "შინაგანი ხმა აყალიბებს ცვლადების სახელებს ტენზორული ბუფერისთვის.", 94, "Inner Speech"),
        ThoughtLogItem("t3", "15:12:05", "შეცდომის პრევენცია (ERN)", "ERN ტალღამ დააფიქსირა მოტორული რყევა სინტაქსურ შეცდომამდე; განხორციელდა კორექცია.", 99, "Pre-Error"),
        ThoughtLogItem("t4", "15:08:30", "ღრმა ნაკადის არქიტექტურის სინთეზი", "მრავალშრიანი ასოციაციური ნეირონული გრაფის მოდელირება BCI ნაკადისთვის.", 96, "Design")
    )
)

// 10. Audio Neuro-Entrainment Beats Generator
data class NeuroEntrainmentState(
    val isPlaying: Boolean = true,
    val activeFrequencyMode: String = "ალფა-ნაკადი (10 Hz)",
    val carrierFrequencyHz: Int = 432,
    val targetWaveHz: Float = 10.0f,
    val volumePct: Int = 65,
    val entrainmentBenefit: String = "ასტიმულირებს მშვიდ კონცენტრაციასა და ალფა-ტალღების სინქრონიზაციას."
)

// 11. Galaxy Buds 2 & Ear-EEG In-Ear Integration
data class EarbudSensorState(
    val isConnected: Boolean = true,
    val deviceName: String = "Samsung Galaxy Buds 2",
    val batteryPct: Int = 92,
    val vpuBoneConductionHz: Float = 142.5f, // Voice Pickup Unit / ყბის მოძრაობისა და ძვლოვანი გამტარობის ვიბრაცია
    val headImuPitchDeg: Float = -2.8f,     // თავის დახრის კუთხე
    val headImuRollDeg: Float = 1.4f,
    val inEarAcousticOcclusionDb: Float = 16.4f, // ყურის არხის აკუსტიკური იზოლაცია / სუნთქვა
    val earEegSimulatedMicrovolts: Float = 14.2f, // სიმულირებული ყურის არხის ბიო-პოტენციალი (μV)
    val headNodDetected: Boolean = false,
    val isEarEegStreaming: Boolean = true,
    val earTipFitConfidencePct: Int = 98
)

data class EnhancedPupilGazeMetrics(
    val pupilDiameterMm: Float = 3.65f,
    val fixationDurationMs: Long = 420L,
    val eyeFixationZone: String = "ცენტრი • კოდის მატრიცაზე ფოკუსი",
    val cognitiveHesitationIndex: Float = 0.18f,
    val subvocalFrequencyHz: Float = 142.5f
)

data class NeuroSyncUiState(
    val isSyncing: Boolean = true,
    val isMasterActive: Boolean = true,
    val isContinuousThoughtStreamActive: Boolean = true,
    val thoughtUpdateIntervalSeconds: Int = 4,
    val lastThoughtUpdatedTimestamp: String = "ახლახანს",
    val matchPercentage: Float = 98.4f,
    val statusText: String = "ნეირონული კავშირი: 98.4% სიზუსტე",
    val touchTapsCount: Int = 14,
    val lastTouchCoords: String = "X: 342, Y: 812",
    val touchCadenceHz: Float = 3.4f,
    val touchPressure: Float = 0.72f,
    val audioDb: Float = 42.5f,
    val speakerOutputDb: Float = 68.0f,
    val cameraGazeX: Float = 0.48f,
    val cameraGazeY: Float = 0.32f,
    val motionTremor: Float = 1.2f,
    val tiltAngleDeg: Float = 14.0f,
    val heartRateBpm: Int = 74,
    val stressLevelPct: Int = 28,
    val activeAppContext: String = "Developer IDE & Neural Research",
    val currentPredictionTitle: String = "აზრების პროგნოზირება (Intent Prediction)",
    val currentPredictionText: String = "ნავარაუდევი აზრი: მომხმარებელი აყალიბებს განზრახვას გააუმჯობესოს კოდის არქიტექტურა და შეამოწმოს მომავალი დიზაინის ფუნქციონალი.",
    val currentActionPlan: String = "• მაღალი ფოკუსის კოგნიტური გარემოს შექმნა\n• IDE ბუფერების წინასწარი მომზადება სწრაფი აკრეფისთვის\n• გარე ხმაურის ფილტრაცია და ალფა-სინქრონიზაცია",
    val dominantMindThought: String = "კოდის არქიტექტურის ოპტიმიზაციის შემდეგი ნაბიჯის ფორმულირება",
    val thoughtCognitiveLoadPct: Int = 42,
    val subconsciousFocusLevel: String = "ღრმა ნაკადის მდგომარეობა (Alpha 10.2 Hz)",
    val alphaBandHz: Float = 10.2f,
    val betaBandHz: Float = 18.5f,
    val thetaBandHz: Float = 6.1f,
    val gammaBandHz: Float = 42.0f,
    val enhancedMetrics: EnhancedPupilGazeMetrics = EnhancedPupilGazeMetrics(),
    val timeHorizons: TimeHorizonPredictions = TimeHorizonPredictions(),
    val hesitationMetrics: MicroHesitationMetrics = MicroHesitationMetrics(),
    val circadian: CircadianEnvironment = CircadianEnvironment(),
    val calibrationWeights: StreamCalibrationWeights = StreamCalibrationWeights(),
    val sandboxActions: ActionSandboxState = ActionSandboxState(),
    val subvocalSpeech: SubvocalSpeechState = SubvocalSpeechState(),
    val mentalImagery: MentalImageryState = MentalImageryState(),
    val preErrorState: PreErrorDetectionState = PreErrorDetectionState(),
    val mindGraph: SemanticMindGraphState = SemanticMindGraphState(),
    val emotionalFriction: EmotionalFrictionState = EmotionalFrictionState(),
    val decisionTree: CognitiveDecisionTreeState = CognitiveDecisionTreeState(),
    val ghostTyping: GhostTypingState = GhostTypingState(),
    val neuroFatigue: NeuroFatigueState = NeuroFatigueState(),
    val thoughtTimeline: ThoughtTimelineState = ThoughtTimelineState(),
    val entrainment: NeuroEntrainmentState = NeuroEntrainmentState(),
    val earbudSensor: EarbudSensorState = EarbudSensorState(),
    val realSensors: com.example.sensor.RealHardwareSensorState = com.example.sensor.RealHardwareSensorState(),
    val cameraGaze: com.example.sensor.RealCameraGazeState = com.example.sensor.RealCameraGazeState(),
    val telemetry: TelemetryState = TelemetryState(),
    val isGeneratingPrediction: Boolean = false,
    val isPermissionsModalOpen: Boolean = false,
    val isExplanationModalOpen: Boolean = false,
    val micPermissionGranted: Boolean = false,
    val cameraPermissionGranted: Boolean = false,
    val notificationsGranted: Boolean = true,
    val usageStatsPermissionGranted: Boolean = false,
    val accessibilityPermissionGranted: Boolean = false,
    val overlayPermissionGranted: Boolean = false
)

class NeuroSyncViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: PredictionRepository
    val history: StateFlow<List<PredictionEntity>>

    val hardwareSensorManager = com.example.sensor.RealHardwareSensorManager(application)
    val cameraGazeAnalyzer = com.example.sensor.RealCameraGazeAnalyzer(application)

    private val _uiState = MutableStateFlow(NeuroSyncUiState())
    val uiState: StateFlow<NeuroSyncUiState> = _uiState.asStateFlow()

    private val httpClient = OkHttpClient.Builder()
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(15, TimeUnit.SECONDS)
        .build()

    init {
        val db = AppDatabase.getDatabase(application)
        repository = PredictionRepository(db.predictionDao())
        history = repository.allPredictions.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

        // Read persisted permissions immediately on startup
        refreshPermissions()

        // Seed initial history safely on IO dispatcher if empty
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val currentList = repository.allPredictions.first()
                if (currentList.isEmpty()) {
                    repository.insert(
                        PredictionEntity(
                            title = "კოგნიტური ფოკუსის რეჟიმი",
                            summary = "დაფიქსირებულია პროგრამირების აქტივობა და დაბალი აკუსტიკური ხმაური. რეკომენდებულია უწყვეტი სამუშაო სესია.",
                            matchConfidence = 98.4f,
                            touchActivityLevel = "მაღალი სიზუსტე (4.2 Hz)",
                            audioSpectrumDb = "28.5 dB (მშვიდი გარემო)",
                            visualContext = "Android Studio / Jetpack Compose",
                            neuralSyncRate = "98.4%",
                            actionPlan = "ფონური შეტყობინებები შეზღუდულია 45 წუთით"
                        )
                    )
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        startTelemetryLoop()

        // Start real hardware sensors (Accelerometer / Gyroscope)
        try {
            hardwareSensorManager.startListening()
            viewModelScope.launch {
                hardwareSensorManager.sensorState.collect { sensor ->
                    _uiState.update { current ->
                        current.copy(
                            realSensors = sensor,
                            motionTremor = sensor.microTremorMagnitude * 10f,
                            stressLevelPct = (100 - sensor.neuromuscularStabilityPct).coerceIn(10, 90)
                        )
                    }
                }
            }
            viewModelScope.launch {
                cameraGazeAnalyzer.gazeState.collect { gaze ->
                    _uiState.update { current ->
                        current.copy(
                            cameraGaze = gaze,
                            heartRateBpm = gaze.opticalRadiancePulseBpm,
                            cameraPermissionGranted = gaze.hasPermission || PermissionHelper.isCameraGranted(getApplication())
                        )
                    }
                }
            }
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }

    fun refreshPermissions() {
        val app = getApplication<Application>()
        _uiState.update {
            it.copy(
                micPermissionGranted = PermissionHelper.isMicGranted(app),
                cameraPermissionGranted = PermissionHelper.isCameraGranted(app),
                notificationsGranted = PermissionHelper.isNotificationsGranted(app),
                usageStatsPermissionGranted = PermissionHelper.isUsageStatsGranted(app),
                accessibilityPermissionGranted = PermissionHelper.isAccessibilityGranted(app),
                overlayPermissionGranted = PermissionHelper.isOverlayGranted(app),
                isMasterActive = PermissionHelper.isMasterSyncEnabled(app)
            )
        }
    }

    fun masterActivateAll(onNeedSystemPermissions: () -> Unit = {}) {
        val app = getApplication<Application>()
        PermissionHelper.setMasterSyncEnabled(app, true)
        PermissionHelper.setNotificationsGranted(app, true)
        
        // Check if runtime permissions need a trigger
        if (!PermissionHelper.isMicGranted(app) || !PermissionHelper.isCameraGranted(app)) {
            onNeedSystemPermissions()
        }

        _uiState.update { current ->
            current.copy(
                isSyncing = true,
                isMasterActive = true,
                statusText = "ნეირონული კავშირი: 99.2% (სრული სინქრონიზაცია)",
                matchPercentage = 99.2f,
                ghostTyping = current.ghostTyping.copy(isEnabled = true),
                entrainment = current.entrainment.copy(isPlaying = true),
                subvocalSpeech = current.subvocalSpeech.copy(isStreaming = true),
                sandboxActions = current.sandboxActions.copy(
                    isDndActive = true,
                    isBinauralAudioOn = true,
                    isImePrewarmed = true
                ),
                earbudSensor = current.earbudSensor.copy(isConnected = true, isEarEegStreaming = true)
            )
        }

        hardwareSensorManager.startListening()
        runNeuralPredictionInference()
    }

    fun masterDeactivateAll() {
        val app = getApplication<Application>()
        PermissionHelper.setMasterSyncEnabled(app, false)
        _uiState.update { current ->
            current.copy(
                isSyncing = false,
                isMasterActive = false,
                statusText = "ნეირონული კავშირი: შეჩერებულია",
                entrainment = current.entrainment.copy(isPlaying = false),
                ghostTyping = current.ghostTyping.copy(isEnabled = false)
            )
        }
    }

    fun toggleMasterSync(onNeedSystemPermissions: () -> Unit = {}) {
        if (_uiState.value.isMasterActive) {
            masterDeactivateAll()
        } else {
            masterActivateAll(onNeedSystemPermissions)
        }
    }

    fun toggleEarbudsConnected() {
        _uiState.update { current ->
            val nextState = !current.earbudSensor.isConnected
            current.copy(
                earbudSensor = current.earbudSensor.copy(
                    isConnected = nextState,
                    isEarEegStreaming = nextState
                )
            )
        }
    }

    fun recalibrateEarbuds() {
        _uiState.update { current ->
            current.copy(
                earbudSensor = current.earbudSensor.copy(
                    earTipFitConfidencePct = (96..99).random(),
                    vpuBoneConductionHz = 135f + Random.nextFloat() * 20f,
                    earEegSimulatedMicrovolts = 12f + Random.nextFloat() * 6f
                )
            )
        }
    }

    fun startCameraGazeTracking(lifecycleOwner: androidx.lifecycle.LifecycleOwner, surfaceProvider: androidx.camera.core.Preview.SurfaceProvider? = null) {
        cameraGazeAnalyzer.startCamera(lifecycleOwner, surfaceProvider)
        PermissionHelper.setCameraGranted(getApplication(), true)
        _uiState.update { it.copy(cameraPermissionGranted = true) }
    }

    fun stopCameraGazeTracking() {
        cameraGazeAnalyzer.stopCamera()
    }

    fun setCameraPermissionGranted(granted: Boolean) {
        PermissionHelper.setCameraGranted(getApplication(), granted)
        _uiState.update { it.copy(cameraPermissionGranted = granted) }
    }

    fun setNotificationsPermissionGranted(granted: Boolean) {
        PermissionHelper.setNotificationsGranted(getApplication(), granted)
        _uiState.update { it.copy(notificationsGranted = granted) }
    }

    override fun onCleared() {
        super.onCleared()
        hardwareSensorManager.stopListening()
        cameraGazeAnalyzer.stopCamera()
    }

    private fun startTelemetryLoop() {
        viewModelScope.launch {
            var thoughtPredictionCycleCounter = 0
            while (true) {
                delay(1000)
                if (_uiState.value.isSyncing) {
                    thoughtPredictionCycleCounter++
                    val newTouch = (0.7f + Random.nextFloat() * 0.28f)
                    val newAudio = if (_uiState.value.micPermissionGranted) {
                        (_uiState.value.audioDb / 100f).coerceIn(0.1f, 1.0f)
                    } else {
                        (0.3f + Random.nextFloat() * 0.4f)
                    }
                    val newVisual = (0.85f + Random.nextFloat() * 0.14f)
                    val newMotion = (0.5f + Random.nextFloat() * 0.45f)
                    val newBiometrics = (0.65f + Random.nextFloat() * 0.3f)
                    val newNeural = (0.95f + Random.nextFloat() * 0.04f)
                    val newMatch = 97.5f + Random.nextFloat() * 2.3f

                    val newGazeX = (0.2f + Random.nextFloat() * 0.6f)
                    val newGazeY = (0.2f + Random.nextFloat() * 0.6f)
                    val newHeartRate = 68 + Random.nextInt(18)
                    val newSpeakerDb = 50f + Random.nextFloat() * 35f
                    val newTremor = 0.5f + Random.nextFloat() * 2.2f

                    val newAlpha = 8f + Random.nextFloat() * 4f
                    val newBeta = 13f + Random.nextFloat() * 15f
                    val newTheta = 4f + Random.nextFloat() * 3.8f
                    val newGamma = 30f + Random.nextFloat() * 25f
                    val newCogLoad = (30 + (newHeartRate - 60) * 0.8 + (newTremor * 10)).coerceIn(15.0, 95.0).toInt()

                    val focusStates = listOf(
                        "ღრმა ნაკადის მდგომარეობა (Alpha ${String.format(java.util.Locale.US, "%.1f", newAlpha)} Hz)",
                        "აქტიური ამოცანის გადაჭრა (Beta ${String.format(java.util.Locale.US, "%.1f", newBeta)} Hz)",
                        "ქვეცნობიერი დამუშავება (Theta ${String.format(java.util.Locale.US, "%.1f", newTheta)} Hz)",
                        "მაღალი ინტუიციური პიკი (Gamma ${String.format(java.util.Locale.US, "%.1f", newGamma)} Hz)"
                    )

                    val matchStr = String.format(java.util.Locale.US, "%.1f", newMatch)
                    val matchVal = matchStr.toFloatOrNull() ?: 98.4f

                    val earbudUpdate = _uiState.value.earbudSensor.copy(
                        vpuBoneConductionHz = 130f + Random.nextFloat() * 25f,
                        headImuPitchDeg = -4f + Random.nextFloat() * 8f,
                        headImuRollDeg = -2f + Random.nextFloat() * 4f,
                        earEegSimulatedMicrovolts = 10f + Random.nextFloat() * 8f,
                        headNodDetected = Random.nextFloat() > 0.85f
                    )

                    val enhanced = EnhancedPupilGazeMetrics(
                        pupilDiameterMm = 3.2f + (_uiState.value.cameraGaze.opticalPupilDilationScore * 2.2f),
                        fixationDurationMs = _uiState.value.cameraGaze.fixationDurationMs,
                        eyeFixationZone = _uiState.value.cameraGaze.fixationZone,
                        cognitiveHesitationIndex = _uiState.value.hesitationMetrics.hesitationIndex,
                        subvocalFrequencyHz = earbudUpdate.vpuBoneConductionHz
                    )

                    _uiState.update { current ->
                        current.copy(
                            matchPercentage = matchVal,
                            statusText = "ნეირონული კავშირი: ${matchStr}% სიზუსტე",
                            cameraGazeX = newGazeX,
                            cameraGazeY = newGazeY,
                            heartRateBpm = newHeartRate,
                            speakerOutputDb = newSpeakerDb,
                            motionTremor = newTremor,
                            alphaBandHz = newAlpha,
                            betaBandHz = newBeta,
                            thetaBandHz = newTheta,
                            gammaBandHz = newGamma,
                            thoughtCognitiveLoadPct = newCogLoad,
                            subconsciousFocusLevel = focusStates.random(),
                            earbudSensor = earbudUpdate,
                            enhancedMetrics = enhanced,
                            telemetry = TelemetryState(
                                touchValue = newTouch,
                                audioValue = newAudio,
                                visualValue = newVisual,
                                motionValue = newMotion,
                                biometricsValue = newBiometrics,
                                neuralValue = newNeural
                            )
                        )
                    }

                    // 🌟 AUTO-UPDATE PREDICTED THOUGHT STREAM EVERY 3-4 SECONDS CONTINUOUSLY
                    val updateInterval = _uiState.value.thoughtUpdateIntervalSeconds
                    if (_uiState.value.isContinuousThoughtStreamActive && thoughtPredictionCycleCounter >= updateInterval) {
                        thoughtPredictionCycleCounter = 0
                        autoCyclePredictedThought()
                    }
                }
            }
        }
    }

    private fun autoCyclePredictedThought() {
        val state = _uiState.value
        val timeNow = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())
        
        val dynamicThoughts = listOf(
            Triple(
                "კოდის სტრუქტურის ოპტიმიზაცია და კომპოუზის აჩქარება",
                "ნავარაუდევი აზრი: გონებაში აყალიბებთ Jetpack Compose-ის მდგომარეობების რეფაქტორინგს და ეკრანის რენდერის ოპტიმიზაციას.",
                "• Compose Compiler მინიჭება\n• StateFlow რეაქტიული ბუფერის მომზადება\n• ალფა-ფოკუსის შენარჩუნება"
            ),
            Triple(
                "სუბვოკალური ენის დეკოდირება (Inner Speech)",
                "ნავარაუდევი აზრი: შინაგანი ხმით წარმოთქვამთ ცვლადების სახელებსა და შემდეგ ლოგიკურ პირობებს (${String.format(Locale.US, "%.1f", state.enhancedMetrics.subvocalFrequencyHz)} Hz VPU).",
                "• ქართული ფონემების კლასტერირება\n• კლავიატურის ბუფერის წინასწარი შევსება\n• აკუსტიკური ფილტრაცია"
            ),
            Triple(
                "თვალის გუგის დილატაცია & ვიზუალური ფოკუსი",
                "ნავარაუდევი აზრი: მზერა ფიქსირებულია '${state.enhancedMetrics.eyeFixationZone}'-ზე (${String.format(Locale.US, "%.2f", state.enhancedMetrics.pupilDiameterMm)}მმ გუგა); მიმდინარეობს ვიზუალური ინფორმაციის გაანალიზება.",
                "• ეკრანის კონტრასტის დაბალანსება\n• ყურადღების ფოკუსის გამოყოფა\n• ვიზუალური დაღლილობის პრევენცია"
            ),
            Triple(
                "Pre-Error (ERN) წინასწარი შეცდომის შეგრძნება",
                "ნავარაუდევი აზრი: ტვინის მოტორულმა ცენტრმა დააფიქსირა მიკრო-ყოყმანი (-180ms); გადაწყვეტილება გადამოწმების პროცესშია.",
                "• შეცდომის პრევენციის ბარიერი\n• სინტაქსური ავტო-კორექცია\n• მოტორული სტაბილიზაცია"
            ),
            Triple(
                "იდეის სინთეზი & ალგორითმული არქიტექტურა",
                "ნავარაუდევი აზრი: გამა ტალღების პიკი (${String.format(Locale.US, "%.1f", state.gammaBandHz)} Hz) ადასტურებს ახალი იდეის ან ალგორითმის სწრაფ გონებრივ მოდელირებას.",
                "• IDE ქეშის ინდექსირება\n• არაკრიტიკული აპლიკაციების გაჩუმება\n• სინაფსური მეხსიერების გაძლიერება"
            ),
            Triple(
                "Galaxy Buds 2 თავის კინემატიკა & თანხმობა",
                "ნავარაუდევი აზრი: თავის დახრის კუთხე (${String.format(Locale.US, "%.1f", state.earbudSensor.headImuPitchDeg)}°) და ყურის არხის EEG ადასტურებს თანხმობასა და მაღალ ყურადღებას.",
                "• Ear-EEG ბიოპოტენციალის სინქრონიზაცია\n• სივრცითი აუდიოს გააქტიურება\n• ტელემეტრიის დამახსოვრება"
            ),
            Triple(
                "კოგნიტური სიმშვიდე & ღრმა ალფა-ნაკადი",
                "ნავარაუდევი აზრი: დაბალი სტრესის მაჩვენებელი (${state.stressLevelPct}%) და 72 BPM პულსი მიუთითებს მენტალურ ჰარმონიასა და შემოქმედებით მუხტზე.",
                "• 10Hz ბინაურალური ტალღების მხარდაჭერა\n• ეკრანის მუქი თემის ადაპტაცია\n• პროდუქტიული ნაკადის დაცვა"
            )
        )

        val nextThought = dynamicThoughts.filter { it.first != state.currentPredictionTitle }.random()
        val newLog = ThoughtLogItem(
            id = "auto_${System.currentTimeMillis()}",
            timestamp = timeNow,
            title = nextThought.first,
            detail = nextThought.second,
            confidencePct = (96..99).random(),
            category = "Live Intent"
        )

        _uiState.update { current ->
            current.copy(
                currentPredictionTitle = nextThought.first,
                currentPredictionText = nextThought.second,
                currentActionPlan = nextThought.third,
                lastThoughtUpdatedTimestamp = timeNow,
                dominantMindThought = nextThought.first,
                thoughtTimeline = current.thoughtTimeline.copy(
                    historyLogs = (listOf(newLog) + current.thoughtTimeline.historyLogs).take(25)
                )
            )
        }
    }

    fun toggleContinuousThoughtStream() {
        _uiState.update { it.copy(isContinuousThoughtStreamActive = !it.isContinuousThoughtStreamActive) }
    }

    fun setThoughtUpdateInterval(seconds: Int) {
        _uiState.update { it.copy(thoughtUpdateIntervalSeconds = seconds.coerceIn(2, 10)) }
    }

    private var lastTapTimestamp = System.currentTimeMillis()

    fun toggleSyncing() {
        _uiState.update { it.copy(isSyncing = !it.isSyncing) }
    }

    fun registerTouchTap(x: Float, y: Float) {
        val now = System.currentTimeMillis()
        val latency = (now - lastTapTimestamp).coerceIn(40L, 2500L)
        lastTapTimestamp = now

        val newCount = _uiState.value.touchTapsCount + 1
        val hesitationScore = (latency / 1200f).coerceIn(0.05f, 0.95f)
        val jitter = ((latency % 100) / 3).coerceIn(5L, 35L).toInt()

        val rhythmState = when {
            latency < 160L -> "ულტრა-სწრაფი უწყვეტი ნაკადი"
            latency < 350L -> "მკაფიო და გადამწყვეტი რიტმი"
            latency < 800L -> "მიკრო-დაყოვნება / ფიქრი"
            else -> "კოგნიტური გადაწყვეტილების პაუზა"
        }

        _uiState.update {
            it.copy(
                touchTapsCount = newCount,
                lastTouchCoords = "X: ${x.toInt()}, Y: ${y.toInt()}",
                hesitationMetrics = MicroHesitationMetrics(
                    interTapLatencyMs = latency,
                    hesitationIndex = hesitationScore,
                    motorJitterPct = jitter,
                    typingRhythmState = rhythmState
                )
            )
        }
    }

    fun applyFeedbackCalibration(isAccurate: Boolean) {
        _uiState.update { current ->
            val prevWeights = current.calibrationWeights
            val iterations = prevWeights.reinforcedIterations + 1
            if (isAccurate) {
                val newConf = (current.matchPercentage + 0.3f).coerceAtMost(99.8f)
                current.copy(
                    matchPercentage = newConf,
                    calibrationWeights = prevWeights.copy(
                        neuralWeight = (prevWeights.neuralWeight * 1.02f).coerceIn(0.2f, 0.5f),
                        touchWeight = (prevWeights.touchWeight * 1.02f).coerceIn(0.15f, 0.4f),
                        calibrationConfidence = newConf,
                        reinforcedIterations = iterations
                    )
                )
            } else {
                val newConf = (current.matchPercentage - 0.5f).coerceAtLeast(88.0f)
                current.copy(
                    matchPercentage = newConf,
                    calibrationWeights = prevWeights.copy(
                        neuralWeight = (prevWeights.neuralWeight * 0.96f).coerceIn(0.2f, 0.5f),
                        touchWeight = (prevWeights.touchWeight * 1.08f).coerceIn(0.15f, 0.4f),
                        audioWeight = (prevWeights.audioWeight * 1.05f).coerceIn(0.1f, 0.3f),
                        calibrationConfidence = newConf,
                        reinforcedIterations = iterations
                    )
                )
            }
        }
    }

    fun toggleSandboxAction(actionKey: String) {
        _uiState.update { current ->
            val sandbox = current.sandboxActions
            val updated = when (actionKey) {
                "DND" -> sandbox.copy(isDndActive = !sandbox.isDndActive)
                "DISPLAY_DIM" -> sandbox.copy(isDisplayDimmed = !sandbox.isDisplayDimmed)
                "BINAURAL" -> sandbox.copy(isBinauralAudioOn = !sandbox.isBinauralAudioOn)
                "IME_PREWARM" -> sandbox.copy(isImePrewarmed = !sandbox.isImePrewarmed)
                "THERMAL" -> sandbox.copy(isThermalOptimized = !sandbox.isThermalOptimized)
                else -> sandbox
            }
            current.copy(sandboxActions = updated)
        }
    }

    fun selectMindGraphNode(nodeId: String) {
        _uiState.update { current ->
            val graph = current.mindGraph
            val selected = graph.nodes.find { it.id == nodeId }
            if (selected != null) {
                current.copy(
                    mindGraph = graph.copy(activeNodeId = nodeId),
                    currentPredictionTitle = "აზროვნების გრაფის ფოკუსი: ${selected.label}",
                    currentPredictionText = "ნავარაუდევი განზრახვა: ასოციაციური კლასტერის ნავიგაცია '${selected.label}' (${selected.category}) სინაფსური წონით ${(selected.weight * 100).toInt()}%."
                )
            } else current
        }
    }

    fun synthesizeMentalImagery(conceptPrompt: String = "") {
        viewModelScope.launch {
            _uiState.update { it.copy(mentalImagery = it.mentalImagery.copy(isSynthesizing = true)) }
            delay(800)
            val concept = if (conceptPrompt.isNotBlank()) conceptPrompt else "მრავალშრიანი ნეირონული ტოპოლოგიის ვიზუალიზაცია"
            val tags = when {
                concept.contains("UI", ignoreCase = true) || concept.contains("დიზაინ", ignoreCase = true) ->
                    listOf("დიზაინ სისტემა", "Compose სტრუქტურა", "ფერთა პალიტრა", "ადაპტური ბადე")
                concept.contains("Code", ignoreCase = true) || concept.contains("კოდ", ignoreCase = true) ->
                    listOf("AST ხე", "Coroutines Flow", "ბაიტკოდის პაიპლაინი", "ასინქრონულობა")
                else ->
                    listOf("სინაფსური კვანძები", "ჰიპერ-ვექტორი", "კვანტური მანიფოლდი", "4D სივრცე")
            }
            _uiState.update {
                it.copy(
                    mentalImagery = it.mentalImagery.copy(
                        isSynthesizing = false,
                        activeConcept = concept,
                        visualFidelityPct = (91..99).random(),
                        thetaGammaCoherence = 0.85f + Random.nextFloat() * 0.12f,
                        imageryTags = tags
                    )
                )
            }
        }
    }

    fun triggerSubvocalSpeechWord(customPhrase: String = "") {
        val phrase = if (customPhrase.isNotBlank()) customPhrase else listOf(
            "დავაკონფიგურიროთ კამერის მზერის სენსორი და თვალის ხამხამი",
            "ახალი არქიტექტურული მოდელის ოპტიმიზაცია და კომპოუზის აჩქარება",
            "სუბვოკალური შინაგანი მეტყველების დეკოდირება და ტელეპათიური შეყვანა",
            "კოგნიტური დაღლილობის შემცირება და ალფა-ტალღების სინქრონიზაცია",
            "val synapticMatrix = Tensor.createFloatBuffer()",
            "გავაანალიზოთ ტვინის ალფა და ბეტა ტალღების თანაფარდობა"
        ).random()

        val tokens = phrase.split(" ").mapIndexed { index, word ->
            SubvocalSpeechToken(
                word = word,
                probability = (0.92f + Random.nextFloat() * 0.07f).coerceAtMost(0.99f),
                latencyOffsetMs = -150 + (index * 40)
            )
        }

        _uiState.update {
            it.copy(
                subvocalSpeech = it.subvocalSpeech.copy(
                    decodedPhrase = phrase,
                    activeTokens = tokens,
                    phonemeFrequencyHz = 24.5f + Random.nextFloat() * 7f
                )
            )
        }
    }

    fun simulateErnPreErrorCheck() {
        val shouldFlagImminent = Random.nextBoolean()
        val prob = if (shouldFlagImminent) (65..88).random() else (5..18).random()
        val ernUv = if (shouldFlagImminent) 8.5f + Random.nextFloat() * 4f else 2.5f + Random.nextFloat() * 2f
        val intervention = if (shouldFlagImminent) {
            "⚡ შეცდომა პრევენცირებულია! მოსალოდნელი შეცდომა ავტო-კორექტირდა კლავიშის აშვებამდე (-280ms)."
        } else {
            "✓ ოპტიმალური აკრეფის ტრაექტორია: მოტორული ანომალიები არ დაფიქსირებულა."
        }

        _uiState.update { current ->
            val prevCount = current.preErrorState.preventedMistakesCount
            current.copy(
                preErrorState = current.preErrorState.copy(
                    ernWaveMagnitudeUv = ernUv,
                    preErrorProbabilityPct = prob,
                    timeToImpactMs = if (shouldFlagImminent) 280 else 450,
                    suggestedIntervention = intervention,
                    isImminentError = shouldFlagImminent,
                    preventedMistakesCount = if (shouldFlagImminent) prevCount + 1 else prevCount
                )
            )
        }
    }

    fun modulateEmotionalValence(deltaValence: Float, deltaArousal: Float) {
        _uiState.update { current ->
            val prev = current.emotionalFriction
            val newValence = (prev.valence + deltaValence).coerceIn(-1.0f, 1.0f)
            val newArousal = (prev.arousal + deltaArousal).coerceIn(0.0f, 1.0f)
            val friction = if (newValence < 0f) (45..75).random() else (5..20).random()
            val mood = when {
                newValence > 0.4f && newArousal > 0.4f -> "მაღალი ინსაითი & შემოქმედებითი ენერგია"
                newValence > 0.4f -> "მშვიდი გააზრებული ნაკადი და ბალანსი"
                newValence < -0.2f && newArousal > 0.5f -> "კოგნიტური ფრიქცია და გადატვირთვა"
                else -> "ნეიტრალური ანალიტიკური ფიქრი"
            }
            val adaptation = if (newValence < 0f) {
                "⚠️ დაფიქსირებულია კოგნიტური ფრიქცია. აქტიურდება ფოკუსის აუდიო და მუქი ფონი."
            } else {
                "✨ ოპტიმალური კოგნიტური ნაკადის მდგომარეობა. ჩართულია ყურადღების ფარი."
            }

            current.copy(
                emotionalFriction = prev.copy(
                    valence = newValence,
                    arousal = newArousal,
                    cognitiveFrictionPct = friction,
                    dominantMood = mood,
                    recommendedAdaptation = adaptation
                )
            )
        }
    }

    fun selectDecisionBranch(branchId: String) {
        _uiState.update { current ->
            val branch = current.decisionTree.branches.find { it.id == branchId }
            if (branch != null) {
                current.copy(
                    decisionTree = current.decisionTree.copy(activeBranchId = branchId),
                    currentPredictionTitle = "შერჩეული განზრახვა: ${branch.title}",
                    currentPredictionText = "არჩეული შტო (${branch.probabilityPct}% ალბათობა): ${branch.description}",
                    currentActionPlan = "• შემდეგი ნაბიჯი: ${branch.nextAction}\n• ნეირონული გამტარობის კალიბრაცია შედეგისთვის."
                )
            } else current
        }
    }

    fun acceptGhostTyping() {
        _uiState.update { current ->
            val ghost = current.ghostTyping
            val acceptedText = ghost.ghostSuggestion
            val newLog = ThoughtLogItem(
                id = "t_${System.currentTimeMillis()}",
                timestamp = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date()),
                title = "ქვეცნობიერი აკრეფა მიღებულია",
                detail = acceptedText,
                confidencePct = ghost.confidencePct,
                category = "Ghost Typing"
            )
            current.copy(
                ghostTyping = ghost.copy(
                    isAccepted = true,
                    typedPrefix = acceptedText
                ),
                thoughtTimeline = current.thoughtTimeline.copy(
                    historyLogs = listOf(newLog) + current.thoughtTimeline.historyLogs
                )
            )
        }
    }

    fun cycleGhostSuggestion() {
        val suggestions = listOf(
            "fun შევამოწმოთკოგნიტურისინქრონიზაცია(ტალღა: FloatArray): Boolean { ... }",
            "მონაცემთა ბაზაში შევინახოთ დეკოდირებული აზრების ქრონოლოგია",
            "დავაკონფიგურიროთ კამერის მზერის ტრეკერი და თვალის ხამხამის სენსორი",
            "გავაანალიზოთ ტვინის ალფა და ბეტა ტალღების თანაფარდობა",
            "val synapticMatrix = Tensor.createFloatBuffer()"
        )
        _uiState.update { current ->
            val nextSuggestion = suggestions.filter { it != current.ghostTyping.ghostSuggestion }.random()
            val prefix = nextSuggestion.split(" ").take(2).joinToString(" ")
            current.copy(
                ghostTyping = current.ghostTyping.copy(
                    ghostSuggestion = nextSuggestion,
                    typedPrefix = prefix,
                    isAccepted = false,
                    confidencePct = (93..99).random()
                )
            )
        }
    }

    fun refreshNeuroFatigueCheck() {
        _uiState.update { current ->
            val theta = current.thetaBandHz
            val beta = current.betaBandHz
            val ratio = if (beta > 0) (theta / beta * 4f).coerceIn(0.8f, 3.8f) else 1.4f
            val energy = (100 - (ratio * 18).toInt()).coerceIn(25, 98)
            val status = when {
                ratio < 1.6f -> "მაღალი მენტალური სისხარტე • 0 გადაღლა"
                ratio < 2.5f -> "ზომიერი კოგნიტური დატვირთვა • სტაბილური ფოკუსი"
                else -> "⚠️ დაღლილობა დაფიქსირდა • რეკომენდებულია 3-წთ დასვენება"
            }
            val endurance = (energy * 0.7f).toInt()
            current.copy(
                neuroFatigue = current.neuroFatigue.copy(
                    mentalEnergyPct = energy,
                    thetaBetaRatio = ratio,
                    cognitiveEnduranceMinutes = endurance,
                    clarityStatus = status
                )
            )
        }
    }

    fun searchThoughtTimeline(query: String) {
        _uiState.update { current ->
            current.copy(
                thoughtTimeline = current.thoughtTimeline.copy(searchQuery = query)
            )
        }
    }

    fun toggleEntrainmentPlay() {
        _uiState.update { current ->
            current.copy(
                entrainment = current.entrainment.copy(isPlaying = !current.entrainment.isPlaying)
            )
        }
    }

    fun setEntrainmentMode(mode: String) {
        val (freq, carrier, benefit) = when (mode) {
            "ალფა-ნაკადი (10 Hz)" -> Triple(10.0f, 432, "ასტიმულირებს მშვიდ კონცენტრაციასა და ალფა-ტალღების სინქრონიზაციას.")
            "თეტა-კრეატივი (6 Hz)" -> Triple(6.0f, 528, "აძლიერებს ღრმა ინტუიციურ აზროვნებასა და ქვეცნობიერ კავშირებს.")
            "გამა-ჰიპერფოკუსი (40 Hz)" -> Triple(40.0f, 400, "მაღალი გამტარობის კოგნიტური ბმა, ლოგიკური ამოცანების სწრაფი გადაწყვეტა.")
            else -> Triple(10.0f, 432, "მშვიდი წონასწორობის ტალღა.")
        }
        _uiState.update { current ->
            current.copy(
                entrainment = current.entrainment.copy(
                    activeFrequencyMode = mode,
                    targetWaveHz = freq,
                    carrierFrequencyHz = carrier,
                    entrainmentBenefit = benefit
                )
            )
        }
    }

    fun updateAudioDb(db: Float) {
        _uiState.update { it.copy(audioDb = db) }
    }

    fun updateHeartRate(bpm: Int) {
        _uiState.update { it.copy(heartRateBpm = bpm) }
    }

    fun updateMotionTremor(tremor: Float) {
        _uiState.update { it.copy(motionTremor = tremor) }
    }

    fun updateCameraGaze(x: Float, y: Float) {
        _uiState.update { it.copy(cameraGazeX = x, cameraGazeY = y) }
    }

    fun setPermissionsModalOpen(open: Boolean) {
        _uiState.update { it.copy(isPermissionsModalOpen = open) }
    }

    fun setExplanationModalOpen(open: Boolean) {
        _uiState.update { it.copy(isExplanationModalOpen = open) }
    }

    fun setMicPermission(granted: Boolean) {
        PermissionHelper.setMicGranted(getApplication(), granted)
        _uiState.update { it.copy(micPermissionGranted = granted) }
    }

    fun setUsageStatsPermission(granted: Boolean) {
        PermissionHelper.setUsageStatsGranted(getApplication(), granted)
        _uiState.update { it.copy(usageStatsPermissionGranted = granted) }
    }

    fun setOverlayPermission(granted: Boolean) {
        PermissionHelper.setOverlayGranted(getApplication(), granted)
        _uiState.update { it.copy(overlayPermissionGranted = granted) }
    }

    fun setAccessibilityPermission(granted: Boolean) {
        PermissionHelper.setAccessibilityGranted(getApplication(), granted)
        _uiState.update { it.copy(accessibilityPermissionGranted = granted) }
    }

    fun setAppContext(contextName: String) {
        _uiState.update { it.copy(activeAppContext = contextName) }
    }

    fun injectStimulus(stimulusType: String) {
        when (stimulusType) {
            "CAFFEINE_SPIKE" -> {
                _uiState.update {
                    it.copy(
                        betaBandHz = 28.4f,
                        gammaBandHz = 48.2f,
                        thoughtCognitiveLoadPct = 68,
                        subconsciousFocusLevel = "მაღალი ბეტა პიკი (ჰიპერ-სიფხიზლე)",
                        currentPredictionText = "ნავარაუდევი აზრი: მომხმარებელი განიცდის დავალებების სწრაფი გადართვის იმპულსს და აჩქარებულ მენტალურ ტემპს.",
                        currentActionPlan = "• IDE ავტო-შევსების დაყოვნების განულება\n• ლურჯი ნათების შემცირება\n• მაღალტემპიანი ემბიენტ ტრეკის ჩართვა"
                    )
                }
            }
            "DEEP_BREATHING" -> {
                _uiState.update {
                    it.copy(
                        alphaBandHz = 11.8f,
                        thetaBandHz = 7.2f,
                        thoughtCognitiveLoadPct = 18,
                        subconsciousFocusLevel = "ღრმა ალფა ჰარმონია (11.8 Hz)",
                        currentPredictionText = "ნავარაუდევი აზრი: მომხმარებელი შედის მშვიდ პარასიმპათიკურ მდგომარეობაში გაზრდილი შემოქმედებითი უნარით.",
                        currentActionPlan = "• ინტერფეისის ფერთა შერბილება\n• არაკრიტიკული შეტყობინებების გაჩუმება\n• სივრცითი აკუსტიკური ნაკადის გააქტიურება"
                    )
                }
            }
            "COMPLEX_PROBLEM" -> {
                _uiState.update {
                    it.copy(
                        gammaBandHz = 52.0f,
                        betaBandHz = 24.1f,
                        thoughtCognitiveLoadPct = 84,
                        subconsciousFocusLevel = "გამა ინსაითის პიკი (52.0 Hz)",
                        currentPredictionText = "ნავარაუდევი აზრი: მომხმარებელი აანალიზებს რთულ ალგორითმულ დამოკიდებულებებსა და ლოგიკურ სტრუქტურას.",
                        currentActionPlan = "• აქტიური კოდის პანელის იზოლაცია\n• CPU პრიორიტეტის მინიჭება კომპილატორისთვის\n• გარე შეფერხებების სრული დაბლოკვა"
                    )
                }
            }
            "NOISE_MASKING" -> {
                _uiState.update {
                    it.copy(
                        thetaBandHz = 6.8f,
                        alphaBandHz = 10.4f,
                        thoughtCognitiveLoadPct = 32,
                        subconsciousFocusLevel = "იზოქრონული აკუსტიკური მასკირება",
                        currentPredictionText = "ნავარაუდევი აზრი: მომხმარებელი ეძებს მშვიდ მენტალურ სივრცეს გარემოს აკუსტიკური ყურადღების გაფანტვის გარეშე.",
                        currentActionPlan = "• თეთრი ხმაურის გენერირება\n• ოთახის რევერბერაციის ჰარმონიკების ფილტრაცია"
                    )
                }
            }
        }
    }

    fun decodeCustomThought(thoughtPrompt: String) {
        if (thoughtPrompt.isBlank()) return
        viewModelScope.launch {
            _uiState.update { it.copy(isGeneratingPrediction = true) }
            kotlinx.coroutines.delay(600)

            val isGeorgian = thoughtPrompt.any { it in '\u10A0'..'\u10FF' }

            val predictedText = if (isGeorgian) {
                "დეკოდირებული აზრი: მომხმარებელი ფიქრობს განახორციელოს - '$thoughtPrompt'."
            } else {
                "Decoded Mind Thought: User is mentally formulating intent around '$thoughtPrompt'."
            }

            val actionPlan = if (isGeorgian) {
                "• სამუშაო გარემოს ავტომატური მომზადება: '$thoughtPrompt'\n• ნეირონული BCI სენსორების მგრძნობელობის ოპტიმიზაცია\n• ფოკუსირებული აკუსტიკური ფონის გააქტიურება"
            } else {
                "• Pre-configuring workspace context for '$thoughtPrompt'\n• Adjusting BCI synaptic sensitivity\n• Optimizing ambient neural acoustic layer"
            }

            val title = if (isGeorgian) "აზრის დეკოდერი (BCI)" else "BCI Thought Decoder"

            _uiState.update {
                it.copy(
                    isGeneratingPrediction = false,
                    currentPredictionTitle = title,
                    currentPredictionText = predictedText,
                    currentActionPlan = actionPlan
                )
            }

            // Save decoded thought to database history
            repository.insert(
                PredictionEntity(
                    title = if (isGeorgian) "ამოცნობილი აზრი: $thoughtPrompt" else "Decoded Thought: $thoughtPrompt",
                    summary = predictedText,
                    matchConfidence = 96.8f,
                    touchActivityLevel = "BCI პირდაპირი ინტერფეისი",
                    audioSpectrumDb = "${_uiState.value.heartRateBpm} BPM / ქვეცნობიერი სინქრონი",
                    visualContext = _uiState.value.activeAppContext,
                    neuralSyncRate = "${String.format(java.util.Locale.US, "%.1f", _uiState.value.matchPercentage)}%",
                    actionPlan = actionPlan
                )
            )
        }
    }

    fun runNeuralPredictionInference() {
        if (_uiState.value.isGeneratingPrediction) return
        _uiState.update { it.copy(isGeneratingPrediction = true) }

        viewModelScope.launch {
            val apiKey = try {
                val field = BuildConfig::class.java.getField("GEMINI_API_KEY")
                (field.get(null) as? String) ?: ""
            } catch (e: Exception) {
                ""
            }

            if (apiKey.isNotBlank()) {
                val apiSuccess = fetchPredictionFromGemini(apiKey)
                if (!apiSuccess) {
                    generateLocalNeuralPrediction()
                }
            } else {
                delay(800)
                generateLocalNeuralPrediction()
            }
            _uiState.update { it.copy(isGeneratingPrediction = false) }
        }
    }

    private suspend fun fetchPredictionFromGemini(apiKey: String): Boolean = withContext(Dispatchers.IO) {
        try {
            val earbudInfo = "Galaxy Buds 2 VPU Bone Conduction: ${_uiState.value.earbudSensor.vpuBoneConductionHz}Hz, Head Tilt: ${_uiState.value.earbudSensor.headImuPitchDeg}°, In-Ear Acoustic: ${_uiState.value.earbudSensor.inEarAcousticOcclusionDb}dB"
            val prompt = """
                You are NeuroSync AI, an advanced unified real-time multimodal human intent predictor adapted for the Georgian language and cognition.
                Synthesize all active sensory, earbud telemetry, and neural simulation streams:
                - Subconscious Brainwave State: ${_uiState.value.subconsciousFocusLevel} (Alpha: ${_uiState.value.alphaBandHz}Hz, Beta: ${_uiState.value.betaBandHz}Hz, Theta: ${_uiState.value.thetaBandHz}Hz, Gamma: ${_uiState.value.gammaBandHz}Hz, Cognitive Load: ${_uiState.value.thoughtCognitiveLoadPct}%)
                - In-Ear & Head Kinematics: $earbudInfo
                - Kinematic Touch Stream: ${_uiState.value.touchTapsCount} taps at ${_uiState.value.lastTouchCoords} (Cadence: ${_uiState.value.touchCadenceHz}Hz, Pressure: ${_uiState.value.touchPressure})
                - Acoustic Spectrum: Ambient ${_uiState.value.audioDb} dB, Speaker ${_uiState.value.speakerOutputDb} dB
                - Ocular & Gaze Stream: Gaze Vector (${_uiState.value.cameraGazeX}, ${_uiState.value.cameraGazeY})
                - Physiological Biometrics & Inertia: Heart Rate ${_uiState.value.heartRateBpm} BPM, Tremor ${_uiState.value.motionTremor} m/s², Tilt ${_uiState.value.tiltAngleDeg}°
                - Foreground Application Context: ${_uiState.value.activeAppContext}
                
                Generate a unified intent prediction IN GEORGIAN LANGUAGE (ქართულ ენაზე) in JSON format with these exact keys:
                {
                  "title": "მოკლე სათაური ქართულად (3-5 სიტყვა)",
                  "summary": "დეტალური პროგნოზი ქართულად: მომხმარებლის შემდეგი ნავარაუდევი მოქმედება და კოგნიტური მდგომარეობა",
                  "actionPlan": "• 3 პუნქტიანი პროაქტიული მოქმედების გეგმა ქართულად"
                }
            """.trimIndent()

            val jsonBody = JSONObject().apply {
                put("contents", org.json.JSONArray().put(
                    JSONObject().put("parts", org.json.JSONArray().put(
                        JSONObject().put("text", prompt)
                    ))
                ))
            }

            val request = Request.Builder()
                .url("https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent?key=$apiKey")
                .post(jsonBody.toString().toRequestBody("application/json; charset=utf-8".toMediaType()))
                .build()

            val response = httpClient.newCall(request).execute()
            if (response.isSuccessful) {
                val respStr = response.body?.string() ?: ""
                val respJson = JSONObject(respStr)
                val text = respJson
                    .getJSONArray("candidates")
                    .getJSONObject(0)
                    .getJSONObject("content")
                    .getJSONArray("parts")
                    .getJSONObject(0)
                    .getString("text")

                val cleanJsonStr = text.substringAfter("{").substringBeforeLast("}")
                val parsed = JSONObject("{$cleanJsonStr}")

                val title = parsed.optString("title", "მულტიმოდალური განზრახვის პროგნოზი")
                val summary = parsed.optString("summary", "მომხმარებელი ემზადება აქტივობის გადასართველად. გარემო ოპტიმიზებულია.")
                val actionPlan = parsed.optString("actionPlan", "• კონტექსტის სინქრონიზაცია\n• ეკრანის კონტრასტის ოპტიმიზაცია\n• სწრაფი მოქმედებების მომზადება")

                val newPrediction = PredictionEntity(
                    title = title,
                    summary = summary,
                    matchConfidence = _uiState.value.matchPercentage,
                    touchActivityLevel = "${_uiState.value.touchTapsCount} შეხება (${_uiState.value.lastTouchCoords})",
                    audioSpectrumDb = "${String.format(java.util.Locale.US, "%.1f", _uiState.value.audioDb)} dB / ${_uiState.value.heartRateBpm} BPM",
                    visualContext = _uiState.value.activeAppContext,
                    neuralSyncRate = "${String.format(java.util.Locale.US, "%.1f", _uiState.value.matchPercentage)}%",
                    actionPlan = actionPlan
                )

                repository.insert(newPrediction)

                _uiState.update {
                    it.copy(
                        currentPredictionTitle = title,
                        currentPredictionText = summary,
                        currentActionPlan = actionPlan
                    )
                }
                return@withContext true
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return@withContext false
    }

    private suspend fun generateLocalNeuralPrediction() {
        val state = _uiState.value
        val isHighCognitive = state.thoughtCognitiveLoadPct > 55
        val isDeepFlow = state.alphaBandHz > 10.0f
        val isUrgentTouch = state.touchTapsCount > 20

        val (title, summary, actionPlan, horizons) = when {
            isHighCognitive && state.activeAppContext.contains("IDE") -> {
                Quadruple(
                    "ალგორითმული რეფაქტორინგი და კოდის ოპტიმიზაცია",
                    "გამა-ტალღების სიხშირე (${String.format(java.util.Locale.US, "%.1f", state.gammaBandHz)}Hz), Galaxy Buds VPU და მზერის კონცენტრაცია მიუთითებს Kotlin/Compose არქიტექტურის აქტიურ გონებრივ სინთეზზე.",
                    "• Kotlin ბაიტკოდის ქეშის ფონური წინასწარ კომპილაცია\n• შეტყობინებების დაბლოკვა და მაქსიმალური კონცენტრაციის რეჟიმი\n• ეკრანის ფერთა ტემპერატურის შერბილება თვალის დასაცავად",
                    TimeHorizonPredictions(
                        horizon30Sec = "+30წმ: კურსორის პოზიციონირება და ტელეპათიური ავტო-შევსება.",
                        horizon5Min = "+5წთ: კოდის კომპილაცია და ტესტების გაშვება 0-შეფერხებით.",
                        horizon30Min = "+30წთ: უწყვეტი ალფა-ფოკუსის შენარჩუნება გადაღლის გარეშე."
                    )
                )
            }
            isUrgentTouch && state.activeAppContext.contains("Messaging") -> {
                Quadruple(
                    "სწრაფი კომუნიკაცია და გადაუდებელი შეტყობინება",
                    "თაჩის აჩქარებული ტემპი (${state.touchTapsCount} შეხება) და ბეტა ტალღები (${String.format(java.util.Locale.US, "%.1f", state.betaBandHz)}Hz) ადასტურებს სწრაფი პასუხის გაგზავნის განზრახვას.",
                    "• კლავიატურის (IME) ბუფერის წინასწარი მომზადება\n• კონტექსტური ქართული პასუხების შეთავაზება\n• შეტყობინების გაგზავნის ქსელური პრიორიტეტის აწევა",
                    TimeHorizonPredictions(
                        horizon30Sec = "+30წმ: შეტყობინების გაგზავნა და შემდეგ დიალოგზე გადასვლა.",
                        horizon5Min = "+5წთ: მიმოწერის დასრულება და მთავარ სამუშაოზე დაბრუნება.",
                        horizon30Min = "+30წთ: ფონური სიჩუმის რეჟიმის ჩართვა გონების განსატვირთად."
                    )
                )
            }
            isDeepFlow -> {
                Quadruple(
                    "ღრმა შემოქმედებითი ფოკუსი და ალფა-ნაკადი",
                    "ალფა-ტალღების სინქრონია (${String.format(java.util.Locale.US, "%.1f", state.alphaBandHz)}Hz), მშვიდი აკუსტიკა (${String.format(java.util.Locale.US, "%.1f", state.audioDb)} dB) და 72 BPM პულსი ადასტურებს შეუფერხებელ ნაკადს.",
                    "• სისტემური შეტყობინებების გათიშვა 60 წუთით\n• 432Hz ბინაურალური ტალღების გენერირება\n• სესიის ავტომატური დამახსოვრება მონაცემთა ბაზაში",
                    TimeHorizonPredictions(
                        horizon30Sec = "+30წმ: უწყვეტი წრფივი ფოკუსირება ეკრანის არეზე.",
                        horizon5Min = "+5წთ: ღრმა კონცეპტუალური ანალიზი კონტექსტის გაფანტვის გარეშე.",
                        horizon30Min = "+30წთ: ალფა-ნაკადის დასრულება; მიკრო-შესვენების რეკომენდაცია."
                    )
                )
            }
            else -> {
                Quadruple(
                    "კონტექსტის შეცვლა და მრავალსენსორული სინქრონიზაცია",
                    "სენსორების კომბინაცია (${state.activeAppContext}, ${state.touchTapsCount} შეხება, ${state.heartRateBpm} BPM პულსი) მიუთითებს სამუშაო პროცესის შეცვლაზე.",
                    "• აქტიური სამუშაო გარემოს შენახვა ქეშში\n• გრაფიკული ბუფერის მომზადება აპლიკაციის გადასართავად\n• ნეირონული კალიბრაციის რეალურ დროში განახლება (+1.4%)",
                    TimeHorizonPredictions(
                        horizon30Sec = "+30წმ: აპლიკაციის გადართვის ჟესტი / ფანჯრის შემცირება.",
                        horizon5Min = "+5წთ: ახალი ამოცანის დაწყება და ტელემეტრიის რეკალიბრაცია.",
                        horizon30Min = "+30წთ: ბაზისური ნეირო-მაჩვენებლების ადაპტაცია ახალ ციკლზე."
                    )
                )
            }
        }

        val newPrediction = PredictionEntity(
            title = title,
            summary = summary,
            matchConfidence = state.matchPercentage,
            touchActivityLevel = "${state.touchTapsCount} შეხება (${state.lastTouchCoords})",
            audioSpectrumDb = "${String.format(java.util.Locale.US, "%.1f", state.audioDb)} dB / ${state.heartRateBpm} BPM",
            visualContext = state.activeAppContext,
            neuralSyncRate = "${String.format(java.util.Locale.US, "%.1f", state.matchPercentage)}%",
            actionPlan = actionPlan
        )

        repository.insert(newPrediction)

        _uiState.update {
            it.copy(
                currentPredictionTitle = title,
                currentPredictionText = summary,
                currentActionPlan = actionPlan,
                timeHorizons = horizons
            )
        }
    }

    fun clearHistory() {
        viewModelScope.launch {
            repository.clearAll()
        }
    }
}
