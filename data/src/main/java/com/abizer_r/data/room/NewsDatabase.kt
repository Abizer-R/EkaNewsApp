package com.abizer_r.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.abizer_r.data.news.local.NewsDao
import com.abizer_r.data.news.local.NewsItemDb

@Database(entities = [NewsItemDb::class], version = 1)
abstract class NewsDatabase : RoomDatabase() {
    abstract fun newsDao(): NewsDao
}