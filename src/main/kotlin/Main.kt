import kotlin.system.exitProcess

fun main(args: Array<String>) {
    val provider = DependencyProvider()
    val executor by provider.mavenOutdatedExecutor()
    try {
        val thresholdYear = 1L
        executor.execute(thresholdYear)
    } catch (e: Exception) {
        println(e.message)
        exitProcess(1)
    }
}