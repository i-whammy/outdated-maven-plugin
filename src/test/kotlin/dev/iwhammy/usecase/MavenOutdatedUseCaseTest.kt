package dev.iwhammy.usecase

import dev.iwhammy.domain.*
import dev.iwhammy.usecase.LatestArtifactOutputPort
import dev.iwhammy.usecase.MavenOutdatedUseCase
import dev.iwhammy.usecase.MavenRemoteRepositoryPort
import dev.iwhammy.usecase.OutdatedArtifactOutputPort
import io.mockk.*
import org.junit.jupiter.api.Test

class MavenOutdatedUseCaseTest {

    @Test
    fun `依存情報を取得し、更新がされていないアーティファクトが存在すれば出力する`() {
        val mavenRemoteRepositoryPort = mockk<MavenRemoteRepositoryPort>()
        val outdatedArtifactOutputPort = mockk<OutdatedArtifactOutputPort>()
        val latestArtifactOutputPort = mockk<LatestArtifactOutputPort>()
        val useCase = MavenOutdatedUseCase(mavenRemoteRepositoryPort, latestArtifactOutputPort, outdatedArtifactOutputPort)

        val artifact1 = Artifact("org.apache.maven", "maven-core")
        val artifact2 = Artifact("org.jetbrains.kotlin", "kotlin-stdlib-jdk8")
        val artifacts = listOf(artifact1, artifact2)
        val remoteRepository = RemoteRepository("central", "https://repo1.maven.org/maven2/")
        val remoteRepositories = listOf(remoteRepository)
        val remoteArtifactCandidate1 = RemoteArtifactCandidate(artifact1, remoteRepositories)
        val remoteArtifactCandidate2 = RemoteArtifactCandidate(artifact2, remoteRepositories)
        val latestRemoteArtifact1 = mockk<LatestRemoteArtifact>()
        val latestRemoteArtifact2 = mockk<LatestRemoteArtifact>()
        val result1 = Found(latestRemoteArtifact1)
        val result2 = Found(latestRemoteArtifact2)
        val outdatedArtifacts = listOf(latestRemoteArtifact1)

        val thresholdYear = 1L

        every { mavenRemoteRepositoryPort.fetchLatestRemoteArtifact(remoteArtifactCandidate1, takeOutLastUpdated()) } returns result1
        every { mavenRemoteRepositoryPort.fetchLatestRemoteArtifact(remoteArtifactCandidate2, takeOutLastUpdated()) } returns result2
        every { latestArtifactOutputPort.output(listOf(latestRemoteArtifact1, latestRemoteArtifact2)) } just Runs
        every { latestRemoteArtifact1.isOutdated(thresholdYear) } returns true
        every { latestRemoteArtifact2.isOutdated(thresholdYear) } returns false
        every { outdatedArtifactOutputPort.output(outdatedArtifacts) } just Runs

        useCase.verifyArtifacts(artifacts, remoteRepositories, thresholdYear)

        verify {
            mavenRemoteRepositoryPort.fetchLatestRemoteArtifact(remoteArtifactCandidate1, takeOutLastUpdated())
            mavenRemoteRepositoryPort.fetchLatestRemoteArtifact(remoteArtifactCandidate2, takeOutLastUpdated())
            latestArtifactOutputPort.output(listOf(latestRemoteArtifact1, latestRemoteArtifact2))
            outdatedArtifactOutputPort.output(outdatedArtifacts)
        }
    }

