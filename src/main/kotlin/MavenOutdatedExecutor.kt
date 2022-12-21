class MavenOutdatedExecutor {
    fun execute() {
        val artifactPort: ArtifactPort = PomArtifactRepository()
        val outdatedDependencyPort: OutdatedDependencyPort = OutputDependencyRepository()
        val outdatedDependencyPrintPort: OutdatedDependencyPrintPort = OutputDependencyPrintDriver()

        val dependencies = artifactPort.fetchArtifacts()
        val outdatedDependencies = outdatedDependencyPort.filterOutdatedDependencies(dependencies)
        if (outdatedDependencies.isNotEmpty()) {
            outdatedDependencyPrintPort.print(outdatedDependencies)
        }
    }
}