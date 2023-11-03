package no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.regler.v1.norskeborgere


import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.RegelId
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.Regelflyt
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.Regelflyt.Companion.regelflytJa
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.Regelflyt.Companion.regelflytNei
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.Regelflyt.Companion.regelflytUavklart
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.Regler
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.Resultat.Companion.nei
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


        val harbrukerOppgittJaIOppholdUtenforEOS = lagRegelflyt(
            regel = hentRegel(RegelId.SP6002),
            hvisJa = Regelflyt.medlemskonklusjonUavklart(ytelse),
            hvisNei = regelflytNei(ytelse, RegelId.SP6002)
        )

        val finnesBrukerSvarForOppholdUtenforEØS = lagRegelflyt(
            regel = hentRegel(RegelId.SP6003),
            hvisJa = harbrukerOppgittJaIOppholdUtenforEOS,
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
