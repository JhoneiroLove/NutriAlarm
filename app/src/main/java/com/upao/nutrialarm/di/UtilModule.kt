package com.upao.nutrialarm.di

import android.content.Context
import com.upao.nutrialarm.util.AlarmManagerUtil
import com.upao.nutrialarm.util.NotificationHelper
import com.upao.nutrialarm.util.PermissionHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UtilModule {

    @Provides
    @Singleton
    fun provideNotificationHelper(@ApplicationContext context: Context): NotificationHelper {
        return NotificationHelper(context)
    }

    @Provides
    @Singleton
    fun provideAlarmManagerUtil(@ApplicationContext context: Context): AlarmManagerUtil {
        return AlarmManagerUtil(context)
    }

    @Provides
    @Singleton
    fun providePermissionHelper(@ApplicationContext context: Context): PermissionHelper {
        return PermissionHelper(context)
    }
}