package com.upao.nutrialarm.data.local.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.upao.nutrialarm.data.local.database.entities.DietMealCrossRef
import com.upao.nutrialarm.data.local.database.entities.MealEntity

@Dao
interface MealDao {

    @Query("SELECT * FROM meals WHERE isPreloaded = 1")
    suspend fun getPreloadedMeals(): List<MealEntity>

    @Query("SELECT * FROM meals WHERE mealType = :mealType")
    suspend fun getMealsByType(mealType: String): List<MealEntity>

    @Query("SELECT * FROM meals WHERE id = :mealId")
    suspend fun getMealById(mealId: String): MealEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMeal(meal: MealEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMeals(meals: List<MealEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDietMealCrossRef(crossRef: DietMealCrossRef)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDietMealCrossRefs(crossRefs: List<DietMealCrossRef>)

    @Query("DELETE FROM meals WHERE isPreloaded = 1")
    suspend fun deletePreloadedMeals()

    @Query("DELETE FROM diet_meal_cross_ref")
    suspend fun deleteAllCrossRefs()
}