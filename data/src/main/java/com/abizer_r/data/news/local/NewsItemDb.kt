package com.abizer_r.data.news.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = News_TABLE_NAME)
data class NewsItemDb(
    val id: String,
    val heading: String,
    val description: String,
    val thumbnailUrl: String,
    @PrimaryKey val newsUrl: String,
    val isCached: Boolean = false,
    val isSaved: Boolean = false
)

const val News_TABLE_NAME = "news_items"
const val NEWS_SOURCE_API = "API"
const val NEWS_SOURCE_USER_SAVED = "USER_SAVED"
