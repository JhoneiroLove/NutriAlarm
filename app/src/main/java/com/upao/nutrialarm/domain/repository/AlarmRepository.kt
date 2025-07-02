package com.upao.nutrialarm.domain.repository

import com.upao.nutrialarm.domain.model.Alarm
import kotlinx.coroutines.flow.Flow

interface AlarmRepository {
    suspend fun getAlarmsByUser(userId: String): List<Alarm>
    fun getAlarmsByUserFlow(userId: String): Flow<List<Alarm>>
    suspend fun getAlarmById(alarmId: String): Alarm?
    suspend fun saveAlarm(alarm: Alarm): Result<Alarm>
    suspend fun updateAlarm(alarm: Alarm): Result<Alarm>
    suspend fun deleteAlarm(alarm: Alarm): Result<Unit>
    suspend fun toggleAlarm(alarmId: String, enabled: Boolean): Result<Unit>
}