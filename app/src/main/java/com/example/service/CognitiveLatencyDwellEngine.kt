package com.example.service

import kotlin.random.Random

data class CognitiveLatencyMetrics(
    val stimulusResponseLatencyMs: Long = 420L,
    val decisionMode: String = "ინტუიციური სწრაფი პასუხი (System 1 Fast)",
    val cognitiveFrictionIndex: Float = 0.22f, // 0.0 to 1.0 (Higher = deliberating/hesitating/filtering truth)
    val hesitationSpikesCount: Int = 1,
    val dwellTimeBeforeActionMs: Long = 850L,
    val thoughtDirectnessConfidencePct: Float = 94.2f,
    val georgianLatencyInsight: String = "მოკლე ლატენტობა (420მწმ) მიუთითებს მყისიერ, გაუცნობიერებლად ჩამოყალიბებულ აზრზე."
)

class CognitiveLatencyDwellEngine {

    private val latencyModes = listOf(
        Triple(280L, "ინტუიციური პირდაპირი იმპულსი (System 1 Fast)", 0.12f),
        Triple(750L, "დაბალანსებული გააზრებული გადაწყვეტილება (System 1.5)", 0.35f),
        Triple(1650L, "სტრატეგიული აწონ-დაწონვა და ლოგიკური ანალიზი (System 2 Deep)", 0.78f),
        Triple(2400L, "კოგნიტური ფრიქცია და ალტერნატივების შედარება (High Friction)", 0.92f)
    )

    private var latencyIndex = 0

    fun computeLatency(
        touchDwellMs: Long = 300L,
        gazeFixationMs: Long = 400L,
        isUserMoving: Boolean = false
    ): CognitiveLatencyMetrics {
        val (baseLatency, modeName, baseFriction) = latencyModes[latencyIndex % latencyModes.size]

        val finalLatency = (baseLatency + touchDwellMs * 0.3f + Random.nextLong(80)).toLong().coerceIn(180L, 4000L)
        val finalFriction = (baseFriction + if (finalLatency > 1200L) 0.15f else -0.05f).coerceIn(0.05f, 0.98f)
        val spikes = if (finalLatency > 1500L) 3 else if (finalLatency > 800L) 1 else 0
        val directnessPct = (100f - (finalFriction * 50f) + Random.nextFloat() * 5f).coerceIn(55f, 99f)

        val insight = when {
            finalLatency < 350L -> "მყისიერი იმპულსი ($finalLatency მწმ) — აზრი უკვე მზად იყო ქვეცნობიერში."
            finalLatency in 350L..900L -> "სტაბილური გააზრება ($finalLatency მწმ) — ოპტიმალური შემეცნებითი ტემპი."
            finalLatency in 901L..1800L -> "სტრატეგიული ანალიზი ($finalLatency მწმ) — ტვინი ამოწმებს რამდენიმე ვარიანტს."
            else -> "მაღალი კოგნიტური ფრიქცია ($finalLatency მწმ) — მიმდინარეობს რთული ლოგიკური გათვლა."
        }

        return CognitiveLatencyMetrics(
            stimulusResponseLatencyMs = finalLatency,
            decisionMode = modeName,
            cognitiveFrictionIndex = finalFriction,
            hesitationSpikesCount = spikes,
            dwellTimeBeforeActionMs = (finalLatency * 1.6f).toLong(),
            thoughtDirectnessConfidencePct = directnessPct,
            georgianLatencyInsight = insight
        )
    }

    fun stepNextLatencyState(): CognitiveLatencyMetrics {
        latencyIndex++
        return computeLatency()
    }
}
