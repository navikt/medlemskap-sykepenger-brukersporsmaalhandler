package no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.regler.v1.oppholdsRegler


import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.BasisRegel
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.RegelId
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.Resultat
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.Resultat.Companion.ja
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.Resultat.Companion.nei
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.Ytelse
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.domene.Brukerinput
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.domene.Datagrunnlag
import java.time.LocalDate

class BleOppholdetAvsluttetForMerEnn90DagerSiden3LandsBorgerRegel(
    ytelse: Ytelse,
    startDatoForYtelse: LocalDate,
    private val brukerInput: Brukerinput?,

) : BasisRegel(RegelId.SP6413, ytelse) {

    override fun operasjon(): Resultat {

        if (brukerInput!!.oppholdUtenforNorge!!.oppholdUtenforNorge.isEmpty()){
            return nei(regelId)
        }
        val oppholdUtenforEØS = brukerInput!!.oppholdUtenforNorge!!.oppholdUtenforNorge.first()
        val oppholdSlutt = LocalDate.parse(oppholdUtenforEØS.perioder.first().tom)
        if (LocalDate.now().minusDays(90).isAfter(oppholdSlutt)){
            return ja(regelId)
        }
        return nei(regelId)

    }



    companion object {
        fun fraDatagrunnlag(datagrunnlag: Datagrunnlag): BleOppholdetAvsluttetForMerEnn90DagerSiden3LandsBorgerRegel {
            return BleOppholdetAvsluttetForMerEnn90DagerSiden3LandsBorgerRegel(
                ytelse = datagrunnlag.ytelse,
                startDatoForYtelse = datagrunnlag.periode.fom,
                brukerInput = datagrunnlag.brukerinput
            )
        }
    }
}
