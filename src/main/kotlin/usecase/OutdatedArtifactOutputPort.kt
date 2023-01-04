package usecase

import domain.LatestArtifact

interface OutdatedArtifactOutputPort {
    fun print(outdatedArtifacts: List<LatestArtifact>)
}
