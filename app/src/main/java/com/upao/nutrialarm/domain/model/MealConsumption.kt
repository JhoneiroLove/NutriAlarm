package com.upao.nutrialarm.domain.model

data class MealConsumption(
    val id: String,
    val userId: String,
    val mealId: String,
    val mealType: MealType,
    val consumedAt: Long = System.currentTimeMillis(),
    val date: String,
    val ironContent: Double,
    val calories: Double,
    val vitaminC: Double,
    val folate: Double
)