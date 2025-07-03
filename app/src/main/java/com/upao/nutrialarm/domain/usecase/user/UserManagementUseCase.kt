package com.upao.nutrialarm.domain.usecase.user

import com.upao.nutrialarm.domain.model.User
import com.upao.nutrialarm.domain.repository.UserRepository
import com.upao.nutrialarm.domain.usecase.meal.InitializeDefaultPreferencesUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserManagementUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val initializeDefaultPreferencesUseCase: InitializeDefaultPreferencesUseCase
) {
    suspend fun createNewUser(user: User): Result<User> {
        return try {
            val result = userRepository.saveUser(user)

            result.fold(
                onSuccess = { savedUser ->
                    // Inicializar preferencias por defecto
                    initializeDefaultPreferencesUseCase(savedUser.id)
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

    suspend fun getCurrentUser(): User? {
        return userRepository.getCurrentUser()
    }

    fun getCurrentUserFlow(): Flow<User?> {
        return userRepository.getCurrentUserFlow()
    }

    suspend fun updateUser(user: User): Result<User> {
        return userRepository.updateUser(user)
    }

    suspend fun getUserHealthStatus(user: User): HealthStatus {
        val bmi = calculateBMI(user.weight, user.height)
        val anemiaRisk = user.anemiaRisk

        return HealthStatus(
            bmi = bmi,
            bmiCategory = getBMICategory(bmi),
            anemiaRisk = anemiaRisk,
            recommendedIronIntake = getRecommendedIronIntake(user),
            recommendedCalories = getRecommendedCalories(user)
        )
    }

    private fun calculateBMI(weight: Double, height: Double): Double {
        val heightInMeters = height / 100.0
        return weight / (heightInMeters * heightInMeters)
    }

    private fun getBMICategory(bmi: Double): String {
        return when {
            bmi < 18.5 -> "Bajo peso"
            bmi < 25.0 -> "Peso normal"
            bmi < 30.0 -> "Sobrepeso"
            else -> "Obesidad"
        }
    }

    private fun getRecommendedIronIntake(user: User): Double {
        return when (user.anemiaRisk) {
            com.upao.nutrialarm.domain.model.AnemiaRisk.LOW -> 8.0
            com.upao.nutrialarm.domain.model.AnemiaRisk.MEDIUM -> 12.0
            com.upao.nutrialarm.domain.model.AnemiaRisk.HIGH -> 18.0
        }
    }

    private fun getRecommendedCalories(user: User): Double {
        val basalMetabolicRate = if (user.age <= 18) {
            // Fórmula para adolescentes
            if (user.age <= 12) 1800.0 else 2200.0
        } else {
            // Fórmula Harris-Benedict simplificada
            88.362 + (13.397 * user.weight) + (4.799 * user.height) - (5.677 * user.age)
        }

        val activityMultiplier = when (user.activityLevel) {
            com.upao.nutrialarm.domain.model.ActivityLevel.SEDENTARY -> 1.2
            com.upao.nutrialarm.domain.model.ActivityLevel.LIGHT -> 1.375
            com.upao.nutrialarm.domain.model.ActivityLevel.MODERATE -> 1.55
            com.upao.nutrialarm.domain.model.ActivityLevel.ACTIVE -> 1.725
            com.upao.nutrialarm.domain.model.ActivityLevel.VERY_ACTIVE -> 1.9
        }

        return basalMetabolicRate * activityMultiplier
    }
}

data class HealthStatus(
    val bmi: Double,
    val bmiCategory: String,
    val anemiaRisk: com.upao.nutrialarm.domain.model.AnemiaRisk,
    val recommendedIronIntake: Double,
    val recommendedCalories: Double
)