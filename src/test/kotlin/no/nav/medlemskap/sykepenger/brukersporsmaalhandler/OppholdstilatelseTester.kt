package no.nav.medlemskap.sykepenger.brukersporsmaalhandler

import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.domain.Konklusjon
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.domain.finnRegelKjøring
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
        Assertions.assertNotNull(konklusjon)
        Assertions.assertTrue(konklusjon.finnRegelKjøring(RegelId.SP6201)!!.svar == Svar.NEI,"Scenario skal  inneholde brukerspørsmål og resultattet skal være likt som før")
        Assertions.assertTrue(konklusjon.finnRegelKjøring(RegelId.SP6001)!!.svar == Svar.JA,"hale skal utføres")
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
        Assertions.assertNull(konklusjon.finnRegelKjøring(RegelId.SP6201),"regel skal ikke bli utført uten brukerspørsmål")
        Assertions.assertEquals(Svar.UAVKLART ,konklusjon.status)
        Assertions.assertEquals("SP6000" ,konklusjon.hvem)
    }

    @Test
    fun `3LandsBrukerSomBryterPaa19_3UtenMedBrukerspørsmål`(){
        val fileContent = Datagrunnlag::class.java.classLoader.getResource("BrukerBrudd_REGEL19_MedBrukerSvarOmOppholdsTilatelse.json").readText(Charsets.UTF_8)
        val respons = TailService().handleKeyValueMessage(UUID.randomUUID().toString(),fileContent)
        val jsonRespons = JacksonParser().ToJson(respons.value)
        val konklusjon:Konklusjon = JacksonParser().toDomainObject(jsonRespons.get("konklusjon").get(0))
        println(jsonRespons.toPrettyString())
        Assertions.assertNotNull(konklusjon)
        Assertions.assertEquals(Svar.JA ,konklusjon.finnRegelKjøring(RegelId.SP6001)!!.svar,"Regelmotor skal kjøres")
        Assertions.assertEquals(Svar.JA,konklusjon.finnRegelKjøring(RegelId.SP6201)!!.svar,"Det skal være regelbrudd for oppholdstilatelse")
        Assertions.assertEquals(Svar.JA,konklusjon.finnRegelKjøring(RegelId.SP6211)!!.svar,"Det finnes brukerspørsmål om oppholdstilatelse")
        Assertions.assertEquals(Svar.NEI,konklusjon.finnRegelKjøring(RegelId.SP6221)!!.svar,"Bruker skal ikke ha permanent oppgholsTilatelse")
        Assertions.assertEquals(Svar.UAVKLART ,konklusjon.status)
        Assertions.assertEquals("SP6000" ,konklusjon.hvem)
    }
    @Test
    fun `3LandsBruker SomBryterPaa19_3 med brukerspormaa som er minst 1 år tilbake og to mnd frem `(){
        val fileContent = Datagrunnlag::class.java.classLoader.getResource("BrukerBrudd_REGEL19_MedBrukerSvarOmOppholdsTilatelseSkalGiJA.json").readText(Charsets.UTF_8)
        val respons = TailService().handleKeyValueMessage(UUID.randomUUID().toString(),fileContent)
        val jsonRespons = JacksonParser().ToJson(respons.value)
        val konklusjon:Konklusjon = JacksonParser().toDomainObject(jsonRespons.get("konklusjon").get(0))
        println(jsonRespons.toPrettyString())
        Assertions.assertNotNull(konklusjon)
        Assertions.assertEquals(Svar.JA ,konklusjon.finnRegelKjøring(RegelId.SP6001)!!.svar,"Regelmotor skal kjøres")
        Assertions.assertEquals(Svar.JA,konklusjon.finnRegelKjøring(RegelId.SP6201)!!.svar,"Det skal være regelbrudd for oppholdstilatelse")
        Assertions.assertEquals(Svar.JA,konklusjon.finnRegelKjøring(RegelId.SP6211)!!.svar,"Det finnes brukerspørsmål om oppholdstilatelse")
        Assertions.assertEquals(Svar.NEI,konklusjon.finnRegelKjøring(RegelId.SP6221)!!.svar,"Bruker skal ikke ha permanent oppgholsTilatelse")
        Assertions.assertEquals(Svar.UAVKLART ,konklusjon.status)
        Assertions.assertEquals("SP6000" ,konklusjon.hvem)
    }


}