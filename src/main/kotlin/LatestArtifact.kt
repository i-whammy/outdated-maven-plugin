import functions.toISOLocalFormattedDate
import functions.toLocalDateTime
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

data class LatestArtifact (private val groupId: GroupId, private val artifactId: ArtifactId, private val lastReleaseTimestamp: Long) {

    fun id() = "$groupId:$artifactId"

    fun lastReleasedLocalDateTime() = this.lastReleaseTimestamp.let(::toLocalDateTime).let(::toISOLocalFormattedDate)

    fun isOutdated(thresholdYear: Long): Boolean {
        val zoneId = ZoneId.of("Z")
        return LocalDateTime.now().minusYears(thresholdYear) >=
                LocalDateTime.ofInstant(Instant.ofEpochMilli(lastReleaseTimestamp), zoneId)
    }
}
