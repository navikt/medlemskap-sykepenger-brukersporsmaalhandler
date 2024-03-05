package no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.regler.v1.oppholdstilatelse


import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.RegelId
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.Regelflyt
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.Regler
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.Ytelse
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.domene.Datagrunnlag
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.domene.GammelkjøringResultat
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.domene.InputPeriode
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.regler.RegelFactory


/*
* SP6201 Regel som sjekker om det er oppgitt brukersvar for oppholdstilatelse
* */
class ReglerForOppholdstilatelse(
    val periode: InputPeriode,
    ytelse: Ytelse,
    regelFactory: RegelFactory,
) : Regler(ytelse, regelFactory) {

    override fun hentHovedflyt(): Regelflyt {


        val erVedtaksdatoForPermanentOppgittFraBrukerMerEn12mndTilbakeITid =
            lagRegelflyt(
                regel = hentRegel(RegelId.SP6223),
                hvisJa = Regelflyt.regelflytJa(ytelse,RegelId.OPHOLDSTILATELSE_FLYT),
                hvisNei = Regelflyt.regelflytUavklart(ytelse,RegelId.OPHOLDSTILATELSE_FLYT)
            )

        val erSluttDatoForMidlertidigOppholdstilatelse2mndFremTidRegel =
            lagRegelflyt(
                regel = hentRegel(RegelId.SP6241),
                hvisJa = Regelflyt.regelflytJa(ytelse,RegelId.OPHOLDSTILATELSE_FLYT),
                hvisNei = Regelflyt.regelflytUavklart(ytelse,RegelId.OPHOLDSTILATELSE_FLYT)
            )

        val erStartDatoForMidlertidigOppholdstilatelse12mndTilbakeITid =
            lagRegelflyt(
                regel = hentRegel(RegelId.SP6232),
                hvisJa = erSluttDatoForMidlertidigOppholdstilatelse2mndFremTidRegel,
                hvisNei = Regelflyt.regelflytUavklart(ytelse,RegelId.OPHOLDSTILATELSE_FLYT)
            )

        val harBrukerOpplystOmPermanentOppholdsTilatelseRegel = lagRegelflyt(
            regel = hentRegel(RegelId.SP6221),
            hvisJa = erVedtaksdatoForPermanentOppgittFraBrukerMerEn12mndTilbakeITid,
            hvisNei = erStartDatoForMidlertidigOppholdstilatelse12mndTilbakeITid,
        )


        val erDetOppgittBrukerspørsmålOmOppholdsTilatelse = lagRegelflyt(
            regel = hentRegel(RegelId.SP6211),
            hvisJa = harBrukerOpplystOmPermanentOppholdsTilatelseRegel,
            hvisNei = Regelflyt.regelflytJa(ytelse,RegelId.OPHOLDSTILATELSE_FLYT),
        )

        val erDetRegelBruddForOppholdTilatelseIGammelFlyt = lagRegelflyt(
            regel = hentRegel(RegelId.SP6201),
            hvisJa = erDetOppgittBrukerspørsmålOmOppholdsTilatelse,
            hvisNei = Regelflyt.regelflytJa(ytelse,RegelId.OPHOLDSTILATELSE_FLYT),
        )


        return erDetRegelBruddForOppholdTilatelseIGammelFlyt
    }

    companion object {
        fun fraDatagrunnlag(datagrunnlag: Datagrunnlag, resultat: GammelkjøringResultat): ReglerForOppholdstilatelse {

            return ReglerForOppholdstilatelse(
                periode = datagrunnlag.periode,
                ytelse = datagrunnlag.ytelse,
                regelFactory = RegelFactory(datagrunnlag=datagrunnlag, gameltResultat = resultat),
            )
        }
    }
}
