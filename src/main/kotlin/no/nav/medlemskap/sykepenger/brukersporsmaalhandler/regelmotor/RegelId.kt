package no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor

enum class RegelId(val identifikator: String, val avklaring: String, val neiBegrunnelse: String = "", val jaBegrunnelse: String = "", val uavklartBegrunnelse: String = "") {
    //
    REGEL_2("2", "Er bruker omfattet av grunnforordningen (EØS)? Dvs er bruker statsborger i et EØS-land inkl. Norge?", "Brukeren er ikke statsborger i et EØS-land."),
    REGEL_11("11", "Er bruker norsk statsborger?", "Brukeren er ikke norsk statsborger"),
    REGEL_28("28", "Har bruker ektefelle i PDL?", "Bruker har ikke ektefelle i PDL"),
    REGEL_29("29", "Er ektefellen en EØS-borger?", "Ektefelle til bruker er ikke EØS-borger"),
    //
    SP6001("SkalHalekjøres","Skal regelmotor prosessere gammel kjøring","Årsaker i gammel kjøring tilsier ikke at hale skal utføres","Årsaker i gammel kjøring tilsier at halen skal kjøres"),
    SP6110("ArbeidUtlandNyModell","Er ny modell for ArbeidUtland oppgitt","Ingen brukersvar på ny modell for Arbeid Utland","Brukersvar for arbeid utland på ny modell finnes"),
    SP6120("ArbeidUtlandNyModell","har bruker oppgitt JA i arbeid utland ny modell","bruker har  oppgitt NEI i arbeid utland ny modell","bruker har oppgitt JA i arbeid utland ny modell"),
    SP6130("ArbeidUtlandGammelModell","Har Bruker svart JA i arbeid utland","Bruker har oppgitt NEI i arbeid utland","Bruker har oppgitt JA i arbeid utland gammel modell"),

    SP6201("Er det regelbrudd i for oppholdstilatelse i gammel flyt","Er det regelbrudd i for oppholdstilatelse i gammel flyt","Det er ingen regelbrudd for oppholdstilatele i gammel flyt","Det er  regelbrudd for oppholdstilatele i gammel flyt"),
    SP6211("Finnes brukersvar for oppholdstilatelse","Finnes brukersvar for oppholdstilatelse","Det finnes ikke brukersvar for oppholdstilatelse","Det finnes brukersvar for oppholdstilatelse"),
    SP6212("Finnes brukersvar for oppholdstilatelse","Finnes brukersvar for oppholdstilatelse","Det finnes ikke brukersvar for oppholdstilatelse","Det finnes brukersvar for oppholdstilatelse"),

    SP6221("Har bruker selv opplyst om permanent oppholdstilatelse","Har bruker selv opplyst om permanent oppholdstilatelse","Bruker har selv  opplyst at hen ikke har  permanent oppholdstilatelse","Bruker har selv   opplyst at hen  har  permanent oppholdstilatelse"),
    SP6222("Er start dato oppgitt av bruker mer en 12 mnd tilbake i tid","Er start dato  oppgitt av bruker mer en 12 mnd tilbake i tid","Start dato oppgitt av bruker er ikke mer en 12 mnd tilbake i tid","Start dato oppgitt av bruker er mer en 12 mnd tilbake i tid"),
    SP6223("Er Vedtaksdato oppgitt av bruker mer en 12 mnd tilbake i tid","Er Vedtaksdato oppgitt av bruker mer en 12 mnd tilbake i tid","Vedtaksdato oppgitt av bruker er ikke mer en 12 mnd tilbake i tid","Vedtaksdato oppgitt av bruker er mer en 12 mnd tilbake i tid"),
    SP6225("kan oppgitt periode slås sammen med UDI periode til en sammenhengende periode","kan oppgitt periode slås sammen med UDI periode til en sammenhengende periode","Oppgitt periode i brukerspørsmål kan ikke slås sammen med UDI opplysninger til en sammenhengden periode","Oppgitt periode i brukerspørsmål kan slås sammen med UDI opplysninger til en sammenhengden periode"),
    SP6226("Er Sammenslått Periode for Oppholdstilatelser og UDI informasjon 1 år tilbake og 2 mnd frem?","Er Sammenslått Periode for Oppholdstilatelser og UDI informasjon 1 år tilbake og 2 mnd frem?","Sammenlagt periode av bruker spørsmål og UDI er ikke lang nok","Sammenlagt periode av bruker spørsmål og UDI er lang nok"),
    SP6229("Er oppgitt oppholdstilatelse likt som PDL innslag","Er oppgitt oppholdstilatelse likt som PDL innslag","oppholdstilatelse  oppgitt av bruker er ikke likt som PDL innslag","oppholdstilatelse  oppgitt av bruker er likt som PDL innslag"),
    SP6229_1("Det er logget avvik i brukeropplysninger","Det er logget avvik i brukeropplysninger","Det er logget avvik i brukeropplysninger","Det er logget avvik i brukeropplysninger"),

