package dev.iwhammy.domain

import dev.iwhammy.domain.Artifact
import dev.iwhammy.domain.MavenMetadataPath
import dev.iwhammy.domain.RemoteRepository
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class MavenMetadataPathTest {

    @Test
    fun `リポジトリとアーティファクトの情報からmetadataのファイルパスを生成する`() {
        Assertions.assertEquals(
            "https://repo1.maven.org/maven2/groupId/artifactId/maven-metadata.xml",
            MavenMetadataPath.of(RemoteRepository("central", "https://repo1.maven.org/maven2/"), Artifact("groupId", "artifactId"))
        )
    }

    @Test
    fun `アーティファクトのgroupIdがドットつなぎの場合スラッシュに置換する`() {
        Assertions.assertEquals(
            "https://repo1.maven.org/maven2/org/groupId/artifactId/maven-metadata.xml",
            MavenMetadataPath.of(RemoteRepository("central", "https://repo1.maven.org/maven2/"), Artifact("org.groupId", "artifactId"))
        )
    }

    @Test
    fun `アーティファクトのartifactIdはドットつなぎでもスラッシュに置換せずそのまま利用する`() {
        Assertions.assertEquals(
            "https://repo1.maven.org/maven2/groupId/kotlin-stdlib-jdk8/maven-metadata.xml",
            MavenMetadataPath.of(RemoteRepository("central", "https://repo1.maven.org/maven2/"), Artifact("groupId", "kotlin-stdlib-jdk8"))
        )
    }

    @Test
    fun `アーティファクトのURLの末尾にスラッシュがない場合は補って利用する`() {
        Assertions.assertEquals(
            "https://repo1.maven.org/maven2/groupId/artifactId/maven-metadata.xml",
            MavenMetadataPath.of(RemoteRepository("central", "https://repo1.maven.org/maven2"), Artifact("groupId", "artifactId"))
        )
    }

}