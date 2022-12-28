class MavenOutdatedExecutor(
    private val artifactPort: ArtifactPort,
    private val mavenRepositoryPort: MavenRepositoryPort,
    private val outdatedArtifactOutputPort: OutdatedArtifactOutputPort
) {

    fun execute(thresholdYear: Long) {
        artifactPort.fetchArtifacts()
            .let { mavenRepositoryPort.fetchLatestArtifacts(it) }
            .filter { it.isOutdated(thresholdYear) }
            .takeIf { it.isNotEmpty() }
            ?.let { outdatedArtifactOutputPort.print(it) }
    }
}