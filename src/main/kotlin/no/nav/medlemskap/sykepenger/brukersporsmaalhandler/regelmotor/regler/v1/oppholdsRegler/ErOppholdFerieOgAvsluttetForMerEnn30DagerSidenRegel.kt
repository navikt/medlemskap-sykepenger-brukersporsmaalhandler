package no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.regler.v1.oppholdsRegler


import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.BasisRegel
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.RegelId
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.Resultat
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.Resultat.Companion.ja
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.Resultat.Companion.nei
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.Ytelse
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.domene.Brukerinput
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.domene.Datagrunnlag
import java.time.Duration
import java.time.LocalDate
import java.time.temporal.ChronoUnit

class ErOppholdFerieOgAvsluttetForMerEnn30DagerSidenRegel(
    ytelse: Ytelse,
    private val startDatoForYtelse: LocalDate,
    private val brukerInput: Brukerinput?,

) : BasisRegel(RegelId.SP6321, ytelse) {
    val FERIE_AARSAK_TEKST = "Jeg var på ferie"
    override fun operasjon(): Resultat {
        //Hvis ingen brukersvar er oppgitt returnerer vi nei
        if (brukerInput!!.oppholdUtenforEos!!.oppholdUtenforEOS.isEmpty()){
            return nei(regelId)
        }

        var oppholdFerie = false
        var oppholdMindreEnn30Dager = false
        var oppholdAvsluttetForMerEnn30DagerSiden = false

        //Henter brukersvar om opphold utenfor EØS
        val oppholdUtenforEØS = brukerInput!!.oppholdUtenforEos!!.oppholdUtenforEOS.first()
        val oppholdSlutt = LocalDate.parse(oppholdUtenforEØS.perioder.first().tom)
        val oppholdStart = LocalDate.parse(oppholdUtenforEØS.perioder.first().fom)

        if (startDatoForYtelse.minusDays(30).isAfter(oppholdSlutt)) {
            oppholdAvsluttetForMerEnn30DagerSiden = true
        }
        if (ChronoUnit.DAYS.between(oppholdStart, oppholdSlutt) < 30) {
            oppholdMindreEnn30Dager = true
        }
        oppholdFerie = oppholdUtenforEØS.grunn.equals(FERIE_AARSAK_TEKST, true)

        if (oppholdAvsluttetForMerEnn30DagerSiden && oppholdMindreEnn30Dager && oppholdFerie){
            return ja(regelId)
        }

        return nei(regelId)

    }



    companion object {
        fun fraDatagrunnlag(datagrunnlag: Datagrunnlag): ErOppholdFerieOgAvsluttetForMerEnn30DagerSidenRegel {
            return ErOppholdFerieOgAvsluttetForMerEnn30DagerSidenRegel(
                ytelse = datagrunnlag.ytelse,
                startDatoForYtelse = datagrunnlag.periode.fom.minusDays(1),
                brukerInput = datagrunnlag.brukerinput
            )
        }
    }
}
