package com.abizer_r.data.news.usecase

import com.abizer_r.data.news.model.NewsResponse
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
    fun execute(): Flow<ResultData<NewsResponse?>> = flow {
        emit(ResultData.Loading())
        // TODO: add safe API call helper
        val articles = repository.getTopHeadlines()
        emit(ResultData.Success(articles))
    }.catch { e ->
        emit(ResultData.Failed(message = e.localizedMessage))
    }.flowOn(Dispatchers.IO)
}
