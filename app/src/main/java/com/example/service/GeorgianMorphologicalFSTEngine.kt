package com.example.service

import java.util.Locale

/**
 * Georgian Morphological Finite State Transducer (FST) Engine
 * Deconstructs and synthesizes polysynthetic Georgian verbs:
 * Preverbs (ზმნისწინები) + Person Markers (პირის ნიშნები) + Root (ფუძე) + Suffixal screeves (ბოლოსართები).
 */
object GeorgianMorphologicalFSTEngine {

    // Preverbs (ზმნისწინები)
    val PREVERBS = listOf("და", "გა", "შე", "მო", "გადა", "ჩა", "ამო", "გან", "წა", "გამო", "მი")

    // Person & Voice Affixes
    val PERSON_PREFIXES = listOf("ვ", "გვ", "მ", "გ", "უ", "ა", "ე", "ი")

    // Screeve suffixes for common verbs
    val SCREEVE_SUFFIXES = listOf("ოთ", "თ", "ს", "ენ", "დით", "დნენ", "ება", "ებული", "ავი", "ელი")

    data class MorphologicalDecomposition(
        val originalWord: String,
        val detectedPreverb: String?,
        val detectedPersonMarker: String?,
        val rootStem: String,
        val detectedSuffix: String?,
        val grammaticalCategory: String,
        val possibleConjugations: List<String>
    )

    /**
     * Deconstruct a Georgian verb or noun into its morphological components
     */
    fun deconstruct(word: String): MorphologicalDecomposition {
        val clean = word.trim().lowercase(Locale.ROOT)
        
        var preverb: String? = null
        var remainder = clean

        // 1. Check for Preverb
        for (pv in PREVERBS.sortedByDescending { it.length }) {
            if (remainder.startsWith(pv) && remainder.length > pv.length + 2) {
                preverb = pv
                remainder = remainder.substring(pv.length)
                break
            }
        }

        // 2. Check for Person marker
        var personMarker: String? = null
        for (pm in PERSON_PREFIXES) {
            if (remainder.startsWith(pm) && remainder.length > pm.length + 2) {
                personMarker = pm
                remainder = remainder.substring(pm.length)
                break
            }
        }

        // 3. Check for Suffix
        var suffix: String? = null
        for (sf in SCREEVE_SUFFIXES.sortedByDescending { it.length }) {
            if (remainder.endsWith(sf) && remainder.length > sf.length + 1) {
                suffix = sf
                remainder = remainder.substring(0, remainder.length - sf.length)
                break
            }
        }

        val root = remainder
        val conjugations = synthesizeConjugations(root, preverb)

        return MorphologicalDecomposition(
            originalWord = word,
            detectedPreverb = preverb,
            detectedPersonMarker = personMarker,
            rootStem = root,
            detectedSuffix = suffix,
            grammaticalCategory = if (preverb != null || suffix != null) "ზმნა (პოლისინთეზური)" else "არსებითი / ზედსართავი სახელი",
            possibleConjugations = conjugations
        )
    }

    /**
     * Generate possible Georgian conjugations from a root and optional preverb
     */
    fun synthesizeConjugations(root: String, basePreverb: String? = null): List<String> {
        val pv = basePreverb ?: "და"
        return listOf(
            "$pv$root" + "ოთ",       // 1st person plural optative: შევამოწმოთ
            "$pv$root" + "ს",        // 3rd person singular present: ამოწმებს
            "$pv$root" + "ებული",    // Past participle: შემოწმებული
            "$pv$root" + "ება",      // Verbal noun: შემოწმება
            "გადა$pv$root" + "ოთ"    // Iterative preverb: გადავამოწმოთ
        )
    }

    /**
     * Predict the complete verb if the user only typed/thought the preverb and first radical
     */
    fun predictFromAffixes(partialAffix: String): List<String> {
        val clean = partialAffix.trim().lowercase(Locale.ROOT)
        return GeorgianNeuroLinguisticEngine.MIND_LEXICON_DATABASE
            .filter { it.word.lowercase(Locale.ROOT).startsWith(clean) }
            .take(6)
            .map { it.word }
    }
}
