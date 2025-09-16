package org.example.project


import androidx.compose.runtime.*
import org.example.project.article.presentation.article_list.ArticleListScreenRoot
import org.example.project.article.presentation.article_list.ArticleListViewModel
import org.jetbrains.compose.ui.tooling.preview.Preview


@Composable
@Preview
fun App() {
    ArticleListScreenRoot(
        viewModel = remember { ArticleListViewModel()},
        onArticleClick = {

        }
    )
}