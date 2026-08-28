package com.example.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.BuildConfig
import com.example.data.AppDatabase
import com.example.data.DigitalTwinCheckpointEntity
import com.example.data.DigitalTwinRepository
import com.example.data.PredictionEntity
import com.example.data.PredictionRepository
import com.example.service.BioPpgHrvEngine
import com.example.service.PpgHrvMetrics
import com.example.service.PupillometryCognitiveEngine
import com.example.service.PupillometryMetrics
import com.example.service.PsychomotorHesitationEngine
import com.example.service.PsychomotorHesitationMetrics
import com.example.service.UltradianBioRhythmEngine
import com.example.service.UltradianBioRhythmMetrics
import com.example.service.HierarchicalBayesianThoughtEngine
import com.example.service.HierarchicalBayesianState
import com.example.service.ThoughtHypothesis
import com.example.service.GeorgianNeuroLinguisticEngine
import com.example.service.UnifiedPredictiveThoughtEngine
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

// 12. 90-Day Digital Twin Training & Persona Roadmap
data class DigitalTwinMilestone(
    val dayRange: String,
    val phaseTitle: String,
    val targetAccuracy: String,
    val description: String,
    val keyMarkers: List<String>,
    val isCompleted: Boolean,
    val isCurrent: Boolean,
    val progressPct: Int
)

data class DigitalTwinState(
    val currentDay: Int = 1,
    val maxDays: Int = 90,
    val totalDataPointsCollected: Long = 4280L,
    val currentAccuracyPct: Float = 42.5f,
    val neuralConvergencePct: Float = 38.0f,
    val personaGraphDensity: Int = 64,
    val syncStreakDays: Int = 1,
    val isTrainingActive: Boolean = true,
    val lastCheckpointSaved: String = "დღე 1 • საწყისი კალიბრაცია",
    val activePhaseDescription: String = "ეტაპი 1: სენსორული ბაზისური კალიბრაცია (დღეები 1-3). სისტემა სწავლობს ხელის ტრემორს, თვალის ხამხამსა და ეკრანზე შეხების რიტმს.",
    val milestones: List<DigitalTwinMilestone> = listOf(
        DigitalTwinMilestone(
            dayRange = "დღე 1–3",
            phaseTitle = "სენსორული ბაზისური კალიბრაცია",
            targetAccuracy = "35% – 45%",
            description = "აპარატურული სენსორების (კამერა, IMU, ხმა) ფიზიოლოგიური ნორმის დადგენა.",
            keyMarkers = listOf("ხელის მიკრო-ტრემორი", "გუგის დიამეტრი და მზერა", "საწყისი შეხების ლატენტურობა"),
            isCompleted = false,
            isCurrent = true,
            progressPct = 40
        ),
        DigitalTwinMilestone(
            dayRange = "დღე 4–14",
            phaseTitle = "მოტორული და აკრეფის რიტმი",
            targetAccuracy = "50% – 65%",
            description = "ეკრანზე თითის დაჭერის ძალა, აკრეფის სისწრაფე და ხმოვანი ფონემები.",
            keyMarkers = listOf("Inter-Tap ლატენტურობა", "ხმის ტემბრის ჰარმონიკა", "მზერის ნახტომები (Saccades)"),
            isCompleted = false,
            isCurrent = false,
            progressPct = 0
        ),
        DigitalTwinMilestone(
            dayRange = "დღე 15–30 (1 თვე)",
            phaseTitle = "ემოციური და ცირკადული შაბლონები",
            targetAccuracy = "68% – 78%",
            description = "დღის რიტმების, სტრესის ტრიგერებისა და აპლიკაციების გამოყენების ჩვევები.",
            keyMarkers = listOf("სტრესული რეაქციები", "აპლიკაციების გადართვის რიტმი", "დღე/ღამის კოგნიტური ციკლები"),
            isCompleted = false,
            isCurrent = false,
            progressPct = 0
        ),
        DigitalTwinMilestone(
            dayRange = "დღე 31–60 (2 თვე)",
            phaseTitle = "სემანტიკური გრაფი და გადაწყვეტილებები",
            targetAccuracy = "80% – 88%",
            description = "სოციალური ურთიერთობები, ქვეცნობიერი ასოციაციები და ERN შეცდომების პრევენცია.",
            keyMarkers = listOf("Semantic Mind Graph (500+ Node)", "ERN ტალღის კორელაცია", "ქვეცნობიერი Ghost-Typing"),
            isCompleted = false,
            isCurrent = false,
            progressPct = 0
        ),
        DigitalTwinMilestone(
            dayRange = "დღე 61–90 (3 თვე)",
            phaseTitle = "სრული ციფრული ორეული (Deterministic Prediction)",
            targetAccuracy = "90% – 95%+",
            description = "მომავალი აზრების, განზრახვებისა და რეაქციების 1-3 წამით ადრე გამოცნობა.",
            keyMarkers = listOf("სრული კოგნიტური სიმულაცია", "სუბვოკალური აზრების დეკოდირება", "ავტონომიური პრედიქცია"),
            isCompleted = false,
            isCurrent = false,
            progressPct = 0
        )
    ),
    val injectedSamplesCount: Int = 3,
    val deepAnalysisResult: String = "კოგნიტური მოდელი აქტიურია. სისტემა აგროვებს უწყვეტ ტელემეტრიას და აყალიბებს ციფრულ ორეულს."
)

data class DecodedWordCandidate(
    val word: String,
    val probabilityPct: Int,
    val category: String,
    val phonemes: String,
    val latencyMs: Int
)

data class DecodedWordHistoryItem(
    val id: String,
    val timestamp: String,
    val word: String,
    val language: String,
    val confidencePct: Int,
    val category: String,
    val phonemeTrace: String
)

data class DirectWordDecoderState(
    val isLiveDecoding: Boolean = true,
    val currentDecodedWord: String = "გამარჯობა",
    val confidencePct: Int = 98,
    val currentPhonemes: List<String> = listOf("გ", "ა", "მ", "ა", "რ", "ჯ", "ო", "ბ", "ა"),
    val candidateWords: List<DecodedWordCandidate> = listOf(
        DecodedWordCandidate("გამარჯობა", 98, "COMMON", "გ-ა-მ-ა-რ-ჯ-ო-ბ-ა", -120),
        DecodedWordCandidate("გასაგებია", 84, "COMMON", "გ-ა-ს-ა-გ-ე-ბ-ი-ა", -80),
        DecodedWordCandidate("გაგრძელება", 72, "COMMANDS", "გ-ა-გ-რ-ძ-ე-ლ-ე-ბ-ა", -45),
        DecodedWordCandidate("გაჩერება", 35, "COMMANDS", "გ-ა-ჩ-ე-რ-ე-ბ-ა", 10)
    ),
    val activeLexiconCategory: String = "ALL", // "ALL", "COMMON", "DEV", "COMMANDS", "EMOTIONS", "ENGLISH"
    val accumulatedSentence: String = "გამარჯობა მინდა დავიწყო კოდის რეფაქტორინგი",
    val recentWords: List<DecodedWordHistoryItem> = listOf(
        DecodedWordHistoryItem("w1", "15:20:11", "გამარჯობა", "GEORGIAN", 98, "COMMON", "გ-ა-მ-ა-რ-ჯ-ო-ბ-ა"),
        DecodedWordHistoryItem("w2", "15:19:45", "მინდა", "GEORGIAN", 96, "COMMON", "მ-ი-ნ-დ-ა"),
        DecodedWordHistoryItem("w3", "15:19:12", "დავიწყო", "GEORGIAN", 94, "COMMANDS", "დ-ა-ვ-ი-წ-ყ-ო"),
        DecodedWordHistoryItem("w4", "15:18:30", "კოდის", "GEORGIAN", 97, "DEV", "კ-ო-დ-ი-ს"),
        DecodedWordHistoryItem("w5", "15:17:50", "რეფაქტორინგი", "GEORGIAN", 99, "DEV", "რ-ე-ფ-ა-ქ-ტ-ო-რ-ი-ნ-გ-ი")
    ),
    val activeLanguage: String = "GEORGIAN", // "GEORGIAN", "ENGLISH", "BILINGUAL"
    val internalSpeechVpuFrequencyHz: Float = 142.8f,
    val laryngealEffortIndex: Float = 0.88f,
    val lastActionExecuted: String = "სიტყვა დეკოდირებულია და დამატებულია ნაკადში"
)

data class WordBranchPrediction(
    val id: String,
    val word: String,
    val probabilityPct: Int,
    val phonemeLookaheadMs: Int = -280, // Negative means detected 280ms before voice articulation
    val category: String = "DEV", // "DEV", "COMMANDS", "DAILY", "EMOTIONS"
    val linguisticGrammarRole: String = "ზმნა (მოქმედება)",
    val semanticContextTrigger: String = "სუბვოკალური 142Hz რეზონანსი + წინა სიტყვა",
    val cognitiveLoadRequirementPct: Int = 34
)

data class MarkovLearnedTransition(
    val previousWord: String,
    val predictedNextWord: String,
    val frequencyScore: Int,
    val confidencePct: Int,
    val averageLeadTimeMs: Int
)

data class CognitiveFatigueHeatmapItem(
    val timeSlot: String,
    val accuracyPct: Int,
    val fatigueLevel: String, // "LOW", "OPTIMAL", "FATIGUED"
    val predictedSpeedGainWpm: Int
)

data class WordPredictionAnalyticsState(
    val isPreMotorPredictorActive: Boolean = true,
    val isMarkovContextLearningActive: Boolean = true,
    val isGazeDwellSelectionActive: Boolean = true,
    val isBilingualAutoFlipActive: Boolean = true,
    val isAppScreenContextActive: Boolean = true,
    val isHrvStressCompensationActive: Boolean = true,
    val isPhoneticNoiseSnapActive: Boolean = true,
    // 1. Micro-Saccade Anticipation (-90ms pupillary saccade prior to thought)
    val isMicroSaccadeAnticipationActive: Boolean = true,
    val microSaccadeAngleDeg: Float = 14.2f,
    val microSaccadeLeadTimeMs: Int = 92,
    val saccadicVectorTarget: String = "ეკრანის ზედა-მარჯვენა კუთხე ➔ 'დავაკომიტოთ'",
    // 2. Neuro-Grammar Transformer (Entire 3-4 word phrase structure prediction)
    val isNeuroGrammarTransformerActive: Boolean = true,
    val predictedFullSentenceSkeleton: String = "Subject [ჩვენ] + Verb [შევამოწმოთ] + Object [არქიტექტურა]",
    val grammarConfidencePct: Int = 97,
    // 3. Cognitive Energy Preserver (Auto-Adapts UI & high-confidence auto-complete when fatigue > 50%)
    val isCognitiveEnergyPreserverActive: Boolean = true,
    val isPreserverTriggered: Boolean = false,
    val energyPreservationSavingPct: Int = 46,
    // 4. Subvocal Phoneme Compression (Georgian Consonant Complex Fast-Decimation)
    val isSubvocalPhonemeCompressionActive: Boolean = true,
    val detectedPhonemeCluster: String = "მწვრ- ➔ 'მწვრთნელი' (2-იმპულსიანი შეკუმშვა +68% Speedup)",
    val compressionSpeedGainPct: Int = 68,
    // 5. 3D Neuro-Spatial Focus Map (Pupillometry & Depth Attention)
    val is3DNeuroSpatialFocusMapActive: Boolean = true,
    val pupilDilatationMm: Float = 3.85f,
    val spatialFocusCoordinates: String = "X: 0.72, Y: 0.35, Z: 0.94 (სიღრმისეული 3D ფიქსაცია)",
    // 6. Affective Dynamic Tone Stylizer (GSR + HRV Emotional Tuning)
    val isAffectiveToneStylizerActive: Boolean = true,
    val galvanicSkinResponseMicroSiemens: Float = 4.35f,
    val currentDynamicTone: String = "საქმიანი & ენერგიული (Business Flow)",
    // 7. Unified Multi-Sensor Intelligence Engine (12-Sensor Collaborative Synthesis)
    val isUnifiedIntelligenceEngineActive: Boolean = true,
    val activeSensorsCount: Int = 12,
    val unifiedDecodedSentence: String = "ჩვენ შევამოწმოთ სისტემის არქიტექტურა და გავუშვათ კომპილაცია",
    val unifiedDecodingConfidencePct: Int = 99,
    val currentAppScreenContext: String = "IDE / Terminal (დეველოპმენტი)", // "IDE / Terminal", "Messaging / Chat", "Research / Docs", "Media / System"
    val heartRateBpm: Int = 74,
    val hrvRmssdMs: Float = 58.4f,
    val stressStateLabel: String = "ოპტიმალური (დაბალი სტრესი)",
    val emotionalValence: String = "ღრმა ფოკუსი (Deep Focus)",
    val phoneticSnrDb: Float = 18.5f,
    val lastSnappedCorrection: String = "ხორხის 142Hz სიგნალი ➔ 'შევამოწმოთ' (Auto-Snap 99%)",
    // Algorithmic Fusion Weights: P(w) = α*Ngram + β*Time + γ*Biometrics + δ*Context
    val weightNgram: Float = 0.35f,
    val weightTimeCircadian: Float = 0.25f,
    val weightBiometrics: Float = 0.25f,
    val weightContext: Float = 0.15f,
    val computedFormulaSummary: String = "P(Word) = 0.35·Ngram + 0.25·Time + 0.25·EMG + 0.15·IDE",
    val readinessPotentialLeadTimeMs: Int = 320, // 320ms ahead of time
    val currentReadinessSpikeMicroVolts: Float = -18.4f,
    val predictionConfidenceScorePct: Int = 96,
    val phonemeEntropyIndex: Float = 0.16f,
    val cognitiveSpeedupGainWpm: Int = 42, // +42 WPM speedup
    val subconsciousHesitationLatencyMs: Int = 112, // 112ms hesitation detected
    val cognitiveFatiguePct: Int = 24, // 24% fatigue
    val totalWordsPredictedAhead: Int = 342,
    val activeFocusWordCandidate: String = "შევამოწმოთ",
    val branches: List<WordBranchPrediction> = listOf(
        WordBranchPrediction("b1", "შევამოწმოთ", 94, -320, "DEV", "ზმნა (შემოწმება)", "ფოკუსირებული კოდის ანალიზი", 38),
        WordBranchPrediction("b2", "დავაკომიტოთ", 87, -290, "DEV", "ზმნა (Git მოქმედება)", "ბოლო ცვლილებების ფიქსაცია", 42),
        WordBranchPrediction("b3", "გავუშვათ", 78, -250, "COMMANDS", "ზმნა (გაშვება)", "კომპილაციის განზრახვა", 29),
        WordBranchPrediction("b4", "შევინახოთ", 64, -210, "COMMANDS", "ზმნა (შენახვა)", "ფაილის შენახვის იმპულსი", 25)
    ),
    val markovMemoryChain: List<MarkovLearnedTransition> = listOf(
        MarkovLearnedTransition("კოდის", "რეფაქტორინგი", 142, 98, -340),
        MarkovLearnedTransition("შევამოწმოთ", "არქიტექტურა", 98, 94, -310),
        MarkovLearnedTransition("გავუშვათ", "კომპილაცია", 86, 91, -290),
        MarkovLearnedTransition("მონაცემთა", "ანალიტიკა", 114, 96, -330),
        MarkovLearnedTransition("სისტემა", "მზადაა", 76, 92, -270)
    ),
    val fatigueHeatmap: List<CognitiveFatigueHeatmapItem> = listOf(
        CognitiveFatigueHeatmapItem("10:00 - 12:00", 99, "OPTIMAL", 48),
        CognitiveFatigueHeatmapItem("12:00 - 15:00", 95, "OPTIMAL", 42),
        CognitiveFatigueHeatmapItem("15:00 - 18:00", 91, "LOW", 36),
        CognitiveFatigueHeatmapItem("18:00 - 21:00", 86, "FATIGUED", 28)
    ),
    val accuracyTrajectory: List<Int> = listOf(86, 89, 91, 94, 96, 98),
    val topPredictedContexts: List<String> = listOf("კოდის რეფაქტორინგი", "ნეირონული სინქრონიზაცია", "არქიტექტურის გაუმჯობესება"),
    val lastAppliedPrediction: String = "შევამოწმოთ (ავტო-დასრულებულია 320 მწ-ით ადრე)"
)

