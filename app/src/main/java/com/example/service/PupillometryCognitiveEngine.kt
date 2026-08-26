package com.example.service

import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sin
import kotlin.math.sqrt

/**
 * 2. Pupillometry & Gaze Micro-Saccades Cognitive Attention Engine
 * Measures pupil diameter (mm), phasic vs tonic dilation velocity (Locus Coeruleus-Norepinephrine system),
 * micro-saccade frequency (Hz), and triggers "Aha! Moment" decision thresholds.
 */
data class PupillometryMetrics(
    val pupilDiameterMm: Float = 3.8f,          // 2.0mm (constricted/bright) to 8.0mm (dilated/aroused)
    val baselinePupilMm: Float = 3.6f,
    val phasicDilationVelocity: Float = 0.42f,   // mm/s (surge during cognitive revelation)
    val microSaccadeRateHz: Float = 1.8f,        // 1-3 Hz normal, >4 Hz high visual search
    val isAhaDecisionMoment: Boolean = false,    // Sudden LC-NE noradrenaline surge (>0.6mm dilation within 250ms)
    val visualFocusFixationDurationMs: Long = 620L,
    val visualEntropyScore: Float = 0.35f,       // 0.0 (laser focus) to 1.0 (scattered gaze)
    val attentionStateDescription: String = "სტაბილური ვიზუალური ფოკუსირება",
    val recentDilationTrace: List<Float> = emptyList()
)

object PupillometryCognitiveEngine {

    private val dilationTrace = mutableListOf<Float>(3.5f, 3.6f, 3.55f, 3.7f, 3.8f, 3.85f, 3.8f)
    private var lastDilationVelocity = 0.2f
    private var ahaCooldownTicks = 0

    fun computePupillometry(
        ambientLux: Float,
        gazeX: Float,
        gazeY: Float,
        isGazeActive: Boolean,
        cognitiveLoad: Float,
        timeDeltaMs: Long = 200L
    ): PupillometryMetrics {
        // Ambient light reflex: higher lux -> pupil constricts
        val lightConstriction = when {
            ambientLux > 600f -> 1.2f
            ambientLux > 200f -> 0.6f
            ambientLux > 50f -> 0.0f
            else -> -0.8f // Dilation in dark
        }

        // Cognitive dilation (NE activation from Locus Coeruleus)
        val cognitiveDilation = cognitiveLoad * 1.6f

        val calculatedDiameter = (3.8f - lightConstriction + cognitiveDilation).coerceIn(2.1f, 7.8f)

        synchronized(dilationTrace) {
            dilationTrace.add(calculatedDiameter)
            if (dilationTrace.size > 16) {
                dilationTrace.removeAt(0)
            }
        }

        // Velocity = delta / dt
        val prevDiameter = if (dilationTrace.size >= 2) dilationTrace[dilationTrace.size - 2] else calculatedDiameter
        val velocity = abs(calculatedDiameter - prevDiameter) / (timeDeltaMs / 1000f)
        lastDilationVelocity = (lastDilationVelocity * 0.7f + velocity * 0.3f)

        // Micro-saccades frequency: estimated from gaze vector fluctuations
        val gazeRadius = sqrt(gazeX * gazeX + gazeY * gazeY)
        val saccadeHz = (1.2f + gazeRadius * 3.5f + cognitiveLoad * 1.5f).coerceIn(0.5f, 5.8f)

        // "Aha! Moment" trigger detection: high dilation velocity + high cognitive load
        if (ahaCooldownTicks > 0) ahaCooldownTicks--
        val isAha = (velocity > 1.2f || (cognitiveLoad > 0.82f && lastDilationVelocity > 0.8f)) && ahaCooldownTicks == 0
        if (isAha) {
            ahaCooldownTicks = 8 // prevent continuous trigger
        }

        val visualEntropy = (saccadeHz / 5.5f).coerceIn(0.1f, 0.95f)

        val desc = when {
            isAha -> "💡 AHA! MOMENT — გადაწყვეტილების მიღების ნეირონული იმპულსი"
            visualEntropy > 0.7f -> "გაფანტული ძიება / ალტერნატივების შეფასება"
            cognitiveDilation > 1.0f -> "ღრმა მენტალური კონცენტრაცია (High Pupil Load)"
            else -> "სტაბილური ვიზუალური ფოკუსირება"
        }

        return PupillometryMetrics(
            pupilDiameterMm = calculatedDiameter,
            baselinePupilMm = 3.6f,
            phasicDilationVelocity = lastDilationVelocity,
            microSaccadeRateHz = saccadeHz,
            isAhaDecisionMoment = isAha,
            visualFocusFixationDurationMs = (800L / saccadeHz.toLong().coerceAtLeast(1)).coerceIn(150L, 2000L),
            visualEntropyScore = visualEntropy,
            attentionStateDescription = desc,
            recentDilationTrace = dilationTrace.toList()
        )
    }

    fun triggerAhaMoment(ambientLux: Float = 120f): PupillometryMetrics {
        lastDilationVelocity = 1.6f
        ahaCooldownTicks = 0
        return computePupillometry(
            ambientLux = ambientLux,
            gazeX = 0.2f,
            gazeY = -0.1f,
            isGazeActive = true,
            cognitiveLoad = 0.96f
        )
    }
}
