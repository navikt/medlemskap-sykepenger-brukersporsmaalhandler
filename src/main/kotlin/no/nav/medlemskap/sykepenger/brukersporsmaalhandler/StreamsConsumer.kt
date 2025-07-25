package no.nav.medlemskap.sykepenger.brukersporsmaalhandler


import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.config.Configuration
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.config.Environment
import org.apache.kafka.clients.CommonClientConfigs
import org.apache.kafka.common.config.SslConfigs
import org.apache.kafka.common.serialization.Serdes
import org.apache.kafka.streams.KafkaStreams
import org.apache.kafka.streams.StreamsBuilder
import org.apache.kafka.streams.StreamsConfig
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.*

class StreamsConsumer(environment: Environment) {
    val log: Logger = LoggerFactory.getLogger(StreamsConsumer::class.java)
    fun stream(): KafkaStreams {
        val start = System.currentTimeMillis()
        log.info("Starter streaming oppsett")
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
        props[StreamsConfig.PROCESSING_GUARANTEE_CONFIG] = StreamsConfig.EXACTLY_ONCE

        val builder = StreamsBuilder()
        builder.stream<String, String>(Configuration.KafkaConfig().streamFrom)
            .map { key, value -> TailService().handleKeyValueMessage(key,value) }
            //loggging for metrikker osv....peek { key, value ->  }
            .to(Configuration.KafkaConfig().streamTo)
        val topology = builder.build()
        val kafkaStreams = KafkaStreams(topology, props)
        kafkaStreams.start()
        log.info("Streaming oppsett ferdig. Tidsbruk : ${System.currentTimeMillis()-start}ms ")
        return kafkaStreams

    }
}
