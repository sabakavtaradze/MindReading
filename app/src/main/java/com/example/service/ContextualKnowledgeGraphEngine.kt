package com.example.service

import java.util.Locale

/**
 * Contextual Knowledge Graph & Named Entity Disambiguation Engine
 * Models semantic entity-relation-entity triples: (Subject, Predicate, Object)
 * e.g., (დეველოპერი, წერს, კოდი), (სისტემა, არეგულირებს, ტემპერატურა)
 */
object ContextualKnowledgeGraphEngine {

    data class KnowledgeTriple(
        val subject: String,
        val predicate: String,
        val `object`: String,
        val domain: String,
        val weight: Float = 1.0f
    )

    private val KNOWLEDGE_GRAPH = listOf(
        // Technical / Coding domain
        KnowledgeTriple("პროგრამისტი", "წერს", "კოდს", "DEV", 1.0f),
        KnowledgeTriple("კოდი", "გადის", "კომპილაციას", "DEV", 0.95f),
        KnowledgeTriple("ალგორითმი", "ამუშავებს", "მონაცემებს", "DEV", 0.95f),
        KnowledgeTriple("არქიტექტურა", "მოითხოვს", "ოპტიმიზაციას", "DEV", 0.9f),
        KnowledgeTriple("მონაცემები", "ინახება", "ბაზაში", "DEV", 0.9f),
        KnowledgeTriple("ფუნქცია", "აბრუნებს", "შედეგს", "DEV", 0.9f),
        KnowledgeTriple("ტესტი", "ამოწმებს", "აპლიკაციას", "DEV", 0.95f),
        KnowledgeTriple("სერვერი", "აგზავნის", "პასუხს", "DEV", 0.85f),
        
        // Smart Automation & Hardware domain
        KnowledgeTriple("სენსორი", "ზომავს", "სიგნალს", "AUTOMATION", 0.95f),
        KnowledgeTriple("კამერა", "აფიქსირებს", "მზერას", "AUTOMATION", 0.95f),
        KnowledgeTriple("შუქი", "ირთვება", "ოთახში", "AUTOMATION", 0.9f),
        KnowledgeTriple("ტემპერატურა", "რეგულირდება", "კონტროლერით", "AUTOMATION", 0.85f),

        // Communication & Daily domain
        KnowledgeTriple("შეტყობინება", "ეგზავნება", "მომხმარებელს", "COMM", 0.9f),
        KnowledgeTriple("მადლობა", "დახმარებისთვის", "გუნდს", "COMM", 0.95f),
        KnowledgeTriple("შეხვედრა", "დაიწყება", "დროულად", "COMM", 0.9f),
        KnowledgeTriple("დავალება", "შესრულებულია", "წარმატებით", "COMM", 0.95f)
    )

    /**
     * Query graph relations for a given active subject or object
     */
    fun findRelatedEntities(activeWord: String): List<String> {
        val clean = activeWord.trim().lowercase(Locale.ROOT)
        if (clean.isBlank()) return emptyList()

        val related = mutableListOf<String>()
        KNOWLEDGE_GRAPH.forEach { triple ->
            if (triple.subject.lowercase(Locale.ROOT).contains(clean) || clean.contains(triple.subject.lowercase(Locale.ROOT))) {
                related.add(triple.predicate)
                related.add(triple.`object`)
            } else if (triple.`object`.lowercase(Locale.ROOT).contains(clean) || clean.contains(triple.`object`.lowercase(Locale.ROOT))) {
                related.add(triple.subject)
                related.add(triple.predicate)
            } else if (triple.predicate.lowercase(Locale.ROOT).contains(clean) || clean.contains(triple.predicate.lowercase(Locale.ROOT))) {
                related.add(triple.`object`)
                related.add(triple.subject)
            }
        }
        return related.distinct()
    }

    /**
     * Get knowledge association score for candidate word given the surrounding context
     */
    fun getGraphAssociationBoost(candidate: String, contextTokens: List<String>): Float {
        val candClean = candidate.lowercase(Locale.ROOT)
        var boost = 1.0f

        for (token in contextTokens.takeLast(4)) {
            val tokenClean = token.lowercase(Locale.ROOT)
            val relations = findRelatedEntities(tokenClean)
            if (relations.any { it.lowercase(Locale.ROOT).contains(candClean) || candClean.contains(it.lowercase(Locale.ROOT)) }) {
                boost += 2.2f
                break
            }
        }
        return boost
    }
}
