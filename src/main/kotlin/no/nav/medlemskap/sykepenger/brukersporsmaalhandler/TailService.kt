package no.nav.medlemskap.sykepenger.brukersporsmaalhandler

import com.fasterxml.jackson.databind.node.ObjectNode
import mu.KotlinLogging
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.domain.*
import org.apache.kafka.streams.KeyValue
import java.time.LocalDate
import net.logstash.logback.argument.StructuredArguments.kv
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.*
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.domene.*
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.domene.extensionfunctions.siste_permisjonPermitteringPeriode
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.domene.extensionfunctions.siste_permisjonPermitteringProsent
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.domene.extensionfunctions.siste_permisjonPermitteringType
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.Årsak

class TailService() {
    private val logger = KotlinLogging.logger { }
    private val secureLogger = KotlinLogging.logger("tjenestekall")

    fun handleKeyValueMessage(key:String, json: String?): KeyValue<String,String> {
        logger.info("Mottatt Kafka melding for callId: $key")

        if (json != null) {
            try {
                val resultatGammelRegelMotorJson = JacksonParser().ToJson(json)
                val resultatGammelRegelMotor:Kjøring = JacksonParser().toDomainObject(resultatGammelRegelMotorJson)
                val kontrollPeriode = InputPeriode(resultatGammelRegelMotor.datagrunnlag.periode.fom.minusYears(1),resultatGammelRegelMotor.datagrunnlag.periode.fom)
                val responsRegelMotorHale = ReglerService.kjørRegler(resultatGammelRegelMotor)
                val konklusjon:Konklusjon = lagKonklusjon(resultatGammelRegelMotor,responsRegelMotorHale)
                val tredjelandsBorger:Boolean = konklusjon.erTredjelandsborger()
                val harHaleProssessertresultatFraGammelRegelmotor:Boolean = responsRegelMotorHale.harHaleProssesertResultatFraGammelRegelmotor()
                val konklusjoner: List<Konklusjon> = listOf(konklusjon)
                val konklusjonerJson = JacksonParser().ToJson(konklusjoner)
                val haleRespons: ObjectNode = resultatGammelRegelMotorJson.deepCopy()
                val t:ObjectNode = haleRespons.set("konklusjon", konklusjonerJson)
                var pdl_samsvar = "NA"
                val SP6229Svar = responsRegelMotorHale.finnRegelResultat(RegelId.SP6229)
                if (SP6229Svar!=null){
                    pdl_samsvar = SP6229Svar.svar.name
                }

                if (resultatGammelRegelMotor.resultat.svar.name != konklusjon.status.name){
                    logger.info("Behandlet ferdig med differanse",
                        kv("callId", key),
                        kv("konklusjon", konklusjon.status.name),
                        kv("kanal", resultatGammelRegelMotor.kanal),
                        kv("utfortAarbeidUtenforNorge", resultatGammelRegelMotor.datagrunnlag.brukerinput.utfortAarbeidUtenforNorgeOpppgitt())
                    )

                    secureLogger.info("post prosessering ferdig. Differanse i svar!",
                        kv("gammeltsvar",resultatGammelRegelMotor.resultat.svar.name),
                        kv("gammelt_aarsaker",resultatGammelRegelMotor.resultat.årsaker.map { it.regelId }.toString()),
                        kv("konklusjon",konklusjon.status.name),
                        kv("avklaringer",konklusjon.avklaringsListe.map { it.regel_id }.toString()),
                        kv("response",haleRespons.toPrettyString()),
                        kv("callId",key),
                        kv("erTredjelandsborger",tredjelandsBorger),
                        kv("harSP6000ProssesertGammeltResultat",harHaleProssessertresultatFraGammelRegelmotor),
                        kv("fnr",resultatGammelRegelMotor.datagrunnlag.fnr),

                        kv("oppholdUtenforEØS",resultatGammelRegelMotor.datagrunnlag.brukerinput.oppholdUtenforEØSOppgitt()),
                        kv("oppholdUtenforEØS_land",resultatGammelRegelMotor.datagrunnlag.brukerinput.oppgittOppholdUtenforEØSLand()),
                        kv("oppholdUtenforEØS_periode",resultatGammelRegelMotor.datagrunnlag.brukerinput.oppgittOppholdUtenforEØSPeriode()),
                        kv("oppholdUtenforEØS_grunn",resultatGammelRegelMotor.datagrunnlag.brukerinput.oppgittOppholdUtenforEØSGrunn()),

                        kv("oppholdUtenforNorge",resultatGammelRegelMotor.datagrunnlag.brukerinput.oppholdUtenforNorgeOpppgitt()),
                        kv("oppholdUtenforNorge_land",resultatGammelRegelMotor.datagrunnlag.brukerinput.oppgittOppholdUtenforNorgeLand()),
                        kv("oppholdUtenforNorge_peridoe",resultatGammelRegelMotor.datagrunnlag.brukerinput.oppgittOppholdUtenforNorgePeriode()),
                        kv("oppholdUtenforNorge_grunn",resultatGammelRegelMotor.datagrunnlag.brukerinput.oppgittOppholdUtenforNorgeGrunn()),

                        kv("utfortAarbeidUtenforNorge",resultatGammelRegelMotor.datagrunnlag.brukerinput.utfortAarbeidUtenforNorgeOpppgitt()),
                        kv("utfortAarbeidUtenforNorge_land",resultatGammelRegelMotor.datagrunnlag.brukerinput.oppgittArbeidUtenforNorgeLand()),
                        kv("utfortAarbeidUtenforNorge_periode",resultatGammelRegelMotor.datagrunnlag.brukerinput.oppgittArbeidUtenforNorgePeriode()),

                        kv("statsborgerskap",resultatGammelRegelMotor.datagrunnlag.statsborgerskap()),

                        kv("mar-gyldighetsperiode",resultatGammelRegelMotor.datagrunnlag.sisteMaritimeArbeidsavtalePeriode()),
                        kv("mar-skipstype",resultatGammelRegelMotor.datagrunnlag.sisteMaritimeArbeidsavtaleSkipstype()),
                        kv("mar-fartsomraade",resultatGammelRegelMotor.datagrunnlag.sisteMaritimeArbeidsavtaleFartsomraade()),
                        kv("mar-skipsregister",resultatGammelRegelMotor.datagrunnlag.sisteMaritimeArbeidsavtaleSkipsregister()),

                        kv("permisjon-periode",resultatGammelRegelMotor.datagrunnlag.siste_permisjonPermitteringPeriode(kontrollPeriode)),
                        kv("permisjon-type",resultatGammelRegelMotor.datagrunnlag.siste_permisjonPermitteringType(kontrollPeriode)),
                        kv("permisjon-prosent",resultatGammelRegelMotor.datagrunnlag.siste_permisjonPermitteringProsent(kontrollPeriode)),

                        kv("oppholdstillatelse",resultatGammelRegelMotor.datagrunnlag.brukerinput.oppholdstillatelseOppgitt()),
                        kv("oppholdstillatelse_periode",resultatGammelRegelMotor.datagrunnlag.brukerinput.oppholdstillatelsePeriode()),
                        kv("udi_oppholdstillatelse_periode",resultatGammelRegelMotor.datagrunnlag.udiOppholdstillatelsePeriode()),
                        kv("udi_oppholdstillatelse_type",resultatGammelRegelMotor.datagrunnlag.udiOppholdstillatelseType()),

                        kv("erTredjelandsborgerMedEØSFamilie",konklusjon.erTredjelandsborgerMedEOSFamilie()),

                        kv("nye_sporsmaal",resultatGammelRegelMotor.datagrunnlag.brukerinput.utfortAarbeidUtenforNorge!=null),
                        kv("antall_dager_sykemelding",resultatGammelRegelMotor.datagrunnlag.periode.antallDager()),
                        kv("PDL_SAMSVAR",pdl_samsvar),
                        kv("aarsaker",resultatGammelRegelMotor.resultat.årsaker.map { it.regelId }.toString()),
                        kv("analyse","NEI"))

                }
                else
                {
                    logger.info("Behandlet ferdig med lik svar",
                        kv("callId", key),
                        kv("konklusjon", konklusjon.status.name),
                        kv("kanal", resultatGammelRegelMotor.kanal),
                        kv("utfortAarbeidUtenforNorge", resultatGammelRegelMotor.datagrunnlag.brukerinput.utfortAarbeidUtenforNorgeOpppgitt())
                    )

                    secureLogger.info("post prosessering ferdig",
                        kv("gammeltsvar",resultatGammelRegelMotor.resultat.svar.name),
                        kv("gammelt_aarsaker",resultatGammelRegelMotor.resultat.årsaker.map { it.regelId }.toString()),
                        kv("konklusjon",konklusjon.status.name),
                        kv("avklaringer",konklusjon.avklaringsListe.map { it.regel_id }.toString()),
                        kv("response",haleRespons.toPrettyString()),
                        kv("callId",key),
                        kv("erTredjelandsborger",tredjelandsBorger),
                        kv("harSP6000ProssesertGammeltResultat",harHaleProssessertresultatFraGammelRegelmotor),
                        kv("fnr",resultatGammelRegelMotor.datagrunnlag.fnr),
                        kv("nye_sporsmaal",resultatGammelRegelMotor.datagrunnlag.brukerinput.utfortAarbeidUtenforNorge!=null),

                        kv("oppholdUtenforEØS",resultatGammelRegelMotor.datagrunnlag.brukerinput.oppholdUtenforEØSOppgitt()),
                        kv("oppholdUtenforEØS_land",resultatGammelRegelMotor.datagrunnlag.brukerinput.oppgittOppholdUtenforEØSLand()),
                        kv("oppholdUtenforEØS_periode",resultatGammelRegelMotor.datagrunnlag.brukerinput.oppgittOppholdUtenforEØSPeriode()),
                        kv("oppholdUtenforEØS_grunn",resultatGammelRegelMotor.datagrunnlag.brukerinput.oppgittOppholdUtenforEØSGrunn()),

                        kv("oppholdUtenforNorge",resultatGammelRegelMotor.datagrunnlag.brukerinput.oppholdUtenforNorgeOpppgitt()),
                        kv("oppholdUtenforNorge_land",resultatGammelRegelMotor.datagrunnlag.brukerinput.oppgittOppholdUtenforNorgeLand()),
                        kv("oppholdUtenforNorge_periode",resultatGammelRegelMotor.datagrunnlag.brukerinput.oppgittOppholdUtenforNorgePeriode()),
                        kv("oppholdUtenforNorge_grunn",resultatGammelRegelMotor.datagrunnlag.brukerinput.oppgittOppholdUtenforNorgeGrunn()),

                        kv("utfortAarbeidUtenforNorge",resultatGammelRegelMotor.datagrunnlag.brukerinput.utfortAarbeidUtenforNorgeOpppgitt()),
                        kv("utfortAarbeidUtenforNorge_land",resultatGammelRegelMotor.datagrunnlag.brukerinput.oppgittArbeidUtenforNorgeLand()),
                        kv("utfortAarbeidUtenforNorge_periode",resultatGammelRegelMotor.datagrunnlag.brukerinput.oppgittArbeidUtenforNorgePeriode()),


                        kv("statsborgerskap",resultatGammelRegelMotor.datagrunnlag.statsborgerskap()),

                        kv("mar-gyldighetsperiode",resultatGammelRegelMotor.datagrunnlag.sisteMaritimeArbeidsavtalePeriode()),
                        kv("mar-skipstype",resultatGammelRegelMotor.datagrunnlag.sisteMaritimeArbeidsavtaleSkipstype()),
                        kv("mar-fartsomraade",resultatGammelRegelMotor.datagrunnlag.sisteMaritimeArbeidsavtaleFartsomraade()),
                        kv("mar-skipsregister",resultatGammelRegelMotor.datagrunnlag.sisteMaritimeArbeidsavtaleSkipsregister()),

                        kv("permisjon-periode",resultatGammelRegelMotor.datagrunnlag.siste_permisjonPermitteringPeriode(kontrollPeriode)),
                        kv("permisjon-type",resultatGammelRegelMotor.datagrunnlag.siste_permisjonPermitteringType(kontrollPeriode)),
                        kv("permisjon-prosent",resultatGammelRegelMotor.datagrunnlag.siste_permisjonPermitteringProsent(kontrollPeriode)),

                        kv("oppholdstillatelse",resultatGammelRegelMotor.datagrunnlag.brukerinput.oppholdstillatelseOppgitt()),
                        kv("oppholdstillatelse_periode",resultatGammelRegelMotor.datagrunnlag.brukerinput.oppholdstillatelsePeriode()),
                        kv("udi_oppholdstillatelse_periode",resultatGammelRegelMotor.datagrunnlag.udiOppholdstillatelsePeriode()),
                        kv("udi_oppholdstillatelse_type",resultatGammelRegelMotor.datagrunnlag.udiOppholdstillatelseType()),

                        kv("erTredjelandsborgerMedEØSFamilie",konklusjon.erTredjelandsborgerMedEOSFamilie()),

                        kv("antall_dager_sykemelding",resultatGammelRegelMotor.datagrunnlag.periode.antallDager()),
                        kv("PDL_SAMSVAR",pdl_samsvar),
                        kv("aarsaker",resultatGammelRegelMotor.resultat.årsaker.map { it.regelId }.toString()),
                        kv("analyse","NEI")
                    )
                }
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

    /*
    * SP9000 ?
    * */
    private fun lagKonklusjon(resultatGammelRegelMotor: Kjøring, responsRegelMotorHale: Resultat): Konklusjon {

        if (Svar.JA == resultatGammelRegelMotor.resultat.svar){
            return Konklusjon(
                dato = resultatGammelRegelMotor.tidspunkt.toLocalDate(),
                status = Svar.JA,
                lovvalg = null,
                dekningForSP = DekningsAltrnativer.UAVKLART,
                medlemskap = Medlemskap("JA",""),
                avklaringsListe = emptyList(),
                reglerKjørt = responsRegelMotorHale.delresultat,
                utledetInformasjoner = responsRegelMotorHale.utledetInformasjon
            )
        }
        if (Svar.NEI == resultatGammelRegelMotor.resultat.svar){
            return Konklusjon(
                dato = resultatGammelRegelMotor.tidspunkt.toLocalDate(),
                status = Svar.NEI,
                lovvalg = null,
                dekningForSP = DekningsAltrnativer.UAVKLART,
                medlemskap = Medlemskap("Nei",""),
                avklaringsListe = finnAvklaringsPunkter(resultatGammelRegelMotor,responsRegelMotorHale),
                reglerKjørt = responsRegelMotorHale.delresultat,
                utledetInformasjoner = responsRegelMotorHale.utledetInformasjon
            )
        }
        if (responsRegelMotorHale.svar == Svar.JA){
            return Konklusjon(
                dato = LocalDate.now(),
                status = Svar.JA,
                lovvalg = null,
                dekningForSP = DekningsAltrnativer.JA,
                medlemskap = Medlemskap("JA",""),
                avklaringsListe = emptyList(),
                reglerKjørt = responsRegelMotorHale.delresultat,
                utledetInformasjoner = responsRegelMotorHale.utledetInformasjon
            )
        }
        else if (responsRegelMotorHale.svar == Svar.NEI){
            return Konklusjon(
                dato = LocalDate.now(),
                status = Svar.NEI,
                lovvalg = null,
                dekningForSP = DekningsAltrnativer.NEI,
                medlemskap = Medlemskap("NEI",""),
                reglerKjørt = responsRegelMotorHale.delresultat,
                avklaringsListe = emptyList(),
                utledetInformasjoner = responsRegelMotorHale.utledetInformasjon
            )
        }
        else{

            return Konklusjon(
                dato = LocalDate.now(),
                status = Svar.UAVKLART,
                lovvalg = null,
                dekningForSP = DekningsAltrnativer.UAVKLART,
                medlemskap = null,
                reglerKjørt = responsRegelMotorHale.delresultat,
                avklaringsListe = finnAvklaringsPunkter(resultatGammelRegelMotor,responsRegelMotorHale),
                utledetInformasjoner = responsRegelMotorHale.utledetInformasjon
            )
        }
    }

    private fun finnAvklaringsPunkter(resultatGammelRegelMotor: Kjøring, responsRegelMotorHale: Resultat): List<avklaring> {
    val avklaringsListe:MutableList<avklaring> = mutableListOf()
        avklaringsListe.addAll(responsRegelMotorHale.årsaker.filterNot { it.regelId == RegelId.SP6510 }.map { mapToAvklaring(it) })
        val avklaringerGammelKjøring = resultatGammelRegelMotor.resultat.årsaker.map{mapToAvklaringModel2(it) }

        //håndterer norske borgere
        if (responsRegelMotorHale.utledetInformasjon.find { Informasjon.NORSK_BORGER == it.informasjon } !=null ){
            avklaringsListe.addAll(finnAvklaringerSomIkkeErSjekketUt(avklaringerGammelKjøring,responsRegelMotorHale,RegelId.SP6510))
            return avklaringsListe.filterNot { it.regel_id == RegelId.SP6510.name }
        }
        //håndterer EØS borgere
        else if (responsRegelMotorHale.utledetInformasjon.find { Informasjon.EØS_BORGER == it.informasjon } !=null ){
            avklaringsListe.addAll(finnAvklaringerSomIkkeErSjekketUt(avklaringerGammelKjøring,responsRegelMotorHale,RegelId.SP6600))
            return avklaringsListe.filterNot { it.regel_id == RegelId.SP6600.name }
        }
        //håndterer 3 lands borgere
        else if (responsRegelMotorHale.utledetInformasjon.find { Informasjon.TREDJELANDSBORGER == it.informasjon } !=null ){
            avklaringsListe.addAll(finnAvklaringerSomIkkeErSjekketUt(avklaringerGammelKjøring,responsRegelMotorHale,RegelId.SP6700))
            return avklaringsListe.filterNot { it.regel_id == RegelId.SP6700.name }
        }
        //håndterer 3 lands borgere med EOS familie
        else if (responsRegelMotorHale.utledetInformasjon.find { Informasjon.TREDJELANDSBORGER_MED_EOS_FAMILIE == it.informasjon } !=null ){
            avklaringsListe.addAll(finnAvklaringerSomIkkeErSjekketUt(avklaringerGammelKjøring,responsRegelMotorHale,RegelId.SP6600))
            return avklaringsListe.filterNot { it.regel_id == RegelId.SP6600.name }
        }

        return avklaringsListe
    }

    private fun finnAvklaringerSomIkkeErSjekketUt(
        avklaringerGammelKjøring: List<avklaring>,
        responsRegelMotorHale: Resultat,
        regelId: RegelId
    ): List<avklaring> {

        if (Svar.UAVKLART == responsRegelMotorHale.svar){
            return avklaringerGammelKjøring
        }

        val kanAlleRegelBruddSjekkesUtRegelResultat = responsRegelMotorHale.finnRegelResultat(regelId)
        if (kanAlleRegelBruddSjekkesUtRegelResultat == null){
            return avklaringerGammelKjøring
        }
        val faktum = kanAlleRegelBruddSjekkesUtRegelResultat!!.utledetInformasjon.find { Informasjon.IKKE_SJEKKET_UT ==it.informasjon }
        if (faktum != null) {
            val rest = avklaringerGammelKjøring.filter { faktum.kilde.contains(it.regel_id) }
           return rest
        }
        return emptyList()
    }

    private fun mapToAvklaringModel2(aarsak: no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.domene.Årsak):avklaring {
        return avklaring(aarsak.regelId,aarsak.avklaring,aarsak.svar.name,"UAVKLART",null,"SP6000", LocalDate.now())
    }

    fun mapToAvklaring(aarsak:Årsak):avklaring{
        return avklaring(aarsak.regelId.name,aarsak.avklaring,aarsak.svar.name,"UAVKLART",null,"SP6000", LocalDate.now())
    }


}

