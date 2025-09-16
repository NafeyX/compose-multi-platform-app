package org.example.project


import androidx.compose.runtime.*
import io.ktor.client.engine.HttpClientEngine
import org.example.project.article.data.network.KtorRemoteArticleDataSource
import org.example.project.article.data.repository.DefaultArticleRepository
import org.example.project.article.presentation.article_list.ArticleListScreenRoot
import org.example.project.article.presentation.article_list.ArticleListViewModel
import org.example.project.core.data.HttpClientFactory
import org.jetbrains.compose.ui.tooling.preview.Preview


@Composable
@Preview
fun App(engine: HttpClientEngine) {
    ArticleListScreenRoot(
        viewModel = remember { ArticleListViewModel(
            articleRepository = DefaultArticleRepository(
                remoteArticleDataSource = KtorRemoteArticleDataSource(
                    httpClient = HttpClientFactory.create(engine)
                )
            )
        )},
        onArticleClick = {

        }
    )
}