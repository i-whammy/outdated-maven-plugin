package dev.iwhammy.driver

import dev.iwhammy.domain.*
import dev.iwhammy.driver.MavenRemoteRepositoryDriver
import io.mockk.*
import okhttp3.OkHttpClient
import okhttp3.Response
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.io.InputStream
import java.time.ZonedDateTime

class MavenRemoteRepositoryDriverTest {

    @Test
    fun `リモートサーバー上にあるアーティファクト候補を受け取って、有効なレスポンスの場合のみサーバー上のアーティファクトとして返す`() {
        val client = mockk<OkHttpClient>()
        val mavenRemoteRepositoryDriver = spyk(MavenRemoteRepositoryDriver(client))

        val remoteRepository1 = RemoteRepository("not-found-repository", "https://example.com/1")
        val remoteRepository2 = RemoteRepository("central", "https://example.com/2")
        val remoteRepository3 = RemoteRepository("not-found-repository", "https://example.com/3")
        val artifact = Artifact("groupId", "artifactId")
        val inputStream = mockk<InputStream>()
        val response1 = mockk<Response>()
        val response2 = mockk<Response>()
        val lastUpdated = mockk<ZonedDateTime>()
        val takeOutLastUpdated = { _: InputStream -> lastUpdated }
        val remoteArtifactCandidate = RemoteArtifactCandidate(artifact, listOf(remoteRepository1, remoteRepository2, remoteRepository3))
        every { mavenRemoteRepositoryDriver.executeGet("https://example.com/1/groupId/artifactId/maven-metadata.xml") } returns response1
        every { mavenRemoteRepositoryDriver.executeGet("https://example.com/2/groupId/artifactId/maven-metadata.xml") } returns response2
        every { response1.isSuccessful } returns false
        every { response2.isSuccessful } returns true
        every { response2.body!!.byteStream() } returns inputStream
        every { response2.body!!.close() } just Runs
        every { response1.close() } just Runs
        every { response2.close() } just Runs
        assertEquals(
            Found(LatestRemoteArtifact(remoteRepository2, artifact, lastUpdated)),
            mavenRemoteRepositoryDriver.fetchLatestRemoteArtifact(remoteArtifactCandidate, takeOutLastUpdated)
        )
    }

    @Test
    fun `渡された候補の中にリモートリポジトリ上で存在するものがない場合、見つからなかったということを伝える`() {
        val client = mockk<OkHttpClient>()
        val mavenRemoteRepositoryDriver = spyk(MavenRemoteRepositoryDriver(client))

        val remoteRepository = RemoteRepository("central", "https://example.com")
        val artifactCandidate = Artifact("groupId", "artifactId")
        val response = mockk<Response>()
        val lastUpdated = mockk<ZonedDateTime>()
        val takeOutLastUpdated = { _: InputStream -> lastUpdated }
        val remoteArtifactCandidate = RemoteArtifactCandidate(artifactCandidate, listOf(remoteRepository))
        every { mavenRemoteRepositoryDriver.executeGet("https://example.com/groupId/artifactId/maven-metadata.xml") } returns response
        every { response.isSuccessful } returns false
        every { response.close() } just Runs
        assertEquals(
            NotFound(artifactCandidate),
            mavenRemoteRepositoryDriver.fetchLatestRemoteArtifact(remoteArtifactCandidate, takeOutLastUpdated)
        )
    }
}