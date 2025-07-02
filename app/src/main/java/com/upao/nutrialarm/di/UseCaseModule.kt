package com.upao.nutrialarm.di

import com.upao.nutrialarm.domain.repository.AlarmRepository
import com.upao.nutrialarm.domain.usecase.alarm.CancelAlarmUseCase
import com.upao.nutrialarm.domain.usecase.alarm.GetAlarmsUseCase
import com.upao.nutrialarm.domain.usecase.alarm.SetAlarmUseCase
import com.upao.nutrialarm.domain.usecase.alarm.ToggleAlarmUseCase
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
}