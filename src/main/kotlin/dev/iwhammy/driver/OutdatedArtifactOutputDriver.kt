package dev.iwhammy.driver

import dev.iwhammy.domain.LatestRemoteArtifact
import org.slf4j.LoggerFactory
import dev.iwhammy.usecase.port.OutdatedArtifactOutputPort

class OutdatedArtifactOutputDriver : OutdatedArtifactOutputPort {

    private val logger = LoggerFactory.getLogger(OutdatedArtifactOutputDriver::class.java)
    override fun output(outdatedArtifacts: List<LatestRemoteArtifact>, thresholdYear: Long) {
        logger.warn("------------------------------------------------------------------------------------------------")
        logger.warn("These artifacts have not been updated more than $thresholdYear year. Consider adopting alternatives.")
        logger.warn("------------------------------------------------------------------------------------------------")
        logger.warn("")
        outdatedArtifacts.forEach { logger.warn(it.toLoggingMessage()) }
        logger.warn("")
    }
}