package com.abizer_r.data.news.repository

import com.abizer_r.data.news.local.NEWS_SOURCE_API
import com.abizer_r.data.news.local.NEWS_SOURCE_USER_SAVED
import com.abizer_r.data.news.local.NewsDao
import com.abizer_r.data.news.local.NewsItemDb
import com.abizer_r.data.news.mappers.toDbEntity
import com.abizer_r.data.news.model.NewsData
import com.abizer_r.data.news.model.NewsItemApi
import com.abizer_r.data.news.remote.NewsApiService
import com.abizer_r.data.util.ResultData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class NewsRepositoryImpl @Inject constructor(
    private val newsApiService: NewsApiService,
    private val newsDao: NewsDao,
) : NewsRepository {

    override suspend fun getTopHeadlines(): Flow<ResultData<NewsData>> = flow {
        emit(ResultData.Loading())

        val remoteNewsResult = try {
            val remoteNewsList = newsApiService.getTopHeadlines()?.articles
            if (!remoteNewsList.isNullOrEmpty()) {
                newsDao.deleteAllNewsBySource(NEWS_SOURCE_API)
                val newDbList = remoteNewsList.map { it.toDbEntity(NEWS_SOURCE_API) }
                newsDao.insertNewsItems(newDbList)

                ResultData.Success(NewsData(newDbList, isOldCachedData = false))
            } else {
                ResultData.Failed("No remote data available")
            }
        } catch (e: Exception) {
            ResultData.Failed(e.localizedMessage ?: "Failed to fetch remote news")
        }

        if (remoteNewsResult is ResultData.Success) {
            emit(remoteNewsResult)
            return@flow
        }

        // Fallback to local data if remote fetch failed
        val localNewsList = getLocalNews()
        if (!localNewsList.isNullOrEmpty()) {
            val remoteError = (remoteNewsResult as? ResultData.Failed)?.message
            val newsData =
                NewsData(localNewsList, isOldCachedData = true, errorMsg = remoteError)
            emit(ResultData.Success(newsData))
        } else {
            emit(ResultData.Failed("No data available"))
        }
    }

    private suspend fun getLocalNews(): List<NewsItemDb>? {
        return try {
            newsDao.getAllNewsItems(source = NEWS_SOURCE_API).first()
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun checkNewsSavedByUrl(newsUrl: String): Boolean {
        return newsDao.getNewsByUrl(newsUrl) != null
    }

    override suspend fun insertNewsToDb(newsItem: NewsItemDb) {
        insertNewsItemsToDb(listOf(newsItem))
    }

    override suspend fun insertNewsItemsToDb(newsItems: List<NewsItemDb>) {
        newsDao.insertNewsItems(newsItems)
    }

    override suspend fun getUserSavedNews(): Flow<List<NewsItemDb>> {
        return newsDao.getAllNewsItems(source = NEWS_SOURCE_USER_SAVED)
    }

    override suspend fun deleteNewsByUrl(url: String) {
        newsDao.deleteNewsByUrl(url)
    }
}