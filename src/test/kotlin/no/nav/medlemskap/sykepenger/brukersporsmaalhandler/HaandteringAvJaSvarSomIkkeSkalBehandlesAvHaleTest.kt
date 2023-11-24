package no.nav.medlemskap.sykepenger.brukersporsmaalhandler

import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.Svar
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.domene.Datagrunnlag
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.util.*

class HaandteringAvJaSvarSomIkkeSkalBehandlesAvHaleTest {

    @Test
    fun `Ja svar skal ha identisk konkluson som resultat`(){
        val fileContent = Datagrunnlag::class.java.classLoader.getResource("JaSvarUtenNyeBrukerSporsmaal.json").readText(Charsets.UTF_8)
        val respons = TailService().handleKeyValueMessage(UUID.randomUUID().toString(),fileContent)
        val jsonRespons = JacksonParser().ToJson(respons.value)
        Assertions.assertEquals(Svar.JA.name ,jsonRespons.get("konklusjon").get(0).get("status").asText())
        Assertions.assertEquals("SP6000" ,jsonRespons.get("konklusjon").get(0).get("hvem").asText())
        println(jsonRespons.toPrettyString())
    }
    @Test
    fun `Ja svar skal ha identisk konkluson som resultat, gammel modell med årsaker til tross for JA`(){
        val fileContent = Datagrunnlag::class.java.classLoader.getResource("JaSvarMedAArsakerIResultat.json").readText(Charsets.UTF_8)
        val respons = TailService().handleKeyValueMessage(UUID.randomUUID().toString(),fileContent)
        val jsonRespons = JacksonParser().ToJson(respons.value)
        Assertions.assertEquals(Svar.JA.name ,jsonRespons.get("konklusjon").get(0).get("status").asText())
        Assertions.assertEquals("SP6000" ,jsonRespons.get("konklusjon").get(0).get("hvem").asText())
        println(jsonRespons.toPrettyString())
    }
}