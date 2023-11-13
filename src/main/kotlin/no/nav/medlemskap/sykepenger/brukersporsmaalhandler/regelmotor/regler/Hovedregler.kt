package no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.regler

import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.*
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.Regel.Companion.jaKonklusjon
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.Regel.Companion.neiKonklusjon
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.Regel.Companion.uavklartKonklusjon
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.Resultat.Companion.finnÅrsaker
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.domene.GammelkjøringResultat
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.domene.Kjøring
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.regler.v1.ArbeidUtenforNorgeRegelFlyt
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.regler.v1.SkalHaleFlytUtføresRegel

import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.regler.v1.oppholdsRegler.ReglerForOppholdUtenforEOS
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.regler.v1.regelutsjekk.ReglerForUtsjekkAvGammelRegelMotorNorskeBorgere
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.regler.v1.regelutsjekk.eosborgere.ReglerForUtsjekkAvGammelRegelMotorEOSBorgere

class Hovedregler(private val kjøring: Kjøring) {

    fun kjørHovedregler(): Resultat {

        val ytelse = Ytelse.SYKEPENGER
        val resultater = mutableListOf<Resultat>()

        val utledetInformasjon:MutableList<UtledetInformasjon> = utledFaktaFraForrigekjoring(kjøring.resultat)
        val skalHaleFlytkjøresResultat = SkalHaleFlytUtføresRegel.fraDatagrunnlag(kjøring).utfør()
        resultater.add(skalHaleFlytkjøresResultat)
        if (Svar.JA == skalHaleFlytkjøresResultat.svar){

            val arbeidItoLand = ArbeidUtenforNorgeRegelFlyt.fraDatagrunnlag(kjøring.datagrunnlag).kjørHovedflyt()
            resultater.add(arbeidItoLand)

            val faktum = utledetInformasjon.map { it.informasjon }.toList()
            if (faktum.contains(Informasjon.NORSK_BORGER)){
                resultater.addAll(kjørReglerForNorskeBorgere())
            }
            else if (faktum.contains(Informasjon.EØS_BORGER) && !faktum.contains(Informasjon.NORSK_BORGER)){
                resultater.addAll(kjørReglerForEøsBorgere())
            }
            else{
                resultater.addAll(kjørReglerForTredjelandsborgere())
            }
        }
        else{
            //hale skal ikke kjøres!!!

            val respons =  lagKonklusjon(uavklartResultat(ytelse), resultater)
            respons.utledetInformasjon = utledetInformasjon
            return respons
        }

        val respons = utledResultat(ytelse, resultater)
        respons.utledetInformasjon = utledetInformasjon
        return respons
    }

    private fun utledFaktaFraForrigekjoring(resultat: GammelkjøringResultat): MutableList<UtledetInformasjon> {
        val fakta = mutableListOf<UtledetInformasjon>()
        if (kjøring.resultat.erNorskBorger()){
            fakta.add(UtledetInformasjon(Informasjon.NORSK_BORGER, listOf(RegelId.REGEL_11.name)))
        }
        if (kjøring.resultat.erEøsBorger()){
            fakta.add(UtledetInformasjon(Informasjon.EØS_BORGER, listOf(RegelId.REGEL_2.name)))
        }

        if (kjøring.resultat.erFamilieEOS()){
            fakta.add(UtledetInformasjon(Informasjon.TREDJELANDSBORGER_MED_EOS_FAMILIE, listOf("REGEL_11","REGEL_2")))
        }


        if (!kjøring.resultat.erNorskBorger() && !kjøring.resultat.erEøsBorger() && !kjøring.resultat.erFamilieEOS()){
            fakta.add(UtledetInformasjon(Informasjon.TREDJELANDSBORGER, listOf("REGEL_28","REGEL_29")))
        }
        return fakta
    }


    private fun kjørReglerForTredjelandsborgere(): List<Resultat> {
        return emptyList()
    }

    private fun kjørReglerForNorskeBorgere(): List<Resultat> {
        return listOf(
            ReglerForOppholdUtenforEOS.fraDatagrunnlag(kjøring.datagrunnlag),
            ReglerForUtsjekkAvGammelRegelMotorNorskeBorgere.fraDatagrunnlag(kjøring.datagrunnlag,kjøring.resultat.årsaker),
        ).map { it.kjørHovedflyt() }
    }

    private fun kjørReglerForEøsBorgere(): List<Resultat> {
        return listOf(
            ReglerForOppholdUtenforEOS.fraDatagrunnlag(kjøring.datagrunnlag),
            ReglerForUtsjekkAvGammelRegelMotorEOSBorgere.fraDatagrunnlag(kjøring.datagrunnlag,kjøring.resultat.årsaker),
        ).map { it.kjørHovedflyt() }
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
