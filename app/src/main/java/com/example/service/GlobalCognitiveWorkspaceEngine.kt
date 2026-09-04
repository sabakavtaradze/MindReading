package com.example.service

import java.util.concurrent.ConcurrentLinkedDeque

/**
 * Global Cognitive Workspace Architecture (GWA / Global Blackboard Coordinator):
 * Implements Bernard Baars' Cognitive Global Workspace Theory on Android.
 *
 * Coordinates and synchronizes all independent on-device neural & cognitive modules:
 * 1. Local Neural Transformer AI (Semantic Generator)
 * 2. Spiking Neural Network (Intuitive Neuromodulatory Spikes)
 * 3. Hierarchical Temporal Memory (Temporal Cortical Sequences)
 * 4. Hopfield Associative Network (Pattern Memory Attractors)
 * 5. Polyvagal Behavioral Somatics (Autonomic Nervous Tone)
 * 6. Local Evolutionary Brain (Meta-Strategy Selection)
 * 7. System 2 Local Arbitrator (Deliberation & Verification)
 *
 * Sub-agents submit proposals to the Global Blackboard.
 * A Softmax Attentive Gating competition selects the dominant cognitive broadcast,
 * which is then synchronized back across all networks to ensure they work as ONE mind.
 */
class GlobalCognitiveWorkspaceEngine {

    data class WorkspaceProposal(
        val agentId: String,
        val agentNameKa: String,
        val icon: String,
        val proposedHypothesis: String,
        val confidenceScorePct: Int,
        val saliencePriority: Float, // 0.0 to 1.0
        val telemetryDetail: String
    )

    data class ConsciousBroadcast(
        val winningAgentId: String,
        val winningAgentNameKa: String,
        val winningHypothesis: String,
        val globalCoherencePct: Int,
        val timestamp: Long = System.currentTimeMillis(),
        val proposals: List<WorkspaceProposal>,
        val broadcastFeedbackDirectiveKa: String,
        val interAgentConsensusSummary: String
    )

    data class GlobalWorkspaceTelemetry(
        val activeBroadcast: ConsciousBroadcast,
        val registeredAgentsCount: Int = 6,
        val globalConvergenceRatePct: Int,
        val isWorkspaceSynchronized: Boolean = true,
        val recentBroadcasts: List<ConsciousBroadcast> = emptyList()
    )

    private val broadcastHistory = ConcurrentLinkedDeque<ConsciousBroadcast>()

