package no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.regler


import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.Regel
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.RegelId
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.domene.Datagrunnlag
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.regler.v1.ArbeidUtenforNorgeBrukerSporsmaalRegel
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.regler.v1.norskeborgere.FinnesBrukerSvarForOppholdUtenforEØSRegel
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.regler.v1.norskeborgere.HarBrukerOppgittOppholdUtenforEØSRegel

class RegelFactory(private val datagrunnlag: Datagrunnlag) {

    fun create(regelIdentifikator: String): Regel {
        val regelId = RegelId.fraRegelIdString(regelIdentifikator)

        return create(regelId)
    }

    fun create(regelId: RegelId): Regel {
        return when (regelId) {
            RegelId.SP6001 -> ArbeidUtenforNorgeBrukerSporsmaalRegel.fraDatagrunnlag(datagrunnlag).regel
            RegelId.SP6002 -> HarBrukerOppgittOppholdUtenforEØSRegel.fraDatagrunnlag(datagrunnlag).regel
            RegelId.SP6003 -> FinnesBrukerSvarForOppholdUtenforEØSRegel.fraDatagrunnlag(datagrunnlag).regel
          else -> throw java.lang.RuntimeException("Ukjent regel: $regelId")
        }
    }
}
