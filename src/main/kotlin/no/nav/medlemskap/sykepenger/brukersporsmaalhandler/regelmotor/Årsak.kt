package no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor

data class Årsak(val regelId: RegelId, val avklaring: String, val svar: Svar) {
    val begrunnelse = regelId.begrunnelse(svar)

    companion object {
        fun fraResultat(resultat: Resultat): Årsak {
            return Årsak(regelId = resultat.regelId, avklaring = resultat.avklaring, svar = resultat.svar)
        }
    }
}
