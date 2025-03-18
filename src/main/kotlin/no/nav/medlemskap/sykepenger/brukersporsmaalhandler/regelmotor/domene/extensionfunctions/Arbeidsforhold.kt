package no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.domene.extensionfunctions

import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.domene.*

fun Arbeidsforhold.sistePermisjonIkontrollPerioden(inputPeriode: InputPeriode): PermisjonPermittering?{
    return null
}

fun List<Arbeidsforhold>.sistePermisjonIkontrollPerioden(inputPeriode: InputPeriode): PermisjonPermittering? {
    var permisjonPermitteringListe = mutableListOf<PermisjonPermittering>()
    val tmp = this.filter { it.permisjonPermittering != null }
        .flatMap { it.permisjonPermittering!! }
        .filter { it.type != PermisjonPermitteringType.PERMITTERING }
        .filter { it.periode.fom!!.isAfter(inputPeriode.fom) && it.periode.fom.isBefore(inputPeriode.tom) }
        .sortedBy { it.periode.fom!! }
        .firstOrNull()
    return tmp
}

fun PeriodeMedNullVerdier.overlapper(inputPeriode: InputPeriode): Boolean {
    return false
    //return it.periode.fom!!.isAfter(inputPeriode.fom) && it.periode.fom.isBefore(inputPeriode.tom)
}


// Finn alle PermisjonsPermitteringer på tvers av alle arbeidsforhold
// Ta vare på de innenfor kontrollperioden
// Returner siste PermisjonsPermittering

