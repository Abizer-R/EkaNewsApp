package com.abizer_r.newsapp.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abizer_r.data.news.repository.NewsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val repository: NewsRepository
) : ViewModel() {

    private val _articles = MutableStateFlow<List<NewsItem>>(emptyList())
    val articles: StateFlow<List<NewsItem>> = _articles

    init {
        fetchTopHeadlines()
    }

    private fun fetchTopHeadlines() {
        viewModelScope.launch {
            try {
                val response = repository.getTopHeadlines()
                val domainItems = response?.articles?.map {
                    it.toDomainNewsItem()
                } ?: emptyList()
                _articles.update { domainItems }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}