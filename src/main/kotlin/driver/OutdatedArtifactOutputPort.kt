package driver

import domain.LatestArtifact
import usecase.OutdatedArtifactOutputPort

class OutdatedArtifactPrintDriver: OutdatedArtifactOutputPort {
    override fun print(outdatedArtifacts: List<LatestArtifact>) {
        println("")
        println("------------------------------------------------------------------------------------------------")
        println("These artifacts have not been updated more than a year. Consider adopting alternatives.")
        println("------------------------------------------------------------------------------------------------")
        println("")
        outdatedArtifacts.forEach { artifact ->
            println("${artifact.id()} - The Last Release Date: ${artifact.lastReleasedLocalDateTime()}")
        }

    }
}