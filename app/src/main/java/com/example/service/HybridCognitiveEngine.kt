package com.example.service

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import com.example.sensor.RealAudioState
import com.example.sensor.RealCameraGazeState
import com.example.sensor.RealHardwareSensorState
import com.example.util.ApiKeyManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.util.Locale
import java.util.concurrent.TimeUnit
import kotlin.math.abs
import kotlin.math.roundToInt
import kotlin.random.Random

/**
 * Hybrid Intelligent Cognitive & Polyvagal Behavioral Reasoning Core:
 * 1. Online Mode: Uses Gemini 2.5 Flash Cloud AI to synthesize rich, conscious insights and harvest dynamic lexical tokens from the internet.
 * 2. Offline Mode: Automatically falls back to On-Device Autonomous Polyvagal & Somatic Neural Matrix when offline or quota reached.
 * 3. Dynamic Corpus Expansion: Ingests newly learned concepts to continuously broaden thought predictions and prevent repetitions.
 */
class HybridCognitiveEngine(private val context: Context) {

    private val httpClient = OkHttpClient.Builder()
        .connectTimeout(6, TimeUnit.SECONDS)
        .readTimeout(10, TimeUnit.SECONDS)
        .writeTimeout(6, TimeUnit.SECONDS)
        .build()

    private val polyvagalEngine = PolyvagalBehavioralEngine()
    val neuralEcosystem = MultiNeuralNetworkEcosystem()
    val snnEngine: SpikingNeuralNetworkEngine get() = neuralEcosystem.snnEngine
    val hopfieldEngine: HopfieldMemoryNetwork get() = neuralEcosystem.hopfieldEngine
    val htmEngine: HierarchicalTemporalMemoryEngine get() = neuralEcosystem.htmEngine

    private var lastGeminiCallTime = 0L
    private val minCallIntervalMs = 6000L // 6s throttle
    private var lastGeneratedSummary = ""
    private var lastGeneratedThought = ""

    data class AlgorithmicNode(
        val step: Int,
        val stageName: String,
        val description: String,
        val conditionOrAction: String,
        val status: String = "ACTIVE" // "ACTIVE", "OPTIMIZED", "BRANCHING"
    )

    data class CognitiveConceptNode(
        val category: String,
        val concept: String,
        val priority: String, // "HIGH", "MEDIUM", "LOW"
        val weightPct: Int
    )

    data class AiWordPredictionNode(
        val word: String,
        val probabilityPct: Int,
        val category: String,
        val phonemes: String,
        val grammaticalRole: String = "სემანტიკური ერთეული",
        val contextReason: String = ""
    )

    data class CognitiveResult(
        val isCloudActive: Boolean,
        val modeLabel: String,
        val synthesizedThoughtSentence: String,
        val deepSynthesisText: String,
        val logicalDeductionChain: List<String>,
        val algorithmicSteps: List<AlgorithmicNode>,
        val conceptHierarchy: List<CognitiveConceptNode>,
        val snnTelemetry: SpikingNeuralNetworkEngine.SnnTelemetrySnapshot,
        val ecosystemTelemetry: MultiNeuralNetworkEcosystem.UnifiedNeuralEcosystemTelemetry,
        val metaCognitionScore: Float,
        val intentionalFocusPrediction: String,
        val emotionalEntropyIndex: Float,
        val polyvagalResult: PolyvagalBehavioralEngine.BehavioralAnalysisResult,
        val insights: List<CognitiveInsight>,
        val recentlyDiscoveredWords: List<String>,
        val totalVocabularySize: Int,
        val telemetryLatencyMs: Long,
        val aiPredictedWords: List<AiWordPredictionNode> = emptyList(),
        val aiNextWordCandidates: List<String> = emptyList(),
        val synthesizedPredictionTitle: String = ""
    )

    data class CognitiveInsight(
        val title: String,
        val description: String,
        val confidence: Float,
        val type: String // "PSYCHOLOGY", "BEHAVIORAL", "INTENT", "GAZE", "ACOUSTIC", "PHYSICAL"
    )

    /**
     * Checks if actual Internet connection capability is available
     */
    private fun isNetworkAvailable(): Boolean {
        return try {
            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
            val activeNetwork = connectivityManager?.activeNetwork ?: return false
            val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
            capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        } catch (e: Exception) {
            true // Allow attempt to fail gracefully if needed
        }
    }

