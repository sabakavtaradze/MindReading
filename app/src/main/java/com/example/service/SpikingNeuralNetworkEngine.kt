package com.example.service

import com.example.sensor.RealAudioState
import com.example.sensor.RealCameraGazeState
import com.example.sensor.RealHardwareSensorState
import kotlin.math.abs
import kotlin.math.exp
import kotlin.math.max
import kotlin.math.min
import kotlin.random.Random

/**
 * High-Performance Biologically-Inspired Spiking Neural Network (SNN) Engine
 * with Leaky Integrate-and-Fire (LIF) Neurons, Hebbian Spike-Timing-Dependent
 * Plasticity (STDP), Liquid Reservoir Dynamics, and Bidirectional AI Neuromodulation.
 */
class SpikingNeuralNetworkEngine {

    companion object {
        const val NUM_CLUSTERS = 5
        const val NEURONS_PER_CLUSTER = 16
        const val TOTAL_NEURONS = NUM_CLUSTERS * NEURONS_PER_CLUSTER // 80 neurons
        const val V_REST = -70.0f // mV
        const val V_RESET = -75.0f // mV
        const val BASE_THRESHOLD = -50.0f // mV
        const val TAU_MEMBRANE = 20.0f // ms
        const val REFRACTORY_PERIOD_MS = 3.0f
    }

    enum class CorticalCluster(val id: Int, val labelKa: String, val description: String) {
        FRONTAL_EXECUTIVE(0, "ფრონტალური ლოგიკა", "აღმასრულებელი დაგეგმვა & ბაიესური გადაწყვეტილებები"),
        TEMPORAL_ACOUSTIC(1, "ტემპორალური აკუსტიკა", "სუბვოკალური სიხშირეები & VAD მეტყველება"),
        PARIETAL_GAZE(2, "პარიეტალური მზერა", "ვიზუალური ფიქსაცია, გუგა & rPPG პულსი"),
        LIMBIC_POLYVAGAL(3, "ლიმბური პოლივაგალური", "ემოციური ენტროპია & ვაგალური ტონუსი"),
        SOMATOSENSORY_MOTOR(4, "სომატომოტორული ბადე", "მიკრო-ტრემორი & ნეირომოტორული სტაბილურობა")
    }

    data class SpikingNeuron(
        val id: Int,
        val cluster: CorticalCluster,
        var membranePotential: Float = V_REST,
        var threshold: Float = BASE_THRESHOLD,
        var lastSpikeTimeMs: Long = 0L,
        var totalSpikeCount: Long = 0L,
        var isRefractory: Boolean = false,
        var recentSpikeRateHz: Float = 0f
    )

    data class SynapticConnection(
        val preNeuronId: Int,
        val postNeuronId: Int,
        var weight: Float, // 0.0f .. 1.0f
        var lastLtpTimeMs: Long = 0L
    )

    data class NeuromodulationState(
        var dopamineLevel: Float = 1.0f, // 0.2 - 2.0 (Plasticity / LTP Booster)
        var serotoninLevel: Float = 1.0f, // 0.2 - 2.0 (Stability & Inhibitory Control)
        var noradrenalineLevel: Float = 1.0f, // 0.2 - 2.0 (Sensory Sensitivity / Threshold Shifter)
        var targetedClusterBoost: CorticalCluster = CorticalCluster.FRONTAL_EXECUTIVE,
        var synapticPruningRate: Float = 0.01f,
        var lastSyncTimestamp: Long = System.currentTimeMillis(),
        var aiFeedbackStatus: String = "ორმხრივი კავშირი ინიციალიზებულია (Gemini ↔ SNN)",
        var closedLoopCycles: Long = 0L
    )

    data class SnnTelemetrySnapshot(
        val totalSpikesPerSec: Float,
        val averageMembranePotential: Float,
        val dominantActiveCluster: CorticalCluster,
        val clusterSpikeFrequencies: Map<CorticalCluster, Float>,
        val meanSynapticWeight: Float,
        val stdpPlasticityRateDelta: Float,
        val neuralCoherenceScore: Float,
        val activeSpikingNeuronsCount: Int,
        val neuromodulation: NeuromodulationState,
        val recentSpikeEvents: List<SpikeEventRecord>
    )

    data class SpikeEventRecord(
        val timestamp: Long,
        val neuronId: Int,
        val cluster: CorticalCluster,
        val spikeIntensity: Float
    )

    // Internal State
    private val neurons = Array(TOTAL_NEURONS) { index ->
        val clusterIndex = index / NEURONS_PER_CLUSTER
        val cluster = CorticalCluster.values()[clusterIndex]
        SpikingNeuron(
            id = index,
            cluster = cluster,
            membranePotential = V_REST + Random.nextFloat() * 5.0f
        )
    }

