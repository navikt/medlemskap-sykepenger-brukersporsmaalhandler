package no.nav.medlemskap.cucumber.mapping.brukersvar

import io.cucumber.datatable.DataTable
import no.nav.medlemskap.cucumber.Domenenøkkel
import no.nav.medlemskap.cucumber.steps.BasisDomeneParser.Companion.mapDataTable
import no.nav.medlemskap.cucumber.steps.BasisDomeneParser.Companion.parseBoolean
import no.nav.medlemskap.cucumber.steps.BasisDomeneParser.Companion.parseDato
import no.nav.medlemskap.cucumber.steps.BasisDomeneParser.Companion.parseString
import no.nav.medlemskap.cucumber.steps.BasisDomeneParser.Companion.parseValgfriDato
import no.nav.medlemskap.cucumber.steps.BasisDomeneParser.Companion.parseValgfriString
import no.nav.medlemskap.cucumber.steps.RadMapper
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.domene.*

class BrukersvarDomeneSpraakParser {
    fun mapBrukersvar(dataTable: DataTable): Brukerinput {
        return mapDataTable(dataTable, BrukersvarMapper())[0]
    }

    fun mapOppholdUtenforEosBrukersvar(dataTable: DataTable): OppholdUtenforEos {
        return mapDataTable(dataTable, OppholdUtenforEosBrukersvarMapper())[0]
    }

    fun mapOppholdstillatelseBrukersvar(dataTable: DataTable): Oppholdstilatelse {
        return mapDataTable(dataTable, OppholdstillatelseBrukersvarMapper())[0]
    }

    class BrukersvarMapper : RadMapper<Brukerinput> {
        override fun mapRad(rad: Map<String, String>): Brukerinput {
            return Brukerinput(
                arbeidUtenforNorge = parseBoolean(BrukersvarDomenebegrep.ARBEID_UTENFOR_NORGE, rad),
                oppholdstilatelse = parseOppholdstillatelseBrukersvar(rad),
                utfortAarbeidUtenforNorge = parseUtfortAarbeidUtenforNorge(rad),
                oppholdUtenforEos = parseOppholdUtenforEos(rad),
                oppholdUtenforNorge = parseOppholdUtenforNorge(rad)
            )
        }
    }

    class OppholdUtenforEosBrukersvarMapper: RadMapper<OppholdUtenforEos> {
        override fun mapRad(rad: Map<String, String>): OppholdUtenforEos {
            return parseOppholdUtenforEos(rad)
        }
    }

    class OppholdstillatelseBrukersvarMapper : RadMapper<Oppholdstilatelse> {
        override fun mapRad(rad: Map<String, String>): Oppholdstilatelse {
            return parseOppholdstillatelseBrukersvar(rad)
        }
    }
}

fun parseUtfortAarbeidUtenforNorge(rad: Map<String, String>): UtfortAarbeidUtenforNorge {
    return UtfortAarbeidUtenforNorge(
        id = "",
        sporsmalstekst = "",
        svar = parseBoolean(BrukersvarDomenebegrep.SVAR, rad),
        arbeidUtenforNorge = parseArbeidUtenforNorge(rad)
    )
}

fun parseArbeidUtenforNorge(rad: Map<String, String>): List<ArbeidUtenforNorge> {
    return listOf(
        ArbeidUtenforNorge(
            id = "",
            arbeidsgiver = "",
            land = parseString(BrukersvarDomenebegrep.LAND, rad),
            perioder = parsePerioder(rad)
        )
    )
}

fun parseOppholdstillatelseBrukersvar(rad: Map<String, String>): Oppholdstilatelse {
    return Oppholdstilatelse(
        id = "",
        sporsmalstekst = "",
        svar = parseBoolean(BrukersvarDomenebegrep.SVAR, rad),
        vedtaksdato = parseDato(BrukersvarDomenebegrep.VEDTAKS_DATO, rad),
        vedtaksTypePermanent = parseBoolean(BrukersvarDomenebegrep.VEDTAKSTYPE_PERMANENT, rad),
        perioder = parsePerioder(rad)
    )
}

fun parseOppholdUtenforEos(rad: Map<String, String>): OppholdUtenforEos {
    return OppholdUtenforEos(
        id = "",
        sporsmalstekst = "",
        svar = parseBoolean(BrukersvarDomenebegrep.SVAR, rad),
        oppholdUtenforEOS = parseOpphold(rad)
    )
}

fun parseOppholdUtenforNorge(rad: Map<String, String>): OppholdUtenforNorge {
    return OppholdUtenforNorge(
        id = "",
        sporsmalstekst = "",
        svar = parseBoolean(BrukersvarDomenebegrep.SVAR, rad),
        oppholdUtenforNorge = parseOpphold(rad)
    )
}

fun parseOpphold(rad: Map<String, String>): List<Opphold> {
    return listOf(
        Opphold(
            id = "",
            land = parseString(BrukersvarDomenebegrep.LAND, rad),
            grunn = parseValgfriString(BrukersvarDomenebegrep.GRUNN, rad) ?: "",
            perioder = parsePerioder(rad)
        )
    )
}

fun parsePerioder(rad: Map<String, String>): List<Periode> {
    return listOf(
        Periode(
            fom = parseValgfriDato(BrukersvarDomenebegrep.FOM, rad).toString(),
            tom = parseValgfriDato(BrukersvarDomenebegrep.TOM, rad).toString()
        )
    )
}

enum class BrukersvarDomenebegrep(val nøkkel: String) : Domenenøkkel {
    FOM("fom"),
    TOM("tom"),
    ARBEID_UTENFOR_NORGE("arbeid utenfor Norge"),
    SVAR("Svar"),
    VEDTAKS_DATO("Vedtaksdato"),
    VEDTAKSTYPE_PERMANENT("Vedtakstype permanent"),
    GRUNN("Grunn"),
    LAND("Land")
    ;

    override fun nøkkel(): String = nøkkel
}