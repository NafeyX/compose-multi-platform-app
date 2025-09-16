package org.example.project.article.presentation.article_list

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ArticleListViewModel  {
    private val _state = MutableStateFlow(ArticleListState())
    val state = _state.asStateFlow()

    fun onAction(action: ArticleListAction){
        when(action) {
            is ArticleListAction.OnArticleClick -> {

            }
            is ArticleListAction.OnSearchQueryChange -> {
                _state.update {
                    it.copy(searchQuery = action.query)
                }
            }
            is ArticleListAction.OnTabSelected -> {

            }
        }
    }
}