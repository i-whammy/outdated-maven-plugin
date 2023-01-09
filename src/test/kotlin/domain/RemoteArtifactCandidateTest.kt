package domain

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class RemoteArtifactCandidateTest {
    @Test
    fun `リポジトリとアーティファクトの情報からmetadataのファイルパスを生成する`() {
        val candidate = RemoteArtifactCandidate(
            RemoteRepository("central", "https://repo1.maven.org/maven2", "/"),
            Artifact("groupId", "artifactId")
        )
        assertEquals(
            "https://repo1.maven.org/maven2/groupId/artifactId/maven-metadata.xml",
            candidate.toMetadataPathCandidate()
        )
    }

    @Test
    fun `アーティファクトのgroupId, artifactIdがドットつなぎの場合スラッシュに置換する`() {
        val candidate = RemoteArtifactCandidate(
            RemoteRepository("central", "https://repo1.maven.org/maven2", "/"),
            Artifact("org.jetbrains.kotlin", "kotlin.stdlib.jdk8")
        )
        assertEquals(
            "https://repo1.maven.org/maven2/org/jetbrains/kotlin/kotlin/stdlib/jdk8/maven-metadata.xml",
            candidate.toMetadataPathCandidate()
        )
    }
}