package no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.domene.extensionfunctions

import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.domene.*



fun List<Arbeidsforhold>.sistePermisjonIkontrollPerioden(inputPeriode: InputPeriode): PermisjonPermittering? {
    var permisjonPermitteringListe = mutableListOf<PermisjonPermittering>()
    val tmp = this.filter { it.permisjonPermittering != null }
        .flatMap { it.permisjonPermittering!! }
        .filter { it.type != PermisjonPermitteringType.PERMITTERING }
        .filter{it.periode.overlapper(inputPeriode)}
        .sortedByDescending { it.periode.fom!! }
        .firstOrNull()
    return tmp
}

fun PeriodeMedNullVerdier.overlapper(inputPeriode: InputPeriode): Boolean {
    val start = this.fom ?: return false // Hvis start er null, ingen overlapping
    val end = this.tom // Kan være null

    return start in inputPeriode.fom..inputPeriode.tom ||
            (start.isBefore(inputPeriode.fom) && (end == null || end.isAfter(inputPeriode.fom)))
}


