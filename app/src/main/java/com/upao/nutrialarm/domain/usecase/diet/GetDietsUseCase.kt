package com.upao.nutrialarm.domain.usecase.diet

import com.upao.nutrialarm.domain.model.AnemiaRisk
import com.upao.nutrialarm.domain.model.Diet
import com.upao.nutrialarm.domain.repository.DietRepository
import javax.inject.Inject

class GetDietsUseCase @Inject constructor(
    private val dietRepository: DietRepository
) {
    suspend operator fun invoke(): List<Diet> {
        return dietRepository.getAllDiets()
    }

    suspend fun getDietsByRiskLevel(riskLevel: AnemiaRisk): List<Diet> {
        return dietRepository.getDietsByRiskLevel(riskLevel)
    }

    suspend fun getDietWithMeals(dietId: String): Diet? {
        return dietRepository.getDietWithMeals(dietId)
    }
}