import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.ObjectMapper
import okhttp3.OkHttpClient
import okhttp3.Request

interface OutdatedDependencyPort {
    fun filterOutdatedDependencies(dependencies: List<Artifact>): List<OutdatedDependency>
}

class OutputDependencyRepository: OutdatedDependencyPort {
    private val client = OkHttpClient()

    private val mapper = ObjectMapper()

    override fun filterOutdatedDependencies(dependencies: List<Artifact>): List<OutdatedDependency> {
        val requests = dependencies.map { buildRequestUrl(it.groupId, it.artifactId) }.map { Request.Builder().url(it).build() }
        val timestampComparer = TimestampComparer(1)
        val outdatedDependencies = mutableListOf<OutdatedDependency>()
        requests.map { request ->
            client.newCall(request).execute().use {
                val responseBody = mapper.readValue(it.body!!.byteStream(), ResponseBody::class.java)
                val artifact = responseBody.response.artifactResponses[0]
                if (timestampComparer.isOutDated(artifact.timestamp)) outdatedDependencies.add(artifact.toOutdatedDependency())
                println("${artifact.id} - The Last Release Date: ${artifact.timestamp.let(::toLocalDateTime).let(::toFormattedDate)}")
            }
        }
        return outdatedDependencies
    }
}

fun ArtifactResponse.toOutdatedDependency(): OutdatedDependency {
    return OutdatedDependency(this.groupId, this.artifactId, this.timestamp)
}

data class OutdatedDependency(val groupId: GroupId, val artifactId: ArtifactId, val lastReleaseTimestamp: Long) {
    fun id() = "$groupId:$artifactId"
}

fun buildRequestUrl(groupId: GroupId, artifactId: ArtifactId): RequestUrl {
    return "https://search.maven.org/solrsearch/select?q=g:$groupId%20AND%20a:$artifactId&wt=json"
}

@JsonIgnoreProperties(ignoreUnknown = true)
data class ResponseBody(@JsonProperty("response") val response: Response)

@JsonIgnoreProperties(ignoreUnknown = true)
data class Response(@JsonProperty("docs") val artifactResponses: List<ArtifactResponse>)

@JsonIgnoreProperties(ignoreUnknown = true)
data class ArtifactResponse(
    @JsonProperty("groupId") val groupId: String,
    @JsonProperty("artifactId") val artifactId: String,
    @JsonProperty("id") val id: String,
    @JsonProperty("timestamp") val timestamp: Long,
    @JsonProperty("latestVersion") val latestVersion: String
)
