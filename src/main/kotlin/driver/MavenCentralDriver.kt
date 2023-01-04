package driver

import domain.LatestArtifact
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.ObjectMapper
import domain.Artifact
import domain.ArtifactId
import domain.GroupId
import okhttp3.OkHttpClient
import okhttp3.Request
import usecase.MavenRepositoryPort

class MavenCentralDriver : MavenRepositoryPort {
    private val client = OkHttpClient()

    private val mapper = ObjectMapper()

    override fun fetchLatestArtifacts(artifacts: List<Artifact>): List<LatestArtifact> {

        val requests =
            artifacts.map { buildRequestUrl(it.groupId, it.artifactId) }.map { Request.Builder().url(it).build() }
        val latestArtifacts = requests.map { request ->
            client.newCall(request).execute().use { response ->
                responseToLatestArtifact(response)
            }
        }
        return latestArtifacts
    }

    private fun responseToLatestArtifact(response: okhttp3.Response): LatestArtifact {
        when (response.code) {
            404 -> throw ArtifactNotFoundException("Artifact Not Found.")
            500 -> throw RuntimeException()
            else -> {
                return response.body!!.byteStream().use { stream ->
                    mapper.readValue(stream, ResponseBody::class.java)
                }.latestArtifact()
            }
        }
    }
}

fun ResponseBody.latestArtifact(): LatestArtifact {
    if (this.response.artifactResponses.isEmpty()) {
        throw ArtifactNotFoundException("Artifact Not Found.")
    }
    val latestArtifact = this.response.artifactResponses[0].toLatestArtifact()
    println("${latestArtifact.id()} - ${latestArtifact.lastReleasedLocalDateTime()}")
    return latestArtifact
}

class ArtifactNotFoundException(override val message: String) : RuntimeException()

typealias RequestUrl = String

fun ArtifactResponse.toLatestArtifact(): LatestArtifact {
    return LatestArtifact(this.groupId, this.artifactId, this.timestamp)
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
    @JsonProperty("g") val groupId: String,
    @JsonProperty("a") val artifactId: String,
    @JsonProperty("id") val id: String,
    @JsonProperty("timestamp") val timestamp: Long,
    @JsonProperty("latestVersion") val latestVersion: String
)