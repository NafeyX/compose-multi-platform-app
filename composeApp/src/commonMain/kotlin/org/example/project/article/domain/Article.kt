package org.example.project.article.domain

data class Article(
    val id: String,
    val author: String,
    val title: String,
    val description: String?,
    val url: String,
    val urlToImage: String?,
    val publishedAt: String,
)