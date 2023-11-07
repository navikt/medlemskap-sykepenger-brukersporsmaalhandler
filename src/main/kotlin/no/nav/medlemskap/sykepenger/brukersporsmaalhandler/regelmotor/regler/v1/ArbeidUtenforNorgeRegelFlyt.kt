package no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.regler.v1



import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.regler.RegelFactory
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.*
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.Regelflyt.Companion.konklusjonNei
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.Regelflyt.Companion.konklusjonUavklart
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.Regelflyt.Companion.medlemskonklusjonUavklart
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.domene.Datagrunnlag
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.domene.InputPeriode

class ArbeidUtenforNorgeRegelFlyt(
    val periode: InputPeriode,
    ytelse: Ytelse,
    regelFactory: RegelFactory,
) : Regler(ytelse, regelFactory, emptyMap()) {

    override fun hentHovedflyt(): Regelflyt {

       val arbeidUtlandOppgittGammelModellRegel = lagRegelflyt(
           regel = hentRegel(RegelId.SP6100),
           hvisJa = konklusjonUavklart(ytelse,RegelId.ARBEID_I_TO_LAND),
           hvisNei = konklusjonNei(ytelse,RegelId.ARBEID_I_TO_LAND),
           hvisUavklart = medlemskonklusjonUavklart(ytelse)
       )

        val harBrukerOppgittArbeidUtenforNorgeNyModell = lagRegelflyt(
            regel = hentRegel(RegelId.SP6110),
            hvisJa = konklusjonUavklart(ytelse,RegelId.ARBEID_I_TO_LAND),
            hvisNei = arbeidUtlandOppgittGammelModellRegel
        )

        return harBrukerOppgittArbeidUtenforNorgeNyModell
    }

    fun kjørRegel(): Resultat {
        return hentHovedflyt().utfør()
    }

    companion object {
        fun fraDatagrunnlag(datagrunnlag: Datagrunnlag): ArbeidUtenforNorgeRegelFlyt {
            with(datagrunnlag) {
                return ArbeidUtenforNorgeRegelFlyt(
                    periode = periode,
                    ytelse = ytelse,
                    regelFactory = RegelFactory(datagrunnlag)
                )
            }
        }
    }
}
