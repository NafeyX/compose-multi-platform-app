package org.example.project.article.data

data class Article(
    val id: String,
    val title: String,
    val description: String?,
    val url: String,
    val urlToImage: String?
)