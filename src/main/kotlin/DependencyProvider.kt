import driver.MavenRemoteRepositoryDriver
import driver.OutdatedArtifactOutputDriver
import okhttp3.OkHttpClient
import org.kodein.di.*
import usecase.MavenOutdatedUseCase
import usecase.MavenRemoteRepositoryPort
import usecase.OutdatedArtifactOutputPort

class DependencyProvider {

    private val dependency = DI {
        val client = OkHttpClient()
        bind<MavenRemoteRepositoryPort> { singleton { MavenRemoteRepositoryDriver(client) }}
        bind<OutdatedArtifactOutputPort> { singleton { OutdatedArtifactOutputDriver() }}
    }

    fun mavenOutdatedUseCase() = dependency.newInstance { MavenOutdatedUseCase(instance(), instance()) }
}