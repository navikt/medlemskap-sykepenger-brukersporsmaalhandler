package no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.regler.v1.oppholdsRegler


import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.BasisRegel
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.RegelId
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.Resultat
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.Resultat.Companion.ja
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.Resultat.Companion.nei
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.Ytelse
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.domene.Brukerinput
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.domene.Datagrunnlag
import java.time.LocalDate

class FinnesBrukerSvarForOppholdUtenforEØSRegel(
    ytelse: Ytelse,
    startDatoForYtelse: LocalDate,
    private val brukerInput: Brukerinput?,

) : BasisRegel(RegelId.SP6301, ytelse) {

    override fun operasjon(): Resultat {


        if (brukerInput!=null && brukerInput.oppholdUtenforEos==null){
            return nei(regelId)
        }
        return ja(regelId)

    }



    companion object {
        fun fraDatagrunnlag(datagrunnlag: Datagrunnlag): FinnesBrukerSvarForOppholdUtenforEØSRegel {
            return FinnesBrukerSvarForOppholdUtenforEØSRegel(
                ytelse = datagrunnlag.ytelse,
                startDatoForYtelse = datagrunnlag.periode.fom,
                brukerInput = datagrunnlag.brukerinput
            )
        }
    }
}
