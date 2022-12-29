import org.apache.maven.model.Dependency
import org.apache.maven.model.io.xpp3.MavenXpp3Reader
import java.io.File
import java.io.FileNotFoundException
import java.io.InputStream

interface ArtifactPort {
    fun fetchArtifacts(): List<Artifact>
}

class PomArtifactRepository: ArtifactPort {

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

data class Artifact(val groupId: GroupId, val artifactId: ArtifactId)