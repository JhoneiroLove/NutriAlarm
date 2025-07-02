package com.upao.nutrialarm.data.local.database.dao

import androidx.room.*
import com.upao.nutrialarm.data.local.database.entities.AlarmEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AlarmDao {

    @Query("SELECT * FROM alarms WHERE userId = :userId")
    suspend fun getAlarmsByUser(userId: String): List<AlarmEntity>

    @Query("SELECT * FROM alarms WHERE userId = :userId")
    fun getAlarmsByUserFlow(userId: String): Flow<List<AlarmEntity>>

    @Query("SELECT * FROM alarms WHERE id = :alarmId")
    suspend fun getAlarmById(alarmId: String): AlarmEntity?

    @Query("SELECT * FROM alarms WHERE userId = :userId AND isEnabled = 1")
    suspend fun getEnabledAlarms(userId: String): List<AlarmEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAlarm(alarm: AlarmEntity)

    @Update
    suspend fun updateAlarm(alarm: AlarmEntity)

    @Delete
    suspend fun deleteAlarm(alarm: AlarmEntity)

    @Query("DELETE FROM alarms WHERE id = :alarmId")
    suspend fun deleteAlarmById(alarmId: String)

    @Query("UPDATE alarms SET isEnabled = :isEnabled WHERE id = :alarmId")
    suspend fun updateAlarmStatus(alarmId: String, isEnabled: Boolean)
}