package no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.regler


import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.Regel
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.RegelId
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.domene.Datagrunnlag
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.domene.Årsak
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.regler.v1.HarBrukerOppgittArbeidUtlandGammelModell
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.regler.v1.HarBrukerOppgittArbeidUtenforNorgeNyModell
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.regler.v1.HarBrukerSvartJAForArbeidUtenforNorgeNyModell
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.regler.v1.oppholdsRegler.FinnesBrukerSvarForOppholdUtenforEØSRegel
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.regler.v1.oppholdsRegler.HarBrukerOppgittOppholdUtenforEØSRegel
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.regler.v1.regelutsjekk.KanAlleRegelBruddSjekkesUtNorskeBorgereRegel


class RegelFactory(private val datagrunnlag: Datagrunnlag,private val årsaker:List<Årsak> = emptyList()) {

    fun create(regelIdentifikator: String): Regel {
        val regelId = RegelId.fraRegelIdString(regelIdentifikator)

        return create(regelId)
    }

    fun create(regelId: RegelId): Regel {
        return when (regelId) {
            RegelId.SP6100 -> HarBrukerOppgittArbeidUtlandGammelModell.fraDatagrunnlag(datagrunnlag).regel
            RegelId.SP6002 -> HarBrukerOppgittOppholdUtenforEØSRegel.fraDatagrunnlag(datagrunnlag).regel
            RegelId.SP6003 -> FinnesBrukerSvarForOppholdUtenforEØSRegel.fraDatagrunnlag(datagrunnlag).regel
            RegelId.SP6110 -> HarBrukerOppgittArbeidUtenforNorgeNyModell.fraDatagrunnlag(datagrunnlag).regel
            RegelId.SP6120 -> HarBrukerSvartJAForArbeidUtenforNorgeNyModell.fraDatagrunnlag(datagrunnlag).regel
            RegelId.SP6500 -> KanAlleRegelBruddSjekkesUtNorskeBorgereRegel.fraDatagrunnlag(datagrunnlag,årsaker).regel
            else -> throw java.lang.RuntimeException("Ukjent regel: $regelId")
        }
    }
}
