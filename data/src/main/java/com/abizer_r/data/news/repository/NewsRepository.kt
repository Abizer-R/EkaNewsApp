package com.abizer_r.data.news.repository

import com.abizer_r.data.news.local.NewsItemDb
import com.abizer_r.data.news.model.NewsData
import com.abizer_r.data.util.ResultData
import kotlinx.coroutines.flow.Flow

interface NewsRepository {
    suspend fun getTopHeadlines(): Flow<ResultData<NewsData>>

    suspend fun insertNewsToDb(newsItem: NewsItemDb)

    suspend fun insertNewsItemsToDb(newsItems: List<NewsItemDb>)

    suspend fun getUserSavedNews(): Flow<List<NewsItemDb>>

    suspend fun deleteNewsByUrl(url: String)

    suspend fun checkNewsSavedByUrl(newsUrl: String): Boolean
}