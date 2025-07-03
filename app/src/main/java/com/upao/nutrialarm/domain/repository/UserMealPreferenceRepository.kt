package com.upao.nutrialarm.domain.repository

import com.upao.nutrialarm.domain.model.MealType
import com.upao.nutrialarm.domain.model.UserMealPreference
import kotlinx.coroutines.flow.Flow

interface UserMealPreferenceRepository {
    suspend fun getUserPreferences(userId: String): List<UserMealPreference>
    fun getUserPreferencesFlow(userId: String): Flow<List<UserMealPreference>>
    suspend fun getPreferenceByMealType(userId: String, mealType: MealType): UserMealPreference?
    suspend fun savePreference(preference: UserMealPreference): Result<UserMealPreference>
    suspend fun deletePreference(preference: UserMealPreference): Result<Unit>
    suspend fun updateSelectedMeal(userId: String, mealType: MealType, mealId: String): Result<Unit>
    suspend fun updateMealTime(userId: String, mealType: MealType, timeSlot: String): Result<Unit>
    suspend fun updateReminderStatus(userId: String, mealType: MealType, enabled: Boolean): Result<Unit>
}