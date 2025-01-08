package com.abizer_r.data.news.usecase

import com.abizer_r.data.news.local.NewsItemDb
import com.abizer_r.data.news.repository.NewsRepository
import javax.inject.Inject

class SavedNewsUseCase @Inject constructor(
    private val repository: NewsRepository
) {
    suspend fun saveToDb(newsItem: NewsItemDb) {
        repository.saveNewsToDb(newsItem)
    }

    suspend fun deleteNews(item: NewsItemDb) {
        repository.deleteNewsByUrl(item.newsUrl)
    }
}