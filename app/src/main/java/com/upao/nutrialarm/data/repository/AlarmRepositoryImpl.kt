package com.upao.nutrialarm.data.repository

import com.upao.nutrialarm.data.local.database.dao.AlarmDao
import com.upao.nutrialarm.domain.model.Alarm
import com.upao.nutrialarm.domain.repository.AlarmRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AlarmRepositoryImpl @Inject constructor(
    private val alarmDao: AlarmDao
) : AlarmRepository {

    override suspend fun getAlarmsByUser(userId: String): List<Alarm> {
        return alarmDao.getAlarmsByUser(userId).map { it.toDomain() }
    }

    override fun getAlarmsByUserFlow(userId: String): Flow<List<Alarm>> {
        return alarmDao.getAlarmsByUserFlow(userId).map { list ->
            list.map { it.toDomain() }
        }
    }

    override suspend fun getAlarmById(alarmId: String): Alarm? {
        return alarmDao.getAlarmById(alarmId)?.toDomain()
    }

    override suspend fun saveAlarm(alarm: Alarm): Result<Alarm> {
        return try {
            alarmDao.insertAlarm(alarm.toEntity())
            Result.success(alarm)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateAlarm(alarm: Alarm): Result<Alarm> {
        return try {
            alarmDao.updateAlarm(alarm.toEntity())
            Result.success(alarm)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteAlarm(alarm: Alarm): Result<Unit> {
        return try {
            alarmDao.deleteAlarm(alarm.toEntity())
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun toggleAlarm(alarmId: String, enabled: Boolean): Result<Unit> {
        return try {
            alarmDao.updateAlarmStatus(alarmId, enabled)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}