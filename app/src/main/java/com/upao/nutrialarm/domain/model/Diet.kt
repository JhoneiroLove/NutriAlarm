package com.upao.nutrialarm.domain.model

data class Diet(
    val id: String,
    val name: String,
    val description: String,
    val anemiaRiskLevel: AnemiaRisk,
    val meals: List<Meal> = emptyList(),
    val ironContent: Double,
    val calories: Double
)