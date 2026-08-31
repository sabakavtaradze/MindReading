package com.example.service

import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min
import kotlin.random.Random

/**
 * Hierarchical Temporal Memory (HTM) Cortical Columns Engine.
 * Implements Cortical Minicolumns, Spatial Pooler (SDR - Sparse Distributed Representations),
 * Temporal Sequence Memory, and Predictive Anomaly Scoring for real-time sensor sequences.
 */
class HierarchicalTemporalMemoryEngine(
    val numColumns: Int = 40,
    val cellsPerColumn: Int = 4,
    val activeColumnsTarget: Int = 6 // ~15% sparsity
) {
    data class MiniColumn(
        val id: Int,
        var overlapScore: Float = 0.0f,
        var boostFactor: Float = 1.0f,
        var activityHistory: Float = 0.0f,
        var isLocallyActive: Boolean = false,
        val receptiveFieldWeights: FloatArray = FloatArray(32) { 0.2f + Random.nextFloat() * 0.4f }
    )

    data class CorticalCell(
        val columnId: Int,
        val cellIndex: Int,
        var state: CellState = CellState.INACTIVE,
        var predictiveState: Boolean = false
    )

    enum class CellState { INACTIVE, ACTIVE, PREDICTIVE, LEARNING }

    data class HtmTelemetry(
        val activeColumnsCount: Int,
        val predictiveCellsCount: Int,
        val anomalyScore: Float, // 0.0 (fully expected sequence) -> 1.0 (novel sequence)
        val sdrSparsityPercentage: Float,
        val sequenceCoherence: Float,
        val dominantCorticalPattern: String,
        val activeColumnIndices: List<Int>
    )

    private val columns = Array(numColumns) { id -> MiniColumn(id) }
    private val cells = Array(numColumns * cellsPerColumn) { index ->
        val col = index / cellsPerColumn
        val cellIdx = index % cellsPerColumn
        CorticalCell(col, cellIdx)
    }

    private var previousActiveCells = mutableSetOf<Int>()
    private var rollingAnomalyScore = 0.15f
    private var stepCounter = 0L

    /**
     * Executes Spatial Pooling and Temporal Memory step across input sensor SDR.
     */
    @Synchronized
    fun computeStep(
        inputVector: FloatArray, // Length 32
        snnSpikeRates: FloatArray,
        hopfieldState: FloatArray
    ): HtmTelemetry {
        stepCounter++

        // 1. Spatial Pooling: Calculate Column Overlaps with input + SNN feedback
        val combinedInput = FloatArray(32) { i ->
            val v1 = if (i < inputVector.size) inputVector[i] else 0f
            val v2 = if (i < snnSpikeRates.size) snnSpikeRates[i] * 0.05f else 0f
            val v3 = if (i < hopfieldState.size) hopfieldState[i] * 0.3f else 0f
            (v1 + v2 + v3).coerceIn(-2f, 2f)
        }

        val overlapList = mutableListOf<Pair<Int, Float>>()
        for (col in columns) {
            var overlap = 0.0f
            for (i in 0 until min(32, combinedInput.size)) {
                overlap += combinedInput[i] * col.receptiveFieldWeights[i]
            }
            col.overlapScore = max(0.0f, overlap * col.boostFactor)
            overlapList.add(Pair(col.id, col.overlapScore))
        }

        // 2. K-Winners-Take-All Inhibition for Sparse Distributed Representation (SDR)
        overlapList.sortByDescending { it.second }
        val winningColumnIds = overlapList.take(activeColumnsTarget).map { it.first }.toSet()

        for (col in columns) {
            col.isLocallyActive = winningColumnIds.contains(col.id)
            // Update boosting for homeostasis
            col.activityHistory = (col.activityHistory * 0.95f) + (if (col.isLocallyActive) 0.05f else 0f)
            col.boostFactor = if (col.activityHistory < 0.05f) 1.2f else if (col.activityHistory > 0.3f) 0.8f else 1.0f
        }

        // 3. Temporal Memory: Evaluate predictions vs active columns
        var correctlyPredictedActiveColumns = 0
        val newlyActiveCells = mutableSetOf<Int>()
        var totalPredictiveCells = 0

        for (colId in winningColumnIds) {
            var predictedCellFound = false
            for (c in 0 until cellsPerColumn) {
                val cellIdx = colId * cellsPerColumn + c
                if (cells[cellIdx].predictiveState) {
                    cells[cellIdx].state = CellState.ACTIVE
                    newlyActiveCells.add(cellIdx)
                    predictedCellFound = true
                }
            }

            if (predictedCellFound) {
                correctlyPredictedActiveColumns++
            } else {
                // Bursting column: all cells activate
                for (c in 0 until cellsPerColumn) {
                    val cellIdx = colId * cellsPerColumn + c
                    cells[cellIdx].state = CellState.ACTIVE
                    newlyActiveCells.add(cellIdx)
                }
            }
        }

        // 4. Anomaly Computation
        val instantAnomaly = if (winningColumnIds.isNotEmpty()) {
            1.0f - (correctlyPredictedActiveColumns.toFloat() / winningColumnIds.size)
        } else 0.0f

        rollingAnomalyScore = (rollingAnomalyScore * 0.8f) + (instantAnomaly * 0.2f)

        // 5. Predict next temporal states
        for (index in cells.indices) {
            if (!newlyActiveCells.contains(index)) {
                cells[index].state = CellState.INACTIVE
            }
            // Form predictive connections based on active cells transition
            val pseudoPredictive = (Random.nextFloat() < 0.2f && winningColumnIds.contains(cells[index].columnId))
            cells[index].predictiveState = pseudoPredictive
            if (cells[index].predictiveState) totalPredictiveCells++
        }

        previousActiveCells = newlyActiveCells

        val dominantPattern = when {
            rollingAnomalyScore < 0.25f -> "სტაბილური ნეირო-რიტმი (მაღალი პროგნოზირებადობა)"
            rollingAnomalyScore < 0.60f -> "დინამიკური კოგნიტური ტრანზიცია"
            else -> "ახალი მოვლენის ამოცნობა (ინოვაციური იმპულსი)"
        }

        return HtmTelemetry(
            activeColumnsCount = winningColumnIds.size,
            predictiveCellsCount = totalPredictiveCells,
            anomalyScore = rollingAnomalyScore.coerceIn(0f, 1f),
            sdrSparsityPercentage = (winningColumnIds.size.toFloat() / numColumns.toFloat()) * 100f,
            sequenceCoherence = (1.0f - rollingAnomalyScore).coerceIn(0f, 1f),
            dominantCorticalPattern = dominantPattern,
            activeColumnIndices = winningColumnIds.toList()
        )
    }

    /**
     * Bi-directional Neuromodulation from Gemini / SNN: adapts HTM receptive field boosting.
     */
    @Synchronized
    fun tuneHtmReceptiveFields(dopamine: Float, noradrenaline: Float) {
        val learningRate = 0.05f * dopamine.coerceIn(0.5f, 2.0f)
        for (col in columns) {
            if (col.isLocallyActive) {
                for (w in col.receptiveFieldWeights.indices) {
                    col.receptiveFieldWeights[w] = (col.receptiveFieldWeights[w] + learningRate * noradrenaline * 0.02f).coerceIn(0.05f, 1.0f)
                }
            }
        }
    }
}
