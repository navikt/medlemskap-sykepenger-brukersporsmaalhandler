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
    var arbeidsavtaler: List<Arbeidsavtale>
)

data class InputPeriode(
    val fom: LocalDate,
    val tom: LocalDate
)
data class PdlPersonHistorikk(
    val oppholdstilatelser:List<PdlOppholdsTilatelse> = emptyList(),
    val statsborgerskap:List<Statsborgerskap> = emptyList()
)
data class PdlOppholdsTilatelse(
    val type:String,
    val oppholdFra:LocalDate?,
    val oppholdTil:LocalDate?
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
    val oppholdstillatelsePaSammeVilkar:OppholdstillatelsePaSammeVilkar?
)
data class OppholdstillatelsePaSammeVilkar(
    val periode:UdiPeriode,
    val type: String? = null
)
data class UdiPeriode(
    val fom:LocalDate,
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