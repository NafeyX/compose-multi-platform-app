@file:OptIn(FlowPreview::class)

package org.example.project.article.presentation.article_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.example.project.article.domain.Article
import org.example.project.article.domain.ArticleRepository
import org.example.project.core.domain.onError
import org.example.project.core.domain.onSuccess
import org.example.project.core.presentation.toUiText

class ArticleListViewModel(
    private val articleRepository: ArticleRepository
) : ViewModel() {

    private var cachedArticles = emptyList<Article>()
    private var searchJob: Job? = null

    private val _state = MutableStateFlow(ArticleListState())
    val state = _state.asStateFlow()

    init {
        // Load initial articles when ViewModel is created
        loadInitialArticles()
        // Start observing search query changes
        observeSearchQuery()
    }

    fun onAction(action: ArticleListAction) {
        when(action) {
            is ArticleListAction.OnArticleClick -> {
                // Handle article click
            }
            is ArticleListAction.OnSearchQueryChange -> {
                _state.update {
                    it.copy(searchQuery = action.query)
                }
            }
            is ArticleListAction.OnTabSelected -> {
                _state.update {
                    it.copy(selectedTabIndex = action.index)
                }
            }
        }
    }

    private fun loadInitialArticles() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            // Load top headlines for initial content (empty query = top headlines)
            articleRepository
                .searchArticles("") // Empty query will get top headlines
                .onSuccess { articles ->
                    cachedArticles = articles
                    _state.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = null,
                            searchResults = articles
                        )
                    }
                }
                .onError { error ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = error.toUiText(),
                            searchResults = emptyList()
                        )
                    }
                }
        }
    }

    private fun observeSearchQuery() {
        _state
            .map { it.searchQuery }
            .distinctUntilChanged()
            .debounce(500L)
            .onEach { query ->
                when {
                    query.isBlank() -> {
                        _state.update {
                            it.copy(
                                errorMessage = null,
                                searchResults = cachedArticles
                            )
                        }
                    }
                    query.length >= 2 -> {
                        searchJob?.cancel()
                        searchJob = searchArticles(query)
                    }
                }
            }
            .launchIn(viewModelScope)
    }

    private fun searchArticles(query: String) = viewModelScope.launch {
        _state.update {
            it.copy(isLoading = true)
        }

        articleRepository
            .searchArticles(query)
            .onSuccess { searchArticles ->
                _state.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = null,
                        searchResults = searchArticles
                    )
                }
            }
            .onError { error ->
                _state.update {
                    it.copy(
                        searchResults = emptyList(),
                        isLoading = false,
                        errorMessage = error.toUiText()
                    )
                }
            }
    }
}