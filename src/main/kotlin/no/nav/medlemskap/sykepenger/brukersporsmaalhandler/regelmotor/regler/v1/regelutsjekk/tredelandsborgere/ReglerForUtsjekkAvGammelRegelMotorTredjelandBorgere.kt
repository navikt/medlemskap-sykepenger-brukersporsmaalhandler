package no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.regler.v1.regelutsjekk.tredelandsborgere


import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.RegelId
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.Regelflyt
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.Regelflyt.Companion.regelflytJa
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.Regelflyt.Companion.regelflytUavklart
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.Regler
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.Ytelse
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.domene.Datagrunnlag
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.domene.InputPeriode
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.domene.Årsak
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.regler.RegelFactory

class ReglerForUtsjekkAvGammelRegelMotorTredjelandBorgere(
    val periode: InputPeriode,
    ytelse: Ytelse,
    regelFactory: RegelFactory,
) : Regler(ytelse, regelFactory) {

    override fun hentHovedflyt(): Regelflyt {


        val kanAlleRegelBruddSjekkesUtEOSBorgere = lagRegelflyt(
            regel = hentRegel(RegelId.SP6700),
            hvisJa = regelflytJa(ytelse, RegelId.REGEL_UTSJEKK),
            hvisNei = regelflytUavklart(ytelse,RegelId.REGEL_UTSJEKK),
        )


        return kanAlleRegelBruddSjekkesUtEOSBorgere
    }

    companion object {
        fun fraDatagrunnlag(datagrunnlag: Datagrunnlag,årsaker:List<Årsak>): ReglerForUtsjekkAvGammelRegelMotorTredjelandBorgere {

            return ReglerForUtsjekkAvGammelRegelMotorTredjelandBorgere(
                periode = datagrunnlag.periode,
                ytelse = datagrunnlag.ytelse,
                regelFactory = RegelFactory(datagrunnlag, årsaker = årsaker),
            )
        }
    }
}
