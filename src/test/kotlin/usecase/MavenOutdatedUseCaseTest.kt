package usecase

import domain.*
import io.mockk.*
import org.junit.jupiter.api.Test

class MavenOutdatedUseCaseTest {

    @Test
    fun `依存情報を取得し、更新がされていないアーティファクトが存在すれば出力する`() {
        val mavenRemoteRepositoryPort = mockk<MavenRemoteRepositoryPort>()
        val outdatedArtifactOutputPort = mockk<OutdatedArtifactOutputPort>()
        val useCase = MavenOutdatedUseCase(mavenRemoteRepositoryPort, outdatedArtifactOutputPort)

        val artifact1 = Artifact("org.apache.maven", "maven-core")
        val artifact2 = Artifact("org.jetbrains.kotlin", "kotlin-stdlib-jdk8")
        val artifacts = listOf(artifact1, artifact2)
        val remoteRepository = RemoteRepository("central", "https://repo1.maven.org/maven2")
        val remoteRepositories = listOf(remoteRepository)
        val remoteArtifactCandidates = listOf(
            RemoteArtifactCandidate(remoteRepository, artifact1),
            RemoteArtifactCandidate(remoteRepository, artifact2),
        )
        val latestRemoteArtifact1 = mockk<LatestRemoteArtifact>()
        val latestRemoteArtifact2 = mockk<LatestRemoteArtifact>()
        val latestRemoteArtifacts = listOf(
            latestRemoteArtifact1,
            latestRemoteArtifact2
        )
        val outdatedArtifacts = listOf(
            latestRemoteArtifact1
        )

        val thresholdYear = 1L

        every { mavenRemoteRepositoryPort.fetchLatestRemoteArtifacts(remoteArtifactCandidates, takeOutLastUpdated()) } returns latestRemoteArtifacts
        every { latestRemoteArtifact1.isOutdated(thresholdYear) } returns true
        every { latestRemoteArtifact2.isOutdated(thresholdYear) } returns false
        every { outdatedArtifactOutputPort.output(outdatedArtifacts) } just Runs

        useCase.verifyArtifacts(artifacts, remoteRepositories, thresholdYear)

        verify {
            mavenRemoteRepositoryPort.fetchLatestRemoteArtifacts(remoteArtifactCandidates, takeOutLastUpdated())
            outdatedArtifactOutputPort.output(outdatedArtifacts)
        }
    }

    @Test
    fun `依存情報を取得し、更新がされていないアーティファクトが存在しなければ何もしない`() {

        val mavenRemoteRepositoryPort = mockk<MavenRemoteRepositoryPort>()
        val outdatedArtifactOutputPort = mockk<OutdatedArtifactOutputPort>()
        val useCase = MavenOutdatedUseCase(mavenRemoteRepositoryPort, outdatedArtifactOutputPort)

        val artifact1 = Artifact("org.apache.maven", "maven-core")
        val artifact2 = Artifact("org.jetbrains.kotlin", "kotlin-stdlib-jdk8")
        val artifacts = listOf(artifact1, artifact2)
        val remoteRepository = RemoteRepository("central", "https://repo1.maven.org/maven2")
        val remoteRepositories = listOf(remoteRepository)
        val remoteArtifactCandidates = listOf(
            RemoteArtifactCandidate(remoteRepository, artifact1),
            RemoteArtifactCandidate(remoteRepository, artifact2),
        )
        val latestRemoteArtifact1 = mockk<LatestRemoteArtifact>()
        val latestRemoteArtifact2 = mockk<LatestRemoteArtifact>()
        val latestRemoteArtifacts = listOf(
            latestRemoteArtifact1,
            latestRemoteArtifact2
        )
        val outdatedArtifacts = emptyList<LatestRemoteArtifact>()

        val thresholdYear = 1L

        every { mavenRemoteRepositoryPort.fetchLatestRemoteArtifacts(remoteArtifactCandidates, takeOutLastUpdated()) } returns latestRemoteArtifacts
        every { latestRemoteArtifact1.isOutdated(thresholdYear) } returns false
        every { latestRemoteArtifact2.isOutdated(thresholdYear) } returns false

        useCase.verifyArtifacts(artifacts, remoteRepositories, thresholdYear)

        verify {
            mavenRemoteRepositoryPort.fetchLatestRemoteArtifacts(remoteArtifactCandidates, takeOutLastUpdated())
        }
        verify(exactly = 0) {
            outdatedArtifactOutputPort.output(outdatedArtifacts)
        }
    }
}