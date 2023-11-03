package no.nav.medlemskap.sykepenger.brukersporsmaalhandler

import com.fasterxml.jackson.databind.node.ObjectNode
import mu.KotlinLogging
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.domain.*
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.ReglerService
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.Resultat
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.Svar
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.domene.Kjøring
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.Årsak
import org.apache.kafka.streams.KeyValue
import java.time.LocalDate
import net.logstash.logback.argument.StructuredArguments.kv

class TailService() {
    private val logger = KotlinLogging.logger { }
    private val secureLogger = KotlinLogging.logger("tjenestekall")

    fun handleKeyValueMessage(key:String, json: String?): KeyValue<String,String> {
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
                logger.error("teknisk feil i regelkjøring: ${e.message}",
                    kv("soknadID",key),
                    kv("stacktrace",e.stackTrace)
                )
                return KeyValue(key,json)
            }

        } else {
            return KeyValue(key,json)
        }
    }

    private fun lagKonklusjon(resultatGammelRegelMotor: Kjøring, responsRegelMotorHale: Resultat): Konklusjon {

        if (responsRegelMotorHale.svar == Svar.JA){
            return Konklusjon(
                dato = LocalDate.now(),
                status = Svar.JA,
                lovvalg = null,
                dekning = Dekning("FULL"),
                medlemskap = Medlemskap("JA","§2-1"),
                avklaringsListe = emptyList(),
                reglerKjørt = responsRegelMotorHale.delresultat,
                fakta = responsRegelMotorHale.fakta
            )
        }
        else if (responsRegelMotorHale.svar == Svar.NEI){
            return Konklusjon(
                dato = LocalDate.now(),
                status = Svar.NEI,
                lovvalg = null,
                dekning =null,
                medlemskap = Medlemskap("NEI",""),
                reglerKjørt = responsRegelMotorHale.delresultat,
                avklaringsListe = emptyList(),
                fakta = responsRegelMotorHale.fakta
            )
        }
        else{

            return Konklusjon(
                dato = LocalDate.now(),
                status = Svar.UAVKLART,
                lovvalg = null,
                dekning =null,
                medlemskap = null,
                reglerKjørt = responsRegelMotorHale.delresultat,
                avklaringsListe = finnAvklaringsPunkter(resultatGammelRegelMotor,responsRegelMotorHale),
                fakta = responsRegelMotorHale.fakta
            )
        }
    }

    private fun finnAvklaringsPunkter(resultatGammelRegelMotor: Kjøring, responsRegelMotorHale: Resultat): List<avklaring> {
    val avklaringsListe:MutableList<avklaring> = mutableListOf()
        avklaringsListe.addAll(responsRegelMotorHale.årsaker.map { mapToAvklaring(it) })
        avklaringsListe.addAll(resultatGammelRegelMotor.resultat.årsaker.map { mapToAvklaringModel2(it) })
        return avklaringsListe
    }

    private fun mapToAvklaringModel2(aarsak: no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.domene.Årsak):avklaring {
        return avklaring(aarsak.regelId,aarsak.avklaring,aarsak.svar.name,"UAVKLART",null,"SP6000", LocalDate.now())
    }

    fun mapToAvklaring(aarsak:Årsak):avklaring{
        return avklaring(aarsak.regelId.name,aarsak.avklaring,aarsak.svar.name,"UAVKLART",null,"SP6000", LocalDate.now())
    }


}