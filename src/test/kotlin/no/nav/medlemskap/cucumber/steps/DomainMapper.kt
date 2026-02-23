package no.nav.medlemskap.cucumber.steps

import io.cucumber.datatable.DataTable
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.Svar
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.domene.*
import java.time.LocalDate
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

    fun mapOppholdUtenforNorge(datatable: DataTable): OppholdUtenforNorge? {
        val rows: List<Map<String, String>> = datatable.asMaps(
            String::class.java,
            String::class.java
        )
        val haroppholdtsegutenforEØS = rows.first()["Har oppholdt seg utenfor Norge"].toBoolean()
        if (haroppholdtsegutenforEØS){
            var fom = rows.first()["Fra og med dato"]
            if (fom.equals("TODAYS_DATE")){
                fom = LocalDate.now().toString()
            }
            var tom = rows.first()["Til og med dato"]
            if (tom.equals("TODAYS_DATE")){
                tom = LocalDate.now().toString()
            }
            val land = rows.first()["LAND"]
            return OppholdUtenforNorge(id =UUID.randomUUID().toString(),
                sporsmalstekst = "spørsmåltext ",
                svar= true,
                oppholdUtenforNorge = listOf(
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
            return OppholdUtenforNorge(id =UUID.randomUUID().toString(),
                sporsmalstekst = "spørsmåltext ",
                svar= false,
                oppholdUtenforNorge = emptyList()
            )
        }
        return null
    }

    fun mapInputPeriode(datatable: DataTable): InputPeriode {
        var fomDato = LocalDate.now()
        var tomDato = LocalDate.now()
        val rows: List<Map<String, String>> = datatable.asMaps(
            String::class.java,
            String::class.java
        )

            var fom = rows.first()["Fra og med dato"]
            var tom = rows.first()["Til og med dato"]
            try{
                fomDato = LocalDate.parse(fom)
                tomDato = LocalDate.parse(tom)

            }
            catch (e:Exception){
              throw e
            }
        return InputPeriode(fomDato,tomDato)
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
