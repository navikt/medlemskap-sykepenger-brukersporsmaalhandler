
package no.nav.medlemskap.sykepenger.brukersporsmaalhandler.domain

import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.Fakta
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.Resultat
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.Svar
import java.time.LocalDate


data class Konklusjon(
    val hvem: String = "SP6000",
    val dato:LocalDate,
    val status: Svar,
    val lovvalg:Lovvalg?,
    val medlemskap:Medlemskap?,
    val dekning:Dekning?,
    val fakta: List<Fakta> = emptyList(),
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

data class Dekning(
    val dekning:String,
    //val avklaringsListe: List<avklaring>
)