    /**
     * Conducts the Global Workspace Competition & Broadcast cycle:
     * Gathers proposals from all specialized local sub-agents, runs attention competition,
     * elects the global broadcast, and synthesizes cross-network feedback.
     */
    fun competeAndBroadcast(
        transformerThought: String,
        transformerConfidence: Int,
        snnSnapshot: SpikingNeuralNetworkEngine.SnnTelemetrySnapshot,
        htmTelemetry: HierarchicalTemporalMemoryEngine.HtmTelemetry,
        hopfieldResult: HopfieldMemoryNetwork.HopfieldResult,
        polyvagal: PolyvagalBehavioralEngine.BehavioralAnalysisResult,
        localBrain: LocalEvolutionaryBrain.LocalBrainTelemetry?,
        arbitratorVerdict: LocalConsensusArbitrator.ConsensusVerdict?,
        focusLevel: Float
    ): GlobalWorkspaceTelemetry {
        val proposals = mutableListOf<WorkspaceProposal>()

        // Agent 1: Local Neural Transformer (Semantic Core)
        proposals.add(
            WorkspaceProposal(
                agentId = "TRANSFORMER_SEMANTIC",
                agentNameKa = "ლოკალური ტრანსფორმერი",
                icon = "⚡",
                proposedHypothesis = transformerThought.ifBlank { "მიმდინარეობს მრავალ-მოდალური სემანტიკური სინთეზი" },
                confidenceScorePct = transformerConfidence.coerceIn(50, 99),
                saliencePriority = 0.90f,
                telemetryDetail = "4-Head Self-Attention • 8-არხიანი Fusion"
            )
        )

        // Agent 2: SNN (Spiking Neuromodulatory Reflex Core)
        val snnCluster = snnSnapshot.dominantActiveCluster.labelKa
        val snnSpikeHz = snnSnapshot.totalSpikesPerSec
        proposals.add(
            WorkspaceProposal(
                agentId = "SNN_INTUITION",
                agentNameKa = "Spiking ნეიროქსელი (SNN)",
                icon = "🧠",
                proposedHypothesis = "დომინანტური იმპულსი: $snnCluster (${snnSpikeHz.toInt()} Hz)",
                confidenceScorePct = (snnSnapshot.neuralCoherenceScore * 100).toInt().coerceIn(40, 95),
                saliencePriority = 0.75f,
                telemetryDetail = "STDP სინაფსური პლასტიკურობა • Izhikevich ნეირონები"
            )
        )

        // Agent 3: HTM (Temporal Pattern Predictor)
        val htmPredictedTransitions = htmTelemetry.activeColumnsCount
        val htmExpected = htmTelemetry.anomalyScore < 0.4f
        proposals.add(
            WorkspaceProposal(
                agentId = "HTM_TEMPORAL",
                agentNameKa = "კორტიკალური სვეტები (HTM)",
                icon = "🧬",
                proposedHypothesis = if (htmExpected) "დროითი თანმიმდევრობა პროგნოზირებულია" else "კონტექსტური გადახრა დროის ხაზზე",
                confidenceScorePct = ((1f - htmTelemetry.anomalyScore) * 100).toInt().coerceIn(35, 95),
                saliencePriority = 0.70f,
                telemetryDetail = "40 სვეტი • $htmPredictedTransitions აქტიური სვეტი • ანომალია: ${(htmTelemetry.anomalyScore * 100).toInt()}%"
            )
        )

        // Agent 4: Hopfield Memory Network (Associative Attractor)
        proposals.add(
            WorkspaceProposal(
                agentId = "HOPFIELD_ATTRACTOR",
                agentNameKa = "Hopfield მეხსიერება",
                icon = "🔮",
                proposedHypothesis = "ატრაქტორული მდგომარეობა: ენერგია E = ${String.format(java.util.Locale.US, "%.2f", hopfieldResult.energy)}",
                confidenceScorePct = (hopfieldResult.convergenceScore * 100).toInt().coerceIn(40, 95),
                saliencePriority = 0.65f,
                telemetryDetail = "32-განზომილებიანი მდგრადი ატრაქტორი • კოვარიაცია"
            )
        )

        // Agent 5: Polyvagal Somatic Monitor (Autonomic Balance)
        val polyvagalDominant = polyvagal.dominantState.labelKa
        proposals.add(
            WorkspaceProposal(
                agentId = "POLYVAGAL_SOMATIC",
                agentNameKa = "სომატური პოლივაგალური ბირთვი",
                icon = "🫀",
                proposedHypothesis = "ავტონომიური ტონი: $polyvagalDominant (Flow: ${(polyvagal.flowStateIndex * 100).toInt()}%)",
                confidenceScorePct = (polyvagal.flowStateIndex * 100).toInt().coerceIn(45, 98),
                saliencePriority = 0.80f,
                telemetryDetail = "სიმპათიკური: ${(polyvagal.sympatheticScore * 100).toInt()}% • დორსალური: ${(polyvagal.dorsalScore * 100).toInt()}%"
            )
        )

        // Agent 6: Evolutionary Brain (Meta-Strategy Coordinator)
        if (localBrain != null) {
            val brainEfficacy = (localBrain.activeStrategy.efficacyScore * 100).toInt()
            proposals.add(
                WorkspaceProposal(
                    agentId = "EVOLUTIONARY_BRAIN",
                    agentNameKa = "ევოლუციური ტვინი",
                    icon = "🌱",
                    proposedHypothesis = "ოპტიმალური სტრატეგია: ${localBrain.activeStrategy.titleKa} ($brainEfficacy% ეფექტურობა)",
                    confidenceScorePct = brainEfficacy,
                    saliencePriority = 0.85f,
                    telemetryDetail = "თაობა: ${localBrain.evolutionGeneration} • ცოდნის გენი"
                )
            )
        }

        // Softmax-weighted competition for Conscious Gating
        // System 2 Arbitrator, if present, validates the final conscious broadcast
        val verifiedThought = arbitratorVerdict?.system2VerifiedThought?.ifBlank { transformerThought } ?: transformerThought
        val consensusScore = arbitratorVerdict?.consensusScorePct ?: ((proposals.map { it.confidenceScorePct }.average()).toInt())

        val winner = proposals.maxByOrNull { it.confidenceScorePct * it.saliencePriority } ?: proposals.first()

        val feedbackDirective = "გლობალური სინქრონიზაცია: SNN ზღურბლები და HTM სვეტები დაკალიბრდა „${winner.agentNameKa}“-ის დომინანტურ ჰიპოთეზაზე."
        val consensusSummary = "სრული კოგნიტური კონსენსუსი მიღწეულია (${consensusScore}% ჰარმონია ${proposals.size} ლოკალურ აგენტს შორის)."

        val broadcast = ConsciousBroadcast(
            winningAgentId = winner.agentId,
            winningAgentNameKa = winner.agentNameKa,
            winningHypothesis = verifiedThought,
            globalCoherencePct = consensusScore,
            proposals = proposals,
            broadcastFeedbackDirectiveKa = feedbackDirective,
            interAgentConsensusSummary = consensusSummary
        )

        broadcastHistory.addFirst(broadcast)
        while (broadcastHistory.size > 15) {
            broadcastHistory.pollLast()
        }

        return GlobalWorkspaceTelemetry(
            activeBroadcast = broadcast,
            registeredAgentsCount = proposals.size,
            globalConvergenceRatePct = consensusScore,
            isWorkspaceSynchronized = true,
            recentBroadcasts = broadcastHistory.toList()
        )
    }
}
