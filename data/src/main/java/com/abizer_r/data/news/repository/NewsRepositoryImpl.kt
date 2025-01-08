package com.abizer_r.data.news.repository

import com.abizer_r.data.news.local.NewsDao
import com.abizer_r.data.news.local.NewsItemDb
import com.abizer_r.data.news.model.NewsResponse
import com.abizer_r.data.news.remote.NewsApiService
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class NewsRepositoryImpl @Inject constructor(
    private val newsApiService: NewsApiService,
    private val newsDao: NewsDao,
) : NewsRepository {

    override suspend fun getTopHeadlines(): NewsResponse? {
        return newsApiService.getTopHeadlines()
    }

    override suspend fun checkNewsSavedByUrl(newsUrl: String): Boolean {
        return newsDao.getNewsByUrl(newsUrl) != null
    }

    override suspend fun saveNewsToDb(newsItem: NewsItemDb) {
        saveNewsItemsToDb(listOf(newsItem))
    }

    override suspend fun saveNewsItemsToDb(newsItems: List<NewsItemDb>) {
        newsDao.insertNewsItems(newsItems)
    }

    override suspend fun getSavedNews(source: String): Flow<List<NewsItemDb>> {
        return newsDao.getAllNewsItems(source = source)
    }

    override suspend fun deleteNewsByUrl(url: String) {
        newsDao.deleteNewsByUrl(url)
    }
}