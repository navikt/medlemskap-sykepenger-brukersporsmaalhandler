package no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.domene

enum class Skipsregister(val beskrivelse: String) {
    NIS("Norsk InternasjonaltSkipsregister"),
    NOR("Norsk Ordinært Skipsregister"),
    UTL("Utenlandsk skipsregister");

    companion object {
        fun fraSkipsregisterVerdi(skipsregisterValue: String?): Skipsregister? {
            if (skipsregisterValue.isNullOrEmpty()) return null
            return valueOf(skipsregisterValue.toUpperCase())
        }
    }
}


enum class Fartsomraade(val beskrivelse: String) {
    INNENRIKS("innenriks"),
    UTENRIKS("utenriks");

    companion object {
        fun fraFartsomraadeVerdi(fartsomradeValue: String?): Fartsomraade? {
            if (fartsomradeValue.isNullOrEmpty()) return null
            return valueOf(fartsomradeValue.toUpperCase())
        }
    }
}

enum class Skipstype {
    ANNET,
    BOREPLATTFORM,
    TURIST;

    companion object {
        fun fraSkipstypeVerdi(skipstypeValue: String?): Skipstype? {
            if (skipstypeValue.isNullOrEmpty()) return null
            return valueOf(skipstypeValue.toUpperCase())
        }
    }
}
