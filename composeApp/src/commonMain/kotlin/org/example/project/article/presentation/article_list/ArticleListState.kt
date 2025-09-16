package org.example.project.article.presentation.article_list

import org.example.project.article.data.Article
import org.example.project.core.presentation.UiText

data class ArticleListState(
    val searchQuery: String = "Kotlin",
    val searchResults: List<Article> = emptyList(),
    val favoriteArticles: List<Article> = emptyList(),
    val isLoading: Boolean = false,
    val selectedTabIndex: Int = 0,
    val errorMessage: UiText? = null
)