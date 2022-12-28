import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class MavenCentralDriverTest {

    @Test
    fun `与えられたGroupIdとArtifactIdからURLを作成する`() {
        assertEquals("https://search.maven.org/solrsearch/select?q=g:com.google.inject%20AND%20a:guice&wt=json", buildRequestUrl("com.google.inject", "guice"))
    }

}