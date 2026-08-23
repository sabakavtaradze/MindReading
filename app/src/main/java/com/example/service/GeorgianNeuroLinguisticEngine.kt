package com.example.service

/**
 * Georgian Neural Linguistic Engine
 * Analyzes and generates Georgian morphological verbs, sub-vocal phonemes,
 * thought patterns, and mental semantic associations.
 */
object GeorgianNeuroLinguisticEngine {

    // 33 Georgian Alphabet Phonemes with Neuro-EMG articulation profiles
    data class GeorgianPhoneme(
        val letter: Char,
        val phoneticType: String, // მკვეთრი, ფშვინვიერი, მჟღერი, ნაპრალოვანი, ხმოვანი
        val laryngealEmgFrequencyHz: Float,
        val articulatoryEffort: Float // 0.1 to 1.0
    )

    val GEORGIAN_PHONEME_MAP = mapOf(
        // ხმოვნები (Vowels)
        'ა' to GeorgianPhoneme('ა', "ხმოვანი (ღია)", 14.2f, 0.2f),
        'ე' to GeorgianPhoneme('ე', "ხმოვანი (წინა)", 16.5f, 0.25f),
        'ი' to GeorgianPhoneme('ი', "ხმოვანი (დახურული)", 19.8f, 0.3f),
        'ო' to GeorgianPhoneme('ო', "ხმოვანი (უკანა)", 15.0f, 0.25f),
        'უ' to GeorgianPhoneme('უ', "ხმოვანი (ლაბიალიზებული)", 13.5f, 0.28f),

        // მკვეთრი / გლოტალური თანხმოვნები (High EMG / Glottal Ejectives)
        'ყ' to GeorgianPhoneme('ყ', "ხორხისმიერი მკვეთრი", 34.5f, 0.98f),
        'ჭ' to GeorgianPhoneme('ჭ', "შიშინა მკვეთრი (აფრიკატი)", 31.2f, 0.92f),
        'წ' to GeorgianPhoneme('წ', "სისინა მკვეთრი (აფრიკატი)", 29.8f, 0.88f),
        'კ' to GeorgianPhoneme('კ', "უკანაენისმიერი მკვეთრი", 28.4f, 0.86f),
        'ტ' to GeorgianPhoneme('ტ', "წინაენისმიერი მკვეთრი", 27.5f, 0.84f),
        'პ' to GeorgianPhoneme('პ', "ბაგისმიერი მკვეთრი", 26.0f, 0.82f),

        // ფშვინვიერები (Aspirated)
        'ქ' to GeorgianPhoneme('ქ', "უკანაენისმიერი ფშვინვიერი", 22.4f, 0.65f),
        'თ' to GeorgianPhoneme('თ', "წინაენისმიერი ფშვინვიერი", 21.8f, 0.62f),
        'ფ' to GeorgianPhoneme('ფ', "ბაგისმიერი ფშვინვიერი", 20.5f, 0.58f),
        'ც' to GeorgianPhoneme('ც', "სისინა ფშვინვიერი", 23.1f, 0.66f),
        'ჩ' to GeorgianPhoneme('ჩ', "შიშინა ფშვინვიერი", 24.2f, 0.70f),

        // მჟღერები (Voiced)
        'გ' to GeorgianPhoneme('გ', "უკანაენისმიერი მჟღერი", 18.2f, 0.50f),
        'დ' to GeorgianPhoneme('დ', "წინაენისმიერი მჟღერი", 17.8f, 0.48f),
        'ბ' to GeorgianPhoneme('ბ', "ბაგისმიერი მჟღერი", 16.9f, 0.45f),
        'ძ' to GeorgianPhoneme('ძ', "სისინა მჟღერი", 21.0f, 0.55f),
        'ჯ' to GeorgianPhoneme('ჯ', "შიშინა მჟღერი", 22.0f, 0.58f),

        // ნაპრალოვნები & ხორხისმიერები (Spirants & Fricatives)
        'ხ' to GeorgianPhoneme('ხ', "უკანაენისმიერი ნაპრალოვანი", 25.5f, 0.75f),
        'ღ' to GeorgianPhoneme('ღ', "უკანაენისმიერი მჟღერი ნაპრალოვანი", 20.8f, 0.60f),
        'შ' to GeorgianPhoneme('შ', "შიშინა ფშვინვიერი ნაპრალოვანი", 22.6f, 0.54f),
        'ჟ' to GeorgianPhoneme('ჟ', "შიშინა მჟღერი ნაპრალოვანი", 20.1f, 0.52f),
        'ს' to GeorgianPhoneme('ს', "სისინა ნაპრალოვანი", 21.4f, 0.50f),
        'ზ' to GeorgianPhoneme('ზ', "სისინა მჟღერი ნაპრალოვანი", 19.3f, 0.48f),
        'ჰ' to GeorgianPhoneme('ჰ', "ხორხისმიერი ნაპრალოვანი", 15.6f, 0.35f),

        // სონორები (Sonorants)
        'მ' to GeorgianPhoneme('მ', "ბაგისმიერი ნაზალი", 14.8f, 0.30f),
        'ნ' to GeorgianPhoneme('ნ', "წინაენისმიერი ნაზალი", 15.2f, 0.32f),
        'რ' to GeorgianPhoneme('რ', "წინაენისმიერი ვიბრანტი", 23.8f, 0.68f),
        'ლ' to GeorgianPhoneme('ლ', "წინაენისმიერი ლატერალი", 16.0f, 0.34f)
    )

