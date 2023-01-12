package usecase

import domain.*
import org.slf4j.LoggerFactory

class MavenOutdatedUseCase(
    private val mavenRemoteRepositoryPort: MavenRemoteRepositoryPort,
    private val latestArtifactOutputPort: LatestArtifactOutputPort,
    private val outdatedArtifactOutputPort: OutdatedArtifactOutputPort,
) {

    private val logger = LoggerFactory.getLogger(MavenOutdatedUseCase::class.java)

    fun verifyArtifacts(artifacts: List<Artifact>, remoteRepositories: List<RemoteRepository>, thresholdYear: Long) {
        val fetchResults = artifacts.map { RemoteArtifactCandidate(it, remoteRepositories) }
            .map { mavenRemoteRepositoryPort.fetchLatestRemoteArtifact(it, takeOutLastUpdated()) }
        val latestRemoteArtifacts = resolveFetchResults(fetchResults)
        latestArtifactOutputPort.output(latestRemoteArtifacts)
        latestRemoteArtifacts
            .filter { it.isOutdated(thresholdYear) }
            .takeIf { it.isNotEmpty() }
            ?.let { outdatedArtifactOutputPort.output(it) }
    }

    private fun resolveFetchResults(results: List<LatestRemoteArtifactResult>): List<LatestRemoteArtifact> {
        val latestRemoteArtifacts = mutableListOf<LatestRemoteArtifact>()
        results.forEach {
            when(it) {
                is Found -> latestRemoteArtifacts.add(it.latestRemoteArtifact)
                is NotFound -> logger.warn("No metadata found for artifact ${it.notFoundArtifact.toId()}")
            }
        }
        return latestRemoteArtifacts
    }
}