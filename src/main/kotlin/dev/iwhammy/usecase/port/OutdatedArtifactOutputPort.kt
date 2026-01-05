package dev.iwhammy.usecase.port

import dev.iwhammy.domain.LatestRemoteArtifact

interface OutdatedArtifactOutputPort {
    fun output(outdatedArtifacts: List<LatestRemoteArtifact>, thresholdYear: Long)
}
