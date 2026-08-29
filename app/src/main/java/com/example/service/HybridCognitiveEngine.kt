package com.example.service

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import com.example.sensor.RealAudioState
import com.example.sensor.RealCameraGazeState
import com.example.sensor.RealHardwareSensorState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit
import kotlin.math.abs
import kotlin.math.roundToInt

/**
 * Hybrid Intelligent Cognitive & Polyvagal Behavioral Reasoning Core:
 * 1. Online Mode: Uses Gemini 2.5 Flash Cloud AI (Free Tier API) to synthesize rich, conscious insights and harvest dynamic lexical tokens.
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
    private var lastGeminiCallTime = 0L
    private val minCallIntervalMs = 8000L // 8s throttle to respect free tier (max ~7-8 requests/min)
    private var lastGeneratedSummary = ""

    data class CognitiveResult(
        val isCloudActive: Boolean,
        val modeLabel: String,
        val deepSynthesisText: String,
        val metaCognitionScore: Float,
        val intentionalFocusPrediction: String,
        val emotionalEntropyIndex: Float,
        val polyvagalResult: PolyvagalBehavioralEngine.BehavioralAnalysisResult,
        val insights: List<CognitiveInsight>,
        val recentlyDiscoveredWords: List<String>,
        val totalVocabularySize: Int,
        val telemetryLatencyMs: Long
    )

    data class CognitiveInsight(
        val title: String,
        val description: String,
        val confidence: Float,
        val type: String // "PSYCHOLOGY", "BEHAVIORAL", "INTENT", "GAZE", "ACOUSTIC", "PHYSICAL"
    )

    /**
     * Checks if actual Internet connection is available
     */
    private fun isNetworkAvailable(): Boolean {
        return try {
            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
            val activeNetwork = connectivityManager?.activeNetwork ?: return false
            val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
            capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
                    capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
        } catch (e: Exception) {
            false
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
        val apiKey = com.example.BuildConfig.GEMINI_API_KEY

        // Run On-Device Polyvagal Behavioral Analysis First
        val polyvagalAnalysis = polyvagalEngine.analyzeBehavioralState(
            audio = audio,
            gaze = gaze,
            sensors = sensors,
            cognitiveLoadPct = mentalFatigue * 100f,
            entropyIndex = emotionalEntropy
        )

        // If online, key exists and not throttled -> Try Gemini 2.5 Flash Free Tier
        if (hasInternet && apiKey.isNotBlank() && (startTime - lastGeminiCallTime > minCallIntervalMs)) {
            val cloudResult = tryGeminiReasoning(
                audio = audio,
                gaze = gaze,
                sensors = sensors,
                polyvagal = polyvagalAnalysis,
                emotionalEntropy = emotionalEntropy,
                mentalFatigue = mentalFatigue,
                focusLevel = focusLevel,
                activeThought = activeThought,
                apiKey = apiKey
            )
            if (cloudResult != null) {
                lastGeminiCallTime = startTime
                lastGeneratedSummary = cloudResult

                // 🌟 Autonomous Dynamic Lexicon Ingestion: Harvest new words & terms from the AI synthesis!
                val newHarvestedWords = AutonomousDynamicLexiconLearner.ingestFromAiStream(cloudResult)

                val latency = System.currentTimeMillis() - startTime
                return@withContext buildOnlineResult(
                    synthesis = cloudResult,
                    audio = audio,
                    gaze = gaze,
                    sensors = sensors,
                    polyvagal = polyvagalAnalysis,
                    focus = focusLevel,
                    entropy = emotionalEntropy,
                    newHarvestedWords = newHarvestedWords,
                    latencyMs = latency
                )
            }
        }

        // Offline / Fallback Local Autonomous Neural Matrix (100% On-Device Engine)
        // Dynamically discover offline concept combinations to expand vocabulary
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
            emotionalEntropy = emotionalEntropy,
            mentalFatigue = mentalFatigue,
            focusLevel = focusLevel,
            activeThought = activeThought,
            newHarvestedWords = offlineHarvested,
            latencyMs = offlineLatency
        )
    }

    /**
     * Executes Cloud Gemini 2.5 Flash Reasoning with optimized token context using standard JSONObject
     */
    private fun tryGeminiReasoning(
        audio: RealAudioState,
        gaze: RealCameraGazeState,
        sensors: RealHardwareSensorState,
        polyvagal: PolyvagalBehavioralEngine.BehavioralAnalysisResult,
        emotionalEntropy: Float,
        mentalFatigue: Float,
        focusLevel: Float,
        activeThought: String,
        apiKey: String
    ): String? {
        return try {
            val prompt = """
                შენ ხარ ტელეფონში ჩაშენებული უმაღლესი ნეირო-კოგნიტური & ქცევითი სააზროვნო ინტელექტი (NeuroSync AI).
                გააანალიზე მომხმარებლის მიმდინარე ცოცხალი მულტიმოდალური ანალიტიკა და ჩამოაყალიბე ღრმა, გააზრებული დასკვნა ქართულად (მაქსიმუმ 2-3 წინადადება). გამოიყენე მრავალფეროვანი, ცოცხალი ლექსიკა:
                
                - მიმდინარე აზრი/სიტყვა: "${activeThought.ifBlank { "ფოკუსირებული დაკვირვება" }}"
                - ქცევითი პოლივაგალური მდგომარეობა: ${polyvagal.dominantState.labelKa} (Flow: ${(polyvagal.flowStateIndex * 100).roundToInt()}%, დისონანსი: ${(polyvagal.somaticDissonanceIndex * 100).roundToInt()}%)
                - ყურადღების დრიფტის ჰორიზონტი: ${polyvagal.attentionDriftSeconds} წამი, იმპულსურობა: ${polyvagal.impulsivityRiskPct.roundToInt()}%
                - ხმის დონე & სიხშირე: ${audio.decibels.roundToInt()} dB, ${audio.dominantFrequencyHz.roundToInt()} Hz
                - გუგის დიამეტრი: ${gaze.opticalPupilDiameterMm} მმ, თვალის ხამხამი: ${gaze.eyeBlinkRatePerMin} /წთ, პულსი: ${gaze.opticalRadiancePulseBpm} BPM
                - ფოკუსი: ${(focusLevel * 100).roundToInt()}%, ენტროპია: ${(emotionalEntropy * 100).roundToInt()}%, დაღლილობა: ${(mentalFatigue * 100).roundToInt()}%
                - მოძრაობა: AccelX=${sensors.accelX.roundToInt()}, წნევა=${sensors.atmosphericPressureHpa.roundToInt()} hPa
                
                დაწერე ბუნებრივი, ჭკვიანური და ცოცხალი ნეირო-ფსიქოლოგიური დასკვნა.
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
                firstPart?.optString("text")?.trim()
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
        synthesis: String,
        audio: RealAudioState,
        gaze: RealCameraGazeState,
        sensors: RealHardwareSensorState,
        polyvagal: PolyvagalBehavioralEngine.BehavioralAnalysisResult,
        focus: Float,
        entropy: Float,
        newHarvestedWords: List<String>,
        latencyMs: Long
    ): CognitiveResult {
        val insights = mutableListOf<CognitiveInsight>()

        insights.add(
            CognitiveInsight(
                title = "🌐 Gemini 2.5 Flash ღრმა აზროვნება",
                description = synthesis,
                confidence = 0.98f,
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

        insights.add(
            CognitiveInsight(
                title = "ოპტიკურ-ვასკულარული სინქრონიზაცია",
                description = "გუგის დიამეტრი (${gaze.opticalPupilDiameterMm} მმ) და პულსი (${gaze.opticalRadiancePulseBpm} BPM) ადასტურებს მენტალურ ჩართულობას.",
                confidence = 0.94f,
                type = "GAZE"
            )
        )

        insights.add(
            CognitiveInsight(
                title = "აკუსტიკური გარემოს ინტელექტუალური დეკოდირება",
                description = "ფონური ხმაური (${audio.decibels.roundToInt()} dB) დაბალანსებულია კოგნიტურ ფილტრთან.",
                confidence = 0.91f,
                type = "ACOUSTIC"
            )
        )

        val recentTokens = AutonomousDynamicLexiconLearner.getRecentlyLearnedTokens().map { it.token }.take(8)

        return CognitiveResult(
            isCloudActive = true,
            modeLabel = "Cloud AI (Gemini 2.5 Flash) • ონლაინ აზროვნება",
            deepSynthesisText = synthesis,
            metaCognitionScore = 0.98f,
            intentionalFocusPrediction = if (focus > 0.6f) "კოგნიტური Flow & კონცენტრაცია" else "ემოციური დაკვირვება",
            emotionalEntropyIndex = entropy,
            polyvagalResult = polyvagal,
            insights = insights,
            recentlyDiscoveredWords = recentTokens,
            totalVocabularySize = AutonomousDynamicLexiconLearner.getActiveVocabularyCount(),
            telemetryLatencyMs = latencyMs
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
        emotionalEntropy: Float,
        mentalFatigue: Float,
        focusLevel: Float,
        activeThought: String,
        newHarvestedWords: List<String>,
        latencyMs: Long
    ): CognitiveResult {
        val insights = mutableListOf<CognitiveInsight>()

        // 1. Behavioral Polyvagal Dynamic Insight
        insights.add(
            CognitiveInsight(
                title = "🧠 ქცევითი & პოლივაგალური ნეირო-ანალიტიკა",
                description = polyvagal.behavioralInsightKa,
                confidence = 0.96f,
                type = "BEHAVIORAL"
            )
        )

        // 2. Acoustic Cognition
        val db = audio.decibels
        val freq = audio.dominantFrequencyHz
        val acousticContext = when {
            db < 35f -> "სრული სიჩუმე (მაღალი შინაგანი ფოკუსი და სუბვოკალური მზადყოფნა)"
            db < 65f -> "ნორმალური აკუსტიკური ფონი (${db.roundToInt()} dB, ${freq.roundToInt()} Hz)"
            else -> "მაღალი აკუსტიკური დატვირთვა - აქტიურია გარემო ხმაურის ფილტრი"
        }
        insights.add(
            CognitiveInsight(
                title = "🎙️ აკუსტიკური ნეირო-დეკოდირება",
                description = acousticContext,
                confidence = 0.92f,
                type = "ACOUSTIC"
            )
        )

        // 3. Optical Gaze & Pupil Dilation
        val pupil = gaze.opticalPupilDiameterMm
        val bpm = gaze.opticalRadiancePulseBpm
        val gazeContext = when {
            pupil > 4.0f -> "გუგის გაფართოება (${pupil}მმ) მიუთითებს მაღალ ინტერესსა და აღგზნებადობაზე"
            pupil < 3.0f -> "გუგის შევიწროება - ეკრანის შუქის ადაპტაცია და სიმშვიდე"
            else -> "სტაბილური ბიომეტრიული ბალანსი: პულსი ${bpm} BPM, ხამხამი ${gaze.eyeBlinkRatePerMin}/წთ"
        }
        insights.add(
            CognitiveInsight(
                title = "👁️ ოპტიკური მზერა & ბიომეტრია",
                description = gazeContext,
                confidence = 0.95f,
                type = "GAZE"
            )
        )

        // 4. Motion & Micro-Tremors
        val motionTremor = abs(sensors.accelX) + abs(sensors.accelY) + abs(sensors.accelZ - 9.8f)
        val physicalState = when {
            motionTremor > 4f -> "აქტიური მოძრაობა/სიარული - სენსორული კომპენსაცია ჩართულია"
            motionTremor < 0.6f -> "სრული სტატიკური უძრაობა - მაქსიმალური ფიზიკური კონცენტრაცია"
            else -> "ტელეფონის სტაბილური პოზიცია (წნევა: ${sensors.atmosphericPressureHpa.roundToInt()} hPa)"
        }
        insights.add(
            CognitiveInsight(
                title = "📱 მოწყობილობის ფიზიკური მდგომარეობა",
                description = physicalState,
                confidence = 0.96f,
                type = "PHYSICAL"
            )
        )

        // 5. Autonomous Synthesis
        val autonomousSynthesis = when (polyvagal.dominantState) {
            PolyvagalBehavioralEngine.PolyvagalState.VENTRAL_VAGAL ->
                "ტელეფონის შიდა ინტელექტი: ვენტრალ-ვაგალური ჰარმონია. ტვინი იმყოფება შემოქმედებით Flow ზონაში (${(polyvagal.flowStateIndex * 100).roundToInt()}%). ლექსიკური მატრიცა მუდმივად ფართოვდება."
            PolyvagalBehavioralEngine.PolyvagalState.SYMPATHETIC ->
                "ტელეფონის შიდა ინტელექტი: სიმპათიკური აღგზნებადობა. სენსორები აფიქსირებენ მიკრო-შფოთვას. ყურადღების დრიფტის დრო შეადგენს ${polyvagal.attentionDriftSeconds} წამს."
            PolyvagalBehavioralEngine.PolyvagalState.DORSAL_VAGAL ->
                "ტელეფონის შიდა ინტელექტი: დორსალური შეკავება. დაღლილობის ინდექსი მომატებულია, ალგორითმი იყენებს ენერგიის დამზოგავ აზროვნების ციკლს."
        }

        val recentTokens = AutonomousDynamicLexiconLearner.getRecentlyLearnedTokens().map { it.token }.take(8)

        return CognitiveResult(
            isCloudActive = false,
            modeLabel = "ტელეფონის შიდა ინტელექტი (On-Device Offline) • 100% ავტონომიური",
            deepSynthesisText = if (lastGeneratedSummary.isNotBlank()) "$autonomousSynthesis\n(ბოლო ღრუბლოვანი ანალიზი: $lastGeneratedSummary)" else autonomousSynthesis,
            metaCognitionScore = 0.94f,
            intentionalFocusPrediction = if (focusLevel > 0.6f) "კოგნიტური კონცენტრაცია" else "ემოციური დაკვირვება",
            emotionalEntropyIndex = emotionalEntropy,
            polyvagalResult = polyvagal,
            insights = insights,
            recentlyDiscoveredWords = recentTokens,
            totalVocabularySize = AutonomousDynamicLexiconLearner.getActiveVocabularyCount(),
            telemetryLatencyMs = latencyMs
        )
    }
}