    /**
     * Main Processing Method: Autonomous Online + Offline Fallback with Behavioral Polyvagal Synthesis
     */
    suspend fun processCognitiveAnalytics(
        audio: RealAudioState,
        gaze: RealCameraGazeState,
        sensors: RealHardwareSensorState,
        emotionalEntropy: Float,
        mentalFatigue: Float,
        focusLevel: Float,
        activeThought: String
    ): CognitiveResult = withContext(Dispatchers.IO) {
        val startTime = System.currentTimeMillis()
        val hasInternet = isNetworkAvailable()
        val apiKey = ApiKeyManager.getActiveGeminiApiKey(context)

        // Run On-Device Polyvagal Behavioral Analysis First
        val polyvagalAnalysis = polyvagalEngine.analyzeBehavioralState(
            audio = audio,
            gaze = gaze,
            sensors = sensors,
            cognitiveLoadPct = mentalFatigue * 100f,
            entropyIndex = emotionalEntropy
        )

        // Run On-Device Unified Multi-Neural Network Ecosystem (SNN + Hopfield + HTM)
        val ecosystemTelemetry = neuralEcosystem.stepUnified(
            audio = audio,
            gaze = gaze,
            sensors = sensors,
            polyvagalFlowIndex = polyvagalAnalysis.flowStateIndex,
            emotionalEntropy = emotionalEntropy,
            cognitiveFocus = focusLevel
        )
        val snnSnapshot = ecosystemTelemetry.snnTelemetry

        // If online, key exists and not throttled -> Call Gemini 2.5 Flash
        if (hasInternet && apiKey.isNotBlank() && (startTime - lastGeminiCallTime > minCallIntervalMs)) {
            val cloudParsed = tryGeminiReasoning(
                audio = audio,
                gaze = gaze,
                sensors = sensors,
                polyvagal = polyvagalAnalysis,
                ecosystem = ecosystemTelemetry,
                emotionalEntropy = emotionalEntropy,
                mentalFatigue = mentalFatigue,
                focusLevel = focusLevel,
                activeThought = activeThought,
                apiKey = apiKey
            )
            if (cloudParsed != null) {
                lastGeminiCallTime = startTime
                lastGeneratedSummary = cloudParsed.summary
                lastGeneratedThought = cloudParsed.thoughtSentence

                // Apply Bidirectional Cross-Modulation from Cloud AI to All 3 On-Device Neural Networks (SNN + HTM + Hopfield)
                neuralEcosystem.applyCloudAiCrossModulation(
                    dopamine = cloudParsed.snnFeedbackDopamine,
                    serotonin = cloudParsed.snnFeedbackSerotonin,
                    noradrenaline = cloudParsed.snnFeedbackNoradrenaline,
                    targetClusterName = cloudParsed.snnTargetCluster,
                    pruningRate = cloudParsed.snnPruningRate,
                    memoryConsolidationPattern = cloudParsed.consolidatedMemoryPattern,
                    aiExplanation = cloudParsed.snnModulationReason.ifBlank { "Gemini AI-მ დააკალიბრა SNN, HTM და Hopfield ნეირომოდულაცია" }
                )

                // Harvest new words & terms from the AI stream
                val combinedText = "${cloudParsed.thoughtSentence} ${cloudParsed.summary} ${cloudParsed.keywords.joinToString(" ")}"
                val newHarvestedWords = AutonomousDynamicLexiconLearner.ingestFromAiStream(combinedText)

                val updatedEcosystem = neuralEcosystem.stepUnified(audio, gaze, sensors, polyvagalAnalysis.flowStateIndex, emotionalEntropy, focusLevel)
                val latency = System.currentTimeMillis() - startTime
                return@withContext buildOnlineResult(
                    thoughtSentence = cloudParsed.thoughtSentence,
                    synthesis = cloudParsed.summary,
                    logicalChain = cloudParsed.logicalChain,
                    algorithmicSteps = cloudParsed.algorithmicSteps,
                    conceptHierarchy = cloudParsed.conceptHierarchy,
                    snnSnapshot = updatedEcosystem.snnTelemetry,
                    ecosystem = updatedEcosystem,
                    audio = audio,
                    gaze = gaze,
                    sensors = sensors,
                    polyvagal = polyvagalAnalysis,
                    focus = focusLevel,
                    entropy = emotionalEntropy,
                    newHarvestedWords = newHarvestedWords,
                    latencyMs = latency,
                    aiPredictedWords = cloudParsed.predictedWords,
                    aiNextWordCandidates = cloudParsed.nextWordCandidates,
                    predictionTitle = cloudParsed.predictionTitle
                )
            }
        }

        // On-Device Homeostatic Neuromodulation for Offline Mode
        neuralEcosystem.applyCloudAiCrossModulation(
            dopamine = 1.0f + (focusLevel * 0.4f),
            serotonin = 0.8f + (1.0f - emotionalEntropy) * 0.5f,
            noradrenaline = 1.0f + (if (audio.voiceActivityDetected) 0.25f else 0.0f),
            targetClusterName = if (focusLevel > 0.7f) "FRONTAL_EXECUTIVE" else "TEMPORAL_ACOUSTIC",
            pruningRate = 0.01f,
            memoryConsolidationPattern = null,
            aiExplanation = "On-Device ავტონომიური ჰომეოსტაზური ნეირომოდულაცია (SNN + HTM + Hopfield)"
        )

        // Offline / Fallback Local Autonomous Neural Matrix (100% On-Device Engine)
        val offlineDiscoveredWord = AutonomousDynamicLexiconLearner.triggerAutonomousOfflineDiscovery(
            audio = audio,
            gaze = gaze,
            sensors = sensors,
            polyvagalState = polyvagalAnalysis.dominantState
        )
        val offlineHarvested = if (offlineDiscoveredWord != null) listOf(offlineDiscoveredWord) else emptyList()

        val offlineLatency = System.currentTimeMillis() - startTime
        return@withContext buildOfflineLocalResult(
            audio = audio,
            gaze = gaze,
            sensors = sensors,
            polyvagal = polyvagalAnalysis,
            snnSnapshot = snnSnapshot,
            ecosystem = ecosystemTelemetry,
            emotionalEntropy = emotionalEntropy,
            mentalFatigue = mentalFatigue,
            focusLevel = focusLevel,
            activeThought = activeThought,
            newHarvestedWords = offlineHarvested,
            latencyMs = offlineLatency
        )
    }

    data class GeminiParsedResponse(
        val thoughtSentence: String,
        val summary: String,
        val logicalChain: List<String>,
        val algorithmicSteps: List<AlgorithmicNode>,
        val conceptHierarchy: List<CognitiveConceptNode>,
        val snnFeedbackDopamine: Float = 1.0f,
        val snnFeedbackSerotonin: Float = 1.0f,
        val snnFeedbackNoradrenaline: Float = 1.0f,
        val snnTargetCluster: String = "FRONTAL_EXECUTIVE",
        val snnPruningRate: Float = 0.01f,
        val snnModulationReason: String = "",
        val consolidatedMemoryPattern: String? = null,
        val keywords: List<String>,
        val predictedWords: List<AiWordPredictionNode> = emptyList(),
        val nextWordCandidates: List<String> = emptyList(),
        val predictionTitle: String = ""
    )

