package no.nav.medlemskap.cucumber.steps


import com.fasterxml.jackson.databind.JsonNode
import io.cucumber.datatable.DataTable

import io.cucumber.java.no.Gitt
import io.cucumber.java.no.Når
import io.cucumber.java.no.Og
import io.cucumber.java.no.Så
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.JacksonParser
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.TailService
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.Resultat
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.Svar
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.Ytelse
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.domene.*
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.regler.RegelFactory
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.regler.v1.ArbeidUtenforNorgeRegelFlyt
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.regler.v1.SkalHaleFlytUtføresRegel
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.regler.v1.oppholdsRegler.ReglerForOppholdUtenforEOS
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.regler.v1.oppholdstilatelse.ReglerForOppholdstilatelse
import org.junit.jupiter.api.Assertions
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*


class RegelSteps  {
    var brukerinput :Brukerinput? = null
    var resultat_gammel_kjoring :String? = null
    var utfortAarbeidUtenforNorge:UtfortAarbeidUtenforNorge? = null
    var oppholdUtenforEos:OppholdUtenforEos? = null
    var oppholdstilatelse:Oppholdstilatelse? = null
    var arbeidUtenforNorgeGammelModell = false
    var svar:String? = null
    var årsaker = mutableListOf<Årsak>()
    var regelkjoringResultat:Resultat? = null
    var inputPeriode:InputPeriode = InputPeriode(LocalDate.now(), LocalDate.now())
    var gammelkjøringResultat:Kjøring? = null

    //@Gitt("resultat av medlemskap-oppslag er {string}")
   @Gitt("gammelt resultat for gammel kjøring er {string}")
   fun stiTilGammelKjoringFil(filSti:String){

       val fileContent = Datagrunnlag::class.java.classLoader.getResource(filSti).readText(Charsets.UTF_8)
       this.gammelkjøringResultat = JacksonParser().toDomainObject(fileContent)
        println("lest gammel kjøring fra fil")
   }

    @Gitt("brukersvar om oppholdstitatelse")
    fun brukersporsmaalOppholdsTilatelse(datatable: DataTable){
        this.oppholdstilatelse = DomainMapper().mapOppholdsTilatelse(datatable)
        println("lest gammel kjøring fra fil")
    }

    @Gitt("arbeidUtenforNorgeGammelModell er {string}")
    fun function(arbeidutenfornorge:String){
        this.arbeidUtenforNorgeGammelModell = arbeidutenfornorge.toBoolean()
    }


    @Gitt("OppholdUtenforEosMedFlereInnslag")
    fun OppholdUtenforEos(){
            this.oppholdUtenforEos = no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.domene.OppholdUtenforEos(
                id = UUID.randomUUID().toString(),
                sporsmalstekst = "",
                svar=true,
                oppholdUtenforEOS = listOf(
                    Opphold(
                        id = UUID.randomUUID().toString(),
                        land = "Tyrkia",
                        grunn = "ferie",
                        perioder = listOf(
                            Periode(
                                LocalDate.now().minusMonths(11).toString(),
                                LocalDate.now().minusMonths(10).toString())),
                        ),
                    Opphold(
                        id = UUID.randomUUID().toString(),
                        land = "Tyrkia",
                        grunn = "ferie",
                        perioder = listOf(
                            Periode(
                                LocalDate.now().minusMonths(7).toString(),
                                LocalDate.now().minusMonths(6).toString())),
                    )

            ))
    }
    @Gitt("OppholdUtenforEos")
    fun OppholdUtenforEos(datatable: DataTable){
        this.oppholdUtenforEos = DomainMapper().mapOppholdUtenforEos(datatable)
        //brukerinput = Brukerinput(false, oppholdUtenforEos = this.oppholdUtenforEos)
    }
    @Og("utfoertArbeidUtenforNorge")
    fun arbeidUtenforNorgeNyModell(datatable: DataTable){
        this.utfortAarbeidUtenforNorge = DomainMapper().mapArbeidUtlandNyModell(datatable)
    }

    @Gitt("årsaker i gammel kjøring")
    fun årsaker(datatable:DataTable){
        this.årsaker.addAll(DomainMapper().mapÅrsaker(datatable))
    }
    @Når("medlemskap beregnes med følgende parametre")
    fun fun1(datatable:DataTable){

        val rows: List<Map<String, String>> = datatable.asMaps(
            String::class.java,
            String::class.java
        )
        val v = rows.first()["Har hatt arbeid utenfor Norge"]

        if ("Ja" == v){
            svar= "NOT_OK"
        }
        else
            svar =  "OK"
    }
    @Så("skal svar være {string}")
    fun skal_svar_være( statsborgerskapskategori: String){
        Assertions.assertEquals(statsborgerskapskategori, svar)
    }
    @Når("gammelt resultat for gammelregelkjøring er {string}")
    fun gammeltResultatForGammelregelkjoring(resultat_gammel_kjoring:String){
        this.resultat_gammel_kjoring = resultat_gammel_kjoring
        //println("Resultat gammel kjøring : $resultat_gammel_kjoring")
    }

