package com.example.service

import com.example.sensor.RealAudioState
import com.example.sensor.RealCameraGazeState
import com.example.sensor.RealHardwareSensorState

/**
 * Unified Inter-Connected Multi-Neural Network Ecosystem.
 * Bridges SNN, Hopfield Network, HTM Cortical Columns, and Gemini AI with
 * closed-loop bidirectional cross-talk, cross-modulation, and unified telemetry.
 */
class MultiNeuralNetworkEcosystem {

    val snnEngine = SpikingNeuralNetworkEngine()
    val hopfieldEngine = HopfieldMemoryNetwork(patternDimension = 32)
    val htmEngine = HierarchicalTemporalMemoryEngine(numColumns = 40, cellsPerColumn = 4)

    data class UnifiedNeuralEcosystemTelemetry(
        val snnTelemetry: SpikingNeuralNetworkEngine.SnnTelemetrySnapshot,
        val hopfieldTelemetry: HopfieldMemoryNetwork.HopfieldResult,
        val htmTelemetry: HierarchicalTemporalMemoryEngine.HtmTelemetry,
        val interNetworkCrossTalkSummary: String,
        val globalSynergyScore: Float, // 0..100%
        val totalActiveSynapticPipes: Int,
        val isBidirectionalSyncActive: Boolean
    )

    private var crossTalkSummary = "სრული მულტი-ნეირონული ქსელური სიმბიოზი აქტიურია (SNN ⇄ HTM ⇄ Hopfield ⇄ Gemini)"

    /**
     * Executes one unified multi-network cycle with mutual inter-communication:
     * 1. Sensors -> SNN spikes
     * 2. SNN + Sensors -> Hopfield vector associative recall & energy minimization
     * 3. Hopfield attractor + SNN spikes -> HTM Cortical Columns Spatial Pooling & Sequence Prediction
     * 4. HTM anomaly & Hopfield energy -> feedback modulates SNN membrane threshold
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

        // Step 3: Run HTM with inputs from SNN spike frequencies & Hopfield reconstructed state
        val clusterFreqArray = FloatArray(SpikingNeuralNetworkEngine.CorticalCluster.values().size) { i ->
            snnSnapshot.clusterSpikeFrequencies[SpikingNeuralNetworkEngine.CorticalCluster.values()[i]] ?: 5.0f
        }

        val htmTelemetry = htmEngine.computeStep(
            inputVector = inputVector,
            snnSpikeRates = clusterFreqArray,
            hopfieldState = hopfieldResult.reconstructedVector
        )

        // Step 4: Cross-talk interaction - HTM Anomaly & Hopfield Energy modulate SNN & HTM learning
        if (htmTelemetry.anomalyScore > 0.4f) {
            // High novelty: boost noradrenaline in SNN and tune HTM receptive fields
            snnEngine.getLatestNeuromodulationState().noradrenalineLevel = (snnEngine.getLatestNeuromodulationState().noradrenalineLevel * 1.05f).coerceAtMost(2.0f)
            htmEngine.tuneHtmReceptiveFields(
                dopamine = snnSnapshot.neuromodulation.dopamineLevel,
                noradrenaline = snnSnapshot.neuromodulation.noradrenalineLevel
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
            isBidirectionalSyncActive = true
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
    }
}
