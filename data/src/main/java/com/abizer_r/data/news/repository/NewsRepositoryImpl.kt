package com.abizer_r.data.news.repository

import com.abizer_r.data.news.local.NewsDao
import com.abizer_r.data.news.local.NewsItemDb
import com.abizer_r.data.news.mappers.toDbEntity
import com.abizer_r.data.news.model.NewsData
import com.abizer_r.data.news.remote.NewsApiService
import com.abizer_r.data.util.NetworkConnectionObserver
import com.abizer_r.data.util.ResultData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class NewsRepositoryImpl @Inject constructor(
    private val newsApiService: NewsApiService,
    private val newsDao: NewsDao,
    private val networkConnectionObserver: NetworkConnectionObserver
) : NewsRepository {

    override suspend fun getTopHeadlines(): Flow<ResultData<NewsData>> = flow {
        emit(ResultData.Loading())

        val remoteNewsResult = try {
            val remoteNewsList = newsApiService.getTopHeadlines()?.articles
            if (!remoteNewsList.isNullOrEmpty()) {
                newsDao.deleteAllCachedOnlyNews()

                val newDbList = remoteNewsList.map { remoteItem ->
                    // Check if the item is already marked as "saved"
                    val isSaved = checkNewsSavedByUrl(remoteItem.url ?: "")
                    remoteItem.toDbEntity(
                        isCached = true,
                        isSaved = isSaved
                    )
                }

                insertNewsItemsToDb(newDbList)

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

        val isNetworkConnected = networkConnectionObserver.isNetworkAvailable.first()
        val remoteError = if (isNetworkConnected) {
            (remoteNewsResult as? ResultData.Failed)?.message
        } else "No internet connection"

        // Fallback to local data if remote fetch failed
        val localNewsList = getLocalCachedNews()
        if (!localNewsList.isNullOrEmpty()) {

            val newsData =
                NewsData(localNewsList, isOldCachedData = true, errorMsg = remoteError)
            emit(ResultData.Success(newsData))
        } else {
            emit(ResultData.Failed(remoteError))
        }
    }

    private suspend fun getLocalCachedNews(): List<NewsItemDb>? {
        return try {
            newsDao.getAllCachedNews().first()
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun checkNewsSavedByUrl(newsUrl: String): Boolean {
        val news = newsDao.getNewsByUrl(newsUrl)
        return news?.isSaved == true
    }

    private suspend fun checkNewsCachedByUrl(newsUrl: String): Boolean {
        val news = newsDao.getNewsByUrl(newsUrl)
        return news?.isCached == true
    }

    override suspend fun markNewsAsSaved(newsItem: NewsItemDb) {
        // Check if the item is already marked as "cached"
        val isCached = checkNewsCachedByUrl(newsItem.newsUrl)
        insertNewsToDb(
            newsItem.copy(
                isCached = isCached,
                isSaved = true
            )
        )
    }

    override suspend fun insertNewsToDb(newsItem: NewsItemDb) {
        insertNewsItemsToDb(listOf(newsItem))
    }

    override suspend fun insertNewsItemsToDb(newsItems: List<NewsItemDb>) {
        newsDao.insertNewsItems(newsItems)
    }

    override suspend fun getUserSavedNews(): Flow<List<NewsItemDb>> {
        val savedNews = newsDao.getAllSavedNews()
        return savedNews
    }

    override suspend fun deleteNewsByUrl(url: String) {
        newsDao.deleteNewsByUrl(url)
    }
}