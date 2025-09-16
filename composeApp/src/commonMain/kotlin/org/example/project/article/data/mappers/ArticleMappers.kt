package org.example.project.article.data.mappers

import org.example.project.article.data.dto.SearchedArticleDto
import org.example.project.article.domain.Article

fun SearchedArticleDto.toArticle(): Article {
    return Article(
        id = this.source.id ?: this.url,
        author = this.author ?: "Unknown",
        title = this.title,
        description = this.description ?: "", // Handle null description
        url = this.url,
        urlToImage = if (this.urlToImage.isNullOrBlank()) null else this.urlToImage, // Better null handling
        publishedAt = this.publishedAt
    )
}