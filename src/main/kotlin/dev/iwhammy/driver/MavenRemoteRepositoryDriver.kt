package dev.iwhammy.driver

import dev.iwhammy.domain.*
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.slf4j.LoggerFactory
import dev.iwhammy.usecase.port.MavenRemoteRepositoryPort
import java.io.InputStream
import java.time.ZonedDateTime

class MavenRemoteRepositoryDriver(private val client: OkHttpClient) : MavenRemoteRepositoryPort {

    private val logger = LoggerFactory.getLogger(MavenRemoteRepositoryDriver::class.java)

    override fun fetchLatestRemoteArtifact(
        remoteArtifactCandidate: RemoteArtifactCandidate,
        takeOutLastUpdated: (i: InputStream) -> ZonedDateTime
    ): LatestRemoteArtifactResult {
        val (artifact, repos) = remoteArtifactCandidate
        repos.map { repo ->
            val url = MavenMetadataPath.of(repo, artifact)
            val response = executeGet(url)
            response.use {
                if (response.isSuccessful) {
                    logger.info("Fetching metadata from $url")
                    response.body!!.use {
                        return Found(LatestRemoteArtifact(repo, artifact, takeOutLastUpdated(it.byteStream())))
                    }
                }
                else {
                    logger.info("Not found $url")
                }
            }
        }
        return NotFound(artifact)
    }

    fun executeGet(url: String): Response {
        val request = Request.Builder().url(url).build()
        return client.newCall(request).execute()
    }
}