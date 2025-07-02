package com.upao.nutrialarm.data.local.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "alarms")
data class AlarmEntity(
    @PrimaryKey val id: String,
    val userId: String,
    val mealType: String,
    val time: String, // HH:mm format
    val isEnabled: Boolean = true,
    val days: String, // JSON array as string for DayOfWeek
    val reminderMessage: String = "",
    val createdAt: Long = System.currentTimeMillis()
)