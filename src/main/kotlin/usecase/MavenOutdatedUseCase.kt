package usecase

import domain.*
import org.slf4j.LoggerFactory

class MavenOutdatedUseCase(
    private val mavenRemoteRepositoryPort: MavenRemoteRepositoryPort,
    private val outdatedArtifactOutputPort: OutdatedArtifactOutputPort
) {

    private val logger = LoggerFactory.getLogger(MavenOutdatedUseCase::class.java)

    fun verifyArtifacts(artifacts: List<Artifact>, remoteRepositories: List<RemoteRepository>, thresholdYear: Long) {
        val latestRemoteArtifacts = artifacts.map { RemoteArtifactCandidate(it, remoteRepositories) }
            .map { mavenRemoteRepositoryPort.fetchLatestRemoteArtifact(it, takeOutLastUpdated()) }
        val list = mutableListOf<LatestRemoteArtifact>()
        latestRemoteArtifacts.forEach {
            when(it) {
                is Found -> list.add(it.latestRemoteArtifact)
                is NotFound -> logger.warn("No metadata found for artifact ${it.remoteArtifactCandidate.artifact.toId()}")
            }
        }
        list.filter { it.isOutdated(thresholdYear) }
            .takeIf { it.isNotEmpty() }
            ?.let { outdatedArtifactOutputPort.output(it) }
    }
}