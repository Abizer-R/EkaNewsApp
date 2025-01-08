package com.abizer_r.data.news.usecase

import com.abizer_r.data.news.local.NEWS_SOURCE_USER_SAVED
import com.abizer_r.data.news.local.NewsItemDb
import com.abizer_r.data.news.model.NewsItemApi
import com.abizer_r.data.news.repository.NewsRepository
import com.abizer_r.data.util.ResultData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetNewsUseCase @Inject constructor(
    private val repository: NewsRepository
) {
    fun fetchTopHeadlines(): Flow<ResultData<List<NewsItemApi>>> = flow {
        emit(ResultData.Loading())
        // TODO: add safe API call helper
        val response = repository.getTopHeadlines()
        val newsItems = response?.articles ?: emptyList()
        emit(ResultData.Success(newsItems))
    }.catch { e ->
        emit(ResultData.Failed(message = e.localizedMessage))
    }.flowOn(Dispatchers.IO)


    fun fetchSavedNews(): Flow<ResultData<List<NewsItemDb>>> = flow {
        emit(ResultData.Loading())
        val articles = repository.getSavedNews(source = NEWS_SOURCE_USER_SAVED)
        emit(ResultData.Success(articles))
    }.catch { e ->
        emit(ResultData.Failed(message = e.localizedMessage))
    }.flowOn(Dispatchers.IO)
}
