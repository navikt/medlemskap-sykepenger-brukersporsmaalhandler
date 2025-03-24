package no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.regler.v1.regelutsjekk.tredelandsborgere


import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.*
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.domene.Brukerinput
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.domene.Datagrunnlag
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.domene.Årsak
import java.time.LocalDate

class KanAlleRegelBruddSjekkesUtTredjelandBorgereRegel(
    ytelse: Ytelse,
    startDatoForYtelse: LocalDate,
    private val brukerInput: Brukerinput?,
    private val årsaker: List<Årsak> = emptyList()

    ) : BasisRegel(RegelId.SP6700, ytelse) {


    val reglerSomKanSjekkesUtMedArbeidOgOppholdOppgitt =
        listOf(
            "REGEL_3", "REGEL_9", "REGEL_C", "REGEL_15","REGEL_20", "REGEL_34", "REGEL_21", "REGEL_25", "REGEL_10"
        )
    val reglerSomKanSjekkesUtMedOppholdsTilatelseOppgitt =
        listOf(
            "REGEL_19_3_1"
        )

    override fun operasjon(): Resultat {

        if (årsaker.isEmpty()) {
            return Resultat.ja(regelId)
        }
        val toBeControlled: MutableList<Årsak> = mutableListOf()
        toBeControlled.addAll(årsaker)
        //fjern alle regler som kan sjekkes dersom det er oppgitt brukerspørsmål om oppholdstilatelse.
        if (brukerInput?.oppholdstilatelse !=null){
            toBeControlled.removeIf { reglerSomKanSjekkesUtMedOppholdsTilatelseOppgitt.contains(it.regelId) }
        }
        if (brukerInput?.bådeArbeidUtlandOgOppholdUtenforNorgeOppgitt() == true) {
            toBeControlled.removeIf { reglerSomKanSjekkesUtMedArbeidOgOppholdOppgitt.contains(it.regelId) }
        }

        if (toBeControlled.isEmpty()) {
            return Resultat.ja(regelId)
        }
        return Resultat(
            regelId = RegelId.SP6700,
            svar = Svar.NEI,
            utledetInformasjon = listOf(UtledetInformasjon(Informasjon.IKKE_SJEKKET_UT,toBeControlled.map { it.regelId }))
        )
    }



    companion object {
        fun fraDatagrunnlag(datagrunnlag: Datagrunnlag,årsaker: List<Årsak>): KanAlleRegelBruddSjekkesUtTredjelandBorgereRegel {
            return KanAlleRegelBruddSjekkesUtTredjelandBorgereRegel(
                ytelse = datagrunnlag.ytelse,
                startDatoForYtelse = datagrunnlag.periode.fom,
                brukerInput = datagrunnlag.brukerinput,
                årsaker = årsaker
            )
        }
    }

}

fun Brukerinput.bådeArbeidUtlandOgOppholdUtenforNorgeOppgitt() :Boolean{
    return (utfortAarbeidUtenforNorge?.svar !=null) && (oppholdUtenforNorge?.svar !=null)
}


