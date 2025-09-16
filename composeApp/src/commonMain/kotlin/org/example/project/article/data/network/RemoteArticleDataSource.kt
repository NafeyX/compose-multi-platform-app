package org.example.project.article.data.network

import org.example.project.article.data.dto.SearchResponseDto
import org.example.project.core.domain.DataError
import org.example.project.core.domain.Result

interface RemoteArticleDataSource {
    suspend fun searchArticles(
        query: String,
        country: String = "us",
        category: String = "business",
        apiKey: String
    ): Result<SearchResponseDto, DataError.Remote>
}