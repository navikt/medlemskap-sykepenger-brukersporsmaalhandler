package no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor


abstract class BasisRegel(val regelId: RegelId, val ytelse: Ytelse) {
    val regel = Regel(regelId, ytelse, { operasjon() })

    fun utfør(): Resultat {
        return regel.utfør()
    }

    abstract fun operasjon(): Resultat
}
