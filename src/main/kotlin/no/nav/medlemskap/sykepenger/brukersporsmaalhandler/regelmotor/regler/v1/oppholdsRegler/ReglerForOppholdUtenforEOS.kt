package no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.regler.v1.oppholdsRegler


import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.RegelId
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.Regelflyt
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.Regelflyt.Companion.konklusjonUavklart
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.Regelflyt.Companion.regelflytJa
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.Regler
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.Ytelse
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.domene.Datagrunnlag
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.domene.InputPeriode
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.regler.RegelFactory


/*
* SP6300 Regler for opphold utenfor EØS. Relevant for Norske borgere og EØS borgere
* og 3 lands borgere med EØS familie
* */
class ReglerForOppholdUtenforEOS(
    val periode: InputPeriode,
    ytelse: Ytelse,
    regelFactory: RegelFactory,
) : Regler(ytelse, regelFactory) {

    override fun hentHovedflyt(): Regelflyt {

        val ErOppholdFerieOgAvsluttetForMerEnn30DagerSiden = lagRegelflyt(
            regel = hentRegel(RegelId.SP6321),
            hvisJa = regelflytJa(ytelse),
            hvisNei =konklusjonUavklart(ytelse,RegelId.OPPHOLDUTENFOREOSFLYT),
        )

        val ErOppholdetIUtlandetKortereEnn180Dager = lagRegelflyt(
            regel = hentRegel(RegelId.SP6314),
            hvisJa = regelflytJa(ytelse),
            hvisNei =konklusjonUavklart(ytelse,RegelId.OPPHOLDUTENFOREOSFLYT),
        )

        val bleOppholdetAvsluttetForMerEnn90DagerSiden = lagRegelflyt(
            regel = hentRegel(RegelId.SP6313),
            hvisJa = ErOppholdetIUtlandetKortereEnn180Dager,
            hvisNei =ErOppholdFerieOgAvsluttetForMerEnn30DagerSiden,
        )

        val ErDetBareEttUtenlandsOpphold = lagRegelflyt(
            regel = hentRegel(RegelId.SP6312),
            hvisJa = bleOppholdetAvsluttetForMerEnn90DagerSiden,
            hvisNei =konklusjonUavklart(ytelse,RegelId.OPPHOLDUTENFOREOSFLYT),
        )

        val harbrukerOppholdtSegUtenForEØS = lagRegelflyt(
            regel = hentRegel(RegelId.SP6311),
            hvisJa = ErDetBareEttUtenlandsOpphold,
            hvisNei =regelflytJa(ytelse, RegelId.SP6311),
        )
        val finnesBrukerSvarForOppholdUtenforEØS = lagRegelflyt(
            regel = hentRegel(RegelId.SP6301),
            hvisJa = harbrukerOppholdtSegUtenForEØS,
            hvisNei = konklusjonUavklart(ytelse,RegelId.OPPHOLDUTENFOREOSFLYT),
        )


        return finnesBrukerSvarForOppholdUtenforEØS
    }

    companion object {
        fun fraDatagrunnlag(datagrunnlag: Datagrunnlag): ReglerForOppholdUtenforEOS {

            return ReglerForOppholdUtenforEOS(
                periode = datagrunnlag.periode,
                ytelse = datagrunnlag.ytelse,
                regelFactory = RegelFactory(datagrunnlag),
            )
        }
    }
}
