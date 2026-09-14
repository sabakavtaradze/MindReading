package com.example.service

import java.util.Locale
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

/**
 * SensorCognitiveReasoningEngine
 *
 * Interconnects ALL hardware, biological, optical, acoustic, inertial,
 * tactile, circadian, and neuro-electric sensors into a single coherent
 * cognitive reasoning stream.
 *
 * Formulates natural, human Georgian thoughts, candidate words, and decision
 * trees strictly justified by the cross-sensor biological state.
 */
object SensorCognitiveReasoningEngine {

    data class SensorSnapshot(
        val heartRateBpm: Int = 74,
        val stressLevelPct: Int = 22,
        val audioDb: Float = 34f,
        val audioSpeechDetected: Boolean = false,
        val touchTapsCount: Int = 12,
        val touchRateCadence: Float = 1.2f,
        val touchHesitationIndex: Float = 0.15f,
        val gazeConfidencePct: Int = 92,
        val pupilDiameterMm: Float = 3.8f,
        val blinkRatePerMinute: Float = 14f,
        val isAhaMoment: Boolean = false,
        val motionTremor: Float = 0.08f,
        val stepCount: Int = 0,
        val lightLux: Float = 180f,
        val alphaBandHz: Float = 10.4f,
        val betaBandHz: Float = 16.2f,
        val gammaBandHz: Float = 32.5f,
        val thetaBandHz: Float = 5.8f,
        val isApneaActive: Boolean = false,
        val vpuFrequencyHz: Float = 135f,
        val activeAppContext: String = "IDE / სამუშაო",
        val snnFiringRateHz: Float = 36.5f,
        val htmCorticalColumns: Int = 40,
        val hopfieldPattern: String = "ასოციაციური მეხსიერება",
        val globalWorkspaceWinner: String = "System 2 Deliberative",
        val associativeConcept: String = "კონცენტრაცია"
    )

    data class ReasoningBranch(
        val id: String,
        val title: String,
        val probabilityPct: Int,
        val description: String,
        val nextAction: String
    )

    data class SensorCognitiveResult(
        val title: String,
        val thoughtSentence: String,
        val sensorTrace: String,
        val neuralNetworksTrace: String,
        val sensorReasonExplanation: String,
        val actionPlan: String,
        val candidateWords: List<Pair<String, Int>>,
        val branches: List<ReasoningBranch>,
        val horizon30Sec: String,
        val horizon5Min: String,
        val horizon30Min: String
    )

