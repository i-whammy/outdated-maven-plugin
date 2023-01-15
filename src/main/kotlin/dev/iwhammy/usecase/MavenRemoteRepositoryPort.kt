package dev.iwhammy.usecase

import dev.iwhammy.domain.LatestRemoteArtifactResult
import dev.iwhammy.domain.RemoteArtifactCandidate
import java.io.InputStream
import java.time.ZonedDateTime

interface MavenRemoteRepositoryPort {
    fun fetchLatestRemoteArtifact(
        remoteArtifactCandidate: RemoteArtifactCandidate,
        takeOutLastUpdated: (i: InputStream) -> ZonedDateTime
    ): LatestRemoteArtifactResult
}