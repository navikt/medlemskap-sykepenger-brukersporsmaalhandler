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