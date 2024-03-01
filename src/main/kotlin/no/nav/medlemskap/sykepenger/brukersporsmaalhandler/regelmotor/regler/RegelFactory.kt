package no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.regler


import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.Regel
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.RegelId
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.domene.Datagrunnlag
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.domene.GammelkjøringResultat
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.domene.Årsak
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.regler.v1.HarBrukerOppgittArbeidUtlandGammelModell
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.regler.v1.HarBrukerOppgittArbeidUtenforNorgeNyModell
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.regler.v1.HarBrukerSvartNeiForArbeidUtenforNorgeNyModell
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.regler.v1.oppholdsRegler.*
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.regler.v1.oppholdstilatelse.ErDetRegelBruddForOppholdTilatelseIGammelFlytRegel
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.regler.v1.oppholdstilatelse.FinnesBrukerSvarForOppholdstilatelseRegel
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.regler.v1.regelutsjekk.KanAlleRegelBruddSjekkesUtNorskeBorgereRegel
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.regler.v1.regelutsjekk.eosborgere.KanAlleRegelBruddSjekkesUtEOSBorgereRegel
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.regler.v1.regelutsjekk.tredelandsborgere.KanAlleRegelBruddSjekkesUtTredjelandBorgereRegel


class RegelFactory(private val datagrunnlag: Datagrunnlag,private val årsaker:List<Årsak> = emptyList(),private val gameltResultat:GammelkjøringResultat? = null) {

    fun create(regelIdentifikator: String): Regel {
        val regelId = RegelId.fraRegelIdString(regelIdentifikator)

        return create(regelId)
    }

    fun create(regelId: RegelId): Regel {
        return when (regelId) {

            RegelId.SP6201 -> ErDetRegelBruddForOppholdTilatelseIGammelFlytRegel.fraDatagrunnlag(datagrunnlag,gameltResultat).regel
            RegelId.SP6211 -> FinnesBrukerSvarForOppholdstilatelseRegel.fraDatagrunnlag(datagrunnlag).regel


            RegelId.SP6130 -> HarBrukerOppgittArbeidUtlandGammelModell.fraDatagrunnlag(datagrunnlag).regel

            RegelId.SP6311 -> HarBrukerOppholdtsegUtenForEØSRegel.fraDatagrunnlag(datagrunnlag).regel
            RegelId.SP6312 -> ErDetBareEttUtenlandsoppholdRegel.fraDatagrunnlag(datagrunnlag).regel
            RegelId.SP6313 -> BleOppholdetAvsluttetForMerEnn90DagerSidenRegel.fraDatagrunnlag(datagrunnlag).regel
            RegelId.SP6314 -> ErOppholdetIUtlandetKortereEnn180DagerRegel.fraDatagrunnlag(datagrunnlag).regel


            RegelId.SP6301 -> FinnesBrukerSvarForOppholdUtenforEØSRegel.fraDatagrunnlag(datagrunnlag).regel
            RegelId.SP6110 -> HarBrukerOppgittArbeidUtenforNorgeNyModell.fraDatagrunnlag(datagrunnlag).regel
            RegelId.SP6120 -> HarBrukerSvartNeiForArbeidUtenforNorgeNyModell.fraDatagrunnlag(datagrunnlag).regel
            RegelId.SP6510 -> KanAlleRegelBruddSjekkesUtNorskeBorgereRegel.fraDatagrunnlag(datagrunnlag,årsaker).regel
            RegelId.SP6600 -> KanAlleRegelBruddSjekkesUtEOSBorgereRegel.fraDatagrunnlag(datagrunnlag,årsaker).regel
            RegelId.SP6700 -> KanAlleRegelBruddSjekkesUtTredjelandBorgereRegel.fraDatagrunnlag(datagrunnlag,årsaker).regel
            RegelId.SP6702 -> HarBrukerSvartNeiPaaOppholdUtenforNorgeRegel.fraDatagrunnlag(datagrunnlag).regel
            RegelId.SP6703 -> FinnesBrukerSvarForOppholdUtenforNorgeRegel.fraDatagrunnlag(datagrunnlag).regel
            else -> throw java.lang.RuntimeException("Ukjent regel: $regelId")
        }
    }
}
