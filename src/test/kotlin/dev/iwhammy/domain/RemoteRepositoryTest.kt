package dev.iwhammy.domain

import dev.iwhammy.domain.RemoteRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class RemoteRepositoryTest {
    @Test
    fun `末尾にスラッシュがないURLの場合スラッシュをつけて返す`() {
        assertEquals("https://example.com/", RemoteRepository("central", "https://example.com").normalizedUrl())
    }

    @Test
    fun `末尾にスラッシュがあるURLの場合そのまま返す`() {
        assertEquals("https://example.com/", RemoteRepository("central", "https://example.com/").normalizedUrl())
    }
}