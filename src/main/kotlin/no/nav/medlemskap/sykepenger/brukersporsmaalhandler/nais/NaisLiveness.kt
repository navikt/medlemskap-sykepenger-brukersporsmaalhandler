package no.nav.medlemskap.sykepenger.brukersporsmaalhandler.nais

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.prometheus.client.exporter.common.TextFormat
import mu.KotlinLogging
import org.apache.kafka.streams.KafkaStreams


private val logger = KotlinLogging.logger { }
private val secureLogger = KotlinLogging.logger("tjenestekall")

fun Routing.naisRoutes(
    consumeJob: KafkaStreams
) {
    get("/isAlive") {
        if (consumeJob.state().isRunning) {
            call.respondText("Alive!", ContentType.Text.Plain, HttpStatusCode.OK)
        } else {
            logger.warn("Consumejob sin status er  ${consumeJob.state().name} ")
            call.respondText("Alive!", ContentType.Text.Plain, HttpStatusCode.OK)
        }
    }
    get("/isReady") {
        call.respondText("Ready!", ContentType.Text.Plain, HttpStatusCode.OK)
    }
    get("/metrics") {
        call.respondTextWriter(ContentType.parse(TextFormat.CONTENT_TYPE_004)) {
            writeMetrics004(this, Metrics.registry)
        }
    }

}