package no.nav.medlemskap.sykepenger.brukersporsmaalhandler

import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.domain.Konklusjon
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.domain.finnRegelKjøring
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.RegelId
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.Svar
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.domene.Datagrunnlag
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.domene.Kjøring
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
        Assertions.assertEquals(Svar.UAVKLART ,konklusjon.status)
        Assertions.assertEquals("SP6000" ,konklusjon.hvem)
    }
    @Test
    fun `3LandsBruker SomBryterPaa19_3 med brukerspormaa som er minst 1 år tilbake og to mnd frem_menManglerInnslagIDPL`(){
        val fileContent = Datagrunnlag::class.java.classLoader.getResource("BrukerBrudd_REGEL19_MedBrukerSvarOmOppholdsTilatelseSkalGiJA.json").readText(Charsets.UTF_8)
        val respons = TailService().handleKeyValueMessage(UUID.randomUUID().toString(),fileContent)
        val jsonRespons = JacksonParser().ToJson(respons.value)
        val konklusjon:Konklusjon = JacksonParser().toDomainObject(jsonRespons.get("konklusjon").get(0))
        Assertions.assertNotNull(konklusjon)
        Assertions.assertEquals(Svar.JA ,konklusjon.finnRegelKjøring(RegelId.SP6001)!!.svar,"Regelmotor skal kjøres")
        Assertions.assertEquals(Svar.JA,konklusjon.finnRegelKjøring(RegelId.SP6201)!!.svar,"Det skal være regelbrudd for oppholdstilatelse")
        Assertions.assertEquals(Svar.JA,konklusjon.finnRegelKjøring(RegelId.SP6211)!!.svar,"Det finnes brukerspørsmål om oppholdstilatelse")
        Assertions.assertEquals(Svar.NEI,konklusjon.finnRegelKjøring(RegelId.SP6225)!!.svar,"PDL data og udi data skal ikke kunne slås sammen til en sammengengende periode")
        Assertions.assertEquals(Svar.UAVKLART ,konklusjon.status)
        Assertions.assertEquals("SP6000" ,konklusjon.hvem)
    }
    @Test
    fun `3LandsBruker SomBryterPaa19_3_1 med brukerspormaa men ingen om oppholdstilatelse skal gi UAVKLART `(){
        val fileContent = Datagrunnlag::class.java.classLoader.getResource("BrukerBrudd_REGEL19_3_1_MedBrukerSvarMenIkkeOmOppholdsTilatelseSkalGiUAVKLART.json").readText(Charsets.UTF_8)
        val respons = TailService().handleKeyValueMessage(UUID.randomUUID().toString(),fileContent)
        val jsonRespons = JacksonParser().ToJson(respons.value)
        val konklusjon:Konklusjon = JacksonParser().toDomainObject(jsonRespons.get("konklusjon").get(0))
        Assertions.assertNotNull(konklusjon)
        Assertions.assertEquals(Svar.JA ,konklusjon.finnRegelKjøring(RegelId.SP6001)!!.svar,"Regelmotor skal kjøres")
        Assertions.assertEquals(Svar.JA,konklusjon.finnRegelKjøring(RegelId.SP6201)!!.svar,"Det skal være regelbrudd for oppholdstilatelse")
        Assertions.assertEquals(Svar.NEI,konklusjon.finnRegelKjøring(RegelId.SP6211)!!.svar,"Det skal ikke finnes brukerspørsmål om oppholdstilatelse")

        Assertions.assertEquals(Svar.UAVKLART ,konklusjon.status)
        Assertions.assertEquals("SP6000" ,konklusjon.hvem)
    }
    @Test
    fun `uthenting av oppholdstilatelser fra PDL `(){
        val fileContent = Datagrunnlag::class.java.classLoader.getResource("Regel19_3_1_Brudd_med_PDL_OppholdsDataOgBrukerSporsmaal.json").readText(Charsets.UTF_8)
        val resultatGammelRegelMotorJson = JacksonParser().ToJson(fileContent)
        val resultatGammelRegelMotor: Kjøring = JacksonParser().toDomainObject(resultatGammelRegelMotorJson)
        Assertions.assertNotNull(resultatGammelRegelMotor.datagrunnlag.pdlpersonhistorikk)
        Assertions.assertEquals(3,resultatGammelRegelMotor.datagrunnlag.pdlpersonhistorikk.oppholdstilatelser.size)
    }
    @Test
    fun `uthenting av oppholdstilatelser fra UDI `(){
        val fileContent = Datagrunnlag::class.java.classLoader.getResource("Regel19_3_1_Brudd_med_PDL_OppholdsDataOgBrukerSporsmaal.json").readText(Charsets.UTF_8)
        val resultatGammelRegelMotorJson = JacksonParser().ToJson(fileContent)
        val resultatGammelRegelMotor: Kjøring = JacksonParser().toDomainObject(resultatGammelRegelMotorJson)
        Assertions.assertNotNull(resultatGammelRegelMotor.datagrunnlag.oppholdstillatelse)
        Assertions.assertNotNull(resultatGammelRegelMotor.datagrunnlag.oppholdstillatelse!!.gjeldendeOppholdsstatus?.oppholdstillatelsePaSammeVilkar!!.periode)
    }


}