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

    ) : BasisRegel(RegelId.SP6500, ytelse) {
        val reglerSomSjekkesUtMedArbeidINorgeOgIngenOppholdUtland =
            listOf(
                "REGEL_3"
            )
        val reglerSomSjekkesUtMedArbeidINorgeTrue =
        listOf(
            "REGEL_3","REGEL_9"
        )

    override fun operasjon(): Resultat {

        if (årsaker.isEmpty()){
            return Resultat.ja(regelId)
        }
        val toBeControlled:MutableList<Årsak> = mutableListOf()
        toBeControlled.addAll(årsaker)
        //fjern alle regler som kan sjekkes ut med ingen arbeid i utlandet og ingen opphold i utlandet
        if (true == brukerInput?.bådeArbeidUtlandOgOppholdUtenforEOSFalse()){
           toBeControlled.removeIf{reglerSomSjekkesUtMedArbeidINorgeOgIngenOppholdUtland.contains(it.regelId)}
        }

        if (toBeControlled.isEmpty()){
            return Resultat.ja(regelId)
        }
        if (false == brukerInput?.arbeidUtlandTrue()){
            toBeControlled.removeIf{reglerSomSjekkesUtMedArbeidINorgeTrue.contains(it.regelId)}
        }
        return Resultat(
            regelId = RegelId.SP6500,
            svar = Svar.NEI,
            utledetInformasjon = listOf(UtledetInformasjon(Informasjon.IKKE_SJEKKET_UT,toBeControlled.map { it.regelId }))
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

fun Brukerinput.bådeArbeidUtlandOgOppholdUtenforEOSFalse() :Boolean{
    return (false == utfortAarbeidUtenforNorge?.svar) && (false == oppholdUtenforEos?.svar)
}
fun Brukerinput.arbeidUtlandTrue() :Boolean{
    return (true == utfortAarbeidUtenforNorge?.svar)
}


