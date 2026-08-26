package com.example.service

import kotlin.random.Random

data class VisualSaliencyMetrics(
    val focalPointX: Float = 0.5f,
    val focalPointY: Float = 0.42f,
    val saliencyConfidencePct: Float = 96.2f,
    val fixationDwellDurationMs: Long = 420L,
    val targetedVisualElement: String = "ნეირონული ინფერენციის მატრიცა",
    val predictedVisualIntent: String = "კოდის სტრუქტურისა და ლოგიკის ინსპექცია",
    val heatmapGrid: List<Float> = emptyList(), // 16 items for 4x4 density
    val gazeDispersionRadiusPx: Float = 14.5f
)

class VisualSaliencyEngine {

    private val screenElements = listOf(
        "კოდის ედიტორი და Compose ხედი" to "კოდის სტრუქტურისა და ლოგიკის ინსპექცია",
        "ბაიესური ინფერენციის მატრიცა" to "ალბათური განაწილების შემოწმება",
        "სენსორული ტელემეტრიის ბარათი" to "ბიომეტრიული მონაცემების ვალიდაცია",
        "ნეირო-სინაფსური ისტორია" to "წინა აზრების ქრონოლოგიის გადახედვა",
        "პირდაპირი სიტყვების დეკოდერი" to "სუბვოკალური ფონემების კითხვა"
    )

    private var targetIndex = 0

    fun computeSaliency(
        gazeX: Float = 0.0f,
        gazeY: Float = 0.0f,
        isGazeActive: Boolean = true,
        pupilFixationScore: Float = 85f
    ): VisualSaliencyMetrics {
        // Map normalized gaze (-1..1) to screen focal space (0..1)
        val fx = ((gazeX + 1f) / 2f).coerceIn(0.05f, 0.95f)
        val fy = ((gazeY + 1f) / 2f).coerceIn(0.05f, 0.95f)

        val dwellMs = (280L + (pupilFixationScore.toLong() * 4L) + Random.nextLong(60)).coerceIn(150L, 1200L)
        val confidence = (85.0f + (pupilFixationScore * 0.14f)).coerceIn(80f, 99.5f)

        // Generate 4x4 heatmap grid centered near focal point
        val grid = ArrayList<Float>(16)
        val targetCol = (fx * 4).toInt().coerceIn(0, 3)
        val targetRow = (fy * 4).toInt().coerceIn(0, 3)

        for (r in 0 until 4) {
            for (c in 0 until 4) {
                val dist = kotlin.math.sqrt(((r - targetRow) * (r - targetRow) + (c - targetCol) * (c - targetCol)).toFloat())
                val intensity = (1.0f / (1.0f + dist * 1.6f)).coerceIn(0.05f, 1.0f)
                grid.add(intensity)
            }
        }

        val (elem, intent) = screenElements[targetIndex % screenElements.size]

        return VisualSaliencyMetrics(
            focalPointX = fx,
            focalPointY = fy,
            saliencyConfidencePct = confidence,
            fixationDwellDurationMs = dwellMs,
            targetedVisualElement = elem,
            predictedVisualIntent = intent,
            heatmapGrid = grid,
            gazeDispersionRadiusPx = 12f + (100f - pupilFixationScore) * 0.2f
        )
    }

    fun cycleTargetElement(): VisualSaliencyMetrics {
        targetIndex++
        return computeSaliency(gazeX = (Random.nextFloat() - 0.5f), gazeY = (Random.nextFloat() - 0.5f))
    }
}
