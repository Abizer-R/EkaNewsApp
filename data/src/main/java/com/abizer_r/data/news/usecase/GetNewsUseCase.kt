package com.abizer_r.data.news.usecase

import com.abizer_r.data.news.local.NewsItemDb
import com.abizer_r.data.news.model.NewsData
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
    suspend fun fetchTopHeadlines(): Flow<ResultData<NewsData>> =
        repository.getTopHeadlines()


    fun fetchUserSavedNews(): Flow<ResultData<List<NewsItemDb>>> = flow {
        emit(ResultData.Loading())
        repository.getUserSavedNews().collect { list ->
            emit(ResultData.Success(list))
        }
    }.catch { e ->
        emit(ResultData.Failed(message = e.localizedMessage))
    }.flowOn(Dispatchers.IO)
}
