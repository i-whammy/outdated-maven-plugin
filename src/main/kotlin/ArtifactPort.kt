import org.apache.maven.model.io.xpp3.MavenXpp3Reader
import java.io.File

interface ArtifactPort {
    fun fetchArtifacts(): List<Artifact>
}

class PomArtifactRepository: ArtifactPort {
    override fun fetchArtifacts(): List<Artifact> {
        return MavenXpp3Reader().read(File("/path/to/file").inputStream()).dependencies.map { Artifact(it.groupId, it.artifactId) }
    }
}

data class Artifact(val groupId: GroupId, val artifactId: ArtifactId)