    // Common Georgian Polymorphic & Polysynthetic Thoughts
    val GEORGIAN_THOUGHT_SAMPLES = listOf(
        "ახალი არქიტექტურული მოდელის ოპტიმიზაცია და კომპოუზის ინტერფეისის აჩქარება",
        "ნეირონული ქსელის ასოციაციური კავშირების ანალიზი და მეხსიერების განტვირთვა",
        "გადაწყვეტილების მიღების პროცესში ალტერნატიული გზების გაანალიზება",
        "კოდის სტრუქტურის გამარტივება და ასინქრონული კორუტინების ოპტიმიზაცია",
        "სუბვოკალური შინაგანი მეტყველების დეკოდირება და ტელეპათიური შეყვანა",
        "კოგნიტური დაღლილობის შემცირება და ალფა-ტალღების სინქრონიზაცია",
        "შემდეგი ლოგიკური ნაბიჯის პროგნოზირება ეკრანზე შეხებამდე"
    )

    val GEORGIAN_GHOST_TYPING_CORPUS = listOf(
        Pair("fun შევამოწმოთკოგნიტური", "fun შევამოწმოთკოგნიტურისინქრონიზაცია(ტალღა: FloatArray): Boolean { ... }"),
        Pair("მონაცემთა ბაზაში შევინახოთ", "მონაცემთა ბაზაში შევინახოთ დეკოდირებული აზრების ქრონოლოგია"),
        Pair("დავაკონფიგურიროთ კამერის", "დავაკონფიგურიროთ კამერის მზერის ტრეკერი და თვალის ხამხამის სენსორი"),
        Pair("გავაანალიზოთ ტვინის", "გავაანალიზოთ ტვინის ალფა და ბეტა ტალღების თანაფარდობა")
    )

    // Direct Word Lexicon Categories for Mind Word Decoding
    data class MindLexiconEntry(
        val word: String,
        val category: String, // COMMON, DEV, COMMANDS, EMOTIONS, ENGLISH
        val language: String, // GEORGIAN, ENGLISH
        val emgFrequencyHz: Float,
        val phonemes: List<String>,
        val description: String
    )

