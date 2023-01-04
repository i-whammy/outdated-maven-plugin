package driver

import domain.Artifact
import org.apache.maven.model.io.xpp3.MavenXpp3Reader
import usecase.ArtifactPort
import java.io.File
import java.io.FileNotFoundException

class PomArtifactDriver: ArtifactPort {

    private val mavenReader = MavenXpp3Reader()

    override fun fetchArtifacts(): List<Artifact> {
        val basePomPath = "${System.getProperty("user.dir")}/pom.xml"
        val stream = (File(basePomPath)
            .takeIf { it.canRead() }?.inputStream()
            ?: throw FileNotFoundException("pom.xml not found."))
        return stream.use {
            mavenReader.read(stream).dependencies.map { Artifact(it.groupId, it.artifactId) }
        }
    }
}
