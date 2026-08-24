package com.example.service

import java.util.Locale

/**
 * Contextual Knowledge Graph & Named Entity Ontological Disambiguator
 * 100% On-Device, Free, Zero-Cost semantic triples: Subject ➔ Relation ➔ Object
 * Multiplies candidate priors based on ontological affinity in Georgian.
 */
object GeorgianKnowledgeGraphEngine {

    data class KnowledgeTriple(
        val subject: String,
        val relation: String,
        val `object`: String,
        val weight: Float = 1.0f
    )

    private val TRIPLES = listOf(
        KnowledgeTriple("პროგრამისტი", "წერს", "კოდს", 1.8f),
        KnowledgeTriple("პროგრამისტი", "ტესტავს", "აპლიკაციას", 1.9f),
        KnowledgeTriple("პროგრამისტი", "ასწორებს", "შეცდომას", 1.85f),
        KnowledgeTriple("ალგორითმი", "ამუშავებს", "მონაცემებს", 1.75f),
        KnowledgeTriple("სისტემა", "რეკავს", "განგაშს", 1.6f),
        KnowledgeTriple("მომხმარებელი", "იყენებს", "ინტერფეისს", 1.5f),
        KnowledgeTriple("ექიმი", "ამოწმებს", "პაციენტს", 1.8f),
        KnowledgeTriple("სენსორი", "ზომავს", "ტემპერატურას", 1.9f),
        KnowledgeTriple("კამერა", "აფიქსირებს", "მზერას", 1.95f),
        KnowledgeTriple("ხელოვნური ინტელექტი", "პროგნოზირებს", "სიტყვას", 2.0f)
    )

    data class OntologicalAffinityResult(
        val activeEntities: List<String>,
        val suggestedConcepts: List<String>,
        val graphConnectionStrength: Float
    )

    /**
     * Given context words, retrieve related entities & actions from the local Knowledge Graph
     */
    fun queryKnowledgeGraph(contextPhrase: String): OntologicalAffinityResult {
        val words = contextPhrase.trim().lowercase(Locale.ROOT).split("\\s+".toRegex())
        val foundEntities = mutableSetOf<String>()
        val suggestions = mutableListOf<Pair<String, Float>>()

        for (triple in TRIPLES) {
            val sub = triple.subject.lowercase(Locale.ROOT)
            val rel = triple.relation.lowercase(Locale.ROOT)
            val obj = triple.`object`.lowercase(Locale.ROOT)

            if (words.any { it.contains(sub) || sub.contains(it) }) {
                foundEntities.add(triple.subject)
                suggestions.add(Pair(triple.relation, triple.weight))
                suggestions.add(Pair(triple.`object`, triple.weight * 0.9f))
            } else if (words.any { it.contains(rel) || rel.contains(it) }) {
                foundEntities.add(triple.relation)
                suggestions.add(Pair(triple.`object`, triple.weight))
            }
        }

        val topSuggested = suggestions
            .distinctBy { it.first }
            .sortedByDescending { it.second }
            .take(5)
            .map { it.first }

        val connectionStrength = if (topSuggested.isNotEmpty()) 0.92f else 0.35f

        return OntologicalAffinityResult(
            activeEntities = foundEntities.toList(),
            suggestedConcepts = topSuggested,
            graphConnectionStrength = connectionStrength
        )
    }

    /**
     * Multiplier score for a candidate word if connected via the Knowledge Graph
     */
    fun getGraphAffinityMultiplier(candidateWord: String, activePhrase: String): Float {
        val cleanCandidate = candidateWord.trim().lowercase(Locale.ROOT)
        val affinity = queryKnowledgeGraph(activePhrase)
        return if (affinity.suggestedConcepts.any { it.lowercase(Locale.ROOT).contains(cleanCandidate) || cleanCandidate.contains(it.lowercase(Locale.ROOT)) }) {
            2.2f
        } else {
            1.0f
        }
    }
}
