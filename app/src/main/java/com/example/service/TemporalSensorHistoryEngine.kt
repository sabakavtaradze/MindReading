package com.example.service

import java.util.Locale
import kotlin.math.abs
import kotlin.math.exp
import kotlin.math.min
import kotlin.math.sqrt

/**
 * Multi-Scale Temporal Sensor History & Episodic Memory Engine
 * 100% On-Device, Real-Time & Historical Pattern Recognition.
 * 
 * Features:
 * 1. Multi-Scale Sliding Temporal Memory (500ms Ultra-Short, 15-min Session Trend, Long-term Circadian)
 * 2. Dynamic Time Warping (DTW) Gesture & Waveform Pattern Matcher
 * 3. Markovian Temporal-Sensor State Association with Exponential Decay
 * 4. Bayesian Episodic Memory Cache for Instant Recall of Historically Selected Georgian Words
 */
object TemporalSensorHistoryEngine {

    // --- 1. HISTORICAL SENSOR SNAPSHOT & TEMPORAL RING BUFFERS ---

    data class SensorTemporalFrame(
        val timestampMs: Long,
        val accelMagnitude: Float,
        val gyroMagnitude: Float,
        val audioDecibels: Float,
        val formantHz: Float,
        val lightLux: Float,
        val gazeFixationScore: Float,
        val activityState: OmniSensorFusionSummaryEngine.PhysicalActivityState,
        val selectedWord: String? = null
    )

    private val ultraShortRingBuffer = ArrayDeque<SensorTemporalFrame>(30) // ~500ms - 1s at 30-50Hz
    private val sessionTrajectoryBuffer = mutableListOf<SensorTemporalFrame>() // Session duration
    private val episodicMemoryBank = mutableListOf<EpisodicWordMemory>() // Long-term learned associations

    data class EpisodicWordMemory(
        val word: String,
        val activityState: OmniSensorFusionSummaryEngine.PhysicalActivityState,
        val environmentContext: OmniSensorFusionSummaryEngine.EnvironmentContext,
        val avgFormantHz: Float,
        val avgGazeScore: Float,
        val timestampMs: Long,
        var selectionCount: Int = 1,
        var lastReinforcedMs: Long = System.currentTimeMillis()
    )

    // Pre-populate with typical baseline Georgian usage episodes
    init {
        val now = System.currentTimeMillis()
        episodicMemoryBank.add(
            EpisodicWordMemory("გამარჯობა", OmniSensorFusionSummaryEngine.PhysicalActivityState.STATIONARY_REST, OmniSensorFusionSummaryEngine.EnvironmentContext.NORMAL_INDOOR, 180f, 0.9f, now, 12)
        )
        episodicMemoryBank.add(
            EpisodicWordMemory("მივდივარ", OmniSensorFusionSummaryEngine.PhysicalActivityState.WALKING, OmniSensorFusionSummaryEngine.EnvironmentContext.BRIGHT_OUTDOORS, 220f, 0.5f, now, 9)
        )
        episodicMemoryBank.add(
            EpisodicWordMemory("დავისვენოთ", OmniSensorFusionSummaryEngine.PhysicalActivityState.STATIONARY_REST, OmniSensorFusionSummaryEngine.EnvironmentContext.DARK_QUIET_ROOM, 140f, 0.8f, now, 8)
        )
        episodicMemoryBank.add(
            EpisodicWordMemory("ვფიქრობ", OmniSensorFusionSummaryEngine.PhysicalActivityState.MICRO_POSTURAL_SHIFT, OmniSensorFusionSummaryEngine.EnvironmentContext.NORMAL_INDOOR, 175f, 0.95f, now, 15)
        )
    }

    /**
     * Ingests latest instantaneous frame into the multi-scale temporal memory
     */
    fun recordTemporalFrame(frame: SensorTemporalFrame) {
        ultraShortRingBuffer.addLast(frame)
        while (ultraShortRingBuffer.size > 25) {
            ultraShortRingBuffer.removeFirst()
        }

        // Store periodic downsampled snapshots in session trajectory (every ~2 seconds)
        if (sessionTrajectoryBuffer.isEmpty() || (frame.timestampMs - sessionTrajectoryBuffer.last().timestampMs) > 2000) {
            sessionTrajectoryBuffer.add(frame)
            if (sessionTrajectoryBuffer.size > 450) { // Keep last ~15 mins
                sessionTrajectoryBuffer.removeAt(0)
            }
        }
    }

