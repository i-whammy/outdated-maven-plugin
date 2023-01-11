package driver

import domain.*
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.slf4j.LoggerFactory
import usecase.MavenRemoteRepositoryPort
import java.io.InputStream
import java.time.ZonedDateTime

class MavenRemoteRepositoryDriver(private val client: OkHttpClient) : MavenRemoteRepositoryPort {

    private val logger = LoggerFactory.getLogger(MavenRemoteRepositoryDriver::class.java)

    override fun fetchLatestRemoteArtifact(
        remoteArtifactCandidate: RemoteArtifactCandidate,
        takeOutLastUpdated: (i: InputStream) -> ZonedDateTime
    ): LatestRemoteArtifactResult {
        val artifact = remoteArtifactCandidate.artifact
        remoteArtifactCandidate.remoteRepositoryCandidates.map { remoteRepositoryCandidate ->
            val url = artifact.toMetadataPathCandidate(remoteRepositoryCandidate)
            val response = executeGet(url)
            if (response.isSuccessful) {
                logger.info("Fetching metadata from $url")
                return Found(LatestRemoteArtifact(remoteRepositoryCandidate, artifact, takeOutLastUpdated(response.body!!.byteStream())))
            }
            else {
                logger.info("not found $url")
            }
        }
        return NotFound(remoteArtifactCandidate)
    }

    fun executeGet(url: String): Response {
        val request = Request.Builder().url(url).build()
        return client.newCall(request).execute()
    }
}

fun Artifact.toMetadataPathCandidate(remoteRepository: RemoteRepository): MavenMetadataPath {
    return "${remoteRepository.url}/${
        this.groupId.replace(
            ".",
            "/"
        )
    }/${this.artifactId.replace(".", "/")}/maven-metadata.xml"
}

