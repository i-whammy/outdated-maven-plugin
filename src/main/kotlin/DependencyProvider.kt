import org.kodein.di.*

class DependencyProvider {

    private val dependency = DI {
        bind<ArtifactPort> { singleton { PomArtifactRepository() }}
        bind<MavenRepositoryPort> { singleton { MavenCentralDriver() }}
        bind<OutdatedArtifactOutputPort> { singleton { OutdatedArtifactPrintDriver() }}
    }

    fun mavenOutdatedExecutor() = dependency.newInstance { MavenOutdatedExecutor(instance(), instance(), instance()) }
}