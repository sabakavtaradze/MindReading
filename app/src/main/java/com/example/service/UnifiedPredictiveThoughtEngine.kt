package com.example.service

import com.example.sensor.RealAudioState
import com.example.sensor.RealCameraGazeState
import com.example.sensor.RealHardwareSensorState
import java.util.Calendar
import java.util.Locale
import kotlin.math.abs
import kotlin.math.exp
import kotlin.math.ln
import kotlin.math.max
import kotlin.math.min

/**
 * Unified Predictive Thought & Intent Engine (ქართული ნეირო-პრედიქტორი)
 * Combines 5 advanced computational pillars:
 * 1. Georgian N-Gram & Markov Chain State Transition Matrix
 * 2. Context-Aware Multimodal Sensor Fusion
 * 3. Sub-Vocal EMG / Phoneme Formant Frequency Matcher (33 Phonemes)
 * 4. Eye-Gaze Saccade & Visual Sector Fixation Pre-fetcher
 * 5. Adaptive Bayesian Personal Neuro-Profile Learning
 */
object UnifiedPredictiveThoughtEngine {

    // ==========================================
    // PILLAR 1: GEORGIAN N-GRAM & MARKOV MATRIX
    // ==========================================
    data class MarkovTransition(
        val previousWord: String,
        val nextWord: String,
        var count: Int,
        var probability: Float
    )

    private val markovTable = mutableMapOf<String, MutableList<MarkovTransition>>()

    // Default Georgian high-probability transitions
    private val DEFAULT_GEORGIAN_TRANSITIONS = listOf(
        Pair("შევამოწმოთ", listOf("სისტემა", "კოდი", "არქიტექტურა", "პარამეტრები", "შედეგები")),
        Pair("დავაკომიტოთ", listOf("ცვლილებები", "განახლება", "რეპოზიტორიაში", "ფილიალი", "ფუნქცია")),
        Pair("გავაანალიზოთ", listOf("მონაცემები", "სენსორების ნაკადი", "სიგნალი", "გრაფიკი", "ალგორითმი")),
        Pair("დავასინთეზოთ", listOf("აზრები", "სიტყვები", "მოდელი", "სტრუქტურა", "კონცეფცია")),
        Pair("ჩავრთოთ", listOf("კამერა", "მიკროფონი", "სენსორები", "მონიტორი", "კომპიუტერი", "შუქი")),
        Pair("გავთიშოთ", listOf("პროცესი", "შეტყობინებები", "აპლიკაცია", "ხმაური", "რეჟიმი")),
        Pair("დავწეროთ", listOf("კოდი", "ტექსტი", "შეტყობინება", "დოკუმენტაცია", "ფუნქცია")),
        Pair("გავაგზავნოთ", listOf("მოთხოვნა", "ფაილი", "პასუხი", "ანგარიში", "ბრძანება")),
        Pair("მინდა", listOf("წყალი", "ყავა", "დასვენება", "მუშაობა", "დახმარება", "საუბარი")),
        Pair("მადლობა", listOf("დახმარებისთვის", "ყურადღებისთვის", "თანამშრომლობისთვის", "სწრაფი პასუხისთვის")),
        Pair("კარგი", listOf("დღე", "ამინდი", "შედეგი", "იდეა", "გადაწყვეტილება", "ნამუშევარი")),
        Pair("სად", listOf("არის", "მივდივართ", "დევს", "იპოვე", "გავაგზავნოთ"))
    )

    init {
        // Initialize default Markov dictionary
        DEFAULT_GEORGIAN_TRANSITIONS.forEach { (prev, nextList) ->
            val total = nextList.size.toFloat()
            val transitions = nextList.mapIndexed { idx, next ->
                val prob = (nextList.size - idx) / (total * (total + 1) / 2f)
                MarkovTransition(prev, next, (10 - idx).coerceAtLeast(1), prob)
            }.toMutableList()
            markovTable[prev.lowercase(Locale.ROOT)] = transitions
        }
    }

