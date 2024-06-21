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
    val oppholdstillatelse:UdiOppholdsTilatelse?
)
data class InputPeriode(
    val fom: LocalDate,
    val tom: LocalDate
)
data class PdlPersonHistorikk(
    val oppholdstilatelser:List<PdlOppholdsTilatelse> = emptyList()
)
data class PdlOppholdsTilatelse(
    val type:String,
    val oppholdFra:LocalDate?,
    val oppholdTil:LocalDate?
)
data class UdiOppholdsTilatelse(
    val gjeldendeOppholdsstatus:GjeldendeOppholdsstatus?
)
data class GjeldendeOppholdsstatus(
    val oppholdstillatelsePaSammeVilkar:OppholdstillatelsePaSammeVilkar?
)
data class OppholdstillatelsePaSammeVilkar(
    val periode:UdiPeriode
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