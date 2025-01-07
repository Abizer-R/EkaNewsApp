package com.abizer_r.data.util

sealed class ResultData<out T> {
    data class Loading(val nothing: Nothing? = null) : ResultData<Nothing>()
    data class Success<out T>(val data: T) : ResultData<T>()
    data class Failed(val message: String? = null) : ResultData<Nothing>()
}