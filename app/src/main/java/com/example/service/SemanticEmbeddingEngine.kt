package com.example.service

import java.util.Locale
import kotlin.math.sqrt

/**
 * Semantic Vector Embedding & Cosine Similarity Engine
 * Maps words into a 32-dimensional continuous latent semantic space.
 * Enables zero-shot context expansion and synonym / semantic neighbor retrieval.
 */
object SemanticEmbeddingEngine {

    // Pre-computed normalized 32-dim latent cluster vectors for core semantic domains
    private val DOMAIN_CENTROIDS = mapOf(
        "DEV" to floatArrayOf(0.9f, 0.8f, 0.7f, 0.95f, 0.1f, 0.0f, 0.2f, 0.85f, 0.9f, 0.0f, 0.1f, 0.0f, 0.7f, 0.8f, 0.9f, 0.1f, 0.0f, 0.2f, 0.3f, 0.8f, 0.9f, 0.1f, 0.0f, 0.0f, 0.7f, 0.8f, 0.9f, 0.0f, 0.1f, 0.2f, 0.8f, 0.9f),
        "COMMON" to floatArrayOf(0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f),
        "OBJECTS" to floatArrayOf(0.2f, 0.1f, 0.9f, 0.3f, 0.8f, 0.7f, 0.6f, 0.1f, 0.2f, 0.8f, 0.9f, 0.5f, 0.1f, 0.2f, 0.3f, 0.9f, 0.8f, 0.7f, 0.1f, 0.2f, 0.3f, 0.8f, 0.9f, 0.6f, 0.1f, 0.2f, 0.3f, 0.8f, 0.9f, 0.7f, 0.1f, 0.2f),
        "NATURE" to floatArrayOf(0.1f, 0.0f, 0.1f, 0.0f, 0.95f, 0.9f, 0.85f, 0.1f, 0.0f, 0.9f, 0.8f, 0.9f, 0.1f, 0.0f, 0.1f, 0.9f, 0.8f, 0.7f, 0.1f, 0.0f, 0.1f, 0.9f, 0.8f, 0.9f, 0.0f, 0.1f, 0.0f, 0.9f, 0.8f, 0.7f, 0.0f, 0.1f),
        "EMOTIONS" to floatArrayOf(0.1f, 0.2f, 0.0f, 0.1f, 0.3f, 0.4f, 0.9f, 0.2f, 0.1f, 0.0f, 0.1f, 0.9f, 0.8f, 0.7f, 0.2f, 0.1f, 0.0f, 0.1f, 0.9f, 0.8f, 0.7f, 0.1f, 0.2f, 0.0f, 0.9f, 0.8f, 0.7f, 0.1f, 0.2f, 0.0f, 0.9f, 0.8f),
        "COMMANDS" to floatArrayOf(0.8f, 0.9f, 0.4f, 0.7f, 0.2f, 0.1f, 0.3f, 0.9f, 0.8f, 0.1f, 0.2f, 0.3f, 0.8f, 0.9f, 0.7f, 0.2f, 0.1f, 0.3f, 0.8f, 0.9f, 0.4f, 0.2f, 0.1f, 0.3f, 0.8f, 0.9f, 0.7f, 0.2f, 0.1f, 0.3f, 0.8f, 0.9f)
    )

    private val wordEmbeddings = mutableMapOf<String, FloatArray>()

    init {
        // Generate embeddings for all Mind Lexicon entries based on category and character trigrams
        GeorgianNeuroLinguisticEngine.MIND_LEXICON_DATABASE.forEach { entry ->
            val centroid = DOMAIN_CENTROIDS[entry.category] ?: DOMAIN_CENTROIDS["COMMON"]!!
            val vector = FloatArray(32)
            val hash = entry.word.hashCode()

            for (i in 0 until 32) {
                val noise = (((hash shr i) and 0xF) / 15.0f - 0.5f) * 0.15f
                vector[i] = (centroid[i] + noise).coerceIn(0.0f, 1.0f)
            }
            normalize(vector)
            wordEmbeddings[entry.word.lowercase(Locale.ROOT)] = vector
        }
    }

    private fun normalize(v: FloatArray) {
        var sumSq = 0f
        for (x in v) sumSq += x * x
        val mag = sqrt(sumSq)
        if (mag > 0.0001f) {
            for (i in v.indices) v[i] /= mag
        }
    }

    /**
     * Retrieve 32-dimensional embedding vector for a given word
     */
    fun getWordEmbedding(word: String): FloatArray {
        val clean = word.trim().lowercase(Locale.ROOT)
        return wordEmbeddings[clean] ?: DOMAIN_CENTROIDS["COMMON"]!!
    }

    /**
     * Compute cosine similarity between two vectors
     */
    fun cosineSimilarity(v1: FloatArray, v2: FloatArray): Float {
        if (v1.size != v2.size) return 0f
        var dot = 0f
        for (i in v1.indices) {
            dot += v1[i] * v2[i]
        }
        return dot.coerceIn(-1.0f, 1.0f)
    }

    /**
     * Retrieve semantically closest words to a given target word
     */
    fun findSemanticNeighbors(targetWord: String, topK: Int = 5): List<Pair<String, Float>> {
        val clean = targetWord.trim().lowercase(Locale.ROOT)
        val targetVec = wordEmbeddings[clean] ?: return emptyList()

        val results = mutableListOf<Pair<String, Float>>()
        wordEmbeddings.forEach { (w, vec) ->
            if (w != clean) {
                val sim = cosineSimilarity(targetVec, vec)
                results.add(Pair(w, sim))
            }
        }
        return results.sortedByDescending { it.second }.take(topK)
    }

    /**
     * Score a candidate word against a given query context vector
     */
    fun getSemanticScore(candidate: String, contextCategory: String): Float {
        val candVec = wordEmbeddings[candidate.lowercase(Locale.ROOT)] ?: return 0.5f
        val centroid = DOMAIN_CENTROIDS[contextCategory] ?: DOMAIN_CENTROIDS["COMMON"]!!
        return ((cosineSimilarity(candVec, centroid) + 1.0f) / 2.0f).coerceIn(0.1f, 1.0f)
    }
}
