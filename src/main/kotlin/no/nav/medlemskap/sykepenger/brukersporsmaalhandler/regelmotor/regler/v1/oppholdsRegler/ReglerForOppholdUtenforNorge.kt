package no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.regler.v1.oppholdsRegler


import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.RegelId
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.Regelflyt
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.Regelflyt.Companion.regelflytJa
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.Regler
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.Ytelse
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.domene.Datagrunnlag
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.domene.InputPeriode
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.regler.RegelFactory

class ReglerForOppholdUtenforNorge(
    val periode: InputPeriode,
    ytelse: Ytelse,
    regelFactory: RegelFactory,
) : Regler(ytelse, regelFactory) {

    override fun hentHovedflyt(): Regelflyt {


        val harBrukerOppholdtSegUtenforNorge = lagRegelflyt(
            regel = hentRegel(RegelId.SP6411),
            hvisJa =Regelflyt.medlemskonklusjonUavklart(ytelse),
            hvisNei = regelflytJa(ytelse, RegelId.SP6311),
        )

        val finnesBrukerSvarForOppholdUtenforNorge = lagRegelflyt(
            regel = hentRegel(RegelId.SP6401),
            hvisJa = harBrukerOppholdtSegUtenforNorge,
            hvisNei = Regelflyt.medlemskonklusjonUavklart(ytelse),
        )


        return finnesBrukerSvarForOppholdUtenforNorge
    }

    companion object {
        fun fraDatagrunnlag(datagrunnlag: Datagrunnlag): ReglerForOppholdUtenforNorge {

            return ReglerForOppholdUtenforNorge(
                periode = datagrunnlag.periode,
                ytelse = datagrunnlag.ytelse,
                regelFactory = RegelFactory(datagrunnlag),
            )
        }
    }
}
