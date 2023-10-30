package no.nav.kafkaStreamsSample


import org.junit.jupiter.api.Test

class UavklartTestScenarioer {

    @Test
    fun brukerharoppgittTrueIArbeidUtland(){
        val fileContent = this::class.java.classLoader.getResource("arbeidutenfornorgesample.json").readText(Charsets.UTF_8)

        val v = domapping(fileContent)
        println(v)
    }
    @Test
    fun brukerharoppgittTrueIArbeidUtlandDerSaksbehandlerHarSjekketUtAlleUavklarteAvklaringer(){
        val fileContent = this::class.java.classLoader.getResource("arbeidutenfornorgesample.json").readText(Charsets.UTF_8)

        val v = domapping2(fileContent)
        println(v)
    }
}