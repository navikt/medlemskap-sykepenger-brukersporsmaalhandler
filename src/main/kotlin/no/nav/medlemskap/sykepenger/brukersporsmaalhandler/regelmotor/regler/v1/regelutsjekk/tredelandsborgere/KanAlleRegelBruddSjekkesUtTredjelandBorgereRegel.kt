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
        val reglerSomSjekkesUtMedArbeidINorgeOgIngenOppholdUtland =
            listOf(
                "REGEL_3"
            )
    val reglerSomSjekkesUtOppholdstilatelseOppgitt =
        listOf(
            "REGEL_19_3_1"
        )
    override fun operasjon(): Resultat {

        if (årsaker.isEmpty()){
            return Resultat.ja(regelId)
        }
        val toBeControlled:MutableList<Årsak> = mutableListOf()
        toBeControlled.addAll(årsaker)
        //fjer alle regler som kan sjekkes ut med ingen arbeid i utlandet og ingen opphold i utlandet
        if (true == brukerInput?.bådeArbeidUtlandOgOppholdUtenforNorgeFalse()){
           toBeControlled.removeIf{reglerSomSjekkesUtMedArbeidINorgeOgIngenOppholdUtland.contains(it.regelId)}
        }
        //fjer alle regler som kan sjekkes ut med oppholdsTilatelseOppgitt
        if (toBeControlled.isEmpty()){
            return Resultat.ja(regelId)
        }
        if (brukerInput?.oppholdstilatelse!=null){
            toBeControlled.removeIf{reglerSomSjekkesUtOppholdstilatelseOppgitt.contains(it.regelId)}
        }
        if (toBeControlled.isEmpty()){
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

fun Brukerinput.bådeArbeidUtlandOgOppholdUtenforNorgeFalse() :Boolean{
    return (false == utfortAarbeidUtenforNorge?.svar) && (false == oppholdUtenforNorge?.svar)
}


