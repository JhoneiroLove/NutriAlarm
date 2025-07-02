package com.upao.nutrialarm.data.repository

import com.upao.nutrialarm.data.local.database.dao.DietDao
import com.upao.nutrialarm.data.local.database.dao.MealDao
import com.upao.nutrialarm.data.local.database.PreloadedData
import com.upao.nutrialarm.domain.model.*
import com.upao.nutrialarm.domain.repository.DietRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DietRepositoryImpl @Inject constructor(
    private val dietDao: DietDao,
    private val mealDao: MealDao
) : DietRepository {

    override suspend fun getAllDiets(): List<Diet> {
        return dietDao.getAllDiets().map { it.toDomain() }
    }

    override suspend fun getDietsByRiskLevel(riskLevel: AnemiaRisk): List<Diet> {
        return dietDao.getDietsByRiskLevel(riskLevel.name).map { it.toDomain() }
    }

    override suspend fun getDietWithMeals(dietId: String): Diet? {
        val dietWithMeals = dietDao.getDietWithMeals(dietId)
        return dietWithMeals?.let {
            Diet(
                id = it.diet.id,
                name = it.diet.name,
                description = it.diet.description,
                anemiaRiskLevel = AnemiaRisk.valueOf(it.diet.anemiaRiskLevel),
                meals = it.meals.map { meal -> meal.toDomain() },
                ironContent = it.diet.ironContent,
                calories = it.diet.calories
            )
        }
    }

    override suspend fun getMealsByType(mealType: MealType): List<Meal> {
        return mealDao.getMealsByType(mealType.name).map { it.toDomain() }
    }

    override suspend fun getMealById(mealId: String): Meal? {
        return mealDao.getMealById(mealId)?.toDomain()
    }

    // USAR EL PRELOADEDDATA EXISTENTE - SIN DUPLICAR
    override suspend fun initializePreloadedData() {
        // Verificar si ya hay datos
        val existingMeals = mealDao.getMealsByType(MealType.BREAKFAST.name)
        if (existingMeals.isNotEmpty()) return

        // Insertar datos usando PreloadedData existente
        val meals = PreloadedData.getPreloadedMeals()
        val diets = PreloadedData.getPreloadedDiets()
        val crossRefs = PreloadedData.getDietMealCrossRefs()

        mealDao.insertMeals(meals)
        dietDao.insertDiets(diets)
        mealDao.insertDietMealCrossRefs(crossRefs)
    }
}