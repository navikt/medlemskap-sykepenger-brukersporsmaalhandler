package no.nav.medlemskap.sykepenger.brukersporsmaalhandler

import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.Svar
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.domene.Datagrunnlag
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.util.*


class Feilsok {

    @Test
    fun feilsok(){
        val fileContent = Datagrunnlag::class.java.classLoader.getResource("feilsok.json").readText(Charsets.UTF_8)
        val respons = TailService().handleKeyValueMessage(UUID.randomUUID().toString(),fileContent)
        val jsonRespons = JacksonParser().ToJson(respons.value)
        //Assertions.assertEquals(Svar.JA.name ,jsonRespons.get("konklusjon").get(0).get("status").asText())
        //Assertions.assertEquals("SP6000" ,jsonRespons.get("konklusjon").get(0).get("hvem").asText())
        println(jsonRespons.toPrettyString())
    }
}