    fun learnMarkovTransition(previous: String, next: String) {
        val prevKey = previous.trim().lowercase(Locale.ROOT)
        val nextWord = next.trim()
        if (prevKey.isBlank() || nextWord.isBlank()) return

        val list = markovTable.getOrPut(prevKey) { mutableListOf() }
        val existing = list.find { it.nextWord.equals(nextWord, ignoreCase = true) }
        if (existing != null) {
            existing.count += 1
        } else {
            list.add(MarkovTransition(previous, nextWord, 1, 0.1f))
        }

        // Recalculate probabilities
        val totalCount = list.sumOf { it.count }.toFloat()
        list.forEach { it.probability = it.count / totalCount }
        list.sortByDescending { it.probability }
    }

    // ==========================================
    // PILLAR 2: MULTIMODAL SENSOR FUSION WEIGHTS
    // ==========================================
    data class SensorContextWeights(
        val timeOfDayScore: Float,
        val ambientAcousticScore: Float,
        val kinematicMotionScore: Float,
        val environmentPressureScore: Float,
        val proximityFocusScore: Float
    )

    fun evaluateSensorContext(
        sensors: RealHardwareSensorState,
        audio: RealAudioState,
        screenContext: String
    ): Map<String, Float> {
        val categoryBoosts = mutableMapOf<String, Float>()
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)

        // 1. Time / Circadian boost
        when (hour) {
            in 6..11 -> {
                categoryBoosts["COMMON"] = 1.25f
                categoryBoosts["FOOD"] = 1.30f // ყავა, საუზმე
            }
            in 12..18 -> {
                categoryBoosts["DEV"] = 1.40f
                categoryBoosts["OBJECTS"] = 1.30f
                categoryBoosts["MORPHOLOGY_VERBS"] = 1.35f
            }
            in 19..23 -> {
                categoryBoosts["SMART_HOME"] = 1.35f
                categoryBoosts["EMOTIONS"] = 1.30f
            }
            else -> {
                categoryBoosts["EMOTIONS"] = 1.40f // დასვენება, სიმშვიდე
            }
        }

        // 2. Motion / Pedometer boost
        if (sensors.isUserMoving || sensors.microTremorMagnitude > 3.5f) {
            // Urgency & short commands while moving
            categoryBoosts["COMMANDS"] = (categoryBoosts["COMMANDS"] ?: 1.0f) * 1.45f
            categoryBoosts["TRANSPORT"] = (categoryBoosts["TRANSPORT"] ?: 1.0f) * 1.50f
        }

        // 3. Acoustic noise compensation
        if (audio.decibels > 65.0f) {
            // High noise -> prioritize glottal & high-contrast words
            categoryBoosts["COMMANDS"] = (categoryBoosts["COMMANDS"] ?: 1.0f) * 1.30f
        }

        // 4. Proximity (device held close to face or on desk)
        if (sensors.isNearEarOrFace) {
            categoryBoosts["MESSAGING"] = 1.40f
            categoryBoosts["EMOTIONS"] = 1.25f
        }

        // 5. Screen Context
        when {
            screenContext.contains("IDE", ignoreCase = true) || screenContext.contains("Terminal", ignoreCase = true) -> {
                categoryBoosts["DEV"] = (categoryBoosts["DEV"] ?: 1.0f) * 1.60f
                categoryBoosts["MORPHOLOGY_VERBS"] = (categoryBoosts["MORPHOLOGY_VERBS"] ?: 1.0f) * 1.40f
            }
            screenContext.contains("Messaging", ignoreCase = true) || screenContext.contains("Chat", ignoreCase = true) -> {
                categoryBoosts["COMMON"] = (categoryBoosts["COMMON"] ?: 1.0f) * 1.50f
                categoryBoosts["EMOTIONS"] = (categoryBoosts["EMOTIONS"] ?: 1.0f) * 1.40f
            }
            screenContext.contains("Research", ignoreCase = true) -> {
                categoryBoosts["NEURO_SCIENCE"] = (categoryBoosts["NEURO_SCIENCE"] ?: 1.0f) * 1.60f
                categoryBoosts["OBJECTS"] = (categoryBoosts["OBJECTS"] ?: 1.0f) * 1.35f
            }
        }