    SP6301("Finnes brukersvar for OppholdUtenfor EØS","Finnes brukersvar for OppholdUtenfor EØS","Det finnes ikke brukersvar for OppholdUtenforEØS","Det finnes brukersvar for OppholdUtenforEØS"),
    SP6311("Opphold Utenfor EØS","Har Bruker Oppholdt seg utenfor EØS","Bruker har oppgitt NEI i Opphold utenfor EØS","Bruker har oppgitt JA i Opphold utenfor EØS","Bruker har oppgitt JA i Opphold utenfor EØS"),
    SP6312("Er det bare 1 utenlandsOpphold","Er det bare ett utenlandsopphold?","Det er flere en ett utenlandsopphold registrert","Det er bare ett utenlandsopphold registrert","Bruker har oppgitt flere utenlandsopphold utenfor EØS"),
    SP6313("Ble oppholdet avsluttet for mer enn 90 dager siden","Ble oppholdet avsluttet for mer enn 90 dager siden?","Det er mindre en 90 dager siden oppholdet utenfor EØS ble avsluttet","det er mer en 90 dager siden oppholdet ble avsluttet","det er mindre en 90 dager siden oppholdet utenfor EØS ble avsluttet"),
    SP6314("Er oppholdet i utlandet kortere enn 180 dager","Er oppholdet i utlandet kortere enn 180 dager?","Oppholdet utenfor EØS er lengere en 180 dager","oppholdet utenfor EØS er kortere en 180 dager","oppholdet utenfor EØS er lengere en 180 dager"),

    SP6401("Finnes brukersvar for OppholdUtenfor Norge","Finnes brukersvar for OppholdUtenfor Norge","Det finnes ikke brukersvar for OppholdUtenfor Norge","Det finnes brukersvar for OppholdUtenfor Norge"),
    SP6411("Opphold Utenfor Norge","Har Bruker oppholdt seg utenfor Norge Norge","Bruker har oppgitt JA i Opphold utenfor Norge","Bruker har oppgitt NEI i Opphold utenfor Norge","Bruker har oppgitt JA i Opphold utenfor Norge"),
    SP6412("Er det bare 1 utenlandsOpphold","Er det bare ett utenlandsopphold?","Det er flere en ett utenlandsopphold registrert","Det er bare ett utenlandsopphold registrert","Bruker har oppgitt flere utenlandsopphold utenfor EØS"),
    SP6413("Ble oppholdet avsluttet for mer enn 90 dager siden","Ble oppholdet avsluttet for mer enn 90 dager siden?","Det er mindre en 90 dager siden oppholdet utenfor EØS ble avsluttet","det er mer en 90 dager siden oppholdet ble avsluttet","det er mindre en 90 dager siden oppholdet utenfor EØS ble avsluttet"),
    SP6414("Er oppholdet i utlandet kortere enn 180 dager","Er oppholdet i utlandet kortere enn 180 dager?","Oppholdet utenfor Norge er lengere en 180 dager","oppholdet utenfor Norge er kortere en 180 dager","oppholdet utenfor Norge er lengere en 180 dager"),

    SP6510("Regelutsjekk Norske borgere","Kan alle regelbrudd sjekkes ut","Det finnes regelbrudd som ikke kan djekkes ut automatisk","alle regelbrudd kan sjekkes ut automatisk"),
    SP6600("Regelutsjekk EOS borgere","Kan alle regelbrudd sjekkes ut","Det finnes regelbrudd som ikke kan djekkes ut automatisk","alle regelbrudd kan sjekkes ut automatisk"),
    SP6700("Regelutsjekk 3 land borgere","Kan alle regelbrudd sjekkes ut","Det finnes regelbrudd som ikke kan djekkes ut automatisk","alle regelbrudd kan sjekkes ut automatisk"),

    OPHOLDSTILATELSE_FLYT("OPHOLDSTILATELSE_FLYT", "Tilsier ny oppholdstilatelse flyt at bruker har oppholdstilatelse"),
    REGEL_UTSJEKK("Utsjekk av regel brudd", "Kan alle regelbrudd fra gammel regelmotor sjekkes ut?"),
    ARBEID_UTLAND_FLYT("ARBEID_UTLAND_FLYT", "Har bruker på noen måte gitt svar på arbeid utland?"),
    OPPHOLDUTENFOREOSFLYT("OPPHOLD_UTENFOR_EOS_FLYT", "Har bruker oppgitt opphold utenfor EØS?"),
    OPPHOLD_UTENFOR_NORGE_FLYT("OPPHOLD_UTENFOR_NORGE_FLYT", "Har bruker oppgitt opphold utenfor Norge?"),

    REGEL_OPPLYSNINGER("OPPLYSNINGER", "Finnes det registrerte opplysninger på bruker?", "Alle de følgende ble NEI"),
    REGEL_FLYT_KONKLUSJON("RFK", "Svar på regelflyt", "Regelflyt konkluderer med NEI"),
    REGEL_MEDLEM_KONKLUSJON("LOVME", "Er bruker medlem?", "Kan ikke konkludere med medlemskap", "Bruker er medlem", "Kan ikke konkludere med medlemskap");

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
