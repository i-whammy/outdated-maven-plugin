package domain

data class RemoteRepository(val id: String, val url: String) {
    fun normalizedUrl(): String {
        return if (url.endsWith("/")) url else "$url/"
    }
}

data class RemoteArtifactCandidate(val artifact: Artifact, val remoteRepositoryCandidates: List<RemoteRepository>)