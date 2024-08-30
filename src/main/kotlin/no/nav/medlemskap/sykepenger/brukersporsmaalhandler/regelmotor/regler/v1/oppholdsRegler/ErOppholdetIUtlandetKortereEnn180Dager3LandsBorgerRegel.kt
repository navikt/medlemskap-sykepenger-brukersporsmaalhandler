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
import java.time.Period
import java.time.temporal.ChronoUnit

class ErOppholdetIUtlandetKortereEnn180Dager3LandsBorgerRegel(
    ytelse: Ytelse,
    startDatoForYtelse: LocalDate,
    private val brukerInput: Brukerinput?,

) : BasisRegel(RegelId.SP6414, ytelse) {

    override fun operasjon(): Resultat {

        val oppholdUtenforNorge = brukerInput!!.oppholdUtenforNorge!!.oppholdUtenforNorge.first()

        val oppholdStart = LocalDate.parse(oppholdUtenforNorge.perioder.first().fom)
        val oppholdSlutt = LocalDate.parse(oppholdUtenforNorge.perioder.first().tom)
        val daysBetween = oppholdStart.until(oppholdSlutt,ChronoUnit.DAYS)
        //val duration = Duration.between(oppholdStart,oppholdSlutt).toDays()
        if (daysBetween>180){
            return nei(regelId)
        }
        return ja(regelId)

    }



    companion object {
        fun fraDatagrunnlag(datagrunnlag: Datagrunnlag): ErOppholdetIUtlandetKortereEnn180Dager3LandsBorgerRegel {
            return ErOppholdetIUtlandetKortereEnn180Dager3LandsBorgerRegel(
                ytelse = datagrunnlag.ytelse,
                startDatoForYtelse = datagrunnlag.periode.fom,
                brukerInput = datagrunnlag.brukerinput
            )
        }
    }
}
