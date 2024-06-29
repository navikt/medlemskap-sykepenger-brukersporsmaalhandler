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




        /*
        val erVedtaksdatoForPermanentOppgittFraBrukerMerEn12mndTilbakeITid =
            lagRegelflyt(
                regel = hentRegel(RegelId.SP6223),
                hvisJa = Regelflyt.regelflytJa(ytelse,RegelId.OPHOLDSTILATELSE_FLYT),
                hvisNei = Regelflyt.regelflytUavklart(ytelse,RegelId.OPHOLDSTILATELSE_FLYT)
            )

         */

        /*
        val erStartdatoPermanentOppgittFraBrukerMerEn12mndTilbakeITid =
            lagRegelflyt(
                regel = hentRegel(RegelId.SP6222),
                hvisJa = erVedtaksdatoForPermanentOppgittFraBrukerMerEn12mndTilbakeITid,
                hvisNei = Regelflyt.regelflytUavklart(ytelse,RegelId.OPHOLDSTILATELSE_FLYT)
            )

         */
        /*
        val erSluttDatoForMidlertidigOppholdstilatelse2mndFremTidRegel =
            lagRegelflyt(
                regel = hentRegel(RegelId.SP6241),
                hvisJa = Regelflyt.regelflytJa(ytelse,RegelId.OPHOLDSTILATELSE_FLYT),
                hvisNei = Regelflyt.regelflytUavklart(ytelse,RegelId.OPHOLDSTILATELSE_FLYT)
            )

         */

        /*
        val erStartDatoForMidlertidigOppholdstilatelse12mndTilbakeITid =
            lagRegelflyt(
                regel = hentRegel(RegelId.SP6231),
                hvisJa = erSluttDatoForMidlertidigOppholdstilatelse2mndFremTidRegel,
                hvisNei = Regelflyt.regelflytUavklart(ytelse,RegelId.OPHOLDSTILATELSE_FLYT)
            )

         */

        /*
        val harBrukerOpplystOmPermanentOppholdsTilatelseRegel = lagRegelflyt(
            regel = hentRegel(RegelId.SP6221),
            hvisJa = erStartdatoPermanentOppgittFraBrukerMerEn12mndTilbakeITid,
            hvisNei = erStartDatoForMidlertidigOppholdstilatelse12mndTilbakeITid,
        )

         */


        val erSammenSlåttPeriodeMinst1ÅrTilbakeOg2MndFrem = lagRegelflyt(
            regel = hentRegel(RegelId.SP6226),
            hvisJa = Regelflyt.regelflytJa(ytelse,RegelId.OPHOLDSTILATELSE_FLYT),
            hvisNei = Regelflyt.regelflytUavklart(ytelse,RegelId.OPHOLDSTILATELSE_FLYT),
        )
        //vi skal ikke slå ut på denne regelen lenger, men vil ha sporing at regelen har svart NEI
        val erOppgittOppholdstilatelseFunksjoneltLiktPDLInnslag = lagRegelflyt(
            regel = hentRegel(RegelId.SP6229),
            hvisJa = erSammenSlåttPeriodeMinst1ÅrTilbakeOg2MndFrem,
            hvisNei = erSammenSlåttPeriodeMinst1ÅrTilbakeOg2MndFrem,
        )

        val kanOppgittPeriodeSlaasSammenMedUDIPeriodeTilEnSammenhengedePeriode = lagRegelflyt(
            regel = hentRegel(RegelId.SP6225),
            hvisJa = erOppgittOppholdstilatelseFunksjoneltLiktPDLInnslag,
            hvisNei = Regelflyt.regelflytUavklart(ytelse,RegelId.OPHOLDSTILATELSE_FLYT),
        )

        val erDetOppgittBrukerspørsmålOmOppholdsTilatelse = lagRegelflyt(
            regel = hentRegel(RegelId.SP6211),
            hvisJa = kanOppgittPeriodeSlaasSammenMedUDIPeriodeTilEnSammenhengedePeriode,
            hvisNei = Regelflyt.regelflytJa(ytelse,RegelId.OPHOLDSTILATELSE_FLYT),
        )
        val erDetOppgittBrukerspørsmålOmOppholdsTilatelseSelvOmIkkeRegelBrudd = lagRegelflyt(
            regel = hentRegel(RegelId.SP6212),
            hvisJa = Regelflyt.regelflytUavklart(ytelse,RegelId.OPHOLDSTILATELSE_FLYT),
            hvisNei = Regelflyt.regelflytJa(ytelse,RegelId.OPHOLDSTILATELSE_FLYT),
        )


        val erDetRegelBruddForOppholdTilatelseIGammelFlyt = lagRegelflyt(
            regel = hentRegel(RegelId.SP6201),
            hvisJa = erDetOppgittBrukerspørsmålOmOppholdsTilatelse,
            hvisNei = erDetOppgittBrukerspørsmålOmOppholdsTilatelseSelvOmIkkeRegelBrudd,
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
