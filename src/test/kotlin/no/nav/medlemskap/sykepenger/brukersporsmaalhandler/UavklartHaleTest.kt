package no.nav.medlemskap.sykepenger.brukersporsmaalhandler

import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.domain.Konklusjon
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.RegelId
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.Svar
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.domene.Datagrunnlag
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.util.*

class UavklartHaleTest {
    @Test
    fun `Uavklart med Kun en regel (regel 3) uten brukerspørsmål skal behandles i halen og få uavklart`(){
        val fileContent = Datagrunnlag::class.java.classLoader.getResource("UavklartKunRegel3UtenBrukerSvarNorskBorger.json").readText(Charsets.UTF_8)
        val respons = TailService().handleKeyValueMessage(UUID.randomUUID().toString(),fileContent)
        val jsonRespons = JacksonParser().ToJson(respons.value)
        val konklusjon:Konklusjon = JacksonParser().toDomainObject(jsonRespons.get("konklusjon").get(0))
        Assertions.assertNotNull(konklusjon)
        Assertions.assertEquals(Svar.NEI,konklusjon.reglerKjørt.find { it.regelId == RegelId.ARBEID_UTLAND_FLYT }?.delresultat!!.find { it.regelId == RegelId.SP6110 }!!.svar)
        Assertions.assertEquals(Svar.UAVKLART ,konklusjon.status)
        Assertions.assertEquals("SP6000" ,konklusjon.hvem)
    }
    @Test
    fun `Uavklart med Kun en regel (regel 3) med  brukerspørsmål Alle Nei, skal behandles i halen og få JA`(){
        val fileContent = Datagrunnlag::class.java.classLoader.getResource("UavklartKunRegel3MedBrukerSvarAlleSvarNeiNorskBorger.json").readText(Charsets.UTF_8)
        val respons = TailService().handleKeyValueMessage(UUID.randomUUID().toString(),fileContent)
        val jsonRespons = JacksonParser().ToJson(respons.value)
        val konklusjon:Konklusjon = JacksonParser().toDomainObject(jsonRespons.get("konklusjon").get(0))
        println(jsonRespons.toPrettyString())
        Assertions.assertNotNull(konklusjon)
        Assertions.assertEquals(Svar.JA ,konklusjon.status)
        Assertions.assertEquals("SP6000" ,konklusjon.hvem)
    }
}