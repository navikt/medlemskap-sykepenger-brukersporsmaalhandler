package no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.domene


import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.Ytelse
import java.time.LocalDate

data class Datagrunnlag(
    val ytelse: Ytelse,
    val periode: InputPeriode,
    val fnr:String,
    val brukerinput:Brukerinput,
)
data class InputPeriode(
    val fom: LocalDate,
    val tom: LocalDate
)