import kotlin.system.exitProcess

fun main(args: Array<String>) {
    val provider = DependencyProvider()
    val executor by provider.mavenOutdatedExecutor()
    try {
        executor.execute(1)
    } catch (e: Exception) {
        println(e.message)
        exitProcess(1)
    }
}