package com.abizer_r.data.news.model

import com.abizer_r.data.news.local.NewsItemDb

data class NewsData (
    val newsList: List<NewsItemDb>,
    val isOldCachedData: Boolean,
    val errorMsg: String? = null
)