    // Sparse Synaptic Matrix (each neuron connects to ~12 random target neurons)
    private val synapses = mutableListOf<SynapticConnection>()

    private val neuromodulation = NeuromodulationState()
    private val recentSpikeHistory = mutableListOf<SpikeEventRecord>()
    private var lastStepTimeMs = System.currentTimeMillis()
    private var plasticityDeltaAccumulator = 0f
    private var totalSpikesWindow = 0

    init {
        // Initialize Structured Sparse Recurrent Topology
        for (pre in 0 until TOTAL_NEURONS) {
            val preCluster = pre / NEURONS_PER_CLUSTER
            // Intra-cluster dense connections (local recurrent micro-circuits)
            for (offset in 1..8) {
                val post = (preCluster * NEURONS_PER_CLUSTER) + ((pre + offset) % NEURONS_PER_CLUSTER)
                if (pre != post) {
                    synapses.add(SynapticConnection(pre, post, weight = 0.3f + Random.nextFloat() * 0.4f))
                }
            }
            // Inter-cluster sparse long-range projections (connecting executive to sensory/limbic)
            for (targetCluster in 0 until NUM_CLUSTERS) {
                if (targetCluster != preCluster && Random.nextFloat() < 0.35f) {
                    val targetNeuron = targetCluster * NEURONS_PER_CLUSTER + Random.nextInt(NEURONS_PER_CLUSTER)
                    synapses.add(SynapticConnection(pre, targetNeuron, weight = 0.2f + Random.nextFloat() * 0.3f))
                }
            }
        }
    }

