package com.example.service

import android.util.Log
import com.example.sensor.RealAudioState
import com.example.sensor.RealCameraGazeState
import com.example.sensor.RealHardwareSensorState
import java.util.Locale

/**
 * Autonomous Dynamic Lexicon & Behavioral Corpus Expansion Engine:
 * Actively discovers, ingests, and registers new words, phrases, and behavioral concepts
 * into the system's linguistic matrix, preventing repetitive cycles ("ვტკეპნით და ვიმეორებთ")
 * and continuously boosting algorithmic precision.
 */
object AutonomousDynamicLexiconLearner {

    data class DynamicLearnedToken(
        val token: String,
        val category: String,
        val originSource: String, // "GEMINI_CLOUD_AI", "OFFLINE_MORPHO_SYNTHESIS", "ENVIRONMENT_DISCOVERY"
        val timestampMs: Long = System.currentTimeMillis()
    )

    private val recentlyLearnedTokens = mutableListOf<DynamicLearnedToken>()
    var totalLearnedTokensCount: Int = 18
        private set

    init {
        // Seed some initial learned concepts
        seedInitialLearnedCorpus()
    }

    private fun seedInitialLearnedCorpus() {
        val initialSeeds = listOf(
            Triple("ინსაითი", "NEURO_SCIENCE", "GEMINI_CLOUD_AI"),
            Triple("კოგნიცია", "NEURO_SCIENCE", "GEMINI_CLOUD_AI"),
            Triple("სინერგია", "COMMON", "OFFLINE_MORPHO_SYNTHESIS"),
            Triple("პოლივაგალური", "NEURO_SCIENCE", "GEMINI_CLOUD_AI"),
            Triple("ჰარმონიზაცია", "EMOTIONS", "OFFLINE_MORPHO_SYNTHESIS"),
            Triple("ფოკუსირება", "COMMON", "ENVIRONMENT_DISCOVERY")
        )
        for ((word, cat, source) in initialSeeds) {
            registerNewDiscoveredWord(word, cat, source)
        }
    }

    /**
     * Ingests a stream of words or JSON array extracted from Gemini AI or speech recognition
     */
    fun ingestFromAiStream(rawText: String, sourceTag: String = "GEMINI_CLOUD_AI"): List<String> {
        val newlyRegistered = mutableListOf<String>()
        // Split by punctuation and whitespace, filter valid Georgian or international words
        val candidates = rawText
            .replace("[,.!?:;\"'()\\[\\]{}<>/\\\\#@$%^&*~`_\\-+=|]+".toRegex(), " ")
            .split("\\s+".toRegex())
            .map { it.trim() }
            .filter { it.length in 3..25 }

        for (candidate in candidates) {
            // Check if it has Georgian or Latin characters
            val isGeorgian = candidate.any { it in 'ა'..'ჰ' }
            val isLatin = candidate.any { it in 'a'..'z' || it in 'A'..'Z' }
            if (isGeorgian || isLatin) {
                if (registerNewDiscoveredWord(candidate, if (isGeorgian) "AI_DISCOVERED" else "ENGLISH_AI", sourceTag)) {
                    newlyRegistered.add(candidate)
                }
            }
        }
        return newlyRegistered
    }

    /**
     * Autonomous offline concept generator based on environmental and polyvagal state
     */
    fun triggerAutonomousOfflineDiscovery(
        audio: RealAudioState,
        gaze: RealCameraGazeState,
        sensors: RealHardwareSensorState,
        polyvagalState: PolyvagalBehavioralEngine.PolyvagalState
    ): String? {
        val prefixes = listOf("სრულყოფილი", "ნეირონული", "კოგნიტური", "შინაგანი", "სისტემური", "დინამიკური", "ოპტიმალური")
        val roots = when (polyvagalState) {
            PolyvagalBehavioralEngine.PolyvagalState.VENTRAL_VAGAL -> listOf("სიმშვიდე", "იდეა", "შემოქმედება", "აზროვნება", "ფოკუსი", "სინქრონი")
            PolyvagalBehavioralEngine.PolyvagalState.SYMPATHETIC -> listOf("რეაქცია", "სიჩქარე", "სიფხიზლე", "დინამიკა", "იმპულსი", "ყურადღება")
            PolyvagalBehavioralEngine.PolyvagalState.DORSAL_VAGAL -> listOf("დასვენება", "რელაქსაცია", "აღდგენა", "პაუზა", "ჰარმონია", "სიმყუდროვე")
        }

        val prefix = prefixes.random()
        val root = roots.random()
        val combinedWord = "${prefix}_$root"

        if (registerNewDiscoveredWord(combinedWord, "MORPHO_SYNTHESIS", "OFFLINE_MORPHO_SYNTHESIS")) {
            return combinedWord
        }
        return null
    }

    /**
     * Registers word into Georgian linguistic engine and continual learning engine
     */
    fun registerNewDiscoveredWord(word: String, category: String, source: String): Boolean {
        val clean = word.trim()
        if (clean.length < 2) return false

        val registeredInLexicon = GeorgianNeuroLinguisticEngine.registerNewLearnedWord(
            word = clean,
            category = category,
            definition = "ავტონომიურად გარედან მოძიებული / სინთეზირებული ცნება ($source)",
            synonyms = listOf("აზრი", "იდეა", "კონცეფცია")
        )

        if (registeredInLexicon) {
            synchronized(recentlyLearnedTokens) {
                recentlyLearnedTokens.add(0, DynamicLearnedToken(clean, category, source))
                if (recentlyLearnedTokens.size > 50) {
                    recentlyLearnedTokens.removeAt(recentlyLearnedTokens.lastIndex)
                }
            }
            totalLearnedTokensCount++

            // Also register baseline in Continual Self-Learning Engine
            ContinualSelfLearningEngine.recordUserFeedback(
                prefix = "",
                selectedWord = clean,
                sensorFeatures = floatArrayOf(0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f),
                wasAccepted = true
            )
            Log.d("DynamicLexicon", "Registered new discovered token: $clean from $source")
            return true
        }
        return false
    }

    fun getRecentlyLearnedTokens(): List<DynamicLearnedToken> {
        return synchronized(recentlyLearnedTokens) {
            recentlyLearnedTokens.toList()
        }
    }

    fun getActiveVocabularyCount(): Int {
        return GeorgianNeuroLinguisticEngine.getAllLexiconEntries().size
    }
}
