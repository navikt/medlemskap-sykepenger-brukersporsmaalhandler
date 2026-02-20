package no.nav.medlemskap.cucumber.mapping

import io.cucumber.datatable.DataTable
import no.nav.medlemskap.cucumber.Domenenøkkel
import no.nav.medlemskap.cucumber.steps.BasisDomeneParser.Companion.mapDataTable
import no.nav.medlemskap.cucumber.steps.BasisDomeneParser.Companion.parseDato
import no.nav.medlemskap.cucumber.steps.RadMapper
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.domene.InputPeriode

class BasisDomeneSpraakParser {
    fun mapInputperiode(dataTable: DataTable): InputPeriode {
        return mapDataTable(dataTable, InputperiodeMapper())[0]
    }

    class InputperiodeMapper : RadMapper<InputPeriode> {
        override fun mapRad(rad: Map<String, String>): InputPeriode {
            return parseInputPeriode(rad)
        }
    }
}

fun parseInputPeriode(rad: Map<String, String>): InputPeriode {
    return InputPeriode(
        fom = parseDato(BasisDomenebegrep.FOM, rad),
        tom = parseDato(BasisDomenebegrep.TOM, rad)
    )
}

enum class BasisDomenebegrep(val nøkkel: String) : Domenenøkkel {
    FOM("fom"),
    TOM("tom")
    ;

    override fun nøkkel(): String = nøkkel
}