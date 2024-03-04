package no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.regler.v1.oppholdstilatelse


import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.BasisRegel
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.RegelId
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.Resultat
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.Resultat.Companion.ja
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.Resultat.Companion.nei
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.Ytelse
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.domene.Brukerinput
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.domene.Datagrunnlag
import java.time.LocalDate

class ErSluttDatoForMidlertidigOppholdstilatelse2mndFremTidRegel(
    ytelse: Ytelse,
    startDatoForYtelse: LocalDate,
    private val brukerInput: Brukerinput?,

) : BasisRegel(RegelId.SP6241, ytelse) {

    override fun operasjon(): Resultat {


        if (brukerInput!=null && brukerInput.oppholdstilatelse!=null
            && !brukerInput.oppholdstilatelse.vedtaksTypePermanent
            && LocalDate.parse(brukerInput.oppholdstilatelse.perioder.first().tom).isAfter(LocalDate.now().plusMonths(2))
            ){
            return ja(regelId)
        }
        return nei(regelId)

    }



    companion object {
        fun fraDatagrunnlag(datagrunnlag: Datagrunnlag): ErSluttDatoForMidlertidigOppholdstilatelse2mndFremTidRegel {
            return ErSluttDatoForMidlertidigOppholdstilatelse2mndFremTidRegel(
                ytelse = datagrunnlag.ytelse,
                startDatoForYtelse = datagrunnlag.periode.fom,
                brukerInput = datagrunnlag.brukerinput
            )
        }
    }
}