    @Når("oppholdUtenforEØSRegler kjøres")
    fun oppholdUtenforEØSReglerKjøres(){
        regelkjoringResultat = ReglerForOppholdUtenforEOS.fraDatagrunnlag(hentDatagrunnlag()).kjørHovedflyt()
        print("oppholdUtenforEØSRegler kjøres")
    }

    @Når("oppholdstilatelseRegler kjøres")
    fun oppholdtilatelseReglerKjøres(){
        regelkjoringResultat = ReglerForOppholdstilatelse.fraDatagrunnlag(hentDatagrunnlag(),this.gammelkjøringResultat!!.resultat).kjørHovedflyt()
        print("oppholdstilatelseRegler kjøres")
    }

    @Når("arbeidutenforNorgeFlyt kjøres")
    fun arbeidutenforNorgeFlytkjøres(){
        regelkjoringResultat = ArbeidUtenforNorgeRegelFlyt.fraDatagrunnlag(hentDatagrunnlag()).kjørHovedflyt()
        print("test")
    }

    @Når("regel {string} kjøres")
    fun NårRegelKjøres(regelId: String){

    val regelFactory = RegelFactory(hentDatagrunnlag(),this.årsaker)
    val regel = regelFactory.create(regelId)
        regelkjoringResultat = regel.utfør()
    }


    private fun hentDatagrunnlag(): Datagrunnlag {
    return Datagrunnlag(
        ytelse = Ytelse.SYKEPENGER,
        periode = inputPeriode,
        fnr = "12345678901",
        brukerinput = Brukerinput(
            arbeidUtenforNorge = this.arbeidUtenforNorgeGammelModell,
            oppholdUtenforEos = this.oppholdUtenforEos,
            utfortAarbeidUtenforNorge = this.utfortAarbeidUtenforNorge,
            oppholdUtenforNorge = null,
            oppholdstilatelse = this.oppholdstilatelse
        )
    )
    }


    @Og("årsaker er {string}")
    fun arsakerEr(årsaker:String){
        årsaker.split(",").forEach { this.årsaker.add(Årsak(it,"", Svar.JA)) }
        val kjøring = Kjøring(
            tidspunkt = LocalDateTime.now(),
            kanal = "",
            datagrunnlag = Datagrunnlag(
                ytelse = Ytelse.SYKEPENGER,
                periode = InputPeriode(
                    fom=LocalDate.now(),
                    tom = LocalDate.now()),
                fnr = "",
                brukerinput= Brukerinput(
                    arbeidUtenforNorge = false,
                    oppholdstilatelse = null,
                    utfortAarbeidUtenforNorge = null,
                    oppholdUtenforNorge = null)
            ),
            resultat = GammelkjøringResultat(
                regelId = "",
                svar= Svar.valueOf(resultat_gammel_kjoring!!),
                begrunnelse = "",
                avklaring = "",
                delresultat = emptyList(),
                årsaker = this.årsaker
            )

        )
        regelkjoringResultat = SkalHaleFlytUtføresRegel.fraDatagrunnlag(kjøring).utfør()
    }
    @Så("skal resultat av regel være  være {string}")
    fun skalResultatAvRegelVære( forventetSvar: String?){

        Assertions.assertEquals(forventetSvar,regelkjoringResultat?.svar?.name)
    }
    @Og("årsak etter regelkjøring er {string}")
    fun årsak_etter_regelkjøring_er(forventetÅrsakRegelID :String){
        if ("NULL".equals(forventetÅrsakRegelID.uppercase()))
            {
            Assertions.assertTrue(regelkjoringResultat!!.årsaker.isEmpty())
            }
        else{
            Assertions.assertEquals(forventetÅrsakRegelID,
                regelkjoringResultat?.årsaker?.first()?.regelId?.name ?: "")
        }
    }
    @Og("begrunnelse på årsak er {string}")
    fun begrunnelse_paa_aarsak_er(begrunnelse :String){
        if ("NULL".equals(begrunnelse.uppercase()))
        {
            Assertions.assertTrue(regelkjoringResultat!!.årsaker.isEmpty())
        }
        else{
            Assertions.assertEquals(begrunnelse,
                regelkjoringResultat?.årsaker?.first()?.begrunnelse?: "")
        }
    }
}

fun String.toBoolean(): Boolean {
    when (this.uppercase()) {
        "TRUE" -> return true
        "FALSE" -> return false
    }
    return false
}