package com.abizer_r.data.news.repository

import com.abizer_r.data.news.local.NewsItemDb
import com.abizer_r.data.news.model.NewsResponse

interface NewsRepository {
    suspend fun getTopHeadlines(): NewsResponse?

    suspend fun saveNewsToDb(newsItem: NewsItemDb)

    suspend fun saveNewsItemsToDb(newsItems: List<NewsItemDb>)

    suspend fun getSavedNews(source: String): List<NewsItemDb>

    suspend fun deleteNewsByUrl(url: String)
}