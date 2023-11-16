package no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor

import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.domene.Kjøring
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.regler.Hovedregler

object ReglerService {

    fun kjørRegler(kjøring: Kjøring): Resultat {
        return Hovedregler(kjøring).kjørHovedregler()
    }
}
