package org.example.project.article.data.dto

import kotlinx.serialization.Serializable


@Serializable
data class SearchResponseDto(
    val articles: List<SearchedArticleDto>
)