import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import kotlin.test.assertEquals

class MainTest {
    @Test
    fun `与えられたGroupIdとArtifactIdからURLを作成する`() {
        assertEquals("https://search.maven.org/solrsearch/select?q=g:com.google.inject%20AND%20a:guice&wt=json", buildRequestUrl("com.google.inject", "guice"))
    }

    @Test
    fun `与えられたミリ秒のタイムスタンプをLocalDateTimeに変換する`() {
        assertEquals(LocalDateTime.of(2022,1,25,7,6,17), toLocalDateTime(1643061977000))
    }

    @Test
    fun `与えられたLocalDateTimeを所定のフォーマットに変換する`() {
        assertEquals("2022-12-10", toFormattedDate(LocalDateTime.of(2022,12,10,0,0,0)))
    }
}