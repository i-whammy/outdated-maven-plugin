import org.apache.maven.model.io.xpp3.MavenXpp3Reader
import java.io.File

interface ArtifactPort {
    fun fetchArtifacts(): List<Artifact>
}

class PomArtifactRepository: ArtifactPort {
    override fun fetchArtifacts(): List<Artifact> {
        val basePomPath = "${System.getProperty("user.dir")}/pom.xml"
        return MavenXpp3Reader().read(File(basePomPath).inputStream()).dependencies.map { Artifact(it.groupId, it.artifactId) }
    }
}

data class Artifact(val groupId: GroupId, val artifactId: ArtifactId)