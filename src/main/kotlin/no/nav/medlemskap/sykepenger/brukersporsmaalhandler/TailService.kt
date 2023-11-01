package no.nav.medlemskap.sykepenger.brukersporsmaalhandler

import com.fasterxml.jackson.databind.node.ObjectNode
import mu.KotlinLogging
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.domain.Konklusjon
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.domain.Status
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.domain.avklaring
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.ReglerService
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.Resultat
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.domene.Kjøring
import org.apache.kafka.streams.KeyValue
import java.time.LocalDate

class TailService() {
    private val logger = KotlinLogging.logger { }
    private val secureLogger = KotlinLogging.logger("tjenestekall")
    fun handleMessage(json: String?): String? {
        if (json != null) {
            try {
                val resultatGammelRegelMotorJson = JacksonParser().ToJson(json)
                val resultatGammelRegelMotor:Kjøring = JacksonParser().toDomainObject(resultatGammelRegelMotorJson)
                val responsRegelMotorHale = ReglerService.kjørRegler(resultatGammelRegelMotor)
                val konklusjon:Konklusjon = lagKonklusjon(resultatGammelRegelMotor,responsRegelMotorHale)
                val konklusjoner: List<Konklusjon> = listOf(konklusjon)
                val konklusjonerJson = JacksonParser().ToJson(konklusjoner)
                val haleRespons: ObjectNode = resultatGammelRegelMotorJson.deepCopy()
                val t:ObjectNode = haleRespons.set("konklusjon", konklusjonerJson)
                return haleRespons.toPrettyString()
            } catch (e: Exception) {
                println("ERROR:" + e.message)
                return json
            }

        } else {
            return json
        }
    }
    fun handleMessage2(key:String,json: String?): KeyValue<String,String> {
        if (json != null) {
            try {
                val resultatGammelRegelMotorJson = JacksonParser().ToJson(json)
                val resultatGammelRegelMotor:Kjøring = JacksonParser().toDomainObject(resultatGammelRegelMotorJson)
                val responsRegelMotorHale = ReglerService.kjørRegler(resultatGammelRegelMotor)
                val konklusjon:Konklusjon = lagKonklusjon(resultatGammelRegelMotor,responsRegelMotorHale)
                val konklusjoner: List<Konklusjon> = listOf(konklusjon)
                val konklusjonerJson = JacksonParser().ToJson(konklusjoner)
                val haleRespons: ObjectNode = resultatGammelRegelMotorJson.deepCopy()
                val t:ObjectNode = haleRespons.set("konklusjon", konklusjonerJson)
                return KeyValue(key,haleRespons.toPrettyString())
            } catch (e: Exception) {
                println("ERROR:" + e.message)
                return KeyValue(key,json)
            }

        } else {
            return KeyValue(key,json)
        }
    }

    private fun lagKonklusjon(resultatGammelRegelMotor: Kjøring, responsRegelMotorHale: Resultat): Konklusjon {
        val konklusjon = Konklusjon(
            dato = LocalDate.now(),
            status = Status.UAVKLART,
            lovvalg = null,
            medlemskap = null,
            dekning = null,
            avklaringsListe = listOf(
                avklaring(
                    regel_id = "SP6001",
                    avklaringstekst = "Har du utført arbeid utenfor norge",
                    svar = "JA",
                    status = "UAVKLART",
                    beskrivelse = null,
                    hvem = "SP6000",
                    tidspunkt = LocalDate.now()
                ),
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
        return konklusjon

    }
}