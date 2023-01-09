package usecase

import domain.LatestRemoteArtifact
import domain.RemoteArtifactCandidate
import java.io.InputStream
import java.time.ZonedDateTime

interface MavenRemoteRepositoryPort {
    fun fetchLatestRemoteArtifacts(remoteArtifactCandidates: List<RemoteArtifactCandidate>, takeOutLastUpdatedFn: (i: InputStream) -> ZonedDateTime): List<LatestRemoteArtifact>
}