package domain

import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

data class LatestRemoteArtifact(val remoteRepository: RemoteRepository, val artifact: Artifact, val lastUpdated: ZonedDateTime) {
    fun isOutdated(thresholdYear: Long): Boolean {
        val now = ZonedDateTime.now(ZoneId.of("Z"))
        return lastUpdated.isBefore(now.minusYears(thresholdYear))
    }

    fun toLoggingMessage(): String {
        return "${artifact.toId()} - The Last Release Date: ${lastUpdated.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))}"
    }
}