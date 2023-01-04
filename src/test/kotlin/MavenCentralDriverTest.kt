import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class MavenCentralDriverTest {

    @Test
    fun `与えられたGroupIdとArtifactIdからURLを作成する`() {
        assertEquals("https://search.maven.org/solrsearch/select?q=g:com.google.inject%20AND%20a:guice&wt=json", buildRequestUrl("com.google.inject", "guice"))
    }

    @Test
    fun `ArtifactResponseから最新のアーティファクトを生成する`() {
        assertEquals(LatestArtifact("groupId", "artifactId", 1672824139L),
            ArtifactResponse("groupId", "artifactId", "id", 1672824139L, "1.0.0-SNAPSHOT").toLatestArtifact())
    }

    @Test
    fun `ResponseBodyから最新のアーティファクトを取り出す`() {
        assertEquals(LatestArtifact("groupId", "artifactId", 1672824139L),
            ResponseBody(Response(listOf(ArtifactResponse("groupId", "artifactId", "id", 1672824139L, "1.0.0-SNAPSHOT")))).latestArtifact())
    }
}