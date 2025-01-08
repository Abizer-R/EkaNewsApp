package com.abizer_r.newsapp.ui.home

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
}
@HiltViewModel
class NewsViewModel @Inject constructor(
    private val getNewsUseCase: GetNewsUseCase
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
}