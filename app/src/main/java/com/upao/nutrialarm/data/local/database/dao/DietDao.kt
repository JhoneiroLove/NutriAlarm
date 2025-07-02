package com.upao.nutrialarm.data.local.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.upao.nutrialarm.data.local.database.entities.DietEntity
import com.upao.nutrialarm.data.local.database.relations.DietWithMeals

@Dao
interface DietDao {

    @Query("SELECT * FROM diets")
    suspend fun getAllDiets(): List<DietEntity>

    @Query("SELECT * FROM diets WHERE anemiaRiskLevel = :riskLevel")
    suspend fun getDietsByRiskLevel(riskLevel: String): List<DietEntity>

    @Transaction
    @Query("SELECT * FROM diets WHERE id = :dietId")
    suspend fun getDietWithMeals(dietId: String): DietWithMeals?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDiet(diet: DietEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDiets(diets: List<DietEntity>)

    @Query("DELETE FROM diets WHERE isPreloaded = 1")
    suspend fun deletePreloadedDiets()
}
