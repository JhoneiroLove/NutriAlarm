package com.upao.nutrialarm.domain.usecase.diet

import com.upao.nutrialarm.domain.model.Meal
import com.upao.nutrialarm.domain.model.MealType
import com.upao.nutrialarm.domain.repository.DietRepository
import javax.inject.Inject

class GetMealsForTimeUseCase @Inject constructor(
    private val dietRepository: DietRepository
) {
    suspend operator fun invoke(mealType: MealType): List<Meal> {
        return dietRepository.getMealsByType(mealType)
    }

    suspend fun getMealById(mealId: String): Meal? {
        return dietRepository.getMealById(mealId)
    }

    suspend fun getAllMealsGroupedByType(): Map<MealType, List<Meal>> {
        val result = mutableMapOf<MealType, List<Meal>>()

        MealType.values().forEach { mealType ->
            result[mealType] = dietRepository.getMealsByType(mealType)
        }

        return result
    }
}