package driver

import domain.LatestRemoteArtifact
import org.slf4j.LoggerFactory
import usecase.OutdatedArtifactOutputPort

class OutdatedArtifactOutputDriver : OutdatedArtifactOutputPort {

    private val logger = LoggerFactory.getLogger(OutdatedArtifactOutputDriver::class.java)
    override fun output(outdatedArtifacts: List<LatestRemoteArtifact>) {
        logger.warn("------------------------------------------------------------------------------------------------")
        logger.warn("These artifacts have not been updated more than a year. Consider adopting alternatives.")
        logger.warn("------------------------------------------------------------------------------------------------")
        logger.warn("")
        outdatedArtifacts.forEach { logger.warn(it.toLoggingMessage()) }
        logger.warn("")
    }
}