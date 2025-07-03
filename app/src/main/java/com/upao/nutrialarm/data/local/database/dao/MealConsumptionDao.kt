package com.upao.nutrialarm.data.local.database.dao

import androidx.room.*
import com.upao.nutrialarm.data.local.database.entities.MealConsumptionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MealConsumptionDao {

    @Query("SELECT * FROM meal_consumption WHERE userId = :userId AND date = :date ORDER BY consumedAt DESC")
    suspend fun getMealConsumptionForDay(userId: String, date: String): List<MealConsumptionEntity>

    @Query("SELECT * FROM meal_consumption WHERE userId = :userId AND date = :date ORDER BY consumedAt DESC")
    fun getMealConsumptionForDayFlow(userId: String, date: String): Flow<List<MealConsumptionEntity>>

    @Query("SELECT * FROM meal_consumption WHERE userId = :userId AND mealType = :mealType AND date = :date")
    suspend fun getMealConsumptionByType(userId: String, mealType: String, date: String): List<MealConsumptionEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMealConsumption(consumption: MealConsumptionEntity)

    @Delete
    suspend fun deleteMealConsumption(consumption: MealConsumptionEntity)

    @Query("DELETE FROM meal_consumption WHERE userId = :userId AND mealType = :mealType AND date = :date")
    suspend fun deleteMealConsumptionByType(userId: String, mealType: String, date: String)

    @Query("""
        SELECT SUM(ironContent) as totalIron,
               SUM(calories) as totalCalories,
               SUM(vitaminC) as totalVitaminC,
               SUM(folate) as totalFolate,
               COUNT(*) as totalMeals
        FROM meal_consumption 
        WHERE userId = :userId AND date = :date
    """)
    suspend fun getDailyNutritionSummary(userId: String, date: String): DailyNutritionSummary?

    @Query("SELECT DISTINCT mealType FROM meal_consumption WHERE userId = :userId AND date = :date")
    suspend fun getConsumedMealTypes(userId: String, date: String): List<String>
}

data class DailyNutritionSummary(
    val totalIron: Double,
    val totalCalories: Double,
    val totalVitaminC: Double,
    val totalFolate: Double,
    val totalMeals: Int
)