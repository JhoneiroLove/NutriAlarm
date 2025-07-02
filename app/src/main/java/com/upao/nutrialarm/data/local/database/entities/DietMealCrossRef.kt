package com.upao.nutrialarm.data.local.database.entities

import androidx.room.Entity

@Entity(
    tableName = "diet_meal_cross_ref",
    primaryKeys = ["dietId", "mealId"]
)
data class DietMealCrossRef(
    val dietId: String,
    val mealId: String
)
