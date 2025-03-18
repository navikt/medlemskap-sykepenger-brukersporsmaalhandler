package no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.domene.extensionfunctions

import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.domene.Datagrunnlag
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.domene.InputPeriode

fun Datagrunnlag.siste_permisjonPermitteringPeriode(kontrollperiode: InputPeriode):String{
    val permisjon =this.arbeidsforhold.sistePermisjonIkontrollPerioden(kontrollperiode)
    if (permisjon!=null){
        return "${permisjon.periode.fom} - ${permisjon.periode.tom}"
    }
    else{
        return "NA"
    }

}
fun Datagrunnlag.siste_permisjonPermitteringType(kontrollperiode: InputPeriode):String{
    val permisjon =this.arbeidsforhold.sistePermisjonIkontrollPerioden(kontrollperiode)
    if (permisjon!=null){
        return permisjon.type.name
    }
    else{
        return "NA"
    }
}
fun Datagrunnlag.siste_permisjonPermitteringProsent(kontrollperiode: InputPeriode): Double?{
    val permisjon =this.arbeidsforhold.sistePermisjonIkontrollPerioden(kontrollperiode)
    if (permisjon!=null){
        return permisjon.prosent
    }
    else{
        return null
    }
}
