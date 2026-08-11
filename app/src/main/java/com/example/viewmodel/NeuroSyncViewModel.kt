package com.example.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.BuildConfig
import com.example.data.AppDatabase
import com.example.data.PredictionEntity
import com.example.data.PredictionRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.util.concurrent.TimeUnit
import kotlin.random.Random

data class TelemetryState(
    val touchValue: Float = 0.85f,
    val audioValue: Float = 0.42f,
    val visualValue: Float = 0.91f,
    val motionValue: Float = 0.65f,
    val biometricsValue: Float = 0.78f,
    val neuralValue: Float = 0.98f
)

data class NeuroSyncUiState(
    val isSyncing: Boolean = true,
    val matchPercentage: Float = 98.4f,
    val statusText: String = "SYNAPTIC OVERLAY: 98.4% MATCH",
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
    val currentPredictionTitle: String = "Mind Thought Prediction",
    val currentPredictionText: String = "Predicted Thought: User is forming an intention to optimize code architecture and evaluate upcoming feature designs.",
    val currentActionPlan: String = "• Synthesizing high-focus cognitive environment\n• Pre-allocating IDE buffers for rapid input\n• Filtering ambient audio frequencies",
    val dominantMindThought: String = "Formulating next architecture optimization step",
    val thoughtCognitiveLoadPct: Int = 42,
    val subconsciousFocusLevel: String = "Deep Flow State (Alpha 10.2 Hz)",
    val alphaBandHz: Float = 10.2f,
    val betaBandHz: Float = 18.5f,
    val thetaBandHz: Float = 6.1f,
    val gammaBandHz: Float = 42.0f,
    val telemetry: TelemetryState = TelemetryState(),
    val isGeneratingPrediction: Boolean = false,
    val isPermissionsModalOpen: Boolean = false,
    val isExplanationModalOpen: Boolean = false,
    val micPermissionGranted: Boolean = false,
    val usageStatsPermissionGranted: Boolean = false,
    val accessibilityPermissionGranted: Boolean = false,
    val overlayPermissionGranted: Boolean = false
)

class NeuroSyncViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: PredictionRepository
    val history: StateFlow<List<PredictionEntity>>

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

        // Seed initial history if empty
        viewModelScope.launch {
            if (history.value.isEmpty()) {
                repository.insert(
                    PredictionEntity(
                        title = "Cognitive Focus Mode",
                        summary = "Detected deep coding activity and low audio noise. Recommending uninterrupted session.",
                        matchConfidence = 98.4f,
                        touchActivityLevel = "High Precision (4.2 Hz)",
                        audioSpectrumDb = "28.5 dB (Quiet Ambient)",
                        visualContext = "Android Studio / Jetpack Compose",
                        neuralSyncRate = "98.4%",
                        actionPlan = "Suppressed background notifications for 45 mins"
                    )
                )
            }
        }

        startTelemetryLoop()
    }

    private fun startTelemetryLoop() {
        viewModelScope.launch {
            while (true) {
                delay(1200)
                if (_uiState.value.isSyncing) {
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
                    val newMatch = 97f + Random.nextFloat() * 2.8f

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
                        "Deep Flow State (Alpha ${String.format("%.1f", newAlpha)} Hz)",
                        "Active Problem Solving (Beta ${String.format("%.1f", newBeta)} Hz)",
                        "Subconscious Processing (Theta ${String.format("%.1f", newTheta)} Hz)",
                        "High Insight Spike (Gamma ${String.format("%.1f", newGamma)} Hz)"
                    )

                    _uiState.value = _uiState.value.copy(
                        matchPercentage = String.format("%.1f", newMatch).toFloat(),
                        statusText = "SYNAPTIC OVERLAY: ${String.format("%.1f", newMatch)}% MATCH",
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
            }
        }
    }

    fun toggleSyncing() {
        _uiState.update { it.copy(isSyncing = !it.isSyncing) }
    }

    fun registerTouchTap(x: Float, y: Float) {
        val newCount = _uiState.value.touchTapsCount + 1
        _uiState.update {
            it.copy(
                touchTapsCount = newCount,
                lastTouchCoords = "X: ${x.toInt()}, Y: ${y.toInt()}"
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
        _uiState.update { it.copy(micPermissionGranted = granted) }
    }

    fun setUsageStatsPermission(granted: Boolean) {
        _uiState.update { it.copy(usageStatsPermissionGranted = granted) }
    }

    fun setOverlayPermission(granted: Boolean) {
        _uiState.update { it.copy(overlayPermissionGranted = granted) }
    }

    fun setAccessibilityPermission(granted: Boolean) {
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
                        subconsciousFocusLevel = "High Beta Spike (Hyper-Alertness)",
                        currentPredictionText = "Predicted Thought: User is experiencing rapid task-switching impulse and elevated mental processing speed.",
                        currentActionPlan = "• Accelerating IDE auto-complete latency\n• Dimming blue light output\n• Queueing high-bpm ambient track"
                    )
                }
            }
            "DEEP_BREATHING" -> {
                _uiState.update {
                    it.copy(
                        alphaBandHz = 11.8f,
                        thetaBandHz = 7.2f,
                        thoughtCognitiveLoadPct = 18,
                        subconsciousFocusLevel = "Deep Alpha Harmony (11.8 Hz)",
                        currentPredictionText = "Predicted Thought: User is entering calm parasympathetic rest state with heightened creative problem solving.",
                        currentActionPlan = "• Softening UI color scheme\n• Muting non-critical background notifications\n• Enabling spatial acoustic flow"
                    )
                }
            }
            "COMPLEX_PROBLEM" -> {
                _uiState.update {
                    it.copy(
                        gammaBandHz = 52.0f,
                        betaBandHz = 24.1f,
                        thoughtCognitiveLoadPct = 84,
                        subconsciousFocusLevel = "Gamma Insight Peak (52.0 Hz)",
                        currentPredictionText = "Predicted Thought: User is synthesizing complex algorithmic dependencies and multi-variable logic structure.",
                        currentActionPlan = "• Isolating active code panel\n• Allocating extra CPU priority to compiler\n• Blocking external interruptions"
                    )
                }
            }
            "NOISE_MASKING" -> {
                _uiState.update {
                    it.copy(
                        thetaBandHz = 6.8f,
                        alphaBandHz = 10.4f,
                        thoughtCognitiveLoadPct = 32,
                        subconsciousFocusLevel = "Isochronic Acoustic Masking State",
                        currentPredictionText = "Predicted Thought: User is seeking quiet mental space free from ambient acoustic distractions.",
                        currentActionPlan = "• Generating counter-phase white noise\n• Filtering room reverberation harmonics"
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
                    touchActivityLevel = "BCI Direct Mind Interface",
                    audioSpectrumDb = "${_uiState.value.heartRateBpm} BPM / Subconscious Sync",
                    visualContext = _uiState.value.activeAppContext,
                    neuralSyncRate = "${String.format("%.1f", _uiState.value.matchPercentage)}%",
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
            val prompt = """
                You are NeuroSync AI, a futuristic real-time human intent predictor.
                Context signals:
                - Touch Taps Activity: ${_uiState.value.touchTapsCount} taps
                - Last Touch Position: ${_uiState.value.lastTouchCoords}
                - Audio Ambient Decibel: ${_uiState.value.audioDb} dB
                - Active Foreground Screen App: ${_uiState.value.activeAppContext}
                
                Generate a concise, highly realistic intent prediction in JSON format with these exact keys:
                {
                  "title": "Short Intent Title (3-5 words)",
                  "summary": "Detailed intent prediction explaining what user plans to do in next 2-5 mins",
                  "actionPlan": "3 bullet points of proactive assistant actions"
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

                val title = parsed.optString("title", "Intent Prediction")
                val summary = parsed.optString("summary", "User is transitioning tasks. Preparing optimized environment.")
                val actionPlan = parsed.optString("actionPlan", "• Syncing context\n• Optimizing screen contrast\n• Preparing quick actions")

                val newPrediction = PredictionEntity(
                    title = title,
                    summary = summary,
                    matchConfidence = _uiState.value.matchPercentage,
                    touchActivityLevel = "${_uiState.value.touchTapsCount} Taps (${_uiState.value.lastTouchCoords})",
                    audioSpectrumDb = "${String.format("%.1f", _uiState.value.audioDb)} dB",
                    visualContext = _uiState.value.activeAppContext,
                    neuralSyncRate = "${_uiState.value.matchPercentage}%",
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
        val samplePredictions = listOf(
            Triple(
                "Break & Relaxation Planned",
                "User is finishing a heavy typing cycle with quiet audio. Likely planning a 10-minute break. Preparing 'Deep Focus' playlist and dimming display in 120s.",
                "• Dim display luminance by 25%\n• Queue ambient resting soundscape\n• Silence incoming non-urgent pings"
            ),
            Triple(
                "Communication & Message Drafting",
                "Rapid touch tap sequences detected on input field with active social app. User intends to dispatch an urgent response.",
                "• Autocomplete common phrases\n• Verify recipient availability\n• Keep keyboard responsive"
            ),
            Triple(
                "Technical Deep Dive & Code Review",
                "High visual gaze stability on technical documentation and low audio background. User is solving a complex algorithmic problem.",
                "• Pre-fetch relevant stack documentation\n• Enable dark high-contrast mode\n• Reserve local compute resources"
            ),
            Triple(
                "Media Consumption & Audio Stream",
                "Audio spectrum fluctuation combined with full-screen context change. User intends to watch a video tutorial or listen to podcast.",
                "• Route high-fidelity audio output\n• Lock screen rotation\n• Disable idle screen timeout"
            )
        )

        val selected = samplePredictions.random()
        val newPrediction = PredictionEntity(
            title = selected.first,
            summary = selected.second,
            matchConfidence = _uiState.value.matchPercentage,
            touchActivityLevel = "${_uiState.value.touchTapsCount} Taps (${_uiState.value.lastTouchCoords})",
            audioSpectrumDb = "${String.format("%.1f", _uiState.value.audioDb)} dB",
            visualContext = _uiState.value.activeAppContext,
            neuralSyncRate = "${_uiState.value.matchPercentage}%",
            actionPlan = selected.third
        )

        repository.insert(newPrediction)

        _uiState.update {
            it.copy(
                currentPredictionTitle = selected.first,
                currentPredictionText = selected.second,
                currentActionPlan = selected.third
            )
        }
    }

    fun clearHistory() {
        viewModelScope.launch {
            repository.clearAll()
        }
    }
}
