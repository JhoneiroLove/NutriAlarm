package com.upao.nutrialarm.di

import android.content.Context
import com.upao.nutrialarm.data.remote.admob.AdMobService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AdMobModule {

    @Provides
    @Singleton
    fun provideAdMobService(@ApplicationContext context: Context): AdMobService {
        return AdMobService(context)
    }
}