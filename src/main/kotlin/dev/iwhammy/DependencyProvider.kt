package dev.iwhammy

import dev.iwhammy.driver.LatestArtifactOutputDriver
import dev.iwhammy.driver.MavenRemoteRepositoryDriver
import dev.iwhammy.driver.OutdatedArtifactOutputDriver
import okhttp3.OkHttpClient
import org.kodein.di.*
import dev.iwhammy.usecase.LatestArtifactOutputPort
import dev.iwhammy.usecase.MavenOutdatedUseCase
import dev.iwhammy.usecase.MavenRemoteRepositoryPort
import dev.iwhammy.usecase.OutdatedArtifactOutputPort

class DependencyProvider {

    private val dependency = DI {
        val client = OkHttpClient()
        bind<MavenRemoteRepositoryPort> { singleton { MavenRemoteRepositoryDriver(client) }}
        bind<LatestArtifactOutputPort> { singleton { LatestArtifactOutputDriver() }}
        bind<OutdatedArtifactOutputPort> { singleton { OutdatedArtifactOutputDriver() }}
    }

    fun mavenOutdatedUseCase() = dependency.newInstance { MavenOutdatedUseCase(instance(), instance(), instance()) }
}