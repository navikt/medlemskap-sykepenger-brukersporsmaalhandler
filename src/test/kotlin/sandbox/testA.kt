package sandbox

import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.JacksonParser
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.TailService
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.domene.Brukerinput
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.domene.Datagrunnlag
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.domene.Oppholdstilatelse
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.domene.Periode
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.util.*

class testA {
@Test
fun test(){
    val fileContent = Datagrunnlag::class.java.classLoader.getResource("arbeidutenfornorgesample.json").readText(Charsets.UTF_8)
   val respons = TailService().handleKeyValueMessage(UUID.randomUUID().toString(),fileContent)
    //println(JacksonParser().ToJson(respons.value).toPrettyString())

    val permanent = Oppholdstilatelse(
        id=UUID.randomUUID().toString(),
        sporsmalstekst = "",
        svar = true,
        vedtaksdato = LocalDate.now(),
        vedtaksTypePermanent = true
    )
    val midlertidig = Oppholdstilatelse(
        id=UUID.randomUUID().toString(),
        sporsmalstekst = "",
        svar = true,
        vedtaksdato = LocalDate.now(),
        vedtaksTypePermanent = false,
        perioder = listOf(Periode(LocalDate.now().minusMonths(2).toString(),LocalDate.now().plusMonths(2).toString()))
    )

    println(JacksonParser().ToJson(midlertidig).toPrettyString())
}
}