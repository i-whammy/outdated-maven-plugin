import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

fun main(args: Array<String>) {

    val artifactPort: ArtifactPort = PomArtifactRepository()
    val outdatedDependencyPort: OutdatedDependencyPort = OutputDependencyRepository()
    val outdatedDependencyPrintPort: OutdatedDependencyPrintPort = OutputDependencyPrintDriver()

    val dependencies = artifactPort.fetchArtifacts()
    val outdatedDependencies = outdatedDependencyPort.filterOutdatedDependencies(dependencies)
    if (outdatedDependencies.isNotEmpty()) {
        outdatedDependencyPrintPort.print(outdatedDependencies)
    }
}

fun toLocalDateTime(epochMilli: Long): LocalDateTime =
    LocalDateTime.ofInstant(Instant.ofEpochMilli(epochMilli), ZoneId.of("Z"))

fun toFormattedDate(localDateTime: LocalDateTime): String = localDateTime.format(DateTimeFormatter.ISO_LOCAL_DATE)

typealias GroupId = String
typealias ArtifactId = String
typealias RequestUrl = String

class TimestampComparer(private val thresholdYear: Long) {
    fun isOutDated(epochMilliSeconds: Long): Boolean {
        val zoneId = ZoneId.of("Z")
        return LocalDateTime.ofInstant(Instant.now(), zoneId).minusYears(thresholdYear) >= LocalDateTime.ofInstant(Instant.ofEpochMilli(epochMilliSeconds), zoneId)
    }
}