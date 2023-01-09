package usecase

import domain.Artifact
import domain.RemoteArtifactCandidate
import domain.RemoteRepository
import domain.takeOutLastUpdated

class MavenOutdatedUseCase(
    private val mavenRemoteRepositoryPort: MavenRemoteRepositoryPort,
    private val outdatedArtifactOutputPort: OutdatedArtifactOutputPort
) {

    fun verifyArtifacts(artifacts: List<Artifact>, remoteRepositories: List<RemoteRepository>, thresholdYear: Long) {
        val remoteArtifactCandidates =
            remoteRepositories.map { repo -> artifacts.map { artifact -> RemoteArtifactCandidate(repo, artifact) } }
                .flatten()
        val remoteArtifacts = mavenRemoteRepositoryPort.fetchLatestRemoteArtifacts(remoteArtifactCandidates, takeOutLastUpdated())
        remoteArtifacts
            .filter { it.isOutdated(thresholdYear) }
            .takeIf { it.isNotEmpty() }
            ?.let { outdatedArtifactOutputPort.output(it) }
    }
}