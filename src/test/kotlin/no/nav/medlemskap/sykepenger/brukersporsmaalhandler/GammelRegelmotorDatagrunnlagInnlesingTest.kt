package no.nav.medlemskap.sykepenger.brukersporsmaalhandler

import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.domain.DekningsAltrnativer
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.domain.Konklusjon
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.domain.erTredjelandsborgerMedEOSFamilie
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.Informasjon
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.Svar
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.UtledetInformasjon
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.domene.*
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.domene.extensionfunctions.siste_permisjonPermitteringPeriode
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.domene.extensionfunctions.siste_permisjonPermitteringProsent
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.domene.extensionfunctions.siste_permisjonPermitteringType
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.time.LocalDate

class GammelRegelmotorDatagrunnlagInnlesingTest {

    @Test
    fun returnererSisteMaritimeForholdTilLogging() {
        val fileContent =
            Datagrunnlag::class.java.classLoader.getResource("REGEL7_1.json")
                .readText(Charsets.UTF_8)
        val jsonRespons = JacksonParser().ToJson(fileContent)
        val resultatGammelRegelMotor: Kjøring = JacksonParser().toDomainObject(jsonRespons)
        Assertions.assertTrue(
            resultatGammelRegelMotor.datagrunnlag.arbeidsforhold.isNotEmpty(),
            "Datagrunnlag skal inneholde arbeidsforhold"
        )
        Assertions.assertEquals("INNENRIKS",resultatGammelRegelMotor.datagrunnlag.sisteMaritimeArbeidsavtaleFartsomraade())
        Assertions.assertEquals("NOR",resultatGammelRegelMotor.datagrunnlag.sisteMaritimeArbeidsavtaleSkipsregister())
        Assertions.assertEquals("ANNET",resultatGammelRegelMotor.datagrunnlag.sisteMaritimeArbeidsavtaleSkipstype())
        Assertions.assertEquals("fom: 2024-11-01 , tom: null",resultatGammelRegelMotor.datagrunnlag.sisteMaritimeArbeidsavtalePeriode())
    }

    @Test
    fun dersomBrukerEr3LandsBorgerMedEOSFamilieSkalFunksjonReturnereTrue() {
        val konklusjon = Konklusjon(
            hvem = "TEST6000",
            dato = LocalDate.now(),
            status = Svar.JA,
            lovvalg = null,
            medlemskap = null,
            dekningForSP = DekningsAltrnativer.JA,
            utledetInformasjoner = listOf(UtledetInformasjon(
                informasjon = Informasjon.TREDJELANDSBORGER_MED_EOS_FAMILIE,
                kilde = emptyList()
            ))
        )
        Assertions.assertTrue(konklusjon.erTredjelandsborgerMedEOSFamilie())
    }
    @Test
    fun dersomBrukerIkkeEr3LandsBorgerMedEOSFamilieSkalFunksjonReturnereFalse() {
        val konklusjon = Konklusjon(
            hvem = "TEST6000",
            dato = LocalDate.now(),
            status = Svar.JA,
            lovvalg = null,
            medlemskap = null,
            dekningForSP = DekningsAltrnativer.JA,
            utledetInformasjoner = listOf(UtledetInformasjon(
                informasjon = Informasjon.TREDJELANDSBORGER,
                kilde = emptyList()
            ))
        )
        Assertions.assertFalse (konklusjon.erTredjelandsborgerMedEOSFamilie())
    }

    @Test
    fun haandtererNullVerdierIMaritimLogging(){
        val fileContent =
            Datagrunnlag::class.java.classLoader.getResource("REGEL_C.json")
                .readText(Charsets.UTF_8)
        val jsonRespons = JacksonParser().ToJson(fileContent)
        val resultatGammelRegelMotor: Kjøring = JacksonParser().toDomainObject(jsonRespons)
        Assertions.assertTrue(
            resultatGammelRegelMotor.datagrunnlag.arbeidsforhold.isNotEmpty(),
            "Datagrunnlag skal inneholde arbeidsforhold"
        )
        Assertions.assertEquals("IKKE_OPPGITT",resultatGammelRegelMotor.datagrunnlag.sisteMaritimeArbeidsavtaleFartsomraade())
        Assertions.assertEquals("IKKE_OPPGITT",resultatGammelRegelMotor.datagrunnlag.sisteMaritimeArbeidsavtaleSkipsregister())
        Assertions.assertEquals("IKKE_OPPGITT",resultatGammelRegelMotor.datagrunnlag.sisteMaritimeArbeidsavtaleSkipstype())
        Assertions.assertEquals("IKKE_OPPGITT",resultatGammelRegelMotor.datagrunnlag.sisteMaritimeArbeidsavtalePeriode())
    }

    @Test
    fun innlesingAvPermisjonsPermitteringTest() {
        val fileContent =
            Datagrunnlag::class.java.classLoader.getResource("REGEL_33.json")
                .readText(Charsets.UTF_8)
        val jsonRespons = JacksonParser().ToJson(fileContent)
        val resultatGammelRegelMotor: Kjøring = JacksonParser().toDomainObject(jsonRespons)
        Assertions.assertTrue(
            resultatGammelRegelMotor.datagrunnlag.arbeidsforhold.first().permisjonPermittering!!.isNotEmpty(),
            "Datagrunnlag skal inneholde PermisjonsPermittering"
        )

    }
    @Test
    fun uthentingavLoggDataForPermisjonsPermitering() {
        val fileContent =
            Datagrunnlag::class.java.classLoader.getResource("REGEL_33.json")
                .readText(Charsets.UTF_8)
        val jsonRespons = JacksonParser().ToJson(fileContent)
        val resultatGammelRegelMotor: Kjøring = JacksonParser().toDomainObject(jsonRespons)
        Assertions.assertTrue(
            resultatGammelRegelMotor.datagrunnlag.arbeidsforhold.first().permisjonPermittering!!.isNotEmpty(),
            "Datagrunnlag skal inneholde PermisjonsPermittering"
        )
        val kontrollPeriode = InputPeriode(resultatGammelRegelMotor.datagrunnlag.periode.fom.minusYears(1),resultatGammelRegelMotor.datagrunnlag.periode.tom)
        Assertions.assertEquals("2024-12-17 - null",resultatGammelRegelMotor.datagrunnlag.siste_permisjonPermitteringPeriode(kontrollPeriode),"perioden passer ikke")
        Assertions.assertEquals("PERMISJON_MED_FORELDREPENGER",resultatGammelRegelMotor.datagrunnlag.siste_permisjonPermitteringType(kontrollPeriode),"type passer ikke")
        Assertions.assertEquals(100.0,resultatGammelRegelMotor.datagrunnlag.siste_permisjonPermitteringProsent(kontrollPeriode),"prosent passer ikke")

    }
}



