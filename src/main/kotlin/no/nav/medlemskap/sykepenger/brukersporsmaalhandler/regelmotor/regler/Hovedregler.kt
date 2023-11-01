package no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.regler


import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.JacksonParser
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.*
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.Regel.Companion.jaKonklusjon
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.Regel.Companion.neiKonklusjon
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.Regel.Companion.uavklartKonklusjon
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.Resultat.Companion.finnÅrsaker
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.domene.GammelkjøringResultat
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.domene.Kjøring

import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.regler.v1.ReglerForBrukerSporsmaal

class Hovedregler(private val kjøring: Kjøring) {

    private val reglerForBrukerSporsmaal = ReglerForBrukerSporsmaal.fraDatagrunnlag(kjøring.datagrunnlag)
    fun kjørHovedregler(): Resultat {

        val ytelse = Ytelse.SYKEPENGER
        val resultater = mutableListOf<Resultat>()

        val fakta:MutableList<Fakta> = utledFaktaFraForrigekjoring(kjøring.resultat)
        val faktum = fakta.map { it.faktum }.toList()
        if (faktum.contains(Faktum.NORSK_BORGER)){
            //kjør flyt for Norske borgere
        }
        else if (faktum.contains(Faktum.EØS_BORGER) && !faktum.contains(Faktum.NORSK_BORGER)){
            // kjør flyt for EOS borgere

            //
        }
        else{
            //kjør 3 lands borgere flyt
        }

        val brukerspørsmålResultat = reglerForBrukerSporsmaal.kjørRegel()
        resultater.add(brukerspørsmålResultat)

        val respons = utledResultat(ytelse, resultater)
        respons.fakta = fakta
        return respons
    }

    private fun utledFaktaFraForrigekjoring(resultat: GammelkjøringResultat): MutableList<Fakta> {
        val fakta = mutableListOf<Fakta>()
        if (kjøring.resultat.erNorskBorger()){
            fakta.add(Fakta(Faktum.NORSK_BORGER, listOf(RegelId.REGEL_11.name)))
        }
        if (kjøring.resultat.erEøsBorger()){
            fakta.add(Fakta(Faktum.EØS_BORGER, listOf(RegelId.REGEL_2.name)))
        }

        if (kjøring.resultat.erFamilieEOS()){
            fakta.add(Fakta(Faktum.TREDJELANDSBORGER_MED_EOS_FAMILIE, listOf("REGEL_11","REGEL_2")))
        }


        if (!kjøring.resultat.erNorskBorger() && !kjøring.resultat.erEøsBorger() && !kjøring.resultat.erFamilieEOS()){
            fakta.add(Fakta(Faktum.TREDJELANDSBORGER, listOf("REGEL_28","REGEL_29")))
        }
        return fakta
    }


    private fun kjørReglerForTredjelandsborgere(): List<Resultat> {
        return emptyList()
    }

    private fun kjørReglerForNorskeBorgere(overstyrteRegler: Map<RegelId, Svar>): List<Resultat> {
        return emptyList()
    }

    private fun kjørReglerForEøsBorgere(
        overstyrteRegler: Map<RegelId, Svar>,
        resultatEOSFamilie: Resultat?
    ): List<Resultat> {
        val resultater = mutableListOf<Resultat>()

        if (resultatEOSFamilie?.svar == Svar.JA) {

        }
        val reglerForEØSBorgerResultater = listOf<Resultat>()

        resultater.addAll(reglerForEØSBorgerResultater)

        return resultater
    }

    private fun kjørFellesRegler(): List<Resultat> {
        return emptyList()
    }

    companion object {
        private fun utledResultat(ytelse: Ytelse, resultater: List<Resultat>): Resultat {

            val førsteNei = resultater.find { it.svar == Svar.NEI }
            if (førsteNei != null) {
                return lagKonklusjon(neiResultat(ytelse), resultater)
            }

            val medlemskonklusjon = resultater.find { it.erMedlemskonklusjon() }
            if (medlemskonklusjon != null) {
                return lagKonklusjon(konklusjon(ytelse, medlemskonklusjon), resultater)
            }

            if (resultater.all { it.svar == Svar.JA }) {
                return lagKonklusjon(jaResultat(ytelse), resultater)
            }

            return lagKonklusjon(uavklartResultat(ytelse), resultater)
        }

        private fun lagKonklusjon(konklusjon: Resultat, resultater: List<Resultat>): Resultat {
            return konklusjon.copy(delresultat = lagDelresultat(resultater), årsaker = resultater.finnÅrsaker())
        }

        private fun lagDelresultat(resultater: List<Resultat>): List<Resultat> {
            return resultater.map { if (it.regelId == RegelId.REGEL_FLYT_KONKLUSJON || it.regelId == RegelId.REGEL_MEDLEM_KONKLUSJON && it.delresultat.isNotEmpty()) it.delresultat.first() else it }
        }

        private fun konklusjon(ytelse: Ytelse, resultat: Resultat): Resultat {
            return when (resultat.svar) {
                Svar.JA -> jaKonklusjon(ytelse).utfør().copy(harDekning = resultat.harDekning, dekning = resultat.dekning)
                Svar.NEI -> neiKonklusjon(ytelse).utfør().copy(harDekning = resultat.harDekning, dekning = resultat.dekning)
                Svar.UAVKLART -> uavklartKonklusjon(ytelse).utfør().copy(harDekning = resultat.harDekning, dekning = resultat.dekning)
            }
        }

        private fun uavklartResultat(ytelse: Ytelse): Resultat {
            return uavklartKonklusjon(ytelse).utfør()
        }

        private fun neiResultat(ytelse: Ytelse): Resultat {
            return neiKonklusjon(ytelse).utfør()
        }

        private fun jaResultat(ytelse: Ytelse): Resultat {
            return jaKonklusjon(ytelse).utfør()
        }
    }
}
