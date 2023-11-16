package no.nav.medlemskap.sykepenger.brukersporsmaalhandler

import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.config.Configuration
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.config.Environment
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.nais.createHttpServer
import org.slf4j.Logger
import org.slf4j.LoggerFactory


fun main() {
    Application().start()
}

class Application(private val env: Environment = System.getenv(),
                  private val streamConsumer:StreamsConsumer = StreamsConsumer(env)
) {
    companion object {
        val log: Logger = LoggerFactory.getLogger(Application::class.java)
    }

    fun start() {
        log.info("Start application")

        @OptIn(DelicateCoroutinesApi::class)
        runBlocking  {
        val consumeJob = launch {
            val stream = streamConsumer.stream()
            createHttpServer(stream).start(wait = true)
        }

        }



    }
}