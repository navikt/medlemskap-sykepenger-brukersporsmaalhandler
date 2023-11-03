package no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.regler.v1



import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.regler.RegelFactory
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.*
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.Regelflyt.Companion.konklusjonJa
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.Regelflyt.Companion.medlemskonklusjonUavklart
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.domene.Datagrunnlag
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.domene.InputPeriode

class ReglerForBrukerSporsmaal(
    val periode: InputPeriode,
    ytelse: Ytelse,
    regelFactory: RegelFactory,
) : Regler(ytelse, regelFactory, emptyMap()) {

    override fun hentHovedflyt(): Regelflyt {

        return lagRegelflyt(
            regel = hentRegel(RegelId.SP6001),
            hvisJa = medlemskonklusjonUavklart(ytelse),
            hvisNei = konklusjonJa(ytelse)
        )
    }

    fun kjørRegel(): Resultat {
        return hentHovedflyt().utfør()
    }

    companion object {
        fun fraDatagrunnlag(datagrunnlag: Datagrunnlag): ReglerForBrukerSporsmaal {
            with(datagrunnlag) {
                return ReglerForBrukerSporsmaal(
                    periode = periode,
                    ytelse = ytelse,
                    regelFactory = RegelFactory(datagrunnlag)
                )
            }
        }
    }
}
