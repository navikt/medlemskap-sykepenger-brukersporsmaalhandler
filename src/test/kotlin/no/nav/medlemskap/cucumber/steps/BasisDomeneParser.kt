package no.nav.medlemskap.cucumber.steps

import io.cucumber.datatable.DataTable
import no.nav.medlemskap.cucumber.Domenenøkkel
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.Datohjelper
import java.time.LocalDate

abstract class BasisDomeneParser {
    companion object {
        fun parseDato(domeneBegrep: Domenenøkkel, rad: Map<String, String>): LocalDate {
            return parseDato(domeneBegrep.nøkkel(), rad)
        }

        fun parseValgfriDato(domeneBegrep: Domenenøkkel, rad: Map<String, String>): LocalDate? {
            return parseValgfriDato(domeneBegrep.nøkkel(), rad)
        }

        fun parseBooleanMedBooleanVerdi(domeneBegrep: Domenenøkkel, rad: Map<String,String>): Boolean {
            val verdi = verdi(domeneBegrep.nøkkel(), rad)

            return when (verdi) {
                "true" -> true
                else -> false
            }
        }

        fun parseBoolean(domeneBegrep: Domenenøkkel, rad: Map<String, String>): Boolean {
            val verdi = verdi(domeneBegrep.nøkkel(), rad)

            return when (verdi) {
                "Ja" -> true
                else -> false
            }
        }

        fun parseValgfriBoolean(domenebegrep: String, rad: Map<String, String?>): Boolean? {
            if (rad.get(domenebegrep) == null || rad.get(domenebegrep) == "") return null

            return when(rad.get(domenebegrep)) {
                "Ja" -> true
                "Nei" -> false
                else -> null
            }
        }

        fun parseString(domeneBegrep: Domenenøkkel, rad: Map<String, String>): String {
            return verdi(domeneBegrep.nøkkel(), rad)
        }

        fun parseValgfriString(domeneBegrep: Domenenøkkel, rad: Map<String, String>): String? = valgfriVerdi(domeneBegrep.nøkkel(), rad)

        fun parseDato(domeneBegrep: String, rad: Map<String, String>): LocalDate {
            return Datohjelper.parseDato(verdi(domeneBegrep,rad))
        }

        fun parseValgfriDato(domeneBegrep: String, rad: Map<String, String?>): LocalDate? {
            return if(rad.get(domeneBegrep) == null || rad.get(domeneBegrep) == "") null
            else Datohjelper.parseDato(rad.get(domeneBegrep)!!)
        }

        fun verdi(nøkkel: String, rad: Map<String, String>): String {
            val verdi = rad.get(nøkkel)

            if (verdi == null || verdi == "") {
                throw java.lang.RuntimeException("Fant ingen verdi for $nøkkel")
            }
            return verdi
        }

        fun valgfriVerdi(nøkkel: String, rad: Map<String, String>): String? = rad.get(nøkkel)

        fun <T> mapDataTable(dataTable: DataTable, radMapper: RadMapper<T>): List<T> {
            return dataTable.asMaps().map { radMapper.mapRad(it) }
        }
    }
}

interface RadMapper<T> {
    fun mapRad(rad: Map<String, String>): T
}