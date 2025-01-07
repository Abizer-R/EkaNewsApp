package com.abizer_r.data.news.repository

import com.abizer_r.data.news.model.NewsResponse
import com.abizer_r.data.news.remote.NewsApiService
import javax.inject.Inject

class NewsRepositoryImpl @Inject constructor(
    private val newsApiService: NewsApiService
) : NewsRepository {

    override suspend fun getTopHeadlines(): NewsResponse? {
        return newsApiService.getTopHeadlines()
    }

}