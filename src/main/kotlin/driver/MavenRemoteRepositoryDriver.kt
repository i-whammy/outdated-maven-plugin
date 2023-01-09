package driver

import domain.*
import okhttp3.OkHttpClient
import okhttp3.Request
import org.slf4j.LoggerFactory
import usecase.MavenRemoteRepositoryPort
import java.io.InputStream
import java.lang.Exception
import java.time.ZonedDateTime

class MavenRemoteRepositoryDriver(private val client: OkHttpClient) : MavenRemoteRepositoryPort {

    private val logger = LoggerFactory.getLogger(MavenRemoteRepositoryDriver::class.java)

    override fun fetchLatestRemoteArtifacts(
        remoteArtifactCandidates: List<RemoteArtifactCandidate>,
        takeOutLastUpdatedFn: (i: InputStream) -> ZonedDateTime
    ): List<LatestRemoteArtifact> {
        val latestRemoteArtifacts = mutableListOf<LatestRemoteArtifact>()
        remoteArtifactCandidates.map { candidate ->
            val pathCandidate = candidate.toMetadataPathCandidate()
            val response = executeGet(pathCandidate)
            if (response.isSuccessful) {
                logger.info("Fetching metadata from ${candidate.toMetadataPathCandidate()}")
                latestRemoteArtifacts.add(
                    LatestRemoteArtifact(
                        candidate.remoteRepository, candidate.artifactCandidate,
                        takeOutLastUpdatedFn(response.body!!.byteStream())
                    )
                )
            } else {
                logger.info("Metadata not found. ${candidate.toMetadataPathCandidate()}")
            }
        }
        return latestRemoteArtifacts
    }

    fun executeGet(url: String): okhttp3.Response {
        val request = Request.Builder().url(url).build()
        return client.newCall(request).execute()
    }
}

fun RemoteArtifactCandidate.toMetadataPathCandidate(): MavenMetadataPath {
    return "${remoteRepository.url}${
        artifactCandidate.groupId.replace(
            ".",
            "/"
        )
    }/${artifactCandidate.artifactId.replace(".", "/")}/maven-metadata.xml"
}
