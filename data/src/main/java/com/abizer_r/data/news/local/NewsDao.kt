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

    @Query("SELECT * FROM $News_TABLE_NAME WHERE newsUrl = :newsUrl LIMIT 1")
    suspend fun getNewsByUrl(newsUrl: String): NewsItemDb?

    @Query("SELECT * from $News_TABLE_NAME WHERE isCached = 1")
    fun getAllCachedNews(): Flow<List<NewsItemDb>>

    @Query("SELECT * from $News_TABLE_NAME WHERE isSaved = 1")
    fun getAllSavedNews(): Flow<List<NewsItemDb>>

    @Query("DELETE FROM $News_TABLE_NAME WHERE newsUrl = :url")
    suspend fun deleteNewsByUrl(url: String)

    @Query("DELETE FROM $News_TABLE_NAME WHERE isCached = 1 AND isSaved = 0")
    suspend fun deleteAllCachedOnlyNews()
}