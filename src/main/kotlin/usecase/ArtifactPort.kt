package usecase

import domain.Artifact

interface ArtifactPort {
    fun fetchArtifacts(): List<Artifact>
}