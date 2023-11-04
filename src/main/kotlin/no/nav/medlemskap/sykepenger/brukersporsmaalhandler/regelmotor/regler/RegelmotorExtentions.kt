package no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.regler

import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.RegelId
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.Resultat
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.Svar

fun Resultat.arbeiderItoLand(): Svar? {
   return  this.finnRegelResultat(RegelId.ARBEID_I_TO_LAND)?.svar
}

fun Resultat.oppholderSegIEØS(): Svar? {
    return  this.finnRegelResultat(RegelId.SP6002)?.svar
}