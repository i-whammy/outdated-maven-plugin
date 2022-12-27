class MavenOutdatedExecutor {

    private val artifactPort: ArtifactPort = PomArtifactRepository()
    private val outdatedDependencyPort: OutdatedDependencyPort = OutputDependencyRepository()
    private val outdatedDependencyPrintPort: OutdatedDependencyPrintPort = OutputDependencyPrintDriver()

    fun execute() {
        val dependencies = artifactPort.fetchArtifacts()
        val outdatedDependencies = outdatedDependencyPort.filterOutdatedDependencies(dependencies)
        if (outdatedDependencies.isNotEmpty()) {
            outdatedDependencyPrintPort.print(outdatedDependencies)
        }
    }
}