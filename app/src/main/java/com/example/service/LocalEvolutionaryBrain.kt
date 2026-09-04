package com.example.service

import com.example.sensor.RealAudioState
import com.example.sensor.RealCameraGazeState
import com.example.sensor.RealHardwareSensorState
import java.util.concurrent.ConcurrentHashMap
import kotlin.math.abs
import kotlin.math.roundToInt
import kotlin.random.Random

/**
 * On-Device Self-Evolving Cognitive Brain (ლოკალური ევოლუციური ტვინი).
 * Continuously evaluates cognitive state, organizes algorithmic execution plans,
 * directs external AI (Gemini) with accumulated experience, and refines strategies
 * via continuous reinforcement feedback & synaptic plasticity.
 */
class LocalEvolutionaryBrain {

    data class CognitiveStrategy(
        val id: String,
        val titleKa: String,
        val descriptionKa: String,
        val domain: String, // "REASONING", "ATTENTION", "PACING", "MEMORY", "CREATIVITY"
        var usageCount: Int = 0,
        var successCount: Int = 0,
        var efficacyScore: Float = 0.75f, // 0.0 .. 1.0
        var synapticWeight: Float = 0.50f, // 0.0 .. 1.0
        var lastEvolutionDelta: Float = 0f
    )

    data class StrategyRank(
        val id: String,
        val titleKa: String,
        val efficacyPct: Int,
        val usageCount: Int,
        val synapticWeightPct: Int
    )

    data class LocalBrainTelemetry(
        val activeStrategy: CognitiveStrategy,
        val evolutionGeneration: Int,
        val experiencePoints: Long,
        val currentRewardDelta: Float,
        val adaptationStatusKa: String,
        val algorithmicPlanSteps: List<String>,
        val topStrategiesRanked: List<StrategyRank>,
        val guidanceForGemini: String,
        val isEvolving: Boolean = true
    )

    private val strategies = ConcurrentHashMap<String, CognitiveStrategy>()

    private var evolutionGeneration: Int = 1
    private var experiencePoints: Long = 120L
    private var lastActiveStrategyId: String = "ANALYTIC_DECOMPOSITION"

    private var previousFocus: Float = 0.7f
    private var previousEntropy: Float = 0.2f
    private var previousFatigue: Float = 0.3f

    init {
        initDefaultStrategies()
    }

    private fun initDefaultStrategies() {
        registerStrategy(
            CognitiveStrategy(
                id = "ANALYTIC_DECOMPOSITION",
                titleKa = "ალგორითმული დაშლა და ეტაპობრივი ამოხსნა",
                descriptionKa = "კომპლექსური გამოწვევის დაყოფა მცირე, გადაჭრად ლოგიკურ ქვესაფეხურებად",
                domain = "REASONING",
                efficacyScore = 0.88f,
                synapticWeight = 0.85f
            )
        )
        registerStrategy(
            CognitiveStrategy(
                id = "FLOW_ACCELERATION",
                titleKa = "ღრმა Flow-ს კონცენტრაცია და ხმაურის ფილტრაცია",
                descriptionKa = "მენტალური ფოკუსის მაქსიმიზაცია და გარე გამღიზიანებლების იგნორირება",
                domain = "ATTENTION",
                efficacyScore = 0.84f,
                synapticWeight = 0.80f
            )
        )
        registerStrategy(
            CognitiveStrategy(
                id = "SENSORY_PACING_CALM",
                titleKa = "პარასიმპათიკური რეგულაცია და მენტალური განტვირთვა",
                descriptionKa = "სტრესის, ტრემორის და გულისცემის დამშვიდება ნელი რიტმის მეშვეობით",
                domain = "PACING",
                efficacyScore = 0.81f,
                synapticWeight = 0.74f
            )
        )
        registerStrategy(
            CognitiveStrategy(
                id = "SUBVOCAL_LEXICON_EXPANSION",
                titleKa = "სუბვოკალური ენობრივი ფორმულირება & ლექსიკის გამდიდრება",
                descriptionKa = "გაუაზრებელი აზრების ქართულ სინტაქსურ წინადადებად ჩამოყალიბება",
                domain = "CREATIVITY",
                efficacyScore = 0.86f,
                synapticWeight = 0.78f
            )
        )
        registerStrategy(
            CognitiveStrategy(
                id = "ANOMALY_RESYNCHRONIZATION",
                titleKa = "HTM ანომალიების გადალახვა და მეხსიერების რეორგანიზაცია",
                descriptionKa = "მოულოდნელი სენსორული ცვლილებების ადაპტირება და ნეირონული სვეტების გადაწყობა",
                domain = "MEMORY",
                efficacyScore = 0.79f,
                synapticWeight = 0.70f
            )
        )
        registerStrategy(
            CognitiveStrategy(
                id = "ASSOCIATIVE_INTUITION",
                titleKa = "Hopfield-ის ასოციაციური მეხსიერების ნახტომი",
                descriptionKa = "წარსული მსგავსი გამოცდილების ამოტივტივება და გადაწყვეტილების დაჩქარება",
                domain = "MEMORY",
                efficacyScore = 0.83f,
                synapticWeight = 0.76f
            )
        )
    }

