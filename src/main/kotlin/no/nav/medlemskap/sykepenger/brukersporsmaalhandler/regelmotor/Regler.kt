package no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor

import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.regler.RegelFactory
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.Regelflyt.Companion.medlemskonklusjonUavklart
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.Resultat.Companion.nei
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.Resultat.Companion.uavklart

abstract class Regler(
    val ytelse: Ytelse,
    val regelFactory: RegelFactory,
    val overstyrteRegler: Map<RegelId, Svar> = emptyMap()
) {

    abstract fun hentHovedflyt(): Regelflyt

    fun kjørHovedflyt(): Resultat {
        return kjørRegelflyt(hentHovedflyt())
    }

    protected fun kjørRegelflyt(regelflyt: Regelflyt): Resultat {
        return regelflyt.utfør()
    }

    protected fun lagRegelflyt(
        regel: Regel,
        hvisJa: Regelflyt? = null,
        hvisNei: Regelflyt? = null,
        hvisUavklart: Regelflyt = medlemskonklusjonUavklart(ytelse),
        årsak: Årsak? = null
    ): Regelflyt {
        return Regelflyt(
            regel = regel,
            hvisJa = hvisJa,
            hvisNei = hvisNei,
            hvisUavklart = hvisUavklart,
            overstyrteRegler = overstyrteRegler,
            årsak = årsak
        )
    }

    protected fun hentRegel(regelId: RegelId): Regel {
        return regelFactory.create(regelId)
    }

    companion object {
        private fun utledResultat(resultater: Map<RegelId, Resultat>): Resultat {

            return when {
                resultater.values.any { it.svar == Svar.JA } -> uavklart(RegelId.REGEL_OPPLYSNINGER, årsak = Årsak.fraResultat(resultater.values.first { it.svar == Svar.JA }))
                else -> nei(RegelId.REGEL_OPPLYSNINGER)
            }
        }

        fun minstEnAvDisse(vararg regler: Regel): Resultat {
            val delresultatMap = regler.map { it.regelId to it.utfør() }.toMap()

            return utledResultat(delresultatMap).copy(
                delresultat = delresultatMap.values.toList()
            )
        }
    }
}
