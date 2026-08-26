package com.example.service

import kotlin.math.PI
import kotlin.math.sin
import kotlin.random.Random

data class RespiratoryPatternMetrics(
    val respirationRateBpm: Float = 14.5f,
    val breathingPhase: String = "INHALATION", // INHALATION, EXHALATION, COGNITIVE_APNEA_HOLD, RESTING_RHYTHM
    val apneaHoldDurationSec: Float = 0.0f,
    val isCognitiveApneaActive: Boolean = false,
    val vagalRespiratoryIndex: Float = 0.82f,
    val thoughtPacingState: String = "მშვიდი დიფუზური რიტმი",
    val tidalVolumeEstimationMl: Float = 480f,
    val recentBreathingTrace: List<Float> = emptyList(),
    val georgianInsight: String = "სუნთქვის რიტმი ოპტიმალურია. კოგნიტური დატვირთვა სტაბილურია."
)

class RespiratoryPatternEngine {

    private val breathingTrace = ArrayDeque<Float>(32)
    private var phaseAngle = 0.0f
    private var apneaTicks = 0
    private var isSimulatedApnea = false

    init {
        for (i in 0 until 32) {
            val y = sin(i * 0.25f) * 0.8f
            breathingTrace.addLast(y)
        }
    }

    fun computeRespiration(
        accelZ: Float = 9.8f,
        accelTremor: Float = 0.05f,
        audioDb: Float = 28f,
        stressLevelPct: Int = 35,
        cognitiveLoad: Float = 0.6f
    ): RespiratoryPatternMetrics {
        // High cognitive load or deliberate focus induces "Cognitive Apnea" (breath holding while thinking)
        val isDeepThinking = cognitiveLoad > 0.82f || isSimulatedApnea

        if (isDeepThinking) {
            apneaTicks++
        } else {
            apneaTicks = 0
            isSimulatedApnea = false
        }

        val apneaSec = apneaTicks * 0.5f
        val isApnea = apneaSec >= 2.0f

        val baseBpm = if (isApnea) {
            7.5f
        } else {
            12.0f + (stressLevelPct * 0.12f) + (Random.nextFloat() * 1.2f)
        }

        val phaseSpeed = if (isApnea) 0.02f else (baseBpm / 60f) * 2f * PI.toFloat() * 0.15f
        phaseAngle = (phaseAngle + phaseSpeed) % (2f * PI.toFloat())

        val wave = if (isApnea) {
            0.85f + (sin(phaseAngle * 0.5f) * 0.05f)
        } else {
            sin(phaseAngle) + (accelTremor * 0.2f)
        }

        breathingTrace.addLast(wave)
        if (breathingTrace.size > 32) {
            breathingTrace.removeFirst()
        }

        val phaseName = when {
            isApnea -> "COGNITIVE_APNEA_HOLD"
            wave > 0.25f -> "INHALATION"
            wave < -0.25f -> "EXHALATION"
            else -> "RESTING_RHYTHM"
        }

        val pacingState = when {
            isApnea -> "🧠 კოგნიტური აპნოე • ღრმა ლოგიკური გადაწყვეტილების მიღება"
            stressLevelPct > 70 -> "⚡ აჩქარებული რესპირაცია • მაღალი აგზნებადობა"
            baseBpm < 13f -> "🧘 ღრმა ვაგუსური რელაქსაცია • შემოქმედებითი დიფუზია"
            else -> "🌱 დაბალანსებული ფოკუსი • უწყვეტი აზროვნება"
        }

        val vagalIndex = (1.0f - (stressLevelPct / 120f) + if (isApnea) 0.15f else 0.0f).coerceIn(0.1f, 1.0f)
        val tidalMl = 420f + (1.0f - stressLevelPct / 100f) * 180f + if (isApnea) 100f else 0f

        val georgianSummary = if (isApnea) {
            "დაფიქსირებულია სუნთქვის შეკავება (${String.format("%.1f", apneaSec)}წმ) — ტვინი ამუშავებს რთულ ლოგიკურ არგუმენტს."
        } else {
            "სუნთქვის სიხშირე: ${String.format("%.1f", baseBpm)} BPM. ვაგუსური ინდექსი: ${String.format("%.2f", vagalIndex)}."
        }

        return RespiratoryPatternMetrics(
            respirationRateBpm = baseBpm,
            breathingPhase = phaseName,
            apneaHoldDurationSec = apneaSec,
            isCognitiveApneaActive = isApnea,
            vagalRespiratoryIndex = vagalIndex,
            thoughtPacingState = pacingState,
            tidalVolumeEstimationMl = tidalMl,
            recentBreathingTrace = breathingTrace.toList(),
            georgianInsight = georgianSummary
        )
    }

    fun triggerCognitiveApneaSimulation(): RespiratoryPatternMetrics {
        isSimulatedApnea = true
        apneaTicks = 6
        return computeRespiration(cognitiveLoad = 0.95f)
    }
}
