package com.example.service

import com.example.sensor.RealAudioState
import com.example.sensor.RealCameraGazeState
import com.example.sensor.RealHardwareSensorState
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt

/**
 * Polyvagal Behavioral Entity & Somatic Neuro-Dynamics Predictor:
 * Evaluates autonomic nervous system regulation based on Polyvagal Theory:
 * 1. Ventral Vagal (Social Engagement & Creative Flow State)
 * 2. Sympathetic (Fight-or-Flight / Acute Stress & Micro-Tremors)
 * 3. Dorsal Vagal (Freeze / Energy Depletion / Cognitive Shutdown)
 *
 * Also computes:
 * - Somatic Cognitive Dissonance (Discrepancy between outward calm and internal autonomic strain)
 * - Micro-Attention Drift & Impulsivity Horizon (Estimated seconds before distraction)
 * - Flow State Coherence Index
 */
class PolyvagalBehavioralEngine {

    enum class PolyvagalState(val labelKa: String, val descriptionKa: String) {
        VENTRAL_VAGAL("ვენტრალ-ვაგალური (Ventral Vagal)", "სოციალური უსაფრთხოება, შემოქმედებითი Flow და მაღალი კოგნიტური სინქრონი"),
        SYMPATHETIC("სიმპათიკური (Sympathetic)", "აქტიური სტრესი, შფოთვა, ბრძოლა-ან-გაქცევა და გაფანტული ყურადღება"),
        DORSAL_VAGAL("დორსალ-ვაგალური (Dorsal Vagal)", "ენერგიის გამოფიტვა, გათიშვა (Freeze) და კოგნიტური დაღლილობა")
    }

    data class BehavioralAnalysisResult(
        val dominantState: PolyvagalState,
        val ventralScore: Float,       // 0.0 to 1.0
        val sympatheticScore: Float,   // 0.0 to 1.0
        val dorsalScore: Float,        // 0.0 to 1.0
        val somaticDissonanceIndex: Float, // 0.0 (aligned) to 1.0 (high internal discord)
        val attentionDriftSeconds: Int,    // 5 to 180 seconds
        val impulsivityRiskPct: Float,     // 0% to 100%
        val flowStateIndex: Float,         // 0.0 to 1.0
        val behavioralInsightKa: String
    )

    private var baselineBpm: Float = 72f
    private var baselineDecibels: Float = 42f

