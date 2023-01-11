package usecase

import domain.*
import java.io.InputStream
import java.time.ZonedDateTime

interface MavenRemoteRepositoryPort {
    fun fetchLatestRemoteArtifact(
        remoteArtifactCandidate: RemoteArtifactCandidate,
        takeOutLastUpdated: (i: InputStream) -> ZonedDateTime
    ): LatestRemoteArtifactResult
}