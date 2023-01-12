package usecase

import domain.LatestRemoteArtifact

interface LatestArtifactOutputPort {

    fun output(latestRemoteArtifacts: List<LatestRemoteArtifact>)
}