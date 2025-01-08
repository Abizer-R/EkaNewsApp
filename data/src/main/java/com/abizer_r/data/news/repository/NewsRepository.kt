package com.abizer_r.data.news.repository

import com.abizer_r.data.news.local.NewsItemDb
import com.abizer_r.data.news.model.NewsResponse

interface NewsRepository {
    suspend fun getTopHeadlines(): NewsResponse?

    suspend fun saveNews(newsItem: NewsItemDb)

    suspend fun getSavedNews(source: String): List<NewsItemDb>
}