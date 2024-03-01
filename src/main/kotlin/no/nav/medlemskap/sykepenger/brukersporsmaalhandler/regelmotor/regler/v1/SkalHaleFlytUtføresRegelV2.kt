package no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.regler.v1

import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.BasisRegel
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.RegelId
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.Resultat
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.Resultat.Companion.ja
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.Resultat.Companion.nei
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.Ytelse
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.domene.Brukerinput

import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.domene.GammelkjøringResultat
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.domene.Kjøring
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.domene.inneholderNyeBrukerSpørsmål


class SkalHaleFlytUtføresRegelV2(
    ytelse: Ytelse,
    private val resultatGammelRegelMotor: GammelkjøringResultat?,
    private val brukerinput: Brukerinput,

) : BasisRegel(RegelId.SP6001, ytelse) {

    override fun operasjon(): Resultat {

        if (brukerinput.inneholderNyeBrukerSpørsmål()){
            return ja(regelId)
        }

        return nei(regelId)

    }


    companion object {
        fun fraDatagrunnlag(gammelRegelmotorkjøring: Kjøring): SkalHaleFlytUtføresRegelV2 {
            return SkalHaleFlytUtføresRegelV2(
                ytelse = gammelRegelmotorkjøring.datagrunnlag.ytelse,
                resultatGammelRegelMotor = gammelRegelmotorkjøring.resultat,
                brukerinput = gammelRegelmotorkjøring.datagrunnlag.brukerinput

            )
        }
    }
}
