package com.example.hymykyl_minno.ui.localization

object AppStrings {

    fun sidebarTitle(lang: String): String = when (lang) {
        "EN" -> "Your experience\nis important to us."
        "SV" -> "Din upplevelse\när viktig för oss."
        else -> "Kokemuksesi\non meille tärkeä."
    }

    fun sidebarDesc(lang: String): String = when (lang) {
        "EN" -> "Responses are anonymous and help us improve. This takes about 2 minutes."
        "SV" -> "Svaren är anonyma och hjälper oss att bli bättre. Detta tar cirka 2 minuter."
        else -> "Vastaukset ovat anonyymejä ja auttavat meitä kehittymään. Tähän kuluu noin 2 minuuttia."
    }

    fun stepIndicator(step: Int, lang: String): String = when (lang) {
        "EN" -> "Step $step / 4"
        "SV" -> "Steg $step / 4"
        else -> "Vaihe $step / 4"
    }

    fun btnNext(lang: String): String = when (lang) {
        "EN" -> "Next"
        "SV" -> "Fortsätt"
        else -> "Jatka"
    }

    fun btnBack(lang: String): String = when (lang) {
        "EN" -> "Back"
        "SV" -> "Tillbaka"
        else -> "Takaisin"
    }

    fun btnSkip(lang: String): String = when (lang) {
        "EN" -> "Skip"
        "SV" -> "Hoppa över"
        else -> "Ohita"
    }

    fun btnSubmit(lang: String): String = when (lang) {
        "EN" -> "Submit feedback"
        "SV" -> "Skicka feedback"
        else -> "Lähetä palaute"
    }

    // Step 1: NPS
    fun npsTitle(lang: String): String = when (lang) {
        "EN" -> "How likely are you to recommend HyMy-kylä services?"
        "SV" -> "Hur sannolikt är det att du skulle rekommendera HyMy-kyläs tjänster?"
        else -> "Kuinka todennäköisesti suosittelisit HyMy-kylän palveluita?"
    }

    fun npsNotAtAll(lang: String): String = when (lang) {
        "EN" -> "Not at all"
        "SV" -> "Inte alls"
        else -> "En lainkaan"
    }

    fun npsExtremelyLikely(lang: String): String = when (lang) {
        "EN" -> "Extremely likely"
        "SV" -> "Ytterst sannolikt"
        else -> "Erittäin todennäköisesti"
    }

    // Step 2: Service Selection
    fun serviceTitle(lang: String): String = when (lang) {
        "EN" -> "Which service did you use?"
        "SV" -> "Vilken tjänst använde du?"
        else -> "Mitä palvelua käytit?"
    }

    fun localizedServiceName(canonicalName: String, lang: String): String = when (canonicalName) {
        "Suun terveydenhuolto" -> when (lang) {
            "EN" -> "Oral Health Care"
            "SV" -> "Munhälsovård"
            else -> "Suun terveydenhuolto"
        }
        "Fysioterapia & osteopatia" -> when (lang) {
            "EN" -> "Physiotherapy & Osteopathy"
            "SV" -> "Fysioterapi & osteopati"
            else -> "Fysioterapia & osteopatia"
        }
        "Senioripalvelut" -> when (lang) {
            "EN" -> "Senior Services"
            "SV" -> "Seniortjänster"
            else -> "Senioripalvelut"
        }
        "Perhepalvelut" -> when (lang) {
            "EN" -> "Family Services"
            "SV" -> "Familjetjänster"
            else -> "Perhepalvelut"
        }
        "Aikuisneuvola" -> when (lang) {
            "EN" -> "Adult Health Clinic"
            "SV" -> "Vuxenrådgivning"
            else -> "Aikuisneuvola"
        }
        "Toimintaterapia" -> when (lang) {
            "EN" -> "Occupational Therapy"
            "SV" -> "Arbetsterapi"
            else -> "Toimintaterapia"
        }
        "Apuvälinepalvelut" -> when (lang) {
            "EN" -> "Assistive Device Services"
            "SV" -> "Hjälpmedelstjänster"
            else -> "Apuvälinepalvelut"
        }
        "Matalan kynnyksen palvelut" -> when (lang) {
            "EN" -> "Low-Threshold Services"
            "SV" -> "Lågsköskeltjänster"
            else -> "Matalan kynnyksen palvelut"
        }
        else -> canonicalName
    }

    // Step 3: Detailed Feedback
    fun detailedTitle(serviceName: String, lang: String): String = when (lang) {
        "EN" -> "A few more questions about $serviceName."
        "SV" -> "Några frågor till om $serviceName."
        else -> "Vielä muutama kysymys palvelusta $serviceName."
    }

    // Step 4: Comment
    fun commentTitle(lang: String): String = when (lang) {
        "EN" -> "Would you like to tell us anything else?"
        "SV" -> "Vill du berätta något annat?"
        else -> "Haluatko kertoa jotain muuta?"
    }

    fun commentSubtitle(lang: String): String = when (lang) {
        "EN" -> "Optional — tell us what stood out, what could be improved, or other thoughts."
        "SV" -> "Frivilligt — berätta vad du kom ihåg, vad som kan förbättras eller andra tankar."
        else -> "Vapaaehtoinen — kerro mikä jäi mieleen, mitä voisi parantaa tai muita ajatuksia."
    }

    fun commentPlaceholder(lang: String): String = when (lang) {
        "EN" -> "Write your feedback here..."
        "SV" -> "Skriv din feedback här..."
        else -> "Kirjoita palautteesi tähän..."
    }

    // Step 5: Thank You
    fun thankYouTitle(lang: String): String = when (lang) {
        "EN" -> "Thank you for your feedback!"
        "SV" -> "Tack för din feedback!"
        else -> "Kiitos palautteestasi!"
    }

    fun thankYouSubtitle(lang: String): String = when (lang) {
        "EN" -> "Your feedback helps us improve our services even further."
        "SV" -> "Din feedback hjälper oss att göra våra tjänster ännu bättre."
        else -> "Palautteesi auttaa meitä kehittämään palveluitamme entistä paremmiksi."
    }

    fun thankYouResetText(seconds: Int, lang: String): String = when (lang) {
        "EN" -> "Screen resets in $seconds seconds."
        "SV" -> "Skärmen återgår om $seconds sekunder."
        else -> "Näkymä palaa alkutilaan $seconds sekunnin kuluttua."
    }

    fun btnReturnToStart(lang: String): String = when (lang) {
        "EN" -> "Return to start"
        "SV" -> "Gå tillbaka till starten"
        else -> "Palaa alkuun"
    }
}
