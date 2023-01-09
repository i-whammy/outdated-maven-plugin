package driver

import domain.LatestRemoteArtifact
import usecase.OutdatedArtifactOutputPort
import java.time.format.DateTimeFormatter

class OutdatedArtifactOutputDriver : OutdatedArtifactOutputPort {
    override fun output(outdatedArtifacts: List<LatestRemoteArtifact>) {
        println("")
        println("------------------------------------------------------------------------------------------------")
        println("These artifacts have not been updated more than a year. Consider adopting alternatives.")
        println("------------------------------------------------------------------------------------------------")
        println("")
        outdatedArtifacts.forEach { artifact ->
            println(
                "${artifact.artifact.groupId}:${artifact.artifact.artifactId} - The Last Release Date: ${
                    artifact.lastUpdated.format(
                        DateTimeFormatter.ofPattern("yyyy-MM-dd")
                    )
                }"
            )
        }
    }
}