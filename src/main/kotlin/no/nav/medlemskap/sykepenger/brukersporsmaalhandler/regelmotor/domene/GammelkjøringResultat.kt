package no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.domene


import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.RegelId
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.Svar


data class GammelkjøringResultat (
    val regelId:String,
    val svar:Svar,
    val begrunnelse:String,
    val avklaring:String,
    val delresultat:List<GammelkjøringResultat> =listOf(),
    val årsaker:List<Årsak> =listOf(),
){
    fun erNorskBorger(): Boolean {
        return finnRegelResultat(RegelId.REGEL_11.name)?.svar == Svar.JA
    }
    fun erEøsBorger(): Boolean {
        return finnRegelResultat(RegelId.REGEL_2.name)?.svar == Svar.JA
    }
    fun erFamilieEOS(): Boolean {
        return erEktefelleEOS()
    }
    fun erTredjelandsborger(): Boolean {
        return !erEøsBorger()
    }
    fun erEktefelleEOS(): Boolean {
        return finnRegelResultat(RegelId.REGEL_28.name)?.svar == Svar.JA && finnRegelResultat(RegelId.REGEL_29.name)?.svar == Svar.JA
    }
    fun finnRegelResultat(regelid:String):GammelkjøringResultat?{
        return delresultat.flatMap { it.delresultat }.firstOrNull{it.regelId == regelid}
    }
}
data class Årsak(val regelId: String, val avklaring: String, val svar: Svar)

