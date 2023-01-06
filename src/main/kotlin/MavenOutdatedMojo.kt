import org.apache.maven.plugin.AbstractMojo
import org.apache.maven.plugins.annotations.Mojo

@Mojo(name = "mvn-outdated")
class MavenOutdatedMojo: AbstractMojo() {
    override fun execute() {
        main(emptyArray())
    }
}