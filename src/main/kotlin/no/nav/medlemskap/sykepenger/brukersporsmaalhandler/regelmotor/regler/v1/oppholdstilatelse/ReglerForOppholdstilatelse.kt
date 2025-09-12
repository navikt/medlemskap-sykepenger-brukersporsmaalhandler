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

        val erSammenSlåttPeriodeMinst1ÅrTilbakeOg2MndFrem = lagRegelflyt(
            regel = hentRegel(RegelId.SP6226),
            hvisJa = Regelflyt.regelflytJa(ytelse,RegelId.OPHOLDSTILATELSE_FLYT),
            hvisNei = Regelflyt.regelflytUavklart(ytelse,RegelId.OPHOLDSTILATELSE_FLYT),
        )

        val kanOppgittPeriodeSlaasSammenMedUDIPeriodeTilEnSammenhengedePeriode = lagRegelflyt(
            regel = hentRegel(RegelId.SP6225),
            hvisJa = erSammenSlåttPeriodeMinst1ÅrTilbakeOg2MndFrem,
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
