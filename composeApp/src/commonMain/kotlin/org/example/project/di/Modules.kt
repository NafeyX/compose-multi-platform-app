package org.example.project.di

import org.example.project.article.data.network.KtorRemoteArticleDataSource
import org.example.project.article.data.network.RemoteArticleDataSource
import org.example.project.article.data.repository.DefaultArticleRepository
import org.example.project.article.domain.ArticleRepository
import org.example.project.article.presentation.article_list.ArticleListViewModel
import org.example.project.core.data.HttpClientFactory
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

expect val platformModule: Module

val sharedModule = module {
    single { HttpClientFactory.create(get()) }
    singleOf(::KtorRemoteArticleDataSource).bind<RemoteArticleDataSource>()
    singleOf(::DefaultArticleRepository).bind<ArticleRepository>()

    viewModelOf(::ArticleListViewModel)
}