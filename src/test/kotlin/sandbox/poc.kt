package sandbox

import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.Svar
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.domene.Årsak

fun main(){

    val årsaker = listOf<Årsak>(
        Årsak("1","test",Svar.JA),
        Årsak("2","test",Svar.JA),
        Årsak("3","test",Svar.JA),
        Årsak("4","test",Svar.JA),
        )
    val toBeControlled:MutableList<Årsak> = mutableListOf()
    toBeControlled.addAll(årsaker)
    val checkedOut = toBeControlled.filter { it .regelId =="1" }
    toBeControlled.removeAll(checkedOut)
    println(toBeControlled.size)
}