package no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.domene

import java.time.LocalDateTime


data class Kjøring(
    val tidspunkt : LocalDateTime,
    val kanal:String,
    val datagrunnlag: Datagrunnlag,
    val resultat: GammelkjøringResultat,
)