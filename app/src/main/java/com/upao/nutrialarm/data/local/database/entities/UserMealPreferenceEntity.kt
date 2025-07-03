package com.upao.nutrialarm.data.local.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_meal_preferences")
data class UserMealPreferenceEntity(
    @PrimaryKey val id: String,
    val userId: String,
    val mealType: String, // MealType as String
    val selectedMealId: String,
    val timeSlot: String, // "06:30"
    val isActive: Boolean = true,
    val reminderEnabled: Boolean = true,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)