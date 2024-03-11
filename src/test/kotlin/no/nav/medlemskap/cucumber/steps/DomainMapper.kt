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

    fun mapOppholdsTilatelse(datatable: DataTable): Oppholdstilatelse? {
        val rows: List<Map<String, String>> = datatable.asMaps(
            String::class.java,
            String::class.java
        )
        val permanent = rows.first()["Permanent"].toBoolean()
        if (permanent){
            var fom = rows.first()["FOM"]
            if (fom.equals("TODAYS_DATE")){
                fom = LocalDate.now().toString()
            }

            var vdate = rows.first()["VDATO"]
            if (vdate.equals("TODAYS_DATE")){
                vdate = LocalDate.now().toString()
            }
            return Oppholdstilatelse(
                id = UUID.randomUUID().toString(),
                sporsmalstekst = "",
                svar = true,
                vedtaksdato = LocalDate.parse(vdate),
                vedtaksTypePermanent = true,
                perioder = listOf(Periode(fom!!,LocalDate.MAX.toString()))
            )
        }
        else{
            var fom = rows.first()["FOM"]
            if (fom.equals("TODAYS_DATE")){
                fom = LocalDate.now().toString()
            }
            var tom = rows.first()["TOM"]
            if (tom.equals("TODAYS_DATE")){
                tom = LocalDate.now().toString()
            }
            val vdate = rows.first()["VDATO"]
            return Oppholdstilatelse(
                id = UUID.randomUUID().toString(),
                sporsmalstekst = "",
                svar = true,
                vedtaksdato = LocalDate.parse(vdate),
                vedtaksTypePermanent = false,
                perioder = listOf(Periode(fom!!,tom!!))
            )
        }
        return null
    }
    fun mapOppholdUtenforEos(datatable: DataTable): OppholdUtenforEos? {
        val rows: List<Map<String, String>> = datatable.asMaps(
            String::class.java,
            String::class.java
        )
        val haroppholdtsegutenforEØS = rows.first()["Har oppholdt seg utenfor EØS"].toBoolean()
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
