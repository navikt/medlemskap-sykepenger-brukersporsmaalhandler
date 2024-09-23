
package no.nav.medlemskap.sykepenger.brukersporsmaalhandler.domain

import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.*
import java.time.LocalDate


data class Konklusjon(
    val hvem: String = "SP6000",
    val dato:LocalDate,
    val status: Svar,
    val lovvalg:Lovvalg?,
    val medlemskap:Medlemskap?,
    val dekningForSP:DekningsAltrnativer = DekningsAltrnativer.UAVKLART,
    val utledetInformasjoner: List<UtledetInformasjon> = emptyList(),
    val reglerKjørt: List<Resultat> = listOf(),
    val avklaringsListe: List<avklaring> = emptyList()
)
data class avklaring(
    val regel_id:String,
    val avklaringstekst:String,
    val svar:String,
    val status:String,
    val beskrivelse:String?,
    val hvem:String,
    val tidspunkt:LocalDate
)

enum class Status{UAVKLART,JA,NEI}

data class Lovvalg(
    val lovvalgsland:String,
   // val avklaringsListe: List<avklaring>
)
data class Medlemskap(
    val erMedlem:String,
    val ftlHjemmel:String,
    //val avklaringsListe: List<avklaring>
)



enum class DekningsAltrnativer{
    JA,
    NEI,
    UAVKLART

}

enum class Trygdeavtale{ //er omfattet av en trygdeavtale
    JA,
    NEI,
    UAVKLART

}

fun Konklusjon.finnRegelKjøring(regelID:RegelId):Resultat?{

    return (reglerKjørt + reglerKjørt.flatMap { it.delresultat }).find { it.regelId == regelID }
}
//utledet_informasjon

fun Konklusjon.erTredjelandsborger():Boolean{
    return utledetInformasjoner.any { (it.informasjon == Informasjon.TREDJELANDSBORGER_MED_EOS_FAMILIE) || it.informasjon == Informasjon.TREDJELANDSBORGER }
}
