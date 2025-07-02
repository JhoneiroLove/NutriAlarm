package com.upao.nutrialarm.data.local.database.dao

import androidx.room.*
import com.upao.nutrialarm.data.local.database.entities.DietMealCrossRef

@Dao
interface DietMealCrossRefDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCrossRef(crossRef: DietMealCrossRef)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCrossRefs(crossRefs: List<DietMealCrossRef>)

    @Delete
    suspend fun deleteCrossRef(crossRef: DietMealCrossRef)

    @Query("DELETE FROM diet_meal_cross_ref WHERE dietId = :dietId")
    suspend fun deleteCrossRefsByDiet(dietId: String)

    @Query("DELETE FROM diet_meal_cross_ref WHERE mealId = :mealId")
    suspend fun deleteCrossRefsByMeal(mealId: String)

    @Query("SELECT * FROM diet_meal_cross_ref WHERE dietId = :dietId")
    suspend fun getCrossRefsByDiet(dietId: String): List<DietMealCrossRef>
}