package usecase

import domain.LatestRemoteArtifact

interface OutdatedArtifactOutputPort {
    fun output(outdatedArtifacts: List<LatestRemoteArtifact>)
}
