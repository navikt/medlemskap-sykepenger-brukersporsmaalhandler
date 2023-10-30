
package no.nav.medlemskap.sykepenger.brukersporsmaalhandler.domain

import java.time.LocalDate


data class Konklusjon(
    val dato:LocalDate,
    val status:Status,
    val lovvalg:Lovvalg?,
    val medlemskap:Medlemskap?,
    val dekning:Dekning?,
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



