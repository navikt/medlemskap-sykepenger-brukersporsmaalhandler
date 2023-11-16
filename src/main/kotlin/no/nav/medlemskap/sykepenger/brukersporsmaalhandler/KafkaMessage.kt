package no.nav.medlemskap.sykepenger.brukersporsmaalhandler

import java.time.LocalDateTime


data class VurderingMessageRecord(val partition:Int, val offset:Long, val value : String, val key:String?, val topic:String, val timestamp: LocalDateTime, val timestampType:String)

