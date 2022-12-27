class MavenOutdatedExecutor(
    private val artifactPort: ArtifactPort,
    private val outdatedDependencyPort: OutdatedDependencyPort,
    private val outdatedDependencyPrintPort: OutdatedDependencyPrintPort
) {

    fun execute() {
        val dependencies = artifactPort.fetchArtifacts()
        val outdatedDependencies = outdatedDependencyPort.filterOutdatedDependencies(dependencies)
        if (outdatedDependencies.isNotEmpty()) {
            outdatedDependencyPrintPort.print(outdatedDependencies)
        }
    }
}