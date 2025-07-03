package com.upao.nutrialarm.data.repository

import com.upao.nutrialarm.data.local.database.dao.DailyNutritionSummary
import com.upao.nutrialarm.data.local.database.dao.MealConsumptionDao
import com.upao.nutrialarm.domain.model.MealConsumption
import com.upao.nutrialarm.domain.repository.MealConsumptionRepository
import com.upao.nutrialarm.domain.repository.DietRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MealConsumptionRepositoryImpl @Inject constructor(
    private val mealConsumptionDao: MealConsumptionDao,
    private val dietRepository: DietRepository
) : MealConsumptionRepository {

    override suspend fun getMealConsumptionForDay(userId: String, date: String): List<MealConsumption> {
        return mealConsumptionDao.getMealConsumptionForDay(userId, date).map { it.toDomain() }
    }

    override fun getMealConsumptionForDayFlow(userId: String, date: String): Flow<List<MealConsumption>> {
        return mealConsumptionDao.getMealConsumptionForDayFlow(userId, date).map { list ->
            list.map { it.toDomain() }
        }
    }

    override suspend fun getMealConsumptionByType(userId: String, mealType: String, date: String): List<MealConsumption> {
        return mealConsumptionDao.getMealConsumptionByType(userId, mealType, date).map { it.toDomain() }
    }

    override suspend fun insertMealConsumption(consumption: MealConsumption): Result<MealConsumption> {
        return try {
            mealConsumptionDao.insertMealConsumption(consumption.toEntity())
            Result.success(consumption)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteMealConsumption(consumption: MealConsumption): Result<Unit> {
        return try {
            mealConsumptionDao.deleteMealConsumption(consumption.toEntity())
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteMealConsumptionByType(userId: String, mealType: String, date: String): Result<Unit> {
        return try {
            mealConsumptionDao.deleteMealConsumptionByType(userId, mealType, date)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getDailyNutritionSummary(userId: String, date: String): DailyNutritionSummary? {
        return mealConsumptionDao.getDailyNutritionSummary(userId, date)
    }

    override suspend fun getConsumedMealTypes(userId: String, date: String): List<String> {
        return mealConsumptionDao.getConsumedMealTypes(userId, date)
    }

    override suspend fun markMealAsConsumed(userId: String, mealId: String, mealType: String): Result<MealConsumption> {
        return try {
            val meal = dietRepository.getMealById(mealId)
            if (meal != null) {
                val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
                val consumption = MealConsumption(
                    id = "${userId}_${mealId}_${System.currentTimeMillis()}",
                    userId = userId,
                    mealId = mealId,
                    mealType = meal.mealType,
                    date = today,
                    ironContent = meal.ironContent,
                    calories = meal.calories,
                    vitaminC = meal.vitaminC,
                    folate = meal.folate
                )
                insertMealConsumption(consumption)
            } else {
                Result.failure(Exception("Meal not found"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}