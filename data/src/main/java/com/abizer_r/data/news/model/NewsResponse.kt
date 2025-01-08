package com.abizer_r.data.news.model

data class NewsResponse(
    val articles: List<Article>? = null,
    val status: String? = null,
    val totalResults: Int? = null
)

data class Article(
    val description: String? = null,
    val title: String? = null,
    val url: String? = null,
    val urlToImage: String? = null
)