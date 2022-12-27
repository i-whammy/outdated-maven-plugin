import functions.toFormattedDate
import functions.toLocalDateTime

interface OutdatedDependencyPrintPort {
    fun print(outdatedDependencies: List<OutdatedDependency>)
}

class OutdatedDependencyPrintDriver: OutdatedDependencyPrintPort {
    override fun print(outdatedDependencies: List<OutdatedDependency>) {
        println("")
        println("------------------------------------------------------------------------------------------------")
        println("These artifacts have not been updated more than a year. Consider adopting alternatives.")
        println("------------------------------------------------------------------------------------------------")
        println("")
        outdatedDependencies.forEach { d ->
            println("${d.id()} - The Last Release Date: ${d.lastReleaseTimestamp.let(::toLocalDateTime).let(::toFormattedDate)}")
        }

    }
}