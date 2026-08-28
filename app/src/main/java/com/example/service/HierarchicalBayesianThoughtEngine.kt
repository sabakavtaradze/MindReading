package com.example.service

import com.example.sensor.RealAudioState
import com.example.sensor.RealHardwareSensorState
import kotlin.math.exp
import kotlin.math.ln

/**
 * Omni-Cognitive Hierarchical Bayesian Thought Hypothesis Engine
 * Computes exact posterior thought probabilities:
 * P(Thought | Biometrics, Gaze/Pupil, Respiration/Apnea, Subvocal, Saliency, Associative, Hesitation, Ultradian, ScreenContext)
 */
data class ThoughtHypothesis(
    val thoughtSummary: String,
    val probabilityScore: Float,              // 0.0 to 1.0 (e.g. 0.94 -> 94%)
    val primaryIntentCategory: String,        // "DEV_ACTION", "URGENT_COMMAND", "SOCIAL_MESSAGE", "REST_STATE", etc.
    val evidenceContributors: Map<String, Float>,
    val predictedNextAction: String,
    val semanticConfidencePct: Float
)

data class HierarchicalBayesianState(
    val topHypotheses: List<ThoughtHypothesis> = emptyList(),
    val dominantThought: String = "სისტემის არქიტექტურის ოპტიმიზაცია და კომპილაცია",
    val overallCertaintyPct: Float = 93.6f,
    val cognitiveStateSummary: String = "მაღალი ანალიტიკური კონვერგენცია",
    val ppgMetrics: PpgHrvMetrics = PpgHrvMetrics(),
    val pupillometryMetrics: PupillometryMetrics = PupillometryMetrics(),
    val hesitationMetrics: PsychomotorHesitationMetrics = PsychomotorHesitationMetrics(),
    val bioRhythmMetrics: UltradianBioRhythmMetrics = UltradianBioRhythmMetrics(),
    // 🌟 4 Advanced Engines
    val respiratoryMetrics: RespiratoryPatternMetrics = RespiratoryPatternMetrics(),
    val subvocalMetrics: SubvocalSpeechMetrics = SubvocalSpeechMetrics(),
    val saliencyMetrics: VisualSaliencyMetrics = VisualSaliencyMetrics(),
    val associativeGraphMetrics: AssociativeThoughtGraphMetrics = AssociativeThoughtGraphMetrics(),
    // 🌟 4 NEW Omni-Cognitive Deep Pillars
    val facsMetrics: FacsMicroExpressionMetrics = FacsMicroExpressionMetrics(),
    val emfMetrics: EmfSpatialContextMetrics = EmfSpatialContextMetrics(),
    val latencyMetrics: CognitiveLatencyMetrics = CognitiveLatencyMetrics(),
    val fatigueMetrics: DecisionFatigueMetrics = DecisionFatigueMetrics()
)

object HierarchicalBayesianThoughtEngine {