    /**
     * Primary Step: Ingests real sensors, updates membrane potentials via LIF equations,
     * fires spikes, applies Hebbian STDP, and produces an SNN Telemetry Snapshot.
     */
    @Synchronized
    fun step(
        audio: RealAudioState,
        gaze: RealCameraGazeState,
        sensors: RealHardwareSensorState,
        polyvagalFlowIndex: Float,
        emotionalEntropy: Float,
        cognitiveFocus: Float
    ): SnnTelemetrySnapshot {
        val currentTimeMs = System.currentTimeMillis()
        val dtMs = max(1.0f, min(100.0f, (currentTimeMs - lastStepTimeMs).toFloat()))
        lastStepTimeMs = currentTimeMs

        // 1. Calculate Injected Currents for each Cortical Cluster from Real Sensors
        val clusterCurrents = FloatArray(NUM_CLUSTERS)

        // Cluster 0: Frontal Executive (Driven by Focus, Bayesian Stability, Low Entropy)
        clusterCurrents[0] = (cognitiveFocus * 25.0f) + ((1.0f - emotionalEntropy) * 15.0f)
        if (neuromodulation.targetedClusterBoost == CorticalCluster.FRONTAL_EXECUTIVE) {
            clusterCurrents[0] += 12.0f * neuromodulation.dopamineLevel
        }

        // Cluster 1: Temporal Acoustic (Driven by SPL Decibels, Dominant Frequency, VAD)
        val audioNorm = (audio.decibels / 90.0f).coerceIn(0f, 1.5f)
        val freqNorm = (audio.dominantFrequencyHz / 2000.0f).coerceIn(0f, 1.5f)
        val vadBoost = if (audio.voiceActivityDetected) 18.0f else 4.0f
        clusterCurrents[1] = (audioNorm * 18.0f) + (freqNorm * 8.0f) + vadBoost

        // Cluster 2: Parietal Gaze (Driven by Fixation Duration, Pupil Dilation, rPPG Pulse)
        val fixationNorm = (gaze.fixationDurationMs / 1500.0f).coerceIn(0f, 2.0f)
        val pupilNorm = (gaze.opticalPupilDiameterMm / 5.0f).coerceIn(0.5f, 1.8f)
        val pulseNorm = (gaze.opticalRadiancePulseBpm / 100.0f).coerceIn(0.5f, 1.5f)
        clusterCurrents[2] = (fixationNorm * 16.0f) + (pupilNorm * 12.0f) + (pulseNorm * 8.0f)

        // Cluster 3: Limbic Polyvagal (Driven by Polyvagal Flow, Emotional Entropy, Heart Rate)
        val flowNorm = polyvagalFlowIndex.coerceIn(0f, 1f)
        val entropyNorm = emotionalEntropy.coerceIn(0f, 1f)
        clusterCurrents[3] = (flowNorm * 20.0f) + (entropyNorm * 22.0f * (2.2f - neuromodulation.serotoninLevel))

        // Cluster 4: Somatosensory Motor (Driven by 3-Axis Accel, Gyroscope & Micro-Tremor)
        val tremorNorm = (sensors.microTremorMagnitude * 100.0f).coerceIn(0f, 2.5f)
        val gyroNorm = (abs(sensors.gyroX) + abs(sensors.gyroY) + abs(sensors.gyroZ)).coerceIn(0f, 3.0f)
        clusterCurrents[4] = (tremorNorm * 15.0f) + (gyroNorm * 10.0f) + 5.0f

        // Apply Noradrenaline Modulation: sharpens sensory responsiveness
        for (i in 0 until NUM_CLUSTERS) {
            clusterCurrents[i] *= neuromodulation.noradrenalineLevel
        }

        // 2. LIF Membrane Integration and Spiking Phase
        var currentStepSpikes = 0
        var totalMembranePotential = 0.0f
        val clusterSpikeCounts = IntArray(NUM_CLUSTERS)
        val newlyFiredNeuronIds = mutableListOf<Int>()

        // Effective Membrane Time Constant modulated by Serotonin (Serotonin enhances stability)
        val effectiveTau = TAU_MEMBRANE * neuromodulation.serotoninLevel.coerceIn(0.5f, 2.0f)
        val decayFactor = exp(-dtMs / effectiveTau)

        for (neuron in neurons) {
            val clusterIdx = neuron.cluster.id
            val injectedCurrent = clusterCurrents[clusterIdx] + (Random.nextFloat() * 2.0f - 1.0f)

            // Check refractory period
            if (currentTimeMs - neuron.lastSpikeTimeMs < REFRACTORY_PERIOD_MS) {
                neuron.isRefractory = true
                neuron.membranePotential = V_RESET
            } else {
                neuron.isRefractory = false
                // Leaky Integration: V(t+dt) = V_rest + (V(t) - V_rest)*decay + I*R
                val deltaV = (neuron.membranePotential - V_REST) * decayFactor
                neuron.membranePotential = V_REST + deltaV + (injectedCurrent * 0.45f)
            }

            // Spike Condition
            if (!neuron.isRefractory && neuron.membranePotential >= neuron.threshold) {
                // FIRE SPIKE!
                neuron.membranePotential = V_RESET
                neuron.lastSpikeTimeMs = currentTimeMs
                neuron.totalSpikeCount++
                currentStepSpikes++
                clusterSpikeCounts[clusterIdx]++
                newlyFiredNeuronIds.add(neuron.id)

                // Dynamic Threshold Adaptation: threshold jumps on spike, then decays
                neuron.threshold = BASE_THRESHOLD + 4.0f

                // Record spike event
                recentSpikeHistory.add(
                    SpikeEventRecord(
                        timestamp = currentTimeMs,
                        neuronId = neuron.id,
                        cluster = neuron.cluster,
                        spikeIntensity = (injectedCurrent / 30.0f).coerceIn(0.5f, 1.0f)
                    )
                )
            } else {
                // Threshold decay back to baseline
                neuron.threshold += (BASE_THRESHOLD - neuron.threshold) * 0.08f
            }

            totalMembranePotential += neuron.membranePotential
        }

        // Keep spike history window bounded
        while (recentSpikeHistory.size > 60) {
            recentSpikeHistory.removeAt(0)
        }

        // 3. Synaptic Propagation & STDP (Spike-Timing-Dependent Plasticity)
        // Dopamine acts as neuromodulatory reward multiplier for LTP
        val ltpRate = 0.04f * neuromodulation.dopamineLevel
        val ltdRate = 0.025f / neuromodulation.serotoninLevel.coerceIn(0.5f, 2.0f)
        var stepPlasticityDelta = 0.0f

        for (synapse in synapses) {
            val preNeuron = neurons[synapse.preNeuronId]
            val postNeuron = neurons[synapse.postNeuronId]

            // If pre-neuron fired, transmit post-synaptic current
            if (newlyFiredNeuronIds.contains(synapse.preNeuronId)) {
                postNeuron.membranePotential += synapse.weight * 3.5f
            }

            // STDP Learning Rule
            if (newlyFiredNeuronIds.contains(synapse.postNeuronId)) {
                val timeDiff = synapse.lastLtpTimeMs - preNeuron.lastSpikeTimeMs
                if (preNeuron.lastSpikeTimeMs > 0 && currentTimeMs - preNeuron.lastSpikeTimeMs < 40) {
                    // Pre fired right before Post -> Long-Term Potentiation (LTP)
                    val delta = ltpRate * exp(-(currentTimeMs - preNeuron.lastSpikeTimeMs) / 15.0f)
                    synapse.weight = (synapse.weight + delta).coerceIn(0.05f, 1.0f)
                    synapse.lastLtpTimeMs = currentTimeMs
                    stepPlasticityDelta += delta
                } else if (currentTimeMs - preNeuron.lastSpikeTimeMs > 60) {
                    // Post fired without Pre -> Long-Term Depression (LTD)
                    val delta = ltdRate * 0.5f
                    synapse.weight = (synapse.weight - delta).coerceIn(0.05f, 1.0f)
                    stepPlasticityDelta -= delta
                }
            }

            // Synaptic Pruning: slight baseline decay
            if (neuromodulation.synapticPruningRate > 0f) {
                synapse.weight = max(0.05f, synapse.weight - (neuromodulation.synapticPruningRate * 0.001f))
            }
        }

        plasticityDeltaAccumulator = (plasticityDeltaAccumulator * 0.85f) + (stepPlasticityDelta * 0.15f)
        totalSpikesWindow = (totalSpikesWindow * 0.8f + currentStepSpikes * 0.2f).toInt()

        // 4. Compute Metrics
        val avgMembrane = totalMembranePotential / TOTAL_NEURONS
        val meanSynapticWeight = synapses.map { it.weight }.average().toFloat()

        var dominantCluster = CorticalCluster.FRONTAL_EXECUTIVE
        var maxSpikes = -1
        val clusterFreqs = mutableMapOf<CorticalCluster, Float>()

        for (cluster in CorticalCluster.values()) {
            val count = clusterSpikeCounts[cluster.id]
            val freqHz = (count.toFloat() / (dtMs / 1000f)).coerceAtLeast(0f)
            clusterFreqs[cluster] = freqHz
            if (count > maxSpikes) {
                maxSpikes = count
                dominantCluster = cluster
            }
        }

        val totalSpikesPerSec = (currentStepSpikes.toFloat() / (dtMs / 1000f)).coerceAtLeast(0f)
        val coherence = ((meanSynapticWeight * 0.6f) + (1.0f - emotionalEntropy) * 0.4f).coerceIn(0f, 1f)

        return SnnTelemetrySnapshot(
            totalSpikesPerSec = totalSpikesPerSec,
            averageMembranePotential = avgMembrane,
            dominantActiveCluster = dominantCluster,
            clusterSpikeFrequencies = clusterFreqs,
            meanSynapticWeight = meanSynapticWeight,
            stdpPlasticityRateDelta = plasticityDeltaAccumulator,
            neuralCoherenceScore = coherence,
            activeSpikingNeuronsCount = newlyFiredNeuronIds.size,
            neuromodulation = neuromodulation.copy(),
            recentSpikeEvents = recentSpikeHistory.toList()
        )
    }

