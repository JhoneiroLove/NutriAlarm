package com.upao.nutrialarm.di

import com.upao.nutrialarm.data.local.preferences.PreferencesManager
import com.upao.nutrialarm.data.remote.firebase.AuthService
import com.upao.nutrialarm.domain.repository.AlarmRepository
import com.upao.nutrialarm.domain.repository.UserMealPreferenceRepository
import com.upao.nutrialarm.domain.repository.MealConsumptionRepository
import com.upao.nutrialarm.domain.repository.UserRepository
import com.upao.nutrialarm.domain.usecase.alarm.*
import com.upao.nutrialarm.domain.usecase.auth.LogoutUseCase
import com.upao.nutrialarm.domain.usecase.meal.*
import com.upao.nutrialarm.domain.usecase.user.GetCurrentUserUseCase
import com.upao.nutrialarm.util.AlarmManagerUtil
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    @Singleton
    fun provideLogoutUseCase(
        authService: AuthService,
        preferencesManager: PreferencesManager
    ): LogoutUseCase {
        return LogoutUseCase(authService, preferencesManager)
    }

    // USER USE CASES
    @Provides
    @Singleton
    fun provideGetCurrentUserUseCase(
        userRepository: UserRepository
    ): GetCurrentUserUseCase {
        return GetCurrentUserUseCase(userRepository)
    }

    // ALARM USE CASES
    @Provides
    @Singleton
    fun provideSetAlarmUseCase(
        alarmRepository: AlarmRepository,
        alarmManagerUtil: AlarmManagerUtil
    ): SetAlarmUseCase {
        return SetAlarmUseCase(alarmRepository, alarmManagerUtil)
    }

    @Provides
    @Singleton
    fun provideCancelAlarmUseCase(
        alarmRepository: AlarmRepository,
        alarmManagerUtil: AlarmManagerUtil
    ): CancelAlarmUseCase {
        return CancelAlarmUseCase(alarmRepository, alarmManagerUtil)
    }

    @Provides
    @Singleton
    fun provideGetAlarmsUseCase(
        alarmRepository: AlarmRepository
    ): GetAlarmsUseCase {
        return GetAlarmsUseCase(alarmRepository)
    }

    @Provides
    @Singleton
    fun provideToggleAlarmUseCase(
        alarmRepository: AlarmRepository,
        alarmManagerUtil: AlarmManagerUtil
    ): ToggleAlarmUseCase {
        return ToggleAlarmUseCase(alarmRepository, alarmManagerUtil)
    }

    // MEAL PREFERENCE USE CASES
    @Provides
    @Singleton
    fun provideGetUserMealPreferencesUseCase(
        repository: UserMealPreferenceRepository
    ): GetUserMealPreferencesUseCase {
        return GetUserMealPreferencesUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideSaveUserMealPreferenceUseCase(
        repository: UserMealPreferenceRepository
    ): SaveUserMealPreferenceUseCase {
        return SaveUserMealPreferenceUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideGetUserMealPreferenceByTypeUseCase(
        repository: UserMealPreferenceRepository
    ): GetUserMealPreferenceByTypeUseCase {
        return GetUserMealPreferenceByTypeUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideDeleteUserMealPreferenceUseCase(
        repository: UserMealPreferenceRepository
    ): DeleteUserMealPreferenceUseCase {
        return DeleteUserMealPreferenceUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideInitializeDefaultPreferencesUseCase(
        repository: UserMealPreferenceRepository
    ): InitializeDefaultPreferencesUseCase {
        return InitializeDefaultPreferencesUseCase(repository)
    }

    // USE CASES PARA PROGRESO DINÁMICO
    @Provides
    @Singleton
    fun provideGetNextMealUseCase(
        mealConsumptionRepository: MealConsumptionRepository
    ): GetNextMealUseCase {
        return GetNextMealUseCase(mealConsumptionRepository)
    }

    @Provides
    @Singleton
    fun provideGetDailyProgressUseCase(
        mealConsumptionRepository: MealConsumptionRepository,
        userRepository: UserRepository
    ): GetDailyProgressUseCase {
        return GetDailyProgressUseCase(mealConsumptionRepository, userRepository)
    }
}