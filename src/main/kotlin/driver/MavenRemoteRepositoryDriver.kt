package driver

import domain.*
import okhttp3.OkHttpClient
import okhttp3.Request
import usecase.MavenRemoteRepositoryPort
import java.io.InputStream
import java.time.ZonedDateTime

class MavenRemoteRepositoryDriver(private val client: OkHttpClient) : MavenRemoteRepositoryPort {

    override fun fetchLatestRemoteArtifacts(remoteArtifactCandidates: List<RemoteArtifactCandidate>, takeOutLastUpdatedFn: (i: InputStream) -> ZonedDateTime): List<LatestRemoteArtifact> {
        return remoteArtifactCandidates.map {
            val pathCandidate = it.toMetadataPathCandidate()
            val response = executeGet(pathCandidate)
            LatestRemoteArtifact(it.remoteRepository, it.artifactCandidate,
                takeOutLastUpdatedFn(response.body!!.byteStream()))
        }
    }

    fun executeGet(url: String): okhttp3.Response {
        val request = Request.Builder().url(url).build()
        return client.newCall(request).execute()
    }
}

fun RemoteArtifactCandidate.toMetadataPathCandidate(): MavenMetadataPath {
    return "${remoteRepository.url}${remoteRepository.baseDir}${artifactCandidate.groupId.replace(".", "/")}/${artifactCandidate.artifactId.replace(".", "/")}/maven-metadata.xml"
}
