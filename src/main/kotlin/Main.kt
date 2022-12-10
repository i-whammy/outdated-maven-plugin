import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.ObjectMapper
import okhttp3.OkHttpClient
import okhttp3.Request
import org.apache.maven.model.io.xpp3.MavenXpp3Reader
import java.io.File
import java.time.Instant
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

fun main(args: Array<String>) {
    val model = MavenXpp3Reader().read(File("/path/to/file").inputStream())
    val requests =
        model.dependencies.map { buildRequestUrl(it.groupId, it.artifactId) }.map { Request.Builder().url(it).build() }
    val client = OkHttpClient()
    requests.map { request ->
        client.newCall(request).execute().use {
            val mapper = ObjectMapper()
            val response = mapper.readValue(it.body!!.byteStream(), R::class.java)
            val doc = response.response.documents[0]
            println("${doc.id} - The Last Release Date: ${doc.timestamp.let(::toLocalDateTime).let(::toFormattedDate)}")
        }
    }
}

fun buildRequestUrl(groupId: GroupId, artifactId: ArtifactId): RequestUrl {
    return "https://search.maven.org/solrsearch/select?q=g:$groupId%20AND%20a:$artifactId&wt=json"
}

fun toLocalDateTime(epochMilli: Long): LocalDateTime =
    LocalDateTime.ofInstant(Instant.ofEpochMilli(epochMilli), TimeZone.getDefault().toZoneId())

fun toFormattedDate(localDateTime: LocalDateTime): String = localDateTime.format(DateTimeFormatter.ISO_LOCAL_DATE)

typealias GroupId = String
typealias ArtifactId = String
typealias RequestUrl = String

@JsonIgnoreProperties(ignoreUnknown = true)
data class R(@JsonProperty("response") val response: Response)

@JsonIgnoreProperties(ignoreUnknown = true)
data class Response(@JsonProperty("docs") val documents: List<Doc>)

@JsonIgnoreProperties(ignoreUnknown = true)
data class Doc(
    @JsonProperty("id") val id: String,
    @JsonProperty("timestamp") val timestamp: Long,
    @JsonProperty("latestVersion") val latestVersion: String
)