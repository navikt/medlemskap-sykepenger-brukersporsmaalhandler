package no.nav.medlemskap.sykepenger.brukersporsmaalhandler

import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.domene.*
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

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
}

