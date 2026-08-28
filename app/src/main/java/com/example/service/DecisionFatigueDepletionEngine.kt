package com.example.service

import kotlin.random.Random

data class DecisionFatigueMetrics(
    val mentalEnergyReservePct: Int = 82,     // 100% full capacity, <35% high fatigue
    val decisionDepletionLevel: String = "მაღალი კოგნიტური გამტარუნარიანობა (Peak Agency)",
    val accumulatedDecisionsToday: Int = 142,
    val cognitiveEntropyJitter: Float = 0.18f,
    val willPowerCapacityIndex: Float = 0.85f,
    val heuristicBiasTendency: String = "ლოგიკური აბსტრაქცია (Low Bias)", // At high fatigue, mind shifts to "მყისიერი ევრისტიკა და მარტივი არჩევანი"
    val recommendedRestWindowMin: Int = 0,
    val georgianFatigueSummary: String = "მენტალური ენერგია 82%-ია. ტვინი მზადაა რთული აბსტრაქტული ამოცანების გადასაჭრელად."
)

class DecisionFatigueDepletionEngine {

    private val fatigueProfiles = listOf(
        Pair(88, "სრული კოგნიტური რესურსი • რთული აბსტრაქტული აზროვნება"),
        Pair(68, "სტაბილური ენერგია • პროდუქტიული ლოგიკური ნაკადი"),
        Pair(44, "ზომიერი დაღლილობა • გადასვლა ევრისტიკულ გადაწყვეტილებებზე"),
        Pair(22, "მენტალური გამოფიტვა (Decision Fatigue) • საჭიროა 15წთ განტვირთვა")
    )

    private var profileIndex = 0

    fun computeFatigue(
        sessionDurationMinutes: Int = 45,
        stressLevelPct: Int = 30,
        ultradianEnergy: Int = 80
    ): DecisionFatigueMetrics {
        val (baseReserve, desc) = fatigueProfiles[profileIndex % fatigueProfiles.size]

        val reserve = (baseReserve - (stressLevelPct * 0.1f) + (ultradianEnergy * 0.1f)).toInt().coerceIn(10, 100)
        val decisions = 120 + profileIndex * 65 + Random.nextInt(20)
        val willPower = (reserve / 100f).coerceIn(0.1f, 1.0f)
        val jitter = (1.0f - willPower) * 0.6f + Random.nextFloat() * 0.05f

        val bias = when {
            reserve > 70 -> "კომპლექსური ლოგიკური აბსტრაქცია (Low Bias)"
            reserve > 40 -> "დაბალანსებული პრაგმატული არჩევანი"
            else -> "ინსტინქტური ევრისტიკა & ენერგოდამზოგავი აზრი (High Bias)"
        }

        val restMin = if (reserve < 40) (40 - reserve) else 0

        val summary = when {
            reserve > 75 -> "ტვინის ენერგია მაღალია ($reserve%). აქტიურია კომპლექსური არქიტექტურული აზროვნება."
            reserve in 45..75 -> "ენერგია ოპტიმალურ ზონაშია ($reserve%). აზრები პრაგმატული და მიზანმიმართულია."
            else -> "გადაწყვეტილების მიღების გადაღლა ($reserve%). ტვინი ირჩევს უმარტივეს ალტერნატივებს."
        }

        return DecisionFatigueMetrics(
            mentalEnergyReservePct = reserve,
            decisionDepletionLevel = desc,
            accumulatedDecisionsToday = decisions,
            cognitiveEntropyJitter = jitter,
            willPowerCapacityIndex = willPower,
            heuristicBiasTendency = bias,
            recommendedRestWindowMin = restMin,
            georgianFatigueSummary = summary
        )
    }

    fun stepNextFatigueState(): DecisionFatigueMetrics {
        profileIndex++
        return computeFatigue()
    }
}
