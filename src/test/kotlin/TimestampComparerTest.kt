import io.mockk.every
import io.mockk.mockkStatic
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import java.time.ZoneOffset

class TimestampComparerTest {

    @Test
    fun `現在の時刻から閾値を引いた値よりも以前のエポックミリ秒を渡されたらtrueを返す`() {
        mockkStatic("java.time.LocalDateTime")
        every { LocalDateTime.now() } returns LocalDateTime.of(2022,1,1,0,0,0)
        assertTrue { TimestampComparer(1).isOutDated(LocalDateTime.of(2021,1,1,0,0,0).toEpochSecond(ZoneOffset.UTC) * 1000) }
    }

    @Test
    fun `現在の時刻から閾値を引いた値よりも後のエポックミリ秒を渡されたらfalseを返す`() {
        mockkStatic("java.time.LocalDateTime")
        every { LocalDateTime.now() } returns LocalDateTime.of(2022,1,1,0,0,0)
        assertTrue { TimestampComparer(1).isOutDated(LocalDateTime.of(2021,1,1,0,0,1).toEpochSecond(ZoneOffset.UTC) * 1000) }
    }

}