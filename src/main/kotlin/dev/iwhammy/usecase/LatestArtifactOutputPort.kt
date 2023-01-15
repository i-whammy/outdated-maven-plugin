package dev.iwhammy.usecase

import dev.iwhammy.domain.LatestRemoteArtifact

interface LatestArtifactOutputPort {

    fun output(latestRemoteArtifacts: List<LatestRemoteArtifact>)
}