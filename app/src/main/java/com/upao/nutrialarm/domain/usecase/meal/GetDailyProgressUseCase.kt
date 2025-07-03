package com.upao.nutrialarm.domain.usecase.meal

import com.upao.nutrialarm.domain.model.User
import com.upao.nutrialarm.domain.repository.MealConsumptionRepository
import com.upao.nutrialarm.domain.repository.UserRepository
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class GetDailyProgressUseCase @Inject constructor(
    private val mealConsumptionRepository: MealConsumptionRepository,
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(userId: String): DailyProgress {
        val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        val user = userRepository.getUserById(userId)

        val summary = mealConsumptionRepository.getDailyNutritionSummary(userId, today)
        val targets = calculateTargets(user)

        return DailyProgress(
            iron = ProgressItem(
                current = summary?.totalIron ?: 0.0,
                target = targets.ironTarget,
                unit = "mg",
                label = "Hierro"
            ),
            calories = ProgressItem(
                current = summary?.totalCalories ?: 0.0,
                target = targets.caloriesTarget,
                unit = "cal",
                label = "Calorías"
            ),
            meals = ProgressItem(
                current = (summary?.totalMeals ?: 0).toDouble(),
                target = 6.0, // 6 comidas al día
                unit = "",
                label = "Comidas"
            ),
            water = ProgressItem(
                current = 6.0, // Esto podría ser otro tracking
                target = 8.0,
                unit = "vasos",
                label = "Agua"
            )
        )
    }

    private fun calculateTargets(user: User?): NutritionTargets {
        if (user == null) {
            return NutritionTargets(18.0, 2000.0)
        }

        val ironTarget = when (user.anemiaRisk) {
            com.upao.nutrialarm.domain.model.AnemiaRisk.LOW -> 8.0
            com.upao.nutrialarm.domain.model.AnemiaRisk.MEDIUM -> 12.0
            com.upao.nutrialarm.domain.model.AnemiaRisk.HIGH -> 18.0
        }

        val caloriesTarget = calculateCaloriesTarget(user)

        return NutritionTargets(ironTarget, caloriesTarget)
    }

    private fun calculateCaloriesTarget(user: User): Double {
        // Cálculo básico de Harris-Benedict
        val basalRate = if (user.age <= 18) {
            if (user.age <= 12) 1800.0 else 2200.0
        } else {
            88.362 + (13.397 * user.weight) + (4.799 * user.height) - (5.677 * user.age)
        }

        val activityMultiplier = when (user.activityLevel) {
            com.upao.nutrialarm.domain.model.ActivityLevel.SEDENTARY -> 1.2
            com.upao.nutrialarm.domain.model.ActivityLevel.LIGHT -> 1.375
            com.upao.nutrialarm.domain.model.ActivityLevel.MODERATE -> 1.55
            com.upao.nutrialarm.domain.model.ActivityLevel.ACTIVE -> 1.725
            com.upao.nutrialarm.domain.model.ActivityLevel.VERY_ACTIVE -> 1.9
        }

        return basalRate * activityMultiplier
    }
}

data class DailyProgress(
    val iron: ProgressItem,
    val calories: ProgressItem,
    val meals: ProgressItem,
    val water: ProgressItem
)

data class ProgressItem(
    val current: Double,
    val target: Double,
    val unit: String,
    val label: String
) {
    val percentage: Int
        get() = ((current / target) * 100).toInt().coerceAtMost(100)

    val formattedCurrent: String
        get() = if (unit.isEmpty()) current.toInt().toString() else "${if (current % 1.0 == 0.0) current.toInt() else String.format("%.1f", current)}"

    val formattedTarget: String
        get() = if (unit.isEmpty()) target.toInt().toString() else "${if (target % 1.0 == 0.0) target.toInt() else String.format("%.1f", target)}"
}

data class NutritionTargets(
    val ironTarget: Double,
    val caloriesTarget: Double
)