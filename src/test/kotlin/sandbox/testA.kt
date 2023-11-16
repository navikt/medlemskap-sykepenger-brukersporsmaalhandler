package sandbox

import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.JacksonParser
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.TailService
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.domene.Datagrunnlag
import org.junit.jupiter.api.Test
import java.util.*

class testA {
@Test
fun test(){
    val fileContent = Datagrunnlag::class.java.classLoader.getResource("arbeidutenfornorgesample.json").readText(Charsets.UTF_8)
   val respons = TailService().handleKeyValueMessage(UUID.randomUUID().toString(),fileContent)
    println(JacksonParser().ToJson(respons.value).toPrettyString())

}
}