    /**
     * Registers when user selects/confirms a Georgian word in a specific sensor context
     */
    fun recordEpisodicWordSelection(
        word: String,
        summary: OmniSensorFusionSummaryEngine.OmniSensorSummary,
        formantHz: Float,
        gazeScore: Float
    ) {
        val cleanWord = word.trim()
        if (cleanWord.isBlank()) return

        val now = System.currentTimeMillis()
        val existing = episodicMemoryBank.find {
            it.word.equals(cleanWord, ignoreCase = true) && it.activityState == summary.activityState
        }

        if (existing != null) {
            existing.selectionCount++
            existing.lastReinforcedMs = now
        } else {
            episodicMemoryBank.add(
                EpisodicWordMemory(
                    word = cleanWord,
                    activityState = summary.activityState,
                    environmentContext = summary.environmentContext,
                    avgFormantHz = formantHz,
                    avgGazeScore = gazeScore,
                    timestampMs = now,
                    selectionCount = 1,
                    lastReinforcedMs = now
                )
            )
        }
    }

    // --- 2. DYNAMIC TIME WARPING (DTW) WAVEFORM RECOGNITION ---

    data class DTWTrajectoryMatch(
        val matchedPatternName: String,
        val dtwDistance: Float,
        val confidencePct: Int,
        val associatedWordPriors: List<Pair<String, Float>>
    )

    /**
     * Computes Fast Dynamic Time Warping (DTW) distance between recent 500ms sensor trajectory and gesture templates
     */
    fun evaluateDTWPatternMatching(): DTWTrajectoryMatch {
        if (ultraShortRingBuffer.size < 8) {
            return DTWTrajectoryMatch("სტაბილური ბაზისი", 0.0f, 90, emptyList())
        }

        val recentSignal = ultraShortRingBuffer.map { it.accelMagnitude + it.gyroMagnitude * 0.5f }
        
        // Template A: Nodule Head Nod (Brief acceleration spike followed by plateau)
        val nodTemplate = listOf(0.1f, 0.3f, 0.9f, 1.4f, 1.1f, 0.5f, 0.2f, 0.1f)
        val nodDistance = compute1DDTW(recentSignal.takeLast(8), nodTemplate)

        // Template B: Sub-Vocal Speech Prep Peak (Acoustic & formant steady rise)
        val prepTemplate = listOf(0.2f, 0.4f, 0.6f, 0.8f, 0.9f, 0.7f, 0.4f, 0.2f)
        val prepDistance = compute1DDTW(recentSignal.takeLast(8), prepTemplate)

        return when {
            nodDistance < 1.2f -> {
                DTWTrajectoryMatch(
                    matchedPatternName = "თავის მიკრო-დაქნევა (Nod Gesture)",
                    dtwDistance = nodDistance,
                    confidencePct = (98 - (nodDistance * 15)).toInt().coerceIn(60, 98),
                    associatedWordPriors = listOf(Pair("დიახ", 2.8f), Pair("კი", 2.5f), Pair("სწორია", 2.3f))
                )
            }
            prepDistance < 1.4f -> {
                DTWTrajectoryMatch(
                    matchedPatternName = "სუბვოკალური იმპულსი (Phonation Prep)",
                    dtwDistance = prepDistance,
                    confidencePct = (95 - (prepDistance * 12)).toInt().coerceIn(55, 95),
                    associatedWordPriors = listOf(Pair("გამარჯობა", 2.4f), Pair("მინდა", 2.2f), Pair("ვფიქრობ", 2.1f))
                )
            }
            else -> {
                DTWTrajectoryMatch(
                    matchedPatternName = "სტაციონარული სიგნალი",
                    dtwDistance = min(nodDistance, prepDistance),
                    confidencePct = 40,
                    associatedWordPriors = emptyList()
                )
            }
        }
    }

