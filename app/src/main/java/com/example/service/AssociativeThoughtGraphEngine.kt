package com.example.service

import kotlin.random.Random

data class AssociativeThoughtCandidate(
    val thoughtText: String,
    val associativeStrengthPct: Float,
    val semanticHopDistance: Int,
    val reasoningCategory: String
)

data class AssociativeLink(
    val fromNode: String,
    val toNode: String,
    val weight: Float
)

data class AssociativeThoughtGraphMetrics(
    val activeSeedConcept: String = "კოდირება და ალგორითმი",
    val hopfieldEnergy: Float = -12.4f, // Lower energy = more stable mental attractor state
    val associativeDriftVelocity: Float = 1.35f,
    val upcomingAssociativePredictions: List<AssociativeThoughtCandidate> = emptyList(),
    val activeNeuralLinks: List<AssociativeLink> = emptyList(),
    val associativeInsightGeorgian: String = "ტვინი ასოციაციურად გადადის ლოგიკური ანალიზიდან ოპტიმიზაციის ფაზაზე."
)

class AssociativeThoughtGraphEngine {

    private val seedConcepts = listOf(
        "პროგრამირება & კოდი" to listOf(
            AssociativeThoughtCandidate("ბაგის აღმოჩენა და გასწორება", 96.5f, 1, "Direct Cause-Effect"),
            AssociativeThoughtCandidate("ტესტირების გაშვება", 92.0f, 1, "Procedural Sequence"),
            AssociativeThoughtCandidate("არქიტექტურული რეფაქტორინგი", 88.4f, 2, "High-Level Optimization"),
            AssociativeThoughtCandidate("ყავის შესვენება", 84.1f, 2, "Biological Homeostasis")
        ),
        "კომუნიკაცია & შეტყობინება" to listOf(
            AssociativeThoughtCandidate("სწრაფი პასუხის გაგზავნა", 98.2f, 1, "Direct Action"),
            AssociativeThoughtCandidate("საკონტაქტო პირის მოძიება", 91.5f, 1, "Information Retrieval"),
            AssociativeThoughtCandidate("შეხვედრის დაგეგმვა", 87.0f, 2, "Temporal Planning"),
            AssociativeThoughtCandidate("დოკუმენტის გაზიარება", 83.2f, 2, "Context Sharing")
        ),
        "კრეატიული იდეა & დიზაინი" to listOf(
            AssociativeThoughtCandidate("ვიზუალური ინტერფეისის ესკიზი", 95.8f, 1, "Visual Synthesis"),
            AssociativeThoughtCandidate("ფერთა პალიტრის შერჩევა", 90.4f, 1, "Aesthetic Evaluation"),
            AssociativeThoughtCandidate("მომხმარებლის გამოცდილების გაუმჯობესება", 89.1f, 2, "Empathy Mapping"),
            AssociativeThoughtCandidate("პროტოტიპის შექმნა", 86.5f, 2, "Implementation")
        ),
        "გადაღლა & განტვირთვა" to listOf(
            AssociativeThoughtCandidate("თვალების დასვენება და სუნთქვა", 97.4f, 1, "Vagal Recovery"),
            AssociativeThoughtCandidate("ყავის ან წყლის მიღება", 93.8f, 1, "Hydration Drive"),
            AssociativeThoughtCandidate("სამუშაო გარემოს შეცვლა", 85.6f, 2, "Sensory Refresh"),
            AssociativeThoughtCandidate("დღის შეჯამება", 82.0f, 3, "Metacognitive Close")
        )
    )

    private var currentSeedIndex = 0

    fun computeAssociativeGraph(
        dominantThought: String = "კოდირება",
        stressLevelPct: Int = 35,
        cognitiveEnergy: Float = 80f
    ): AssociativeThoughtGraphMetrics {
        val (seed, candidates) = seedConcepts[currentSeedIndex % seedConcepts.size]

        val energy = -10f - (cognitiveEnergy * 0.15f) + (stressLevelPct * 0.05f)
        val drift = 0.8f + (stressLevelPct * 0.015f) + Random.nextFloat() * 0.3f

        val links = listOf(
            AssociativeLink(seed, candidates[0].thoughtText, candidates[0].associativeStrengthPct / 100f),
            AssociativeLink(seed, candidates[1].thoughtText, candidates[1].associativeStrengthPct / 100f),
            AssociativeLink(candidates[0].thoughtText, candidates[2].thoughtText, candidates[2].associativeStrengthPct / 100f),
            AssociativeLink(candidates[1].thoughtText, candidates[3].thoughtText, candidates[3].associativeStrengthPct / 100f)
        )

        val insight = "აქტიური აზრი: „$seed“. ჰოპფილდის ატრაქტორი სტაბილურია ($energy eV). წინასწარ ნავარაუდებია: „${candidates[0].thoughtText}“."

        return AssociativeThoughtGraphMetrics(
            activeSeedConcept = seed,
            hopfieldEnergy = energy,
            associativeDriftVelocity = drift,
            upcomingAssociativePredictions = candidates,
            activeNeuralLinks = links,
            associativeInsightGeorgian = insight
        )
    }

    fun stepNextAssociativeConcept(): AssociativeThoughtGraphMetrics {
        currentSeedIndex++
        return computeAssociativeGraph()
    }
}
