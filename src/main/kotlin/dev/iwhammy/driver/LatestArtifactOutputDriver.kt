package dev.iwhammy.driver

import dev.iwhammy.domain.LatestRemoteArtifact
import org.slf4j.LoggerFactory
import dev.iwhammy.usecase.LatestArtifactOutputPort

class LatestArtifactOutputDriver: LatestArtifactOutputPort {
    private val logger = LoggerFactory.getLogger(LatestArtifactOutputDriver::class.java)

    override fun output(latestRemoteArtifacts: List<LatestRemoteArtifact>) {
        logger.info("")
        logger.info("------------------------------------------------------------------------------------------------")
        logger.info("Last released dates")
        logger.info("------------------------------------------------------------------------------------------------")
        logger.info("")
        latestRemoteArtifacts.forEach { logger.info(it.toLoggingMessage()) }
        logger.info("")
    }
}