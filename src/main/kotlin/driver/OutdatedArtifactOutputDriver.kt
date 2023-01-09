package driver

import domain.LatestRemoteArtifact
import org.slf4j.LoggerFactory
import usecase.OutdatedArtifactOutputPort
import java.time.format.DateTimeFormatter

class OutdatedArtifactOutputDriver : OutdatedArtifactOutputPort {

    private val logger = LoggerFactory.getLogger(OutdatedArtifactOutputDriver::class.java)
    override fun output(outdatedArtifacts: List<LatestRemoteArtifact>) {
        logger.info("")
        logger.info("------------------------------------------------------------------------------------------------")
        logger.info("These artifacts have not been updated more than a year. Consider adopting alternatives.")
        logger.info("------------------------------------------------------------------------------------------------")
        logger.info("")
        outdatedArtifacts.forEach { artifact ->
            logger.info(
                "${artifact.artifact.groupId}:${artifact.artifact.artifactId} - The Last Release Date: ${
                    artifact.lastUpdated.format(
                        DateTimeFormatter.ofPattern("yyyy-MM-dd")
                    )
                }"
            )
        }
        logger.info("")
    }
}