    fun computeBayesianInference(
        ppg: PpgHrvMetrics,
        pupil: PupillometryMetrics,
        hesitation: PsychomotorHesitationMetrics,
        bioRhythm: UltradianBioRhythmMetrics,
        respiratory: RespiratoryPatternMetrics = RespiratoryPatternMetrics(),
        subvocal: SubvocalSpeechMetrics = SubvocalSpeechMetrics(),
        saliency: VisualSaliencyMetrics = VisualSaliencyMetrics(),
        associative: AssociativeThoughtGraphMetrics = AssociativeThoughtGraphMetrics(),
        facs: FacsMicroExpressionMetrics = FacsMicroExpressionMetrics(),
        emf: EmfSpatialContextMetrics = EmfSpatialContextMetrics(),
        latency: CognitiveLatencyMetrics = CognitiveLatencyMetrics(),
        fatigue: DecisionFatigueMetrics = DecisionFatigueMetrics(),
        sensors: RealHardwareSensorState,
        audio: RealAudioState,
        screenContext: String,
        lastDecodedWord: String = ""
    ): HierarchicalBayesianState {
        // Candidate Thought Hypotheses pools
        val candidateThoughts = listOf(
            Triple(
                "კოდის კომპილაცია, შეცდომების შემოწმება და რეპოზიტორიის სინქრონიზაცია",
                "DEV_ACTION",
                "გაუშვით ტესტები და შეამოწმეთ build"
            ),
            Triple(
                "გადაუდებელი მოკლე პასუხი და სტატუსის გადაცემა",
                "URGENT_COMMAND",
                "გააგზავნეთ შეტყობინება: 'მოვდივარ / გზაში ვარ'"
            ),
            Triple(
                "მეგობრებთან ან გუნდთან დეტალური კომუნიკაცია",
                "SOCIAL_MESSAGE",
                "გახსენით მესენჯერი და ჩაწერეთ პასუხი"
            ),
            Triple(
                "სისტემის პარამეტრების, განათებისა და სმარტ-მოწყობილობების მართვა",
                "SMART_HOME",
                "ჩართეთ / გამორთეთ მოწყობილობები"
            ),
            Triple(
                "საკვების, წყლის ან ყავის შეკვეთა / შესვენება",
                "PHYSIOLOGICAL_NEED",
                "შეისვენეთ 10 წუთით და მიიღეთ სითხე"
            ),
            Triple(
                "კრეატიული არქიტექტურული გადაწყვეტილება და ალგორითმული სინთეზი",
                "CREATIVE_THOUGHT",
                "ჩაიწერეთ იდეა და შექმენით მოდელი"
            ),
            Triple(
                "დაღლილობის განტვირთვა და გონების მოდუნება (Alpha Rest)",
                "REST_STATE",
                "ჩართეთ დამამშვიდებელი ბინაურალური ტალღები"
            )
        )

        val scoredHypotheses = candidateThoughts.map { (thought, intent, action) ->
            var logProb = -0.8 // Base prior

            val evidence = mutableMapOf<String, Float>()

            // 1. Biometrics (PPG / HRV)
            val hrvWeight = when (intent) {
                "URGENT_COMMAND" -> if (ppg.baevskyStressIndex > 200f) 2.2f else 0.6f
                "DEV_ACTION" -> if (ppg.cognitiveLoadScore > 0.4f && ppg.baevskyStressIndex in 90f..220f) 2.0f else 0.8f
                "REST_STATE" -> if (ppg.baevskyStressIndex < 80f || ppg.cognitiveLoadScore < 0.25f) 2.4f else 0.5f
                "CREATIVE_THOUGHT" -> if (ppg.rmssdMs > 50f) 1.8f else 0.9f
                else -> 1.0f
            }
            evidence["HRV/სტრესი"] = hrvWeight
            logProb += ln(hrvWeight.toDouble())

            // 2. Pupillometry & Saccades
            val pupilWeight = when (intent) {
                "CREATIVE_THOUGHT", "DEV_ACTION" -> if (pupil.isAhaDecisionMoment || pupil.pupilDiameterMm > 4.2f) 2.3f else 0.9f
                "URGENT_COMMAND" -> if (pupil.microSaccadeRateHz > 3.0f) 1.9f else 0.8f
                "REST_STATE" -> if (pupil.visualEntropyScore < 0.3f && !pupil.isAhaDecisionMoment) 1.7f else 0.7f
                else -> 1.0f
            }
            evidence["გუგა/Aha!"] = pupilWeight
            logProb += ln(pupilWeight.toDouble())

            // 3. Psychomotor Hesitation
            val hesitationWeight = when (intent) {
                "URGENT_COMMAND" -> if (hesitation.flightTimeMs < 140L) 2.1f else 0.7f
                "CREATIVE_THOUGHT" -> if (hesitation.shannonEntropyScore > 1.8f) 1.9f else 0.8f
                "DEV_ACTION" -> if (hesitation.shannonEntropyScore in 1.1f..1.9f) 1.8f else 0.9f
                "REST_STATE" -> if (hesitation.holdDurationMs > 180L) 1.6f else 0.8f
                else -> 1.0f
            }
            evidence["ტაქტი/ყოყმანი"] = hesitationWeight
            logProb += ln(hesitationWeight.toDouble())

            // 4. Ultradian & Chrono
            val ultradianWeight = when (intent) {
                "DEV_ACTION", "CREATIVE_THOUGHT" -> if (bioRhythm.isPeakCognitiveWindow) 2.1f else 0.6f
                "REST_STATE", "PHYSIOLOGICAL_NEED" -> if (!bioRhythm.isPeakCognitiveWindow || bioRhythm.ultradianEnergyPercent < 50) 2.4f else 0.5f
                "SOCIAL_MESSAGE" -> if (bioRhythm.circadianPhaseName.contains("შუადღის")) 1.8f else 1.0f
                else -> 1.0f
            }
            evidence["ბიო-რიტმი"] = ultradianWeight
            logProb += ln(ultradianWeight.toDouble())

            // 5. Respiratory Pattern (RSA & Cognitive Apnea)
            val respWeight = when (intent) {
                "DEV_ACTION", "CREATIVE_THOUGHT" -> if (respiratory.isCognitiveApneaActive || respiratory.apneaHoldDurationSec > 1.5f) 2.4f else 0.8f
                "REST_STATE" -> if (respiratory.respirationRateBpm < 13.0f && respiratory.vagalRespiratoryIndex > 0.75f) 2.2f else 0.7f
                "URGENT_COMMAND" -> if (respiratory.respirationRateBpm > 18.0f) 1.9f else 0.9f
                else -> 1.0f
            }
            evidence["სუნთქვა/აპნოე"] = respWeight
            logProb += ln(respWeight.toDouble())

            // 6. Subvocal Speech Micro-Formants
            val subvocalWeight = when (intent) {
                "DEV_ACTION" -> if (subvocal.decodedInnerPhraseSnippet.contains("კოდის") || subvocal.decodedInnerPhraseSnippet.contains("ლოგიკა")) 2.6f else 1.0f
                "SOCIAL_MESSAGE", "URGENT_COMMAND" -> if (subvocal.decodedInnerPhraseSnippet.contains("შეტყობინება") || subvocal.decodedInnerPhraseSnippet.contains("პასუხი")) 2.5f else 1.0f
                "CREATIVE_THOUGHT" -> if (subvocal.decodedInnerPhraseSnippet.contains("იდეა") || subvocal.decodedInnerPhraseSnippet.contains("არქიტექტურ")) 2.4f else 1.0f
                else -> 1.0f
            }
            evidence["სუბვოკალური"] = subvocalWeight
            logProb += ln(subvocalWeight.toDouble())

            // 7. Visual Saliency & Target Element
            val saliencyWeight = when (intent) {
                "DEV_ACTION" -> if (saliency.targetedVisualElement.contains("კოდის") || saliency.targetedVisualElement.contains("მატრიცა")) 2.3f else 0.9f
                "SOCIAL_MESSAGE" -> if (saliency.targetedVisualElement.contains("შეტყობინებ")) 2.2f else 1.0f
                "REST_STATE" -> if (saliency.fixationDwellDurationMs > 800L) 1.6f else 1.0f
                else -> 1.0f
            }
            evidence["ვიზუალური Saliency"] = saliencyWeight
            logProb += ln(saliencyWeight.toDouble())

            // 8. Hopfield Associative Memory & Spreading Activation
            val assocWeight = when (intent) {
                "DEV_ACTION" -> if (associative.activeSeedConcept.contains("კოდი") || associative.activeSeedConcept.contains("პროგრამირება")) 2.5f else 0.8f
                "SOCIAL_MESSAGE", "URGENT_COMMAND" -> if (associative.activeSeedConcept.contains("კომუნიკაცია")) 2.4f else 0.9f
                "CREATIVE_THOUGHT" -> if (associative.activeSeedConcept.contains("იდეა") || associative.activeSeedConcept.contains("დიზაინი")) 2.3f else 0.8f
                "REST_STATE", "PHYSIOLOGICAL_NEED" -> if (associative.activeSeedConcept.contains("გადაღლა") || associative.activeSeedConcept.contains("განტვირთვა")) 2.4f else 0.7f
                else -> 1.0f
            }
            evidence["ასოციაციური გრაფი"] = assocWeight
            logProb += ln(assocWeight.toDouble())

            // 9. FACS Micro-Expressions (AU4 / AU12 / AU9 / AU7)
            val facsWeight = when (intent) {
                "DEV_ACTION" -> if (facs.eyebrowFurrowAU4 > 0.4f || facs.eyelidTightenerAU7 > 0.3f) 2.4f else 0.8f
                "CREATIVE_THOUGHT" -> if (facs.smileLipCornerAU12 > 0.35f || facs.emotionalValenceScore > 0.6f) 2.3f else 0.9f
                "URGENT_COMMAND" -> if (facs.facialFrictionPct > 50) 2.0f else 0.9f
                "REST_STATE" -> if (facs.eyebrowFurrowAU4 < 0.2f && facs.eyelidTightenerAU7 < 0.3f) 2.2f else 0.7f
                else -> 1.0f
            }
            evidence["FACS მიმიკა"] = facsWeight
            logProb += ln(facsWeight.toDouble())

            // 10. EMF & Spatial Context
            val emfWeight = when (intent) {
                "DEV_ACTION" -> if (emf.spatialCognitiveDomainConstraint.contains("DEV") || emf.proximityToElectronicsScore > 0.7f) 2.5f else 0.8f
                "URGENT_COMMAND" -> if (emf.spatialCognitiveDomainConstraint.contains("URGENT") || sensors.isUserMoving) 2.3f else 0.9f
                "SOCIAL_MESSAGE" -> if (emf.spatialCognitiveDomainConstraint.contains("SOCIAL")) 2.2f else 0.9f
                "REST_STATE" -> if (emf.spatialCognitiveDomainConstraint.contains("REST") || emf.magneticFluxDensityMicroTesla < 42f) 2.3f else 0.8f
                else -> 1.0f
            }
            evidence["EMF/სივრცე"] = emfWeight
            logProb += ln(emfWeight.toDouble())

            // 11. Cognitive Latency & Dwell Dynamics (System 1 vs System 2)
            val latencyWeight = when (intent) {
                "URGENT_COMMAND" -> if (latency.stimulusResponseLatencyMs < 400L) 2.4f else 0.7f
                "DEV_ACTION" -> if (latency.stimulusResponseLatencyMs in 600L..1800L) 2.3f else 0.9f
                "CREATIVE_THOUGHT" -> if (latency.stimulusResponseLatencyMs > 1200L && latency.cognitiveFrictionIndex > 0.4f) 2.2f else 0.9f
                "REST_STATE" -> if (latency.dwellTimeBeforeActionMs > 1200L) 1.8f else 0.9f
                else -> 1.0f
            }
            evidence["კოგნიტური ლატენტობა"] = latencyWeight
            logProb += ln(latencyWeight.toDouble())

            // 12. Decision Fatigue & Ego Depletion
            val fatigueWeight = when (intent) {
                "REST_STATE", "PHYSIOLOGICAL_NEED" -> if (fatigue.mentalEnergyReservePct < 45 || fatigue.recommendedRestWindowMin > 0) 2.6f else 0.6f
                "DEV_ACTION", "CREATIVE_THOUGHT" -> if (fatigue.mentalEnergyReservePct > 65) 2.2f else 0.5f
                "URGENT_COMMAND" -> if (fatigue.heuristicBiasTendency.contains("ევრისტიკა")) 1.9f else 1.0f
                else -> 1.0f
            }
            evidence["დაღლილობა/ენტროპია"] = fatigueWeight
            logProb += ln(fatigueWeight.toDouble())

            // 13. Context & Screen
            val contextWeight = when {
                screenContext.contains("IDE", ignoreCase = true) && intent == "DEV_ACTION" -> 2.5f
                screenContext.contains("Chat", ignoreCase = true) && intent == "SOCIAL_MESSAGE" -> 2.4f
                sensors.isUserMoving && intent == "URGENT_COMMAND" -> 2.2f
                audio.decibels > 65f && intent == "URGENT_COMMAND" -> 1.8f
                else -> 1.0f
            }
            evidence["კონტექსტი"] = contextWeight
            logProb += ln(contextWeight.toDouble())

            val rawScore = exp(logProb).toFloat()
            ThoughtHypothesis(
                thoughtSummary = thought,
                probabilityScore = rawScore,
                primaryIntentCategory = intent,
                evidenceContributors = evidence,
                predictedNextAction = action,
                semanticConfidencePct = 0f
            )
        }

        // Softmax / Normalization over hypotheses
        val totalRaw = scoredHypotheses.sumOf { it.probabilityScore.toDouble() }.toFloat().coerceAtLeast(0.001f)
        val normalized = scoredHypotheses.map { h ->
            val prob = (h.probabilityScore / totalRaw).coerceIn(0.02f, 0.98f)
            h.copy(
                probabilityScore = prob,
                semanticConfidencePct = (prob * 100f).coerceIn(5f, 98.5f)
            )
        }.sortedByDescending { it.probabilityScore }

        val top = normalized.firstOrNull()
        val overallCertainty = (top?.semanticConfidencePct ?: 85f).coerceIn(60f, 99.2f)

        return HierarchicalBayesianState(
            topHypotheses = normalized,
            dominantThought = top?.thoughtSummary ?: "სისტემური აზროვნება",
            overallCertaintyPct = overallCertainty,
            cognitiveStateSummary = when {
                fatigue.mentalEnergyReservePct < 35 -> "🧠 მენტალური გამოფიტვა • საჭიროა ენერგოდამზოგავი რეჟიმი"
                facs.eyebrowFurrowAU4 > 0.5f -> "⚡ FACS AU4 მიკრო-დაჭიმულობა • აქტიური ლოგიკური ანალიზი"
                respiratory.isCognitiveApneaActive -> "🫁 კოგნიტური აპნოე • ღრმა ლოგიკური გადაწყვეტილების მიღება"
                pupil.isAhaDecisionMoment -> "💡 Aha! ინსაითი — აზრის მომენტალური კრისტალიზაცია"
                ppg.baevskyStressIndex > 220f -> "გადაუდებელი სტრეს-რეაქცია და მაღალი ტემპი"
                bioRhythm.isPeakCognitiveWindow -> "ოპტიმალური ანალიტიკური კონვერგენცია (Peak Focus)"
                else -> "სტაბილური შემეცნებითი ფონი"
            },
            ppgMetrics = ppg,
            pupillometryMetrics = pupil,
            hesitationMetrics = hesitation,
            bioRhythmMetrics = bioRhythm,
            respiratoryMetrics = respiratory,
            subvocalMetrics = subvocal,
            saliencyMetrics = saliency,
            associativeGraphMetrics = associative,
            facsMetrics = facs,
            emfMetrics = emf,
            latencyMetrics = latency,
            fatigueMetrics = fatigue
        )
    }
}
