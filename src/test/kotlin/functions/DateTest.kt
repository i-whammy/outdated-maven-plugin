package functions

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

class DateTest {
    @Test
    fun `与えられたエポックミリ秒のタイムスタンプをLocalDateTimeに変換する`() {
        assertEquals(LocalDateTime.of(2022,1,25,7,6,17), toLocalDateTime(1643094377000))
    }

    @Test
    fun `与えられたLocalDateTimeを所定のフォーマットに変換する`() {
        assertEquals("2022-12-10", toISOLocalFormattedDate(LocalDateTime.of(2022,12,10,0,0,0)))
    }
}