    private fun registerStrategy(strategy: CognitiveStrategy) {
        strategies[strategy.id] = strategy
    }

    /**
     * Executes one complete Evolutionary Feedback & Learning Epoch:
     * 1. Evaluates reward of the PREVIOUS strategy based on biometric deltas.
     * 2. Updates synaptic plasticity & efficacy scores (Reinforcement STDP).
     * 3. Selects the BEST or most adaptive strategy for the CURRENT context.
     * 4. Algorithmically organizes the pipeline for Gemini and On-Device components.
     */
    @Synchronized
    fun evaluateAndEvolve(
        audio: RealAudioState,
        gaze: RealCameraGazeState,
        sensors: RealHardwareSensorState,
        polyvagal: PolyvagalBehavioralEngine.BehavioralAnalysisResult,
        ecosystem: MultiNeuralNetworkEcosystem.UnifiedNeuralEcosystemTelemetry,
        focusLevel: Float,
        emotionalEntropy: Float,
        mentalFatigue: Float,
        activeThought: String
    ): LocalBrainTelemetry {

        // 1. Calculate Reward Delta from previous cycle
        val deltaFocus = focusLevel - previousFocus
        val deltaEntropy = previousEntropy - emotionalEntropy // Positive if entropy decreased
        val deltaFatigue = previousFatigue - mentalFatigue    // Positive if fatigue decreased

        // Multi-objective Biometric Reward
        val reward = (deltaFocus * 0.50f + deltaEntropy * 0.35f + deltaFatigue * 0.15f).coerceIn(-1f, 1f)

        // 2. Reinforce or Penalize the previously active strategy
        val previousStrategy = strategies[lastActiveStrategyId]
        var adaptationMsg = "სტაბილური ნეირონული მდგომარეობა"

        if (previousStrategy != null) {
            previousStrategy.usageCount++
            previousStrategy.lastEvolutionDelta = reward

            if (reward > 0.05f) {
                // Success: Dopaminergic Reinforcement
                previousStrategy.successCount++
                previousStrategy.efficacyScore = (previousStrategy.efficacyScore * 0.88f + 0.12f * 0.98f).coerceIn(0.1f, 0.99f)
                previousStrategy.synapticWeight = (previousStrategy.synapticWeight + 0.03f).coerceIn(0.1f, 1.0f)
                experiencePoints += (reward * 60 + 15).toLong()
                adaptationMsg = "✨ სტრატეგია „${previousStrategy.titleKa}“ განმტკიცდა (+${(reward * 100).roundToInt()}% Reward)"
            } else if (reward < -0.05f) {
                // Decay / Adaptation Trigger
                previousStrategy.efficacyScore = (previousStrategy.efficacyScore * 0.92f + 0.08f * 0.50f).coerceIn(0.1f, 0.99f)
                previousStrategy.synapticWeight = (previousStrategy.synapticWeight - 0.02f).coerceIn(0.1f, 1.0f)
                experiencePoints += 5L
                adaptationMsg = "⚡ სტრატეგია დაკორექტირდა უკეთესი მიდგომის მოსაძებნად"
            } else {
                experiencePoints += 8L
            }
        }

        // Advance evolution generation every 200 XP
        val newGeneration = (experiencePoints / 200).toInt() + 1
        if (newGeneration > evolutionGeneration) {
            evolutionGeneration = newGeneration
            adaptationMsg = "🚀 ევოლუციის ახალი თაობა (#$evolutionGeneration)! ლოკალურმა ტვინმა მიდგომა დახვეწა."
        }

        // Save current metrics for next cycle delta
        previousFocus = focusLevel
        previousEntropy = emotionalEntropy
        previousFatigue = mentalFatigue

        // 3. Contextual Strategy Selection (Context + Efficacy + Epsilon-Greedy exploration)
        val selectedStrategyId = when {
            mentalFatigue > 0.65f || polyvagal.dorsalScore > 0.55f -> "SENSORY_PACING_CALM"
            ecosystem.htmTelemetry.anomalyScore > 0.40f -> "ANOMALY_RESYNCHRONIZATION"
            focusLevel > 0.75f && mentalFatigue < 0.45f -> "FLOW_ACCELERATION"
            audio.voiceActivityDetected || audio.decibels > 48f -> "SUBVOCAL_LEXICON_EXPANSION"
            ecosystem.hopfieldTelemetry.convergenceScore > 0.8f -> "ASSOCIATIVE_INTUITION"
            else -> {
                // Epsilon-greedy exploration (15% chance to explore other strategies)
                if (Random.nextFloat() < 0.15f) {
                    strategies.keys.toList().random()
                } else {
                    // Exploit highest efficacy strategy
                    strategies.values.maxByOrNull { it.efficacyScore }?.id ?: "ANALYTIC_DECOMPOSITION"
                }
            }
        }

        lastActiveStrategyId = selectedStrategyId
        val activeStrategy = strategies[selectedStrategyId] ?: strategies.values.first()

        // 4. Algorithmically organize execution steps for the current state
        val algorithmicPlan = listOf(
            "1. სენსორული ტელემეტრია: dB=${audio.decibels.roundToInt()}, BPM=${gaze.opticalRadiancePulseBpm}, ფოკუსი=${(focusLevel * 100).roundToInt()}%",
            "2. ლოკალური ტვინის არჩევანი: „${activeStrategy.titleKa}“ (ეფექტურობა: ${(activeStrategy.efficacyScore * 100).roundToInt()}%, თაობა: #$evolutionGeneration)",
            "3. ევოლუციური სინთეზი: Gemini AI-ს ინსტრუქტირება ლოკალური გამოცდილების გათვალისწინებით",
            "4. დახურული წრედი: STDP სინაფსური პლასტიკურობის განმტკიცება და Hopfield ატრაქტორში ჩაწერა"
        )

        // 5. Construct Guidance for Gemini AI
        val guidanceForGemini = buildString {
            append("ლოკალური ტვინის (თაობა #$evolutionGeneration, XP=$experiencePoints) დირექტივა:\n")
            append("• არჩეული ევოლუციური სტრატეგია: „${activeStrategy.titleKa}“ (${activeStrategy.descriptionKa})\n")
            append("• სტრატეგიის დაგროვილი ეფექტურობა: ${(activeStrategy.efficacyScore * 100).roundToInt()}%\n")
            append("• ბიომეტრიული უკუკავშირი: Reward=${String.format("%.2f", reward)}, ფოკუსი=${(focusLevel * 100).roundToInt()}%\n")
            append("გთხოვ, ამოცანის გადაწყვეტისას მკაცრად დაეყრდნო ამ სტრატეგიას და დაეხმარო ლოკალურ ტვინს აზროვნების დახვეწაში.")
        }

        // 6. Ranked Top Strategies
        val ranked = strategies.values
            .sortedByDescending { it.efficacyScore }
            .map {
                StrategyRank(
                    id = it.id,
                    titleKa = it.titleKa,
                    efficacyPct = (it.efficacyScore * 100).roundToInt(),
                    usageCount = it.usageCount,
                    synapticWeightPct = (it.synapticWeight * 100).roundToInt()
                )
            }

        return LocalBrainTelemetry(
            activeStrategy = activeStrategy,
            evolutionGeneration = evolutionGeneration,
            experiencePoints = experiencePoints,
            currentRewardDelta = reward,
            adaptationStatusKa = adaptationMsg,
            algorithmicPlanSteps = algorithmicPlan,
            topStrategiesRanked = ranked,
            guidanceForGemini = guidanceForGemini,
            isEvolving = true
        )
    }

    fun getGeneration(): Int = evolutionGeneration
    fun getExperiencePoints(): Long = experiencePoints
}
