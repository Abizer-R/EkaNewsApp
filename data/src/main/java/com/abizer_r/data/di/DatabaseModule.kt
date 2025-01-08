package com.abizer_r.data.di

import android.content.Context
import androidx.room.Room
import com.abizer_r.data.news.local.NewsDao
import com.abizer_r.data.room.NewsDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideNewsDatabase(
        @ApplicationContext context: Context
    ): NewsDatabase {
        return Room.databaseBuilder(
            context,
            NewsDatabase::class.java,
            "news_database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideNewsDao(database: NewsDatabase): NewsDao {
        return database.newsDao()
    }
}