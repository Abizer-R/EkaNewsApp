package com.abizer_r.data.news.repository

import com.abizer_r.data.news.model.NewsResponse

interface NewsRepository {
    suspend fun getTopHeadlines(): NewsResponse?
}