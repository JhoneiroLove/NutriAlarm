package com.upao.nutrialarm.di

import android.content.Context
import androidx.room.Room
import com.upao.nutrialarm.data.local.database.AppDatabase
import com.upao.nutrialarm.data.local.database.dao.*
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
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            "nutrialarm_database"
        )
            .fallbackToDestructiveMigration() // Para desarrollo - quitar en producción
            .build()
    }

    @Provides
    fun provideUserDao(database: AppDatabase): UserDao = database.userDao()

    @Provides
    fun provideDietDao(database: AppDatabase): DietDao = database.dietDao()

    @Provides
    fun provideMealDao(database: AppDatabase): MealDao = database.mealDao()

    @Provides
    fun provideAlarmDao(database: AppDatabase): AlarmDao = database.alarmDao()

    @Provides
    fun provideUserMealPreferenceDao(database: AppDatabase): UserMealPreferenceDao =
        database.userMealPreferenceDao()
}