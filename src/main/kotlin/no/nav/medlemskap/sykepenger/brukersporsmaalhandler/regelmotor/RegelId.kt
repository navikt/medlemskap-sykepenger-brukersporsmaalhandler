package no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor

enum class RegelId(val identifikator: String, val avklaring: String, val neiBegrunnelse: String = "", val jaBegrunnelse: String = "", val uavklartBegrunnelse: String = "") {
    //
    REGEL_2("2", "Er bruker omfattet av grunnforordningen (EØS)? Dvs er bruker statsborger i et EØS-land inkl. Norge?", "Brukeren er ikke statsborger i et EØS-land."),
    REGEL_11("11", "Er bruker norsk statsborger?", "Brukeren er ikke norsk statsborger"),
    REGEL_28("28", "Har bruker ektefelle i PDL?", "Bruker har ikke ektefelle i PDL"),
    REGEL_29("29", "Er ektefellen en EØS-borger?", "Ektefelle til bruker er ikke EØS-borger"),
    //
    REGEL_MEDLEM_KONKLUSJON("LOVME", "Er bruker medlem?", "Kan ikke konkludere med medlemskap", "Bruker er medlem", "Kan ikke konkludere med medlemskap"),
    REGEL_FLYT_KONKLUSJON("RFK", "Svar på regelflyt", "Regelflyt konkluderer med NEI"),
    REGEL_OPPLYSNINGER("OPPLYSNINGER", "Finnes det registrerte opplysninger på bruker?", "Alle de følgende ble NEI"),
    GS6001("ArbeidUtland","Har Bruker svart JA i arbeid utland","Bruker har oppgitt NEI i arbeid utland","jabegrunnelse","uavklartRespons")
    ;

    fun begrunnelse(svar: Svar): String {
        return when (svar) {
            Svar.JA -> jaBegrunnelse
            Svar.NEI -> neiBegrunnelse
            Svar.UAVKLART -> uavklartBegrunnelse
        }
    }

    companion object {
        fun fraRegelIdString(regelIdStr: String): RegelId {

            return values().firstOrNull { it.identifikator == regelIdStr || it.name == regelIdStr }
                ?: throw RuntimeException("Fant ikke regelId $regelIdStr")
        }

        fun RegelId.metricName(): String = identifikator + ". " + avklaring.replace("?", "")
    }
}
