package domain

import org.w3c.dom.Element
import java.io.InputStream
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import javax.xml.parsers.DocumentBuilderFactory

data class Artifact(val groupId: String, val artifactId: String) {
    fun toId(): String = "$groupId:$artifactId"
}

sealed class LatestRemoteArtifactResult
data class Found(val latestRemoteArtifact: LatestRemoteArtifact): LatestRemoteArtifactResult()
data class NotFound(val notFoundArtifact: Artifact): LatestRemoteArtifactResult()

fun takeOutLastUpdated(): (i: InputStream) -> ZonedDateTime {
    return {
        val documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(it)
        val versioning = documentBuilder.documentElement.getElementsByTagName("versioning").item(0) as Element
        val lastUpdatedValue = versioning.getElementsByTagName("lastUpdated").item(0).textContent
        ZonedDateTime.of(LocalDateTime.parse(lastUpdatedValue, DateTimeFormatter.ofPattern("yyyyMMddHHmmss")), ZoneId.of("Z"))
    }
}