package com.upao.nutrialarm.data.local.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "meal_consumption")
data class MealConsumptionEntity(
    @PrimaryKey val id: String,
    val userId: String,
    val mealId: String,
    val mealType: String,
    val consumedAt: Long = System.currentTimeMillis(),
    val date: String, // "2025-07-02" format
    val ironContent: Double,
    val calories: Double,
    val vitaminC: Double,
    val folate: Double
)