    /**
     * Synthesizes all sensory streams and deduces what the conscious/subconscious mind
     * is thinking, why it is thinking it, and what words/decisions emerge.
     */
    fun reasonThoughtFromSensors(sensor: SensorSnapshot): SensorCognitiveResult {
        val hr = sensor.heartRateBpm.coerceIn(40, 180)
        val pupil = sensor.pupilDiameterMm.coerceIn(2.0f, 6.0f)
        val audio = sensor.audioDb.coerceIn(10f, 100f)
        val blinks = sensor.blinkRatePerMinute.coerceIn(2f, 50f)
        val alpha = sensor.alphaBandHz.coerceIn(7f, 14f)
        val tremor = sensor.motionTremor.coerceIn(0f, 5f)
        val taps = sensor.touchTapsCount
        val lux = sensor.lightLux.coerceIn(0f, 10000f)

        // Concise sensor summary line
        val sensorTrace = buildString {
            append("[❤️ პულსი: $hr BPM]")
            append(" • [👁️ გუგა: ${String.format(Locale.US, "%.1f", pupil)}მმ]")
            append(" • [🎙️ ხმა: ${audio.toInt()} dB]")
            append(" • [✋ შეხება: $taps]")
            append(" • [🌊 ალფა: ${String.format(Locale.US, "%.1f", alpha)}Hz]")
            if (lux < 25f) append(" • [🌙 ბნელი]")
            if (sensor.isAhaMoment) append(" • [💡 Aha!]")
            if (sensor.isApneaActive) append(" • [🫁 აპნოე]")
            if (sensor.stepCount > 5 || tremor > 0.35f) append(" • [🚶 მოძრაობა]")
        }

        // Interconnected Multi-Neural Networks Trace
        val neuralTrace = buildString {
            append("⚡ SNN [${String.format(Locale.US, "%.1f", sensor.snnFiringRateHz)} Hz]")
            append(" ⇄ 🧬 HTM [${sensor.htmCorticalColumns} სვეტი]")
            append(" ⇄ 🌌 Hopfield [${sensor.hopfieldPattern}]")
            append(" ⇄ 🏆 Workspace [${sensor.globalWorkspaceWinner}]")
            if (sensor.associativeConcept.isNotBlank()) {
                append(" ⇄ 🕸️ გრაფი [${sensor.associativeConcept}]")
            }
        }

        // Cross-Sensor Reasoning Rules
        return when {
            // 1. AHA! MOMENT OR SUDDEN PUPIL DILATION IN CALM STATE
            sensor.isAhaMoment || (pupil >= 4.2f && hr in 55..82) -> {
                val title = "მოულოდნელი იდეის გაჩენა (Aha! მომენტი)"
                val thought = "გუგები გაფართოვდა (${String.format(Locale.US, "%.1f", pupil)} მმ) და პულსი მშვიდია ($hr BPM) — უეცრად საუკეთესო გამოსავალი დავინახე და მინდა დაუყოვნებლივ განვახორციელო."
                val reason = "რადგან გუგის გაფართოება დაფიქსირდა მშვიდი გულისცემის ფონზე, ტვინმა განიცადა კოგნიტური გამონათება და ოპტიმალური გადაწყვეტა იპოვა."
                val actionPlan = "• იდეის მოკლედ ჩანიშვნა რვეულში\n• გადაწყვეტის ლოგიკური შემოწმება\n• პირველი ნაბიჯის შესრულება"
                val words = listOf(
                    "იდეა" to 98,
                    "გადაწყვეტა" to 96,
                    "სწორი" to 93,
                    "ჩანიშვნა" to 89,
                    "შედეგი" to 86,
                    "კარგი" to 82
                )
                val branches = listOf(
                    ReasoningBranch("br_aha_1", "შტო A: იდეის ჩანიშვნა და დაფიქსირება", 65, "დაუყოვნებლივ დაფიქსირდეს მიგნებული გადაწყვეტა.", "ჩანიშვნის ბუფერი"),
                    ReasoningBranch("br_aha_2", "შტო B: ლოგიკური გადამოწმება", 25, "შეფასდეს იდეის პრაქტიკული სისწორე.", "ვალიდაცია"),
                    ReasoningBranch("br_aha_3", "შტო C: საქმის გაგრძელება", 10, "შედეგის ინტეგრირება მიმდინარე პროცესში.", "გაგრძელება")
                )
                SensorCognitiveResult(
                    title = title,
                    thoughtSentence = thought,
                    sensorTrace = sensorTrace,
                    neuralNetworksTrace = neuralTrace,
                    sensorReasonExplanation = reason,
                    actionPlan = actionPlan,
                    candidateWords = words,
                    branches = branches,
                    horizon30Sec = "+30წმ: იდეის მონახაზის შექმნა ეკრანზე",
                    horizon5Min = "+5წთ: მიგნების დეტალური დამუშავება",
                    horizon30Min = "+30წთ: წარმატებული შედეგის მიღწევა"
                )
            }

            // 2. COGNITIVE APNEA (BREATH HOLDING) & PEAK ANALYTICAL LOGIC
            sensor.isApneaActive || (sensor.gammaBandHz > 35f && sensor.touchHesitationIndex > 0.35f) -> {
                val title = "რთული ლოგიკური ამოცანის ანალიზი"
                val thought = "სუნთქვა შევაჩერე და თითი ეკრანზე შევაყოვნე — რთულ კოგნიტურ კვანძს ვხსნი და ზუსტ ნაბიჯს ვარჩევ."
                val reason = "რადგან დაფიქსირდა სუნთქვის ხანმოკლე შეკავება (კოგნიტური აპნოე) და გამა-ტალღების ზრდა (${String.format(Locale.US, "%.1f", sensor.gammaBandHz)}Hz), ტვინი მაქსიმალურ ანალიტიკურ ძალისხმევას ხარჯავს."
                val actionPlan = "• მშვიდად ამოსუნთქვა და ჟანგბადის მიწოდება\n• არჩევანის ორ ვარიანტამდე დაყვანა\n• გააზრებული გადაწყვეტილების მიღება"
                val words = listOf(
                    "ანალიზი" to 97,
                    "ლოგიკა" to 95,
                    "გადაწყვეტილება" to 92,
                    "სუნთქვა" to 88,
                    "ამოცანა" to 85,
                    "შედეგი" to 81
                )
                val branches = listOf(
                    ReasoningBranch("br_apn_1", "შტო A: ზუსტი ლოგიკური გადაწყვეტა", 60, "ანალიზის დასრულება და ნაბიჯის გადადგმა.", "გადაწყვეტის არჩევა"),
                    ReasoningBranch("br_apn_2", "შტო B: ამოსუნთქვა და გადამოწმება", 30, "ღრმა ჩასუნთქვა გონების განსატვირთად.", "ჟანგბადის ბალანსი"),
                    ReasoningBranch("br_apn_3", "შტო C: ალტერნატივის ძებნა", 10, "სხვა შესაძლო ვარიანტის განხილვა.", "შედარება")
                )
                SensorCognitiveResult(
                    title = title,
                    thoughtSentence = thought,
                    sensorTrace = sensorTrace,
                    neuralNetworksTrace = neuralTrace,
                    sensorReasonExplanation = reason,
                    actionPlan = actionPlan,
                    candidateWords = words,
                    branches = branches,
                    horizon30Sec = "+30წმ: ამოცანის გადაჭრა და ამოსუნთქვა",
                    horizon5Min = "+5წთ: ლოგიკური ჯაჭვის დასრულება",
                    horizon30Min = "+30წთ: შემდეგ ეტაპზე გადასვლა"
                )
            }

            // 3. ELEVATED HEART RATE OR STRESS & RAPID TOUCHES (URGENT REACTION)
            hr > 84 || (sensor.stressLevelPct > 55 && taps > 15) -> {
                val title = "სწრაფი კომუნიკაცია და გადაუდებელი მოქმედება"
                val thought = "პულსი აჩქარებულია ($hr BPM) და თითის შეხების ტემპი მაღალია — მსურს სწრაფად ვუპასუხო შეტყობინებას და საქმე დროულად მოვაგვარო."
                val reason = "რადგან გულისცემა მომატებულია ($hr BPM) და ტაქტილური შეხებები აჩქარებულია, სიმპათიკური ნერვული სისტემა ითხოვს სწრაფ, გადამწყვეტ რეაგირებას."
                val actionPlan = "• მოკლე და მკაფიო პასუხის გაგზავნა\n• ღრმა ჩასუნთქვა პულსის დასამშვიდებლად\n• სამუშაო ტემპის თანდათან სტაბილიზაცია"
                val words = listOf(
                    "სწრაფად" to 98,
                    "პასუხი" to 95,
                    "გაგზავნა" to 92,
                    "დიახ" to 89,
                    "ახლა" to 86,
                    "სიმშვიდე" to 82
                )
                val branches = listOf(
                    ReasoningBranch("br_urg_1", "შტო A: პასუხის სწრაფად გაგზავნა", 62, "შეტყობინების დაუყოვნებლივ დასრულება.", "გაგზავნა"),
                    ReasoningBranch("br_urg_2", "შტო B: პულსის შენელება და დამშვიდება", 28, "სუნთქვის გათანაბრება და სიმშვიდე.", "ჩასუნთქვა"),
                    ReasoningBranch("br_urg_3", "შტო C: პაუზის აღება", 10, "ხანმოკლე 1-წუთიანი შესვენება.", "პაუზა")
                )
                SensorCognitiveResult(
                    title = title,
                    thoughtSentence = thought,
                    sensorTrace = sensorTrace,
                    neuralNetworksTrace = neuralTrace,
                    sensorReasonExplanation = reason,
                    actionPlan = actionPlan,
                    candidateWords = words,
                    branches = branches,
                    horizon30Sec = "+30წმ: შეტყობინების გაგზავნა",
                    horizon5Min = "+5წთ: პულსის ნორმალიზება (72 BPM)",
                    horizon30Min = "+30წთ: მშვიდ სამუშაო რეჟიმში დაბრუნება"
                )
            }

            // 4. EYE STRAIN & BLINKING / TIRED GAZE
            blinks > 22f || (sensor.gazeConfidencePct < 70 && pupil < 2.9f) -> {
                val title = "თვალების დასვენება და განტვირთვა"
                val thought = "თვალებს ხშირად ვახამხამებ (წუთში ${blinks.toInt()}-ჯერ) — მზერა ოდნავ დაიღალა, 1 წუთით ეკრანს უნდა მოვცილდე და ფანჯარაში გავიხედო."
                val reason = "რადგან თვალების ხამხამის სიხშირე მაღალია (${blinks.toInt()}/წთ) და გუგა შევიწროებულია, მხედველობითი ანალიზატორი ითხოვს ეკრანისგან ხანმოკლე დასვენებას."
                val actionPlan = "• 20 წამით მზერის შორს გადატანა\n• რამდენჯერმე ღრმად ჩასუნთქვა\n• თვალების დახამხამება და მოდუნება"
                val words = listOf(
                    "დასვენება" to 98,
                    "თვალები" to 96,
                    "მზერა" to 92,
                    "ფანჯარა" to 88,
                    "წყალი" to 85,
                    "სიმშვიდე" to 82
                )
                val branches = listOf(
                    ReasoningBranch("br_eye_1", "შტო A: მზერის შორს გადატანა", 65, "ეკრანიდან მზერის მოცილება 30 წამით.", "განტვირთვა"),
                    ReasoningBranch("br_eye_2", "შტო B: წყლის დალევა", 25, "წყლის მიღება და ენერგიის აღდგენა.", "ჰიდრატაცია"),
                    ReasoningBranch("br_eye_3", "შტო C: მუშაობის გაგრძელება", 10, "შემსუბუქებული ეკრანის განათებით მუშაობა.", "შერბილება")
                )
                SensorCognitiveResult(
                    title = title,
                    thoughtSentence = thought,
                    sensorTrace = sensorTrace,
                    neuralNetworksTrace = neuralTrace,
                    sensorReasonExplanation = reason,
                    actionPlan = actionPlan,
                    candidateWords = words,
                    branches = branches,
                    horizon30Sec = "+30წმ: თვალების მოდუნება და ფანჯარაში გახედვა",
                    horizon5Min = "+5წთ: ენერგიის სრული აღდგენა",
                    horizon30Min = "+30წთ: კომფორტული მუშაობის გაგრძელება"
                )
            }

            // 5. PHYSICAL MOTION / KINETIC ACTIVITY (WALKING, MOVING)
            sensor.stepCount > 5 || tremor > 0.3f -> {
                val title = "მოძრაობაში ყოფნა და გზის გაგრძელება"
                val thought = "აქსელერომეტრი მოძრაობას აფიქსირებს — გზაში ვარ, ნაბიჯს მივყვები და პარალელურად შემდეგ ამოცანაზე ვფიქრობ."
                val reason = "რადგან მოძრაობის სენსორები (ინერციული ტრემორი ${String.format(Locale.US, "%.2f", tremor)} m/s²) აჩვენებს ფიზიკურ აქტიურობას, გონება სივრცით ორიენტაციასა და გადაადგილებაზეა მიმართული."
                val actionPlan = "• გარემოს უსაფრთხოდ კონტროლი\n• ნაბიჯის რიტმის შენარჩუნება\n• დანიშნულების ადგილას დროულად მისვლა"
                val words = listOf(
                    "მივდივარ" to 97,
                    "ნაბიჯი" to 95,
                    "გზა" to 92,
                    "სწრაფი" to 88,
                    "ქუჩა" to 85,
                    "დრო" to 81
                )
                val branches = listOf(
                    ReasoningBranch("br_mot_1", "შტო A: გზის გაგრძელება დანიშნულებისკენ", 65, "სიარულის რიტმის შენარჩუნება.", "გადაადგილება"),
                    ReasoningBranch("br_mot_2", "შტო B: მოკლე გაჩერება და ორიენტაცია", 25, "მიმართულების დაზუსტება.", "შემოწმება"),
                    ReasoningBranch("br_mot_3", "შტო C: ტელეფონის ჯიბეში ჩადება", 10, "უსაფრთხო სიარული ხელების თავისუფლებით.", "ჯიბეში ჩადება")
                )
                SensorCognitiveResult(
                    title = title,
                    thoughtSentence = thought,
                    sensorTrace = sensorTrace,
                    neuralNetworksTrace = neuralTrace,
                    sensorReasonExplanation = reason,
                    actionPlan = actionPlan,
                    candidateWords = words,
                    branches = branches,
                    horizon30Sec = "+30წმ: უწყვეტი ნაბიჯით სიარული",
                    horizon5Min = "+5წთ: დანიშნულების ადგილზე მისვლა",
                    horizon30Min = "+30წთ: სამუშაო სივრცეში დაბრუნება"
                )
            }

            // 6. HIGH AMBIENT NOISE & NEED FOR SILENCE OR EARPHONES
            audio > 58f -> {
                val title = "გარემოს ხმაური და ყურადღების შენარჩუნება"
                val thought = "ირგვლივ ხმაურია (${audio.toInt()} dB) — ყურადღება მეფანტება, ყურსასმენები უნდა მოვირგო ან უფრო მშვიდ სივრცეში გადავიდე."
                val reason = "რადგან მიკროფონი აფიქსირებს მომატებულ ხმაურს (${audio.toInt()} dB), სმენითი დატვირთვა არღვევს კონცენტრაციას და ტვინი სიჩუმეს ეძებს."
                val actionPlan = "• ყურსასმენების ჩართვა / მუსიკის ფონი\n• უფრო მშვიდი ადგილის პოვნა\n• ყურადღების ხელახალი კონცენტრირება"
                val words = listOf(
                    "ხმაური" to 97,
                    "სიჩუმე" to 95,
                    "ყურსასმენი" to 92,
                    "ყურადღება" to 88,
                    "სიმშვიდე" to 85,
                    "ოთახი" to 81
                )
                val branches = listOf(
                    ReasoningBranch("br_noi_1", "შტო A: ყურსასმენების მორგება", 60, "გარე ხმაურის იზოლირება ყურსასმენით.", "ხმის ჩახშობა"),
                    ReasoningBranch("br_noi_2", "შტო B: მშვიდ ოთახში გადასვლა", 30, "სივრცის შეცვლა სამუშაო სიმშვიდისთვის.", "ადგილის შეცვლა"),
                    ReasoningBranch("br_noi_3", "შტო C: ყურადღების მობილიზება", 10, "ხმაურის მიუხედავად ფოკუსის დაჭერა.", "კონცენტრაცია")
                )
                SensorCognitiveResult(
                    title = title,
                    thoughtSentence = thought,
                    sensorTrace = sensorTrace,
                    neuralNetworksTrace = neuralTrace,
                    sensorReasonExplanation = reason,
                    actionPlan = actionPlan,
                    candidateWords = words,
                    branches = branches,
                    horizon30Sec = "+30წმ: ყურსასმენების მორგება",
                    horizon5Min = "+5წთ: აკუსტიკური სიმშვიდის შექმნა",
                    horizon30Min = "+30წთ: უწყვეტი პროდუქტიული მუშაობა"
                )
            }

            // 7. LOW LIGHT & EVENING / NIGHT RELAXATION
            lux < 25f -> {
                val title = "საღამოს სიმშვიდე და დასვენება"
                val thought = "ოთახში დაბალი განათებაა (${lux.toInt()} Lux) — თვალები ისვენებს, დროა საღამოს მშვიდ რეჟიმზე გადავიდე და დავისვენო."
                val reason = "რადგან განათების სენსორი აჩვენებს მკრთალ შუქს (${lux.toInt()} Lux), ცირკადული ბიორიტმი მელატონინის გამომუშავებასა და მოსვენებას უწყობს ხელს."
                val actionPlan = "• ეკრანის სიკაშკაშის შემცირება\n• დღის საქმეების შეჯამება\n• მშვიდი ძილისთვის მომზადება"
                val words = listOf(
                    "საღამო" to 97,
                    "სიმშვიდე" to 95,
                    "ძილი" to 92,
                    "დასვენება" to 88,
                    "ოთახი" to 85,
                    "მყუდრო" to 81
                )
                val branches = listOf(
                    ReasoningBranch("br_lux_1", "შტო A: საღამოს განტვირთვა", 65, "დღის დასრულება და მოდუნება.", "მშვიდი საღამო"),
                    ReasoningBranch("br_lux_2", "შტო B: ეკრანის ჩაბნელება", 25, "თვალის დაცვა ღამის რეჟიმით.", "ღამის რეჟიმი"),
                    ReasoningBranch("br_lux_3", "შტო C: მოკლე ჩანიშვნა ხვალისთვის", 10, "ხვალ გასაკეთებელი საქმის ჩანიშვნა.", "გეგმა ხვალისთვის")
                )
                SensorCognitiveResult(
                    title = title,
                    thoughtSentence = thought,
                    sensorTrace = sensorTrace,
                    neuralNetworksTrace = neuralTrace,
                    sensorReasonExplanation = reason,
                    actionPlan = actionPlan,
                    candidateWords = words,
                    branches = branches,
                    horizon30Sec = "+30წმ: ეკრანის ფერების შერბილება",
                    horizon5Min = "+5წთ: საქმეების შეჯამება",
                    horizon30Min = "+30წთ: მშვიდი ძილისა და დასვენების დაწყება"
                )
            }

            // 8. DEEP ALPHA FLOW & QUIET HARMONY (DEFAULT OPTIMAL CREATIVE STATE)
            else -> {
                val title = "მშვიდი შემოქმედებითი ნაკადი და ფოკუსი"
                val thought = "სრული სიჩუმეა (${audio.toInt()} dB), პულსი სტაბილურია ($hr BPM) და ალფა-ტალღები დომინირებს (${String.format(Locale.US, "%.1f", alpha)}Hz) — აზრები თავისუფლად და თანმიმდევრულად მიედინება."
                val reason = "რადგან გარემო მშვიდია (${audio.toInt()} dB) და ალფა-ტალღები დომინირებს (${String.format(Locale.US, "%.1f", alpha)}Hz), გონება იმყოფება ოპტიმალურ Flow ნაკადში."
                val actionPlan = "• მიმდინარე ამოცანის მშვიდად გაგრძელება\n• ზედმეტი შეფერხებების გამორიცხვა\n• შემოქმედებითი შედეგის მიღწევა"
                val words = listOf(
                    "ნაკადი" to 98,
                    "ფიქრი" to 95,
                    "საქმე" to 92,
                    "სიმშვიდე" to 89,
                    "შექმნა" to 86,
                    "წესრიგი" to 83
                )
                val branches = listOf(
                    ReasoningBranch("br_flow_1", "შტო A: საქმის შეუფერხებელი გაგრძელება", 68, "მიმდინარე სამუშაო ნაკადის შენარჩუნება.", "აქტიური ფოკუსი"),
                    ReasoningBranch("br_flow_2", "შტო B: ახალი იდეის ჩანიშვნა", 22, "შემოქმედებითი ნაპერწკლის დაფიქსირება.", "ჩანიშვნა"),
                    ReasoningBranch("br_flow_3", "შტო C: ხანმოკლე ყავის შესვენება", 10, "ენერგიის მცირე შევსება.", "შესვენება")
                )
                SensorCognitiveResult(
                    title = title,
                    thoughtSentence = thought,
                    sensorTrace = sensorTrace,
                    neuralNetworksTrace = neuralTrace,
                    sensorReasonExplanation = reason,
                    actionPlan = actionPlan,
                    candidateWords = words,
                    branches = branches,
                    horizon30Sec = "+30წმ: ფოკუსირებული წერა და მუშაობა",
                    horizon5Min = "+5წთ: ამოცანის პირველი ეტაპის დასრულება",
                    horizon30Min = "+30წთ: მაღალი ხარისხის შედეგის მიღება"
                )
            }
        }
    }
}
