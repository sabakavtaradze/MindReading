package com.example.service

import java.util.Locale
import java.util.concurrent.ConcurrentLinkedDeque
import kotlin.math.sqrt

/**
 * Local Episodic Memory Graph & Associative Retrieval Core:
 * Maintains an on-device multidimensional vector graph of past mental states,
 * user feedback, and context-action associations.
 * Allows instant nearest-neighbor similarity recall (Cos-Sim) without any external servers.
 */
class LocalEpisodicMemoryGraph(private val maxCapacity: Int = 120) {

    data class EpisodicMemoryNode(
        val id: String,
        val timestamp: Long,
        val featureVector: FloatArray, // 16-dimensional normalised biometric/cognitive state
        val decodedThought: String,
        val contextCategory: String,
        val hrBpm: Float,
        val pupilMm: Float,
        val flowIndex: Float,
        val reinforcementWeight: Float, // Higher if verified by user
        val summaryKa: String
    ) {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false
            other as EpisodicMemoryNode
            return id == other.id
        }

        override fun hashCode(): Int = id.hashCode()
    }

    data class MemoryRecallResult(
        val topMatch: EpisodicMemoryNode?,
        val similarityScorePct: Int, // 0..100
        val totalIndexedEpisodes: Int,
        val associativeExplanationKa: String,
        val nearestNeighbors: List<Pair<String, Int>> = emptyList()
    )

    private val episodicBuffer = ConcurrentLinkedDeque<EpisodicMemoryNode>()

    init {
        // Pre-seed with foundational archetypal cognitive states
        seedInitialMemoryNodes()
    }

    private fun seedInitialMemoryNodes() {
        val seeds = listOf(
            Triple(
                "ღრმა ანალიტიკური კონცენტრაცია და ალგორითმული ფიქრი",
                "FOCUSED_CODING",
                floatArrayOf(0.85f, 0.20f, 0.75f, 0.90f, 0.15f, 0.88f, 0.10f, 0.92f, 0.30f, 0.80f, 0.12f, 0.70f, 0.85f, 0.20f, 0.90f, 0.80f)
            ),
            Triple(
                "შემოქმედებითი შთაგონება და ახალი იდეების ასოციაციური ძიება",
                "CREATIVE_EXPLORATION",
                floatArrayOf(0.45f, 0.65f, 0.85f, 0.70f, 0.35f, 0.60f, 0.40f, 0.75f, 0.80f, 0.60f, 0.25f, 0.85f, 0.65f, 0.50f, 0.70f, 0.85f)
            ),
            Triple(
                "კოგნიტური დაღლილობა, ყურადღების დრიფტი და ენერგიის აღდგენის საჭიროება",
                "COGNITIVE_FATIGUE",
                floatArrayOf(0.20f, 0.80f, 0.25f, 0.30f, 0.85f, 0.20f, 0.75f, 0.30f, 0.20f, 0.35f, 0.80f, 0.25f, 0.30f, 0.75f, 0.20f, 0.30f)
            ),
            Triple(
                "სწრაფი ვერბალური დეკოდირება და სუბვოკალური მეტყველების სინთეზი",
                "INNER_SPEECH_STREAM",
                floatArrayOf(0.70f, 0.30f, 0.65f, 0.80f, 0.25f, 0.75f, 0.20f, 0.85f, 0.50f, 0.70f, 0.30f, 0.60f, 0.80f, 0.35f, 0.85f, 0.75f)
            )
        )

        seeds.forEachIndexed { idx, (thought, cat, vec) ->
            episodicBuffer.add(
                EpisodicMemoryNode(
                    id = "seed_$idx",
                    timestamp = System.currentTimeMillis() - (idx * 3600_000L),
                    featureVector = normalizeVector(vec),
                    decodedThought = thought,
                    contextCategory = cat,
                    hrBpm = 72f + (idx * 3f),
                    pupilMm = 3.4f,
                    flowIndex = 0.80f - (idx * 0.15f),
                    reinforcementWeight = 1.0f,
                    summaryKa = "არქეტიპული ეპიზოდი: $cat"
                )
            )
        }
    }

    /**
     * Stores a new episodic experience into memory
     */
    fun recordEpisode(
        thought: String,
        category: String,
        vector: FloatArray,
        hrBpm: Float,
        pupilMm: Float,
        flowIndex: Float,
        reinforcement: Float = 1.0f
    ) {
        if (thought.isBlank()) return

        val normVec = normalizeVector(vector)
        val node = EpisodicMemoryNode(
            id = "ep_${System.currentTimeMillis()}_${(100..999).random()}",
            timestamp = System.currentTimeMillis(),
            featureVector = normVec,
            decodedThought = thought,
            contextCategory = category,
            hrBpm = hrBpm,
            pupilMm = pupilMm,
            flowIndex = flowIndex,
            reinforcementWeight = reinforcement,
            summaryKa = "ეპიზოდი: „${thought.take(30)}...“"
        )

        episodicBuffer.addFirst(node)
        while (episodicBuffer.size > maxCapacity) {
            episodicBuffer.pollLast()
        }
    }

    /**
     * Queries the memory graph for the closest matching historical experience
     */
    fun recallClosestEpisode(currentVector: FloatArray): MemoryRecallResult {
        if (episodicBuffer.isEmpty()) {
            return MemoryRecallResult(
                topMatch = null,
                similarityScorePct = 0,
                totalIndexedEpisodes = 0,
                associativeExplanationKa = "ეპიზოდური მეხსიერების ბუფერი ინიციალიზდება..."
            )
        }

        val normQuery = normalizeVector(currentVector)
        var bestNode: EpisodicMemoryNode? = null
        var bestSim = -1.0f
        val scoredList = mutableListOf<Pair<EpisodicMemoryNode, Float>>()

        for (node in episodicBuffer) {
            val sim = computeCosineSimilarity(normQuery, node.featureVector) * (0.85f + 0.15f * node.reinforcementWeight.coerceIn(0.5f, 1.5f))
            scoredList.add(node to sim)
            if (sim > bestSim) {
                bestSim = sim
                bestNode = node
            }
        }

        val simPct = (bestSim.coerceIn(0f, 1f) * 100).toInt()
        val nearest = scoredList
            .sortedByDescending { it.second }
            .take(3)
            .map { it.first.decodedThought.take(24) + "..." to (it.second.coerceIn(0f, 1f) * 100).toInt() }

        val narrative = if (bestNode != null && simPct >= 65) {
            "გახსენებულია მსგავსი გამოცდილება (${simPct}% თანხვედრა): „${bestNode.decodedThought.take(35)}...“ (${bestNode.contextCategory})."
        } else {
            "ახალი გამოცდილება (თანხვედრა $simPct%): ფორმირდება ახალი ასოციაციური კვანძი."
        }

        return MemoryRecallResult(
            topMatch = bestNode,
            similarityScorePct = simPct,
            totalIndexedEpisodes = episodicBuffer.size,
            associativeExplanationKa = narrative,
            nearestNeighbors = nearest
        )
    }

    private fun normalizeVector(vec: FloatArray): FloatArray {
        var sumSquares = 0.0f
        for (v in vec) {
            sumSquares += v * v
        }
        val mag = sqrt(sumSquares.toDouble()).toFloat()
        if (mag <= 0.00001f) return vec

        val norm = FloatArray(vec.size)
        for (i in vec.indices) {
            norm[i] = vec[i] / mag
        }
        return norm
    }

    private fun computeCosineSimilarity(v1: FloatArray, v2: FloatArray): Float {
        val len = minOf(v1.size, v2.size)
        var dot = 0.0f
        for (i in 0 until len) {
            dot += v1[i] * v2[i]
        }
        return dot.coerceIn(-1.0f, 1.0f)
    }
}
