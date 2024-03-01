package no.nav.medlemskap.sykepenger.brukersporsmaalhandler

import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.domain.Konklusjon
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.RegelId
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.Svar
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.domene.Datagrunnlag
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.util.*

class OppholdstilatelseTester {



    @Test
    fun `Bruker bryter på kun regel 3 med mange brukerspørsmål, oppholdstilatelse ok skal føre til at regler om opphold ikke kjøres `(){
        val fileContent = Datagrunnlag::class.java.classLoader.getResource("IkkeNorskEOSBorgerUavklarRegel3MedBrukeSvarFalseIArbeidUtlandOgOppholdOgOppholdsTilatelse.json").readText(Charsets.UTF_8)
        val respons = TailService().handleKeyValueMessage(UUID.randomUUID().toString(),fileContent)
        val jsonRespons = JacksonParser().ToJson(respons.value)
        val konklusjon:Konklusjon = JacksonParser().toDomainObject(jsonRespons.get("konklusjon").get(0))
        println(jsonRespons.toPrettyString())
        Assertions.assertNotNull(konklusjon)
        Assertions.assertTrue(konklusjon.reglerKjørt.find { it.regelId==RegelId.SP6201 }!!.svar == Svar.JA,"Scenario skal  inneholde brukerspørsmål og resultattet skal være likt som før")
        Assertions.assertTrue(konklusjon.reglerKjørt.find { it.regelId==RegelId.SP6001 }!!.svar == Svar.JA,"hale skal utføres")
        Assertions.assertEquals(Svar.JA ,konklusjon.status)
        Assertions.assertEquals("SP6000" ,konklusjon.hvem)
    }

    @Test
    fun `BrukerSomBryterPaa19_3UtenBrukerspørsmål`(){
        val fileContent = Datagrunnlag::class.java.classLoader.getResource("BrukerMedBruddPåOppholdstilatelseReglerUtenBrukerSpørsmål.json").readText(Charsets.UTF_8)
        val respons = TailService().handleKeyValueMessage(UUID.randomUUID().toString(),fileContent)
        val jsonRespons = JacksonParser().ToJson(respons.value)
        val konklusjon:Konklusjon = JacksonParser().toDomainObject(jsonRespons.get("konklusjon").get(0))
        println(jsonRespons.toPrettyString())
        Assertions.assertNotNull(konklusjon)
        Assertions.assertNull(konklusjon.reglerKjørt.find { it.regelId==RegelId.SP6201 },"regel skal ikke bli utført uten brukerspørsmål")
        Assertions.assertEquals(Svar.UAVKLART ,konklusjon.status)
        Assertions.assertEquals("SP6000" ,konklusjon.hvem)
    }


}