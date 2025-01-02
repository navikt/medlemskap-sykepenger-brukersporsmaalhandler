package no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.domene


import com.natpryce.konfig.booleanType
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