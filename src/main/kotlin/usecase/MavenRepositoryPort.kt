package usecase

import domain.Artifact
import domain.LatestArtifact

interface MavenRepositoryPort {
    fun fetchLatestArtifacts(artifacts: List<Artifact>): List<LatestArtifact>
}