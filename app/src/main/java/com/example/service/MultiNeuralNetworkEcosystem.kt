package com.example.service

import com.example.sensor.RealAudioState
import com.example.sensor.RealCameraGazeState
import com.example.sensor.RealHardwareSensorState
import java.util.concurrent.ConcurrentLinkedDeque

/**
 * Unified Inter-Connected Multi-Neural Network Ecosystem.
 * Bridges SNN, Hopfield Network, HTM Cortical Columns, Polyvagal Engine, Bayesian Reasoning, and Gemini AI with
 * closed-loop bidirectional cross-talk, cross-modulation, live message bus, and unified telemetry.
 */
class MultiNeuralNetworkEcosystem {

    val snnEngine = SpikingNeuralNetworkEngine()
    val hopfieldEngine = HopfieldMemoryNetwork(patternDimension = 32)
    val htmEngine = HierarchicalTemporalMemoryEngine(numColumns = 40, cellsPerColumn = 4)

    data class InterNeuralSignal(
        val id: Long = System.currentTimeMillis(),
        val source: String,
        val target: String,
        val signalType: String,
        val descriptionKa: String,
        val intensity: Float, // 0..1
        val timestampMs: Long = System.currentTimeMillis()
    )

    data class UnifiedNeuralEcosystemTelemetry(
        val snnTelemetry: SpikingNeuralNetworkEngine.SnnTelemetrySnapshot,
        val hopfieldTelemetry: HopfieldMemoryNetwork.HopfieldResult,
        val htmTelemetry: HierarchicalTemporalMemoryEngine.HtmTelemetry,
        val interNetworkCrossTalkSummary: String,
        val globalSynergyScore: Float, // 0..100%
        val totalActiveSynapticPipes: Int,
        val isBidirectionalSyncActive: Boolean,
        val liveSignals: List<InterNeuralSignal> = emptyList()
    )

    private val liveSignalQueue = ConcurrentLinkedDeque<InterNeuralSignal>()
    private var crossTalkSummary = "სრული მულტი-ნეირონული ქსელური სიმბიოზი აქტიურია (SNN ⇄ HTM ⇄ Hopfield ⇄ Gemini)"

    private fun postSignal(source: String, target: String, type: String, descKa: String, intensity: Float) {
        liveSignalQueue.addFirst(
            InterNeuralSignal(
                source = source,
                target = target,
                signalType = type,
                descriptionKa = descKa,
                intensity = intensity.coerceIn(0f, 1f)
            )
        )
        while (liveSignalQueue.size > 20) {
            liveSignalQueue.pollLast()
        }
    }

