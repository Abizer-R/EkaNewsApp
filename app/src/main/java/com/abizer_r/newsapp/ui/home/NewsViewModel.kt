package com.abizer_r.newsapp.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abizer_r.data.news.local.NEWS_SOURCE_API
import com.abizer_r.data.news.local.NEWS_SOURCE_USER_SAVED
import com.abizer_r.data.news.local.NewsItemDb
import com.abizer_r.data.news.repository.NewsRepository
import com.abizer_r.data.news.usecase.GetNewsUseCase
import com.abizer_r.data.util.ResultData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class HomeScreenState {
    object Loading : HomeScreenState()
    data class Success(val articles: List<NewsItem>) : HomeScreenState()
    data class Failure(val errorMessage: String? = null) : HomeScreenState()

    fun getNewsList(): List<NewsItem> =
        if (this is Success) articles else emptyList()
}
@HiltViewModel
class NewsViewModel @Inject constructor(
    private val getNewsUseCase: GetNewsUseCase,
    private val newsRepository: NewsRepository  // TODO: define a saveNewsUseCase
) : ViewModel() {

    private val _screenState = MutableStateFlow<HomeScreenState>(HomeScreenState.Loading)
    val screenState: StateFlow<HomeScreenState> = _screenState

    init {
        fetchTopHeadlines()
    }

    fun fetchTopHeadlines() = viewModelScope.launch {
        getNewsUseCase.execute().onEach { result ->
            val newState = when (result) {
                is ResultData.Loading -> HomeScreenState.Loading
                is ResultData.Failed -> HomeScreenState.Failure(result.message)
                is ResultData.Success -> {
                    val articles =
                        result.data?.articles?.map { it.toDomainNewsItem() } ?: emptyList()
                    HomeScreenState.Success(articles)
                }
            }
            _screenState.update { newState }
        }.collect()
    }

    fun saveNews(
        newsId: String?,
        source: String = NEWS_SOURCE_USER_SAVED
    ) = viewModelScope.launch {
        val newsItem = screenState.value.getNewsList().find { it.localId == newsId }
        if (newsItem == null)
            return@launch
        val dbItem = newsItem.toNewsItemDb().copy(source = source)
        newsRepository.saveNews(dbItem)
    }


    private val _savedNewsState = MutableStateFlow<List<NewsItemDb>>(emptyList())
    val savedNewsState: StateFlow<List<NewsItemDb>> = _savedNewsState

    fun fetchSavedNews(
        source: String = NEWS_SOURCE_API
    ) = viewModelScope.launch {
        _savedNewsState.value = newsRepository.getSavedNews(source)
    }
}