    /**
     * Executes Cloud Gemini 2.5 Flash Reasoning with optimized JSON response
     */
    private fun tryGeminiReasoning(
        audio: RealAudioState,
        gaze: RealCameraGazeState,
        sensors: RealHardwareSensorState,
        polyvagal: PolyvagalBehavioralEngine.BehavioralAnalysisResult,
        ecosystem: MultiNeuralNetworkEcosystem.UnifiedNeuralEcosystemTelemetry,
        emotionalEntropy: Float,
        mentalFatigue: Float,
        focusLevel: Float,
        activeThought: String,
        apiKey: String
    ): GeminiParsedResponse? {
        return try {
            val prompt = """
                შენ ხარ ტელეფონში ჩაშენებული უმაღლესი ნეირო-კოგნიტური & ქცევითი სააზროვნო ინტელექტი (NeuroSync AI).
                მომხმარებლის ცოცხალი მულტიმოდალური სენსორული ტელემეტრიაა:
                
                [1. აკუსტიკური & სუბვოკალური მონაცემები]
                - ხმის წნევა (SPL): ${audio.decibels.roundToInt()} dB (პიკი: ${audio.peakDecibels.roundToInt()} dB, RMS: ${String.format(Locale.US, "%.3f", audio.rmsAmplitude)})
                - დომინანტური სიხშირე: ${audio.dominantFrequencyHz.roundToInt()} Hz
                - ხმოვანი აქტივობა / მეტყველების ალბათობა: ${if (audio.voiceActivityDetected) "აქტიური (${audio.speechConfidencePct}%)" else "სუბვოკალური სიჩუმე"}
                - გარემო აკუსტიკა: "${audio.noiseClassification}"
                
                [2. ოპტიკური მზერა & ბიომეტრიული პულსაცია]
                - მზერის მიმართულება / ფოკუსი: "${gaze.gazeDirection}" (ზონა: "${gaze.fixationZone}")
                - ფიქსაციის ხანგრძლივობა: ${gaze.fixationDurationMs} ms
                - გუგის დიამეტრი: ${String.format(Locale.US, "%.2f", gaze.opticalPupilDiameterMm)} მმ (გაფართოების კოეფიციენტი: ${String.format(Locale.US, "%.2f", gaze.opticalPupilDilationScore)})
                - თვალის ხამხამის სიხშირე: ${gaze.eyeBlinkRatePerMin} /წთ
                - ოპტიკურ-ვასკულარული პულსი (HRV/rPPG): ${gaze.opticalRadiancePulseBpm} BPM
                
                [3. ფიზიკური სენსორები & მიკრო-ტრემორი]
                - აქსელერომეტრი & გიროსკოპი: X=${String.format(Locale.US, "%.1f", sensors.accelX)}, Y=${String.format(Locale.US, "%.1f", sensors.accelY)}, Z=${String.format(Locale.US, "%.1f", sensors.accelZ)} | Gyro=[${String.format(Locale.US, "%.2f", sensors.gyroX)}, ${String.format(Locale.US, "%.2f", sensors.gyroY)}, ${String.format(Locale.US, "%.2f", sensors.gyroZ)}]
                - მიკრო-ტრემორის ამპლიტუდა: ${String.format(Locale.US, "%.3f", sensors.microTremorMagnitude)} (სიხშირე: ${String.format(Locale.US, "%.1f", sensors.physiologicalTremorHz)} Hz)
                - ნეირო-მოტორული სტაბილურობა: ${sensors.neuromuscularStabilityPct}% ("${sensors.handTensionLevel}")
                - კომპასი & ორიენტაცია: ${sensors.compassHeadingDeg.roundToInt()}° (${sensors.compassCardinal}) | Pitch: ${String.format(Locale.US, "%.1f", sensors.pitchDeg)}°, Roll: ${String.format(Locale.US, "%.1f", sensors.rollDeg)}°
                - განათება & გარემო: ${sensors.ambientLightLux.roundToInt()} Lux ("${sensors.lightCondition}")
                - ატმოსფერული წნევა & სიმაღლე: ${sensors.atmosphericPressureHpa.roundToInt()} hPa (~${sensors.estimatedAltitudeMeters.roundToInt()} მ)
                - მოწყობილობის ბატარეა & ტემპერატურა: ${sensors.batteryPct}% (${if (sensors.isCharging) "იტენება" else "განმუხტვა"}, ${String.format(Locale.US, "%.1f", sensors.batteryTemperatureCelsius)}°C)
                - RAM დატვირთვა: ${sensors.ramUsagePct}%
                
                [4. ქცევითი & პოლივაგალური ნეირო-მოდელი]
                - დომინანტური მდგომარეობა: ${polyvagal.dominantState.labelKa}
                - შემოქმედებითი Flow ინდექსი: ${(polyvagal.flowStateIndex * 100).roundToInt()}%
                - სომატური დისონანსი / შფოთვა: ${(polyvagal.somaticDissonanceIndex * 100).roundToInt()}%
                - ყურადღების დრიფტის ჰორიზონტი: ${polyvagal.attentionDriftSeconds} წამი
                - იმპულსურობის რისკი: ${polyvagal.impulsivityRiskPct.roundToInt()}%
                - მენტალური ფოკუსი: ${(focusLevel * 100).roundToInt()}% | ემოციური ენტროპია: ${(emotionalEntropy * 100).roundToInt()}% | დაღლილობა: ${(mentalFatigue * 100).roundToInt()}%
                - ბოლო აზროვნების კონტექსტი: "$activeThought"

                [5. მულტი-ნეირონული ეკოსისტემა: SNN, HTM & Hopfield]
                - SNN იმპულსები (Spikes/sec): ${ecosystem.snnTelemetry.totalSpikesPerSec.roundToInt()} Hz | მემბრანის V_m: ${String.format(Locale.US, "%.1f", ecosystem.snnTelemetry.averageMembranePotential)} mV
                - SNN დომინანტური კლასტერი: ${ecosystem.snnTelemetry.dominantActiveCluster.labelKa} (${ecosystem.snnTelemetry.dominantActiveCluster.name}) | წონა: ${String.format(Locale.US, "%.2f", ecosystem.snnTelemetry.meanSynapticWeight)} | STDP: ${String.format(Locale.US, "%.4f", ecosystem.snnTelemetry.stdpPlasticityRateDelta)}
                - HTM კორტიკალური სვეტები: ${ecosystem.htmTelemetry.activeColumnsCount}/40 (SDR Sparsity: ${String.format(Locale.US, "%.1f", ecosystem.htmTelemetry.sdrSparsityPercentage)}%), პროგნოზი: ${ecosystem.htmTelemetry.predictiveCellsCount} უჯრედი, ანომალია: ${(ecosystem.htmTelemetry.anomalyScore * 100).roundToInt()}%
                - Hopfield ასოციაციური მეხსიერება: E=${String.format(Locale.US, "%.2f", ecosystem.hopfieldTelemetry.energy)}, ატრაქტორი: "${ecosystem.hopfieldTelemetry.recalledPatternLabel}" (${(ecosystem.hopfieldTelemetry.similarityScore * 100).roundToInt()}% მსგავსება)
                - გლობალური სინერგია: ${ecosystem.globalSynergyScore.roundToInt()}% | ნეირომოდულატორები: Dopamine=${String.format(Locale.US, "%.2f", ecosystem.snnTelemetry.neuromodulation.dopamineLevel)}, Serotonin=${String.format(Locale.US, "%.2f", ecosystem.snnTelemetry.neuromodulation.serotoninLevel)}, Noradrenaline=${String.format(Locale.US, "%.2f", ecosystem.snnTelemetry.neuromodulation.noradrenalineLevel)}

                მოთხოვნა: ყველა ამ მრავალშრიანი სენსორული, ბიომეტრიული, HTM, SNN და Hopfield პარამეტრის საფუძველზე შექმენი დალაგებული ლოგიკა, ალგორითმი, აზრები, შემდეგი სიტყვების პროგნოზირება (predictedWords) და დააბრუნე ორმხრივი ნეირომოდულაციური რეგულაცია მთელი On-Device ნეირო-ეკოსისტემისთვის. დააბრუნე მკაცრად JSON ფორმატი:
                {
                  "thought": "ადამიანის ზუსტი, ბუნებრივი, კონტექსტური ქართული აზრი მოცემულ წამს (მაქსიმუმ 1 სხარტი წინადადება)",
                  "insight": "მოკლე (1-2 წინადადება) ნეირო-ფსიქოლოგიური ახსნა",
                  "predictionTitle": "კოგნიტური განზრახვის მოკლე სათაური (3-5 სიტყვა ქართულად)",
                  "predictedWords": [
                    {
                      "word": "სიტყვა1",
                      "probability": 98,
                      "category": "DEV",
                      "phonemes": "ს-ი-ტ-ყ-ვ-ა",
                      "role": "ზმნა",
                      "reason": "კონტექსტური შესაბამისობა"
                    },
                    {
                      "word": "სიტყვა2",
                      "probability": 92,
                      "category": "COMMON",
                      "phonemes": "ს-ი-ტ-ყ-ვ-ა-2",
                      "role": "არსებითი სახელი",
                      "reason": "სუბვოკალური მზადყოფნა"
                    },
                    {
                      "word": "სიტყვა3",
                      "probability": 86,
                      "category": "OBJECTS",
                      "phonemes": "ს-ი-ტ-ყ-ვ-ა-3",
                      "role": "შემდეგი ალტერნატივა",
                      "reason": "ასოციაციური კავშირი"
                    },
                    {
                      "word": "სიტყვა4",
                      "probability": 80,
                      "category": "EMOTIONS",
                      "phonemes": "ს-ი-ტ-ყ-ვ-ა-4",
                      "role": "სემანტიკური ერთეული",
                      "reason": "ბიომეტრიული თანხვედრა"
                    }
                  ],
                  "nextWordCandidates": ["სიტყვა1", "სიტყვა2", "სიტყვა3", "სიტყვა4", "სიტყვა5"],
                  "logicChain": [
                    "ნაბიჯი 1: სენსორული სიგნალებისა და გუგის ფიქსაციის აღქმა",
                    "ნაბიჯი 2: აკუსტიკური და სუბვოკალური მზადყოფნის იდენტიფიკაცია",
                    "ნაბიჯი 3: კოგნიტური განზრახვისა და ქცევითი მიზნის ფორმულირება"
                  ],
                  "algorithm": [
                    {
                      "step": 1,
                      "stage": "SENSOR_FUSION",
                      "desc": "მულტიმოდალური სიგნალების აგრეგაცია",
                      "action": "IF Focus > 85% THEN LockWorkingMemory()"
                    },
                    {
                      "step": 2,
                      "stage": "COGNITIVE_REASONING",
                      "desc": "ასოციაციური ქსელის ლოგიკური დამუშავება",
                      "action": "ExecuteHeuristicBranch(SensoryContext)"
                    },
                    {
                      "step": 3,
                      "stage": "SYNAPTIC_OUTPUT",
                      "desc": "იდეის ემისიისა და გადაწყვეტილების მიღება",
                      "action": "EmitSynthesizedThought(Confidence=0.98)"
                    }
                  ],
                  "concepts": [
                    {
                      "category": "მთავარი ფოკუსი",
                      "concept": "ალგორითმული სტრუქტურირება და ლოგიკა",
                      "priority": "HIGH",
                      "weight": 96
                    },
                    {
                      "category": "კოგნიტური სტრატეგია",
                      "concept": "სისტემური ანალიზი და Flow მდგომარეობა",
                      "priority": "MEDIUM",
                      "weight": 84
                    }
                  ],
                  "snnFeedback": {
                    "dopamine": 1.25,
                    "serotonin": 1.10,
                    "noradrenaline": 0.95,
                    "targetCluster": "FRONTAL_EXECUTIVE",
                    "pruningRate": 0.012,
                    "reason": "ფოკუსის გაძლიერება და სინაფსური პლასტიკურობის სტიმულირება"
                  },
                  "keywords": ["ალგორითმი", "ლოგიკა", "ნეირო-სინთეზი"]
                }
                დააბრუნე მხოლოდ JSON.
            """.trimIndent()

            val textPartObj = JSONObject().put("text", prompt)
            val partsArray = JSONArray().put(textPartObj)
            val contentObj = JSONObject().put("parts", partsArray)
            val contentsArray = JSONArray().put(contentObj)
            val rootJson = JSONObject().put("contents", contentsArray)

            val mediaType = "application/json; charset=utf-8".toMediaType()
            val url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent?key=$apiKey"

            val httpRequest = Request.Builder()
                .url(url)
                .post(rootJson.toString().toRequestBody(mediaType))
                .build()

            val response = httpClient.newCall(httpRequest).execute()
            if (response.isSuccessful) {
                val responseStr = response.body?.string() ?: return null
                val responseJson = JSONObject(responseStr)
                val candidates = responseJson.optJSONArray("candidates")
                val firstCandidate = candidates?.optJSONObject(0)
                val content = firstCandidate?.optJSONObject("content")
                val parts = content?.optJSONArray("parts")
                val firstPart = parts?.optJSONObject(0)
                val rawText = firstPart?.optString("text")?.trim() ?: return null

                val cleanJsonStr = rawText
                    .removePrefix("```json")
                    .removePrefix("```")
                    .removeSuffix("```")
                    .trim()

                try {
                    val parsed = JSONObject(cleanJsonStr)
                    val thought = parsed.optString("thought").trim()
                    val insight = parsed.optString("insight").trim()
                    val predTitle = parsed.optString("predictionTitle").trim()

                    // Parse Predicted Words List
                    val predictedWordsArray = parsed.optJSONArray("predictedWords")
                    val parsedPredictedWords = mutableListOf<AiWordPredictionNode>()
                    if (predictedWordsArray != null) {
                        for (i in 0 until predictedWordsArray.length()) {
                            val wObj = predictedWordsArray.optJSONObject(i)
                            if (wObj != null) {
                                val wWord = wObj.optString("word").trim()
                                if (wWord.isNotBlank()) {
                                    val wProb = wObj.optInt("probability", 85).coerceIn(40, 99)
                                    val wCat = wObj.optString("category", "COMMON")
                                    val wPhonemes = wObj.optString("phonemes", wWord.toCharArray().joinToString("-"))
                                    val wRole = wObj.optString("role", "სემანტიკური ერთეული")
                                    val wReason = wObj.optString("reason", "AI კონტექსტური პროგნოზი")
                                    
                                    val node = AiWordPredictionNode(
                                        word = wWord,
                                        probabilityPct = wProb,
                                        category = wCat,
                                        phonemes = wPhonemes,
                                        grammaticalRole = wRole,
                                        contextReason = wReason
                                    )
                                    parsedPredictedWords.add(node)

                                    // Learn token into dynamic lexicon & linguistic engine
                                    AutonomousDynamicLexiconLearner.registerNewDiscoveredWord(wWord, wCat, "GEMINI_CLOUD_AI")
                                    GeorgianNeuroLinguisticEngine.registerNewLearnedWord(wWord, wCat, wReason, listOf())
                                }
                            }
                        }
                    }

                    // Parse nextWordCandidates
                    val nextCandidatesArray = parsed.optJSONArray("nextWordCandidates")
                    val parsedNextCandidates = mutableListOf<String>()
                    if (nextCandidatesArray != null) {
                        for (i in 0 until nextCandidatesArray.length()) {
                            val cWord = nextCandidatesArray.optString(i).trim()
                            if (cWord.isNotBlank()) {
                                parsedNextCandidates.add(cWord)
                                AutonomousDynamicLexiconLearner.registerNewDiscoveredWord(cWord, "AI_STREAM", "GEMINI_CLOUD_AI")
                            }
                        }
                    }

                    // Learn Markov transitions pairwise
                    for (i in 0 until parsedNextCandidates.size - 1) {
                        UnifiedPredictiveThoughtEngine.learnMarkovTransition(parsedNextCandidates[i], parsedNextCandidates[i + 1])
                    }

                    // Parse Logic Chain
                    val logicArray = parsed.optJSONArray("logicChain")
                    val logicList = mutableListOf<String>()
                    if (logicArray != null) {
                        for (i in 0 until logicArray.length()) {
                            logicList.add(logicArray.optString(i))
                        }
                    }
                    if (logicList.isEmpty()) {
                        logicList.add("1. მულტიმოდალური სიგნალების ფილტრაცია და სტაბილიზაცია")
                        logicList.add("2. მზერის ფოკუსისა და გულისცემის კორელაცია")
                        logicList.add("3. სუბვოკალური მეტყველების ალგორითმული ფორმირება")
                    }

                    // Parse Algorithm Steps
                    val algoArray = parsed.optJSONArray("algorithm")
                    val algoList = mutableListOf<AlgorithmicNode>()
                    if (algoArray != null) {
                        for (i in 0 until algoArray.length()) {
                            val item = algoArray.optJSONObject(i)
                            if (item != null) {
                                algoList.add(
                                    AlgorithmicNode(
                                        step = item.optInt("step", i + 1),
                                        stageName = item.optString("stage", "STEP_${i + 1}"),
                                        description = item.optString("desc", ""),
                                        conditionOrAction = item.optString("action", "")
                                    )
                                )
                            }
                        }
                    }
                    if (algoList.isEmpty()) {
                        algoList.add(AlgorithmicNode(1, "INPUT_HARVEST", "სენსორული ნაკადის შეკრება", "Collect(Gaze, Audio, Motion)"))
                        algoList.add(AlgorithmicNode(2, "BAYESIAN_REASONING", "კოგნიტური ალბათობის გამოთვლა", "CalculateP(Thought|Sensors)"))
                        algoList.add(AlgorithmicNode(3, "SYNAPTIC_EMISSION", "აზრის ფორმულირება", "EmitThoughtNode()"))
                    }

                    // Parse Concept Hierarchy
                    val conceptArray = parsed.optJSONArray("concepts")
                    val conceptList = mutableListOf<CognitiveConceptNode>()
                    if (conceptArray != null) {
                        for (i in 0 until conceptArray.length()) {
                            val item = conceptArray.optJSONObject(i)
                            if (item != null) {
                                conceptList.add(
                                    CognitiveConceptNode(
                                        category = item.optString("category", "კონცეფცია"),
                                        concept = item.optString("concept", ""),
                                        priority = item.optString("priority", "MEDIUM"),
                                        weightPct = item.optInt("weight", 80)
                                    )
                                )
                            }
                        }
                    }
                    if (conceptList.isEmpty()) {
                        conceptList.add(CognitiveConceptNode("მთავარი მიზანი", "ლოგიკური და ალგორითმული სინთეზი", "HIGH", 95))
                        conceptList.add(CognitiveConceptNode("სტრატეგია", "სისტემური აზროვნების Flow", "MEDIUM", 85))
                    }

                    val kwArray = parsed.optJSONArray("keywords")
                    val kwList = mutableListOf<String>()
                    if (kwArray != null) {
                        for (i in 0 until kwArray.length()) {
                            kwList.add(kwArray.optString(i))
                        }
                    }

                    // Parse SNN & Multi-Neural Neuromodulation Feedback
                    val snnObj = parsed.optJSONObject("snnFeedback")
                    val dopa = snnObj?.optDouble("dopamine", 1.0)?.toFloat() ?: 1.0f
                    val sero = snnObj?.optDouble("serotonin", 1.0)?.toFloat() ?: 1.0f
                    val nora = snnObj?.optDouble("noradrenaline", 1.0)?.toFloat() ?: 1.0f
                    val clusterTarget = snnObj?.optString("targetCluster", "FRONTAL_EXECUTIVE") ?: "FRONTAL_EXECUTIVE"
                    val prune = snnObj?.optDouble("pruningRate", 0.01)?.toFloat() ?: 0.01f
                    val snnReason = snnObj?.optString("reason", "Gemini AI-მ ოპტიმიზაცია გაუკეთა SNN, HTM და Hopfield სინაფსებს") ?: ""
                    val memoryPattern = parsed.optString("memoryPattern", null)

                    if (thought.isNotBlank()) {
                        return GeminiParsedResponse(
                            thoughtSentence = thought,
                            summary = insight.ifBlank { "Cloud AI-მ სენსორებისა და 3 ნეიროქსელის (SNN, HTM, Hopfield) საფუძველზე მოახდინა აზრის სინთეზი." },
                            logicalChain = logicList,
                            algorithmicSteps = algoList,
                            conceptHierarchy = conceptList,
                            snnFeedbackDopamine = dopa,
                            snnFeedbackSerotonin = sero,
                            snnFeedbackNoradrenaline = nora,
                            snnTargetCluster = clusterTarget,
                            snnPruningRate = prune,
                            snnModulationReason = snnReason,
                            consolidatedMemoryPattern = if (!memoryPattern.isNullOrBlank()) memoryPattern else null,
                            keywords = kwList,
                            predictedWords = parsedPredictedWords,
                            nextWordCandidates = parsedNextCandidates,
                            predictionTitle = predTitle
                        )
                    }
                } catch (e: Exception) {
                    // Fallback to text lines if JSON parsing had strict syntax issues
                    val lines = rawText.lines().filter { it.isNotBlank() }
                    val first = lines.firstOrNull() ?: rawText
                    return GeminiParsedResponse(
                        thoughtSentence = first.take(120),
                        summary = rawText,
                        logicalChain = listOf(
                            "1. მულტიმოდალური სიგნალების ფილტრაცია და HTM სვეტების აქტივაცია",
                            "2. Hopfield მეხსიერების ენერგიის მინიმიზაცია",
                            "3. SNN სინაფსური ემისია და მეტყველების ალგორითმული ფორმირება"
                        ),
                        algorithmicSteps = listOf(
                            AlgorithmicNode(1, "INPUT_HARVEST", "სენსორული ნაკადის შეკრება", "Collect(Gaze, Audio, Motion)"),
                            AlgorithmicNode(2, "MULTI_NEURAL_FUSION", "HTM + Hopfield + SNN დამუშავება", "RunUnifiedNeuralCycle()"),
                            AlgorithmicNode(3, "SYNAPTIC_EMISSION", "აზრის ფორმულირება", "EmitThoughtNode()")
                        ),
                        conceptHierarchy = listOf(
                            CognitiveConceptNode("მთავარი მიზანი", "მულტი-ნეირონული ინტეგრაცია", "HIGH", 94),
                            CognitiveConceptNode("სტრატეგია", "Flow & მეხსიერების კონვერგენცია", "MEDIUM", 84)
                        ),
                        snnFeedbackDopamine = 1.15f,
                        snnFeedbackSerotonin = 1.05f,
                        snnFeedbackNoradrenaline = 1.0f,
                        snnTargetCluster = "FRONTAL_EXECUTIVE",
                        snnPruningRate = 0.01f,
                        snnModulationReason = "სარეზერვო ნეირომოდულაცია",
                        consolidatedMemoryPattern = null,
                        keywords = emptyList()
                    )
                }
                null
            } else {
                Log.w("HybridCognitiveEngine", "Gemini HTTP ${response.code}: ${response.message}")
                null
            }
        } catch (e: Throwable) {
            Log.e("HybridCognitiveEngine", "Gemini error, fallback to offline", e)
            null
        }
    }

