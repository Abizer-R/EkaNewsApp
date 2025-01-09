package com.abizer_r.newsapp.ui.saved

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abizer_r.data.news.local.NEWS_SOURCE_USER_SAVED
import com.abizer_r.data.news.usecase.GetNewsUseCase
import com.abizer_r.data.news.usecase.SavedNewsUseCase
import com.abizer_r.data.util.ResultData
import com.abizer_r.newsapp.ui.home.model.NewsItem
import com.abizer_r.newsapp.ui.home.model.toDbEntity
import com.abizer_r.newsapp.ui.home.model.toUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class SavedNewsScreenState {
    data object Loading : SavedNewsScreenState()
    data class Success(val articles: List<NewsItem>) : SavedNewsScreenState()
    data class Failure(val errorMessage: String? = null) : SavedNewsScreenState()

    fun getNewsList(): List<NewsItem> =
        if (this is Success) articles else emptyList()
}
@HiltViewModel
class SavedNewsViewModel @Inject constructor(
    private val getNewsUseCase: GetNewsUseCase,
    private val savedNewsUseCase: SavedNewsUseCase
) : ViewModel() {

    private val _screenState = MutableStateFlow<SavedNewsScreenState>(SavedNewsScreenState.Loading)
    val screenState: StateFlow<SavedNewsScreenState> = _screenState

    init {
        fetchSavedNews()
    }

    fun fetchSavedNews() = viewModelScope.launch {
        getNewsUseCase.fetchUserSavedNews().onEach { result ->
            val newState = when (result) {
                is ResultData.Loading -> SavedNewsScreenState.Loading
                is ResultData.Failed -> SavedNewsScreenState.Failure(result.message)
                is ResultData.Success -> {
                    val newsItems = result.data.map { it.toUiModel() }
                    SavedNewsScreenState.Success(newsItems)
                }
            }
            _screenState.update { newState }
        }.collect()
    }

    fun deleteSavedNews(item: NewsItem) = viewModelScope.launch {
        savedNewsUseCase.unSaveNews(item.newsUrl)
    }

    fun saveNews(
        newsId: String?,
        source: String = NEWS_SOURCE_USER_SAVED
    ) = viewModelScope.launch {
        val newsItem = screenState.value.getNewsList().find { it.id == newsId }
        if (newsItem == null)
            return@launch
        val dbItem = newsItem.toDbEntity().copy()
        savedNewsUseCase.markNewsAsSaved(dbItem)
    }
}