package functions

import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

fun toLocalDateTime(epochMilli: Long): LocalDateTime =
    LocalDateTime.ofInstant(Instant.ofEpochMilli(epochMilli), ZoneId.of("Z"))

fun toISOLocalFormattedDate(localDateTime: LocalDateTime): String = localDateTime.format(DateTimeFormatter.ISO_LOCAL_DATE)
