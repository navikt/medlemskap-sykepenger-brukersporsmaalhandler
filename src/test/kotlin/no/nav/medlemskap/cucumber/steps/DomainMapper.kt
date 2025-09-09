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


    fun mapPdlOppholdsTilatelse(datatable: DataTable):List<PdlOppholdsTilatelse> {
        val rows: List<Map<String, String>> = datatable.asMaps(
            String::class.java,
            String::class.java
        )
        val fom = rows.first()["PDL_FOM"]
        val tom = rows.first()["PDL_TOM"]
        val type = rows.first()["TYPE"]

        return listOf(PdlOppholdsTilatelse(type!!,LocalDate.parse(fom),LocalDate.parse(tom)))
    }
    fun mapUdiOppholdsTilatelse(datatable: DataTable):UdiOppholdsTilatelse {
        val rows: List<Map<String, String>> = datatable.asMaps(
            String::class.java,
            String::class.java
        )
        val fom = rows.first()["UDI_FOM"]
        val tom = rows.first()["UDI_TOM"]
        val type = rows.first()["TYPE"]
        val udiFrom = LocalDate.parse(fom)
        var udiTom : LocalDate? = null
        try{
             udiTom = LocalDate.parse(tom)
        }
        catch (e:Exception){
            //null er default verdi
        }
        return UdiOppholdsTilatelse(
            gjeldendeOppholdsstatus = GjeldendeOppholdsstatus(
                oppholdstillatelsePaSammeVilkar = OppholdstillatelsePaSammeVilkar
                    (
                    periode = UdiPeriode(udiFrom,udiTom),
                    type=type,
                    soknadIkkeAvgjort=null,

                )
            )
        )
    }
    fun mapPermanentUdiOppholdsTilatelse(datatable: DataTable):UdiOppholdsTilatelse {
        val rows: List<Map<String, String>> = datatable.asMaps(
            String::class.java,
            String::class.java
        )
        val fom = rows.first()["UDI_FOM"]
        val tom = rows.first()["UDI_TOM"]
        val type = rows.first()["TYPE"]
        val udiFrom = LocalDate.parse(fom)
        var udiTom : LocalDate? = null
        try{
            udiTom = LocalDate.parse(tom)
        }
        catch (e:Exception){
            //null er default verdi
        }
        return UdiOppholdsTilatelse(
            gjeldendeOppholdsstatus = GjeldendeOppholdsstatus(
                oppholdstillatelsePaSammeVilkar = OppholdstillatelsePaSammeVilkar
                    (
                    periode = UdiPeriode(udiFrom,udiTom)

                )
            )
        )
    }
    fun mapPdlOppholdsTilatelseMedFlereRader(datatable: DataTable):List<PdlOppholdsTilatelse> {
        val pdlOppholdsTilatelseList = mutableListOf<PdlOppholdsTilatelse>()
        val rows: List<Map<String, String>> = datatable.asMaps(
            String::class.java,
            String::class.java
        )
        rows.forEach {
            pdlOppholdsTilatelseList.add(PdlOppholdsTilatelse(it.get("TYPE")!!,LocalDate.parse(it.get("PDL_FOM")),LocalDate.parse(it.get("PDL_TOM"))))
        }

        return pdlOppholdsTilatelseList
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

    fun mapOppholdUtenforEos(datatable: DataTable, ferie: Boolean = false): OppholdUtenforEos? {
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
            val ferie = if (ferie) "Jeg var på ferie" else ""
            val land = rows.first()["LAND"]
            return OppholdUtenforEos(id =UUID.randomUUID().toString(),
                sporsmalstekst = "spørsmåltext ",
                svar= true,
                oppholdUtenforEOS = listOf(
                    Opphold(
                        id = "",
                        land=land!!,
                        grunn = ferie,
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
