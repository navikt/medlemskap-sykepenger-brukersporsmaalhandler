package no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.regler.v1.oppholdstilatelse


import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.BasisRegel
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.RegelId
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.Resultat
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.Resultat.Companion.ja
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.Resultat.Companion.nei
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.Ytelse
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.domene.Brukerinput
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.domene.Datagrunnlag
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.domene.PdlOppholdsTilatelse
import java.time.LocalDate
import java.time.temporal.ChronoUnit

class ErOppgittOppholdstilatelseFunksjoneltLiktPDLInnslagRegel(
    ytelse: Ytelse,
    startDatoForYtelse: LocalDate,
    private val brukerInput: Brukerinput?,
    private val pdlOpphold:List<PdlOppholdsTilatelse>

) : BasisRegel(RegelId.SP6229, ytelse) {

    final val numberofDaysSlack = 1L

    fun isDateWithinRange(dateToCheck: LocalDate, referenceDate: LocalDate, daysRange: Long = 5): Boolean {
        val daysBetween = ChronoUnit.DAYS.between(referenceDate, dateToCheck)
        return daysBetween in -daysRange..daysRange
    }
    override fun operasjon(): Resultat {

        var aktuellPdlOppholdsTilatelse:PdlOppholdsTilatelse? = null
        if (brukerInput!=null && brukerInput.oppholdstilatelse!=null){
            val fom = LocalDate.parse(brukerInput.oppholdstilatelse.perioder.first().fom)
            val tom = LocalDate.parse(brukerInput.oppholdstilatelse.perioder.first().tom)
            aktuellPdlOppholdsTilatelse = pdlOpphold.filter {
                it.oppholdFra!=null &&
                it.oppholdTil!=null &&
                    (
                    isDateWithinRange(fom,it.oppholdFra,numberofDaysSlack) &&
                    isDateWithinRange(tom,it.oppholdTil,numberofDaysSlack)
                    )
            }.firstOrNull()
        }

       if (aktuellPdlOppholdsTilatelse!=null){
           return ja(regelId)
       }
        else{
            return nei(regelId)
       }

    }



    companion object {
        fun fraDatagrunnlag(datagrunnlag: Datagrunnlag): ErOppgittOppholdstilatelseFunksjoneltLiktPDLInnslagRegel {
            return ErOppgittOppholdstilatelseFunksjoneltLiktPDLInnslagRegel(
                ytelse = datagrunnlag.ytelse,
                startDatoForYtelse = datagrunnlag.periode.fom,
                brukerInput = datagrunnlag.brukerinput,
                pdlOpphold = datagrunnlag.pdlpersonhistorikk.oppholdstilatelser
            )
        }
    }
}
