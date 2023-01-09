package driver

import domain.*
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import okhttp3.OkHttpClient
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.io.InputStream
import java.time.ZonedDateTime

class MavenRemoteRepositoryDriverTest {

    @Test
    fun `リモートサーバー上にあるアーティファクト候補を受け取って、サーバー上のアーティファクトとして返す`() {
        val client = mockk<OkHttpClient>()
        val mavenRemoteRepositoryDriver = spyk(MavenRemoteRepositoryDriver(client))

        val remoteRepository = RemoteRepository("central", "https://example.com", "/")
        val artifactCandidate = Artifact("groupId", "artifactId")
        val inputStream = mockk<InputStream>()
        val response = mockk<okhttp3.Response>()
        val lastUpdated = mockk<ZonedDateTime>()
        val takeOutLastUpdated = { _: InputStream -> lastUpdated }
        val remoteArtifactCandidates = listOf(
                RemoteArtifactCandidate(
                    remoteRepository,
                    artifactCandidate
                )
        )
        every { mavenRemoteRepositoryDriver.executeGet("https://example.com/groupId/artifactId/maven-metadata.xml") } returns response
        every { response.body!!.byteStream() } returns inputStream
        assertEquals(
            listOf(LatestRemoteArtifact(remoteRepository, artifactCandidate, lastUpdated)),
            mavenRemoteRepositoryDriver.fetchLatestRemoteArtifacts(remoteArtifactCandidates, takeOutLastUpdated)
        )
    }
}