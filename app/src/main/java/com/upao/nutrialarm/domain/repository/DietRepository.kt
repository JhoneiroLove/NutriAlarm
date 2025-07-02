package com.upao.nutrialarm.domain.repository

import com.upao.nutrialarm.domain.model.AnemiaRisk
import com.upao.nutrialarm.domain.model.Diet
import com.upao.nutrialarm.domain.model.Meal
import com.upao.nutrialarm.domain.model.MealType

interface DietRepository {
    suspend fun getAllDiets(): List<Diet>
    suspend fun getDietsByRiskLevel(riskLevel: AnemiaRisk): List<Diet>
    suspend fun getDietWithMeals(dietId: String): Diet?
    suspend fun getMealsByType(mealType: MealType): List<Meal>
    suspend fun getMealById(mealId: String): Meal?
    suspend fun initializePreloadedData()
}