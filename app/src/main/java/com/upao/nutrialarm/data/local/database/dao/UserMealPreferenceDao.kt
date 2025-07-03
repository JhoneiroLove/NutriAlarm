package com.upao.nutrialarm.data.local.database.dao

import androidx.room.*
import com.upao.nutrialarm.data.local.database.entities.UserMealPreferenceEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserMealPreferenceDao {

    @Query("SELECT * FROM user_meal_preferences WHERE userId = :userId AND isActive = 1")
    suspend fun getUserPreferences(userId: String): List<UserMealPreferenceEntity>

    @Query("SELECT * FROM user_meal_preferences WHERE userId = :userId AND isActive = 1")
    fun getUserPreferencesFlow(userId: String): Flow<List<UserMealPreferenceEntity>>

    @Query("SELECT * FROM user_meal_preferences WHERE userId = :userId AND mealType = :mealType AND isActive = 1")
    suspend fun getPreferenceByMealType(userId: String, mealType: String): UserMealPreferenceEntity?

    @Query("SELECT * FROM user_meal_preferences WHERE id = :preferenceId")
    suspend fun getPreferenceById(preferenceId: String): UserMealPreferenceEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPreference(preference: UserMealPreferenceEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPreferences(preferences: List<UserMealPreferenceEntity>)

    @Update
    suspend fun updatePreference(preference: UserMealPreferenceEntity)

    @Delete
    suspend fun deletePreference(preference: UserMealPreferenceEntity)

    @Query("UPDATE user_meal_preferences SET selectedMealId = :mealId, updatedAt = :timestamp WHERE userId = :userId AND mealType = :mealType")
    suspend fun updateSelectedMeal(userId: String, mealType: String, mealId: String, timestamp: Long = System.currentTimeMillis())

    @Query("UPDATE user_meal_preferences SET timeSlot = :timeSlot, updatedAt = :timestamp WHERE userId = :userId AND mealType = :mealType")
    suspend fun updateMealTime(userId: String, mealType: String, timeSlot: String, timestamp: Long = System.currentTimeMillis())

    @Query("UPDATE user_meal_preferences SET reminderEnabled = :enabled, updatedAt = :timestamp WHERE userId = :userId AND mealType = :mealType")
    suspend fun updateReminderStatus(userId: String, mealType: String, enabled: Boolean, timestamp: Long = System.currentTimeMillis())

    @Query("DELETE FROM user_meal_preferences WHERE userId = :userId")
    suspend fun deleteAllUserPreferences(userId: String)

    @Query("SELECT COUNT(*) FROM user_meal_preferences WHERE userId = :userId AND isActive = 1")
    suspend fun getUserPreferencesCount(userId: String): Int
}