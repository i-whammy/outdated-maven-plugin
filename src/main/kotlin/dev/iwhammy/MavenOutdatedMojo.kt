package dev.iwhammy

import dev.iwhammy.domain.Artifact
import dev.iwhammy.domain.RemoteRepository
import org.apache.maven.plugin.AbstractMojo
import org.apache.maven.plugins.annotations.Mojo
import org.apache.maven.plugins.annotations.Parameter
import org.apache.maven.project.MavenProject

@Mojo(name = "outdated")
class MavenOutdatedMojo: AbstractMojo() {

    @Parameter(property = "project")
    private lateinit var project: MavenProject
    override fun execute() {
        val artifacts = project.dependencies.map { Artifact(it.groupId, it.artifactId) }
        val remoteRepositories = project.remoteArtifactRepositories.map { RemoteRepository(it.id, it.url) }
        val thresholdYear = 1L
        val useCase by DependencyProvider().mavenOutdatedUseCase()
        useCase.verifyArtifacts(artifacts, remoteRepositories, thresholdYear)
    }
}