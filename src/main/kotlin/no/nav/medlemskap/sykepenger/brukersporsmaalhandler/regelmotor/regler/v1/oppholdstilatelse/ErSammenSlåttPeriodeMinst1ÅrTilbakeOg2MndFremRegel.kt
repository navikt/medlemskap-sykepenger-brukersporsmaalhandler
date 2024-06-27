package no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.regler.v1.oppholdstilatelse


import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.BasisRegel
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.RegelId
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.Resultat
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.Resultat.Companion.ja
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.Resultat.Companion.nei
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.Ytelse
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.domene.*
import java.time.LocalDate
import java.time.temporal.ChronoUnit

class ErSammenSlåttPeriodeMinst1ÅrTilbakeOg2MndFremRegel(
    ytelse: Ytelse,
    private val startDatoForYtelse: LocalDate,
    private val brukerInput: Brukerinput?,
    private val udiOpphold: UdiOppholdsTilatelse?

) : BasisRegel(RegelId.SP6226, ytelse) {


    override fun operasjon(): Resultat {

        if (brukerInput!!.oppholdstilatelse == null || udiOpphold == null || udiOpphold.periode() == null ){
            return nei(regelId)
        }

        var brukerinputFom:LocalDate = LocalDate.MAX
        try{
           brukerinputFom = LocalDate.parse(brukerInput.oppholdstilatelse!!.perioder.first().fom)
        }
        catch (e:Exception){
            //brukerinputFom LocalDate.max default valure
        }
        if (
            brukerinputFom.isBefore(startDatoForYtelse.minusYears(1)) &&
            (
                    udiOpphold.gjeldendeOppholdsstatus?.oppholdstillatelsePaSammeVilkar?.periode!!.tom == null ||
                    udiOpphold.gjeldendeOppholdsstatus?.oppholdstillatelsePaSammeVilkar?.periode.tom!!.isAfter(startDatoForYtelse.plusMonths(2)))
            ) {
            return ja(regelId)
        }

        return nei(regelId)


    }



    companion object {
        fun fraDatagrunnlag(datagrunnlag: Datagrunnlag): ErSammenSlåttPeriodeMinst1ÅrTilbakeOg2MndFremRegel {
            return ErSammenSlåttPeriodeMinst1ÅrTilbakeOg2MndFremRegel(
                ytelse = datagrunnlag.ytelse,
                startDatoForYtelse = datagrunnlag.periode.fom,
                brukerInput = datagrunnlag.brukerinput,
                udiOpphold = datagrunnlag.oppholdstillatelse
            )
        }
    }
}