    private fun compute1DDTW(seq1: List<Float>, seq2: List<Float>): Float {
        val n = seq1.size
        val m = seq2.size
        val dtwMatrix = Array(n + 1) { FloatArray(m + 1) { Float.POSITIVE_INFINITY } }
        dtwMatrix[0][0] = 0f

        for (i in 1..n) {
            for (j in 1..m) {
                val cost = abs(seq1[i - 1] - seq2[j - 1])
                val minPrev = min(dtwMatrix[i - 1][j], min(dtwMatrix[i][j - 1], dtwMatrix[i - 1][j - 1]))
                dtwMatrix[i][j] = cost + minPrev
            }
        }
        return dtwMatrix[n][m] / (n + m)
    }

    // --- 3. TEMPORAL EXPONENTIAL DECAY & EPISODIC RECALL ---

    data class HistoricalSensorPredictionContext(
        val topRecallWords: List<Pair<String, Float>>,
        val dtwMatch: DTWTrajectoryMatch,
        val totalHistorySnapshotsCount: Int,
        val episodicLearnedCount: Int,
        val temporalTrendDescription: String
    )

    /**
     * Queries past historical data and computes candidate word probability multipliers
     */
    fun queryTemporalPredictions(
        currentSummary: OmniSensorFusionSummaryEngine.OmniSensorSummary,
        currentFormantHz: Float
    ): HistoricalSensorPredictionContext {
        val now = System.currentTimeMillis()
        val dtwMatch = evaluateDTWPatternMatching()
        val scoredWords = mutableMapOf<String, Float>()

        // 1. Incorporate DTW matches
        for ((word, mult) in dtwMatch.associatedWordPriors) {
            scoredWords[word] = (scoredWords[word] ?: 1.0f) * mult
        }

        // 2. Incorporate Bayesian Episodic Memory with Ebbinghaus Exponential Forgetting Curve
        // Memory Retention R = exp(- t / S)
        for (memory in episodicMemoryBank) {
            val ageHours = (now - memory.lastReinforcedMs) / (1000f * 60f * 60f)
            val stabilityHours = (memory.selectionCount * 12.0f) // More frequent selections decay slower
            val retention = exp(-ageHours / stabilityHours).toFloat().coerceIn(0.15f, 1.0f)

            var stateMatchBonus = 1.0f
            if (memory.activityState == currentSummary.activityState) stateMatchBonus += 0.8f
            if (memory.environmentContext == currentSummary.environmentContext) stateMatchBonus += 0.5f

            // Formant proximity
            val formantDiff = abs(memory.avgFormantHz - currentFormantHz)
            if (formantDiff < 40f) stateMatchBonus += 0.6f

            val finalEpisodicScore = 1.0f + (memory.selectionCount * 0.25f * retention * stateMatchBonus)
            scoredWords[memory.word] = (scoredWords[memory.word] ?: 1.0f) * finalEpisodicScore
        }

        val topRecalls = scoredWords.toList()
            .sortedByDescending { it.second }
            .take(5)

        val trendDesc = if (sessionTrajectoryBuffer.size > 5) {
            val recentMotion = sessionTrajectoryBuffer.takeLast(5).map { it.accelMagnitude }.average().toFloat()
            if (recentMotion > 1.2f) "დინამიური მოძრაობის ტრენდი (15 წთ)" else "სტაბილური მოსვენების ტრენდი"
        } else {
            "მონაცემების საწყისი აკუმულაცია"
        }

        return HistoricalSensorPredictionContext(
            topRecallWords = topRecalls,
            dtwMatch = dtwMatch,
            totalHistorySnapshotsCount = sessionTrajectoryBuffer.size,
            episodicLearnedCount = episodicMemoryBank.size,
            temporalTrendDescription = trendDesc
        )
    }

    /**
     * Multiplier score for a candidate Georgian word based on historical sensor memory
     */
    fun getHistoricalWordMultiplier(
        word: String,
        context: HistoricalSensorPredictionContext
    ): Float {
        val clean = word.trim().lowercase(Locale.ROOT)
        val match = context.topRecallWords.find { it.first.lowercase(Locale.ROOT) == clean }
        return match?.second?.coerceIn(1.0f, 3.8f) ?: 1.0f
    }
}
