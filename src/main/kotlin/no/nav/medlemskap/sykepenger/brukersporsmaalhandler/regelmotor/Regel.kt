package no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor




data class Regel(
    val regelId: RegelId,
    val ytelse: Ytelse,
    val operasjon: () -> Resultat
) {
    fun utfør(): Resultat = operasjon.invoke().apply {
    }.copy(
        regelId = regelId
    )

    companion object {
        fun regelUavklart(ytelse: Ytelse, regelId: RegelId, konklusjonstype: Konklusjonstype = Konklusjonstype.REGEL) = Regel(
            regelId = regelId,
            ytelse = ytelse,
            operasjon = { Resultat.uavklart(regelId, konklusjonstype) }
        )

        fun regelJa(ytelse: Ytelse, regelId: RegelId, konklusjonstype: Konklusjonstype = Konklusjonstype.REGEL) = Regel(
            regelId = regelId,
            ytelse = ytelse,
            operasjon = { Resultat.ja(regelId, konklusjonstype) }
        )

        fun regelNei(ytelse: Ytelse, regelId: RegelId, konklusjonstype: Konklusjonstype = Konklusjonstype.REGEL) = Regel(
            regelId = regelId,
            ytelse = ytelse,
            operasjon = { Resultat.nei(regelId, konklusjonstype) }
        )

        fun uavklartKonklusjon(ytelse: Ytelse, regelId: RegelId = RegelId.REGEL_MEDLEM_KONKLUSJON) = Regel(
            regelId = regelId,
            ytelse = ytelse,
            operasjon = { Resultat.uavklart(regelId, Konklusjonstype.MEDLEM) }
        )

        fun jaKonklusjon(ytelse: Ytelse, regelId: RegelId = RegelId.REGEL_MEDLEM_KONKLUSJON) = Regel(
            regelId = regelId,
            ytelse = ytelse,
            operasjon = { Resultat.ja(regelId, Konklusjonstype.MEDLEM) }
        )

        fun neiKonklusjon(ytelse: Ytelse, regelId: RegelId = RegelId.REGEL_MEDLEM_KONKLUSJON) = Regel(
            regelId = regelId,
            ytelse = ytelse,
            operasjon = { Resultat.nei(regelId, Konklusjonstype.MEDLEM) }
        )
    }
}
