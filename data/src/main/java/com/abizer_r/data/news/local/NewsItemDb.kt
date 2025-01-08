package com.abizer_r.data.news.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = News_TABLE_NAME)
data class NewsItemDb(
    @PrimaryKey val id: String,
    val heading: String,
    val description: String,
    val thumbnailUrl: String,
    val newsUrl: String,
    val source: String = NEWS_SOURCE_API
)

const val News_TABLE_NAME = "news_items"
const val NEWS_SOURCE_API = "API"
const val NEWS_SOURCE_USER_SAVED = "USER_SAVED"