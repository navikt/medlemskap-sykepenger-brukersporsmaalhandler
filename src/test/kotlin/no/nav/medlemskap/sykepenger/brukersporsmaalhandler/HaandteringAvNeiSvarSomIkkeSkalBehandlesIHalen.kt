package no.nav.medlemskap.sykepenger.brukersporsmaalhandler

import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.domain.Konklusjon
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.Informasjon
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.Svar
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.domene.Datagrunnlag
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.util.*

class HaandteringAvNeiSvarSomIkkeSkalBehandlesIHalen {
    @Test
    fun `NEi svar skal ha identisk konkluson som resultat`(){
        val fileContent = Datagrunnlag::class.java.classLoader.getResource("NeiSvarUtenNyeBrukerSporsmaal.json").readText(Charsets.UTF_8)
        val respons = TailService().handleKeyValueMessage(UUID.randomUUID().toString(),fileContent)
        val jsonRespons = JacksonParser().ToJson(respons.value)
        val konklusjon:Konklusjon = JacksonParser().toDomainObject(jsonRespons.get("konklusjon").get(0))
        val tredjelandsBorger:Boolean = konklusjon.utledetInformasjoner.filter { (it.informasjon == Informasjon.TREDJELANDSBORGER_MED_EOS_FAMILIE) || it.informasjon == Informasjon.TREDJELANDSBORGER }.isNotEmpty()
        Assertions.assertNotNull(konklusjon)
        Assertions.assertEquals(Svar.NEI ,konklusjon.status)
        Assertions.assertEquals("SP6000" ,konklusjon.hvem)
        println(jsonRespons.toPrettyString())
    }
}