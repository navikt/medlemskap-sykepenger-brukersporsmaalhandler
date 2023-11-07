package no.nav.kafkaStreamsSample




import com.fasterxml.jackson.databind.node.ObjectNode
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.JacksonParser
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.config.Configuration
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.domain.*
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.Svar
import org.apache.kafka.clients.CommonClientConfigs
import org.apache.kafka.common.config.SslConfigs
import org.apache.kafka.common.serialization.Serdes
import org.apache.kafka.streams.KafkaStreams

import org.apache.kafka.streams.StreamsBuilder
import org.apache.kafka.streams.StreamsConfig
import java.time.LocalDate

import java.util.*


fun main(args: Array<String>) {
    val props = Properties()

    props[StreamsConfig.APPLICATION_ID_CONFIG] = "medlemskap-hale-Test"
    props[StreamsConfig.BOOTSTRAP_SERVERS_CONFIG] = "nav-dev-kafka-nav-dev.aivencloud.com:26484"

    props[StreamsConfig.CACHE_MAX_BYTES_BUFFERING_CONFIG] = 0
// Since the input topic uses Strings for both key and value, set the default Serdes to String.
// Since the input topic uses Strings for both key and value, set the default Serdes to String.
    props[StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG] = Serdes.String().javaClass.getName()
    props[StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG] = Serdes.String().javaClass.getName()
    props[SslConfigs.SSL_KEYSTORE_LOCATION_CONFIG] = Configuration.KafkaConfig().keystoreLocation
    props[SslConfigs.SSL_KEYSTORE_PASSWORD_CONFIG] = Configuration.KafkaConfig().keystorePassword
    props[CommonClientConfigs.SECURITY_PROTOCOL_CONFIG] = Configuration.KafkaConfig().securityProtocol
    props[CommonClientConfigs.CLIENT_ID_CONFIG] = Configuration.KafkaConfig().clientId
    props[SslConfigs.SSL_TRUSTSTORE_LOCATION_CONFIG] = Configuration.KafkaConfig().trustStorePath
    props[SslConfigs.SSL_TRUSTSTORE_PASSWORD_CONFIG] = Configuration.KafkaConfig().keystorePassword
    props[SslConfigs.SSL_KEYSTORE_TYPE_CONFIG] = Configuration.KafkaConfig().keystoreType
    val builder = StreamsBuilder()
        builder.stream<String,String>("medlemskap.medlemskap-uavklart")
        .peek { key, value -> println("Observed event: $value") }
        .mapValues { t-> domapping(t) }
        .to("medlemskap.medlemskap-avklart-test")
    val topology = builder.build()
    val kafkaStreams = KafkaStreams(topology,props)
    println(topology.describe())
    kafkaStreams.start()



}

fun domapping(t: String?) :String?{
    if (t != null) {
        try {
            val node = JacksonParser().ToJson(t)
            val new: ObjectNode =  node.deepCopy()
            val konklusjon = Konklusjon(
                dato = LocalDate.now(),
                status = Svar.UAVKLART,
                lovvalg = null,
                medlemskap = null,
                avklaringsListe = listOf(
                    avklaring(
                        regel_id = "SP6001",
                        avklaringstekst = "Har du utført arbeid utenfor norge",
                        svar = "JA",
                        status = "UAVKLART",
                        beskrivelse = null,
                        hvem = "SP6000",
                        tidspunkt = LocalDate.now()),
                    avklaring(
                        regel_id = "REGEL_1_3_1",
                        avklaringstekst = "Er hele perioden uten medlemskap innenfor 12-måneders perioden?",
                        svar = "NEI",
                        status = "UAVKLART",
                        beskrivelse = null,
                        hvem = "SP6000/Gammel regel motor",
                        tidspunkt = LocalDate.now()
                    )
                )
            )
            val konklusjoner:List<Konklusjon> = listOf(konklusjon)
            new.put("konklusjon",JacksonParser().ToJson(konklusjoner))
            return new.toPrettyString()
        }
        catch (e:Exception){
            println("ERROR:"+e.message)
            return t
        }

    }
    else{
        return t
    }
}
fun domapping2(t: String?) :String?{
    if (t != null) {
        try {
            val node = JacksonParser().ToJson(t)
            val new: ObjectNode =  node.deepCopy()
            val konklusjon = Konklusjon(
                dato = LocalDate.now().minusDays(10),
                status = Svar.UAVKLART,
                lovvalg = null,
                medlemskap = null,
                avklaringsListe = listOf(
                    avklaring(
                        regel_id = "SP6001",
                        avklaringstekst = "Har du utført arbeid utenfor norge",
                        svar = "JA",
                        status = "UAVKLART",
                        beskrivelse = null,
                        hvem = "SP6000",
                        tidspunkt = LocalDate.now()),
                    avklaring(
                        regel_id = "REGEL_1_3_1",
                        avklaringstekst = "Er hele perioden uten medlemskap innenfor 12-måneders perioden?",
                        svar = "NEI",
                        status = "UAVKLART",
                        beskrivelse = null,
                        hvem = "SP6000/Gammel regel motor",
                        tidspunkt = LocalDate.now()
                    )
                )
            )
            val konklusjon2 = Konklusjon(
                dato = LocalDate.now().minusDays(2),
                status = Svar.JA,
                lovvalg = Lovvalg("NOR"),
                medlemskap = Medlemskap("JA","§2-1"),
                dekningForSP = DekningsAltrnativer.JA,
                avklaringsListe = listOf(
                    avklaring(
                        regel_id = "SP6001",
                        avklaringstekst = "Har du utført arbeid utenfor norge",
                        svar = "JA",
                        status = "AVKLART",
                        beskrivelse = "har sjekket perioder og de er innenfor lovloge perioder.",
                        hvem = "Saksbehandler Lisa Tønne",
                        tidspunkt = LocalDate.now().minusDays(2)),
                    avklaring(
                        regel_id = "REGEL_1_3_1",
                        avklaringstekst = "Er hele perioden uten medlemskap innenfor 12-måneders perioden?",
                        svar = "NEI",
                        status = "AVKLART",
                        beskrivelse = "Søker kom tilbake til norge før tiden, og fyller derfor kravene til medlemskap § 2.1",
                        hvem = "Saksbehandler Lisa Tønne",
                        tidspunkt = LocalDate.now().minusDays(2)),
                    )
                )
            val konklusjoner:List<Konklusjon> = listOf(konklusjon,konklusjon2)
            new.put("konklusjon",JacksonParser().ToJson(konklusjoner))
            return new.toPrettyString()
        }
        catch (e:Exception){
            println("ERROR:"+e.message)
            return t
        }

    }
    else{
        return t
    }
}


