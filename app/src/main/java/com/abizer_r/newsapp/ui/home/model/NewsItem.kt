package com.abizer_r.newsapp.ui.home.model

import com.abizer_r.data.news.local.NewsItemDb
import com.abizer_r.data.news.model.NewsItemApi

data class NewsItem(
    val id: String,
    val heading: String,
    val description: String,
    val thumbnailUrl: String,
    val newsUrl: String
)



fun NewsItemApi.toUiModel(): NewsItem {
    return NewsItem(
        id = id,
        heading = this.title ?: "",
        description = this.description ?: "",
        thumbnailUrl = this.urlToImage ?: "",
        newsUrl = this.url ?: ""
    )
}

fun NewsItemDb.toUiModel(): NewsItem {
    return NewsItem(
        id = id,
        heading = this.heading,
        description = description,
        thumbnailUrl = thumbnailUrl,
        newsUrl = newsUrl
    )
}

fun NewsItem.toDbEntity(): NewsItemDb {
    return NewsItemDb(
        id = this.id,
        heading = this.heading,
        description = description,
        thumbnailUrl = thumbnailUrl,
        newsUrl = newsUrl
    )
}

