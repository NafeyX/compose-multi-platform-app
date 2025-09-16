package org.example.project.article.domain

import org.example.project.core.domain.DataError
import org.example.project.core.domain.Result

interface ArticleRepository {
    suspend fun searchArticles(query: String): Result<List<Article>, DataError.Remote>
}