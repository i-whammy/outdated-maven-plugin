package domain

import io.mockk.every
import io.mockk.mockkStatic
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import java.time.ZoneOffset

class LatestArtifactTest {

    @Test
    fun `現在の時刻から最後にリリースされた時刻を引いた値が閾値を超えていたらtrueを返す`() {
        mockkStatic("java.time.LocalDateTime")
        every { LocalDateTime.now() } returns LocalDateTime.of(2023, 1, 1, 0, 0, 0)
        val latestArtifact = LatestArtifact(
            "com.example",
            "sample",
            LocalDateTime.of(2022, 1, 1, 0, 0, 0).toEpochSecond(ZoneOffset.UTC) * 1000
        )
        assertTrue { latestArtifact.isOutdated(1L) }
    }

    @Test
    fun `現在の時刻から最後にリリースされた時刻を引いた値が閾値を超えていたらfalseを返す`() {
        mockkStatic("java.time.LocalDateTime")
        every { LocalDateTime.now() } returns LocalDateTime.of(2023, 1, 1, 0, 0, 0)
        val latestArtifact = LatestArtifact(
            "com.example",
            "sample",
            LocalDateTime.of(2022, 1, 1, 0, 0, 1).toEpochSecond(ZoneOffset.UTC) * 1000
        )
        assertFalse { latestArtifact.isOutdated(1L) }
    }

}