package org.example.project.article.presentation.article_list

import org.example.project.article.domain.Article

sealed interface ArticleListAction {
    data class OnSearchQueryChange(val query: String): ArticleListAction
    data class OnArticleClick(val article: Article): ArticleListAction
    data class OnTabSelected(val index: Int): ArticleListAction
}