import functions.toISOLocalFormattedDate
import functions.toLocalDateTime

data class LatestArtifact (private val groupId: GroupId, private val artifactId: ArtifactId, private val lastReleaseTimestamp: Long) {

    fun id() = "$groupId:$artifactId"

    fun lastReleasedLocalDateTime() = this.lastReleaseTimestamp.let(::toLocalDateTime).let(::toISOLocalFormattedDate)

    fun isOutdated(thresholdYear: Long): Boolean {
        return TimestampComparer(thresholdYear).isOutDated(this.lastReleaseTimestamp)
    }
}
