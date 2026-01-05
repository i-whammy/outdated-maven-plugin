package dev.iwhammy.usecase.port

import dev.iwhammy.domain.LatestRemoteArtifact

interface LatestArtifactOutputPort {

    fun output(latestRemoteArtifacts: List<LatestRemoteArtifact>)
}