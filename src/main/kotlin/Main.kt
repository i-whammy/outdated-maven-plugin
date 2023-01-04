import kotlin.system.exitProcess

fun main(args: Array<String>) {
    val provider = DependencyProvider()
    val useCase by provider.mavenOutdatedUseCase()
    try {
        val thresholdYear = 1L
        useCase.execute(thresholdYear)
    } catch (e: Exception) {
        println(e.message)
        exitProcess(1)
    }
}