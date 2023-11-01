package no.nav.medlemskap.sykepenger.brukersporsmaalhandler


import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.config.Configuration
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.config.Environment
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.config.KafkaConfig
import org.apache.kafka.clients.CommonClientConfigs
import org.apache.kafka.common.config.SslConfigs
import org.apache.kafka.common.serialization.Serdes
import org.apache.kafka.streams.KafkaStreams
import org.apache.kafka.streams.StreamsBuilder
import org.apache.kafka.streams.StreamsConfig
import java.util.*

class StreamsConsumer(environment: Environment) {

    fun stream(): KafkaStreams {
        val props = Properties()

        props[StreamsConfig.APPLICATION_ID_CONFIG] = Configuration.KafkaConfig().applicationID
        props[StreamsConfig.BOOTSTRAP_SERVERS_CONFIG] = Configuration.KafkaConfig().bootstrapServers

        props[StreamsConfig.CACHE_MAX_BYTES_BUFFERING_CONFIG] = 0
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
        builder.stream<String, String>(Configuration.KafkaConfig().streamFrom)
            .map { key, value -> TailService().handleMessage2(key,value) }
            .to(Configuration.KafkaConfig().streamTo)
        val topology = builder.build()
        val kafkaStreams = KafkaStreams(topology, props)
        kafkaStreams.start()
        return kafkaStreams

    }
}
