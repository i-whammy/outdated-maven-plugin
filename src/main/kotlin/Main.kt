import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.ObjectMapper
import okhttp3.OkHttpClient
import okhttp3.Request
import org.apache.maven.model.io.xpp3.MavenXpp3Reader
import java.io.File
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

fun main(args: Array<String>) {
    val model = MavenXpp3Reader().read(File("/path/to/file").inputStream())
    val requests =
        model.dependencies.map { buildRequestUrl(it.groupId, it.artifactId) }.map { Request.Builder().url(it).build() }
    val client = OkHttpClient()
    val timestampComparer = TimestampComparer(1)
    val outdatedArtifacts = mutableListOf<Artifact>()
    requests.map { request ->
        client.newCall(request).execute().use {
            val mapper = ObjectMapper()
            val responseBody = mapper.readValue(it.body!!.byteStream(), ResponseBody::class.java)
            val artifact = responseBody.response.artifacts[0]
            if (timestampComparer.isOutDated(artifact.timestamp)) outdatedArtifacts.add(artifact)
            println("${artifact.id} - The Last Release Date: ${artifact.timestamp.let(::toLocalDateTime).let(::toFormattedDate)}")
        }
    }
    if (outdatedArtifacts.isNotEmpty()) {
        println("")
        println("------------------------------------------------------------------------------------------------")
        println("These artifacts are not updated more than a year. Consider adopting alternatives.")
        println("------------------------------------------------------------------------------------------------")
        println("")
        outdatedArtifacts.forEach { artifact ->
            println("${artifact.id} - The Last Release Date: ${artifact.timestamp.let(::toLocalDateTime).let(::toFormattedDate)}")
        }
    }
}

fun buildRequestUrl(groupId: GroupId, artifactId: ArtifactId): RequestUrl {
    return "https://search.maven.org/solrsearch/select?q=g:$groupId%20AND%20a:$artifactId&wt=json"
}

fun toLocalDateTime(epochMilli: Long): LocalDateTime =
    LocalDateTime.ofInstant(Instant.ofEpochMilli(epochMilli), ZoneId.of("Z"))

fun toFormattedDate(localDateTime: LocalDateTime): String = localDateTime.format(DateTimeFormatter.ISO_LOCAL_DATE)

typealias GroupId = String
typealias ArtifactId = String
typealias RequestUrl = String

@JsonIgnoreProperties(ignoreUnknown = true)
data class ResponseBody(@JsonProperty("response") val response: Response)

@JsonIgnoreProperties(ignoreUnknown = true)
data class Response(@JsonProperty("docs") val artifacts: List<Artifact>)

@JsonIgnoreProperties(ignoreUnknown = true)
data class Artifact(
    @JsonProperty("id") val id: String,
    @JsonProperty("timestamp") val timestamp: Long,
    @JsonProperty("latestVersion") val latestVersion: String
)

class TimestampComparer(private val thresholdYear: Long) {
    fun isOutDated(epochMilliSeconds: Long): Boolean {
        val zoneId = ZoneId.of("Z")
        return LocalDateTime.ofInstant(Instant.now(), zoneId).minusYears(thresholdYear) >= LocalDateTime.ofInstant(Instant.ofEpochMilli(epochMilliSeconds), zoneId)
    }
}