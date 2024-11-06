package no.nav.medlemskap.sykepenger.brukersporsmaalhandler

import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.domene.Datagrunnlag
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.domene.Kjøring
import org.junit.jupiter.api.Test
import java.util.*

class HåndteringAvUavklartSomSkalBehandlesAvHaleTest {
    @Test
    fun `Uaklart med Regel 3 OG regel 9 aarsaker skal beahandles i halen`(){
        val fileContent = Datagrunnlag::class.java.classLoader.getResource("UavklartRegel3og9.json").readText(Charsets.UTF_8)
        val respons = TailService().handleKeyValueMessage(UUID.randomUUID().toString(),fileContent)
        val jsonRespons = JacksonParser().ToJson(respons.value)
        println(jsonRespons.toPrettyString())

    }
    @Test
    fun `Uaklart med Regel 3 skal beahandles i halen`(){
        val fileContent = Datagrunnlag::class.java.classLoader.getResource("UavklartRegel3.json").readText(Charsets.UTF_8)
        val respons = TailService().handleKeyValueMessage(UUID.randomUUID().toString(),fileContent)
        val jsonRespons = JacksonParser().ToJson(respons.value)
        println(jsonRespons.toPrettyString())

    }
    @Test
    fun `Uaklart med MEDL brudd  skal beahandles i halen, men bare delvis`(){
        val fileContent = Datagrunnlag::class.java.classLoader.getResource("BrukerBruddRegel1_4UtenBrukerSvar.json").readText(Charsets.UTF_8)
        val respons = TailService().handleKeyValueMessage(UUID.randomUUID().toString(),fileContent)
        val jsonRespons = JacksonParser().ToJson(respons.value)
        println(jsonRespons.toPrettyString())

    }

    @Test
    fun parseTest(){
        val fileContent = Datagrunnlag::class.java.classLoader.getResource("BrukerBruddRegel1_4UtenBrukerSvar.json").readText(Charsets.UTF_8)

        val resultatGammelRegelMotor: Kjøring = JacksonParser().toDomainObject(fileContent)
        println()
    }
}