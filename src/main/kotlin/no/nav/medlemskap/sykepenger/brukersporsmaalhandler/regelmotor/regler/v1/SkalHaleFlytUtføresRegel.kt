package no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.regler.v1

import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.BasisRegel
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.RegelId
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.Resultat
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.Resultat.Companion.ja
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.Resultat.Companion.nei
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.Ytelse

import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.domene.GammelkjøringResultat
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.domene.Kjøring


class SkalHaleFlytUtføresRegel(
    ytelse: Ytelse,
    private val resultatGammelRegelMotor: GammelkjøringResultat?,

) : BasisRegel(RegelId.SP6001, ytelse) {

    override fun operasjon(): Resultat {

        if (1 == resultatGammelRegelMotor?.årsaker?.size  && resultatGammelRegelMotor.årsaker.first().regelId == "REGEL_3"){
            return ja(regelId)
        }
        if (2 == resultatGammelRegelMotor?.årsaker?.size  &&
            resultatGammelRegelMotor.årsaker
            .map { it.regelId }
            .containsAll(listOf("REGEL_3","REGEL_9"))
            ) {
            return ja(regelId)
            }
        else{
            return nei(regelId)
        }
    }


    companion object {
        fun fraDatagrunnlag(gammelRegelmotorkjøring: Kjøring): SkalHaleFlytUtføresRegel {
            return SkalHaleFlytUtføresRegel(
                ytelse = gammelRegelmotorkjøring.datagrunnlag.ytelse,
                resultatGammelRegelMotor = gammelRegelmotorkjøring.resultat
            )
        }
    }
}
