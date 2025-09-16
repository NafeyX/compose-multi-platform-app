package org.example.project.article.presentation.article_list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.example.project.article.data.Article
import org.example.project.article.presentation.article_list.components.ArticleList
import org.example.project.article.presentation.article_list.components.ArticleSearchBar
import org.example.project.core.presentation.DarkBlue
import org.example.project.core.presentation.DesertWhite
import org.example.project.core.presentation.SandYellow
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import third_cmp.composeapp.generated.resources.Res
import third_cmp.composeapp.generated.resources.favorites
import third_cmp.composeapp.generated.resources.no_favorite_articles
import third_cmp.composeapp.generated.resources.no_search_results
import third_cmp.composeapp.generated.resources.search_results


@Composable
fun ArticleListScreenRoot(
    viewModel: ArticleListViewModel = koinViewModel(),
    onArticleClick: (Article) -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    ArticleListScreen(
        state = state,
        onAction = { action ->
            when(action) {
                is ArticleListAction.OnArticleClick -> onArticleClick(action.article)
                else -> Unit
            }
            viewModel.onAction(action)

        }
    )

}


@Composable
private fun ArticleListScreen(
    state: ArticleListState,
    onAction: (ArticleListAction) -> Unit
){
    val keyboardController = LocalSoftwareKeyboardController.current
    val pagerState = rememberPagerState { 2 }
    val searchResultsListState = rememberLazyListState()
    val favoritesSearchResultsListState = rememberLazyListState()


    LaunchedEffect(state.searchResults) {
        searchResultsListState.animateScrollToItem(0)
    }
    LaunchedEffect(state.selectedTabIndex) {
        pagerState.animateScrollToPage(state.selectedTabIndex)
    }

    LaunchedEffect(pagerState.currentPage) {
        onAction(ArticleListAction.OnTabSelected(pagerState.currentPage))
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBlue),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ArticleSearchBar(
            searchQuery = state.searchQuery,
            onSearchQueryChange = {
                onAction(ArticleListAction.OnSearchQueryChange(it))
            },
            onImeSearch = {
                keyboardController?.hide()
            },
            modifier = Modifier
                .widthIn(max = 400.dp)
                .fillMaxWidth()
                .padding(16.dp)
        )
        Surface(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            color = DesertWhite,
            shape = RoundedCornerShape(
                topStart = 32.dp,
                topEnd = 32.dp
            )
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                TabRow(
                    selectedTabIndex = state.selectedTabIndex,
                    modifier = Modifier
                        .padding(vertical = 12.dp)
                        .widthIn(max = 700.dp)
                        .fillMaxWidth(),
                    containerColor = DesertWhite,
                    contentColor = SandYellow,
                    indicator = { tabPositions ->
                        TabRowDefaults.SecondaryIndicator(
                            color = SandYellow,
                            modifier = Modifier
                                .tabIndicatorOffset(tabPositions[state.selectedTabIndex])
                        )
                    }
                ) {
                    Tab(
                        selected = state.selectedTabIndex == 0,
                        onClick = {
                            onAction(ArticleListAction.OnTabSelected(0))
                        },
                        modifier = Modifier.weight(1f),
                        selectedContentColor = SandYellow,
                        unselectedContentColor = Color.Black.copy(alpha = 0.5f)
                    ) {
                        Text(
                            text = stringResource(Res.string.search_results),
                            modifier = Modifier
                                .padding(vertical = 12.dp)
                        )
                    }
                    Tab(
                        selected = state.selectedTabIndex == 1,
                        onClick = {
                            onAction(ArticleListAction.OnTabSelected(1))
                        },
                        modifier = Modifier.weight(1f),
                        selectedContentColor = SandYellow,
                        unselectedContentColor = Color.Black.copy(alpha = 0.5f)
                    ) {
                        Text(
                            text = stringResource(Res.string.favorites),
                            modifier = Modifier
                                .padding(vertical = 12.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))
                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {pageIndex ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ){
                        when(pageIndex) {
                            0 -> {
                                if(state.isLoading){
                                    CircularProgressIndicator()
                                } else {
                                    when {
                                        state.errorMessage != null -> {
                                            Text(
                                                text = state.errorMessage.asString(),
                                                textAlign = TextAlign.Center,
                                                style = MaterialTheme.typography.headlineSmall,
                                                color = MaterialTheme.colorScheme.error
                                            )
                                        }
                                        state.searchResults.isEmpty() -> {
                                            Text(
                                                text = stringResource(Res.string.no_search_results),
                                                textAlign = TextAlign.Center,
                                                style = MaterialTheme.typography.headlineSmall,
                                                color = MaterialTheme.colorScheme.error
                                            )
                                        }
                                        else -> {
                                            ArticleList(
                                                articles = state.searchResults,
                                                onArticleClick = {
                                                    onAction(ArticleListAction.OnArticleClick(it))
                                                },
                                                modifier = Modifier.fillMaxSize(),
                                                scrollState = searchResultsListState
                                            )
                                        }
                                    }
                                }
                            }
                            1 -> {
                                if(state.favoriteArticles.isEmpty()) {
                                    Text(
                                        text = stringResource(Res.string.no_favorite_articles),
                                        textAlign = TextAlign.Center,
                                        style = MaterialTheme.typography.headlineSmall,
                                    )
                                } else {
                                    ArticleList(
                                        articles = state.favoriteArticles,
                                        onArticleClick = {
                                            onAction(ArticleListAction.OnArticleClick(it))
                                        },
                                        modifier = Modifier.fillMaxSize(),
                                        scrollState = favoritesSearchResultsListState
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}