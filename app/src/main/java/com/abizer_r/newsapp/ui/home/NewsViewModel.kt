package com.abizer_r.newsapp.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abizer_r.data.news.local.NEWS_SOURCE_USER_SAVED
import com.abizer_r.data.news.usecase.GetNewsUseCase
import com.abizer_r.data.news.usecase.SavedNewsUseCase
import com.abizer_r.data.util.ResultData
import com.abizer_r.newsapp.ui.home.model.NewsItem
import com.abizer_r.newsapp.ui.home.model.toDbEntity
import com.abizer_r.newsapp.ui.home.model.toUiModel
import com.abizer_r.data.util.NetworkConnectionObserver
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class HomeScreenState {
    data object Loading : HomeScreenState()
    data class Success(
        val articles: List<NewsItem>,
        val isOldCachedData: Boolean = false,
        val errorMessage: String? = null
    ) : HomeScreenState()
    data class Failure(val errorMessage: String? = null) : HomeScreenState()

    fun getNewsList(): List<NewsItem> =
        if (this is Success) articles else emptyList()
}
@HiltViewModel
class NewsViewModel @Inject constructor(
    private val getNewsUseCase: GetNewsUseCase,
    private val saveNewsUseCase: SavedNewsUseCase,
    private val networkConnectionObserver: NetworkConnectionObserver
) : ViewModel() {

    private val _screenState = MutableStateFlow<HomeScreenState>(HomeScreenState.Loading)
    val screenState: StateFlow<HomeScreenState> = _screenState

    private val _navigateToSavedScreen = MutableStateFlow<Boolean>(false)
    val navigateToSavedScreenState: StateFlow<Boolean> = _navigateToSavedScreen

    val isNetworkAvailable: Boolean get() = networkConnectionObserver.isConnected()

    init {
        fetchTopHeadlines()
    }

    fun fetchTopHeadlines() = viewModelScope.launch {
        getNewsUseCase.fetchTopHeadlines().onEach { result ->
            val newState = when (result) {
                is ResultData.Loading -> HomeScreenState.Loading
                is ResultData.Failed -> HomeScreenState.Failure(result.message)
                is ResultData.Success -> {
                    HomeScreenState.Success(
                        articles = result.data.newsList.map { it.toUiModel() },
                        isOldCachedData = result.data.isOldCachedData,
                        errorMessage = result.data.errorMsg
                    )
                }
            }
            _screenState.update { newState }
        }.collect()
    }

    fun saveNews(
        newsId: String?,
        source: String = NEWS_SOURCE_USER_SAVED
    ) = viewModelScope.launch {
        val newsItem = screenState.value.getNewsList().find { it.id == newsId }
        if (newsItem == null)
            return@launch
        val dbItem = newsItem.toDbEntity().copy(source = source)
        saveNewsUseCase.saveToDb(dbItem)
        _navigateToSavedScreen.update { true }
    }

}