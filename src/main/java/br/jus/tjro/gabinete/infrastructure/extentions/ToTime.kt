package br.jus.tjro.gabinete.infrastructure.extentions

import java.time.LocalTime

fun String.toTime(): LocalTime? {
    val time = this.split(":")
    return LocalTime.of(time[0].toInt(), (time.getOrElse(1) { "0" }).toInt())
}
