package com.upao.nutrialarm.data.local.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val id: String,
    val email: String,
    val name: String,
    val age: Int,
    val weight: Double,
    val height: Double,
    val activityLevel: String,
    val anemiaRisk: String,
    val createdAt: Long = System.currentTimeMillis()
)