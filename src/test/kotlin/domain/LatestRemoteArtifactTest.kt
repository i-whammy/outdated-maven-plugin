package domain

import io.mockk.every
import io.mockk.mockkStatic
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class LatestRemoteArtifactTest {

    @Test
    fun `現在の時刻から最後にリリースされた時刻を引いた値が閾値を超えていたらtrueを返す`() {
        mockkStatic("java.time.ZonedDateTime")
        every { ZonedDateTime.now(ZoneId.of("Z")) } returns ZonedDateTime.of(
            LocalDateTime.of(2023, 1, 1, 0, 0, 0),
            ZoneId.of("Z")
        )
        val latestArtifact = LatestRemoteArtifact(
            RemoteRepository("central", "https://example.com"),
            Artifact("groupId", "artifactId"),
            ZonedDateTime.of(LocalDateTime.of(2021, 1, 1, 0, 0, 0), ZoneId.of("Z"))
        )
        assertTrue { latestArtifact.isOutdated(1L) }
    }

    @Test
    fun `現在の時刻から最後にリリースされた時刻を引いた値が閾値を超えていなければfalseを返す`() {
        mockkStatic("java.time.ZonedDateTime")
        every { ZonedDateTime.now(ZoneId.of("Z")) } returns ZonedDateTime.of(
            LocalDateTime.of(2023, 1, 1, 0, 0, 0),
            ZoneId.of("Z")
        )
        val latestArtifact = LatestRemoteArtifact(
            RemoteRepository("central", "https://example.com"),
            Artifact("groupId", "artifactId"),
            ZonedDateTime.of(LocalDateTime.of(2022, 1, 1, 0, 0, 1), ZoneId.of("Z"))
        )
        assertFalse { latestArtifact.isOutdated(1L) }
    }

    @Test
    fun `ロギング用の文字列を返す`() {
        assertEquals(
            "groupId:artifactId - The Last Release Date: 2020-01-01",
            LatestRemoteArtifact(
                RemoteRepository("central", "https://example.net"),
                Artifact("groupId", "artifactId"),
                ZonedDateTime.of(LocalDateTime.of(2020,1,1,0,0,0), ZoneId.of("Z"))
            ).toLoggingMessage())
    }
}