package no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.regler.v1

import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.BasisRegel
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.RegelId
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.Resultat
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.Resultat.Companion.ja
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.Resultat.Companion.nei
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.Ytelse
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.domene.*


class HarBrukerNorskMedlemskapIMedlSiste28DagerRegel(
    ytelse: Ytelse,
    private val resultatGammelRegelMotor: GammelkjøringResultat?,
    private val brukerinput: Brukerinput,
    val datagrunnlag:Datagrunnlag,

) : BasisRegel(RegelId.SP6002, ytelse) {

    override fun operasjon(): Resultat {
        val medlemskapsListe = datagrunnlag.medlemskap.filter { it.periodeStatus=="GYLD"  && it.lovvalgsland=="NOR"}
        val found = medlemskapsListe.any { it.tilOgMed.isAfter(datagrunnlag.periode.tom) && it.fraOgMed.isBefore(datagrunnlag.periode.fom.plusDays(28)) }
        if (found) {
            return ja(regelId)
        }
       else{
           return nei(regelId)
        }

    }


    companion object {
        fun fraDatagrunnlag(gammelRegelmotorkjøring: Kjøring): HarBrukerNorskMedlemskapIMedlSiste28DagerRegel {
            return HarBrukerNorskMedlemskapIMedlSiste28DagerRegel(
                ytelse = gammelRegelmotorkjøring.datagrunnlag.ytelse,
                resultatGammelRegelMotor = gammelRegelmotorkjøring.resultat,
                brukerinput = gammelRegelmotorkjøring.datagrunnlag.brukerinput,
                datagrunnlag = gammelRegelmotorkjøring.datagrunnlag

            )
        }
    }
}
