package com.upao.nutrialarm.domain.repository

import com.upao.nutrialarm.domain.model.MealConsumption
import com.upao.nutrialarm.data.local.database.dao.DailyNutritionSummary
import kotlinx.coroutines.flow.Flow

interface MealConsumptionRepository {
    suspend fun getMealConsumptionForDay(userId: String, date: String): List<MealConsumption>
    fun getMealConsumptionForDayFlow(userId: String, date: String): Flow<List<MealConsumption>>
    suspend fun getMealConsumptionByType(userId: String, mealType: String, date: String): List<MealConsumption>
    suspend fun insertMealConsumption(consumption: MealConsumption): Result<MealConsumption>
    suspend fun deleteMealConsumption(consumption: MealConsumption): Result<Unit>
    suspend fun saveMealConsumption(consumption: MealConsumption): Result<MealConsumption>
    suspend fun deleteMealConsumptionByType(userId: String, mealType: String, date: String): Result<Unit>
    suspend fun getDailyNutritionSummary(userId: String, date: String): DailyNutritionSummary?
    suspend fun getConsumedMealTypes(userId: String, date: String): List<String>
    suspend fun markMealAsConsumed(userId: String, mealId: String, mealType: String): Result<MealConsumption>
}