package no.nav.medlemskap.cucumber.mapping.udi

import io.cucumber.datatable.DataTable
import no.nav.medlemskap.cucumber.Domenenøkkel
import no.nav.medlemskap.cucumber.steps.BasisDomeneParser.Companion.mapDataTable
import no.nav.medlemskap.cucumber.steps.BasisDomeneParser.Companion.parseDato
import no.nav.medlemskap.cucumber.steps.BasisDomeneParser.Companion.parseValgfriBoolean
import no.nav.medlemskap.cucumber.steps.BasisDomeneParser.Companion.parseValgfriDato
import no.nav.medlemskap.cucumber.steps.BasisDomeneParser.Companion.parseValgfriString
import no.nav.medlemskap.cucumber.steps.RadMapper
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.domene.*

class UdiDomeneSpraakParser {
    fun mapUdiOppholdstillatelse(dataTable: DataTable): UdiOppholdsTilatelse? {
        return mapDataTable(dataTable, UdiOppholdstillatelseMapper())[0]
    }

    class UdiOppholdstillatelseMapper : RadMapper<UdiOppholdsTilatelse> {
        override fun mapRad(rad: Map<String, String>): UdiOppholdsTilatelse {
            return UdiOppholdsTilatelse(gjeldendeOppholdsstatus =
                    GjeldendeOppholdsstatus(
                oppholdstillatelsePaSammeVilkar =
                    OppholdstillatelsePaSammeVilkar(
                        periode = UdiPeriode(
                            parseDato(UdiDomenebegrep.FOM, rad),
                            parseValgfriDato(UdiDomenebegrep.TOM, rad)
                        ),
                        type = parseValgfriString(UdiDomenebegrep.TYPE, rad),
                        soknadIkkeAvgjort = parseValgfriBoolean(UdiDomenebegrep.SOKNAD_IKKE_AVGJORT.nøkkel, rad)
                    )
                )
            )
        }
    }
}

enum class UdiDomenebegrep(val nøkkel: String): Domenenøkkel {
    FOM("fom"),
    TOM("tom"),
    TYPE("Type"),
    SOKNAD_IKKE_AVGJORT("Søknad ikke avgjort")
    ;
    override fun nøkkel(): String = nøkkel
}