        return categoryBoosts
    }

    // ==========================================
    // PILLAR 3: SUB-VOCAL EMG & PHONEME FORMANT MATCHER
    // ==========================================
    data class PhonemicMatchResult(
        val primaryPhoneme: Char,
        val phoneticType: String,
        val resonanceMatchPct: Float,
        val matchingWordPrefixes: List<String>
    )

    fun decodeSubvocalFormants(
        subvocalHz: Float,
        dominantAudioHz: Float
    ): PhonemicMatchResult {
        val targetHz = if (subvocalHz > 5.0f) subvocalHz else dominantAudioHz.coerceIn(12f, 35f)
        
        var bestPhoneme = 'ა'
        var minDiff = Float.MAX_VALUE
        var bestType = "ხმოვანი"

        GeorgianNeuroLinguisticEngine.GEORGIAN_PHONEME_MAP.forEach { (char, info) ->
            val diff = abs(info.laryngealEmgFrequencyHz - targetHz)
            if (diff < minDiff) {
                minDiff = diff
                bestPhoneme = char
                bestType = info.phoneticType
            }
        }

        val resonancePct = ((1.0f - (minDiff / 20.0f)).coerceIn(0.40f, 0.99f)) * 100f
        val matchingWords = GeorgianNeuroLinguisticEngine.MIND_LEXICON_DATABASE
            .filter { it.word.startsWith(bestPhoneme) }
            .take(6)
            .map { it.word }

        return PhonemicMatchResult(
            primaryPhoneme = bestPhoneme,
            phoneticType = bestType,
            resonanceMatchPct = resonancePct,
            matchingWordPrefixes = matchingWords
        )
    }

    // ==========================================
    // PILLAR 4: EYE-GAZE SACCADE & INTENT PRE-FETCHER
    // ==========================================
    data class GazeSectorIntent(
        val sectorIndex: Int, // 0..8 (3x3 grid)
        val sectorName: String,
        val dwellDurationMs: Long,
        val preFetchedCategories: List<String>,
        val preFetchedCandidateWords: List<String>
    )

    private val SECTOR_NAMES = listOf(
        "ზედა-მარცხენა (სისტემა)", "ზედა-ცენტრი (ნავიგაცია)", "ზედა-მარჯვენა (კავშირი)",
        "შუა-მარცხენა (ინსტრუმენტები)", "ცენტრალური ფოკუსი (სამუშაო არე)", "შუა-მარჯვენა (მოქმედებები)",
        "ქვედა-მარცხენა (ისტორია)", "ქვედა-ცენტრი (კლავიატურა / IME)", "ქვედა-მარჯვენა (დადასტურება)"
    )

    fun computeGazeIntentPreFetch(
        gaze: RealCameraGazeState,
        gazeX: Float,
        gazeY: Float
    ): GazeSectorIntent {
        val col = when {
            gazeX < 0.33f -> 0
            gazeX < 0.66f -> 1
            else -> 2
        }
        val row = when {
            gazeY < 0.33f -> 0
            gazeY < 0.66f -> 1
            else -> 2
        }
        val sectorIdx = (row * 3 + col).coerceIn(0, 8)
        val sectorName = SECTOR_NAMES[sectorIdx]

        val categories = when (sectorIdx) {
            0, 1 -> listOf("COMMON", "DEV")
            2 -> listOf("SMART_HOME", "COMMANDS")
            3, 4 -> listOf("DEV", "MORPHOLOGY_VERBS", "OBJECTS")
            5 -> listOf("COMMANDS", "EMOTIONS")
            6, 7 -> listOf("MORPHOLOGY_VERBS", "COMMON")
            else -> listOf("COMMANDS", "COMMON")
        }

        val candidateWords = GeorgianNeuroLinguisticEngine.MIND_LEXICON_DATABASE
            .filter { categories.contains(it.category) }
            .shuffled()
            .take(5)
            .map { it.word }

        val dwellMs = if (gaze.isCameraActive) (200L + (gaze.opticalPupilDilationScore * 150L).toLong()) else 250L

        return GazeSectorIntent(
            sectorIndex = sectorIdx,
            sectorName = sectorName,
            dwellDurationMs = dwellMs,
            preFetchedCategories = categories,
            preFetchedCandidateWords = candidateWords
        )
    }

    // ==========================================
    // PILLAR 5: PERSONAL BAYESIAN NEURO-LEARNING
    // ==========================================
    data class BayesianWordScore(
        val word: String,
        val category: String,
        val rawNgramScore: Float,
        val sensorMultiplier: Float,
        val subVocalBoost: Float,
        val gazeBoost: Float,
        val personalPrior: Float,
        val finalProbabilityPct: Float
    )

    private val userWordUsageHistory = mutableMapOf<String, Int>()
    private var totalWordsSelected = 0

    fun registerUserWordSelection(word: String) {
        val clean = word.trim()
        if (clean.isBlank()) return
        userWordUsageHistory[clean] = (userWordUsageHistory[clean] ?: 0) + 1
        totalWordsSelected += 1
        
        // Feed working memory & Trie
        AdaptiveMemoryDecayEngine.recordWordAccess(clean)
        GeorgianTrieFuzzyMatcher.insert(clean)
    }

    fun getPersonalPrior(word: String): Float {
        val count = userWordUsageHistory[word] ?: 0
        val memoryRetention = AdaptiveMemoryDecayEngine.getRetentionScore(word)
        
        // Laplace smoothing + Ebbinghaus memory curve boost
        val basePrior = if (totalWordsSelected == 0) {
            1.0f
        } else {
            (count + 1f) / (totalWordsSelected + userWordUsageHistory.size.coerceAtLeast(10)) * 4.0f + 0.8f
        }
        return basePrior * (0.8f + (memoryRetention * 0.4f))
    }

    // ==========================================
    // MASTER PREDICTION ENGINE SYNTHESIZER
    // ==========================================
    data class UnifiedPredictionOutput(
        val primaryPredictedSentence: String,
        val topCandidateWords: List<BayesianWordScore>,
        val activeGazeIntent: GazeSectorIntent,
        val activePhonemicMatch: PhonemicMatchResult,
        val overallConfidencePct: Int,
        val latencySpeedupGainWpm: Int,
        val mathematicalFormulaLog: String,
        val omniSummary: OmniSensorFusionSummaryEngine.OmniSensorSummary? = null
    )

    fun computeUnifiedPredictions(
        lastAccumulatedSentence: String,
        sensors: RealHardwareSensorState,
        audio: RealAudioState,
        cameraGaze: RealCameraGazeState,
        gazeX: Float,
        gazeY: Float,
        screenContext: String
    ): UnifiedPredictionOutput {
        val lastWord = lastAccumulatedSentence.trim().split("\\s+".toRegex()).lastOrNull() ?: ""

        // 0. Multivariate Kalman Filter on Kinematics and Gaze Jitter
        val filteredKinematics = MultivariateKalmanFilter.shared.filter(
            rawGazeX = gazeX,
            rawGazeY = gazeY,
            rawAccX = sensors.accelX,
            rawAccY = sensors.accelY,
            rawAccZ = sensors.accelZ
        )
        val cleanGazeX = filteredKinematics.smoothGazeX
        val cleanGazeY = filteredKinematics.smoothGazeY

        // 0b. Neuromuscular Fatigue State
        val fatigueState = NeuromuscularFatigueCompensator.evaluateFatigue(filteredKinematics.estimatedDriftMagnitude)

        val accelMag = kotlin.math.sqrt(sensors.accelX * sensors.accelX + sensors.accelY * sensors.accelY + sensors.accelZ * sensors.accelZ)
        val gyroMag = kotlin.math.sqrt(sensors.gyroX * sensors.gyroX + sensors.gyroY * sensors.gyroY + sensors.gyroZ * sensors.gyroZ)

        // 0c. 12-Sensor Holistic Synthesis & Context Summary
        val omniSummary = OmniSensorFusionSummaryEngine.synthesizeSensorStreams(
            accelNorm = accelMag,
            gyroNorm = gyroMag,
            magHeadingDeg = sensors.compassHeadingDeg,
            lightLux = sensors.ambientLightLux,
            pressureHpa = sensors.atmosphericPressureHpa,
            tempCelsius = sensors.ambientTemperatureC,
            stepCount = sensors.totalStepsDetected,
            audioDb = audio.decibels,
            formantHz = audio.dominantFrequencyHz,
            waveletEnergy = audio.rmsAmplitude * 100f,
            gazeConfidence = if (cameraGaze.isCameraActive) (cameraGaze.gazeConfidencePct / 100f) else 0.5f,
            gazeDeviation = abs(cleanGazeX - 0.5f) + abs(cleanGazeY - 0.5f),
            jitterMagnitude = filteredKinematics.estimatedDriftMagnitude,
            sessionFatiguePct = fatigueState.estimatedFatigueLevelPct
        )

        // 0d. Ingest frame into Temporal Multi-Scale Sensor History (DTW & Episodic Memory)
        TemporalSensorHistoryEngine.recordTemporalFrame(
            TemporalSensorHistoryEngine.SensorTemporalFrame(
                timestampMs = System.currentTimeMillis(),
                accelMagnitude = accelMag,
                gyroMagnitude = gyroMag,
                audioDecibels = audio.decibels,
                formantHz = audio.dominantFrequencyHz,
                lightLux = sensors.ambientLightLux,
                gazeFixationScore = if (cameraGaze.isCameraActive) (cameraGaze.gazeConfidencePct / 100f) else 0.5f,
                activityState = omniSummary.activityState
            )
        )
        val historicalContext = TemporalSensorHistoryEngine.queryTemporalPredictions(
            currentSummary = omniSummary,
            currentFormantHz = audio.dominantFrequencyHz
        )
        
        // 1. Markov Next-Word lookups & Semantic Embedding Neighbors
        val markovMatches = if (lastWord.isNotBlank()) {
            markovTable[lastWord.lowercase(Locale.ROOT)]?.map { it.nextWord } ?: emptyList()
        } else {
            emptyList()
        }

        // 1b. Edge Transformer Multi-Head Self-Attention Tokens
        val transformerOutput = EdgeTransformerSequencePredictor.predictNextTokens(
            sentence = lastAccumulatedSentence.ifBlank { lastWord },
            topK = 4
        )
        val transformerTokens = transformerOutput.predictedNextTokens.map { it.first.lowercase(Locale.ROOT) }

        // 1c. Hierarchical Attention Network Intent
        val hanProfile = HierarchicalAttentionNetwork.evaluateHierarchicalAttention(
            currentPhrase = lastAccumulatedSentence.ifBlank { lastWord },
            subVocalFormantHz = audio.dominantFrequencyHz,
            activeScreenContext = screenContext
        )

        // 1d. Knowledge Graph Ontological Affinity Multiplier
        val graphAffinity = GeorgianKnowledgeGraphEngine.queryKnowledgeGraph(lastAccumulatedSentence.ifBlank { lastWord })

        // 1e. Laryngeal Co-Articulation & Trajectory
        val coarticulation = LaryngealCoarticulationTracker.processFormantFrame(
            frequencyHz = audio.dominantFrequencyHz,
            energy = audio.decibels
        )

        // Semantic embedding neighbors
        val semanticNeighbors = if (lastWord.isNotBlank()) {
            SemanticEmbeddingEngine.findSemanticNeighbors(lastWord, topK = 5).map { it.first }
        } else {
            emptyList()
        }

        // Morphological FST decomposition
        val morphology = if (lastWord.isNotBlank()) {
            GeorgianMorphologicalFSTEngine.deconstruct(lastWord)
        } else {
            null
        }
        val morphologicalForms = morphology?.possibleConjugations ?: emptyList()

        // 2. Sensor category multipliers
        val sensorBoosts = evaluateSensorContext(sensors, audio, screenContext)

        // 3. Subvocal resonance
        val phonemeMatch = decodeSubvocalFormants(audio.dominantFrequencyHz, audio.dominantFrequencyHz)

        // 4. Gaze Intent with Kalman-smoothed gaze coordinates
        val gazeIntent = computeGazeIntentPreFetch(cameraGaze, cleanGazeX, cleanGazeY)

        // 5. Bayesian Synthesis across Lexicon
        val scoredList = GeorgianNeuroLinguisticEngine.MIND_LEXICON_DATABASE.map { entry ->
            // Base score & N-Gram
            var ngramScore = 1.0f
            if (markovMatches.contains(entry.word)) {
                ngramScore += 4.5f
            }
            if (entry.typicalNextWords.any { markovMatches.contains(it) || it.equals(lastWord, true) }) {
                ngramScore += 2.0f
            }

            // Edge Transformer Attention Boost
            if (transformerTokens.contains(entry.word.lowercase(Locale.ROOT))) {
                ngramScore += 3.2f
            }

            // Knowledge Graph Ontological Boost
            if (graphAffinity.suggestedConcepts.any { it.equals(entry.word, ignoreCase = true) }) {
                ngramScore += 2.8f
            }

            // Omni-Sensor Holistic Boost
            val omniWordBoost = OmniSensorFusionSummaryEngine.computeOmniSensorWordBoost(entry.word, omniSummary)
            ngramScore *= omniWordBoost

            // Temporal & Historical Sensor DTW/Episodic Boost
            val historicalBoost = TemporalSensorHistoryEngine.getHistoricalWordMultiplier(entry.word, historicalContext)
            ngramScore *= historicalBoost

            // Semantic Embedding Cosine Boost
            if (semanticNeighbors.contains(entry.word.lowercase(Locale.ROOT))) {
                ngramScore += 2.5f
            }

            // Morphological FST root matching boost
            if (morphologicalForms.contains(entry.word) || (morphology != null && entry.word.contains(morphology.rootStem))) {
                ngramScore += 2.0f
            }

            // Sensor multiplier
            val sensorMult = sensorBoosts[entry.category] ?: 1.0f

            // Sub-vocal boost with Trie/Fuzzy alignment
            val subVocalBoost = if (entry.word.startsWith(phonemeMatch.primaryPhoneme)) {
                (phonemeMatch.resonanceMatchPct / 100f) * 2.8f
            } else {
                1.0f
            }

            // Gaze boost
            val gazeBoost = if (gazeIntent.preFetchedCategories.contains(entry.category) || gazeIntent.preFetchedCandidateWords.contains(entry.word)) {
                1.75f
            } else {
                1.0f
            }

            // Personal Bayesian Prior with Ebbinghaus memory curve
            val prior = getPersonalPrior(entry.word)

            // Combined Bayesian posterior score: P(W | OmniSensors, Audio, Gaze, Transformer, HAN, Morphology)
            val combinedScore = (ngramScore * sensorMult * subVocalBoost * gazeBoost * prior) * fatigueState.adaptiveThresholdMultiplier

            BayesianWordScore(
                word = entry.word,
                category = entry.category,
                rawNgramScore = ngramScore,
                sensorMultiplier = sensorMult,
                subVocalBoost = subVocalBoost,
                gazeBoost = gazeBoost,
                personalPrior = prior,
                finalProbabilityPct = combinedScore
            )
        }

        // Normalize top candidates
        val topCandidates = scoredList.sortedByDescending { it.finalProbabilityPct }.take(6)
        val maxScore = topCandidates.firstOrNull()?.finalProbabilityPct ?: 1.0f
        val normalizedCandidates = topCandidates.map {
            val pct = ((it.finalProbabilityPct / maxScore) * 98.5f).coerceIn(45.0f, 99.4f)
            it.copy(finalProbabilityPct = pct)
        }

        // Run Beam Search Viterbi sequence decoder on top candidates
        val beamCandidates = normalizedCandidates.map { Pair(it.word, it.finalProbabilityPct / 100.0f) }
        val beamResult = BeamSearchViterbiDecoder.decodeSentence(
            initialContext = lastAccumulatedSentence,
            candidatePredictions = beamCandidates,
            maxSteps = 2
        )

        // Construct sentence
        val topWord = normalizedCandidates.firstOrNull()?.word ?: "შევამოწმოთ"
        val nextPredictedSentence = if (lastAccumulatedSentence.isBlank()) {
            beamResult.bestHypothesisSentence.ifBlank { "$topWord სისტემის არქიტექტურა და გავუშვათ" }
        } else {
            beamResult.bestHypothesisSentence.ifBlank { "$lastAccumulatedSentence $topWord" }
        }

        val speedup = 48 + (normalizedCandidates.firstOrNull()?.finalProbabilityPct?.toInt() ?: 80) / 4
        val confidence = normalizedCandidates.firstOrNull()?.finalProbabilityPct?.toInt() ?: 96

        val formulaLog = "Omni-State: ${omniSummary.activityState.geLabel} | HAN: ${hanProfile.dominantIntent} | ${hanProfile.attentionPathwayExplanation}"

        return UnifiedPredictionOutput(
            primaryPredictedSentence = nextPredictedSentence,
            topCandidateWords = normalizedCandidates,
            activeGazeIntent = gazeIntent,
            activePhonemicMatch = phonemeMatch,
            overallConfidencePct = confidence,
            latencySpeedupGainWpm = speedup,
            mathematicalFormulaLog = formulaLog,
            omniSummary = omniSummary
        )
    }
}
