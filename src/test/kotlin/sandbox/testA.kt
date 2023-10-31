package sandbox

import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.JacksonParser
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.domene.Datagrunnlag
import org.junit.jupiter.api.Test

class testA {
@Test
fun test(){
    val fileContent = Datagrunnlag::class.java.classLoader.getResource("arbeidutenfornorgesample.json").readText(Charsets.UTF_8)
   // val datagrunnlag: Datagrunnlag = JacksonParser().toDomainObject(fileContent)

}
}