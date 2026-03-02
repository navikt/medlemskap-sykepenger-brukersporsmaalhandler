package no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.domene


import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.Ytelse
import java.time.LocalDate
import java.time.temporal.ChronoUnit

data class Datagrunnlag(
    val ytelse: Ytelse,
    val periode: InputPeriode,
    val fnr:String,
    val brukerinput:Brukerinput,
    val pdlpersonhistorikk:PdlPersonHistorikk,
    val oppholdstillatelse:UdiOppholdsTilatelse?,
    val arbeidsforhold: List<Arbeidsforhold> = listOf()
)

fun Datagrunnlag.arbeidsforholdAvType(type: Arbeidsforholdstype): List<Arbeidsforhold> {
    return arbeidsforhold.filter {
        it.arbeidsforholdstype == type
    }
}

fun Datagrunnlag.maritimeArbeidsAvtaler():List<Arbeidsavtale> {
    return arbeidsforholdAvType(Arbeidsforholdstype.MARITIMT).flatMap { it.arbeidsavtaler }.sortedByDescending { it.gyldighetsperiode.fom }
}

fun Datagrunnlag.sisteMaritimeArbeidsAvtale(): Arbeidsavtale?{
    return maritimeArbeidsAvtaler().firstOrNull()
}


enum class Arbeidsforholdstype(val kodeverdi: String) {
    FRILANSER("frilanserOppdragstakerHonorarPersonerMm"),
    MARITIMT("maritimtArbeidsforhold"),
    NORMALT("ordinaertArbeidsforhold"),
    FORENKLET("forenkletOppgjoersordning"),
    ANDRE("pensjonOgAndreTyperYtelserUtenAnsettelsesforhold");

    companion object {
        fun fraArbeidsforholdtypeVerdi(arbeidsforholdstypeVerdi: String): Arbeidsforholdstype {
            return values().first { it.kodeverdi == arbeidsforholdstypeVerdi }
        }
    }
}

data class Arbeidsavtale(
    var periode: ArbeidsForholdPeriode,
    val gyldighetsperiode: ArbeidsForholdPeriode,
    val yrkeskode: String,
    val skipsregister: Skipsregister?,
    val fartsomraade: Fartsomraade?,
    val stillingsprosent: Double?,
    val beregnetAntallTimerPrUke: Double?,
    val skipstype: Skipstype?
) {
    fun getStillingsprosent(): Double {
        if (stillingsprosent == 0.0 && beregnetAntallTimerPrUke != null && beregnetAntallTimerPrUke > 0) {
            val beregnetStillingsprosent = (beregnetAntallTimerPrUke / 37.5) * 100
            return Math.round(beregnetStillingsprosent * 10.0) / 10.0
        }

        return stillingsprosent ?: 100.0
    }
}


data class Arbeidsforhold(
    val periode: ArbeidsForholdPeriode,
    val arbeidsforholdstype: Arbeidsforholdstype,
    var arbeidsavtaler: List<Arbeidsavtale>,
    val permisjonPermittering: List<PermisjonPermittering>?
)

data class PermisjonPermittering(
    val periode: PeriodeMedNullVerdier,
    val permisjonPermitteringId: String,
    val prosent: Double?,
    val type: PermisjonPermitteringType,
    val varslingskode: String?
)

enum class PermisjonPermitteringType(val kodeverdi: String) {
    PERMISJON("permisjon"),
    PERMISJON_MED_FORELDREPENGER("permisjonMedForeldrepenger"),
    PERMISJON_VED_MILITAERTJENESTE("permisjonVedMilitaertjeneste"),
    PERMITTERING("permittering"),
    UTDANNINGSPERMISJON("utdanningspermisjon"),
    VELFERDSPERMISJON("velferdspermisjon"),
    ANDRE_IKKE_LOVFESTEDE_PERMISJONER("andreIkkeLovfestedePermisjoner"),
    ANDRE_LOVFESTEDE_PERMISJONER("andreLovfestedePermisjoner"),
    UTDANNINGSPERMISJON_IKKE_LOVFESTET("utdanningspermisjonIkkeLovfestet"),
    UTDANNINGSPERMISJON_LOVFESTET("utdanningspermisjonLovfestet"),
    ANNET("Annet")
    ;

    companion object {
        fun fraPermisjonPermitteringVerdi(permisjonPermittering: String): PermisjonPermitteringType {
            return PermisjonPermitteringType.values().first { it.kodeverdi == permisjonPermittering }
        }
    }
}

data class InputPeriode(
    val fom: LocalDate,
    val tom: LocalDate
)
data class PdlPersonHistorikk(
    val statsborgerskap:List<Statsborgerskap> = emptyList()
)

