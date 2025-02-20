package no.nav.medlemskap.sykepenger.brukersporsmaalhandler

import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.domene.ArbeidUtenforNorge
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.domene.Brukerinput
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.domene.Datagrunnlag
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.domene.Kjøring
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.domene.Opphold
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.domene.OppholdUtenforEos
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.domene.OppholdUtenforNorge
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.domene.Periode
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.domene.UtfortAarbeidUtenforNorge
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.domene.oppgittArbeidUtenforNorgeLand
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.domene.oppgittArbeidUtenforNorgePeriode
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.domene.oppgittOppholdUtenforEØSGrunn
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.domene.oppgittOppholdUtenforEØSLand
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.domene.oppgittOppholdUtenforEØSPeriode
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.domene.oppgittOppholdUtenforNorgeGrunn
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.domene.oppgittOppholdUtenforNorgeLand
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.domene.oppgittOppholdUtenforNorgePeriode
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.domene.oppholdUtenforEØSOppgitt
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.domene.statsborgerskap
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.util.UUID

class BrukerspørsmålLoggingTest {

    @Test
    fun IngenBrukerspørsmålOppgittSkalGiFalseResultat(){
        val fileContent = Datagrunnlag::class.java.classLoader.getResource("JaSvarUtenNyeBrukerSporsmaal.json").readText(Charsets.UTF_8)
        val jsonRespons = JacksonParser().ToJson(fileContent)
        val resultatGammelRegelMotor: Kjøring = JacksonParser().toDomainObject(jsonRespons)
        Assertions.assertFalse(resultatGammelRegelMotor.datagrunnlag.brukerinput.oppholdUtenforEØSOppgitt(),"Når opphold utenfor EØS ikke er oppgitt skal false returneres")
    }

