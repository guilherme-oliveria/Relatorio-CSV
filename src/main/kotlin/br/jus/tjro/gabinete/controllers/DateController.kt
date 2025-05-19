package br.jus.tjro.gabinete.controllers

import org.springframework.format.annotation.DateTimeFormat.ISO.DATE_TIME
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime
import java.util.Optional.ofNullable
import org.springframework.format.annotation.DateTimeFormat as Format
import org.springframework.web.bind.annotation.PathVariable as Path


@RestController
@RequestMapping("date")
class DateController {

    @GetMapping("/isSync/{date}")
    fun isSync(@Format(iso = DATE_TIME) @Path("date") clientTime: LocalDateTime): ResponseEntity<Boolean> {
        val sync = isSynced(clientTime, LocalDateTime.now())
        return ResponseEntity.of(ofNullable(sync))
    }

    companion object {
        internal fun isSynced(clientTime: LocalDateTime, serverTime: LocalDateTime): Boolean {
            val before = serverTime.minusMinutes(15).minusSeconds(1)
            val after  = serverTime.plusMinutes(15).plusSeconds(1)
            return clientTime.isAfter(before) && clientTime.isBefore(after)
        }
    }

}
