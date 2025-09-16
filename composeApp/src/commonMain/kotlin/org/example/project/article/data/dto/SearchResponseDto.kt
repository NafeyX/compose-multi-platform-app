package org.example.project.article.data.dto

import kotlinx.serialization.Serializable


@Serializable
data class SearchResponseDto(
    val status: String,
    val totalResults: Int,
    val articles: List<SearchedArticleDto>
)