package dev.crsi.manders.utils

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

class DateConverter {
    @RequiresApi(Build.VERSION_CODES.O)
    fun parseDate(iso8601Date: String): LocalDateTime {
        val instant = Instant.parse(iso8601Date)
        val zoneId = ZoneId.of("America/Bogota")
        return LocalDateTime.ofInstant(instant, zoneId)
    }
}