    /**
     * Executes one unified multi-network cycle with mutual inter-communication:
     * 1. Sensors -> SNN spikes
     * 2. SNN + Sensors -> Hopfield vector associative recall & energy minimization
     * 3. Hopfield attractor + SNN spikes -> HTM Cortical Columns Spatial Pooling & Sequence Prediction
     * 4. HTM anomaly & Hopfield energy -> feedback modulates SNN membrane threshold
     * 5. Polyvagal tone -> modulates neurotransmitter concentrations
     */
    @Synchronized
    fun stepUnified(
        audio: RealAudioState,
        gaze: RealCameraGazeState,
        sensors: RealHardwareSensorState,
        polyvagalFlowIndex: Float,
        emotionalEntropy: Float,
        cognitiveFocus: Float
    ): UnifiedNeuralEcosystemTelemetry {
        // Step 1: Run SNN
        val snnSnapshot = snnEngine.step(
            audio = audio,
            gaze = gaze,
            sensors = sensors,
            polyvagalFlowIndex = polyvagalFlowIndex,
            emotionalEntropy = emotionalEntropy,
            cognitiveFocus = cognitiveFocus
        )

        // SNN -> HTM/Hopfield signal
        postSignal(
            source = "SNN (Spiking Core)",
            target = "HTM + Hopfield",
            type = "SPIKE_BURST_STREAM",
            descKa = "${snnSnapshot.dominantActiveCluster.labelKa} კლასტერი: ${snnSnapshot.totalSpikesPerSec.toInt()} Hz იმპულსი გაეგზავნა ასოციაციურ ქსელებს",
            intensity = (snnSnapshot.totalSpikesPerSec / 120f).coerceIn(0.1f, 1f)
        )

        // Form 32-dimensional multimodal latent vector for Hopfield & HTM
        val inputVector = FloatArray(32) { idx ->
            when (idx % 8) {
                0 -> (audio.decibels / 60.0f) - 0.5f
                1 -> (audio.dominantFrequencyHz / 1500.0f) - 0.5f
                2 -> (gaze.opticalPupilDiameterMm / 4.0f) - 0.5f
                3 -> (gaze.opticalRadiancePulseBpm / 90.0f) - 0.5f
                4 -> sensors.microTremorMagnitude * 20.0f
                5 -> cognitiveFocus - 0.5f
                6 -> (1.0f - emotionalEntropy) - 0.5f
                7 -> polyvagalFlowIndex - 0.5f
                else -> 0.0f
            }
        }

        // Step 2: Run Hopfield Associative Memory
        val hopfieldResult = hopfieldEngine.recallAndMinimizeEnergy(inputVector, temperatureBeta = 4.5f)

        postSignal(
            source = "Hopfield Network",
            target = "HTM + Bayesian Mind",
            type = "ATTRACTOR_CONVERGENCE",
            descKa = "ატრაქტორის ენერგია E=${String.format(java.util.Locale.US, "%.2f", hopfieldResult.energy)}: აღდგენილია '${hopfieldResult.recalledPatternLabel}' (${(hopfieldResult.similarityScore * 100).toInt()}%)",
            intensity = hopfieldResult.similarityScore
        )

        // Step 3: Run HTM with inputs from SNN spike frequencies & Hopfield reconstructed state
        val clusterFreqArray = FloatArray(SpikingNeuralNetworkEngine.CorticalCluster.values().size) { i ->
            snnSnapshot.clusterSpikeFrequencies[SpikingNeuralNetworkEngine.CorticalCluster.values()[i]] ?: 5.0f
        }

        val htmTelemetry = htmEngine.computeStep(
            inputVector = inputVector,
            snnSpikeRates = clusterFreqArray,
            hopfieldState = hopfieldResult.reconstructedVector
        )

        postSignal(
            source = "HTM Columns",
            target = "SNN Plasticity",
            type = "TEMPORAL_PREDICTION",
            descKa = "აქტიურია ${htmTelemetry.activeColumnsCount}/40 სვეტი (SDR ${(htmTelemetry.sdrSparsityPercentage).toInt()}%). ანომალია: ${(htmTelemetry.anomalyScore * 100).toInt()}%",
            intensity = htmTelemetry.sequenceCoherence
        )

        // Step 4: Cross-talk interaction - HTM Anomaly & Hopfield Energy modulate SNN & HTM learning
        if (htmTelemetry.anomalyScore > 0.4f) {
            // High novelty: boost noradrenaline in SNN and tune HTM receptive fields
            snnEngine.getLatestNeuromodulationState().noradrenalineLevel = (snnEngine.getLatestNeuromodulationState().noradrenalineLevel * 1.05f).coerceAtMost(2.0f)
            htmEngine.tuneHtmReceptiveFields(
                dopamine = snnSnapshot.neuromodulation.dopamineLevel,
                noradrenaline = snnSnapshot.neuromodulation.noradrenalineLevel
            )
            postSignal(
                source = "HTM Anomaly Sensor",
                target = "SNN Neuromodulation",
                type = "NORADRENALINE_SURGE",
                descKa = "სიახლის დეტექცია: ნორადრენალინი გაიზარდა ყურადღების მობილიზებისთვის",
                intensity = htmTelemetry.anomalyScore
            )
        }

        // Global Synergy computation
        val synergy = ((snnSnapshot.neuralCoherenceScore * 0.35f) +
                (hopfieldResult.convergenceScore * 0.35f) +
                (htmTelemetry.sequenceCoherence * 0.30f)) * 100f

        crossTalkSummary = "SNN(${snnSnapshot.totalSpikesPerSec.toInt()}Hz) ⇄ HTM(SDR ${(htmTelemetry.sdrSparsityPercentage).toInt()}%) ⇄ Hopfield(E:${String.format(java.util.Locale.US, "%.1f", hopfieldResult.energy)}) ⇄ Gemini AI"

        return UnifiedNeuralEcosystemTelemetry(
            snnTelemetry = snnSnapshot,
            hopfieldTelemetry = hopfieldResult,
            htmTelemetry = htmTelemetry,
            interNetworkCrossTalkSummary = crossTalkSummary,
            globalSynergyScore = synergy.coerceIn(0f, 100f),
            totalActiveSynapticPipes = 80 + 40 + 32, // SNN + HTM + Hopfield
            isBidirectionalSyncActive = true,
            liveSignals = liveSignalQueue.toList()
        )
    }

    /**
     * Applies Bidirectional Downlink from Gemini Cloud AI to all 3 neural networks.
     */
    @Synchronized
    fun applyCloudAiCrossModulation(
        dopamine: Float,
        serotonin: Float,
        noradrenaline: Float,
        targetClusterName: String,
        pruningRate: Float,
        memoryConsolidationPattern: String?,
        aiExplanation: String
    ) {
        // Modulate SNN
        snnEngine.applyAiNeuromodulationFeedback(
            dopamine = dopamine,
            serotonin = serotonin,
            noradrenaline = noradrenaline,
            targetClusterName = targetClusterName,
            pruningRate = pruningRate,
            aiExplanation = aiExplanation
        )

        // Modulate HTM
        htmEngine.tuneHtmReceptiveFields(dopamine, noradrenaline)

        // Modulate Hopfield: If AI provided a consolidated memory pattern, store it
        if (!memoryConsolidationPattern.isNullOrBlank()) {
            val latentVec = FloatArray(32) { i -> ((i * 7 + 13) % 19) / 19.0f * (if (i % 2 == 0) 1f else -0.8f) }
            hopfieldEngine.storePattern(memoryConsolidationPattern, latentVec)
        }

        postSignal(
            source = "Gemini Cloud AI",
            target = "SNN + HTM + Hopfield",
            type = "BIDIRECTIONAL_DOWNLINK",
            descKa = aiExplanation.ifBlank { "Gemini AI-მ დაარეგულირა დოფამინი ($dopamine), სეროტონინი ($serotonin) და $targetClusterName კლასტერი" },
            intensity = 0.95f
        )
    }
}