    @Test
    fun IngenBrukerspørsmålOppgittSkalIkkeFeileVedLogging(){
        val fileContent = Datagrunnlag::class.java.classLoader.getResource("JaSvarUtenNyeBrukerSporsmaal.json").readText(Charsets.UTF_8)
        val jsonRespons = JacksonParser().ToJson(fileContent)
        val resultatGammelRegelMotor: Kjøring = JacksonParser().toDomainObject(jsonRespons)
        Assertions.assertEquals("[NOR]",resultatGammelRegelMotor.datagrunnlag.statsborgerskap())
        Assertions.assertEquals("IKKE_OPPGITT",resultatGammelRegelMotor.datagrunnlag.brukerinput.oppgittArbeidUtenforNorgeLand())
        Assertions.assertEquals("IKKE_OPPGITT",resultatGammelRegelMotor.datagrunnlag.brukerinput.oppgittArbeidUtenforNorgePeriode())
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


    @Test
    fun brukerspørsmaalOppholdOppgittLoggingTest(){

       val brukerinput = Brukerinput(
            arbeidUtenforNorge = false,
            utfortAarbeidUtenforNorge = UtfortAarbeidUtenforNorge(
                id = UUID.randomUUID().toString(),
                sporsmalstekst = "",
                svar = true,
                arbeidUtenforNorge = listOf(ArbeidUtenforNorge(
                    id = UUID.randomUUID().toString(),
                    arbeidsgiver = "Svenska Volo",
                    land = "Sverige",
                    perioder = listOf(Periode(LocalDate.of(2020, 1, 1).toString(), LocalDate.of(2020, 12, 31).toString())),
                ))
            ),
            oppholdUtenforEos = OppholdUtenforEos(
                    id = UUID.randomUUID().toString(),
                    sporsmalstekst = "",
                    svar = true,
                    oppholdUtenforEOS = listOf(
                        Opphold(
                            id = UUID.randomUUID().toString(),
                            land = "USA",
                            grunn = "Ferie",
                            perioder = listOf(
                                Periode(
                                    LocalDate.of(2020, 1, 1).toString(), LocalDate.of(2020, 12, 31).toString()
                                )
                            )
                        )
                    )
                ),
            oppholdUtenforNorge= OppholdUtenforNorge(
                id = UUID.randomUUID().toString(),
                sporsmalstekst = "",
                svar = true,
                oppholdUtenforNorge = listOf(
                    Opphold(
                        id = UUID.randomUUID().toString(),
                        land = "Sverige",
                        grunn = "Studier",
                        perioder = listOf(
                            Periode(
                                LocalDate.of(2024, 1, 1).toString(), LocalDate.of(2024, 1, 2).toString()
                            )
                        )
                    )
                )
            )
        )

        Assertions.assertEquals("Sverige",brukerinput.oppgittOppholdUtenforNorgeLand(),"Feil i tolkning av land")
        Assertions.assertEquals("Periode(fom=2024-01-01, tom=2024-01-02)",brukerinput.oppgittOppholdUtenforNorgePeriode(),"Feil i tolkning av periode")
        Assertions.assertEquals("Studier",brukerinput.oppgittOppholdUtenforNorgeGrunn(),"Feil i tolkning av periode")

        Assertions.assertEquals("USA",brukerinput.oppgittOppholdUtenforEØSLand(),"Feil i tolkning av land")
        Assertions.assertEquals("Periode(fom=2020-01-01, tom=2020-12-31)",brukerinput.oppgittOppholdUtenforEØSPeriode(),"Feil i tolkning av periode")
        Assertions.assertEquals("Ferie",brukerinput.oppgittOppholdUtenforEØSGrunn(),"Feil i tolkning av periode")
    }

    @Test
    fun brukerspørsmaalOppholdOppgittMenMedNeiSvarLoggingTest(){

        val brukerinput = Brukerinput(
            arbeidUtenforNorge = false,
            utfortAarbeidUtenforNorge = UtfortAarbeidUtenforNorge(
                id = UUID.randomUUID().toString(),
                sporsmalstekst = "",
                svar = false,
                arbeidUtenforNorge = listOf()
            ),
            oppholdUtenforEos = OppholdUtenforEos(
                id = UUID.randomUUID().toString(),
                sporsmalstekst = "",
                svar = false,
                oppholdUtenforEOS = listOf()
            ),
            oppholdUtenforNorge= OppholdUtenforNorge(
                id = UUID.randomUUID().toString(),
                sporsmalstekst = "",
                svar = false,
                oppholdUtenforNorge = listOf()
            )
        )

        Assertions.assertEquals("IKKE_OPPGITT",brukerinput.oppgittOppholdUtenforNorgeLand(),"Feil i tolkning av land")
        Assertions.assertEquals("IKKE_OPPGITT",brukerinput.oppgittOppholdUtenforNorgePeriode(),"Feil i tolkning av periode")
        Assertions.assertEquals("IKKE_OPPGITT",brukerinput.oppgittOppholdUtenforNorgeGrunn(),"Feil i tolkning av periode")

        Assertions.assertEquals("IKKE_OPPGITT",brukerinput.oppgittOppholdUtenforEØSLand(),"Feil i tolkning av land")
        Assertions.assertEquals("IKKE_OPPGITT",brukerinput.oppgittOppholdUtenforEØSPeriode(),"Feil i tolkning av periode")
        Assertions.assertEquals("IKKE_OPPGITT",brukerinput.oppgittOppholdUtenforEØSGrunn(),"Feil i tolkning av periode")
    }

    @Test
    fun brukerspørsmaalOppholdIkkeOppgittSvarLoggingTest(){

        val brukerinput = Brukerinput(
            arbeidUtenforNorge = false,
            utfortAarbeidUtenforNorge = null,
            oppholdUtenforEos = null,
            oppholdUtenforNorge= null
        )

        Assertions.assertEquals("IKKE_OPPGITT",brukerinput.oppgittOppholdUtenforNorgeLand(),"Feil i tolkning av land")
        Assertions.assertEquals("IKKE_OPPGITT",brukerinput.oppgittOppholdUtenforNorgePeriode(),"Feil i tolkning av periode")
        Assertions.assertEquals("IKKE_OPPGITT",brukerinput.oppgittOppholdUtenforNorgeGrunn(),"Feil i tolkning av periode")

        Assertions.assertEquals("IKKE_OPPGITT",brukerinput.oppgittOppholdUtenforEØSLand(),"Feil i tolkning av land")
        Assertions.assertEquals("IKKE_OPPGITT",brukerinput.oppgittOppholdUtenforEØSPeriode(),"Feil i tolkning av periode")
        Assertions.assertEquals("IKKE_OPPGITT",brukerinput.oppgittOppholdUtenforEØSGrunn(),"Feil i tolkning av periode")
    }

}