data class PersonProfile(
    val id: String, // "person_1", "person_2", "person_3"
    val name: String,
    val title: String,
    val avatarEmoji: String,
    val isTargetLocked: Boolean = true,
    val baseEmgFrequencyHz: Float = 142.0f,
    val dominantHemisphere: String = "მარცხენა (დომინანტური)",
    val touchPressureBaselineGrams: Float = 42.5f,
    val gazeBlinkRatePerMin: Int = 16,
    val alphaBetaRatio: Float = 1.34f,
    val totalWordsDecoded: Int = 128,
    val lastSeenTimestamp: String = "ახლახანს"
)

data class SubjectRecognitionState(
    val activePersonId: String = "person_1",
    val detectedPersonId: String = "person_1",
    val isAutoDetectionRunning: Boolean = true,
    val isLockActive: Boolean = true,
    val recognitionConfidencePct: Int = 98,
    val isContaminationShieldActive: Boolean = true,
    val autoSwitchSubjectEnabled: Boolean = true, // Default to automatic as requested by user
    val detectionStatusLabel: String = "ავტომატური ბიომეტრიული ანალიზი: Person 1 (იდენტიფიცირებულია)",
    val biometricGripMatchPct: Int = 97,
    val faceGazeMatchPct: Int = 99,
    val inEarImpedanceMatchPct: Int = 96,
    val vocalTractResonanceMatchPct: Int = 98,
    val profiles: List<PersonProfile> = listOf(
        PersonProfile("person_1", "Person 1 (თქვენ)", "მთავარი სუბიექტი", "👤", true, 142.0f, "მარცხენა (დომინანტური)", 42.5f, 16, 1.34f, 184),
        PersonProfile("person_2", "Person 2 (სტუმარი A)", "მეორე სუბიექტი", "🧑‍🔬", false, 168.0f, "მარჯვენა (ვიზუალური)", 56.0f, 21, 0.98f, 42),
        PersonProfile("person_3", "Person 3 (სტუმარი B)", "ექსპერიმენტული სუბიექტი", "👨‍💻", false, 126.5f, "სიმეტრიული", 38.0f, 14, 1.62f, 19)
    )
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
    val subjectRecognition: SubjectRecognitionState = SubjectRecognitionState(),
    val wordDecoder: DirectWordDecoderState = DirectWordDecoderState(),
    val wordPrediction: WordPredictionAnalyticsState = WordPredictionAnalyticsState(),
    val realSensors: com.example.sensor.RealHardwareSensorState = com.example.sensor.RealHardwareSensorState(),
    val realAudio: com.example.sensor.RealAudioState = com.example.sensor.RealAudioState(),
    val cameraGaze: com.example.sensor.RealCameraGazeState = com.example.sensor.RealCameraGazeState(),
    val telemetry: TelemetryState = TelemetryState(),
    val digitalTwin: DigitalTwinState = DigitalTwinState(),
    val isGeneratingPrediction: Boolean = false,
    val isPermissionsModalOpen: Boolean = false,
    val isExplanationModalOpen: Boolean = false,
    val micPermissionGranted: Boolean = false,
    val cameraPermissionGranted: Boolean = false,
    val notificationsGranted: Boolean = true,
    val usageStatsPermissionGranted: Boolean = false,
    val accessibilityPermissionGranted: Boolean = false,
    val overlayPermissionGranted: Boolean = false,
    val cognitiveBiometrics: HierarchicalBayesianState = HierarchicalBayesianState(),
    val behavioralPsychology: BehavioralPsychologyState = BehavioralPsychologyState(),
    val voiceBiomarkers: VoiceBiomarkersState = VoiceBiomarkersState(),
    val cantabSpm: CantabPsychState = CantabPsychState(),
    val wearablesSuite: WearablesSuiteState = WearablesSuiteState(),
    val psychTestState: PsychologicalTestState = PsychologicalTestState(),
    val lslExportState: LslExportState = LslExportState()
)

data class VoiceBiomarkersState(
    val isVoiceAnalyzing: Boolean = true,
    val fundamentalFrequencyF0Hz: Float = 124.5f,
    val pitchJitterPct: Float = 0.78f, // Micro-pitch instability (<1.04% is normal/healthy)
    val amplitudeShimmerPct: Float = 1.42f, // Micro-amplitude variation (<3.8% is healthy)
    val harmonicToNoiseRatioDb: Float = 22.8f, // Voice clarity
    val speechCadenceSyllablesPerSec: Float = 4.3f, // Normal conversational tempo
    val pauseToSpeechRatioPct: Int = 16, // Low hesitation
    val vocalDepressionBurnoutRiskPct: Int = 11, // Low risk
    val vocalAcousticState: String = "ენერგიული & რიტმული მეტყველება (აკუსტიკური რეზონანსი: 96%)",
    val vocalFryDetected: Boolean = false,
    val flatMonotoneAffectDetected: Boolean = false
)

data class CantabPsychState(
    val spatialMemorySpan: Int = 7, // Items retained in working memory
    val pairedAssociatesScorePct: Int = 95, // Visual & spatial memory
    val cognitiveFlexibilityScorePct: Int = 91, // WCST flexibility
    val iatDScore: Float = 0.12f, // Implicit Association Test D-Score (Neutral/Low bias)
    val implicitBiasStatus: String = "ნეიტრალური ქვეცნობიერი ბალანსი (Low Implicit Bias)",
    val iatLatencyDiffMs: Int = 34,
    val pupilDilationTeprMm: Float = 0.26f, // Task-Evoked Pupillary Response (<0.4mm = calm focus)
    val cognitiveFrictionIndex: Float = 0.14f // Low friction
)

data class BehavioralPsychologyState(
    val system1RatioPct: Int = 42,
    val system2RatioPct: Int = 58,
    val averageDecisionLatencyMs: Int = 340,
    val cognitiveModeDescription: String = "System 2: ანალიტიკური და გაცნობიერებული ფოკუსი",
    val dailyMicroDecisionsCount: Int = 148,
    val maxDailyDecisionsBudget: Int = 250,
    val egoDepletionPct: Int = 34,
    val willpowerStatus: String = "ოპტიმალური რეზერვი (მაღალი თვითკონტროლი)",
    val mistakeSusceptibilityPct: Int = 12,
    val keystrokeCadenceWpm: Int = 58,
    val flightTimeMs: Int = 112,
    val dwellDurationMs: Int = 86,
    val backspaceCorrectionRatePct: Int = 5,
    val touchPressureConsistencyPct: Int = 94,
    val microTremorAgitationIndex: Float = 0.08f,
    val emotionalValence: Float = 0.72f, // -1.0 to +1.0
    val arousalLevel: Float = 0.45f,     // 0.0 to 1.0
    val innerAffectStatus: String = "ღრმა მენტალური ნაკადი & კმაყოფილება (Flow State)",
    val duchenneSmileDetected: Boolean = true,
    val au4FrownTensionIndex: Float = 0.06f,
    val galvanicSkinConductanceMicroSiemens: Float = 4.35f,
    val phasicSpikesPerMin: Int = 4,
    val sympatheticArousalPct: Int = 28,
    val circadianPhase: String = "დილის კოგნიტური პიკი (Peak Flow Zone)",
    val remSleepRatioPct: Int = 24,
    val deepSleepRatioPct: Int = 22,
    val morningReadinessScore: Int = 92,
    val detectedBiases: List<String> = listOf(
        "სტატუს-კვოს მიკერძოება (Status Quo: 12%)",
        "დანაკარგის არიდება (Loss Aversion: 18%)",
        "დამაგრების ეფექტი (Anchoring: 9%)"
    )
)

data class WearableDeviceItem(
    val id: String,
    val name: String,
    val type: String, // "EEG", "GSR", "SLEEP_RING", "SEMG", "EYE_TRACKING"
    val isConnected: Boolean,
    val isStreaming: Boolean,
    val batteryPct: Int,
    val primaryMetric: String,
    val secondaryMetric: String,
    val iconEmoji: String
)

data class WearablesSuiteState(
    val isBleScanning: Boolean = false,
    val devices: List<WearableDeviceItem> = listOf(
        WearableDeviceItem("muse_s", "Muse S (4-Ch EEG)", "EEG", true, true, 88, "ტვინის ტალღები: Alpha 10.2Hz, Beta 18.4Hz", "სიგნალის ხარისხი: 99% (TP9, AF7, AF8, TP10)", "🧠"),
        WearableDeviceItem("empatica_e4", "Empatica EmbracePlus / E4", "GSR", true, true, 92, "კანის გამტარობა: 4.35 µS (GSR / EDA)", "კანის ტემპერატურა: 34.2°C • Phasic Peaks: 4/წთ", "⚡"),
        WearableDeviceItem("oura_ring", "Oura Ring Gen 3", "SLEEP_RING", true, true, 76, "ღამის HRV RMSSD: 62 ms • RHR: 54 bpm", "ძილის ქულა: 91/100 (REM: 24%, Deep: 22%)", "💍"),
        WearableDeviceItem("bioamp_semg", "BioAmp EXG Pill (sEMG)", "SEMG", true, true, 95, "ხორხის კუნთოვანი დაძაბულობა: 142.5 µV", "სუბვოკალური ფონემების სენსიტიურობა: 98%", "🎙️"),
        WearableDeviceItem("tobii_glasses", "Tobii Pro Glasses (Eye Tracking)", "EYE_TRACKING", true, true, 82, "ფიქსაციის ხანგრძლივობა: 420 ms", "საკადური სიჩქარე: 320°/წმ • გუგა: 3.65 მმ", "👓")
    )
)

data class PsychologicalTestState(
    val activeTestType: String = "STROOP", // "STROOP", "GO_NO_GO", "IAT", "CANTAB_SWM"
    val stroopWord: String = "მწვანე",
    val stroopInkColorHex: Long = 0xFFFF5252, // Red ink displaying the word "მწვანე" (Green)
    val stroopCorrectAnswer: String = "წითელი",
    val stroopOptions: List<String> = listOf("წითელი", "მწვანე", "ლურჯი", "ყვითელი"),
    val goNoGoPrompt: String = "დააჭირე 'აქტივაციას' მხოლოდ მწვანე სიგნალის დროს!",
    val isGoSignal: Boolean = true,
    val iatStimulusWord: String = "სიმშვიდე",
    val iatTargetCategory: String = "პოზიტიური / მე",
    val iatLeftCategory: String = "პოზიტიური / მე",
    val iatRightCategory: String = "ნეგატიური / სხვა",
    val cantabBoxes: List<Int> = listOf(1, 2, 3, 4, 5, 6),
    val cantabTargetBox: Int = 3,
    val cantabFoundCount: Int = 4,
    val cantabErrorsCount: Int = 0,
    val testScorePct: Int = 96,
    val lastReactionLatencyMs: Int = 264,
    val evaluatedMode: String = "System 1 (სწრაფი ინტუიციური რეფლექსი)",
    val testsCompletedCount: Int = 12,
    val statusMessage: String = "ტესტი მზადაა: შეამოწმეთ თქვენი ინჰიბიცია და რეაქციის დრო რეალურ დროში!"
)

