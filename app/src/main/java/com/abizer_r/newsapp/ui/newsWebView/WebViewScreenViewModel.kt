package com.abizer_r.newsapp.ui.newsWebView

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abizer_r.data.news.usecase.SavedNewsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WebViewScreenViewModel @Inject constructor(
    private val savedNewsUseCase: SavedNewsUseCase
) : ViewModel() {

    private val _isSaved = MutableStateFlow(false)
    val isSaved: StateFlow<Boolean> = _isSaved

    private val _newsUnsaved = MutableStateFlow<Boolean>(false)
    val newsUnsaved: StateFlow<Boolean> = _newsUnsaved

    fun checkIfSaved(newsUrl: String) = viewModelScope.launch {
        val result = savedNewsUseCase.checkNewsSavedByUrl(newsUrl)
        _isSaved.value = result
    }

    fun deleteNews(newsUrl: String) = viewModelScope.launch {
        savedNewsUseCase.deleteNewsByUrl(newsUrl)
        _newsUnsaved.update { true }
        _isSaved.update { false }
    }

    fun resetUnsavedState() {
        _newsUnsaved.update { false }
    }

}
