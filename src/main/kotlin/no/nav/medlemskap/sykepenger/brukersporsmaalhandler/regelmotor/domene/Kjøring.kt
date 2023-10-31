package no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.domene



data class Kjøring(
    val datagrunnlag: Datagrunnlag,
    val resultat: GammelkjøringResultat,
)