    fun analyzeBehavioralState(
        audio: RealAudioState,
        gaze: RealCameraGazeState,
        sensors: RealHardwareSensorState,
        cognitiveLoadPct: Float,
        entropyIndex: Float
    ): BehavioralAnalysisResult {
        val bpm = gaze.opticalRadiancePulseBpm.toFloat().coerceIn(45f, 160f)
        val pupil = gaze.opticalPupilDiameterMm
        val blinkRate = gaze.eyeBlinkRatePerMin
        val db = audio.decibels.coerceIn(20f, 100f)
        val motionTremor = (abs(sensors.accelX) + abs(sensors.accelY) + abs(sensors.accelZ - 9.8f)).coerceIn(0f, 15f)

        // 1. Sympathetic Activation Score (Elevated BPM, Rapid Blinking, High Motion, Tremors)
        val bpmElevation = ((bpm - baselineBpm) / 35f).coerceIn(0f, 1f)
        val blinkElevation = ((blinkRate - 18) / 25f).coerceIn(0f, 1f)
        val tremorElevation = (motionTremor / 4.0f).coerceIn(0f, 1f)
        val audioArousal = ((db - baselineDecibels) / 30f).coerceIn(0f, 1f)
        val sympathetic = (bpmElevation * 0.35f + blinkElevation * 0.25f + tremorElevation * 0.20f + audioArousal * 0.20f).coerceIn(0f, 1f)

        // 2. Dorsal Vagal Score (Hypo-arousal, High Cognitive Fatigue, Low Reactivity, Slow BPM or Sluggish Gaze)
        val fatigueWeight = (cognitiveLoadPct / 100f).coerceIn(0f, 1f)
        val hypoPupil = if (pupil < 2.8f) 0.6f else 0.1f
        val lowHeartRate = if (bpm < 58f) 0.7f else 0.1f
        val dorsal = (fatigueWeight * 0.50f + hypoPupil * 0.25f + lowHeartRate * 0.25f).coerceIn(0f, 1f)

        // 3. Ventral Vagal (Calm, coherent breathing, steady gaze, optimal pupil, balanced heart rate)
        val steadyGaze = if (pupil in 3.0f..4.2f && blinkRate in 12..24) 0.85f else 0.3f
        val steadyMotion = if (motionTremor < 1.0f) 0.9f else 0.3f
        val ventral = ((1.0f - max(sympathetic, dorsal)) * 0.5f + steadyGaze * 0.3f + steadyMotion * 0.2f).coerceIn(0.05f, 0.98f)

        // Determine Dominant State
        val dominantState = when {
            ventral >= sympathetic && ventral >= dorsal -> PolyvagalState.VENTRAL_VAGAL
            sympathetic > dorsal -> PolyvagalState.SYMPATHETIC
            else -> PolyvagalState.DORSAL_VAGAL
        }

        // 4. Somatic Cognitive Dissonance
        // High if external sound/motion is calm (db < 45, tremor < 0.5) but internal BPM or sympathetic arousal is high
        val outwardCalm = if (db < 45f && motionTremor < 0.8f) 1.0f else 0.2f
        val internalStress = sympathetic
        val somaticDissonance = (outwardCalm * internalStress * 0.85f + (entropyIndex * 0.35f)).coerceIn(0.05f, 0.95f)

        // 5. Attention Drift Horizon & Impulsivity Risk
        // High sympathetic/dorsal reduces attention span
        val baseSeconds = 90f
        val driftModifier = (ventral * 1.5f) - (sympathetic * 0.8f) - (dorsal * 0.9f)
        val attentionDrift = (baseSeconds * (1f + driftModifier)).toInt().coerceIn(6, 180)
        val impulsivityRisk = (sympathetic * 65f + entropyIndex * 35f).coerceIn(5f, 98f)

        // 6. Flow State Coherence Index
        val flowState = (ventral * 0.7f + (1f - somaticDissonance) * 0.3f).coerceIn(0.05f, 0.99f)

        // Generate Georgian Behavioral Synthesis Insight
        val insightKa = when (dominantState) {
            PolyvagalState.VENTRAL_VAGAL -> {
                if (flowState > 0.75f) {
                    "მაღალი კოგნიტური ჰარმონია: ნერვული სისტემა იმყოფება სრულ Flow მდგომარეობაში. ყურადღების დრიფტი მინიმალურია (${attentionDrift}წმ), აზრთა სინთეზი მაქსიმალურად პროდუქტიულია."
                } else {
                    "სტაბილური სოციალურ-კოგნიტური უსაფრთხოება: ბიომეტრიული პარამეტრები გაწონასწორებულია, ტვინი მზადაა ახალი ცნებების ასათვისებლად."
                }
            }
            PolyvagalState.SYMPATHETIC -> {
                "სიმპათიკური აღგზნებადობა: დაფიქსირებულია პულსის აჩქარება (${bpm.roundToInt()} BPM) და მიკრო-შფოთვა. იმპულსურობის რისკი: ${impulsivityRisk.roundToInt()}%. ყურადღება შეიძლება გაიფანტოს ${attentionDrift} წამში."
            }
            PolyvagalState.DORSAL_VAGAL -> {
                "დორსალური კოგნიტური შეკავება: ფიქსირდება ენერგეტიკული გადაღლა (${(dorsal * 100).roundToInt()}%). რეკომენდებულია სენსორული გადატვირთვის შემცირება და დასვენება."
            }
        }

        return BehavioralAnalysisResult(
            dominantState = dominantState,
            ventralScore = ventral,
            sympatheticScore = sympathetic,
            dorsalScore = dorsal,
            somaticDissonanceIndex = somaticDissonance,
            attentionDriftSeconds = attentionDrift,
            impulsivityRiskPct = impulsivityRisk,
            flowStateIndex = flowState,
            behavioralInsightKa = insightKa
        )
    }
}
