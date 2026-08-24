package com.example.service

import java.util.Locale
import kotlin.math.exp
import kotlin.math.ln

/**
 * Adaptive Memory Decay & Recency-Frequency (TF-IDF + Ebbinghaus) Engine
 * Models cognitive working memory decay over time: R = e^(-t / S)
 * Balances immediate short-term context against long-term vocabulary habits.
 */
object AdaptiveMemoryDecayEngine {

    data class MemoryTrace(
        val word: String,
        var count: Int,
        var lastAccessedTimestamp: Long,
        var strength: Float = 1.0f // Memory stability parameter S
    )

    private val workingMemoryCache = mutableMapOf<String, MemoryTrace>()
    private var totalAccessCount = 0

    // Ebbinghaus Half-Life in milliseconds (approx. 10 minutes of active session)
    private const val MEMORY_HALF_LIFE_MS = 600_000.0

    /**
     * Record access to a word, strengthening its memory trace
     */
    fun recordWordAccess(word: String) {
        val clean = word.trim().lowercase(Locale.ROOT)
        if (clean.isBlank()) return

        val now = System.currentTimeMillis()
        totalAccessCount++

        val trace = workingMemoryCache.getOrPut(clean) {
            MemoryTrace(word = clean, count = 0, lastAccessedTimestamp = now, strength = 1.0f)
        }

        trace.count += 1
        trace.lastAccessedTimestamp = now
        // Spaced repetition boost
        trace.strength = (trace.strength + 0.35f).coerceAtMost(5.0f)
    }

    /**
     * Calculate current memory retention score (0.0 to 1.0) using Ebbinghaus decay
     */
    fun getRetentionScore(word: String): Float {
        val clean = word.trim().lowercase(Locale.ROOT)
        val trace = workingMemoryCache[clean] ?: return 0.1f

        val now = System.currentTimeMillis()
        val elapsedMs = (now - trace.lastAccessedTimestamp).coerceAtLeast(0L)

        // R = exp(- (elapsed / (half_life * strength)))
        val retention = exp(-elapsedMs / (MEMORY_HALF_LIFE_MS * trace.strength)).toFloat()

        // Combine with TF-IDF term frequency
        val tf = trace.count.toFloat() / (totalAccessCount.coerceAtLeast(1))
        val tfBoost = (1.0f + ln(1.0f + tf * 10f)).toFloat()

        return (retention * tfBoost).coerceIn(0.1f, 3.5f)
    }

    /**
     * Clean expired memory traces to keep heap lean
     */
    fun pruneOldTraces() {
        val now = System.currentTimeMillis()
        val expiredKeys = workingMemoryCache.filter { (_, trace) ->
            (now - trace.lastAccessedTimestamp) > (MEMORY_HALF_LIFE_MS * 5)
        }.keys

        expiredKeys.forEach { workingMemoryCache.remove(it) }
    }
}
