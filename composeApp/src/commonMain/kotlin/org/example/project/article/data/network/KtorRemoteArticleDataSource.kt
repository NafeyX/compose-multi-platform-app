package org.example.project.article.data.network

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import org.example.project.article.data.dto.SearchResponseDto
import org.example.project.core.BASE_URL
import org.example.project.core.data.safeCall
import org.example.project.core.domain.DataError
import org.example.project.core.domain.Result

class KtorRemoteArticleDataSource(
    private val httpClient: HttpClient
): RemoteArticleDataSource {
    override suspend fun searchArticles(
        query: String,
        country: String,
        category: String,
        apiKey: String
    ): Result<SearchResponseDto, DataError.Remote> {
        return safeCall {
            httpClient.get(
                urlString = "$BASE_URL/top-headlines"
            ) {
                parameter("country", country)
                parameter("category", category)
                parameter("apiKey", apiKey)
                if (query.isNotBlank()) {
                    parameter("q", query)
                }
            }
        }
    }
}