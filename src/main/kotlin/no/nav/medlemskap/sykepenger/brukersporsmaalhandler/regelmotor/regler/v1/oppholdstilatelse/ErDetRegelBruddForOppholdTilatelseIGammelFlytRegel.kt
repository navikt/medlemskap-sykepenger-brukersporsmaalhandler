package no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.regler.v1.oppholdstilatelse


import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.*
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.Resultat.Companion.ja
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.Resultat.Companion.nei
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.domene.Brukerinput
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.domene.Datagrunnlag
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.domene.GammelkjøringResultat
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.domene.Kjøring
import java.time.LocalDate

class ErDetRegelBruddForOppholdTilatelseIGammelFlytRegel(
    ytelse: Ytelse,
    private val gammelkjøringResultat: GammelkjøringResultat?,

) : BasisRegel(RegelId.SP6201, ytelse) {

    override fun operasjon(): Resultat {

        val resultatRegelFlytOpphTilatelse = gammelkjøringResultat!!.delresultat.find { it.regelId=="REGEL_UDI_LOVLIG_OPPHOLD" }

        if (resultatRegelFlytOpphTilatelse == null){
            return nei(regelId)
            }

        if (resultatRegelFlytOpphTilatelse!=null && Svar.JA == resultatRegelFlytOpphTilatelse.svar){
             return nei(regelId)
        }

        return ja(regelId)

    }



    companion object {
        fun fraDatagrunnlag(datagrunnlag: Datagrunnlag,gammelkjøringResultat: GammelkjøringResultat?): ErDetRegelBruddForOppholdTilatelseIGammelFlytRegel {
            return ErDetRegelBruddForOppholdTilatelseIGammelFlytRegel(
                ytelse = datagrunnlag.ytelse,
                gammelkjøringResultat = gammelkjøringResultat
            )
        }
    }
}
