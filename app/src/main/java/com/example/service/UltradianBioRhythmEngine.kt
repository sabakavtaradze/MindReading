package com.example.service

import java.util.Calendar
import kotlin.math.cos
import kotlin.math.sin

/**
 * 4. Ultradian & Circadian Bio-Rhythm Model
 * Models Kleitman's Basic Rest-Activity Cycle (BRAC - 90-120 min oscillations)
 * coupled with 24-hour Circadian chronobiology to estimate cognitive energy,
 * analytical acuity, fatigue slope, and optimal thought synthesis windows.
 */
data class UltradianBioRhythmMetrics(
    val currentUltradianPhaseMinutes: Int = 45,       // 0 to 90 min
    val ultradianEnergyPercent: Int = 84,             // 0 to 100%
    val isPeakCognitiveWindow: Boolean = true,        // Top 30-70 min of 90-min cycle
    val circadianPhaseName: String = "დილის ანალიტიკური პიკი",
    val cognitiveFuelReservePct: Int = 78,
    val recoveryRecommendationMinutes: Int = 12,
    val optimalThoughtDomain: String = "ალგორითმული / სისტემური გადაწყვეტილებები",
    val cycleProgressRatio: Float = 0.5f             // 0.0 to 1.0
)

object UltradianBioRhythmEngine {

    fun computeBioRhythm(): UltradianBioRhythmMetrics {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        // Total minutes from midnight
        val totalMinutes = hour * 60 + minute
        val cycleLength = 90
        val cycleProgressMin = totalMinutes % cycleLength
        val cycleRatio = cycleProgressMin.toFloat() / cycleLength.toFloat()

        // Ultradian wave: Peak at middle of 90 min (sine wave)
        val angle = cycleRatio * 2 * Math.PI
        val waveVal = sin(angle - Math.PI / 2).toFloat() // -1.0 to +1.0
        val ultradianEnergy = (((waveVal + 1.0f) / 2.0f) * 60f + 40f).toInt().coerceIn(30, 100)

        val isPeak = cycleProgressMin in 20..65

        // Circadian profile
        val (circadianName, cognitiveFuel, optimalDomain) = when (hour) {
            in 6..8 -> Triple("დილის გამოღვიძება & კორტიზოლის ზრდა", 85, "დღის დაგეგმვა & მოკლე ამოცანები")
            in 9..12 -> Triple("ანალიტიკური პიკი (Peak Alpha Focus)", 95, "კომპლექსური ლოგიკა, კოდი, არქიტექტურა")
            in 13..15 -> Triple("შუადღის პოსტ-პრანდიალური დიპი", 58, "კომუნიკაცია, მესენჯერი, რუტინული შემოწმება")
            in 16..19 -> Triple("მეორე კოგნიტური ტალღა (Prefrontal Boost)", 88, "სინთეზი, გადაწყვეტილებები, იმპლემენტაცია")
            in 20..23 -> Triple("საღამოს შემოქმედებითი სინაფსი", 70, "კრეატიული იდეები, რეფლექსია, სმარტ-ჰოუმი")
            else -> Triple("ღამის ქვეცნობიერი კონსოლიდაცია", 45, "მინიმალური გადაუდებელი ბრძანებები")
        }

        val recoveryMin = if (!isPeak) (90 - cycleProgressMin).coerceIn(5, 25) else 0

        return UltradianBioRhythmMetrics(
            currentUltradianPhaseMinutes = cycleProgressMin,
            ultradianEnergyPercent = (ultradianEnergy * (cognitiveFuel / 100f)).toInt().coerceIn(20, 100),
            isPeakCognitiveWindow = isPeak,
            circadianPhaseName = circadianName,
            cognitiveFuelReservePct = cognitiveFuel,
            recoveryRecommendationMinutes = recoveryMin,
            optimalThoughtDomain = optimalDomain,
            cycleProgressRatio = cycleRatio
        )
    }
}
