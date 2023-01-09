package domain

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.io.ByteArrayInputStream
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime

class TakeOutLastUpdatedTest {

    @Test
    fun `入力のストリームを受け取って、その中からlastUpdatedの値を取り出す`() {
        val input = """
            <?xml version="1.0" encoding="UTF-8"?>
            <metadata>
              <groupId>com.example</groupId>
              <artifactId>example-artifact</artifactId>
              <versioning>
                <latest>22.12</latest>
                <release>22.12</release>
                <versions>
                  <version>22.12</version>
                </versions>
                <lastUpdated>20221212123456</lastUpdated>
              </versioning>
            </metadata>
        """.trimIndent().toByteArray()
        assertEquals(
            ZonedDateTime.of(
                LocalDateTime.of(2022,12,12,12,34,56),
                ZoneId.of("Z")
            ),
            takeOutLastUpdated().invoke(ByteArrayInputStream(input))
        )
    }
}