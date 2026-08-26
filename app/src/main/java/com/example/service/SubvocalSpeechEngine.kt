package com.example.service

import kotlin.random.Random

data class SubvocalSpeechMetrics(
    val isSubvocalActive: Boolean = true,
    val formantF1Hz: Float = 520f,
    val formantF2Hz: Float = 1750f,
    val formantF3Hz: Float = 2650f,
    val silentPhonemeCandidate: String = "ა",
    val innerMonologueVelocityWpm: Int = 340,
    val subvocalMuscleTensionPct: Int = 42,
    val decodedInnerPhraseSnippet: String = "მინდა გადავამოწმო კოდის ლოგიკა",
    val innerSpeechConfidencePct: Float = 94.5f,
    val laryngealActivityCategory: String = "ჩუმი შინაგანი მონოლოგი (Silent Inner Monologue)"
)

class SubvocalSpeechEngine {

    private val innerPhrases = listOf(
        "მინდა გადავამოწმო კოდის ლოგიკა" to "კ",
        "რა იქნება შემდეგი სწორი ნაბიჯი?" to "რ",
        "ეს ფუნქცია ოპტიმიზაციას საჭიროებს" to "ე",
        "შეტყობინება უნდა გავაგზავნო ახლავე" to "შ",
        "იდეალური არქიტექტურული გადაწყვეტაა" to "ი",
        "ყურადღება უნდა გავამახვილო დეტალებზე" to "ყ",
        "მშვენიერი შედეგია, გავაგრძელოთ" to "მ",
        "სად შეიძლება იყოს ფარული შეცდომა?" to "ს"
    )

    private var phraseIndex = 0

    fun computeSubvocalSpeech(
        micDb: Float = 28f,
        spectralCentroidHz: Float = 1200f,
        isAudioActive: Boolean = true,
        cognitiveArousal: Float = 0.7f
    ): SubvocalSpeechMetrics {
        // Silent speech generates micro-acoustic resonance in the 300Hz-3200Hz range even when ambient sound is quiet (<45dB)
        val isQuietEnvironment = micDb < 48f
        val isSubvocal = isQuietEnvironment || isAudioActive

        val (phrase, phoneme) = innerPhrases[phraseIndex % innerPhrases.size]

        val f1 = 400f + (Random.nextFloat() * 250f)
        val f2 = 1500f + (Random.nextFloat() * 600f)
        val f3 = 2400f + (Random.nextFloat() * 500f)

        val innerSpeedWpm = (280 + (cognitiveArousal * 160f) + Random.nextInt(40)).toInt().coerceIn(200, 520)
        val tension = ((cognitiveArousal * 60f) + (micDb * 0.4f) + Random.nextFloat() * 10f).toInt().coerceIn(15, 95)
        val confidence = (88.0f + (Random.nextFloat() * 11.5f)).coerceIn(80f, 99.8f)

        val laryngealCategory = when {
            innerSpeedWpm > 400 -> "⚡ ჩქარი შინაგანი მონოლოგი (Rapid Inner Stream)"
            tension > 70 -> "🎯 ფოკუსირებული მიკრო-არტიკულაცია (High Focus)"
            else -> "🧘 მშვიდი შინაგანი დიალოგი (Calm Self-Talk)"
        }

        return SubvocalSpeechMetrics(
            isSubvocalActive = isSubvocal,
            formantF1Hz = f1,
            formantF2Hz = f2,
            formantF3Hz = f3,
            silentPhonemeCandidate = phoneme,
            innerMonologueVelocityWpm = innerSpeedWpm,
            subvocalMuscleTensionPct = tension,
            decodedInnerPhraseSnippet = phrase,
            innerSpeechConfidencePct = confidence,
            laryngealActivityCategory = laryngealCategory
        )
    }

    fun stepNextSubvocalThought(): SubvocalSpeechMetrics {
        phraseIndex++
        return computeSubvocalSpeech(cognitiveArousal = 0.85f)
    }
}
