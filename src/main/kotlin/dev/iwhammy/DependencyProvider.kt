package dev.iwhammy

import dev.iwhammy.driver.LatestArtifactOutputDriver
import dev.iwhammy.driver.MavenRemoteRepositoryDriver
import dev.iwhammy.driver.OutdatedArtifactOutputDriver
import okhttp3.OkHttpClient
import org.kodein.di.*
import dev.iwhammy.usecase.port.LatestArtifactOutputPort
import dev.iwhammy.usecase.MavenOutdatedUseCase
import dev.iwhammy.usecase.port.MavenRemoteRepositoryPort
import dev.iwhammy.usecase.port.OutdatedArtifactOutputPort

object DependencyProvider {
    private val client = OkHttpClient()

    private val dependency = DI {
        bind<MavenRemoteRepositoryPort> { singleton { MavenRemoteRepositoryDriver(client) }}
        bind<LatestArtifactOutputPort> { singleton { LatestArtifactOutputDriver() }}
        bind<OutdatedArtifactOutputPort> { singleton { OutdatedArtifactOutputDriver() }}
    }

    fun mavenOutdatedUseCase() = dependency.newInstance { MavenOutdatedUseCase(instance(), instance(), instance()) }
}