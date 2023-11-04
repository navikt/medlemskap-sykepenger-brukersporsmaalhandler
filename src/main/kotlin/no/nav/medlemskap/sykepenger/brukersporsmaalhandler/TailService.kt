package no.nav.medlemskap.sykepenger.brukersporsmaalhandler

import com.fasterxml.jackson.databind.node.ObjectNode
import mu.KotlinLogging
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.domain.*
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.domene.Kjøring
import org.apache.kafka.streams.KeyValue
import java.time.LocalDate
import net.logstash.logback.argument.StructuredArguments.kv
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.*
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.regler.arbeiderItoLand
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.regler.oppholderSegIEØS

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

        println("Arbeider i to land : ${responsRegelMotorHale.arbeiderItoLand()?.name}")
        println("Oppholder seg i EØS : ${responsRegelMotorHale.oppholderSegIEØS()?.name}")
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
        avklaringsListe.addAll(responsRegelMotorHale.årsaker.filterNot { it.regelId == RegelId.SP6500 }.map { mapToAvklaring(it) })
        val avklaringerGammelKjøring = resultatGammelRegelMotor.resultat.årsaker.map{mapToAvklaringModel2(it) }

        //håndterer norske borgere
        if (responsRegelMotorHale.fakta.find { Faktum.NORSK_BORGER == it.faktum } !=null ){

            val kanAlleRegelBruddSjekkesUtNorskeBorgereRegelResultat = responsRegelMotorHale.finnRegelResultat(RegelId.SP6500)
            val faktum = kanAlleRegelBruddSjekkesUtNorskeBorgereRegelResultat!!.fakta.find { Faktum.IKKE_SJEKKET_UT ==it.faktum }
            if (faktum != null) {
                val rest = avklaringerGammelKjøring.filter { faktum.kilde.contains(it.regel_id) }
                avklaringsListe.addAll(rest)
            }
        }

        return avklaringsListe
    }

    private fun mapToAvklaringModel2(aarsak: no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.domene.Årsak):avklaring {
        return avklaring(aarsak.regelId,aarsak.avklaring,aarsak.svar.name,"UAVKLART",null,"SP6000", LocalDate.now())
    }

    fun mapToAvklaring(aarsak:Årsak):avklaring{
        return avklaring(aarsak.regelId.name,aarsak.avklaring,aarsak.svar.name,"UAVKLART",null,"SP6000", LocalDate.now())
    }


}