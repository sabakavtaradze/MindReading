package com.example.service

/**
 * User Neuromuscular Fatigue & Adaptation Compensator
 * 100% Free & Local Physiological State Machine.
 * Tracks session duration, muscle tremor frequency drift, and adjusts signal gain thresholds dynamically.
 */
object NeuromuscularFatigueCompensator {

    data class FatigueCompensationState(
        val sessionDurationMinutes: Int,
        val estimatedFatigueLevelPct: Int,
        val adaptiveThresholdMultiplier: Float,
        val recommendedRestAlert: Boolean,
        val signalGainBoostDb: Float
    )

    private var sessionStartTimestamp = System.currentTimeMillis()
    private var totalInputsRegistered = 0

    fun registerUserInput() {
        totalInputsRegistered++
    }

    /**
     * Compute current physiological fatigue score and adaptive threshold adjustment
     */
    fun evaluateFatigue(currentJitterMagnitude: Float): FatigueCompensationState {
        val now = System.currentTimeMillis()
        val elapsedMinutes = ((now - sessionStartTimestamp) / 60_000).toInt()

        // Fatigue increases with time and high tremor/jitter
        val timeFactor = (elapsedMinutes * 1.2f).coerceAtMost(50f)
        val jitterFactor = (currentJitterMagnitude * 40f).coerceIn(0f, 35f)
        val inputDensityFactor = (totalInputsRegistered * 0.1f).coerceAtMost(15f)

        val totalFatiguePct = (timeFactor + jitterFactor + inputDensityFactor).coerceIn(5f, 95f).toInt()

        // If user is fatigued, lower the activation threshold so easier micro-gestures trigger words
        val thresholdMultiplier = if (totalFatiguePct > 60) {
            0.75f // 25% more sensitive
        } else if (totalFatiguePct > 40) {
            0.88f
        } else {
            1.0f
        }

        val gainBoost = if (totalFatiguePct > 50) 3.5f else 0.0f
        val needRest = totalFatiguePct > 80 && elapsedMinutes > 45

        return FatigueCompensationState(
            sessionDurationMinutes = elapsedMinutes,
            estimatedFatigueLevelPct = totalFatiguePct,
            adaptiveThresholdMultiplier = thresholdMultiplier,
            recommendedRestAlert = needRest,
            signalGainBoostDb = gainBoost
        )
    }

    fun resetSession() {
        sessionStartTimestamp = System.currentTimeMillis()
        totalInputsRegistered = 0
    }
}
