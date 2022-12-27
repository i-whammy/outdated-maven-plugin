import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

class TimestampComparer(private val thresholdYear: Long) {
    fun isOutDated(epochMilliSeconds: Long): Boolean {
        val zoneId = ZoneId.of("Z")
        return LocalDateTime.ofInstant(Instant.now(), zoneId).minusYears(thresholdYear) >= LocalDateTime.ofInstant(
            Instant.ofEpochMilli(epochMilliSeconds), zoneId
        )
    }
}