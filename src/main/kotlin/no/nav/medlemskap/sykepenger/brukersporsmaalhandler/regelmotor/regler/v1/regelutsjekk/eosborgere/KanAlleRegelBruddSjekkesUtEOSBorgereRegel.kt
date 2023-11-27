package no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.regler.v1.regelutsjekk.eosborgere


import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.*
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.domene.Brukerinput
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.domene.Datagrunnlag
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.domene.Årsak
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.regler.v1.regelutsjekk.arbeidUtlandTrue
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.regler.v1.regelutsjekk.bådeArbeidUtlandOgOppholdUtenforEOSFalse
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.regler.v1.regelutsjekk.tredelandsborgere.KanAlleRegelBruddSjekkesUtTredjelandBorgereRegel
import java.time.LocalDate

class KanAlleRegelBruddSjekkesUtEOSBorgereRegel(
    ytelse: Ytelse,
    startDatoForYtelse: LocalDate,
    private val brukerInput: Brukerinput?,
    private val årsaker: List<Årsak> = emptyList()

    ) : BasisRegel(RegelId.SP6600, ytelse) {
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
        //fjer alle regler som kan sjekkes ut med ingen arbeid i utlandet og ingen opphold i utlandet
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
            regelId = RegelId.SP6600,
            svar = Svar.NEI,
            utledetInformasjon = listOf(UtledetInformasjon(Informasjon.IKKE_SJEKKET_UT,toBeControlled.map { it.regelId }))
        )
    }



    companion object {
        fun fraDatagrunnlag(datagrunnlag: Datagrunnlag,årsaker: List<Årsak>): KanAlleRegelBruddSjekkesUtEOSBorgereRegel {
            return KanAlleRegelBruddSjekkesUtEOSBorgereRegel(
                ytelse = datagrunnlag.ytelse,
                startDatoForYtelse = datagrunnlag.periode.fom,
                brukerInput = datagrunnlag.brukerinput,
                årsaker = årsaker
            )
        }
    }
}



