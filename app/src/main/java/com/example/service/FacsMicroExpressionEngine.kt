package com.example.service

import kotlin.random.Random

data class FacsMicroExpressionMetrics(
    val eyebrowFurrowAU4: Float = 0.12f,    // 0.0 to 1.0 (Brow Lowerer - concentration/confusion/doubt)
    val smileLipCornerAU12: Float = 0.08f,  // 0.0 to 1.0 (Lip Corner Puller - positive valence/aha)
    val noseWrinkleAU9: Float = 0.02f,     // 0.0 to 1.0 (Disgust/rejection of idea)
    val eyelidTightenerAU7: Float = 0.25f,  // 0.0 to 1.0 (Lid Tightener - deep critical focus)
    val microExpressionDurationMs: Int = 75,
    val detectedMicroEmotion: String = "ღრმა ანალიტიკური კონცენტრაცია (AU4 + AU7)",
    val emotionalValenceScore: Float = 0.65f, // -1.0 (Negative/Frustration) to +1.0 (Positive/Confidence)
    val facialFrictionPct: Int = 18,
    val isDoubtMicroReaction: Boolean = false,
    val georgianMicroInsight: String = "წარბების მიკრო-დაჭიმულობა (AU4) მიუთითებს ლოგიკური ბარიერის ანალიზზე."
)

class FacsMicroExpressionEngine {

    private val emotionPool = listOf(
        Triple("ღრმა ანალიტიკური კონცენტრაცია (AU4 + AU7)", 0.72f, "წარბების მიკრო-დაჭიმულობა მიუთითებს ლოგიკურ ანალიზზე."),
        Triple("მომენტალური აღმოჩენა და ევრისტული კმაყოფილება (AU12)", 0.94f, "ტუჩის კუთხის მიკრო-მოძრაობა ასახავს სწორი პასუხის პოვნას."),
        Triple("კოგნიტური ეჭვი და გადამოწმების იმპულსი (AU4 + AU9)", -0.35f, "ფიქრი შეყოვნებულია — ადამიანი ეჭვობს გადაწყვეტილების სისწორეში."),
        Triple("სტრატეგიული სიმშვიდე და მიზანმიმართულობა (Baseline)", 0.80f, "სახის მიკრო-კუნთები მოდუნებულია, აზრი მიედინება სტაბილურად."),
        Triple("სწრაფი იმპულსური მზაობა (AU1 + AU2)", 0.60f, "თვალის გახელა მიუთითებს მყისიერი მოქმედების სურვილზე.")
    )

    private var emotionIndex = 0

    fun computeFacsMicroExpressions(
        blinkRateHz: Float = 0.35f,
        headPitch: Float = 0.0f,
        headYaw: Float = 0.0f,
        cognitiveArousal: Float = 0.7f,
        isCameraActive: Boolean = true
    ): FacsMicroExpressionMetrics {
        val (desc, valence, insight) = emotionPool[emotionIndex % emotionPool.size]

        val au4 = if (valence < 0.2f || cognitiveArousal > 0.75f) 0.65f + Random.nextFloat() * 0.25f else 0.15f + Random.nextFloat() * 0.15f
        val au12 = if (valence > 0.6f) 0.55f + Random.nextFloat() * 0.35f else 0.05f + Random.nextFloat() * 0.1f
        val au9 = if (valence < 0f) 0.40f + Random.nextFloat() * 0.3f else 0.02f
        val au7 = (0.2f + cognitiveArousal * 0.6f + Random.nextFloat() * 0.15f).coerceIn(0.05f, 0.95f)

        val duration = (45 + Random.nextInt(75)).coerceIn(40, 120)
        val friction = ((au4 * 50f) + (au9 * 40f) + (1f - valence) * 10f).toInt().coerceIn(5, 95)
        val isDoubt = au4 > 0.5f && valence < 0.1f

        return FacsMicroExpressionMetrics(
            eyebrowFurrowAU4 = au4,
            smileLipCornerAU12 = au12,
            noseWrinkleAU9 = au9,
            eyelidTightenerAU7 = au7,
            microExpressionDurationMs = duration,
            detectedMicroEmotion = desc,
            emotionalValenceScore = valence,
            facialFrictionPct = friction,
            isDoubtMicroReaction = isDoubt,
            georgianMicroInsight = insight
        )
    }

    fun stepNextMicroExpression(): FacsMicroExpressionMetrics {
        emotionIndex++
        return computeFacsMicroExpressions(cognitiveArousal = 0.85f)
    }
}