    @Test
    fun `依存情報を取得し、最新のアーティファクトを取得できたものだけ有効範囲内か確認する`() {
        val mavenRemoteRepositoryPort = mockk<MavenRemoteRepositoryPort>()
        val outdatedArtifactOutputPort = mockk<OutdatedArtifactOutputPort>()
        val latestArtifactOutputPort = mockk<LatestArtifactOutputPort>()
        val useCase = MavenOutdatedUseCase(mavenRemoteRepositoryPort, latestArtifactOutputPort, outdatedArtifactOutputPort)

        val artifact1 = Artifact("org.apache.maven", "maven-core")
        val artifact2 = Artifact("org.jetbrains.kotlin", "kotlin-stdlib-jdk8")
        val artifacts = listOf(artifact1, artifact2)
        val remoteRepository = RemoteRepository("central", "https://repo1.maven.org/maven2/")
        val remoteRepositories = listOf(remoteRepository)
        val remoteArtifactCandidate1 = RemoteArtifactCandidate(artifact1, remoteRepositories)
        val remoteArtifactCandidate2 = RemoteArtifactCandidate(artifact2, remoteRepositories)
        val latestRemoteArtifact = mockk<LatestRemoteArtifact>()
        val result1 = Found(latestRemoteArtifact)
        val result2 = NotFound(artifact2)
        val outdatedArtifacts = listOf(latestRemoteArtifact)

        val thresholdYear = 1L

        every { mavenRemoteRepositoryPort.fetchLatestRemoteArtifact(remoteArtifactCandidate1, takeOutLastUpdated()) } returns result1
        every { mavenRemoteRepositoryPort.fetchLatestRemoteArtifact(remoteArtifactCandidate2, takeOutLastUpdated()) } returns result2
        every { latestArtifactOutputPort.output(listOf(latestRemoteArtifact)) } just Runs
        every { latestRemoteArtifact.isOutdated(thresholdYear) } returns true
        every { outdatedArtifactOutputPort.output(outdatedArtifacts) } just Runs

        useCase.verifyArtifacts(artifacts, remoteRepositories, thresholdYear)

        verify {
            mavenRemoteRepositoryPort.fetchLatestRemoteArtifact(remoteArtifactCandidate1, takeOutLastUpdated())
            mavenRemoteRepositoryPort.fetchLatestRemoteArtifact(remoteArtifactCandidate2, takeOutLastUpdated())
            latestArtifactOutputPort.output(listOf(latestRemoteArtifact))
            outdatedArtifactOutputPort.output(outdatedArtifacts)
        }
    }

    @Test
    fun `依存情報を取得し、更新がされていないアーティファクトが存在しなければ何もしない`() {
        val mavenRemoteRepositoryPort = mockk<MavenRemoteRepositoryPort>()
        val outdatedArtifactOutputPort = mockk<OutdatedArtifactOutputPort>()
        val latestArtifactOutputPort = mockk<LatestArtifactOutputPort>()
        val useCase = MavenOutdatedUseCase(mavenRemoteRepositoryPort, latestArtifactOutputPort, outdatedArtifactOutputPort)

        val artifact = Artifact("org.apache.maven", "maven-core")
        val artifacts = listOf(artifact)
        val remoteRepository1 = RemoteRepository("central", "https://repo1.maven.org/maven2/")
        val remoteRepository2 = RemoteRepository("google-asia", "https://maven-central-asia.storage-download.googleapis.com/")
        val remoteRepositories = listOf(remoteRepository1, remoteRepository2)
        val remoteArtifactCandidate = RemoteArtifactCandidate(artifact, remoteRepositories)
        val latestRemoteArtifact = mockk<LatestRemoteArtifact>()
        val result = Found(latestRemoteArtifact)
        val outdatedArtifacts = emptyList<LatestRemoteArtifact>()

        val thresholdYear = 1L

        every { mavenRemoteRepositoryPort.fetchLatestRemoteArtifact(remoteArtifactCandidate, takeOutLastUpdated()) } returns result
        every { latestArtifactOutputPort.output(listOf(latestRemoteArtifact)) } just Runs
        every { latestRemoteArtifact.isOutdated(thresholdYear) } returns false

        useCase.verifyArtifacts(artifacts, remoteRepositories, thresholdYear)

        verify {
            mavenRemoteRepositoryPort.fetchLatestRemoteArtifact(remoteArtifactCandidate, takeOutLastUpdated())
            latestArtifactOutputPort.output(listOf(latestRemoteArtifact))
        }
        verify(exactly = 0) {
            outdatedArtifactOutputPort.output(outdatedArtifacts)
        }
    }
}