package no.nav.medlemskap.sykepenger.brukersporsmaalhandler.nais

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import mu.KotlinLogging
import org.apache.kafka.streams.KafkaStreams


private val logger = KotlinLogging.logger { }

fun Routing.naisRoutes(
    consumeJob: KafkaStreams
) {
    get("/isAlive") {
        val state = consumeJob.state()
        if (state == KafkaStreams.State.RUNNING || state == KafkaStreams.State.REBALANCING) {
            call.respondText("Alive!", ContentType.Text.Plain, HttpStatusCode.OK)
        } else {
            logger.error("Consumejob sin status er ${state.name} — returnerer 503 slik at poden restartes")
            call.respondText("Not alive!", ContentType.Text.Plain, HttpStatusCode.ServiceUnavailable)
        }
    }
    get("/isReady") {
        call.respondText("Ready!", ContentType.Text.Plain, HttpStatusCode.OK)
    }
    get("/metrics") {
        call.respondText(Metrics.registry.scrape(), ContentType.parse("text/plain; version=0.0.4; charset=utf-8"))
    }
}