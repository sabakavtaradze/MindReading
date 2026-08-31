package com.example.service

import kotlin.math.abs
import kotlin.math.exp
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sqrt

/**
 * Continuous Modern Hopfield Associative Memory Network.
 * Uses energy minimization (Lyapunov Energy Function) and associative projection
 * to reconstruct degraded/incomplete neural patterns and recall past cognitive memories.
 */
class HopfieldMemoryNetwork(
    val patternDimension: Int = 32,
    val maxStoredPatterns: Int = 16
) {
    // Memory bank matrix: Stored cognitive archetypes (M patterns of dimension D)
    private val memoryBank = Array(maxStoredPatterns) { FloatArray(patternDimension) { 0f } }
    private val patternLabels = Array(maxStoredPatterns) { "Memory_${it + 1}" }
    private var storedCount = 0
    private var currentEnergy: Float = 0.0f
    private var memoryConvergenceIndex: Float = 0.0f
    private var lastRecalledIndex: Int = 0

    init {
        // Seed baseline cognitive attractor states (Focus, Flow, High Energy, Resting, Creative Synthesis, Hesitation)
        seedAttractorPattern(0, "ღრმა ფოკუსი & ანალიზი", FloatArray(patternDimension) { i -> if (i % 2 == 0) 1.0f else -0.5f })
        seedAttractorPattern(1, "პოლივაგალური Flow", FloatArray(patternDimension) { i -> if (i % 3 == 0) 1.0f else 0.2f })
        seedAttractorPattern(2, "აკუსტიკური მეტყველება", FloatArray(patternDimension) { i -> if (i < patternDimension / 2) 0.9f else -0.8f })
        seedAttractorPattern(3, "ვიზუალური ფიქსაცია", FloatArray(patternDimension) { i -> if (i % 4 == 0) 1.2f else -0.2f })
        seedAttractorPattern(4, "სისტემური ლოგიკა", FloatArray(patternDimension) { i -> if (i % 5 == 0) 1.1f else 0.6f })
        seedAttractorPattern(5, "ემოციური ენტროპია", FloatArray(patternDimension) { i -> if (i % 2 != 0) 1.0f else -1.0f })
    }

    private fun seedAttractorPattern(index: Int, label: String, vector: FloatArray) {
        if (index in 0 until maxStoredPatterns) {
            val normalized = normalizeVector(vector)
            System.arraycopy(normalized, 0, memoryBank[index], 0, patternDimension)
            patternLabels[index] = label
            storedCount = max(storedCount, index + 1)
        }
    }

    data class HopfieldResult(
        val energy: Float,
        val energyDelta: Float,
        val recalledPatternLabel: String,
        val similarityScore: Float,
        val reconstructedVector: FloatArray,
        val convergenceScore: Float,
        val storedMemoriesCount: Int = 6
    )

    /**
     * Associates input state vector with stored memory attractor basins.
     * Computes continuous Hopfield energy: E = -beta^-1 * log(sum(exp(beta * (x . m_i))))
     */
    @Synchronized
    fun recallAndMinimizeEnergy(
        inputVector: FloatArray,
        temperatureBeta: Float = 4.0f
    ): HopfieldResult {
        val normalizedInput = normalizeVector(inputVector.copyOf(patternDimension))
        val affinities = FloatArray(storedCount)
        var maxAffinity = -Float.MAX_VALUE
        var bestIndex = 0

        for (i in 0 until storedCount) {
            var dot = 0.0f
            for (d in 0 until patternDimension) {
                dot += normalizedInput[d] * memoryBank[i][d]
            }
            affinities[i] = dot
            if (dot > maxAffinity) {
                maxAffinity = dot
                bestIndex = i
            }
        }

        // Softmax-like projection for modern continuous Hopfield
        var sumExp = 0.0f
        val expWeights = FloatArray(storedCount)
        for (i in 0 until storedCount) {
            val expVal = exp(min(20.0f, max(-20.0f, temperatureBeta * (affinities[i] - maxAffinity))))
            expWeights[i] = expVal
            sumExp += expVal
        }

        val reconstructed = FloatArray(patternDimension) { 0f }
        if (sumExp > 0f) {
            for (i in 0 until storedCount) {
                val weight = expWeights[i] / sumExp
                for (d in 0 until patternDimension) {
                    reconstructed[d] += weight * memoryBank[i][d]
                }
            }
        }

        val prevEnergy = currentEnergy
        // Lyapunov Energy
        currentEnergy = - (1.0f / temperatureBeta) * kotlin.math.ln(max(1e-5f, sumExp)) - maxAffinity
        val energyDelta = currentEnergy - prevEnergy
        val similarity = (maxAffinity + 1.0f) / 2.0f // mapped 0..1
        memoryConvergenceIndex = (memoryConvergenceIndex * 0.8f) + (similarity * 0.2f)
        lastRecalledIndex = bestIndex

        return HopfieldResult(
            energy = currentEnergy,
            energyDelta = energyDelta,
            recalledPatternLabel = patternLabels[bestIndex],
            similarityScore = similarity.coerceIn(0f, 1f),
            reconstructedVector = reconstructed,
            convergenceScore = memoryConvergenceIndex.coerceIn(0f, 1f),
            storedMemoriesCount = storedCount
        )
    }

    /**
     * Ingests a new cognitive pattern consolidated by Gemini or SNN into long-term Hopfield memory
     */
    @Synchronized
    fun storePattern(label: String, vector: FloatArray) {
        val targetIdx = storedCount % maxStoredPatterns
        val normalized = normalizeVector(vector.copyOf(patternDimension))
        System.arraycopy(normalized, 0, memoryBank[targetIdx], 0, patternDimension)
        patternLabels[targetIdx] = label
        if (storedCount < maxStoredPatterns) storedCount++
    }

    private fun normalizeVector(v: FloatArray): FloatArray {
        var mag = 0.0f
        for (x in v) mag += x * x
        mag = sqrt(max(1e-6f, mag))
        return FloatArray(v.size) { i -> v[i] / mag }
    }

    fun getMemoryCount() = storedCount
    fun getStoredLabels() = patternLabels.take(storedCount)
}
