package com.upao.nutrialarm.domain.usecase.diet

import com.upao.nutrialarm.domain.model.User
import com.upao.nutrialarm.domain.model.UserMealPreference
import com.upao.nutrialarm.domain.model.MealType
import com.upao.nutrialarm.domain.model.getDefaultTime
import com.upao.nutrialarm.domain.repository.UserRepository
import com.upao.nutrialarm.domain.usecase.meal.SaveUserMealPreferenceUseCase
import javax.inject.Inject

class SaveUserDietUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val saveUserMealPreferenceUseCase: SaveUserMealPreferenceUseCase
) {
    suspend operator fun invoke(
        user: User,
        selectedMeals: Map<MealType, String> // MealType -> MealId
    ): Result<User> {
        return try {
            // Guardar usuario actualizado
            val saveUserResult = userRepository.saveUser(user)

            saveUserResult.fold(
                onSuccess = { savedUser ->
                    // Crear preferencias para cada tipo de comida
                    selectedMeals.forEach { (mealType, mealId) ->
                        val preference = UserMealPreference(
                            id = "${user.id}_${mealType.name}",
                            userId = user.id,
                            mealType = mealType,
                            selectedMealId = mealId,
                            timeSlot = mealType.getDefaultTime(),
                            reminderEnabled = true
                        )

                        saveUserMealPreferenceUseCase(preference)
                    }

                    Result.success(savedUser)
                },
                onFailure = { exception ->
                    Result.failure(exception)
                }
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}