data class Statsborgerskap(
    val landkode:String,
    val fom:LocalDate?,
    val tom:LocalDate?,
    val historisk: Boolean
)
data class UdiOppholdsTilatelse(
    val gjeldendeOppholdsstatus:GjeldendeOppholdsstatus?
)
data class GjeldendeOppholdsstatus(
    val oppholdstillatelsePaSammeVilkar:OppholdstillatelsePaSammeVilkar?,
    val eosellerEFTAOpphold: EOSellerEFTAOpphold?,
    val ikkeOppholdstillatelseIkkeOppholdsPaSammeVilkarIkkeVisum: IkkeOppholdstillatelseIkkeOppholdsPaSammeVilkarIkkeVisum?,
    val uavklart: Uavklart?
)
data class EOSellerEFTAOpphold(
    val periode: Periode?,
    val eosellerEFTAOppholdType: EOSellerEFTAOppholdType,
    val eosellerEFTAGrunnlagskategoriOppholdsrettType: EOSellerEFTAGrunnlagskategoriOppholdsrettType?,
    val eosellerEFTAGrunnlagskategoriOppholdstillatelseType: EOSellerEFTAGrunnlagskategoriOppholdsTillatelseType?
)

data class OppholdstillatelsePaSammeVilkar(
    val periode:UdiPeriode,
    val type: String? = null,
    val soknadIkkeAvgjort: Boolean? = null
)

data class IkkeOppholdstillatelseIkkeOppholdsPaSammeVilkarIkkeVisum(
    val utvistMedInnreiseForbud: UtvistMedInnreiseForbud?,
    var avslagEllerBortfallAvPOBOSellerTilbakekallEllerFormeltVedtak: AvslagEllerBortfallAvPOBOSellerTilbakekallEllerFormeltVedtak?,
    var ovrigIkkeOpphold: OvrigIkkeOpphold?
)

data class Uavklart(
    val uavklart: Boolean = false
)

data class UtvistMedInnreiseForbud(
    val innreiseForbud: JaNeiUavklart?
)

data class AvslagEllerBortfallAvPOBOSellerTilbakekallEllerFormeltVedtak(
    val avgjorelsesDato: LocalDate?
)

data class OvrigIkkeOpphold(
    val ovrigIkkeOppholdsKategori: OvrigIkkeOppholdsKategori?
)

enum class OvrigIkkeOppholdsKategori(val kodeverdi: String) {
    OPPHEVET_INNREISEFORBUD("OpphevetInnreiseforbud"),
    ANNULERING_AV_VISUM("AnnuleringAvVisum"),
    UTLOPT_OPPHOLDSTILLATELSE("UtloptOppholdstillatelse"),
    UTLOPT_EO_SELLER_EFTA_OPPHOLDSRETT_ELLER_EO_SELLER_EFTA_OPPHOLDSTILLATELSE("UtloptEOSellerEFTAOppholdsrettEllerEOSellerEFTAOppholdstillatelse");

    companion object {
        fun fraOvrigIkkeOppholdsKategoriType(
            ovrigIkkeOppholdsKategori: String?
        ): OvrigIkkeOppholdsKategori? {
            return OvrigIkkeOppholdsKategori.values()
                .firstOrNull { it.kodeverdi == ovrigIkkeOppholdsKategori }
        }
    }
}

enum class JaNeiUavklart(val jaNeiUavklart: String) {
    JA("Ja"),
    NEI("Nei"),
    UAVKLART("Uavklart");

    companion object {
        fun fraJaNeiUavklartVerdi(jaNeiUavklartVerdi: String?): JaNeiUavklart? {
            if (jaNeiUavklartVerdi.isNullOrEmpty()) return null
            return valueOf(jaNeiUavklartVerdi.toUpperCase())
        }
    }
}

enum class EOSellerEFTAOppholdType(val kodeverdi: String) {
    EOS_ELLER_EFTA_BESLUTNING_OM_OPPHOLDSRETT("EOSellerEFTABeslutningOmOppholdsrett"),
    EOS_ELLER_EFTA_VEDTAK_OM_VARIG_OPPHOLDSRETT("EOSellerEFTAVedtakOmVarigOppholdsrett"),
    EOS_ELLER_EFTA_OPPHOLDSTILLATELSE("EOSellerEFTAOppholdstillatelse");

    companion object {
        fun fraEOSellerEFTAOppholdTypeVerdi(EOSellerEFTAOppholdTypeVerdi: String?): EOSellerEFTAOppholdType? {
            return values().firstOrNull { it.kodeverdi == EOSellerEFTAOppholdTypeVerdi }
        }
    }
}

