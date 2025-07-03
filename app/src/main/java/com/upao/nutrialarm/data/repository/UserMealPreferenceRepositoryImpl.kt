package com.upao.nutrialarm.data.repository

import com.upao.nutrialarm.data.local.database.dao.UserMealPreferenceDao
import com.upao.nutrialarm.domain.model.MealType
import com.upao.nutrialarm.domain.model.UserMealPreference
import com.upao.nutrialarm.domain.repository.UserMealPreferenceRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserMealPreferenceRepositoryImpl @Inject constructor(
    private val dao: UserMealPreferenceDao
) : UserMealPreferenceRepository {

    override suspend fun getUserPreferences(userId: String): List<UserMealPreference> {
        return dao.getUserPreferences(userId).map { it.toDomain() }
    }

    override fun getUserPreferencesFlow(userId: String): Flow<List<UserMealPreference>> {
        return dao.getUserPreferencesFlow(userId).map { list ->
            list.map { it.toDomain() }
        }
    }

    override suspend fun getPreferenceByMealType(userId: String, mealType: MealType): UserMealPreference? {
        return dao.getPreferenceByMealType(userId, mealType.name)?.toDomain()
    }

    override suspend fun savePreference(preference: UserMealPreference): Result<UserMealPreference> {
        return try {
            dao.insertPreference(preference.toEntity())
            Result.success(preference)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deletePreference(preference: UserMealPreference): Result<Unit> {
        return try {
            dao.deletePreference(preference.toEntity())
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateSelectedMeal(userId: String, mealType: MealType, mealId: String): Result<Unit> {
        return try {
            dao.updateSelectedMeal(userId, mealType.name, mealId)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateMealTime(userId: String, mealType: MealType, timeSlot: String): Result<Unit> {
        return try {
            dao.updateMealTime(userId, mealType.name, timeSlot)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateReminderStatus(userId: String, mealType: MealType, enabled: Boolean): Result<Unit> {
        return try {
            dao.updateReminderStatus(userId, mealType.name, enabled)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}