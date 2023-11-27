package no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.regler.v1.oppholdsRegler


import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.RegelId
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.Regelflyt
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.Regelflyt.Companion.regelflytJa
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.Regler
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.Ytelse
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.domene.Datagrunnlag
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.domene.InputPeriode
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.regler.RegelFactory

class ReglerForOppholdUtenforEOS(
    val periode: InputPeriode,
    ytelse: Ytelse,
    regelFactory: RegelFactory,
) : Regler(ytelse, regelFactory) {

    override fun hentHovedflyt(): Regelflyt {


        val harbrukerOppgittNeiIOppholdUtenforEOS = lagRegelflyt(
            regel = hentRegel(RegelId.SP6311),
            hvisJa = regelflytJa(ytelse, RegelId.SP6311),
            hvisNei = Regelflyt.medlemskonklusjonUavklart(ytelse),
        )
        val finnesBrukerSvarForOppholdUtenforEØS = lagRegelflyt(
            regel = hentRegel(RegelId.SP6301),
            hvisJa = harbrukerOppgittNeiIOppholdUtenforEOS,
            hvisNei = Regelflyt.medlemskonklusjonUavklart(ytelse),
        )


        return finnesBrukerSvarForOppholdUtenforEØS
    }

    companion object {
        fun fraDatagrunnlag(datagrunnlag: Datagrunnlag): ReglerForOppholdUtenforEOS {

            return ReglerForOppholdUtenforEOS(
                periode = datagrunnlag.periode,
                ytelse = datagrunnlag.ytelse,
                regelFactory = RegelFactory(datagrunnlag),
            )
        }
    }
}
