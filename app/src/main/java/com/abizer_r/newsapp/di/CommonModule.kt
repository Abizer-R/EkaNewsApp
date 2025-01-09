package com.abizer_r.newsapp.di

import android.content.Context
import com.abizer_r.data.util.NetworkConnectionObserver
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CommonModule {

    @Provides
    @Singleton
    fun provideNetworkConnectionObserver(@ApplicationContext context: Context): NetworkConnectionObserver {
        return NetworkConnectionObserver(context)
    }
}