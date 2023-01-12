package domain

class MavenMetadataPath {
    companion object {
        fun of(remoteRepository: RemoteRepository, artifact: Artifact): String {
            return "${remoteRepository.normalizedUrl()}${artifact.groupId.replace(".", "/")}/${artifact.artifactId}/maven-metadata.xml"
        }
    }
}