package com.abizer_r.data.news.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface NewsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNewsItems(newsItems: List<NewsItemDb>)

    @Query("SELECT * FROM news_items WHERE newsUrl = :newsUrl LIMIT 1")
    suspend fun getNewsByUrl(newsUrl: String): NewsItemDb?

    @Query("SELECT * from $News_TABLE_NAME WHERE source = :source")
    fun getAllNewsItems(source: String): Flow<List<NewsItemDb>>

    @Query("DELETE FROM news_items WHERE newsUrl = :url")
    suspend fun deleteNewsByUrl(url: String)
}