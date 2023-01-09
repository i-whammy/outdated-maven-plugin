package domain

import io.mockk.every
import io.mockk.mockkStatic
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime

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

}