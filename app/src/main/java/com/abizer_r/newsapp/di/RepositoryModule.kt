package com.abizer_r.newsapp.di

import com.abizer_r.data.news.repository.NewsRepository
import com.abizer_r.data.news.repository.NewsRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindNewsRepository(newsRepoImpl: NewsRepositoryImpl): NewsRepository
}
