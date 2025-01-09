package com.abizer_r.data.news.usecase

import com.abizer_r.data.news.local.NewsItemDb
import com.abizer_r.data.news.repository.NewsRepository
import javax.inject.Inject

class SavedNewsUseCase @Inject constructor(
    private val repository: NewsRepository
) {
    suspend fun markNewsAsSaved(newsItem: NewsItemDb) {
        repository.markNewsAsSaved(newsItem)
    }

    suspend fun unSaveNews(url: String) {
        repository.unSaveNews(url)
    }

    suspend fun checkNewsSavedByUrl(newsUrl: String): Boolean {
        return repository.checkNewsSavedByUrl(newsUrl)
    }
}