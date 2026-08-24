package com.example.service

import java.util.Locale
import kotlin.math.min

/**
 * Georgian Trie & Fuzzy Phonetic Distance Matcher
 * Fast sub-millisecond prefix searches and Levenshtein/Soundex phonetic error tolerance.
 */
object GeorgianTrieFuzzyMatcher {

    class TrieNode {
        val children = mutableMapOf<Char, TrieNode>()
        var isEndOfWord: Boolean = false
        var word: String? = null
        var category: String? = null
        var frequency: Int = 0
    }

    private val root = TrieNode()

    // Phonetic similarity matrix for Georgian consonants (homorganic / articulatory proximity)
    private val PHONETIC_SIMILARITY_GROUPS = listOf(
        setOf('კ', 'ქ', 'ყ', 'გ'),       // Velar / Glottal / Ejective
        setOf('ტ', 'თ', 'დ', 'წ', 'ც'),  // Dental / Alveolar / Affricates
        setOf('პ', 'ფ', 'ბ', 'მ'),       // Labial / Bilabial
        setOf('ჭ', 'ჩ', 'ჯ', 'შ', 'ჟ'),  // Palato-alveolar / Sibilant
        setOf('ს', 'ზ', 'ც', 'ძ', 'წ'),  // Alveolar sibilants
        setOf('ხ', 'ღ', 'ჰ')             // Fricatives / Laryngeal
    )

    init {
        // Populate Trie from Mind Lexicon Database
        GeorgianNeuroLinguisticEngine.MIND_LEXICON_DATABASE.forEach { entry ->
            insert(entry.word, entry.category)
        }
    }

    fun insert(word: String, category: String = "COMMON") {
        val clean = word.trim().lowercase(Locale.ROOT)
        if (clean.isBlank()) return

        var curr = root
        for (ch in clean) {
            curr = curr.children.getOrPut(ch) { TrieNode() }
        }
        curr.isEndOfWord = true
        curr.word = word
        curr.category = category
        curr.frequency += 1
    }

    /**
     * Search words starting with the given prefix
     */
    fun searchPrefix(prefix: String, limit: Int = 10): List<String> {
        val clean = prefix.trim().lowercase(Locale.ROOT)
        if (clean.isBlank()) return emptyList()

        var curr = root
        for (ch in clean) {
            curr = curr.children[ch] ?: return fuzzySearch(clean, maxDistance = 2, limit = limit)
        }

        val results = mutableListOf<Pair<String, Int>>()
        collectAll(curr, results)
        return results.sortedByDescending { it.second }.take(limit).map { it.first }
    }

    private fun collectAll(node: TrieNode, results: MutableList<Pair<String, Int>>) {
        if (node.isEndOfWord && node.word != null) {
            results.add(Pair(node.word!!, node.frequency))
        }
        for (child in node.children.values) {
            collectAll(child, results)
        }
    }

    /**
     * Damerau-Levenshtein distance with Georgian phonetic cost reduction
     */
    fun fuzzySearch(query: String, maxDistance: Int = 2, limit: Int = 8): List<String> {
        val cleanQuery = query.trim().lowercase(Locale.ROOT)
        if (cleanQuery.isBlank()) return emptyList()

        val scoredWords = mutableListOf<Pair<String, Float>>()

        GeorgianNeuroLinguisticEngine.MIND_LEXICON_DATABASE.forEach { entry ->
            val target = entry.word.lowercase(Locale.ROOT)
            val dist = computePhoneticDistance(cleanQuery, target)
            if (dist <= maxDistance + 0.5f) {
                scoredWords.add(Pair(entry.word, dist))
            }
        }

        return scoredWords.sortedBy { it.second }.take(limit).map { it.first }
    }

    fun computePhoneticDistance(s1: String, s2: String): Float {
        val len1 = s1.length
        val len2 = s2.length
        val dp = Array(len1 + 1) { FloatArray(len2 + 1) }

        for (i in 0..len1) dp[i][0] = i.toFloat()
        for (j in 0..len2) dp[0][j] = j.toFloat()

        for (i in 1..len1) {
            val c1 = s1[i - 1]
            for (j in 1..len2) {
                val c2 = s2[j - 1]
                val subCost = if (c1 == c2) {
                    0.0f
                } else if (arePhoneticallySimilar(c1, c2)) {
                    0.35f // Low penalty for similar sound (e.g. ქ vs კ)
                } else {
                    1.0f
                }

                dp[i][j] = min(
                    min(dp[i - 1][j] + 1.0f, dp[i][j - 1] + 1.0f),
                    dp[i - 1][j - 1] + subCost
                )
            }
        }
        return dp[len1][len2]
    }

    private fun arePhoneticallySimilar(c1: Char, c2: Char): Boolean {
        return PHONETIC_SIMILARITY_GROUPS.any { group -> group.contains(c1) && group.contains(c2) }
    }
}