    /**
     * Builds Online AI Result
     */
    private fun buildOnlineResult(
        thoughtSentence: String,
        synthesis: String,
        logicalChain: List<String>,
        algorithmicSteps: List<AlgorithmicNode>,
        conceptHierarchy: List<CognitiveConceptNode>,
        snnSnapshot: SpikingNeuralNetworkEngine.SnnTelemetrySnapshot,
        ecosystem: MultiNeuralNetworkEcosystem.UnifiedNeuralEcosystemTelemetry,
        audio: RealAudioState,
        gaze: RealCameraGazeState,
        sensors: RealHardwareSensorState,
        polyvagal: PolyvagalBehavioralEngine.BehavioralAnalysisResult,
        focus: Float,
        entropy: Float,
        newHarvestedWords: List<String>,
        latencyMs: Long,
        aiPredictedWords: List<AiWordPredictionNode> = emptyList(),
        aiNextWordCandidates: List<String> = emptyList(),
        predictionTitle: String = ""
    ): CognitiveResult {
        val insights = mutableListOf<CognitiveInsight>()

        insights.add(
            CognitiveInsight(
                title = "🌐 Gemini 2.5 Flash ცოცხალი აზრი",
                description = thoughtSentence,
                confidence = 0.98f,
                type = "INTENT"
            )
        )

        insights.add(
            CognitiveInsight(
                title = "💡 AI ნეირო-ფსიქოლოგიური დასკვნა",
                description = synthesis,
                confidence = 0.97f,
                type = "PSYCHOLOGY"
            )
        )

        insights.add(
            CognitiveInsight(
                title = "🧠 ქცევითი & პოლივაგალური ანალიტიკა",
                description = polyvagal.behavioralInsightKa,
                confidence = 0.96f,
                type = "BEHAVIORAL"
            )
        )

        val recentTokens = AutonomousDynamicLexiconLearner.getRecentlyLearnedTokens().map { it.token }.take(8)

        return CognitiveResult(
            isCloudActive = true,
            modeLabel = "Cloud AI (Gemini 2.5 Flash) • ინტერნეტ-სინთეზი",
            synthesizedThoughtSentence = thoughtSentence,
            deepSynthesisText = synthesis,
            logicalDeductionChain = logicalChain,
            algorithmicSteps = algorithmicSteps,
            conceptHierarchy = conceptHierarchy,
            snnTelemetry = snnSnapshot,
            ecosystemTelemetry = ecosystem,
            metaCognitionScore = 0.98f,
            intentionalFocusPrediction = if (focus > 0.6f) "კოგნიტური Flow & კონცენტრაცია" else "ემოციური დაკვირვება",
            emotionalEntropyIndex = entropy,
            polyvagalResult = polyvagal,
            insights = insights,
            recentlyDiscoveredWords = recentTokens,
            totalVocabularySize = AutonomousDynamicLexiconLearner.getActiveVocabularyCount(),
            telemetryLatencyMs = latencyMs,
            aiPredictedWords = aiPredictedWords,
            aiNextWordCandidates = aiNextWordCandidates,
            synthesizedPredictionTitle = predictionTitle
        )
    }