    val MIND_LEXICON_DATABASE = listOf(
        // COMMON (ყოველდღიური & სწრაფი პასუხები)
        MindLexiconEntry("გამარჯობა", "COMMON", "GEORGIAN", 142.5f, listOf("გ", "ა", "მ", "ა", "რ", "ჯ", "ო", "ბ", "ა"), "სალამი / მისალმება"),
        MindLexiconEntry("მადლობა", "COMMON", "GEORGIAN", 128.0f, listOf("მ", "ა", "დ", "ლ", "ო", "ბ", "ა"), "მადლიერების გამოხატვა"),
        MindLexiconEntry("დიახ", "COMMON", "GEORGIAN", 115.2f, listOf("დ", "ი", "ა", "ხ"), "დადებითი თანხმობა"),
        MindLexiconEntry("არა", "COMMON", "GEORGIAN", 98.4f, listOf("ა", "რ", "ა"), "უარყოფა"),
        MindLexiconEntry("კარგი", "COMMON", "GEORGIAN", 132.0f, listOf("კ", "ა", "რ", "გ", "ი"), "თანხმობა / მოწონება"),
        MindLexiconEntry("გასაგებია", "COMMON", "GEORGIAN", 138.6f, listOf("გ", "ა", "ს", "ა", "გ", "ე", "ბ", "ი", "ა"), "გაგება / აღქმა"),
        MindLexiconEntry("შესვენება", "COMMON", "GEORGIAN", 145.2f, listOf("შ", "ე", "ს", "ვ", "ე", "ნ", "ე", "ბ", "ა"), "დასვენების მოთხოვნა"),
        MindLexiconEntry("ყავა", "COMMON", "GEORGIAN", 168.0f, listOf("ყ", "ა", "ვ", "ა"), "ყავის სურვილი (ხორხისმიერი ყ)"),
        MindLexiconEntry("წყალი", "COMMON", "GEORGIAN", 162.4f, listOf("წ", "ყ", "ა", "ლ", "ი"), "წყურვილის რეფლექსი"),
        MindLexiconEntry("დახმარება", "COMMON", "GEORGIAN", 135.0f, listOf("დ", "ა", "ხ", "მ", "ა", "რ", "ე", "ბ", "ა"), "მხარდაჭერის მოთხოვნა"),

        // DEV (დეველოპმენტი & IT)
        MindLexiconEntry("კოდის რეფაქტორინგი", "DEV", "GEORGIAN", 158.4f, listOf("კ", "ო", "დ", "ი", "ს", " ", "რ", "ე", "ფ", "ა", "ქ", "ტ", "ო", "რ", "ი", "ნ", "გ", "ი"), "კოდის სტრუქტურული გაუმჯობესება"),
        MindLexiconEntry("Git Push", "DEV", "ENGLISH", 148.0f, listOf("G", "i", "t", " ", "P", "u", "s", "h"), "ცვლილებების ატვირთვა GitHub-ზე"),
        MindLexiconEntry("Build APK", "DEV", "ENGLISH", 152.0f, listOf("B", "u", "i", "l", "d", " ", "A", "P", "K"), "აპლიკაციის აწყობა და ექსპორტი"),
        MindLexiconEntry("Jetpack Compose", "DEV", "ENGLISH", 144.5f, listOf("J", "e", "t", "p", "a", "c", "k", " ", "C", "o", "m", "p", "o", "s", "e"), "დეკლარაციული UI"),
        MindLexiconEntry("UI ოპტიმიზაცია", "DEV", "GEORGIAN", 150.2f, listOf("U", "I", " ", "ო", "პ", "ტ", "ი", "მ", "ი", "ზ", "ა", "ც", "ი", "ა"), "ინტერფეისის სისწრაფის გაზრდა"),
        MindLexiconEntry("Debug შეცდომა", "DEV", "GEORGIAN", 146.0f, listOf("D", "e", "b", "u", "g", " ", "შ", "ე", "ც", "დ", "ო", "მ", "ა"), "სინტაქსური ან ლოგიკური ხარვეზის ძებნა"),
        MindLexiconEntry("Coroutine Async", "DEV", "ENGLISH", 139.0f, listOf("C", "o", "r", "o", "u", "t", "i", "n", "e"), "ასინქრონული ნაკადები"),

        // COMMANDS (მოქმედებები & ბრძანებები)
        MindLexiconEntry("მუსიკის ჩართვა", "COMMANDS", "GEORGIAN", 136.5f, listOf("მ", "უ", "ს", "ი", "კ", "ი", "ს", " ", "ჩ", "ა", "რ", "თ", "ვ", "ა"), "აუდიო ფლეიერის გააქტიურება"),
        MindLexiconEntry("ფოკუსის რეჟიმი", "COMMANDS", "GEORGIAN", 154.0f, listOf("ფ", "ო", "კ", "უ", "ს", "ი", "ს", " ", "რ", "ე", "ჟ", "ი", "მ", "ი"), "Do Not Disturb და ალფა-ბიტები"),
        MindLexiconEntry("ეკრანის ჩაბნელება", "COMMANDS", "GEORGIAN", 140.0f, listOf("ე", "კ", "რ", "ა", "ნ", "ი", "ს", " ", "ჩ", "ა", "ბ", "ნ", "ე", "ლ", "ე", "ბ", "ა"), "სიკაშკაშის შემცირება და ენერგიის დაზოგვა"),
        MindLexiconEntry("შეტყობინების გაგზავნა", "COMMANDS", "GEORGIAN", 143.2f, listOf("შ", "ე", "ტ", "ყ", "ო", "ბ", "ი", "ნ", "ე", "ბ", "ა"), "მესენჯერის გააქტიურება"),
        MindLexiconEntry("ტაიმერის დაყენება", "COMMANDS", "GEORGIAN", 137.8f, listOf("ტ", "ა", "ი", "მ", "ე", "რ", "ი"), "25 წუთიანი პომოდორო"),

        // EMOTIONS (აზრები & ემოციური მდგომარეობა)
        MindLexiconEntry("ღრმა კონცენტრაცია", "EMOTIONS", "GEORGIAN", 156.0f, listOf("ღ", "რ", "მ", "ა", " ", "კ", "ო", "ნ", "ც", "ე", "ნ", "ტ", "რ", "ა", "ც", "ი", "ა"), "100% ჩართულობა ამოცანაში"),
        MindLexiconEntry("დაღლილობა", "EMOTIONS", "GEORGIAN", 124.0f, listOf("დ", "ა", "ღ", "ლ", "ი", "ლ", "ო", "ბ", "ა"), "მენტალური ენერგიის კლება"),
        MindLexiconEntry("ახალი იდეა", "EMOTIONS", "GEORGIAN", 164.0f, listOf("ა", "ხ", "ა", "ლ", "ი", " ", "ი", "დ", "ე", "ა"), "ინტუიციური გამა-პიკი"),
        MindLexiconEntry("სიმშვიდე", "EMOTIONS", "GEORGIAN", 118.5f, listOf("ს", "ი", "მ", "შ", "ვ", "ი", "დ", "ე"), "დაბალი სტრესი და ჰარმონია"),

        // ENGLISH
        MindLexiconEntry("Hello World", "ENGLISH", "ENGLISH", 134.0f, listOf("H", "e", "l", "l", "o", " ", "W", "o", "r", "l", "d"), "Greeting in English"),
        MindLexiconEntry("Deep Focus", "ENGLISH", "ENGLISH", 148.0f, listOf("D", "e", "e", "p", " ", "F", "o", "c", "u", "s"), "High mental focus state"),
        MindLexiconEntry("Optimize Engine", "ENGLISH", "ENGLISH", 155.0f, listOf("O", "p", "t", "i", "m", "i", "z", "e"), "Speed up calculations")
    )

    fun getPhonemesForString(input: String): List<String> {
        return input.map { char ->
            if (char.isWhitespace()) "␣" else char.toString()
        }
    }
}
