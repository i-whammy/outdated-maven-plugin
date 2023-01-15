package dev.iwhammy.usecase

import dev.iwhammy.domain.LatestRemoteArtifact

interface OutdatedArtifactOutputPort {
    fun output(outdatedArtifacts: List<LatestRemoteArtifact>)
}
