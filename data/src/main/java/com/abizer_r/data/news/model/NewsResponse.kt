package com.abizer_r.data.news.model

import java.util.UUID

data class NewsResponse(
    val newsItems: List<NewsItemApi>? = null,
    val status: String? = null,
    val totalResults: Int? = null
)

data class NewsItemApi(
    val id: String = UUID.randomUUID().toString(),
    val description: String? = null,
    val title: String? = null,
    val url: String? = null,
    val urlToImage: String? = null
)