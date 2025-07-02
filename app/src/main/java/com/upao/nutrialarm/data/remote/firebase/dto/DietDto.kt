package com.upao.nutrialarm.data.remote.firebase.dto

data class DietDto(
    val id: String = "",
    val name: String = "",
    val description: String = "",
    val anemiaRiskLevel: String = "",
    val mealIds: List<String> = emptyList(),
    val ironContent: Double = 0.0,
    val calories: Double = 0.0
) {
    fun toDomain(): Diet {
        return Diet(
            id = id,
            name = name,
            description = description,
            anemiaRiskLevel = AnemiaRisk.valueOf(anemiaRiskLevel),
            meals = emptyList(), // Se cargan por separado
            ironContent = ironContent,
            calories = calories
        )
    }
}

