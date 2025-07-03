package com.upao.nutrialarm.domain.usecase.meal

import com.upao.nutrialarm.domain.model.UserMealPreference
import com.upao.nutrialarm.domain.model.getDefaultTime
import com.upao.nutrialarm.domain.repository.UserMealPreferenceRepository
import javax.inject.Inject

class GetUserMealPreferencesUseCase @Inject constructor(
    private val repository: UserMealPreferenceRepository
) {
    suspend operator fun invoke(userId: String): List<UserMealPreference> {
        return repository.getUserPreferences(userId)
    }
}

class SaveUserMealPreferenceUseCase @Inject constructor(
    private val repository: UserMealPreferenceRepository
) {
    suspend operator fun invoke(preference: UserMealPreference): Result<UserMealPreference> {
        return repository.savePreference(preference)
    }
}

class GetUserMealPreferenceByTypeUseCase @Inject constructor(
    private val repository: UserMealPreferenceRepository
) {
    suspend operator fun invoke(userId: String, mealType: com.upao.nutrialarm.domain.model.MealType): UserMealPreference? {
        return repository.getPreferenceByMealType(userId, mealType)
    }
}

class DeleteUserMealPreferenceUseCase @Inject constructor(
    private val repository: UserMealPreferenceRepository
) {
    suspend operator fun invoke(preference: UserMealPreference): Result<Unit> {
        return repository.deletePreference(preference)
    }
}

class InitializeDefaultPreferencesUseCase @Inject constructor(
    private val repository: UserMealPreferenceRepository
) {
    suspend operator fun invoke(userId: String): Result<List<UserMealPreference>> {
        return try {
            val defaultPreferences = com.upao.nutrialarm.domain.model.MealType.values().map { mealType ->
                UserMealPreference(
                    id = "${userId}_${mealType.name}",
                    userId = userId,
                    mealType = mealType,
                    selectedMealId = "", // Sin comida seleccionada inicialmente
                    timeSlot = mealType.getDefaultTime(),
                    isActive = true,
                    reminderEnabled = false // Desactivado por defecto hasta que seleccionen comida
                )
            }

            // Guardar todas las preferencias por defecto
            val results = defaultPreferences.map { repository.savePreference(it) }

            // Verificar que todas se guardaron correctamente
            val failures = results.filter { it.isFailure }
            if (failures.isNotEmpty()) {
                Result.failure(failures.first().exceptionOrNull() ?: Exception("Error saving preferences"))
            } else {
                Result.success(defaultPreferences)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}