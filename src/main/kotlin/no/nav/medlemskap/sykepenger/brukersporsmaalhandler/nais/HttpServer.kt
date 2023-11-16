package no.nav.medlemskap.sykepenger.brukersporsmaalhandler.nais


import io.ktor.serialization.jackson.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*

import io.ktor.server.plugins.callid.*
import io.ktor.server.plugins.callloging.*
import io.ktor.server.plugins.contentnegotiation.*

import io.ktor.server.routing.*
import io.ktor.http.*
import io.ktor.server.metrics.micrometer.*


import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.micrometer.prometheus.PrometheusMeterRegistry
import io.prometheus.client.exporter.common.TextFormat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.config.*

import org.apache.kafka.streams.KafkaStreams


import java.io.Writer
import java.util.*

fun createHttpServer(consumeJob: KafkaStreams) = embeddedServer(Netty, applicationEngineEnvironment {

    connector { port = 8080 }
    module {

        install(MicrometerMetrics) {
            registry = Metrics.registry
        }
        install(ContentNegotiation) {
            register(ContentType.Application.Json, JacksonConverter(objectMapper))
        }

        Thread.currentThread().setUncaughtExceptionHandler { _, e -> log.error("Uhåndtert feil", e) }
        environment.monitor.subscribe(ApplicationStopping) {
            log.warn("Application shutting down. Cleaning up resources")
            val closed = consumeJob.close()
            log.warn("kafka consumation closed : $closed")
        }

        routing {
            naisRoutes(consumeJob)
        }
    }
})

suspend fun writeMetrics004(writer: Writer, registry: PrometheusMeterRegistry) {
    withContext(Dispatchers.IO) {
        kotlin.runCatching {
            TextFormat.write004(writer, registry.prometheusRegistry.metricFamilySamples())
        }
    }
}

