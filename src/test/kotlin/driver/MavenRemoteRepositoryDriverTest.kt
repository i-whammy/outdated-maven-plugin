package driver

import domain.*
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import okhttp3.OkHttpClient
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.io.InputStream
import java.time.ZonedDateTime

class MavenRemoteRepositoryDriverTest {

    @Test
    fun `リモートサーバー上にあるアーティファクト候補を受け取って、サーバー上のアーティファクトとして返す`() {
        val client = mockk<OkHttpClient>()
        val mavenRemoteRepositoryDriver = spyk(MavenRemoteRepositoryDriver(client))

        val remoteRepository = RemoteRepository("central", "https://example.com/")
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
        every { response.isSuccessful } returns true
        every { response.body!!.byteStream() } returns inputStream
        assertEquals(
            listOf(LatestRemoteArtifact(remoteRepository, artifactCandidate, lastUpdated)),
            mavenRemoteRepositoryDriver.fetchLatestRemoteArtifacts(remoteArtifactCandidates, takeOutLastUpdated)
        )
    }

    @Test
    fun `生成されたパスが無効なものだった場合、そのアーティファクトの取得はスキップする`() {
        val client = mockk<OkHttpClient>()
        val mavenRemoteRepositoryDriver = spyk(MavenRemoteRepositoryDriver(client))

        val remoteRepository = RemoteRepository("central", "https://example.com/")
        val artifactCandidate = Artifact("groupId", "artifactId")
        val noExistingArtifactCandidate = Artifact("groupId", "NOT_FOUND")
        val inputStream = mockk<InputStream>()
        val response = mockk<okhttp3.Response>()
        val notFoundResponse = mockk<okhttp3.Response>()
        val lastUpdated = mockk<ZonedDateTime>()
        val takeOutLastUpdated = { _: InputStream -> lastUpdated }
        val remoteArtifactCandidates = listOf(
            RemoteArtifactCandidate(
                remoteRepository,
                artifactCandidate
            ),
            RemoteArtifactCandidate(
                remoteRepository,
                noExistingArtifactCandidate
            ),
        )
        every { mavenRemoteRepositoryDriver.executeGet("https://example.com/groupId/artifactId/maven-metadata.xml") } returns response
        every { mavenRemoteRepositoryDriver.executeGet("https://example.com/groupId/NOT_FOUND/maven-metadata.xml") } returns notFoundResponse
        every { response.isSuccessful } returns true
        every { notFoundResponse.isSuccessful } returns false
        every { response.body!!.byteStream() } returns inputStream
        assertEquals(
            listOf(LatestRemoteArtifact(remoteRepository, artifactCandidate, lastUpdated)),
            mavenRemoteRepositoryDriver.fetchLatestRemoteArtifacts(remoteArtifactCandidates, takeOutLastUpdated)
        )
    }

    @Test
    fun `リポジトリとアーティファクトの情報からmetadataのファイルパスを生成する`() {
        val candidate = RemoteArtifactCandidate(
            RemoteRepository("central", "https://repo1.maven.org/maven2/"),
            Artifact("groupId", "artifactId")
        )
        assertEquals(
            "https://repo1.maven.org/maven2/groupId/artifactId/maven-metadata.xml",
            candidate.toMetadataPathCandidate()
        )
    }

    @Test
    fun `アーティファクトのgroupId, artifactIdがドットつなぎの場合スラッシュに置換する`() {
        val candidate = RemoteArtifactCandidate(
            RemoteRepository("central", "https://repo1.maven.org/maven2/"),
            Artifact("org.jetbrains.kotlin", "kotlin.stdlib.jdk8")
        )
        assertEquals(
            "https://repo1.maven.org/maven2/org/jetbrains/kotlin/kotlin/stdlib/jdk8/maven-metadata.xml",
            candidate.toMetadataPathCandidate()
        )
    }
}