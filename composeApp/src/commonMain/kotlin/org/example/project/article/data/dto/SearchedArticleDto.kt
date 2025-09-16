package org.example.project.article.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class SearchedArticleDto(
    val source: SourceDto,
    val author: String? = null,
    val title: String,
    val description: String? = null,
    val url: String,
    val urlToImage: String? = null,
    val publishedAt: String,
)