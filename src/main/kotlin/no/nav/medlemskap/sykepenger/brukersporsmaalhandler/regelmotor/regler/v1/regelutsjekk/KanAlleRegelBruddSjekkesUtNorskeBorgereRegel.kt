package no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.regler.v1.regelutsjekk


import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.*
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.domene.Brukerinput
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.domene.Datagrunnlag
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.domene.Årsak
import java.time.LocalDate

class KanAlleRegelBruddSjekkesUtNorskeBorgereRegel(
    ytelse: Ytelse,
    startDatoForYtelse: LocalDate,
    private val brukerInput: Brukerinput?,
    private val årsaker: List<Årsak> = emptyList()

    ) :  BasisRegel(RegelId.SP6510, ytelse) {

    val multiReglerSomKanSjekkesUt =
        listOf(
            "REGEL_11"
        )
    val reglerSomKanSjekkesUt =
        listOf(
            "REGEL_3", "REGEL_9", "REGEL_C", "REGEL_15"
        )

    override fun operasjon(): Resultat {

        if (årsaker.isEmpty()) {
            return Resultat.ja(regelId)
        }
        val toBeControlled: MutableList<Årsak> = mutableListOf()
        toBeControlled.addAll(årsaker)
        //fjern alle regler som kan sjekkes ut med ingen arbeid i utlandet og ingen opphold i utlandet
        if (brukerInput?.bådeArbeidUtlandOgOppholdUtenforEOSOppgitt() == true) {
            toBeControlled.removeIf { reglerSomKanSjekkesUt.contains(it.regelId) }
            //fjern alle innslag som starter med angitt regel i multiRegelLista
            multiReglerSomKanSjekkesUt.forEach { regel ->
                toBeControlled.removeIf{it.regelId.startsWith(regel)}
            }
        }

        if (toBeControlled.isEmpty()) {
            return Resultat.ja(regelId)
        }

        return Resultat(
            regelId = RegelId.SP6510,
            svar = Svar.NEI,
            utledetInformasjon = listOf(
                UtledetInformasjon(
                    Informasjon.IKKE_SJEKKET_UT,
                    toBeControlled.map { it.regelId })
            )
        )
    }
    companion object {
        fun fraDatagrunnlag(datagrunnlag: Datagrunnlag,årsaker: List<Årsak>): KanAlleRegelBruddSjekkesUtNorskeBorgereRegel {
            return KanAlleRegelBruddSjekkesUtNorskeBorgereRegel(
                ytelse = datagrunnlag.ytelse,
                startDatoForYtelse = datagrunnlag.periode.fom,
                brukerInput = datagrunnlag.brukerinput,
                årsaker = årsaker
            )
        }
    }

}

fun Brukerinput.bådeArbeidUtlandOgOppholdUtenforEOSOppgitt() :Boolean{
    return (utfortAarbeidUtenforNorge?.svar !=null ) && (oppholdUtenforEos?.svar !=null)
}


