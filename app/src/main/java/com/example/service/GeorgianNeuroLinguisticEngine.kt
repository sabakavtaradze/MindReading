package com.example.service

/**
 * Georgian Neural Linguistic Engine & Lexical Matrix
 * Comprehensive morphological analyzer, sub-vocal phonetics decoder,
 * polysynthetic verb generator, consonant cluster decimator, and Markov semantic bridge.
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
        'ა' to GeorgianPhoneme('ა', "ხმოვანი (ღია)", 14.2f, 0.20f),
        'ე' to GeorgianPhoneme('ე', "ხმოვანი (წინა)", 16.5f, 0.25f),
        'ი' to GeorgianPhoneme('ი', "ხმოვანი (დახურული)", 19.8f, 0.30f),
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

    // Direct Word Lexicon Categories for Mind Word Decoding
    data class MindLexiconEntry(
        val word: String,
        val category: String, // COMMON, DEV, COMMANDS, EMOTIONS, NEURO_SCIENCE, MORPHOLOGY_VERBS, SMART_HOME, ENGLISH
        val language: String, // GEORGIAN, ENGLISH
        val emgFrequencyHz: Float,
        val phonemes: List<String>,
        val description: String,
        val rootStem: String = "",
        val typicalNextWords: List<String> = emptyList(),
        val clusterSpeedupGainPct: Int = 35
    )

    // Polysynthetic Georgian Morphological Root Template
    data class GeorgianVerbMorphology(
        val root: String,
        val meaning: String,
        val forms: List<String>,
        val preverbs: List<String> = listOf("შე-", "გა-", "და-", "მო-", "გადა-", "ჩა-", "ამო-", "გან-")
    )

    val GEORGIAN_VERB_ROOTS = listOf(
        GeorgianVerbMorphology("მოწმ", "შემოწმება / ვალიდაცია", listOf("შევამოწმოთ", "ამოწმებს", "შემოწმებული", "გადაამოწმეთ", "მოვამოწმებ")),
        GeorgianVerbMorphology("კომიტ", "კოდის ფიქსაცია / Git", listOf("დავაკომიტოთ", "დააკომიტა", "დაკომიტებული", "გადავაკომიტებთ")),
        GeorgianVerbMorphology("სინთეზ", "აზრების შეერთება", listOf("დავასინთეზოთ", "სინთეზირებს", "სინთეზირებული", "გავასინთეზებთ")),
        GeorgianVerbMorphology("ანალიზ", "სიღრმისეული კვლევა", listOf("გავაანალიზოთ", "ანალიზდება", "გაანალიზებული", "ჩავაანალიზოთ")),
        GeorgianVerbMorphology("წერ", "კოდის / ტექსტის გენერაცია", listOf("დავწეროთ", "წერს", "ჩაწერილი", "გადავწეროთ", "გამოვწეროთ")),
        GeorgianVerbMorphology("გზავნ", "ინფორმაციის გაცვლა", listOf("გავაგზავნოთ", "გზავნის", "გაგზავნილი", "ჩავაგზავნოთ")),
        GeorgianVerbMorphology("რთავ", "მოწყობილობის აქტივაცია", listOf("ჩავრთოთ", "რთავს", "ჩართული", "გადავრთოთ")),
        GeorgianVerbMorphology("თიშ", "პროცესის შეწყვეტა", listOf("გავთიშოთ", "თიშავს", "გათიშული", "ჩავთიშოთ")),
        GeorgianVerbMorphology("ფარცხ", "დავარცხნა / გასუფთავება", listOf("ვფარცხავთ", "გავფარცხოთ", "დავფარცხოთ")),
        GeorgianVerbMorphology("წვრთნ", "მოდელის სწავლება", listOf("მწვრთნელი", "გავწვრთნათ", "ვწვრთნით", "გაწვრთნილი")),
        GeorgianVerbMorphology("ფრცქვნ", "გაფცქვნა / დეკოდირება", listOf("გვფრცქვნი", "გავფრცქვნათ", "ვფრცქვნით"))
    )

    // Comprehensive 130+ Georgian & Technical Lexicon Database
    val MIND_LEXICON_DATABASE = listOf(
        // ==========================================
        // 1. COMMON (ყოველდღიური & სწრაფი პასუხები)
        // ==========================================
        MindLexiconEntry("გამარჯობა", "COMMON", "GEORGIAN", 142.5f, listOf("გ", "ა", "მ", "ა", "რ", "ჯ", "ო", "ბ", "ა"), "სალამი / მისალმება", "მარჯვ", listOf("მეგობარო", "როგორ ხარ", "ყველას")),
        MindLexiconEntry("მადლობა", "COMMON", "GEORGIAN", 128.0f, listOf("მ", "ა", "დ", "ლ", "ო", "ბ", "ა"), "მადლიერების გამოხატვა", "მადლ", listOf("დიდი", "დახმარებისთვის", "ყურადღებისთვის")),
        MindLexiconEntry("დიახ", "COMMON", "GEORGIAN", 115.2f, listOf("დ", "ი", "ა", "ხ"), "დადებითი თანხმობა", "", listOf("რა თქმა უნდა", "ზუსტად", "გასაგებია")),
        MindLexiconEntry("არა", "COMMON", "GEORGIAN", 98.4f, listOf("ა", "რ", "ა"), "უარყოფა / შეჩერება", "", listOf("მადლობა", "არ მინდა", "შეცდომაა")),
        MindLexiconEntry("კარგი", "COMMON", "GEORGIAN", 132.0f, listOf("კ", "ა", "რ", "გ", "ი"), "თანხმობა / მოწონება", "კარგ", listOf("იდეაა", "გავაგრძელოთ", "შევთანხმდით")),
        MindLexiconEntry("გასაგებია", "COMMON", "GEORGIAN", 138.6f, listOf("გ", "ა", "ს", "ა", "გ", "ე", "ბ", "ი", "ა"), "გაგება / აღქმა", "გაგ", listOf("სრულად", "ყველაფერი", "მადლობა")),
        MindLexiconEntry("რა თქმა უნდა", "COMMON", "GEORGIAN", 146.0f, listOf("რ", "ა", " ", "თ", "ქ", "მ", "ა"), "მტკიცე თანხმობა", "თქმ", listOf("გავაკეთოთ", "დავიწყოთ")),
        MindLexiconEntry("როგორ ხარ", "COMMON", "GEORGIAN", 134.5f, listOf("რ", "ო", "გ", "ო", "რ"), "კითხვა განწყობაზე", "", listOf("დღეს", "მეგობარო")),
        MindLexiconEntry("ნახვამდის", "COMMON", "GEORGIAN", 140.2f, listOf("ნ", "ა", "ხ", "ვ", "ა", "მ", "დ", "ი", "ს"), "დამშვიდობება", "ნახვ", listOf("დროებით", "მალე შევხვდებით")),
        MindLexiconEntry("მოგესალმებით", "COMMON", "GEORGIAN", 148.0f, listOf("მ", "ო", "გ", "ე", "ს", "ა", "ლ", "მ", "ე", "ბ", "ი", "თ"), "ოფიციალური მისალმება", "სალამ", listOf("კოლეგებო", "ყველას")),
        MindLexiconEntry("გთხოვთ", "COMMON", "GEORGIAN", 126.4f, listOf("გ", "თ", "ხ", "ო", "ვ", "თ"), "თავაზიანი თხოვნა", "თხოვნ", listOf("დამელოდოთ", "გააგრძელოთ")),
        MindLexiconEntry("ბოდიში", "COMMON", "GEORGIAN", 122.0f, listOf("ბ", "ო", "დ", "ი", "შ", "ი"), "ბოდიშის მოხდა", "ბოდიშ", listOf("დაგვიანებისთვის", "შეცდომისთვის")),
        MindLexiconEntry("შესანიშნავია", "COMMON", "GEORGIAN", 152.0f, listOf("შ", "ე", "ს", "ა", "ნ", "ი", "შ", "ნ", "ა", "ვ", "ი", "ა"), "აღფრთოვანება", "ნიშან", listOf("შედეგი", "ნამუშევარი")),
        MindLexiconEntry("აუცილებლად", "COMMON", "GEORGIAN", 144.0f, listOf("ა", "უ", "ც", "ი", "ლ", "ე", "ბ", "ლ", "ა", "დ"), "გარანტირებული მოქმედება", "ვალ", listOf("შევამოწმოთ", "გავაკეთებთ")),
        MindLexiconEntry("ახლავე", "COMMON", "GEORGIAN", 136.0f, listOf("ა", "ხ", "ლ", "ა", "ვ", "ე"), "მყისიერი მოქმედება", "", listOf("მოვრჩები", "დავიწყებ", "გავაკეთებ")),
        MindLexiconEntry("დროულად", "COMMON", "GEORGIAN", 130.5f, listOf("დ", "რ", "ო", "უ", "ლ", "ა", "დ"), "პუნქტუალურობა", "დრო", listOf("დავასრულეთ", "მოვედით")),
        MindLexiconEntry("შევხვდებით", "COMMON", "GEORGIAN", 141.0f, listOf("შ", "ე", "ვ", "ხ", "ვ", "დ", "ე", "ბ", "ი", "თ"), "შეხვედრის დაგეგმვა", "ხვედრ", listOf("ხვალ", "ოფისში", "ონლაინ")),
        MindLexiconEntry("წარმატებები", "COMMON", "GEORGIAN", 147.2f, listOf("წ", "ა", "რ", "მ", "ა", "ტ", "ე", "ბ", "ე", "ბ", "ი"), "კეთილი სურვილი", "მატ", listOf("პროექტში", "დღეს")),
        MindLexiconEntry("შესვენება", "COMMON", "GEORGIAN", 145.2f, listOf("შ", "ე", "ს", "ვ", "ე", "ნ", "ე", "ბ", "ა"), "დასვენების მოთხოვნა", "სვენ", listOf("გვჭირდება", "5 წუთით")),
        MindLexiconEntry("ყავა", "COMMON", "GEORGIAN", 168.0f, listOf("ყ", "ა", "ვ", "ა"), "ყავის სურვილი (ხორხისმიერი ყ)", "", listOf("მინდა", "შესვენებაზე", "ესპრესო"), 65),
        MindLexiconEntry("წყალი", "COMMON", "GEORGIAN", 162.4f, listOf("წ", "ყ", "ა", "ლ", "ი"), "წყურვილის რეფლექსი", "", listOf("გრილი", "დავლევ"), 72),
        MindLexiconEntry("ჩაი", "COMMON", "GEORGIAN", 135.0f, listOf("ჩ", "ა", "ი"), "თბილი სასმელი", "", listOf("მწვანე", "ლიმონით")),
        MindLexiconEntry("დახმარება", "COMMON", "GEORGIAN", 135.0f, listOf("დ", "ა", "ხ", "მ", "ა", "რ", "ე", "ბ", "ა"), "მხარდაჭერის მოთხოვნა", "ხმარ", listOf("მჭირდება", "კოლეგისგან")),

        // ==========================================
        // 2. DEV (დეველოპმენტი, არქიტექტურა & IT)
        // ==========================================
        MindLexiconEntry("კოდის რეფაქტორინგი", "DEV", "GEORGIAN", 158.4f, listOf("კ", "ო", "დ", "ი", "ს", " ", "რ", "ე", "ფ", "ა", "ქ", "ტ", "ო", "რ", "ი", "ნ", "გ", "ი"), "კოდის სტრუქტურული გაუმჯობესება", "ფაქტორ", listOf("დავასრულოთ", "აუცილებელია")),
        MindLexiconEntry("არქიტექტურა", "DEV", "GEORGIAN", 154.2f, listOf("ა", "რ", "ქ", "ი", "ტ", "ე", "ქ", "ტ", "უ", "რ", "ა"), "სისტემური აგებულება", "", listOf("მყარია", "გავმართოთ", "შევამოწმოთ")),
        MindLexiconEntry("კომპილაცია", "DEV", "GEORGIAN", 149.0f, listOf("კ", "ო", "მ", "პ", "ი", "ლ", "ა", "ც", "ი", "ა"), "პროექტის ბილდი", "", listOf("წარმატებით დასრულდა", "გავუშვათ")),
        MindLexiconEntry("დებაგინგი", "DEV", "GEORGIAN", 146.5f, listOf("დ", "ე", "ბ", "ა", "გ", "ი", "ნ", "გ", "ი"), "შეცდომების ძებნა და აღმოფხვრა", "", listOf("ლოგებში", "პროცესშია")),
        MindLexiconEntry("ალგორითმი", "DEV", "GEORGIAN", 151.0f, listOf("ა", "ლ", "გ", "ო", "რ", "ი", "თ", "მ", "ი"), "ლოგიკური გამოთვლების წყება", "", listOf("ოპტიმალურია", "დავხვეწოთ")),
        MindLexiconEntry("მონაცემთა ბაზა", "DEV", "GEORGIAN", 143.0f, listOf("მ", "ო", "ნ", "ა", "ც", "ე", "მ", "თ", "ა"), "Room / SQLite / Cloud storage", "ცემ", listOf("სინქრონიზებულია", "შევინახოთ")),
        MindLexiconEntry("ასინქრონული ნაკადი", "DEV", "GEORGIAN", 156.8f, listOf("ა", "ს", "ი", "ნ", "ქ", "რ", "ო", "ნ", "უ", "ლ", "ი"), "Coroutines & Flow", "", listOf("მუშაობს", "არ ბლოკავს")),
        MindLexiconEntry("ნეირონული ქსელი", "DEV", "GEORGIAN", 160.0f, listOf("ნ", "ე", "ი", "რ", "ო", "ნ", "უ", "ლ", "ი"), "AI / Deep Learning", "", listOf("სწავლობს", "დეკოდირებს")),
        MindLexiconEntry("ფუნქცია", "DEV", "GEORGIAN", 138.0f, listOf("ფ", "უ", "ნ", "ქ", "ც", "ი", "ა"), "კოდის ლოგიკური ბლოკი", "", listOf("გამოვიძახოთ", "გავმართოთ")),
        MindLexiconEntry("ცვლადი", "DEV", "GEORGIAN", 132.5f, listOf("ც", "ვ", "ლ", "ა", "დ", "ი"), "მეხსიერების მდგომარეობა", "ცვლ", listOf("განვაახლოთ", "მუდმივია")),
        MindLexiconEntry("ინტერფეისი", "DEV", "GEORGIAN", 144.0f, listOf("ი", "ნ", "ტ", "ე", "რ", "ფ", "ე", "ი", "ს", "ი"), "Jetpack Compose UI", "", listOf("სწრაფია", "რესპონსიულია")),
        MindLexiconEntry("რეპოზიტორია", "DEV", "GEORGIAN", 147.0f, listOf("რ", "ე", "პ", "ო", "ზ", "ი", "ტ", "ო", "რ", "ი", "ა"), "Data layer / Git source", "", listOf("მზადაა", "გავასუფთაოთ")),
        MindLexiconEntry("ოპტიმიზაცია", "DEV", "GEORGIAN", 153.2f, listOf("ო", "პ", "ტ", "ი", "მ", "ი", "ზ", "ა", "ც", "ი", "ა"), "სისწრაფის და მეხსიერების გაუმჯობესება", "", listOf("წარმატებულია", "დავიწყოთ")),
        MindLexiconEntry("მეხსიერების მართვა", "DEV", "GEORGIAN", 148.5f, listOf("მ", "ე", "ხ", "ს", "ი", "ე", "რ", "ე", "ბ", "ა"), "Garbage Collection & RAM", "ხსოვნ", listOf("ოპტიმალურია", "გავათავისუფლოთ")),
        MindLexiconEntry("ქეშირება", "DEV", "GEORGIAN", 139.0f, listOf("ქ", "ე", "შ", "ი", "რ", "ე", "ბ", "ა"), "მონაცემთა ლოკალური შენახვა", "", listOf("გავააქტიუროთ", "სწრაფი წვდომა")),
        MindLexiconEntry("სინქრონიზაცია", "DEV", "GEORGIAN", 155.0f, listOf("ს", "ი", "ნ", "ქ", "რ", "ო", "ნ", "ი", "ზ", "ა", "ც", "ი", "ა"), "მონაცემთა სრული თანხვედრა", "", listOf("დასრულდა", "უწყვეტია")),
        MindLexiconEntry("ტესტირება", "DEV", "GEORGIAN", 136.0f, listOf("ტ", "ე", "ს", "ტ", "ი", "რ", "ე", "ბ", "ა"), "Unit & UI Verification", "", listOf("მწვანეა", "გავიარეთ")),
        MindLexiconEntry("დეპლოი", "DEV", "GEORGIAN", 142.0f, listOf("დ", "ე", "პ", "ლ", "ო", "ი"), "პროდუქციაში გაშვება", "", listOf("წარმატებით შესრულდა", "მზადაა")),
        MindLexiconEntry("Git Push", "DEV", "ENGLISH", 148.0f, listOf("G", "i", "t", " ", "P", "u", "s", "h"), "ცვლილებების ატვირთვა GitHub-ზე", "", listOf("origin main", "completed")),
        MindLexiconEntry("Build APK", "DEV", "ENGLISH", 152.0f, listOf("B", "u", "i", "l", "d", " ", "A", "P", "K"), "აპლიკაციის აწყობა და ექსპორტი", "", listOf("release", "success")),
        MindLexiconEntry("Jetpack Compose", "DEV", "ENGLISH", 144.5f, listOf("J", "e", "t", "p", "a", "c", "k"), "თანამედროვე დეკლარაციული UI", "", listOf("Material 3", "State")),
        MindLexiconEntry("Kotlin Coroutines", "DEV", "ENGLISH", 150.0f, listOf("K", "o", "t", "l", "i", "n"), "ასინქრონული პროგრამირება", "", listOf("Dispatcher.IO", "Flow")),

        // ==========================================
        // 3. COMMANDS (ჭკვიანი მოქმედებები & სისტემა)
        // ==========================================
        MindLexiconEntry("ფოკუსის რეჟიმი", "COMMANDS", "GEORGIAN", 154.0f, listOf("ფ", "ო", "კ", "უ", "ს", "ი"), "Do Not Disturb და ალფა-ბიტები", "", listOf("ჩაირთო", "გააქტიურებულია")),
        MindLexiconEntry("მუსიკის ჩართვა", "COMMANDS", "GEORGIAN", 136.5f, listOf("მ", "უ", "ს", "ი", "კ", "ა"), "აუდიო ფლეიერის გააქტიურება", "", listOf("ფლეილისტი", "მშვიდი ტალღები")),
        MindLexiconEntry("ხმის მომატება", "COMMANDS", "GEORGIAN", 138.0f, listOf("ხ", "მ", "ა"), "აუდიო დონის გაზრდა", "", listOf("+20%", "მაქსიმუმი")),
        MindLexiconEntry("ხმის დაწევა", "COMMANDS", "GEORGIAN", 132.0f, listOf("ხ", "მ", "ა"), "აუდიო დონის შემცირება", "", listOf("მინიმუმი", "დადუმება")),
        MindLexiconEntry("შეტყობინების გაგზავნა", "COMMANDS", "GEORGIAN", 143.2f, listOf("შ", "ე", "ტ", "ყ", "ო", "ბ", "ი", "ნ", "ე", "ბ", "ა"), "მესენჯერის გააქტიურება", "ტყობ", listOf("კონტაქტთან", "მზადაა")),
        MindLexiconEntry("ტაიმერის დაყენება", "COMMANDS", "GEORGIAN", 137.8f, listOf("ტ", "ა", "ი", "მ", "ე", "რ", "ი"), "25 წუთიანი პომოდორო", "", listOf("25 წუთი", "დაწყებულია")),
        MindLexiconEntry("ეკრანის დაბლოკვა", "COMMANDS", "GEORGIAN", 140.0f, listOf("ე", "კ", "რ", "ა", "ნ", "ი"), "უსაფრთხოების რეჟიმი", "", listOf("მყისიერად", "შენახულია")),
        MindLexiconEntry("სიკაშკაშის შეცვლა", "COMMANDS", "GEORGIAN", 145.0f, listOf("ს", "ი", "კ", "ა", "შ", "კ", "ა", "შ", "ე"), "დისპლეის განათების კორექცია", "", listOf("ავტო-რეჟიმი", "დაწევა")),
        MindLexiconEntry("აპლიკაციის გახსნა", "COMMANDS", "GEORGIAN", 141.5f, listOf("გ", "ა", "ხ", "ს", "ნ", "ა"), "პროგრამის გაშვება", "ხსნ", listOf("ტერმინალი", "ბრაუზერი")),
        MindLexiconEntry("ძებნა", "COMMANDS", "GEORGIAN", 134.0f, listOf("ძ", "ე", "ბ", "ნ", "ა"), "ფაილის ან ინფორმაციის მოძიება", "ძებნ", listOf("სისტემაში", "ინტერნეტში")),
        MindLexiconEntry("ბატარეის დაზოგვა", "COMMANDS", "GEORGIAN", 146.0f, listOf("ბ", "ა", "ტ", "ა", "რ", "ე", "ა"), "ენერგიის ეკონომია", "", listOf("გააქტიურდა", "ეკო რეჟიმი")),
        MindLexiconEntry("სქრინშოტი", "COMMANDS", "GEORGIAN", 139.5f, listOf("ს", "ქ", "რ", "ი", "ნ", "ი"), "ეკრანის გადაღება", "", listOf("შენახულია", "გალერეაში")),
        MindLexiconEntry("მონაცემთა ექსპორტი", "COMMANDS", "GEORGIAN", 152.5f, listOf("ე", "ქ", "ს", "პ", "ო", "რ", "ტ", "ი"), "JSON / CSV ფაილის გენერაცია", "", listOf("დასრულდა", "მზადაა")),

        // ==========================================
        // 4. EMOTIONS (აზრები & ემოციური მდგომარეობა)
        // ==========================================
        MindLexiconEntry("ღრმა კონცენტრაცია", "EMOTIONS", "GEORGIAN", 156.0f, listOf("ღ", "რ", "მ", "ა"), "100% ჩართულობა ამოცანაში", "", listOf("მიღწეულია", "Deep Work"), 60),
        MindLexiconEntry("სიმშვიდე", "EMOTIONS", "GEORGIAN", 118.5f, listOf("ს", "ი", "მ", "შ", "ვ", "ი", "დ", "ე"), "დაბალი სტრესი და ჰარმონია", "მშვიდ", listOf("სრული", "მედიტაციური")),
        MindLexiconEntry("ახალი იდეა", "EMOTIONS", "GEORGIAN", 164.0f, listOf("ი", "დ", "ე", "ა"), "ინტუიციური გამა-პიკი", "", listOf("დავაფიქსიროთ", "ბრწყინვალეა")),
        MindLexiconEntry("დაღლილობა", "EMOTIONS", "GEORGIAN", 124.0f, listOf("დ", "ა", "ღ", "ლ", "ა"), "მენტალური ენერგიის კლება", "ღლ", listOf("იგრძნობა", "შესვენება გვინდა")),
        MindLexiconEntry("სიცხადე", "EMOTIONS", "GEORGIAN", 135.0f, listOf("ც", "ხ", "ა", "დ"), "მკაფიო ხედვა და ლოგიკა", "ცხად", listOf("აზროვნებაში", "სრული")),
        MindLexiconEntry("შემოქმედებითი აღმაფრენა", "EMOTIONS", "GEORGIAN", 158.0f, listOf("შ", "ე", "მ", "ო", "ქ", "მ", "ე", "დ"), "Creative Flow & Inspiration", "ქმნ", listOf("პიკზეა", "დავიწყოთ")),
        MindLexiconEntry("ანალიტიკური აზროვნება", "EMOTIONS", "GEORGIAN", 152.0f, listOf("ა", "ნ", "ა", "ლ", "ი", "ზ"), "ზუსტი ლოგიკური დეკომპოზიცია", "", listOf("აქტიურია", "ფოკუსში")),
        MindLexiconEntry("ენთუზიაზმი", "EMOTIONS", "GEORGIAN", 149.0f, listOf("ე", "ნ", "თ", "უ", "ზ", "ი", "ა", "ზ", "მ"), "მაღალი ენერგია და მოტივაცია", "", listOf("უდიდესია", "გავიმარჯვებთ")),
        MindLexiconEntry("მოდუნება", "EMOTIONS", "GEORGIAN", 121.0f, listOf("დ", "უ", "ნ"), "დაძაბულობის მოხსნა", "დუნ", listOf("სასარგებლოა", "ნელ-ნელა")),
        MindLexiconEntry("შთაგონება", "EMOTIONS", "GEORGIAN", 146.5f, listOf("გ", "ო", "ნ"), "მუზა და ინსაითი", "გონ", listOf("მოვიდა", "ვქმნით")),
        MindLexiconEntry("გადაწყვეტილება", "EMOTIONS", "GEORGIAN", 143.0f, listOf("წ", "ყ", "ვ", "ე", "ტ"), "მყარი არჩევანი", "წყვეტ", listOf("მიღებულია", "საბოლოოა"), 68),
        MindLexiconEntry("ეჭვი", "EMOTIONS", "GEORGIAN", 128.0f, listOf("ე", "ჭ", "ვ"), "გადამოწმების სურვილი", "ეჭვ", listOf("არ მეპარება", "გადავამოწმოთ"), 55),
        MindLexiconEntry("სიხარული", "EMOTIONS", "GEORGIAN", 147.0f, listOf("ხ", "ა", "რ"), "დადებითი ემოციური მუხტი", "ხარ", listOf("წარმატების გამო", "გულწრფელი")),

        // ==========================================
        // 5. NEURO_SCIENCE (ნეირობიოლოგია & ტელემეტრია)
        // ==========================================
        MindLexiconEntry("სუბვოკალური მეტყველება", "NEURO_SCIENCE", "GEORGIAN", 162.0f, listOf("ს", "უ", "ბ", "ვ", "ო", "კ", "ა", "ლ"), "შინაგანი მეტყველების დეკოდირება", "", listOf("დეკოდირებულია", "აქტიურია")),
        MindLexiconEntry("სინაფსური კავშირი", "NEURO_SCIENCE", "GEORGIAN", 158.0f, listOf("ს", "ი", "ნ", "ა", "ფ", "ს"), "ნეირონებს შორის იმპულსის გადაცემა", "", listOf("მყარდება", "პლასტიკურია")),
        MindLexiconEntry("მიკრო-საკადა", "NEURO_SCIENCE", "GEORGIAN", 165.0f, listOf("ს", "ა", "კ", "ა", "დ"), "თვალის მიკრო-ნახტომები (-92ms)", "", listOf("დაფიქსირდა", "წინასწარმეტყველებს"), 58),
        MindLexiconEntry("ალფა ტალღები", "NEURO_SCIENCE", "GEORGIAN", 138.0f, listOf("ა", "ლ", "ფ", "ა"), "8-12 Hz მშვიდი ფოკუსი", "", listOf("დომინირებს", "სინქრონშია")),
        MindLexiconEntry("ბეტა რიტმი", "NEURO_SCIENCE", "GEORGIAN", 148.0f, listOf("ბ", "ე", "ტ", "ა"), "13-30 Hz აქტიური აზროვნება", "", listOf("მაღალია", "კოგნიტური დატვირთვა")),
        MindLexiconEntry("ტეტა ტალღა", "NEURO_SCIENCE", "GEORGIAN", 130.0f, listOf("ტ", "ე", "ტ", "ა"), "4-7 Hz ღრმა ინტუიცია & მეხსიერება", "", listOf("გაძლიერდა", "ინსაითი")),
        MindLexiconEntry("გუგის დილატაცია", "NEURO_SCIENCE", "GEORGIAN", 154.0f, listOf("დ", "ი", "ლ", "ა", "ტ", "ა", "ც"), "Pupillometry (3.85mm)", "", listOf("გაზომილია", "ყურადღების პიკი")),
        MindLexiconEntry("გულისცემის ვარიაბელობა", "NEURO_SCIENCE", "GEORGIAN", 146.0f, listOf("ვ", "ა", "რ", "ი", "ა", "ბ"), "HRV RMSSD სტრესის ინდექსი", "", listOf("58.4ms", "ოპტიმალური")),
        MindLexiconEntry("ელექტრომიოგრამა", "NEURO_SCIENCE", "GEORGIAN", 159.0f, listOf("ე", "მ", "გ"), "ხორხის კუნთების EMG სიგნალი", "", listOf("142Hz", "სუფთა სიგნალი")),
        MindLexiconEntry("ენცეფალოგრამა", "NEURO_SCIENCE", "GEORGIAN", 161.0f, listOf("ე", "ე", "გ"), "ტვინის ელექტრული აქტივობა", "", listOf("მრავალარხიანი", "სტაბილური")),
        MindLexiconEntry("ხორხის რეზონანსი", "NEURO_SCIENCE", "GEORGIAN", 166.0f, listOf("რ", "ე", "ზ", "ო", "ნ", "ა", "ნ", "ს"), "VPU ძვლოვანი გამტარობა", "", listOf("იდენტიფიცირებულია", "ზუსტი")),
        MindLexiconEntry("ბიო-უკუკავშირი", "NEURO_SCIENCE", "GEORGIAN", 152.0f, listOf("ბ", "ი", "ო"), "Real-time Neuro-Feedback", "", listOf("მუშაობს", "თვითრეგულაცია")),

        // ==========================================
        // 6. MORPHOLOGY_VERBS (რთული ქართული ზმნები & თანხმოვანთა კომპლექსები)
        // ==========================================
        MindLexiconEntry("შევამოწმოთ", "MORPHOLOGY_VERBS", "GEORGIAN", 148.0f, listOf("შ", "ე", "ვ", "ა", "მ", "ო", "წ", "მ", "ო", "თ"), "პროექტის ვალიდაცია", "მოწმ", listOf("არქიტექტურა", "კოდი", "მონაცემები"), 62),
        MindLexiconEntry("დავაკომიტოთ", "MORPHOLOGY_VERBS", "GEORGIAN", 152.0f, listOf("დ", "ა", "ვ", "ა", "კ", "ო", "მ", "ი", "ტ", "ო", "თ"), "ცვლილებების შენახვა", "კომიტ", listOf("ცვლილებები", "რეპოზიტორიაში"), 58),
        MindLexiconEntry("გავაანალიზოთ", "MORPHOLOGY_VERBS", "GEORGIAN", 155.0f, listOf("გ", "ა", "ვ", "ა", "ა", "ნ", "ა", "ლ", "ი", "ზ", "ო", "თ"), "სიღრმისეული კვლევა", "ანალიზ", listOf("ტელემეტრია", "შედეგები", "ნაკადი")),
        MindLexiconEntry("დავასინთეზოთ", "MORPHOLOGY_VERBS", "GEORGIAN", 157.0f, listOf("დ", "ა", "ვ", "ა", "ს", "ი", "ნ", "თ", "ე", "ზ", "ო", "თ"), "მრავალსენსორიანი შერწყმა", "სინთეზ", listOf("აზრი", "წინადადება", "სიგნალები")),
        MindLexiconEntry("განვახორციელოთ", "MORPHOLOGY_VERBS", "GEORGIAN", 159.0f, listOf("გ", "ა", "ნ", "ვ", "ა", "ხ", "ო", "რ", "ც", "ი", "ე", "ლ", "ო", "თ"), "გეგმის აღსრულება", "ხორციელ", listOf("ოპტიმიზაცია", "ინტეგრაცია")),
        MindLexiconEntry("გარდავქმნათ", "MORPHOLOGY_VERBS", "GEORGIAN", 153.0f, listOf("გ", "ა", "რ", "დ", "ა", "ვ", "ქ", "მ", "ნ", "ა", "თ"), "ტრანსფორმაცია", "ქმნ", listOf("მოდელი", "სტრუქტურა"), 70),
        MindLexiconEntry("მოვარგოთ", "MORPHOLOGY_VERBS", "GEORGIAN", 144.0f, listOf("მ", "ო", "ვ", "ა", "რ", "გ", "ო", "თ"), "ადაპტაცია და კალიბრაცია", "რგ", listOf("ალგორითმებს", "პერსონას")),
        MindLexiconEntry("გავაუმჯობესოთ", "MORPHOLOGY_VERBS", "GEORGIAN", 150.0f, listOf("გ", "ა", "ვ", "ა", "უ", "მ", "ჯ", "ო", "ბ", "ე", "ს", "ო", "თ"), "ხარისხის აწევა", "ჯობ", listOf("სიზუსტე", "სისწრაფე")),
        MindLexiconEntry("შევინახოთ", "MORPHOLOGY_VERBS", "GEORGIAN", 142.0f, listOf("შ", "ე", "ვ", "ი", "ნ", "ა", "ხ", "ო", "თ"), "მონაცემთა ბაზაში ფიქსაცია", "ნახ", listOf("ბაზაში", "ჩექპოინტი")),
        MindLexiconEntry("გავუშვათ", "MORPHOLOGY_VERBS", "GEORGIAN", 141.0f, listOf("გ", "ა", "ვ", "უ", "შ", "ვ", "ა", "თ"), "პროცესის სტარტი", "შვ", listOf("კომპილაცია", "ტესტები")),
        MindLexiconEntry("გამოვთვალოთ", "MORPHOLOGY_VERBS", "GEORGIAN", 145.0f, listOf("გ", "ა", "მ", "ო", "ვ", "თ", "ვ", "ა", "ლ", "ო", "თ"), "მათემატიკური ანალიზი", "თვლ", listOf("ალბათობა", "წონები")),
        MindLexiconEntry("დავაკალიბროთ", "MORPHOLOGY_VERBS", "GEORGIAN", 149.5f, listOf("დ", "ა", "ვ", "ა", "კ", "ა", "ლ", "ი", "ბ", "რ", "ო", "თ"), "სენსორების გასწორება", "კალიბრ", listOf("სენსორები", "იმპედანსი")),
        MindLexiconEntry("მწვრთნელი", "MORPHOLOGY_VERBS", "GEORGIAN", 172.0f, listOf("მ", "წ", "ვ", "რ", "თ", "ნ", "ე", "ლ", "ი"), "მოდელის გამწვრთნელი (4-თანხმოვანი)", "წვრთნ", listOf("ალგორითმი", "ქსელი"), 85),
        MindLexiconEntry("გვფრცქვნი", "MORPHOLOGY_VERBS", "GEORGIAN", 178.0f, listOf("გ", "ვ", "ფ", "რ", "ც", "ქ", "ვ", "ნ", "ი"), "უნიკალური 6-თანხმოვნიანი კომპლექსი", "ფრცქვნ", listOf("ფენებს", "მონაცემებს"), 90),
        MindLexiconEntry("ვფარცხავთ", "MORPHOLOGY_VERBS", "GEORGIAN", 168.0f, listOf("ვ", "ფ", "ა", "რ", "ც", "ხ", "ა", "ვ", "თ"), "დალაგება / ფილტრაცია", "ფარცხ", listOf("ხარვეზებს", "შემავალ ნაკადს"), 75),
        MindLexiconEntry("გვბრდღვნის", "MORPHOLOGY_VERBS", "GEORGIAN", 175.0f, listOf("გ", "ვ", "ბ", "რ", "დ", "ღ", "ვ", "ნ", "ი", "ს"), "მკვეთრი დინამიკური იმპულსი", "ბრდღვნ", listOf("დაბრკოლებებს"), 88),

        // ==========================================
        // 7. ENGLISH (საერთაშორისო ტერმინოლოგია)
        // ==========================================
        MindLexiconEntry("Hello World", "ENGLISH", "ENGLISH", 134.0f, listOf("H", "e", "l", "l", "o", " ", "W", "o", "r", "l", "d"), "Standard Developer Greeting", "", listOf("from NeuroSync")),
        MindLexiconEntry("Deep Focus", "ENGLISH", "ENGLISH", 148.0f, listOf("D", "e", "e", "p", " ", "F", "o", "c", "u", "s"), "High mental focus state", "", listOf("engaged")),
        MindLexiconEntry("Optimize Engine", "ENGLISH", "ENGLISH", 155.0f, listOf("O", "p", "t", "i", "m", "i", "z", "e"), "Speed up calculations", "", listOf("pipeline")),
        MindLexiconEntry("Clean Architecture", "ENGLISH", "ENGLISH", 151.0f, listOf("C", "l", "e", "a", "n"), "Domain, Data & Presentation separation", "", listOf("pattern")),
        MindLexiconEntry("Realtime Telemetry", "ENGLISH", "ENGLISH", 157.0f, listOf("R", "e", "a", "l", "t", "i", "m", "e"), "Continuous sensor stream", "", listOf("stream active"))
    )

    /**
     * Advanced Morphological & Algorithmic Word Predictor
     * Combines Markov N-grams, Screen Context, Circadian Rhythm, and Subvocal EMG Resonance
     */
    fun predictBestCandidates(
        previousWord: String,
        screenContext: String,
        circadianHour: Int = 14,
        stressLevel: Float = 0.2f,
        currentCluster: String = "",
        limit: Int = 5
    ): List<MindLexiconEntry> {
        val scoredList = MIND_LEXICON_DATABASE.map { entry ->
            var score = 0.0f

            // 1. Markov Transition Weight (Does previous word frequently precede this entry?)
            if (previousWord.isNotBlank()) {
                val prevMatch = MIND_LEXICON_DATABASE.find { it.word == previousWord }
                if (prevMatch?.typicalNextWords?.any { entry.word.contains(it) || it.contains(entry.word) } == true) {
                    score += 45.0f
                }
            }

            // 2. Screen Context Weight
            when {
                screenContext.contains("IDE", ignoreCase = true) || screenContext.contains("Terminal", ignoreCase = true) -> {
                    if (entry.category == "DEV" || entry.category == "MORPHOLOGY_VERBS") score += 35.0f
                }
                screenContext.contains("Messaging", ignoreCase = true) || screenContext.contains("Chat", ignoreCase = true) -> {
                    if (entry.category == "COMMON" || entry.category == "EMOTIONS") score += 35.0f
                }
                screenContext.contains("Research", ignoreCase = true) || screenContext.contains("Docs", ignoreCase = true) -> {
                    if (entry.category == "NEURO_SCIENCE" || entry.category == "MORPHOLOGY_VERBS") score += 35.0f
                }
                else -> {
                    if (entry.category == "COMMANDS" || entry.category == "COMMON") score += 20.0f
                }
            }

            // 3. Subvocal Consonant Cluster Match (Fast Decimation)
            if (currentCluster.isNotBlank()) {
                val cleanCluster = currentCluster.replace("-", "").trim()
                if (entry.word.startsWith(cleanCluster, ignoreCase = true) || entry.rootStem.startsWith(cleanCluster)) {
                    score += 50.0f + (entry.clusterSpeedupGainPct * 0.3f)
                }
            }

            // 4. Circadian & Stress Calibration
            if (stressLevel > 0.6f && (entry.category == "EMOTIONS" || entry.word.contains("სიმშვიდე") || entry.word.contains("შესვენება"))) {
                score += 25.0f
            }

            if (circadianHour in 9..18 && (entry.category == "DEV" || entry.category == "MORPHOLOGY_VERBS")) {
                score += 15.0f
            }

            // Base frequency bonus
            score += (entry.emgFrequencyHz / 20.0f)

            Pair(entry, score)
        }

        return scoredList
            .sortedByDescending { it.second }
            .take(limit)
            .map { it.first }
    }

    /**
     * Finds the nearest Georgian phonetic match for an incoming EMG frequency spike.
     */
    fun snapToNearestPhoneticWord(frequencyHz: Float, categoryFilter: String = "ALL"): MindLexiconEntry {
        val candidates = if (categoryFilter == "ALL") {
            MIND_LEXICON_DATABASE
        } else {
            MIND_LEXICON_DATABASE.filter { it.category == categoryFilter }
        }

        return candidates.minByOrNull { Math.abs(it.emgFrequencyHz - frequencyHz) }
            ?: MIND_LEXICON_DATABASE.first()
    }

    /**
     * Converts a string into phoneme tokens.
     */
    fun getPhonemesForString(input: String): List<String> {
        return input.map { char ->
            if (char.isWhitespace()) "␣" else char.toString()
        }
    }
}
