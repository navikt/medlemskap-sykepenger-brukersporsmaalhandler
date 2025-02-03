package no.nav.medlemskap.sykepenger.brukersporsmaalhandler

import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.config.objectMapper
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.domain.Konklusjon
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.domain.finnRegelKjøring
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.Informasjon
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.RegelId
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.ReglerService
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.Svar
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.domene.Datagrunnlag
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.domene.Kjøring
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.util.*

class OppholdstilatelseTester {



    @Test
    fun `Bruker bryter på kun regel 3 med mange brukerspørsmål om oppholdstilatekse,skal føre til regelBrudd SP6212 `(){
        val fileContent = Datagrunnlag::class.java.classLoader.getResource("IkkeNorskEOSBorgerUavklarRegel3MedBrukeSvarFalseIArbeidUtlandOgOppholdOgOppholdsTilatelse.json").readText(Charsets.UTF_8)
        val respons = TailService().handleKeyValueMessage(UUID.randomUUID().toString(),fileContent)
        val jsonRespons = JacksonParser().ToJson(respons.value)
        val konklusjon:Konklusjon = JacksonParser().toDomainObject(jsonRespons.get("konklusjon").get(0))
        Assertions.assertNotNull(konklusjon)
        Assertions.assertTrue(konklusjon.finnRegelKjøring(RegelId.SP6201)!!.svar == Svar.NEI,"Scenario skal  inneholde brukerspørsmål og resultattet skal være likt som før")
        Assertions.assertTrue(konklusjon.finnRegelKjøring(RegelId.SP6001)!!.svar == Svar.JA,"hale skal utføres")
        Assertions.assertEquals(Svar.UAVKLART ,konklusjon.status)
        Assertions.assertEquals("SP6000" ,konklusjon.hvem)
        Assertions.assertNotNull(konklusjon.avklaringsListe.find { it.regel_id == "SP6212" })

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
        val fileContent = Datagrunnlag::class.java.classLoader.getResource("Regel19_3_1_Brudd_med_PDL_OppholdsDataOgIngenNyeBrukerSporsmaal.json").readText(Charsets.UTF_8)
        val resultatGammelRegelMotorJson = JacksonParser().ToJson(fileContent)
        val resultatGammelRegelMotor: Kjøring = JacksonParser().toDomainObject(resultatGammelRegelMotorJson)
        Assertions.assertNotNull(resultatGammelRegelMotor.datagrunnlag.pdlpersonhistorikk)
        Assertions.assertEquals(3,resultatGammelRegelMotor.datagrunnlag.pdlpersonhistorikk.oppholdstilatelser.size)
    }
    @Test
    fun `uthenting av oppholdstilatelser fra UDI `(){
        val fileContent = Datagrunnlag::class.java.classLoader.getResource("Regel19_3_1_Brudd_med_PDL_OppholdsDataOgIngenNyeBrukerSporsmaal.json").readText(Charsets.UTF_8)
        val resultatGammelRegelMotorJson = JacksonParser().ToJson(fileContent)
        val resultatGammelRegelMotor: Kjøring = JacksonParser().toDomainObject(resultatGammelRegelMotorJson)
        Assertions.assertNotNull(resultatGammelRegelMotor.datagrunnlag.oppholdstillatelse)
        Assertions.assertNotNull(resultatGammelRegelMotor.datagrunnlag.oppholdstillatelse!!.gjeldendeOppholdsstatus?.oppholdstillatelsePaSammeVilkar!!.periode)
    }
    @Test
    fun `uthenting av eksisterende regel resultat fra hale regel motor der Brukerpsørsmål ikke er likt med PDL innslag skal Reggel SP6229 svare NEI `(){
        val fileContent = Datagrunnlag::class.java.classLoader.getResource("BrukerBrudd_REGEL19_MedBrukerSvarIkkeLiktSomPDL.json").readText(Charsets.UTF_8)
        val resultatGammelRegelMotorJson = JacksonParser().ToJson(fileContent)
        val resultatGammelRegelMotor:Kjøring = JacksonParser().toDomainObject(resultatGammelRegelMotorJson)
        val responsRegelMotorHale = ReglerService.kjørRegler(resultatGammelRegelMotor)
        Assertions.assertEquals(Svar.NEI,responsRegelMotorHale.finnRegelResultat(RegelId.SP6229)!!.svar)
    }
    @Test
    fun `uthenting av eksisterende regel resultat fra hale regel motor der Brukerpsørsmål er likt med PDL innslag skal Reggel SP6229 svare NEI `(){
        val fileContent = Datagrunnlag::class.java.classLoader.getResource("BrukerBrudd_REGEL19_MedBrukerSvarLiktSomPDL.json").readText(Charsets.UTF_8)
        val resultatGammelRegelMotorJson = JacksonParser().ToJson(fileContent)
        val resultatGammelRegelMotor:Kjøring = JacksonParser().toDomainObject(resultatGammelRegelMotorJson)
        Assertions.assertNotNull(resultatGammelRegelMotor.datagrunnlag.pdlpersonhistorikk)
        val responsRegelMotorHale = ReglerService.kjørRegler(resultatGammelRegelMotor)
        val tredjelandsBorger:Boolean = responsRegelMotorHale.utledetInformasjon.filter { (it.informasjon == Informasjon.TREDJELANDSBORGER_MED_EOS_FAMILIE) || it.informasjon == Informasjon.TREDJELANDSBORGER }.isNotEmpty()
        Assertions.assertEquals(Svar.JA,responsRegelMotorHale.finnRegelResultat(RegelId.SP6229)!!.svar)
    }
    @Test
    fun `kjøring av regelmotor med gammelt resultat REGELBRUDD_C`(){
        val fileContent = Datagrunnlag::class.java.classLoader.getResource("REGEL_C.json").readText(Charsets.UTF_8)
        val respons = TailService().handleKeyValueMessage(UUID.randomUUID().toString(),fileContent)
        val jsonRespons = JacksonParser().ToJson(respons.value)
        val konklusjon:Konklusjon = JacksonParser().toDomainObject(jsonRespons.get("konklusjon").get(0))
        Assertions.assertEquals(Svar.UAVKLART,konklusjon.status)
        val expected = listOf<String>("SP6120","SP6312","REGEL_C")
        Assertions.assertTrue(konklusjon.avklaringsListe.map{it.regel_id}.containsAll(expected))
    }


}