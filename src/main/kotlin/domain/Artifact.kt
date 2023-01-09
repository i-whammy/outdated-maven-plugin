package domain

import org.w3c.dom.Element
import java.io.InputStream
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import javax.xml.parsers.DocumentBuilderFactory

data class Artifact(val groupId: GroupId, val artifactId: ArtifactId)

data class RemoteRepository(val id: String, val url: String)

data class RemoteArtifactCandidate(val remoteRepository: RemoteRepository, val artifactCandidate: Artifact)

data class LatestRemoteArtifact(val remoteRepository: RemoteRepository, val artifact: Artifact, val lastUpdated: ZonedDateTime) {
    fun isOutdated(thresholdYear: Long): Boolean {
        val now = ZonedDateTime.now(ZoneId.of("Z"))
        return lastUpdated.isBefore(now.minusYears(thresholdYear))
    }
}

typealias MavenMetadataPath = String

fun takeOutLastUpdated(): (i: InputStream) -> ZonedDateTime {
    return {
        val documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(it)
        val versioning = documentBuilder.documentElement.getElementsByTagName("versioning").item(0) as Element
        val lastUpdatedValue = versioning.getElementsByTagName("lastUpdated").item(0).textContent
        ZonedDateTime.of(LocalDateTime.parse(lastUpdatedValue, DateTimeFormatter.ofPattern("yyyyMMddHHmmss")), ZoneId.of("Z"))
    }
}