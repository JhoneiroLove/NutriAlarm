package com.upao.nutrialarm.data.local.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "meals")
data class MealEntity(
    @PrimaryKey val id: String,
    val name: String,
    val description: String,
    val ingredients: String, // JSON array as string
    val mealType: String,
    val ironContent: Double,
    val calories: Double,
    val preparationTime: Int,
    val imageUrl: String,
    val vitaminC: Double,
    val folate: Double,
    val isPreloaded: Boolean = true
)