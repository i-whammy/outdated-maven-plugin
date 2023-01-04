import driver.MavenCentralDriver
import driver.OutdatedArtifactPrintDriver
import driver.PomArtifactDriver
import org.kodein.di.*
import usecase.ArtifactPort
import usecase.MavenOutdatedUseCase
import usecase.MavenRepositoryPort
import usecase.OutdatedArtifactOutputPort

class DependencyProvider {

    private val dependency = DI {
        bind<ArtifactPort> { singleton { PomArtifactDriver() }}
        bind<MavenRepositoryPort> { singleton { MavenCentralDriver() }}
        bind<OutdatedArtifactOutputPort> { singleton { OutdatedArtifactPrintDriver() }}
    }

    fun mavenOutdatedUseCase() = dependency.newInstance { MavenOutdatedUseCase(instance(), instance(), instance()) }
}