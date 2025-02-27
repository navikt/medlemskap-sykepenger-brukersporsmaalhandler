package no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.domene

import java.time.LocalDate
import java.util.*

data class ArbeidUtenforNorge(
    val id: String,
    val arbeidsgiver:String,
    val land:String,
    val perioder: List<Periode>
)

data class Periode(val fom: String, val tom: String= Date().toString())

data class Brukerinput(
    val arbeidUtenforNorge: Boolean,
    val oppholdstilatelse:Oppholdstilatelse?=null,
    val utfortAarbeidUtenforNorge:UtfortAarbeidUtenforNorge?=null,
    val oppholdUtenforEos:OppholdUtenforEos?=null,
    val oppholdUtenforNorge:OppholdUtenforNorge?=null
)


data class OppholdUtenforNorge(
    val id: String,
    val sporsmalstekst: String?,
    val svar:Boolean,
    val oppholdUtenforNorge:List<Opphold>
)

data class OppholdUtenforEos(
    val id: String,
    val sporsmalstekst: String?,
    val svar:Boolean,
    val oppholdUtenforEOS:List<Opphold>
)

data class Opphold(
    val id: String,
    val land:String,
    val grunn:String,
    val perioder: List<Periode>
)
data class Oppholdstilatelse(
    val id: String,
    val sporsmalstekst: String?,
    val svar:Boolean,
    val vedtaksdato: LocalDate,
    val vedtaksTypePermanent:Boolean,
    val perioder:List<Periode> = mutableListOf()
)
data class UtfortAarbeidUtenforNorge(
    val id: String,
    val sporsmalstekst: String?,
    val svar:Boolean,
    val arbeidUtenforNorge:List<ArbeidUtenforNorge>
)

fun Brukerinput.inneholderNyeBrukerSpørsmål():Boolean{
    return  this.utfortAarbeidUtenforNorge!= null ||
            this.oppholdUtenforEos!=null ||
            this.oppholdUtenforNorge!=null ||
            this.oppholdstilatelse !=null
}
fun Brukerinput.oppgittArbeidUtenforNorgeLand():String{
    if (this.utfortAarbeidUtenforNorge !=null && this.utfortAarbeidUtenforNorge.arbeidUtenforNorge.isNotEmpty()){
        return this.utfortAarbeidUtenforNorge.arbeidUtenforNorge.first().land
    }
        return "IKKE_OPPGITT"
}

fun Brukerinput.oppgittArbeidUtenforNorgePeriode(): String {
    try {
        if (this.utfortAarbeidUtenforNorge != null && this.utfortAarbeidUtenforNorge.arbeidUtenforNorge.isNotEmpty()) {
            return this.utfortAarbeidUtenforNorge.arbeidUtenforNorge.first().perioder.first().toString()
        }
        return "IKKE_OPPGITT"
    }
    catch(e:Exception){
        return "IKKE_OPPGITT"
    }
}


fun Brukerinput.oppgittOppholdUtenforNorgePeriode(): String {
    try {
        if (this.oppholdUtenforNorge != null && this.oppholdUtenforNorge.oppholdUtenforNorge.isNotEmpty()) {
            return this.oppholdUtenforNorge.oppholdUtenforNorge.first().perioder.first().toString()
        }
        return "IKKE_OPPGITT"
    }
    catch(e:Exception){
        return "IKKE_OPPGITT"
    }
}

fun Brukerinput.oppgittOppholdUtenforNorgeLand():String{
    if (this.oppholdUtenforNorge !=null && this.oppholdUtenforNorge.oppholdUtenforNorge.isNotEmpty()){
        return this.oppholdUtenforNorge.oppholdUtenforNorge.first().land
    }
    return "IKKE_OPPGITT"
}
fun Brukerinput.oppgittOppholdUtenforNorgeGrunn():String{
    if (this.oppholdUtenforNorge !=null && this.oppholdUtenforNorge.oppholdUtenforNorge.isNotEmpty()){
        return this.oppholdUtenforNorge.oppholdUtenforNorge.first().grunn
    }
    return "IKKE_OPPGITT"
}

fun Brukerinput.oppgittOppholdUtenforEØSPeriode(): String {
    try {
        if (this.oppholdUtenforEos != null && this.oppholdUtenforEos.oppholdUtenforEOS.isNotEmpty()) {
            return this.oppholdUtenforEos.oppholdUtenforEOS.first().perioder.first().toString()
        }
        return "IKKE_OPPGITT"
    }
    catch(e:Exception){
        return "IKKE_OPPGITT"
    }
}

fun Brukerinput.oppgittOppholdUtenforEØSLand():String{
    if (this.oppholdUtenforEos !=null && this.oppholdUtenforEos.oppholdUtenforEOS.isNotEmpty()){
        return this.oppholdUtenforEos.oppholdUtenforEOS.first().land
    }
    return "IKKE_OPPGITT"
}
fun Brukerinput.oppgittOppholdUtenforEØSGrunn():String{
    if (this.oppholdUtenforEos !=null && this.oppholdUtenforEos.oppholdUtenforEOS.isNotEmpty()){
        return this.oppholdUtenforEos.oppholdUtenforEOS.first().grunn
    }
    return "IKKE_OPPGITT"
}


fun Brukerinput.oppholdUtenforEØSOppgitt():Boolean{
  return (
          this.oppholdUtenforEos != null &&
          this.oppholdUtenforEos.svar
          )
}
fun Brukerinput.oppholdUtenforNorgeOpppgitt():Boolean{
    return (
            this.oppholdUtenforNorge != null &&
            this.oppholdUtenforNorge.svar
            )
}
fun Brukerinput.utfortAarbeidUtenforNorgeOpppgitt():Boolean{
    return (
            this.utfortAarbeidUtenforNorge != null &&
            this.utfortAarbeidUtenforNorge.svar
            )
}
fun Brukerinput.oppholdstillatelseOppgitt():Boolean{
    return (
            this.oppholdstilatelse != null &&
            this.oppholdstilatelse.svar
            )
}

fun Brukerinput.oppholdstillatelsePeriode():String{
            if (this.oppholdstilatelse != null && oppholdstilatelse.perioder.isNotEmpty()) {
             return oppholdstilatelse.perioder.first().toString()
            } else if (this.oppholdstilatelse != null && oppholdstilatelse.perioder.isEmpty())
                return "INGEN_OPPHOLDSTILLATELSE_OPPGITT"
            else {
                return "IKKE_OPPGITT"
            }

}