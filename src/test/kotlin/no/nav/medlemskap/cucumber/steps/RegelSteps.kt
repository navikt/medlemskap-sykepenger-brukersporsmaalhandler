package no.nav.medlemskap.cucumber.steps


import io.cucumber.datatable.DataTable

import io.cucumber.java.no.Gitt
import io.cucumber.java.no.Når
import io.cucumber.java.no.Og
import io.cucumber.java.no.Så
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.Resultat
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.Svar
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.Ytelse
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.domene.*
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.regler.RegelFactory
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.regler.v1.SkalHaleFlytUtføresRegel
import org.junit.jupiter.api.Assertions
import java.time.LocalDate
import java.time.LocalDateTime


class RegelSteps  {
    var brukerinput :Brukerinput? = null
    var resultat_gammel_kjoring :String? = null
    var svar:String? = null
    var årsaker = mutableListOf<Årsak>()
    var regelkjoringResultat:Resultat? = null
    var inputPeriode:InputPeriode = InputPeriode(LocalDate.now(), LocalDate.now())
   @Gitt("arbeidUtenforNorgeGammelModell er {string}")
   fun function(arbeidutenfornorge:String){
       brukerinput = Brukerinput(arbeidutenfornorge.toBoolean())
   }
    @Gitt("følgende innslag i brukerinput")
    fun function(datatable:DataTable){
        brukerinput = Brukerinput(false)
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
    fun demo3( statsborgerskapskategori: String){
        Assertions.assertEquals(statsborgerskapskategori, svar)
    }
    @Når("gammelt resultat for gammelregelkjøring er {string}")
    fun gammeltResultatForGammelregelkjoring(resultat_gammel_kjoring:String){
        this.resultat_gammel_kjoring = resultat_gammel_kjoring
        //println("Resultat gammel kjøring : $resultat_gammel_kjoring")
    }

    @Når("regel {string} kjøres")
    fun NårRegelKjøres(regelId: String){

    val regelFactory = RegelFactory(hentDatagrunnlag())
    val regel = regelFactory.create(regelId)
        regelkjoringResultat = regel.utfør()
    }

    private fun hentDatagrunnlag(): Datagrunnlag {
    return Datagrunnlag(
        ytelse = Ytelse.SYKEPENGER,
        periode = inputPeriode,
        fnr = "12345678901",
        brukerinput = brukerinput!!
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
}

fun String.toBoolean(): Boolean {
    when (this.uppercase()) {
        "TRUE" -> return true
        "FALSE" -> return false
    }
    return false
}