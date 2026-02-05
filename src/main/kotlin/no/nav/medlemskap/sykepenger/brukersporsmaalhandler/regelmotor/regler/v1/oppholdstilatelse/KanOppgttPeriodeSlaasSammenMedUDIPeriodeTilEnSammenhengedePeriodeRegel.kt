package no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.regler.v1.oppholdstilatelse


import mu.KotlinLogging
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.BasisRegel
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.RegelId
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.Resultat
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.Resultat.Companion.ja
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.Resultat.Companion.nei
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.Ytelse
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.domene.*
import java.time.LocalDate
import java.time.temporal.ChronoUnit

class KanOppgttPeriodeSlaasSammenMedUDIPeriodeTilEnSammenhengedePeriodeRegel(
    ytelse: Ytelse,
    startDatoForYtelse: LocalDate,
    private val brukerInput: Brukerinput?,
    private val udiOpphold:UdiOppholdsTilatelse?


) : BasisRegel(RegelId.SP6225, ytelse) {

    private val logger = KotlinLogging.logger { }

    val numberofDaysSlack = 1L

    fun isDateWithinRange(dateToCheck: LocalDate, referenceDate: LocalDate, daysRange: Long = 5): Boolean {
        val daysBetween = ChronoUnit.DAYS.between(referenceDate, dateToCheck)
        return daysBetween in -daysRange..daysRange
    }

    override fun operasjon(): Resultat {

        if (udiOpphold == null ||
            udiOpphold.periode() == null  ||
            brukerInput!!.oppholdstilatelse!!.svar == false
        ){
            return nei(regelId)
        }
        var brukerInputTomDato:LocalDate = LocalDate.MIN


        try {
             brukerInputTomDato = LocalDate.parse(brukerInput!!.oppholdstilatelse!!.perioder.first().tom)
        }
        catch (e:Exception){
        //vi bruker LocalDate.MIN som default value..
        }

        /*
       * håndter PERMANENT oppholdstilatelse
       * */
        if (udiOpphold.gjeldendeOppholdsstatus?.oppholdstillatelsePaSammeVilkar?.type == "PERMANENT"){
            if (brukerInputTomDato.isAfter(udiOpphold.periode()!!.fom) || brukerInputTomDato.isEqual(udiOpphold.periode()!!.fom)){
                return ja(regelId)
            }
            return nei(regelId)
        }
        /*
        * Håndter midlertidige oppholdstilatelser fra UDI
        * */

        try {
            if (isDateWithinRange(brukerInputTomDato,
                    udiOpphold.gjeldendeOppholdsstatus!!.oppholdstillatelsePaSammeVilkar!!.periode.fom!!,numberofDaysSlack)){
                return ja(regelId)
            }
        } catch (ex: Exception) {
            logger.info("Ingen fom dato funnet for oppholdstillatelse fra UDI")
        }

        return nei(regelId)

    }




    companion object {
        fun fraDatagrunnlag(datagrunnlag: Datagrunnlag): KanOppgttPeriodeSlaasSammenMedUDIPeriodeTilEnSammenhengedePeriodeRegel {
            return KanOppgttPeriodeSlaasSammenMedUDIPeriodeTilEnSammenhengedePeriodeRegel(
                ytelse = datagrunnlag.ytelse,
                startDatoForYtelse = datagrunnlag.periode.fom,
                brukerInput = datagrunnlag.brukerinput,
                udiOpphold = datagrunnlag.oppholdstillatelse
            )
        }
    }
}
