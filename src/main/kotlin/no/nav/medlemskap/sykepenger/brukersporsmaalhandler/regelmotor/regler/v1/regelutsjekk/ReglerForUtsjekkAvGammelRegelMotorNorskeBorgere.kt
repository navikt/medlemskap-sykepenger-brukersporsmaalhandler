package no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.regler.v1.regelutsjekk


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

class ReglerForUtsjekkAvGammelRegelMotorNorskeBorgere(
    val periode: InputPeriode,
    ytelse: Ytelse,
    regelFactory: RegelFactory,
) : Regler(ytelse, regelFactory) {

    override fun hentHovedflyt(): Regelflyt {


        val kanAlleRegelBruddSjekkesUtNorskeBorgere = lagRegelflyt(
            regel = hentRegel(RegelId.SP6500),
            hvisJa = regelflytJa(ytelse),
            hvisNei = regelflytUavklart(ytelse),
        )


        return kanAlleRegelBruddSjekkesUtNorskeBorgere
    }

    companion object {
        fun fraDatagrunnlag(datagrunnlag: Datagrunnlag,årsaker:List<Årsak>): ReglerForUtsjekkAvGammelRegelMotorNorskeBorgere {

            return ReglerForUtsjekkAvGammelRegelMotorNorskeBorgere(
                periode = datagrunnlag.periode,
                ytelse = datagrunnlag.ytelse,
                regelFactory = RegelFactory(datagrunnlag, årsaker = årsaker),
            )
        }
    }
}
