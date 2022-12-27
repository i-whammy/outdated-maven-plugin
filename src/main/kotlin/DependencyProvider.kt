import org.kodein.di.*

class DependencyProvider {

    private val dependency = DI {
        bind<ArtifactPort> { singleton { PomArtifactRepository() }}
        bind<OutdatedDependencyPort> { singleton { OutdatedDependencyRepository() }}
        bind<OutdatedDependencyPrintPort> { singleton { OutdatedDependencyPrintDriver() }}
    }

    fun mavenOutdatedExecutor() = dependency.newInstance { MavenOutdatedExecutor(instance(), instance(), instance()) }
}