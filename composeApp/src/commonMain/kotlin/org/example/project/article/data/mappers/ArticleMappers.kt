package org.example.project.article.data.mappers

import org.example.project.article.data.dto.SearchedArticleDto
import org.example.project.article.domain.Article

fun SearchedArticleDto.toArticle(): Article {
    return Article(
        id = this.source.id ?: this.url,
        author = this.author ?: "Unknown",
        title = this.title,
        description = this.description,
        url = this.url,
        urlToImage = this.urlToImage,
        publishedAt = this.publishedAt
    )
}