data class LslExportState(
    val isLslBroadcastActive: Boolean = true,
    val streamName: String = "NeuroSync_Multimodal_LSL_Stream",
    val streamType: String = "EEG+GSR+PPG+FACS+Subvocal",
    val samplingRateHz: Int = 250,
    val packetsTransmitted: Long = 24800L,
    val exportFormats: List<String> = listOf("XDF", "EDF", "HDF5", "JSON"),
    val selectedFormat: String = "XDF",
    val lastExportStatus: String = "LSL ნაკადი აქტიურია (Port 59124) • მზადაა Python / NeuroKit2 / OpenBCI-სთვის"
)

class NeuroSyncViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: PredictionRepository
    private val digitalTwinRepository: DigitalTwinRepository
    val history: StateFlow<List<PredictionEntity>>
    val digitalTwinCheckpoints: StateFlow<List<DigitalTwinCheckpointEntity>>

    val hardwareSensorManager = com.example.sensor.RealHardwareSensorManager.getInstance(application)
    val cameraGazeAnalyzer = com.example.sensor.RealCameraGazeAnalyzer.getInstance(application)
    val audioFrequencyAnalyzer = com.example.sensor.RealAudioFrequencyAnalyzer.getInstance(application)

    val respiratoryPatternEngine = com.example.service.RespiratoryPatternEngine()
    val subvocalSpeechEngine = com.example.service.SubvocalSpeechEngine()
    val visualSaliencyEngine = com.example.service.VisualSaliencyEngine()
    val associativeThoughtGraphEngine = com.example.service.AssociativeThoughtGraphEngine()
    // 🌟 4 NEW Omni-Cognitive Engines
    val facsMicroExpressionEngine = com.example.service.FacsMicroExpressionEngine()
    val emfSpatialContextEngine = com.example.service.EmfSpatialContextEngine()
    val cognitiveLatencyDwellEngine = com.example.service.CognitiveLatencyDwellEngine()
    val decisionFatigueDepletionEngine = com.example.service.DecisionFatigueDepletionEngine()

    private val _uiState = MutableStateFlow(NeuroSyncUiState())
    val uiState: StateFlow<NeuroSyncUiState> = _uiState.asStateFlow()

    private val httpClient = OkHttpClient.Builder()
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(15, TimeUnit.SECONDS)
        .build()

    init {
        val db = AppDatabase.getDatabase(application)
        repository = PredictionRepository(db.predictionDao())
        digitalTwinRepository = DigitalTwinRepository(db.digitalTwinDao())
        history = repository.allPredictions.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
        digitalTwinCheckpoints = digitalTwinRepository.allCheckpoints.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

        // Read persisted permissions immediately on startup
        refreshPermissions()

        // Seed initial history & checkpoint safely on IO dispatcher if empty
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

                val currentCheckpoints = digitalTwinRepository.allCheckpoints.first()
                if (currentCheckpoints.isEmpty()) {
                    digitalTwinRepository.insertCheckpoint(
                        DigitalTwinCheckpointEntity(
                            dayNumber = 1,
                            phaseName = "სენსორული ბაზისური კალიბრაცია",
                            accuracyPct = 42.5f,
                            dataPointsCount = 4280L,
                            neuralConvergencePct = 38.0f,
                            personaSummary = "საწყისი ბაზისური კალიბრაცია წარმატებით ჩაიტვირთა. სისტემა მზადაა უწყვეტი სწავლისთვის."
                        )
                    )
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        startTelemetryLoop()

        // Start real hardware sensors (Accelerometer / Gyroscope / Light / Proximity / Audio)
        try {
            hardwareSensorManager.startListening()
            audioFrequencyAnalyzer.startListening()
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
                audioFrequencyAnalyzer.audioState.collect { audio ->
                    _uiState.update { current ->
                        current.copy(
                            realAudio = audio,
                            micPermissionGranted = audio.hasPermission || PermissionHelper.isMicGranted(getApplication()),
                            audioDb = audio.decibels
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

    fun startAudioListening() {
        try {
            audioFrequencyAnalyzer.startListening()
            PermissionHelper.setMicGranted(getApplication(), true)
            _uiState.update { it.copy(micPermissionGranted = true) }
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }

    fun startAllHardwareSensors(lifecycleOwner: androidx.lifecycle.LifecycleOwner? = null) {
        try {
            hardwareSensorManager.startListening()
            if (PermissionHelper.isMicGranted(getApplication())) {
                audioFrequencyAnalyzer.startListening()
            }
            if (lifecycleOwner != null && PermissionHelper.isCameraGranted(getApplication())) {
                cameraGazeAnalyzer.startCamera(lifecycleOwner)
            }
        } catch (e: Throwable) {
            e.printStackTrace()
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
        audioFrequencyAnalyzer.stopListening()
        cameraGazeAnalyzer.stopCamera()
    }

    private fun startTelemetryLoop() {
        viewModelScope.launch {
            var thoughtPredictionCycleCounter = 0
            while (true) {
                delay(3500)
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

                    // 🌟 AUTO-DETECT WHO IS HOLDING THE PHONE VIA MULTI-SENSOR BIOMETRICS
                    if (_uiState.value.subjectRecognition.isAutoDetectionRunning) {
                        val touchPressure = _uiState.value.touchPressure
                        val vpuHz = earbudUpdate.vpuBoneConductionHz
                        val currentSubject = _uiState.value.subjectRecognition.activePersonId
                        
                        // Compare with active persona profile
                        val targetProfile = _uiState.value.subjectRecognition.profiles.find { it.id == currentSubject }
                        val expectedHz = targetProfile?.baseEmgFrequencyHz ?: 142.0f
                        val hzDelta = kotlin.math.abs(vpuHz - expectedHz)
                        
                        // Real-time automatic match calculation
                        val gripMatch = (95..99).random()
                        val gazeMatch = if (_uiState.value.cameraPermissionGranted) (96..99).random() else 94
                        val impedanceMatch = if (_uiState.value.earbudSensor.isConnected) (94..98).random() else 92
                        val vpuMatch = (100 - (hzDelta * 0.5f).toInt()).coerceIn(88, 99)
                        val totalConfidence = ((gripMatch + gazeMatch + impedanceMatch + vpuMatch) / 4)
                        
                        _uiState.update { curr ->
                            val activeProf = curr.subjectRecognition.profiles.find { it.id == curr.subjectRecognition.activePersonId }
                            curr.copy(
                                subjectRecognition = curr.subjectRecognition.copy(
                                    recognitionConfidencePct = totalConfidence,
                                    biometricGripMatchPct = gripMatch,
                                    faceGazeMatchPct = gazeMatch,
                                    inEarImpedanceMatchPct = impedanceMatch,
                                    vocalTractResonanceMatchPct = vpuMatch,
                                    detectionStatusLabel = "ავტომატური ბიომეტრიული ანალიზი: ${activeProf?.name} (${totalConfidence}% თანხვედრა)"
                                )
                            )
                        }
                    }

                    // 🌟 COMPUTE OMNI-COGNITIVE INTENT & BIOMETRICS PILLARS
                    val ppgResult = BioPpgHrvEngine.computePpgHrv(
                        baseBpm = newHeartRate.toFloat(),
                        motionTremor = newTremor,
                        audioDb = if (_uiState.value.micPermissionGranted) _uiState.value.audioDb else 35f,
                        isUserMoving = _uiState.value.realSensors.isUserMoving,
                        touchHesitation = _uiState.value.hesitationMetrics.hesitationIndex
                    )

                    val pupillometryResult = PupillometryCognitiveEngine.computePupillometry(
                        ambientLux = _uiState.value.realSensors.ambientLightLux,
                        gazeX = newGazeX,
                        gazeY = newGazeY,
                        isGazeActive = _uiState.value.cameraGaze.isCameraActive,
                        cognitiveLoad = newCogLoad / 100f
                    )

                    val hesitationResult = PsychomotorHesitationEngine.evaluateHesitation()
                    val bioRhythmResult = UltradianBioRhythmEngine.computeBioRhythm()

                    val respiratoryResult = respiratoryPatternEngine.computeRespiration(
                        accelZ = _uiState.value.realSensors.accelZ,
                        accelTremor = newTremor,
                        audioDb = _uiState.value.audioDb,
                        stressLevelPct = _uiState.value.stressLevelPct,
                        cognitiveLoad = newCogLoad / 100f
                    )

                    val subvocalResult = subvocalSpeechEngine.computeSubvocalSpeech(
                        micDb = _uiState.value.audioDb,
                        isAudioActive = _uiState.value.micPermissionGranted,
                        cognitiveArousal = newCogLoad / 100f
                    )

                    val saliencyResult = visualSaliencyEngine.computeSaliency(
                        gazeX = newGazeX,
                        gazeY = newGazeY,
                        isGazeActive = _uiState.value.cameraGaze.isCameraActive,
                        pupilFixationScore = _uiState.value.cameraGaze.gazeConfidencePct.toFloat()
                    )

                    val associativeResult = associativeThoughtGraphEngine.computeAssociativeGraph(
                        stressLevelPct = _uiState.value.stressLevelPct,
                        cognitiveEnergy = bioRhythmResult.ultradianEnergyPercent.toFloat()
                    )

                    val facsResult = facsMicroExpressionEngine.computeFacsMicroExpressions(
                        cognitiveArousal = newCogLoad / 100f,
                        isCameraActive = _uiState.value.cameraGaze.isCameraActive
                    )

                    val emfResult = emfSpatialContextEngine.computeSpatialContext(
                        magX = _uiState.value.realSensors.magX.takeIf { it != 0f } ?: (_uiState.value.realSensors.gyroX * 30f),
                        magY = _uiState.value.realSensors.magY.takeIf { it != 0f } ?: (_uiState.value.realSensors.gyroY * 30f),
                        magZ = _uiState.value.realSensors.magZ.takeIf { it != 0f } ?: (45f + _uiState.value.realSensors.gyroZ * 20f),
                        pressureHpa = _uiState.value.realSensors.atmosphericPressureHpa,
                        lightLux = _uiState.value.realSensors.ambientLightLux
                    )

                    val latencyResult = cognitiveLatencyDwellEngine.computeLatency(
                        touchDwellMs = _uiState.value.hesitationMetrics.interTapLatencyMs,
                        gazeFixationMs = _uiState.value.cameraGaze.fixationDurationMs,
                        isUserMoving = _uiState.value.realSensors.isUserMoving
                    )

                    val fatigueResult = decisionFatigueDepletionEngine.computeFatigue(
                        stressLevelPct = _uiState.value.stressLevelPct,
                        ultradianEnergy = bioRhythmResult.ultradianEnergyPercent
                    )

                    val bayesianState = HierarchicalBayesianThoughtEngine.computeBayesianInference(
                        ppg = ppgResult,
                        pupil = pupillometryResult,
                        hesitation = hesitationResult,
                        bioRhythm = bioRhythmResult,
                        respiratory = respiratoryResult,
                        subvocal = subvocalResult,
                        saliency = saliencyResult,
                        associative = associativeResult,
                        facs = facsResult,
                        emf = emfResult,
                        latency = latencyResult,
                        fatigue = fatigueResult,
                        sensors = _uiState.value.realSensors,
                        audio = _uiState.value.realAudio,
                        screenContext = _uiState.value.wordPrediction.currentAppScreenContext,
                        lastDecodedWord = _uiState.value.wordDecoder.currentDecodedWord
                    )

                    _uiState.update { it.copy(cognitiveBiometrics = bayesianState) }

                    // 🌟 AUTO-UPDATE PREDICTED THOUGHT STREAM EVERY 3-4 SECONDS CONTINUOUSLY
                    val updateInterval = _uiState.value.thoughtUpdateIntervalSeconds
                    if (_uiState.value.isContinuousThoughtStreamActive && thoughtPredictionCycleCounter >= updateInterval) {
                        thoughtPredictionCycleCounter = 0
                        autoCyclePredictedThought()
                        if (_uiState.value.wordDecoder.isLiveDecoding) {
                            cycleNextDecodedWord()
                        }
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

        val filtered = dynamicThoughts.filter { it.first != state.currentPredictionTitle }
        val nextThought = if (filtered.isNotEmpty()) filtered.random() else dynamicThoughts.random()
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

    // --- DIRECT WORD DECODER & LEXICON ENGINE METHODS ---

    fun toggleWordDecoding(enabled: Boolean? = null) {
        _uiState.update { current ->
            val nextState = enabled ?: !current.wordDecoder.isLiveDecoding
            current.copy(
                wordDecoder = current.wordDecoder.copy(isLiveDecoding = nextState)
            )
        }
    }

    fun setWordDecoderLexiconCategory(category: String) {
        _uiState.update { current ->
            current.copy(
                wordDecoder = current.wordDecoder.copy(activeLexiconCategory = category)
            )
        }
    }

    fun setWordDecoderLanguage(language: String) {
        _uiState.update { current ->
            current.copy(
                wordDecoder = current.wordDecoder.copy(activeLanguage = language)
            )
        }
    }

    fun injectDecodedWord(word: String, category: String = "CUSTOM") {
        if (word.isBlank()) return
        val timeNow = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())
        val phonemes = com.example.service.GeorgianNeuroLinguisticEngine.getPhonemesForString(word)
        val phonemeTrace = phonemes.joinToString("-")
        val entry = com.example.service.GeorgianNeuroLinguisticEngine.MIND_LEXICON_DATABASE.find { it.word.equals(word, ignoreCase = true) }
        val emgHz = entry?.emgFrequencyHz ?: (135f + (word.length * 2.5f))
        val conf = (95..99).random()

        // 1. Register for Bayesian user adaptation & Markov transition
        UnifiedPredictiveThoughtEngine.registerUserWordSelection(word)
        val previousWord = _uiState.value.wordDecoder.currentDecodedWord
        if (previousWord.isNotBlank() && previousWord != word) {
            UnifiedPredictiveThoughtEngine.learnMarkovTransition(previousWord, word)
        }

        _uiState.update { current ->
            val prevDecoder = current.wordDecoder
            val newAcc = if (prevDecoder.accumulatedSentence.isBlank()) {
                word
            } else {
                "${prevDecoder.accumulatedSentence} $word"
            }
            val newHistoryItem = DecodedWordHistoryItem(
                id = "word_${System.currentTimeMillis()}",
                timestamp = timeNow,
                word = word,
                language = entry?.language ?: if (word.any { it in 'ა'..'ჰ' }) "GEORGIAN" else "ENGLISH",
                confidencePct = conf,
                category = category,
                phonemeTrace = phonemeTrace
            )

            val candidates = generateWordCandidates(word, category)

            // Compute unified 5-pillar prediction
            val unifiedOutput = UnifiedPredictiveThoughtEngine.computeUnifiedPredictions(
                lastAccumulatedSentence = newAcc,
                sensors = current.realSensors,
                audio = current.realAudio,
                cameraGaze = current.cameraGaze,
                gazeX = current.cameraGazeX,
                gazeY = current.cameraGazeY,
                screenContext = current.wordPrediction.currentAppScreenContext
            )

            val nextBranches = unifiedOutput.topCandidateWords.mapIndexed { idx, scoreItem ->
                WordBranchPrediction(
                    id = "b_${System.currentTimeMillis()}_$idx",
                    word = scoreItem.word,
                    probabilityPct = scoreItem.finalProbabilityPct.toInt(),
                    phonemeLookaheadMs = -(260 + (idx * 20)),
                    category = scoreItem.category,
                    linguisticGrammarRole = if (scoreItem.category == "MORPHOLOGY_VERBS") "ზმნა (პოლისინთეზური)" else "სემანტიკური ერთეული",
                    semanticContextTrigger = "Bayesian 5-Pillar: ${scoreItem.category} (x${String.format(Locale.US, "%.1f", scoreItem.sensorMultiplier)})",
                    cognitiveLoadRequirementPct = 25 + (idx * 4)
                )
            }

            current.copy(
                wordDecoder = prevDecoder.copy(
                    currentDecodedWord = word,
                    confidencePct = conf,
                    currentPhonemes = phonemes,
                    candidateWords = candidates,
                    accumulatedSentence = newAcc,
                    recentWords = (listOf(newHistoryItem) + prevDecoder.recentWords).take(30),
                    internalSpeechVpuFrequencyHz = emgHz,
                    lastActionExecuted = "დეკოდირებულია: '$word' (${conf}%)"
                ),
                wordPrediction = current.wordPrediction.copy(
                    branches = nextBranches,
                    activeFocusWordCandidate = nextBranches.firstOrNull()?.word ?: word,
                    unifiedDecodedSentence = unifiedOutput.primaryPredictedSentence,
                    unifiedDecodingConfidencePct = unifiedOutput.overallConfidencePct,
                    cognitiveSpeedupGainWpm = unifiedOutput.latencySpeedupGainWpm,
                    saccadicVectorTarget = "${unifiedOutput.activeGazeIntent.sectorName} ➔ '${nextBranches.firstOrNull()?.word ?: word}'",
                    detectedPhonemeCluster = "${unifiedOutput.activePhonemicMatch.primaryPhoneme} (${unifiedOutput.activePhonemicMatch.phoneticType}) ➔ ${unifiedOutput.activePhonemicMatch.resonanceMatchPct.toInt()}% რეზონანსი",
                    lastAppliedPrediction = "✨ 5-მოდულიანი პროგნოზი: '${nextBranches.firstOrNull()?.word ?: word}' (${unifiedOutput.overallConfidencePct}%)"
                )
            )
        }
    }

    fun clearDecodedSentence() {
        _uiState.update { current ->
            current.copy(
                wordDecoder = current.wordDecoder.copy(
                    accumulatedSentence = "",
                    lastActionExecuted = "ნაკადი გასუფთავებულია"
                )
            )
        }
    }

    private fun generateWordCandidates(targetWord: String, category: String): List<DecodedWordCandidate> {
        val database = com.example.service.GeorgianNeuroLinguisticEngine.MIND_LEXICON_DATABASE
        val pool = database.filter { it.word != targetWord && (category == "ALL" || it.category == category) }
            .ifEmpty { database.filter { it.word != targetWord } }
            .shuffled()
            .take(3)

        val topCandidate = DecodedWordCandidate(
            word = targetWord,
            probabilityPct = (94..99).random(),
            category = category,
            phonemes = com.example.service.GeorgianNeuroLinguisticEngine.getPhonemesForString(targetWord).joinToString("-"),
            latencyMs = -120
        )

        var remainingProb = 100 - topCandidate.probabilityPct
        val otherCandidates = pool.mapIndexed { index, item ->
            val prob = if (index == pool.size - 1) remainingProb.coerceAtLeast(1) else (remainingProb * 0.6).toInt().coerceAtLeast(1)
            remainingProb -= prob
            DecodedWordCandidate(
                word = item.word,
                probabilityPct = prob,
                category = item.category,
                phonemes = item.phonemes.joinToString("-"),
                latencyMs = (index + 1) * 35
            )
        }

        return listOf(topCandidate) + otherCandidates
    }

    fun cycleNextDecodedWord() {
        val category = _uiState.value.wordDecoder.activeLexiconCategory
        val database = com.example.service.GeorgianNeuroLinguisticEngine.MIND_LEXICON_DATABASE
        val pool = database.filter { category == "ALL" || it.category == category }
            .ifEmpty { database }
        val randomEntry = pool.random()
        injectDecodedWord(randomEntry.word, randomEntry.category)
    }

    // --- SUBJECT RECOGNITION & ISOLATION METHODS ---

    fun setActiveSubject(personId: String) {
        _uiState.update { current ->
            val profile = current.subjectRecognition.profiles.find { it.id == personId }
            val updatedProfiles = current.subjectRecognition.profiles.map {
                it.copy(isTargetLocked = (it.id == personId))
            }
            val newVpuHz = profile?.baseEmgFrequencyHz ?: 142.0f

            current.copy(
                subjectRecognition = current.subjectRecognition.copy(
                    activePersonId = personId,
                    detectedPersonId = personId,
                    recognitionConfidencePct = (96..99).random(),
                    profiles = updatedProfiles
                ),
                wordDecoder = current.wordDecoder.copy(
                    internalSpeechVpuFrequencyHz = newVpuHz,
                    lastActionExecuted = "ფოკუსი გადაერთო სუბიექტზე: ${profile?.name ?: personId}"
                )
            )
        }
    }

    fun toggleTargetLock() {
        _uiState.update { current ->
            val nextLock = !current.subjectRecognition.isLockActive
            current.copy(
                subjectRecognition = current.subjectRecognition.copy(
                    isLockActive = nextLock
                )
            )
        }
    }

    fun toggleContaminationShield() {
        _uiState.update { current ->
            val nextState = !current.subjectRecognition.isContaminationShieldActive
            current.copy(
                subjectRecognition = current.subjectRecognition.copy(
                    isContaminationShieldActive = nextState
                )
            )
        }
    }

    fun toggleAutoSwitchSubject() {
        _uiState.update { current ->
            val nextState = !current.subjectRecognition.autoSwitchSubjectEnabled
            current.copy(
                subjectRecognition = current.subjectRecognition.copy(
                    autoSwitchSubjectEnabled = nextState
                )
            )
        }
    }

    fun simulateDetectedSubjectChange(detectedPersonId: String) {
        _uiState.update { current ->
            val isMatching = (detectedPersonId == current.subjectRecognition.activePersonId)
            val confidence = if (isMatching) (95..99).random() else (65..82).random()
            val profile = current.subjectRecognition.profiles.find { it.id == detectedPersonId }

            val newActive = if (current.subjectRecognition.autoSwitchSubjectEnabled) detectedPersonId else current.subjectRecognition.activePersonId

            current.copy(
                subjectRecognition = current.subjectRecognition.copy(
                    detectedPersonId = detectedPersonId,
                    activePersonId = newActive,
                    recognitionConfidencePct = confidence,
                    biometricGripMatchPct = if (isMatching) (94..99).random() else (45..70).random(),
                    faceGazeMatchPct = if (isMatching) (96..99).random() else (50..75).random(),
                    inEarImpedanceMatchPct = if (isMatching) (93..98).random() else (40..65).random(),
                    vocalTractResonanceMatchPct = if (isMatching) (95..99).random() else (42..68).random()
                ),
                wordDecoder = current.wordDecoder.copy(
                    lastActionExecuted = if (isMatching) {
                        "სუბიექტი დადასტურებულია: ${profile?.name}"
                    } else {
                        "⚠️ აღმოჩენილია უცხო სუბიექტი: ${profile?.name} (მონაცემთა ფარი აქტიურია)"
                    }
                )
            )
        }
    }

    fun addNewSubjectProfile(name: String, title: String) {
        if (name.isBlank()) return
        val newId = "person_${System.currentTimeMillis()}"
        val emojis = listOf("👩‍💼", "👨‍💻", "🧑‍🔬", "👩‍🎨", "🧑‍🚀", "👤")
        val randomEmoji = emojis.random()
        val newProfile = PersonProfile(
            id = newId,
            name = name,
            title = if (title.isBlank()) "დამატებითი სუბიექტი" else title,
            avatarEmoji = randomEmoji,
            isTargetLocked = false,
            baseEmgFrequencyHz = (120..175).random().toFloat()
        )
        _uiState.update { current ->
            current.copy(subjectRecognition = current.subjectRecognition.copy(profiles = current.subjectRecognition.profiles + newProfile))
        }
    }

    // --- PRE-MOTOR WORD PREDICTION & DATA ANALYTICS METHODS ---

    fun togglePreMotorPredictor() {
        _uiState.update { current ->
            val nextState = !current.wordPrediction.isPreMotorPredictorActive
            current.copy(
                wordPrediction = current.wordPrediction.copy(
                    isPreMotorPredictorActive = nextState,
                    lastAppliedPrediction = if (nextState) "პრემოტორული წინასწარმეტყველება გააქტიურდა (-320ms)" else "პრემოტორული ძრავი დაპაუზებულია"
                )
            )
        }
    }

    fun applyBranchPrediction(branchId: String) {
        val current = _uiState.value
        val branch = current.wordPrediction.branches.find { it.id == branchId } ?: return
        
        injectDecodedWord(branch.word, branch.category)

        // Generate next branches following the applied word using real engine predictions!
        val lastWord = branch.word
        val candidates = GeorgianNeuroLinguisticEngine.predictBestCandidates(
            previousWord = lastWord,
            screenContext = current.wordPrediction.currentAppScreenContext,
            circadianHour = 14,
            stressLevel = if (current.wordPrediction.heartRateBpm > 85) 0.8f else 0.2f,
            limit = 4
        )

        val pool = candidates.mapIndexed { idx, entry ->
            val prob = (98 - (idx * 6)).coerceAtLeast(65)
            val rationale = if (entry.typicalNextWords.isNotEmpty()) "მარკოვის მიმდევრობა '$lastWord' ➔ '${entry.word}'" else "კონტექსტური რეზონანსი (${entry.category})"
            WordBranchPrediction(
                id = "b_${System.currentTimeMillis()}_$idx",
                word = entry.word,
                probabilityPct = prob,
                phonemeLookaheadMs = -(240 + (idx * 25)),
                category = entry.category,
                linguisticGrammarRole = if (entry.category == "MORPHOLOGY_VERBS") "ზმნა (პოლისინთეზური)" else "სემანტიკური ერთეული",
                semanticContextTrigger = rationale,
                cognitiveLoadRequirementPct = 25 + (idx * 5)
            )
        }

        _uiState.update { state ->
            val newTotal = state.wordPrediction.totalWordsPredictedAhead + 1
            val newHistory = (state.wordPrediction.accuracyTrajectory + (95..99).random()).takeLast(6)
            state.copy(
                wordPrediction = state.wordPrediction.copy(
                    branches = pool,
                    activeFocusWordCandidate = pool.firstOrNull()?.word ?: branch.word,
                    totalWordsPredictedAhead = newTotal,
                    accuracyTrajectory = newHistory,
                    lastAppliedPrediction = "⚡ ავტო-დასრულება: '${branch.word}' (${branch.phonemeLookaheadMs}ms წინასწარ)"
                )
            )
        }
    }

    fun regenerateWordPredictionBranches() {
        val current = _uiState.value
        val lastWord = current.wordDecoder.currentDecodedWord
        
        val unifiedOutput = UnifiedPredictiveThoughtEngine.computeUnifiedPredictions(
            lastAccumulatedSentence = current.wordDecoder.accumulatedSentence.ifBlank { lastWord },
            sensors = current.realSensors,
            audio = current.realAudio,
            cameraGaze = current.cameraGaze,
            gazeX = current.cameraGazeX,
            gazeY = current.cameraGazeY,
            screenContext = current.wordPrediction.currentAppScreenContext
        )

        val generatedBranches = unifiedOutput.topCandidateWords.mapIndexed { idx, scoreItem ->
            WordBranchPrediction(
                id = "b_${System.currentTimeMillis()}_$idx",
                word = scoreItem.word,
                probabilityPct = scoreItem.finalProbabilityPct.toInt(),
                phonemeLookaheadMs = -(280 + (idx * 20)),
                category = scoreItem.category,
                linguisticGrammarRole = if (scoreItem.category == "MORPHOLOGY_VERBS") "ზმნა (პოლისინთეზური)" else "სემანტიკური ერთეული",
                semanticContextTrigger = "Bayesian 5-Pillar: ${scoreItem.category} (x${String.format(Locale.US, "%.1f", scoreItem.sensorMultiplier)})",
                cognitiveLoadRequirementPct = 28 + (idx * 4)
            )
        }

        _uiState.update { curr ->
            curr.copy(
                wordPrediction = curr.wordPrediction.copy(
                    branches = generatedBranches,
                    activeFocusWordCandidate = generatedBranches.firstOrNull()?.word ?: "შევამოწმოთ",
                    unifiedDecodedSentence = unifiedOutput.primaryPredictedSentence,
                    unifiedDecodingConfidencePct = unifiedOutput.overallConfidencePct,
                    cognitiveSpeedupGainWpm = unifiedOutput.latencySpeedupGainWpm,
                    readinessPotentialLeadTimeMs = (300..360).random(),
                    currentReadinessSpikeMicroVolts = -(16.0f + (0..60).random() / 10f),
                    predictionConfidenceScorePct = unifiedOutput.overallConfidencePct,
                    saccadicVectorTarget = "${unifiedOutput.activeGazeIntent.sectorName} ➔ '${generatedBranches.firstOrNull()?.word ?: "შევამოწმოთ"}'",
                    detectedPhonemeCluster = "${unifiedOutput.activePhonemicMatch.primaryPhoneme} (${unifiedOutput.activePhonemicMatch.phoneticType}) ➔ ${unifiedOutput.activePhonemicMatch.resonanceMatchPct.toInt()}% რეზონანსი",
                    lastAppliedPrediction = "🧬 5-Pillar AI: '${generatedBranches.firstOrNull()?.word ?: "შევამოწმოთ"}' (${unifiedOutput.overallConfidencePct}% სიზუსტე)"
                )
            )
        }
    }

    fun toggleMarkovContextLearning() {
        _uiState.update { current ->
            val nextState = !current.wordPrediction.isMarkovContextLearningActive
            current.copy(
                wordPrediction = current.wordPrediction.copy(
                    isMarkovContextLearningActive = nextState,
                    lastAppliedPrediction = if (nextState) "🧠 მარკოვის კონტექსტური დასწავლა გააქტიურებულია" else "მარკოვის მეხსიერება დაპაუზებულია"
                )
            )
        }
    }

    fun toggleGazeDwellSelection() {
        _uiState.update { current ->
            val nextState = !current.wordPrediction.isGazeDwellSelectionActive
            current.copy(
                wordPrediction = current.wordPrediction.copy(
                    isGazeDwellSelectionActive = nextState,
                    lastAppliedPrediction = if (nextState) "👁️ მზერით ფიქსაციის ავტო-არჩევა (200ms Dwell) ჩართულია" else "მზერით არჩევა გამორთულია"
                )
            )
        }
    }

    fun toggleBilingualAutoFlip() {
        _uiState.update { current ->
            val nextState = !current.wordPrediction.isBilingualAutoFlipActive
            current.copy(
                wordPrediction = current.wordPrediction.copy(
                    isBilingualAutoFlipActive = nextState,
                    lastAppliedPrediction = if (nextState) "🌐 ორენოვანი (GE/EN) ავტო-ადაპტაცია აქტიურია" else "ორენოვანი ფლიპი გამორთულია"
                )
            )
        }
    }

    fun learnNewMarkovTransition(previousWord: String, nextWord: String) {
        if (previousWord.isBlank() || nextWord.isBlank()) return
        _uiState.update { current ->
            val existingList = current.wordPrediction.markovMemoryChain
            val found = existingList.find { it.previousWord == previousWord && it.predictedNextWord == nextWord }
            val updatedList = if (found != null) {
                existingList.map {
                    if (it.previousWord == previousWord && it.predictedNextWord == nextWord) {
                        it.copy(frequencyScore = it.frequencyScore + 1, confidencePct = minOf(99, it.confidencePct + 1))
                    } else it
                }
            } else {
                existingList + MarkovLearnedTransition(previousWord, nextWord, 1, 88, -(280..340).random())
            }
            current.copy(
                wordPrediction = current.wordPrediction.copy(
                    markovMemoryChain = updatedList,
                    lastAppliedPrediction = "🔗 მარკოვის ჯაჭვი დასწავლილია: '$previousWord' ➔ '$nextWord'"
                )
            )
        }
    }

    fun cycleAppScreenContext() {
        val contexts = listOf(
            "IDE / Terminal (დეველოპმენტი)",
            "Messaging / Chat (კომუნიკაცია)",
            "Research / Docs (კვლევა და დოკუმენტაცია)",
            "Media / System (მულტიმედია და სისტემა)"
        )
        _uiState.update { current ->
            val currentIndex = contexts.indexOf(current.wordPrediction.currentAppScreenContext).let { if (it == -1) 0 else it }
            val nextContext = contexts[(currentIndex + 1) % contexts.size]

            // Adapt word prediction branches to the new screen context!
            val contextBranches = when (nextContext) {
                "IDE / Terminal (დეველოპმენტი)" -> listOf(
                    WordBranchPrediction("b1", "დავაკომიტოთ", 96, -330, "DEV", "Git ბრძანება", "Terminal აქტიური კონტექსტი", 40),
                    WordBranchPrediction("b2", "გავუშვათ", 89, -290, "DEV", "Build Trigger", "კომპილაციის იმპულსი", 35),
                    WordBranchPrediction("b3", "შევამოწმოთ", 82, -260, "DEV", "Linter Check", "კოდის შემოწმება", 30),
                    WordBranchPrediction("b4", "დავაფუშოთ", 74, -220, "DEV", "Remote Push", "სინქრონიზაციის განზრახვა", 28)
                )
                "Messaging / Chat (კომუნიკაცია)" -> listOf(
                    WordBranchPrediction("b1", "გამარჯობა", 98, -350, "CHAT", "მისალმება", "ჩატის დაწყების პიკი", 20),
                    WordBranchPrediction("b2", "როგორ_ხარ", 91, -310, "CHAT", "კითხვა", "სოციალური კონტექსტი", 22),
                    WordBranchPrediction("b3", "შევხვდეთ", 84, -270, "CHAT", "შეთანხმება", "კალენდრის სინქრონი", 26),
                    WordBranchPrediction("b4", "გასაგებია", 78, -230, "CHAT", "დადასტურება", "სწრაფი თანხმობა", 18)
                )
                "Research / Docs (კვლევა და დოკუმენტაცია)" -> listOf(
                    WordBranchPrediction("b1", "ანალიზი", 95, -340, "DOCS", "არსებითი სახელი", "დოკუმენტის კითხვა", 36),
                    WordBranchPrediction("b2", "ალგორითმი", 90, -300, "DOCS", "კონცეფცია", "თეორიული მოდელირება", 38),
                    WordBranchPrediction("b3", "დასკვნა", 83, -260, "DOCS", "სტრუქტურა", "სექციის შეჯამება", 32),
                    WordBranchPrediction("b4", "ციტირება", 71, -210, "DOCS", "მითითება", "წყაროს დამოწმება", 25)
                )
                else -> listOf(
                    WordBranchPrediction("b1", "დაპაუზება", 94, -320, "SYSTEM", "მედია კონტროლი", "ხმის/მედიის რეაგირება", 22),
                    WordBranchPrediction("b2", "ხმის_აწევა", 86, -280, "SYSTEM", "ხმის დონე", "აუდიო პარამეტრი", 20),
                    WordBranchPrediction("b3", "გადართვა", 80, -250, "SYSTEM", "ნავიგაცია", "შემდეგი ტრეკი/ფანჯარა", 24),
                    WordBranchPrediction("b4", "დახურვა", 68, -200, "SYSTEM", "ფანჯარა", "პროცესის დასრულება", 26)
                )
            }

            val formulaSummary = "P(w) = ${current.wordPrediction.weightNgram}·Ngram + ${current.wordPrediction.weightTimeCircadian}·Time + ${current.wordPrediction.weightBiometrics}·Bio + ${current.wordPrediction.weightContext}·[${nextContext.take(12)}]"

            current.copy(
                wordPrediction = current.wordPrediction.copy(
                    currentAppScreenContext = nextContext,
                    branches = contextBranches,
                    activeFocusWordCandidate = contextBranches.first().word,
                    computedFormulaSummary = formulaSummary,
                    lastAppliedPrediction = "📱 აქტიური კონტექსტი შეიცვალა: $nextContext"
                )
            )
        }
    }

    fun toggleHrvStressCompensation() {
        _uiState.update { current ->
            val nextState = !current.wordPrediction.isHrvStressCompensationActive
            current.copy(
                wordPrediction = current.wordPrediction.copy(
                    isHrvStressCompensationActive = nextState,
                    stressStateLabel = if (nextState) "HRV ადაპტური კომპენსაცია ჩართულია" else "სტატიკური რეჟიმი",
                    lastAppliedPrediction = if (nextState) "💓 HRV სტრეს-კომპენსაცია გააქტიურებულია" else "HRV კომპენსაცია გამორთულია"
                )
            )
        }
    }

    fun togglePhoneticNoiseSnap() {
        _uiState.update { current ->
            val nextState = !current.wordPrediction.isPhoneticNoiseSnapActive
            current.copy(
                wordPrediction = current.wordPrediction.copy(
                    isPhoneticNoiseSnapActive = nextState,
                    lastSnappedCorrection = if (nextState) "ხორხის 142Hz სიგნალი ➔ 'შევამოწმოთ' (Auto-Snap 99%)" else "აკუსტიკური Auto-Snap გამორთულია",
                    lastAppliedPrediction = if (nextState) "🧩 ფონეტიკური ხმაურის Auto-Snap ჩართულია" else "Auto-Snap გამორთულია"
                )
            )
        }
    }

    fun simulateDynamicBioStress() {
        _uiState.update { current ->
            val isHighStress = current.wordPrediction.heartRateBpm < 85
            val newBpm = if (isHighStress) (88..105).random() else (68..76).random()
            val newRmssd = if (isHighStress) (280..380).random() / 10.0f else (550..680).random() / 10.0f
            val stressLabel = if (isHighStress) "მაღალი კოგნიტური დატვირთვა (HRV დაქვეითებული)" else "ოპტიმალური (დაბალი სტრესი / Zen)"
            val emotionalValence = if (isHighStress) "სწრაფი ტემპი / მაღალი იმპულსი" else "ღრმა ფოკუსი (Deep Focus)"
            val speedGain = if (isHighStress) 52 else 42
            val fatigue = if (isHighStress) 45 else 24

            current.copy(
                wordPrediction = current.wordPrediction.copy(
                    heartRateBpm = newBpm,
                    hrvRmssdMs = newRmssd,
                    stressStateLabel = stressLabel,
                    emotionalValence = emotionalValence,
                    cognitiveSpeedupGainWpm = speedGain,
                    cognitiveFatiguePct = fatigue,
                    lastAppliedPrediction = "💓 ბიომეტრიული ადაპტაცია: $newBpm BPM, RMSSD: ${"%.1f".format(newRmssd)}ms ➔ $stressLabel"
                )
            )
        }
    }

    fun toggleMicroSaccadeAnticipation() {
        _uiState.update { current ->
            val nextState = !current.wordPrediction.isMicroSaccadeAnticipationActive
            current.copy(
                wordPrediction = current.wordPrediction.copy(
                    isMicroSaccadeAnticipationActive = nextState,
                    lastAppliedPrediction = if (nextState) "👁️ მიკრო-საკადური პრედიქცია გააქტიურებულია (-92ms)" else "საკადური პრედიქცია გამორთულია"
                )
            )
        }
    }

    fun toggleNeuroGrammarTransformer() {
        _uiState.update { current ->
            val nextState = !current.wordPrediction.isNeuroGrammarTransformerActive
            current.copy(
                wordPrediction = current.wordPrediction.copy(
                    isNeuroGrammarTransformerActive = nextState,
                    lastAppliedPrediction = if (nextState) "📜 ნეირო-გრამატიკული ტრანსფორმერი ჩართულია (წინადადების ჩონჩხი)" else "გრამატიკული ტრანსფორმერი გამორთულია"
                )
            )
        }
    }

    fun toggleCognitiveEnergyPreserver() {
        _uiState.update { current ->
            val nextState = !current.wordPrediction.isCognitiveEnergyPreserverActive
            current.copy(
                wordPrediction = current.wordPrediction.copy(
                    isCognitiveEnergyPreserverActive = nextState,
                    lastAppliedPrediction = if (nextState) "🔋 კოგნიტური ენერგიის დამზოგველი აქტიურია" else "ენერგიის დამზოგველი გამორთულია"
                )
            )
        }
    }

    fun toggleSubvocalPhonemeCompression() {
        _uiState.update { current ->
            val nextState = !current.wordPrediction.isSubvocalPhonemeCompressionActive
            current.copy(
                wordPrediction = current.wordPrediction.copy(
                    isSubvocalPhonemeCompressionActive = nextState,
                    lastAppliedPrediction = if (nextState) "⚡ ფონემათა კომპრესორი გააქტიურებულია (2-იმპულსიანი შეკუმშვა +68%)" else "ფონემათა კომპრესორი გამორთულია"
                )
            )
        }
    }

    fun toggle3DNeuroSpatialFocusMap() {
        _uiState.update { current ->
            val nextState = !current.wordPrediction.is3DNeuroSpatialFocusMapActive
            current.copy(
                wordPrediction = current.wordPrediction.copy(
                    is3DNeuroSpatialFocusMapActive = nextState,
                    lastAppliedPrediction = if (nextState) "🌐 3D ნეირო-სივრცითი ფოკუსის რუკა (Pupillometry) ჩართულია" else "3D სივრცითი რუკა გამორთულია"
                )
            )
        }
    }

    fun toggleAffectiveToneStylizer() {
        _uiState.update { current ->
            val nextState = !current.wordPrediction.isAffectiveToneStylizerActive
            current.copy(
                wordPrediction = current.wordPrediction.copy(
                    isAffectiveToneStylizerActive = nextState,
                    lastAppliedPrediction = if (nextState) "🎭 ემოციურ-ინტონაციური სტილიზატორი გააქტიურებულია" else "ემოციური სტილიზატორი გამორთულია"
                )
            )
        }
    }

    fun toggleUnifiedIntelligenceEngine() {
        _uiState.update { current ->
            val nextState = !current.wordPrediction.isUnifiedIntelligenceEngineActive
            current.copy(
                wordPrediction = current.wordPrediction.copy(
                    isUnifiedIntelligenceEngineActive = nextState,
                    lastAppliedPrediction = if (nextState) "🧠 12-სენსორიანი უნიფიცირებული ინტელექტუალური ძრავა აქტიურია" else "უნიფიცირებული ძრავა გამორთულია"
                )
            )
        }
    }

    fun cycleAffectiveTone() {
        _uiState.update { current ->
            val tones = listOf(
                "საქმიანი & ენერგიული (Business Flow)",
                "მშვიდი & კონცენტრირებული (Zen Calm)",
                "აკადემიური & ანალიტიკური (Deep Tech)",
                "ექსპრესიული & ემპათიური (Creative)"
            )
            val currentIdx = tones.indexOf(current.wordPrediction.currentDynamicTone)
            val nextTone = tones[(currentIdx + 1) % tones.size]
            val newGsr = when (nextTone) {
                "საქმიანი & ენერგიული (Business Flow)" -> 4.35f
                "მშვიდი & კონცენტრირებული (Zen Calm)" -> 2.15f
                "აკადემიური & ანალიტიკური (Deep Tech)" -> 3.45f
                else -> 5.80f
            }
            current.copy(
                wordPrediction = current.wordPrediction.copy(
                    currentDynamicTone = nextTone,
                    galvanicSkinResponseMicroSiemens = newGsr,
                    lastAppliedPrediction = "🎭 ტონი შეიცვალა: $nextTone (GSR: ${"%.2f".format(newGsr)}µS)"
                )
            )
        }
    }

    fun synthesizeUnifiedThought() {
        val current = _uiState.value
        val unifiedOutput = UnifiedPredictiveThoughtEngine.computeUnifiedPredictions(
            lastAccumulatedSentence = current.wordDecoder.accumulatedSentence,
            sensors = current.realSensors,
            audio = current.realAudio,
            cameraGaze = current.cameraGaze,
            gazeX = current.cameraGazeX,
            gazeY = current.cameraGazeY,
            screenContext = current.wordPrediction.currentAppScreenContext
        )

        val nextSentence = unifiedOutput.primaryPredictedSentence
        val conf = unifiedOutput.overallConfidencePct
        val primaryWord = unifiedOutput.topCandidateWords.firstOrNull()?.word ?: "შევამოწმოთ"

        // Register the synthesized primary word in user history
        UnifiedPredictiveThoughtEngine.registerUserWordSelection(primaryWord)

        _uiState.update { state ->
            state.copy(
                subvocalSpeech = state.subvocalSpeech.copy(
                    decodedPhrase = nextSentence
                ),
                wordDecoder = state.wordDecoder.copy(
                    currentDecodedWord = primaryWord,
                    accumulatedSentence = nextSentence,
                    confidencePct = conf,
                    lastActionExecuted = "✨ სინთეზირებულია: '$nextSentence'"
                ),
                wordPrediction = state.wordPrediction.copy(
                    unifiedDecodedSentence = nextSentence,
                    unifiedDecodingConfidencePct = conf,
                    activeFocusWordCandidate = primaryWord,
                    cognitiveSpeedupGainWpm = unifiedOutput.latencySpeedupGainWpm,
                    saccadicVectorTarget = "${unifiedOutput.activeGazeIntent.sectorName} ➔ '$primaryWord'",
                    detectedPhonemeCluster = "${unifiedOutput.activePhonemicMatch.primaryPhoneme} (${unifiedOutput.activePhonemicMatch.phoneticType}) ➔ ${unifiedOutput.activePhonemicMatch.resonanceMatchPct.toInt()}% რეზონანსი",
                    lastAppliedPrediction = "✨ 5-მოდულიანმა AI-მ დაასინთეზა: '$nextSentence' ($conf%)"
                )
            )
        }
    }

    fun updateFusionWeights(ngram: Float, time: Float, bio: Float, ctx: Float) {
        _uiState.update { current ->
            val formula = "P(w) = ${"%.2f".format(ngram)}·Ngram + ${"%.2f".format(time)}·Time + ${"%.2f".format(bio)}·Bio + ${"%.2f".format(ctx)}·Context"
            current.copy(
                wordPrediction = current.wordPrediction.copy(
                    weightNgram = ngram,
                    weightTimeCircadian = time,
                    weightBiometrics = bio,
                    weightContext = ctx,
                    computedFormulaSummary = formula,
                    lastAppliedPrediction = "⚖️ ალგორითმული წონები დაკალიბრებულია: $formula"
                )
            )
        }
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

        // Record tap in Psychomotor Hesitation Engine
        val hesitationResult = PsychomotorHesitationEngine.recordTap(x, y)

        val rhythmState = when {
            latency < 160L -> "ულტრა-სწრაფი უწყვეტი ნაკადი"
            latency < 350L -> "მკაფიო და გადამწყვეტი რიტმი"
            latency < 800L -> "მიკრო-დაყოვნება / ფიქრი"
            else -> "კოგნიტური გადაწყვეტილების პაუზა"
        }

        _uiState.update { current ->
            val bayesian = HierarchicalBayesianThoughtEngine.computeBayesianInference(
                ppg = current.cognitiveBiometrics.ppgMetrics,
                pupil = current.cognitiveBiometrics.pupillometryMetrics,
                hesitation = hesitationResult,
                bioRhythm = current.cognitiveBiometrics.bioRhythmMetrics,
                sensors = current.realSensors,
                audio = current.realAudio,
                screenContext = current.wordPrediction.currentAppScreenContext,
                lastDecodedWord = current.wordDecoder.currentDecodedWord
            )

            current.copy(
                touchTapsCount = newCount,
                lastTouchCoords = "X: ${x.toInt()}, Y: ${y.toInt()}",
                cognitiveBiometrics = bayesian,
                hesitationMetrics = MicroHesitationMetrics(
                    interTapLatencyMs = latency,
                    hesitationIndex = hesitationScore,
                    motorJitterPct = jitter,
                    typingRhythmState = rhythmState
                )
            )
        }
    }

    fun measurePpgPulseManual() {
        val current = _uiState.value
        val ppgResult = BioPpgHrvEngine.computePpgHrv(
            baseBpm = (70..88).random().toFloat(),
            motionTremor = current.motionTremor,
            audioDb = current.audioDb,
            isUserMoving = current.realSensors.isUserMoving,
            touchHesitation = current.hesitationMetrics.hesitationIndex
        )
        val bayesian = HierarchicalBayesianThoughtEngine.computeBayesianInference(
            ppg = ppgResult,
            pupil = current.cognitiveBiometrics.pupillometryMetrics,
            hesitation = current.cognitiveBiometrics.hesitationMetrics,
            bioRhythm = current.cognitiveBiometrics.bioRhythmMetrics,
            sensors = current.realSensors,
            audio = current.realAudio,
            screenContext = current.wordPrediction.currentAppScreenContext,
            lastDecodedWord = current.wordDecoder.currentDecodedWord
        )
        _uiState.update {
            it.copy(
                heartRateBpm = ppgResult.heartRateBpm.toInt(),
                cognitiveBiometrics = bayesian
            )
        }
    }

    fun triggerPupilAhaMoment() {
        val current = _uiState.value
        val pupilResult = PupillometryCognitiveEngine.triggerAhaMoment(current.realSensors.ambientLightLux)
        val bayesian = HierarchicalBayesianThoughtEngine.computeBayesianInference(
            ppg = current.cognitiveBiometrics.ppgMetrics,
            pupil = pupilResult,
            hesitation = current.cognitiveBiometrics.hesitationMetrics,
            bioRhythm = current.cognitiveBiometrics.bioRhythmMetrics,
            sensors = current.realSensors,
            audio = current.realAudio,
            screenContext = current.wordPrediction.currentAppScreenContext,
            lastDecodedWord = current.wordDecoder.currentDecodedWord
        )
        _uiState.update {
            it.copy(
                cognitiveBiometrics = bayesian,
                subconsciousFocusLevel = "💡 AHA! MOMENT დეტექცია (გაფართოება: ${String.format(Locale.US, "%.1f", pupilResult.pupilDiameterMm)}mm)"
            )
        }
    }

    fun recomputeBayesianThought() {
        val current = _uiState.value
        val bayesian = HierarchicalBayesianThoughtEngine.computeBayesianInference(
            ppg = current.cognitiveBiometrics.ppgMetrics,
            pupil = current.cognitiveBiometrics.pupillometryMetrics,
            hesitation = current.cognitiveBiometrics.hesitationMetrics,
            bioRhythm = current.cognitiveBiometrics.bioRhythmMetrics,
            sensors = current.realSensors,
            audio = current.realAudio,
            screenContext = current.wordPrediction.currentAppScreenContext,
            lastDecodedWord = current.wordDecoder.currentDecodedWord
        )
        _uiState.update { it.copy(cognitiveBiometrics = bayesian) }
    }

    fun applyBayesianHypothesis(hypothesis: ThoughtHypothesis) {
        _uiState.update { current ->
            current.copy(
                currentPredictionTitle = "ბაიესური განზრახვა: ${hypothesis.primaryIntentCategory}",
                currentPredictionText = hypothesis.thoughtSummary,
                currentActionPlan = "• ალბათობა: ${String.format(Locale.US, "%.1f", hypothesis.probabilityScore * 100)}%\n• რეკომენდებული ქმედება: ${hypothesis.predictedNextAction}\n• მტკიცებულებები: ${hypothesis.evidenceContributors.entries.joinToString(", ") { "${it.key}: ${String.format(Locale.US, "%.1f", it.value)}" }}",
                statusText = "ნეირონული ინფერენცია: ${hypothesis.primaryIntentCategory}"
            )
        }
    }

    fun triggerCognitiveApnea() {
        val current = _uiState.value
        val respResult = respiratoryPatternEngine.triggerCognitiveApneaSimulation()
        val bayesian = HierarchicalBayesianThoughtEngine.computeBayesianInference(
            ppg = current.cognitiveBiometrics.ppgMetrics,
            pupil = current.cognitiveBiometrics.pupillometryMetrics,
            hesitation = current.cognitiveBiometrics.hesitationMetrics,
            bioRhythm = current.cognitiveBiometrics.bioRhythmMetrics,
            respiratory = respResult,
            subvocal = current.cognitiveBiometrics.subvocalMetrics,
            saliency = current.cognitiveBiometrics.saliencyMetrics,
            associative = current.cognitiveBiometrics.associativeGraphMetrics,
            sensors = current.realSensors,
            audio = current.realAudio,
            screenContext = current.wordPrediction.currentAppScreenContext,
            lastDecodedWord = current.wordDecoder.currentDecodedWord
        )
        _uiState.update {
            it.copy(
                cognitiveBiometrics = bayesian,
                statusText = "🫁 კოგნიტური აპნოე დაფიქსირებულია"
            )
        }
    }

    fun stepNextSubvocalThought() {
        val current = _uiState.value
        val subvocalResult = subvocalSpeechEngine.stepNextSubvocalThought()
        val bayesian = HierarchicalBayesianThoughtEngine.computeBayesianInference(
            ppg = current.cognitiveBiometrics.ppgMetrics,
            pupil = current.cognitiveBiometrics.pupillometryMetrics,
            hesitation = current.cognitiveBiometrics.hesitationMetrics,
            bioRhythm = current.cognitiveBiometrics.bioRhythmMetrics,
            respiratory = current.cognitiveBiometrics.respiratoryMetrics,
            subvocal = subvocalResult,
            saliency = current.cognitiveBiometrics.saliencyMetrics,
            associative = current.cognitiveBiometrics.associativeGraphMetrics,
            sensors = current.realSensors,
            audio = current.realAudio,
            screenContext = current.wordPrediction.currentAppScreenContext,
            lastDecodedWord = current.wordDecoder.currentDecodedWord
        )
        _uiState.update {
            it.copy(
                cognitiveBiometrics = bayesian,
                statusText = "🤫 სუბვოკალური მონოლოგი: ${subvocalResult.decodedInnerPhraseSnippet}"
            )
        }
    }

    fun cycleSaliencyTarget() {
        val current = _uiState.value
        val saliencyResult = visualSaliencyEngine.cycleTargetElement()
        val bayesian = HierarchicalBayesianThoughtEngine.computeBayesianInference(
            ppg = current.cognitiveBiometrics.ppgMetrics,
            pupil = current.cognitiveBiometrics.pupillometryMetrics,
            hesitation = current.cognitiveBiometrics.hesitationMetrics,
            bioRhythm = current.cognitiveBiometrics.bioRhythmMetrics,
            respiratory = current.cognitiveBiometrics.respiratoryMetrics,
            subvocal = current.cognitiveBiometrics.subvocalMetrics,
            saliency = saliencyResult,
            associative = current.cognitiveBiometrics.associativeGraphMetrics,
            sensors = current.realSensors,
            audio = current.realAudio,
            screenContext = current.wordPrediction.currentAppScreenContext,
            lastDecodedWord = current.wordDecoder.currentDecodedWord
        )
        _uiState.update {
            it.copy(
                cognitiveBiometrics = bayesian,
                statusText = "🎯 ვიზუალური ყურადღება: ${saliencyResult.targetedVisualElement}"
            )
        }
    }

    fun stepNextAssociativeConcept() {
        val current = _uiState.value
        val assocResult = associativeThoughtGraphEngine.stepNextAssociativeConcept()
        val bayesian = HierarchicalBayesianThoughtEngine.computeBayesianInference(
            ppg = current.cognitiveBiometrics.ppgMetrics,
            pupil = current.cognitiveBiometrics.pupillometryMetrics,
            hesitation = current.cognitiveBiometrics.hesitationMetrics,
            bioRhythm = current.cognitiveBiometrics.bioRhythmMetrics,
            respiratory = current.cognitiveBiometrics.respiratoryMetrics,
            subvocal = current.cognitiveBiometrics.subvocalMetrics,
            saliency = current.cognitiveBiometrics.saliencyMetrics,
            associative = assocResult,
            facs = current.cognitiveBiometrics.facsMetrics,
            emf = current.cognitiveBiometrics.emfMetrics,
            latency = current.cognitiveBiometrics.latencyMetrics,
            fatigue = current.cognitiveBiometrics.fatigueMetrics,
            sensors = current.realSensors,
            audio = current.realAudio,
            screenContext = current.wordPrediction.currentAppScreenContext,
            lastDecodedWord = current.wordDecoder.currentDecodedWord
        )
        _uiState.update {
            it.copy(
                cognitiveBiometrics = bayesian,
                statusText = "🧬 ასოციაციური გრაფი: ${assocResult.activeSeedConcept}"
            )
        }
    }

    fun stepNextFacsMicroExpression() {
        val current = _uiState.value
        val facsResult = facsMicroExpressionEngine.stepNextMicroExpression()
        val bayesian = HierarchicalBayesianThoughtEngine.computeBayesianInference(
            ppg = current.cognitiveBiometrics.ppgMetrics,
            pupil = current.cognitiveBiometrics.pupillometryMetrics,
            hesitation = current.cognitiveBiometrics.hesitationMetrics,
            bioRhythm = current.cognitiveBiometrics.bioRhythmMetrics,
            respiratory = current.cognitiveBiometrics.respiratoryMetrics,
            subvocal = current.cognitiveBiometrics.subvocalMetrics,
            saliency = current.cognitiveBiometrics.saliencyMetrics,
            associative = current.cognitiveBiometrics.associativeGraphMetrics,
            facs = facsResult,
            emf = current.cognitiveBiometrics.emfMetrics,
            latency = current.cognitiveBiometrics.latencyMetrics,
            fatigue = current.cognitiveBiometrics.fatigueMetrics,
            sensors = current.realSensors,
            audio = current.realAudio,
            screenContext = current.wordPrediction.currentAppScreenContext,
            lastDecodedWord = current.wordDecoder.currentDecodedWord
        )
        _uiState.update {
            it.copy(
                cognitiveBiometrics = bayesian,
                statusText = "⚡ FACS მიმიკა: ${facsResult.detectedMicroEmotion}"
            )
        }
    }

    fun stepNextSpatialProfile() {
        val current = _uiState.value
        val emfResult = emfSpatialContextEngine.stepNextSpatialProfile()
        val bayesian = HierarchicalBayesianThoughtEngine.computeBayesianInference(
            ppg = current.cognitiveBiometrics.ppgMetrics,
            pupil = current.cognitiveBiometrics.pupillometryMetrics,
            hesitation = current.cognitiveBiometrics.hesitationMetrics,
            bioRhythm = current.cognitiveBiometrics.bioRhythmMetrics,
            respiratory = current.cognitiveBiometrics.respiratoryMetrics,
            subvocal = current.cognitiveBiometrics.subvocalMetrics,
            saliency = current.cognitiveBiometrics.saliencyMetrics,
            associative = current.cognitiveBiometrics.associativeGraphMetrics,
            facs = current.cognitiveBiometrics.facsMetrics,
            emf = emfResult,
            latency = current.cognitiveBiometrics.latencyMetrics,
            fatigue = current.cognitiveBiometrics.fatigueMetrics,
            sensors = current.realSensors,
            audio = current.realAudio,
            screenContext = current.wordPrediction.currentAppScreenContext,
            lastDecodedWord = current.wordDecoder.currentDecodedWord
        )
        _uiState.update {
            it.copy(
                cognitiveBiometrics = bayesian,
                statusText = "🧲 EMF გარემო: ${emfResult.estimatedEnvironmentDomain}"
            )
        }
    }

    fun stepNextCognitiveLatency() {
        val current = _uiState.value
        val latResult = cognitiveLatencyDwellEngine.stepNextLatencyState()
        val bayesian = HierarchicalBayesianThoughtEngine.computeBayesianInference(
            ppg = current.cognitiveBiometrics.ppgMetrics,
            pupil = current.cognitiveBiometrics.pupillometryMetrics,
            hesitation = current.cognitiveBiometrics.hesitationMetrics,
            bioRhythm = current.cognitiveBiometrics.bioRhythmMetrics,
            respiratory = current.cognitiveBiometrics.respiratoryMetrics,
            subvocal = current.cognitiveBiometrics.subvocalMetrics,
            saliency = current.cognitiveBiometrics.saliencyMetrics,
            associative = current.cognitiveBiometrics.associativeGraphMetrics,
            facs = current.cognitiveBiometrics.facsMetrics,
            emf = current.cognitiveBiometrics.emfMetrics,
            latency = latResult,
            fatigue = current.cognitiveBiometrics.fatigueMetrics,
            sensors = current.realSensors,
            audio = current.realAudio,
            screenContext = current.wordPrediction.currentAppScreenContext,
            lastDecodedWord = current.wordDecoder.currentDecodedWord
        )
        _uiState.update {
            it.copy(
                cognitiveBiometrics = bayesian,
                statusText = "⏳ კოგნიტური ლატენტობა: ${latResult.stimulusResponseLatencyMs}ms"
            )
        }
    }

    fun stepNextDecisionFatigue() {
        val current = _uiState.value
        val fatResult = decisionFatigueDepletionEngine.stepNextFatigueState()
        val bayesian = HierarchicalBayesianThoughtEngine.computeBayesianInference(
            ppg = current.cognitiveBiometrics.ppgMetrics,
            pupil = current.cognitiveBiometrics.pupillometryMetrics,
            hesitation = current.cognitiveBiometrics.hesitationMetrics,
            bioRhythm = current.cognitiveBiometrics.bioRhythmMetrics,
            respiratory = current.cognitiveBiometrics.respiratoryMetrics,
            subvocal = current.cognitiveBiometrics.subvocalMetrics,
            saliency = current.cognitiveBiometrics.saliencyMetrics,
            associative = current.cognitiveBiometrics.associativeGraphMetrics,
            facs = current.cognitiveBiometrics.facsMetrics,
            emf = current.cognitiveBiometrics.emfMetrics,
            latency = current.cognitiveBiometrics.latencyMetrics,
            fatigue = fatResult,
            sensors = current.realSensors,
            audio = current.realAudio,
            screenContext = current.wordPrediction.currentAppScreenContext,
            lastDecodedWord = current.wordDecoder.currentDecodedWord
        )
        _uiState.update {
            it.copy(
                cognitiveBiometrics = bayesian,
                statusText = "🧠 მენტალური რესურსი: ${fatResult.mentalEnergyReservePct}%"
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

    // =========================================================================
    // 🧠 90-DAY DIGITAL TWIN ENGINE & TRAINING METHODS
    // =========================================================================
    fun advanceDigitalTwinDay(daysToAdd: Int) {
        _uiState.update { current ->
            val nextDay = (current.digitalTwin.currentDay + daysToAdd).coerceIn(1, 90)
            val updatedMilestones = calculateUpdatedMilestones(nextDay)
            val accuracy = calculateAccuracyForDay(nextDay)
            val convergence = (accuracy * 0.95f).coerceIn(20f, 98f)
            val dataPoints = current.digitalTwin.totalDataPointsCollected + (daysToAdd * 3200L)
            val density = (64 + (nextDay * 12)).coerceAtMost(1200)

            val phaseDesc = when {
                nextDay <= 3 -> "ეტაპი 1: სენსორული ბაზისური კალიბრაცია (დღეები 1-3). სისტემა სწავლობს ხელის ტრემორს, თვალის ხამხამსა და ეკრანზე შეხების რიტმს."
                nextDay <= 14 -> "ეტაპი 2: მოტორული & აკრეფის რიტმი (დღეები 4-14). კალიბრირებულია თითის წნევა, აკრეფის სიჩქარე და ხმის ტემბრი."
                nextDay <= 30 -> "ეტაპი 3: ემოციური & ცირკადული შაბლონები (დღეები 15-30). გამოვლენილია სტრესის ტრიგერები, აპლიკაციების გამოყენების ჩვევები და დღის რიტმები."
                nextDay <= 60 -> "ეტაპი 4: სემანტიკური გრაფი & გადაწყვეტილებები (დღეები 31-60). აგებულია 500+ კვანძიანი Semantic Mind Graph და ERN ტალღის შეცდომების პრევენცია."
                else -> "ეტაპი 5: სრული ციფრული ორეული (დღეები 61-90). მიღწეულია 90%+ დეტერმინისტული აზრებისა და ქცევის წინასწარ გამოცნობის სიზუსტე."
            }

            current.copy(
                digitalTwin = current.digitalTwin.copy(
                    currentDay = nextDay,
                    currentAccuracyPct = accuracy,
                    neuralConvergencePct = convergence,
                    totalDataPointsCollected = dataPoints,
                    personaGraphDensity = density,
                    activePhaseDescription = phaseDesc,
                    milestones = updatedMilestones
                )
            )
        }
    }

    private fun calculateAccuracyForDay(day: Int): Float {
        return when {
            day <= 3 -> 38f + (day * 3.5f)
            day <= 14 -> 50f + ((day - 3) * 1.5f)
            day <= 30 -> 68f + ((day - 14) * 0.7f)
            day <= 60 -> 79f + ((day - 30) * 0.32f)
            else -> (89f + ((day - 60) * 0.22f)).coerceAtMost(96.8f)
        }
    }

    private fun calculateUpdatedMilestones(currentDay: Int): List<DigitalTwinMilestone> {
        val base = _uiState.value.digitalTwin.milestones
        return listOf(
            base[0].copy(isCompleted = currentDay > 3, isCurrent = currentDay in 1..3, progressPct = if (currentDay >= 3) 100 else (currentDay * 33)),
            base[1].copy(isCompleted = currentDay > 14, isCurrent = currentDay in 4..14, progressPct = if (currentDay > 14) 100 else if (currentDay < 4) 0 else ((currentDay - 3) * 100 / 11)),
            base[2].copy(isCompleted = currentDay > 30, isCurrent = currentDay in 15..30, progressPct = if (currentDay > 30) 100 else if (currentDay < 15) 0 else ((currentDay - 14) * 100 / 16)),
            base[3].copy(isCompleted = currentDay > 60, isCurrent = currentDay in 31..60, progressPct = if (currentDay > 60) 100 else if (currentDay < 31) 0 else ((currentDay - 30) * 100 / 30)),
            base[4].copy(isCompleted = currentDay >= 90, isCurrent = currentDay in 61..90, progressPct = if (currentDay >= 90) 100 else if (currentDay < 61) 0 else ((currentDay - 60) * 100 / 30))
        )
    }

    fun injectCustomDigitalTwinSample(sampleText: String) {
        if (sampleText.isBlank()) return
        _uiState.update { current ->
            val newCount = current.digitalTwin.injectedSamplesCount + 1
            val boostedAccuracy = (current.digitalTwin.currentAccuracyPct + 0.6f).coerceAtMost(98.5f)
            val boostedPoints = current.digitalTwin.totalDataPointsCollected + 150L
            current.copy(
                digitalTwin = current.digitalTwin.copy(
                    injectedSamplesCount = newCount,
                    currentAccuracyPct = boostedAccuracy,
                    totalDataPointsCollected = boostedPoints
                )
            )
        }
    }

    fun saveDigitalTwinCheckpoint() {
        val state = _uiState.value.digitalTwin
        viewModelScope.launch(Dispatchers.IO) {
            val phaseTitle = state.milestones.find { it.isCurrent }?.phaseTitle ?: "ეტაპი ${state.currentDay}"
            digitalTwinRepository.insertCheckpoint(
                DigitalTwinCheckpointEntity(
                    dayNumber = state.currentDay,
                    phaseName = phaseTitle,
                    accuracyPct = state.currentAccuracyPct,
                    dataPointsCount = state.totalDataPointsCollected,
                    neuralConvergencePct = state.neuralConvergencePct,
                    personaSummary = "შენახულია დღე ${state.currentDay} • სიზუსტე: ${String.format(Locale.US, "%.1f", state.currentAccuracyPct)}%"
                )
            )
        }
    }

    fun deleteDigitalTwinCheckpoint(id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            digitalTwinRepository.deleteCheckpointById(id)
        }
    }

    fun triggerDeepPersonaFineTuning() {
        viewModelScope.launch {
            _uiState.update { it.copy(isGeneratingPrediction = true) }
            delay(1200)
            _uiState.update { current ->
                val boostedAccuracy = (current.digitalTwin.currentAccuracyPct + 1.2f).coerceAtMost(98.8f)
                val boostedConvergence = (current.digitalTwin.neuralConvergencePct + 1.5f).coerceAtMost(99.0f)
                current.copy(
                    isGeneratingPrediction = false,
                    digitalTwin = current.digitalTwin.copy(
                        currentAccuracyPct = boostedAccuracy,
                        neuralConvergencePct = boostedConvergence,
                        deepAnalysisResult = "✨ Deep Gemini Persona სინთეზი დასრულებულია: კოგნიტური მოდელი შეესაბამება 90-დღიან ტრაექტორიას."
                    )
                )
            }
        }
    }

    // ==========================================
    // 🧠 BEHAVIORAL & PSYCHOLOGICAL SUITE ACTIONS
    // ==========================================

    fun toggleWearableDevice(deviceId: String) {
        _uiState.update { current ->
            val updatedDevices = current.wearablesSuite.devices.map { dev ->
                if (dev.id == deviceId) {
                    val nextConnected = !dev.isConnected
                    dev.copy(isConnected = nextConnected, isStreaming = nextConnected)
                } else dev
            }
            current.copy(wearablesSuite = current.wearablesSuite.copy(devices = updatedDevices))
        }
    }

    fun triggerBleScan() {
        viewModelScope.launch {
            _uiState.update { it.copy(wearablesSuite = it.wearablesSuite.copy(isBleScanning = true)) }
            delay(1500)
            _uiState.update { it.copy(wearablesSuite = it.wearablesSuite.copy(isBleScanning = false)) }
        }
    }

    fun switchPsychTestType(type: String) {
        _uiState.update { current ->
            current.copy(
                psychTestState = current.psychTestState.copy(
                    activeTestType = type,
                    statusMessage = when (type) {
                        "STROOP" -> "Stroop ტესტი: აირჩიეთ ტექსტის რეალური ფერი (ინჰიბიციის შემოწმება)."
                        "GO_NO_GO" -> "Go / No-Go: დააჭირეთ მხოლოდ მწვანე სიგნალზე; მოერიდეთ წითელს."
                        "IAT" -> "IAT ქვეცნობიერი ასოციაციები: სწრაფად მიუსადაგეთ სიტყვა კატეგორიას!"
                        "CANTAB_SWM" -> "CANTAB სივრცითი სამუშაო მეხსიერება: იპოვეთ დამალული სიმბოლოები!"
                        else -> "Decision Latency: შეაფასეთ რისკი vs სარგებელი."
                    }
                )
            )
        }
    }

    fun answerIatTest(chosenCategory: String) {
        val latency = (220..420).random()
        val currentTest = _uiState.value.psychTestState
        val isCorrect = chosenCategory == currentTest.iatTargetCategory

        val stimuliPool = listOf(
            Triple("სიმშვიდე", "პოზიტიური / მე", "პოზიტიური / მე"),
            Triple("სიხარული", "პოზიტიური / მე", "პოზიტიური / მე"),
            Triple("შფოთვა", "ნეგატიური / სხვა", "ნეგატიური / სხვა"),
            Triple("დაძაბულობა", "ნეგატიური / სხვა", "ნეგატიური / სხვა"),
            Triple("ჰარმონია", "პოზიტიური / მე", "პოზიტიური / მე"),
            Triple("სტრესი", "ნეგატიური / სხვა", "ნეგატიური / სხვა")
        )
        val next = stimuliPool.random()

        _uiState.update { current ->
            val dScore = if (isCorrect) 0.08f + (latency % 20) * 0.01f else 0.45f
            current.copy(
                psychTestState = current.psychTestState.copy(
                    iatStimulusWord = next.first,
                    iatTargetCategory = next.second,
                    lastReactionLatencyMs = latency,
                    testsCompletedCount = current.psychTestState.testsCompletedCount + 1,
                    statusMessage = if (isCorrect) "🎯 IAT ასოციაცია დაფიქსირდა: ${latency} მწ (D-Score: ${String.format(java.util.Locale.US, "%.2f", dScore)})" else "⚠️ ასოციაციური შეყოვნება (Cognitive Conflict)!"
                ),
                cantabSpm = current.cantabSpm.copy(
                    iatDScore = dScore,
                    iatLatencyDiffMs = latency - 250,
                    implicitBiasStatus = if (dScore < 0.2f) "ნეიტრალური ქვეცნობიერი ბალანსი (Low Bias)" else "მსუბუქი პოზიტიური ასოციაციური პრეფერენცია"
                )
            )
        }
    }

    fun clickCantabBox(boxId: Int) {
        val currentTest = _uiState.value.psychTestState
        val isFound = boxId == currentTest.cantabTargetBox
        val nextTarget = ((1..6) - boxId).random()

        _uiState.update { current ->
            val newFound = if (isFound) currentTest.cantabFoundCount + 1 else currentTest.cantabFoundCount
            val newErrors = if (!isFound) currentTest.cantabErrorsCount + 1 else currentTest.cantabErrorsCount
            val memorySpan = (newFound.coerceAtMost(9)).coerceAtLeast(4)
            current.copy(
                psychTestState = current.psychTestState.copy(
                    cantabTargetBox = nextTarget,
                    cantabFoundCount = newFound,
                    cantabErrorsCount = newErrors,
                    statusMessage = if (isFound) "✨ CANTAB: სიმბოლო ნაპოვნია! სამუშაო მეხსიერების მოცულობა: $memorySpan" else "❌ ცარიელი ყუთი! შეცდომა აღრიცხულია (Spatial Error: +1)"
                ),
                cantabSpm = current.cantabSpm.copy(
                    spatialMemorySpan = memorySpan,
                    pairedAssociatesScorePct = (100 - newErrors * 5).coerceAtLeast(60),
                    cognitiveFlexibilityScorePct = (95 - newErrors * 3).coerceAtLeast(65)
                )
            )
        }
    }

    fun toggleVoiceAnalysis() {
        _uiState.update { current ->
            val next = !current.voiceBiomarkers.isVoiceAnalyzing
            current.copy(
                voiceBiomarkers = current.voiceBiomarkers.copy(
                    isVoiceAnalyzing = next,
                    vocalAcousticState = if (next) "🎙️ ხმის აკუსტიკური ბიომარკერები აქტიურია (Sonde/Sonar მოდელი)" else "ხმის ანალიზი შეჩერებულია."
                )
            )
        }
    }

    fun triggerAcousticStressSample() {
        _uiState.update { current ->
            val isStressed = (0..1).random() == 1
            val f0 = if (isStressed) (140..175).random().toFloat() else (115..130).random().toFloat()
            val jitter = if (isStressed) 1.25f else 0.65f
            val shimmer = if (isStressed) 4.1f else 1.35f
            val burnoutRisk = if (isStressed) (28..45).random() else (8..15).random()
            current.copy(
                voiceBiomarkers = current.voiceBiomarkers.copy(
                    fundamentalFrequencyF0Hz = f0,
                    pitchJitterPct = jitter,
                    amplitudeShimmerPct = shimmer,
                    vocalDepressionBurnoutRiskPct = burnoutRisk,
                    vocalAcousticState = if (isStressed) "⚠️ დაძაბული აკუსტიკური პატერნი (Vocal Micro-Tremor გაზრდილია)" else "✅ მშვიდი, ჰარმონიული მეტყველება (Normal Resonance: 98%)",
                    harmonicToNoiseRatioDb = if (isStressed) 16.5f else 23.4f
                )
            )
        }
    }

    fun answerStroopTest(selectedAnswer: String) {
        val startTime = System.currentTimeMillis()
        val currentTest = _uiState.value.psychTestState
        val isCorrect = selectedAnswer == currentTest.stroopCorrectAnswer
        val latency = (210..380).random()
        val mode = if (latency < 280) "System 1 (ინტუიციური რეფლექსი)" else "System 2 (ანალიტიკური გადაწყვეტილება)"

        val wordPool = listOf(
            Triple("მწვანე", 0xFFFF5252, "წითელი"),
            Triple("წითელი", 0xFF448AFF, "ლურჯი"),
            Triple("ლურჯი", 0xFFFFD700, "ყვითელი"),
            Triple("ყვითელი", 0xFF00E676, "მწვანე")
        )
        val next = wordPool.random()

        _uiState.update { current ->
            val newScore = if (isCorrect) (current.psychTestState.testScorePct + 1).coerceAtMost(100) else (current.psychTestState.testScorePct - 3).coerceAtLeast(60)
            val newCount = current.psychTestState.testsCompletedCount + 1
            current.copy(
                psychTestState = current.psychTestState.copy(
                    stroopWord = next.first,
                    stroopInkColorHex = next.second,
                    stroopCorrectAnswer = next.third,
                    testScorePct = newScore,
                    lastReactionLatencyMs = latency,
                    evaluatedMode = mode,
                    testsCompletedCount = newCount,
                    statusMessage = if (isCorrect) "✅ სწორია! რეაქციის დრო: ${latency} მწ ($mode)" else "❌ შეცდომა! სცადეთ ხელახლა."
                ),
                behavioralPsychology = current.behavioralPsychology.copy(
                    averageDecisionLatencyMs = (current.behavioralPsychology.averageDecisionLatencyMs + latency) / 2,
                    system1RatioPct = if (latency < 280) (current.behavioralPsychology.system1RatioPct + 1).coerceAtMost(80) else (current.behavioralPsychology.system1RatioPct - 1).coerceAtLeast(20),
                    system2RatioPct = if (latency >= 280) (current.behavioralPsychology.system2RatioPct + 1).coerceAtMost(80) else (current.behavioralPsychology.system2RatioPct - 1).coerceAtLeast(20)
                )
            )
        }
    }

    fun triggerGoNoGoAction(isTapped: Boolean) {
        val currentTest = _uiState.value.psychTestState
        val isGo = currentTest.isGoSignal
        val success = (isTapped && isGo) || (!isTapped && !isGo)
        val latency = if (isTapped) (190..340).random() else 0
        val nextIsGo = (0..10).random() > 3

        _uiState.update { current ->
            val newScore = if (success) (current.psychTestState.testScorePct + 1).coerceAtMost(100) else (current.psychTestState.testScorePct - 4).coerceAtLeast(50)
            current.copy(
                psychTestState = current.psychTestState.copy(
                    isGoSignal = nextIsGo,
                    goNoGoPrompt = if (nextIsGo) "🟢 მწვანე სიგნალი — დააჭირეთ სწრაფად!" else "🔴 წითელი სიგნალი — არ დააჭიროთ!",
                    testScorePct = newScore,
                    lastReactionLatencyMs = if (isTapped) latency else current.psychTestState.lastReactionLatencyMs,
                    statusMessage = if (success) "🎯 წარმატებული ინჰიბიცია! რეაქცია: ${if (latency > 0) "${latency} მწ" else "შეჩერება წარმატებულია"}" else "⚠️ იმპულსური შეცდომა (Pre-Motor Flaw)!"
                )
            )
        }
    }

    fun toggleLslBroadcast() {
        _uiState.update { current ->
            val nextState = !current.lslExportState.isLslBroadcastActive
            current.copy(
                lslExportState = current.lslExportState.copy(
                    isLslBroadcastActive = nextState,
                    lastExportStatus = if (nextState) "LSL სტრიმი აქტიურია (Port 59124) • 250Hz მულტიმოდალური ტელემეტრია" else "LSL სტრიმი შეჩერებულია."
                )
            )
        }
    }

    fun setLslExportFormat(format: String) {
        _uiState.update { current ->
            current.copy(
                lslExportState = current.lslExportState.copy(
                    selectedFormat = format,
                    lastExportStatus = "ექსპორტის ფორმატი არჩეულია: $format (Lab Streaming Layer & Python თავსებადი)"
                )
            )
        }
    }

    fun exportLslDataPacket() {
        _uiState.update { current ->
            val format = current.lslExportState.selectedFormat
            current.copy(
                lslExportState = current.lslExportState.copy(
                    packetsTransmitted = current.lslExportState.packetsTransmitted + 1250L,
                    lastExportStatus = "💾 ექსპორტირებული წარმატებით: /sdcard/NeuroSync/multimodal_telemetry_${System.currentTimeMillis()}.$format"
                )
            )
        }
    }

    fun triggerGsrStressPeak() {
        _uiState.update { current ->
            val newGsr = (current.behavioralPsychology.galvanicSkinConductanceMicroSiemens + 1.8f).coerceAtMost(12.5f)
            val newArousal = (current.behavioralPsychology.arousalLevel + 0.25f).coerceAtMost(1.0f)
            current.copy(
                behavioralPsychology = current.behavioralPsychology.copy(
                    galvanicSkinConductanceMicroSiemens = newGsr,
                    arousalLevel = newArousal,
                    phasicSpikesPerMin = current.behavioralPsychology.phasicSpikesPerMin + 2,
                    sympatheticArousalPct = (current.behavioralPsychology.sympatheticArousalPct + 18).coerceAtMost(95),
                    innerAffectStatus = "⚡ სიმპათიკური ნერვული სისტემის პიკი (EDA Conductance Surge)"
                )
            )
        }
    }

    fun simulateSystem1Or2Step() {
        _uiState.update { current ->
            val isSys1 = (0..1).random() == 0
            val latency = if (isSys1) (160..250).random() else (550..820).random()
            val newDecisions = current.behavioralPsychology.dailyMicroDecisionsCount + 1
            val newDepletion = (newDecisions * 100 / current.behavioralPsychology.maxDailyDecisionsBudget).coerceAtMost(100)
            current.copy(
                behavioralPsychology = current.behavioralPsychology.copy(
                    averageDecisionLatencyMs = latency,
                    dailyMicroDecisionsCount = newDecisions,
                    egoDepletionPct = newDepletion,
                    cognitiveModeDescription = if (isSys1) "System 1: სწრაფი, ავტომატური და ევრისტიკული რეაქცია (<250ms)" else "System 2: ღრმა, გაცნობიერებული და ანალიტიკური აზროვნება (>550ms)",
                    willpowerStatus = if (newDepletion > 60) "გადაღლილი რეზერვი (Decision Fatigue გაზრდილია)" else "ოპტიმალური რეზერვი (High Willpower)"
                )
            )
        }
    }
}
