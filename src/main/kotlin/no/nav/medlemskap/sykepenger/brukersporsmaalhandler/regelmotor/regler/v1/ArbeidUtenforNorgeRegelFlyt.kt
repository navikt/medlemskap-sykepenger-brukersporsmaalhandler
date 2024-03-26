package no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.regler.v1



import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.regler.RegelFactory
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.*
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.Regelflyt.Companion.konklusjonUavklart
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.Regelflyt.Companion.medlemskonklusjonUavklart
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.Regelflyt.Companion.regelflytJa
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.domene.Datagrunnlag
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.domene.InputPeriode

/*
* SP6100 Regel flyt for arbeid utenfor norge.
* Kjøres for alle borgere uavhengig av regelverk (Norske borgere, EØS borgere og 3 lands borgere)
* */

class ArbeidUtenforNorgeRegelFlyt(
    val periode: InputPeriode,
    ytelse: Ytelse,
    regelFactory: RegelFactory,
) : Regler(ytelse, regelFactory, emptyMap()) {

    override fun hentHovedflyt(): Regelflyt {

       val arbeidUtlandOppgittGammelModellRegel = lagRegelflyt(
           regel = hentRegel(RegelId.SP6130),
           hvisJa = konklusjonUavklart(ytelse,RegelId.ARBEID_UTLAND_FLYT),
           hvisNei = regelflytJa(ytelse,RegelId.ARBEID_UTLAND_FLYT),
           hvisUavklart = medlemskonklusjonUavklart(ytelse)
       )

        val harBrukerSvartJAIArbeidUtlandNyModell = lagRegelflyt(
            regel = hentRegel(RegelId.SP6120),
            hvisJa = konklusjonUavklart(ytelse,RegelId.ARBEID_UTLAND_FLYT),
            hvisNei = regelflytJa(ytelse,RegelId.ARBEID_UTLAND_FLYT),

        )

        val harBrukerOppgittArbeidUtenforNorgeNyModell = lagRegelflyt(
            regel = hentRegel(RegelId.SP6110),
            hvisJa = harBrukerSvartJAIArbeidUtlandNyModell,
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
