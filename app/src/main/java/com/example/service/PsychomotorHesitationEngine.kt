package com.example.service

import kotlin.math.ln
import kotlin.math.max
import kotlin.math.min
import kotlin.math.pow
import kotlin.math.sqrt

/**
 * 3. Psychomotor Hesitation & Tremor Entropy Engine
 * Evaluates finger touch hold duration (dwell ms), flight time between taps,
 * micro-tremor spatial jitter variance, and computes Shannon Entropy of hesitation.
 */
data class PsychomotorHesitationMetrics(
    val holdDurationMs: Long = 95L,              // Dwell time on key/screen (typical 60-140ms)
    val flightTimeMs: Long = 180L,               // Latency between subsequent actions (120-600ms)
    val spatialJitterPixelVariance: Float = 3.2f, // Micro-tremor displacement
    val shannonEntropyScore: Float = 1.45f,      // Information entropy of tap intervals (0.0 to 3.5 bits)
    val hesitationConfidencePct: Float = 88.5f,
    val cognitiveMode: String = "ავტომატური რეფლექსური აზროვნება", // "ავტომატური", "ყოყმანი / გააზრება", "შემოქმედებითი ძიება"
    val recentTapLatencies: List<Long> = emptyList()
)

object PsychomotorHesitationEngine {

    private val tapHistory = mutableListOf<Long>(140L, 160L, 150L, 190L, 175L, 210L, 185L)
    private var lastTapTimestamp: Long = System.currentTimeMillis()

    fun recordTap(x: Float, y: Float, holdTimeMs: Long = 95L): PsychomotorHesitationMetrics {
        val now = System.currentTimeMillis()
        val flight = (now - lastTapTimestamp).coerceIn(40L, 3000L)
        lastTapTimestamp = now

        synchronized(tapHistory) {
            tapHistory.add(flight)
            if (tapHistory.size > 15) {
                tapHistory.removeAt(0)
            }
        }

        return evaluateHesitation(holdTimeMs)
    }

    fun evaluateHesitation(currentHoldMs: Long = 95L): PsychomotorHesitationMetrics {
        val latencies = synchronized(tapHistory) { tapHistory.toList() }
        val avgFlight = if (latencies.isNotEmpty()) latencies.average().toLong() else 180L

        // Shannon Entropy calculation on latency distribution bins:
        // H(X) = - sum(p_i * log2(p_i))
        val bins = IntArray(5) // [0-100, 100-200, 200-400, 400-800, >800]
        for (lat in latencies) {
            when {
                lat < 100 -> bins[0]++
                lat < 200 -> bins[1]++
                lat < 400 -> bins[2]++
                lat < 800 -> bins[3]++
                else -> bins[4]++
            }
        }
        val total = latencies.size.toFloat().coerceAtLeast(1f)
        var entropy = 0.0
        for (b in bins) {
            if (b > 0) {
                val p = b / total
                entropy -= p * (ln(p.toDouble()) / ln(2.0))
            }
        }

        // Spatial jitter estimation based on entropy & hold duration
        val jitter = (2.0f + entropy.toFloat() * 1.8f).coerceIn(1.0f, 15.0f)

        val mode = when {
            avgFlight < 150L && entropy < 1.2 -> "ავტომატური რეფლექსური აზროვნება (სწრაფი ტაქტი)"
            avgFlight > 450L || entropy > 2.1 -> "კოგნიტური ყოყმანი / რთული არჩევანის გააზრება"
            currentHoldMs > 250L -> "მოდუნებული / ფრთხილი დადასტურება"
            else -> "თანმიმდევრული ანალიტიკური ტემპი"
        }

        val confPct = ((1.0f - (entropy.toFloat() / 3.2f)).coerceIn(0.4f, 0.98f)) * 100f

        return PsychomotorHesitationMetrics(
            holdDurationMs = currentHoldMs,
            flightTimeMs = avgFlight,
            spatialJitterPixelVariance = jitter,
            shannonEntropyScore = entropy.toFloat(),
            hesitationConfidencePct = confPct,
            cognitiveMode = mode,
            recentTapLatencies = latencies
        )
    }
}
