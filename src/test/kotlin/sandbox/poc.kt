package sandbox

import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.Svar
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.domene.Datagrunnlag
import no.nav.medlemskap.sykepenger.brukersporsmaalhandler.regelmotor.domene.Årsak
import java.io.File

fun main(){
    val a = Datagrunnlag::class.java.classLoader.getResource("a.txt").readText(Charsets.UTF_8)
    val regel19_1 = Datagrunnlag::class.java.classLoader.getResource("19_1.txt").readText(Charsets.UTF_8)
    val r19_1_list  = mutableListOf<String>()
    val a_list  = mutableListOf<String>()
    val errors = mutableListOf<String>()
    val other = mutableListOf<String>()
    a.split("\n").forEach { a_list.add(it)}
    regel19_1.split("\n").forEach { r19_1_list.add(it)}
    r19_1_list.forEach {
        if (a_list.contains(it)){
            errors.add(it)
        }
        else{
            other.add(it)
        }

    }
    println("antall error vs ikke errors: ${errors.size} av ${a_list.size}")
    println("Errors:")
    errors.forEach { println(it) }
    println("others:")
    other.forEach { println(it) }


}
