package no.nav.medlemskap.cucumber.steps

import io.cucumber.datatable.DataTable
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.Svar
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.domene.*
import java.util.*

class DomainMapper {
    fun mapArbeidUtlandNyModell(datatable: DataTable) : UtfortAarbeidUtenforNorge {
        val rows: List<Map<String, String>> = datatable.asMaps(
            String::class.java,
            String::class.java
        )

        val harArbeidetIUtlanderNyModell = rows.first()["Har hatt arbeid utenfor Norge"].toBoolean()
        if (harArbeidetIUtlanderNyModell){
            val fom = rows.first()["Fra og med dato"]
            val tom = rows.first()["Til og med dato"]
            val land = rows.first()["LAND"]
            return UtfortAarbeidUtenforNorge(id =UUID.randomUUID().toString(),
                sporsmalstekst = "spørsmåltext ",
                svar= true,
                arbeidUtenforNorge = listOf(
                    ArbeidUtenforNorge(
                    id = "",
                    arbeidsgiver = "",
                    land=land!!,
                    perioder = listOf(Periode(fom!!,tom!!))
                    )
                               )
            )
         }
        else{
            return UtfortAarbeidUtenforNorge(id =UUID.randomUUID().toString(),
                sporsmalstekst = "spørsmåltext ",
                svar= false,
                arbeidUtenforNorge = emptyList())
        }

    }

    fun mapOppholdUtenforEos(datatable: DataTable): OppholdUtenforEos? {
        val rows: List<Map<String, String>> = datatable.asMaps(
            String::class.java,
            String::class.java
        )
        val haroppholdtsegutenforEØS = rows.first()["Har oppholdt seg utenfor EØS"].toBoolean()
        if (haroppholdtsegutenforEØS){
            val fom = rows.first()["Fra og med dato"]
            val tom = rows.first()["Til og med dato"]
            val land = rows.first()["LAND"]
            return OppholdUtenforEos(id =UUID.randomUUID().toString(),
                sporsmalstekst = "spørsmåltext ",
                svar= true,
                oppholdUtenforEOS = listOf(
                    Opphold(
                        id = "",
                        land=land!!,
                        grunn = "",
                        perioder = listOf(Periode(fom!!,tom!!))
                    )
                )
            )
        }
        else{
            return OppholdUtenforEos(id =UUID.randomUUID().toString(),
                sporsmalstekst = "spørsmåltext ",
                svar= false,
                oppholdUtenforEOS = emptyList()
            )
        }
        return null
    }
    fun mapÅrsaker(datatable: DataTable): List<Årsak> {
        val rows: List<Map<String, String>> = datatable.asMaps(
            String::class.java,
            String::class.java
        )
        val årsaker = rows.map { it.get("REGELBRUDD") }.toList().filter { it!!.isNotEmpty() }
        val list = årsaker.map { Årsak(it!!,"",Svar.UAVKLART) }
        return list
    }



}
