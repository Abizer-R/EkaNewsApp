package com.abizer_r.data.news.mappers

import com.abizer_r.data.news.local.NewsItemDb
import com.abizer_r.data.news.model.NewsItemApi
import java.util.UUID

fun NewsItemApi.toDbEntity(): NewsItemDb {
    return NewsItemDb(
        id = id,
        heading = this.title ?: "",
        description = this.description ?: "",
        thumbnailUrl = this.urlToImage ?: "",
        newsUrl = this.url ?: ""
    )
}

fun NewsItemDb.toApiModel(): NewsItemApi {
    return NewsItemApi(
        id = id,
        title = this.heading,
        description = description,
        urlToImage = thumbnailUrl,
        url = newsUrl
    )
}