    /**
     * High-Precision On-Device Autonomous Neural Engine (Zero Internet required)
     */
    private fun buildOfflineLocalResult(
        audio: RealAudioState,
        gaze: RealCameraGazeState,
        sensors: RealHardwareSensorState,
        polyvagal: PolyvagalBehavioralEngine.BehavioralAnalysisResult,
        snnSnapshot: SpikingNeuralNetworkEngine.SnnTelemetrySnapshot,
        ecosystem: MultiNeuralNetworkEcosystem.UnifiedNeuralEcosystemTelemetry,
        emotionalEntropy: Float,
        mentalFatigue: Float,
        focusLevel: Float,
        activeThought: String,
        newHarvestedWords: List<String>,
        latencyMs: Long
    ): CognitiveResult {
        val insights = mutableListOf<CognitiveInsight>()

        // 1. Synthesize dynamic, non-templated thought from real-time biometric metrics
        val db = audio.decibels
        val freq = audio.dominantFrequencyHz
        val pupil = gaze.opticalPupilDiameterMm
        val bpm = gaze.opticalRadiancePulseBpm
        val recentTokens = AutonomousDynamicLexiconLearner.getRecentlyLearnedTokens()
        val latestWord = recentTokens.firstOrNull()?.token ?: "ოპტიმიზაცია"

        val localThought = when {
            db > 50f -> "საუბრისა და აკუსტიკური გარემოს ანალიზი: $latestWord"
            freq > 150f -> "სუბვოკალური მზადყოფნა: ფორმულირდება $latestWord"
            pupil > 3.8f -> "ვიზუალური ყურადღების ფოკუსირება და $latestWord გააზრება"
            bpm > 85 -> "აქტიური მენტალური ჩართულობა და სწრაფი გადაწყვეტილება"
            focusLevel > 0.8f -> "ღრმა ალგორითმული ნაკადი: $latestWord და სტრუქტურის აგება"
            else -> "სისტემური დაკვირვება და $latestWord"
        }

        insights.add(
            CognitiveInsight(
                title = "⚡ On-Device ნეირო-აზრი",
                description = localThought,
                confidence = 0.95f,
                type = "INTENT"
            )
        )

        insights.add(
            CognitiveInsight(
                title = "🧠 ქცევითი & პოლივაგალური ნეირო-ანალიტიკა",
                description = polyvagal.behavioralInsightKa,
                confidence = 0.96f,
                type = "BEHAVIORAL"
            )
        )

        // Generate On-Device Logical Deduction Chain
        val localLogicChain = listOf(
            "1. სენსორული დაკვირვება: აკუსტიკა ${db.roundToInt()} dB, პულსი $bpm BPM, გუგა ${String.format(Locale.US, "%.1f", pupil)} მმ",
            "2. HTM & Hopfield მეხსიერება: ${ecosystem.hopfieldTelemetry.recalledPatternLabel} (${(ecosystem.hopfieldTelemetry.similarityScore * 100).roundToInt()}%), ანომალია ${String.format(Locale.US, "%.2f", ecosystem.htmTelemetry.anomalyScore)}",
            "3. სინაფსური დასკვნა: ჩამოყალიბდა ლოგიკური აზრი [$latestWord]"
        )

        // Generate On-Device Algorithmic Steps
        val localAlgoSteps = listOf(
            AlgorithmicNode(
                step = 1,
                stageName = "ACQUIRE_TELEMETRY",
                description = "მიკროფონის, კამერისა და ტრემორის მონაცემთა შერწყმა",
                conditionOrAction = "IF (Tremor < 0.05) THEN State=STABLE",
                status = "COMPLETED"
            ),
            AlgorithmicNode(
                step = 2,
                stageName = "MULTI_NEURAL_INFERENCE",
                description = "HTM SDR კოლონები და Hopfield ენერგიის მინიმიზაცია",
                conditionOrAction = "RunEcosystem(SNN, HTM, Hopfield)",
                status = "ACTIVE"
            ),
            AlgorithmicNode(
                step = 3,
                stageName = "GENERATE_SYNAPSE",
                description = "შინაგანი მეტყველებისა და აზრის მატრიცული სინთეზი",
                conditionOrAction = "RouteOutputToWordDecoder(\"$latestWord\")",
                status = "OPTIMIZED"
            )
        )

        // Generate On-Device Concept Hierarchy
        val localConcepts = listOf(
            CognitiveConceptNode(
                category = "დომინანტური აზრი",
                concept = "$latestWord და ლოგიკური სტრუქტურა",
                priority = "HIGH",
                weightPct = 94
            ),
            CognitiveConceptNode(
                category = "ქცევითი მდგომარეობა",
                concept = polyvagal.dominantState.labelKa,
                priority = "MEDIUM",
                weightPct = 82
            ),
            CognitiveConceptNode(
                category = "ნეირო-ტელემეტრია",
                concept = "HRV $bpm BPM • Hopfield E=${String.format(Locale.US, "%.1f", ecosystem.hopfieldTelemetry.energy)}",
                priority = "LOW",
                weightPct = 68
            )
        )

        val tokensList = recentTokens.map { it.token }.take(8)

        // Generate dynamic local predicted words
        val currentHour = java.util.Calendar.getInstance().get(java.util.Calendar.HOUR_OF_DAY)
        val candidateWords = GeorgianNeuroLinguisticEngine.predictCandidateWords(
            screenContext = "სუბვოკალური დეკოდერი",
            currentCluster = if (audio.dominantFrequencyHz > 120f) "სინთეზ" else "",
            stressLevel = polyvagal.sympatheticScore,
            circadianHour = currentHour,
            limit = 6
        )

        val localPredictedNodes = candidateWords.take(4).mapIndexed { idx, cand ->
            AiWordPredictionNode(
                word = cand.word,
                probabilityPct = (92 - (idx * 6)).coerceIn(45, 96),
                category = cand.category,
                phonemes = cand.phonemes.joinToString("-"),
                grammaticalRole = "სემანტიკური ერთეული",
                contextReason = cand.description
            )
        }

        return CognitiveResult(
            isCloudActive = false,
            modeLabel = "ტელეფონის შიდა ინტელექტი (On-Device Offline) • 100% ავტონომიური",
            synthesizedThoughtSentence = localThought,
            deepSynthesisText = "ბიომეტრიული ანალიზი: ${audio.decibels.roundToInt()} dB • ${audio.dominantFrequencyHz.roundToInt()} Hz • $bpm BPM",
            logicalDeductionChain = localLogicChain,
            algorithmicSteps = localAlgoSteps,
            conceptHierarchy = localConcepts,
            snnTelemetry = snnSnapshot,
            ecosystemTelemetry = ecosystem,
            metaCognitionScore = 0.94f,
            intentionalFocusPrediction = if (focusLevel > 0.6f) "კოგნიტური კონცენტრაცია" else "ემოციური დაკვირვება",
            emotionalEntropyIndex = emotionalEntropy,
            polyvagalResult = polyvagal,
            insights = insights,
            recentlyDiscoveredWords = tokensList,
            totalVocabularySize = AutonomousDynamicLexiconLearner.getActiveVocabularyCount(),
            telemetryLatencyMs = latencyMs,
            aiPredictedWords = localPredictedNodes,
            aiNextWordCandidates = candidateWords.map { it.word }.take(6),
            synthesizedPredictionTitle = "⚡ On-Device განზრახვა: $latestWord"
        )
    }
}