    /**
     * Bidirectional Downlink: Applies Gemini AI Neuromodulatory Feedback directly
     * to the SNN. The Cloud AI tunes on-device dopamine, serotonin, noradrenaline,
     * target cluster excitation, and synaptic pruning rates.
     */
    @Synchronized
    fun applyAiNeuromodulationFeedback(
        dopamine: Float,
        serotonin: Float,
        noradrenaline: Float,
        targetClusterName: String,
        pruningRate: Float,
        aiExplanation: String
    ) {
        val targetCluster = when (targetClusterName.uppercase()) {
            "TEMPORAL_ACOUSTIC" -> CorticalCluster.TEMPORAL_ACOUSTIC
            "PARIETAL_GAZE" -> CorticalCluster.PARIETAL_GAZE
            "LIMBIC_POLYVAGAL" -> CorticalCluster.LIMBIC_POLYVAGAL
            "SOMATOSENSORY_MOTOR" -> CorticalCluster.SOMATOSENSORY_MOTOR
            else -> CorticalCluster.FRONTAL_EXECUTIVE
        }

        neuromodulation.dopamineLevel = dopamine.coerceIn(0.2f, 2.0f)
        neuromodulation.serotoninLevel = serotonin.coerceIn(0.2f, 2.0f)
        neuromodulation.noradrenalineLevel = noradrenaline.coerceIn(0.2f, 2.0f)
        neuromodulation.targetedClusterBoost = targetCluster
        neuromodulation.synapticPruningRate = pruningRate.coerceIn(0.001f, 0.05f)
        neuromodulation.lastSyncTimestamp = System.currentTimeMillis()
        neuromodulation.aiFeedbackStatus = aiExplanation
        neuromodulation.closedLoopCycles++
    }

    fun getLatestNeuromodulationState(): NeuromodulationState = neuromodulation.copy()
}
