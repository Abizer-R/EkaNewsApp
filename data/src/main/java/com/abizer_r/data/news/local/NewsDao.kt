package com.abizer_r.data.news.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface NewsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNewsItems(newsItems: List<NewsItemDb>)

    @Query("SELECT * from $News_TABLE_NAME WHERE source = :source")
    suspend fun getAllNewsItems(source: String): List<NewsItemDb>

    @Query("DELETE FROM news_items WHERE newsUrl = :url")
    suspend fun deleteNewsByUrl(url: String)
}