fun main(args: Array<String>) {
    val provider = DependencyProvider()
    val executor by provider.mavenOutdatedExecutor()
    executor.execute(1)
}