enum class EOSellerEFTAGrunnlagskategoriOppholdsrettType(val kodeverdi: String) {
    VARIG("Varig"),
    INGEN_INFORMASJON("IngenInformasjon"),
    FAMILIE("Familie"),
    TJENESTEYTING_ELLER_ETABLERING("TjenesteytingEllerEtablering"),
    UAVKLART("Uavklart");

    companion object {
        fun fraEOSellerEFTAGrunnlagskategoriOppholdsrettType(
            eosEllerEFTAGrunnlagskategoriOppholdsrettType: String?
        ): EOSellerEFTAGrunnlagskategoriOppholdsrettType? {
            return EOSellerEFTAGrunnlagskategoriOppholdsrettType.values()
                .firstOrNull { it.kodeverdi == eosEllerEFTAGrunnlagskategoriOppholdsrettType }
        }
    }
}

enum class EOSellerEFTAGrunnlagskategoriOppholdsTillatelseType(val kodeverdi: String) {
    EGNE_MIDLER_ELLER_FASTE_PERIODISKE_YTELSER("EgneMidlerEllerFastePeriodiskeYtelser"),
    ARBEID("Arbeid"),
    UTDANNING("Utdanning"),
    FAMILIE("Familie"),
    TJENESTEYTING_ELLER_ETABLERING("TjenesteytingEllerEtablering"),
    UAVKLART("Uavklart");

    companion object {
        fun fraEOSellerEFTAGrunnlagskategoriOppholdsTillatelseType(
            eosEllerEFTAGrunnlagskategoriOppholdsTillatelseType: String?
        ): EOSellerEFTAGrunnlagskategoriOppholdsTillatelseType? {
            return EOSellerEFTAGrunnlagskategoriOppholdsTillatelseType.values()
                .firstOrNull { it.kodeverdi == eosEllerEFTAGrunnlagskategoriOppholdsTillatelseType }
        }
    }
}


data class UdiPeriode(
    val fom:LocalDate?,
    val tom:LocalDate?
)

fun UdiOppholdsTilatelse.periode():UdiPeriode?{
    var udiPeriode : UdiPeriode? = null
    try{
        udiPeriode = this.gjeldendeOppholdsstatus!!.oppholdstillatelsePaSammeVilkar!!.periode
    }
    catch(e:Exception){

    }
    return udiPeriode
}
fun InputPeriode.antallDager():Long{
    if (tom!=null){
            return ChronoUnit.DAYS.between(this.fom,tom)
        }
    return -1
}

fun Datagrunnlag.statsborgerskap(): String{
    return this.pdlpersonhistorikk.statsborgerskap.filter { it.historisk == false}.map { it.landkode }.toString()
}

fun Datagrunnlag.udiOppholdstillatelsePeriode(): String{
    if (this.oppholdstillatelse == null  || this.oppholdstillatelse.periode() == null ){
        return "IKKE_OPPGITT"
    } else {
        return this.oppholdstillatelse.periode().toString()
    }
}
fun Datagrunnlag.udiOppholdstillatelseType(): String{
    if (this.oppholdstillatelse == null  || this.oppholdstillatelse.gjeldendeOppholdsstatus==null || this.oppholdstillatelse.gjeldendeOppholdsstatus.oppholdstillatelsePaSammeVilkar==null){
        return "IKKE_OPPGITT"
    }
    else {
        if (true == this.oppholdstillatelse.gjeldendeOppholdsstatus.oppholdstillatelsePaSammeVilkar.soknadIkkeAvgjort){
            return "SOKNAD-IKKE_AVGJORT"
        }
        else{
            return this.oppholdstillatelse.gjeldendeOppholdsstatus.oppholdstillatelsePaSammeVilkar.type!!
        }
    }
}

fun Datagrunnlag.sisteMaritimeArbeidsavtalePeriode(): String{
    val v = sisteMaritimeArbeidsAvtale()
    if (v == null){
        return "IKKE_OPPGITT"
    } else {
        return "fom: ${v.gyldighetsperiode.fom} , tom: ${v.gyldighetsperiode.tom}"
    }
}

fun Datagrunnlag.sisteMaritimeArbeidsavtaleSkipstype(): String{
    val v = sisteMaritimeArbeidsAvtale()
    if (v == null){
        return "IKKE_OPPGITT"
    } else {
        return v.skipstype.toString()
    }
}

fun Datagrunnlag.sisteMaritimeArbeidsavtaleSkipsregister(): String{
    val v = sisteMaritimeArbeidsAvtale()
    if (v == null){
        return "IKKE_OPPGITT"
    } else {
        return v.skipsregister.toString()
    }
}

fun Datagrunnlag.sisteMaritimeArbeidsavtaleFartsomraade(): String{
    val v = sisteMaritimeArbeidsAvtale()
    if (v == null){
        return "IKKE_OPPGITT"
    } else {
        return v.fartsomraade.toString()
    }
}