import io.mockk.*
import org.junit.jupiter.api.Test

class MavenOutdatedExecutorTest {

    @Test
    fun `依存情報を取得し、更新がされていないアーティファクトが存在すれば出力する`() {
        val artifactPort = mockk<ArtifactPort>()
        val mavenRepositoryPort = mockk<MavenRepositoryPort>()
        val outdatedArtifactOutputPort = mockk<OutdatedArtifactOutputPort>()
        val executor = MavenOutdatedExecutor(artifactPort, mavenRepositoryPort, outdatedArtifactOutputPort)
        val artifact1 = mockk<Artifact>()
        val artifact2 = mockk<Artifact>()
        val latestArtifact1 = mockk<LatestArtifact>()
        val latestArtifact2 = mockk<LatestArtifact>()
        val artifacts = listOf(artifact1, artifact2)
        val latestArtifacts = listOf(latestArtifact1, latestArtifact2)
        val outdatedArtifacts = listOf(latestArtifact1)

        val thresholdYear = 1L

        every { artifactPort.fetchArtifacts() } returns artifacts
        every { mavenRepositoryPort.fetchLatestArtifacts(artifacts) } returns latestArtifacts
        every { latestArtifact1.isOutdated(thresholdYear) } returns true
        every { latestArtifact2.isOutdated(thresholdYear) } returns false
        every { outdatedArtifactOutputPort.print(outdatedArtifacts) } just Runs

        executor.execute(thresholdYear)

        verify {
            artifactPort.fetchArtifacts()
            mavenRepositoryPort.fetchLatestArtifacts(artifacts)
            outdatedArtifactOutputPort.print(outdatedArtifacts)
        }
    }

    @Test
    fun `依存情報を取得し、更新がされていないアーティファクトが存在しなければ何もしない`() {

        val artifactPort = mockk<ArtifactPort>()
        val mavenRepositoryPort = mockk<MavenRepositoryPort>()
        val outdatedArtifactOutputPort = mockk<OutdatedArtifactOutputPort>()
        val executor = MavenOutdatedExecutor(artifactPort, mavenRepositoryPort, outdatedArtifactOutputPort)
        val artifact1 = mockk<Artifact>()
        val artifact2 = mockk<Artifact>()
        val latestArtifact1 = mockk<LatestArtifact>()
        val latestArtifact2 = mockk<LatestArtifact>()
        val artifacts = listOf(artifact1, artifact2)
        val latestArtifacts = listOf(latestArtifact1, latestArtifact2)
        val outdatedArtifacts = listOf(latestArtifact1)

        val thresholdYear = 1L

        every { artifactPort.fetchArtifacts() } returns artifacts
        every { mavenRepositoryPort.fetchLatestArtifacts(artifacts) } returns latestArtifacts
        every { latestArtifact1.isOutdated(thresholdYear) } returns false
        every { latestArtifact2.isOutdated(thresholdYear) } returns false
        every { outdatedArtifactOutputPort.print(outdatedArtifacts) } just Runs

        executor.execute(thresholdYear)

        verify {
            artifactPort.fetchArtifacts()
            mavenRepositoryPort.fetchLatestArtifacts(artifacts)
        }
        verify(exactly = 0) {
            outdatedArtifactOutputPort.print(outdatedArtifacts)
        }
    }
}