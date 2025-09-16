package org.example.project.article.data.repository

import org.example.project.article.data.mappers.toArticle
import org.example.project.article.data.network.RemoteArticleDataSource
import org.example.project.article.domain.Article
import org.example.project.article.domain.ArticleRepository
import org.example.project.core.apikey
import org.example.project.core.domain.DataError
import org.example.project.core.domain.Result
import org.example.project.core.domain.map

class DefaultArticleRepository(
    private val remoteArticleDataSource: RemoteArticleDataSource
): ArticleRepository {
    override suspend fun searchArticles(query: String): Result<List<Article>, DataError.Remote> {
        return remoteArticleDataSource
            .searchArticles(query, "us", "business", apikey)
            .map { dto ->
                dto.articles.map { it.toArticle() }
            }
    }
}