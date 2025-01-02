package no.nav.medlemskap.sykepenger.brukersporsmaalhandler

import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.domene.Brukerinput
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.domene.Datagrunnlag
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.domene.Kjøring
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.domene.oppgittArbeidUtenforNorgeLand
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.domene.oppgittArbeidUtenforNorgePeriode
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.domene.oppholdUtenforEØSOppgitt
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.domene.statsborgerskap
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class BrukerspørsmålLoggingTest {

    @Test
    fun IngenBrukerspørsmålOppgittSkalGiFalseResultat(){
        val fileContent = Datagrunnlag::class.java.classLoader.getResource("JaSvarUtenNyeBrukerSporsmaal.json").readText(Charsets.UTF_8)
        val jsonRespons = JacksonParser().ToJson(fileContent)
        val resultatGammelRegelMotor: Kjøring = JacksonParser().toDomainObject(jsonRespons)
        Assertions.assertFalse(resultatGammelRegelMotor.datagrunnlag.brukerinput.oppholdUtenforEØSOppgitt(),"Når opphold utenfor EØS ikke er oppgitt skal false returneres")
    }

    @Test
    fun IkkeNorskBorgerMedArbeidUtlandOppgitt_Stasborgerskap(){
        val fileContent = Datagrunnlag::class.java.classLoader.getResource("BrukerIkkeNorskMedArbeidUtlandOppgitt.json").readText(Charsets.UTF_8)
        val jsonRespons = JacksonParser().ToJson(fileContent)
        val resultatGammelRegelMotor: Kjøring = JacksonParser().toDomainObject(jsonRespons)

        Assertions.assertEquals("[DNK]",resultatGammelRegelMotor.datagrunnlag.statsborgerskap(),"statsborgerskap skal hentes ut")
    }
    @Test
    fun IkkeNorskBorgerMedArbeidUtlandOppgitt_ArbeidUtlandPeriode(){
        val fileContent = Datagrunnlag::class.java.classLoader.getResource("BrukerIkkeNorskMedArbeidUtlandOppgitt.json").readText(Charsets.UTF_8)
        val jsonRespons = JacksonParser().ToJson(fileContent)
        val resultatGammelRegelMotor: Kjøring = JacksonParser().toDomainObject(jsonRespons)

        Assertions.assertEquals("Periode(fom=2024-01-01, tom=2024-02-15)",resultatGammelRegelMotor.datagrunnlag.brukerinput.oppgittArbeidUtenforNorgePeriode(),"statsborgerskap skal hentes ut")
    }
    @Test
    fun IkkeNorskBorgerMedArbeidUtlandOppgitt_OppgittLandArbeidetI(){
        val fileContent = Datagrunnlag::class.java.classLoader.getResource("BrukerIkkeNorskMedArbeidUtlandOppgitt.json").readText(Charsets.UTF_8)
        val jsonRespons = JacksonParser().ToJson(fileContent)
        val resultatGammelRegelMotor: Kjøring = JacksonParser().toDomainObject(jsonRespons)

        Assertions.assertEquals("Danmark",resultatGammelRegelMotor.datagrunnlag.brukerinput.oppgittArbeidUtenforNorgeLand(),"oppgitt land skal hentes ut")
    }
    @Test
    fun brukerspørsmaalOppholdUtenforEOSMedFalseSvar(){
        val fileContent = Datagrunnlag::class.java.classLoader.getResource("UavklartKunRegel3MedBrukerSvarAlleSvarNeiNorskBorger.json").readText(Charsets.UTF_8)
        val jsonRespons = JacksonParser().ToJson(fileContent)
        val resultatGammelRegelMotor: Kjøring = JacksonParser().toDomainObject(jsonRespons)
        Assertions.assertFalse(resultatGammelRegelMotor.datagrunnlag.brukerinput.oppholdUtenforEØSOppgitt(),"Når opphold utenfor EØS ikke er oppgitt skal false returneres")
    }
    @Test
    fun brukerspørsmaalOppholdUtenforEOSMedTrueSvar(){
        val fileContent = Datagrunnlag::class.java.classLoader.getResource("IkkeNorskEOSBorgerUavklarRegel3MedBrukeSvarTrueArbeidUtlandOgOpphold.json").readText(Charsets.UTF_8)
        val jsonRespons = JacksonParser().ToJson(fileContent)
        val resultatGammelRegelMotor: Kjøring = JacksonParser().toDomainObject(jsonRespons)
        Assertions.assertTrue(resultatGammelRegelMotor.datagrunnlag.brukerinput.oppholdUtenforEØSOppgitt(),"Når opphold utenfor EØS er oppgitt og det svares JA  skal true returneres")
    }






}

