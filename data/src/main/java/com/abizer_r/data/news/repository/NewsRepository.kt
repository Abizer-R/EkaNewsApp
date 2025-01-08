package com.abizer_r.data.news.repository

import com.abizer_r.data.news.local.NewsItemDb
import com.abizer_r.data.news.model.NewsResponse
import kotlinx.coroutines.flow.Flow

interface NewsRepository {
    suspend fun getTopHeadlines(): NewsResponse?

    suspend fun saveNewsToDb(newsItem: NewsItemDb)

    suspend fun saveNewsItemsToDb(newsItems: List<NewsItemDb>)

    suspend fun getSavedNews(source: String): Flow<List<NewsItemDb>>

    suspend fun deleteNewsByUrl(url: String)

    suspend fun checkNewsSavedByUrl(newsUrl: String): Boolean
}