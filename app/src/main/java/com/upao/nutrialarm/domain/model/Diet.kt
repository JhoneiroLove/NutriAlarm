package com.upao.nutrialarm.domain.model

data class Diet(
    val id: String = "",
    val name: String = "",
    val description: String = "",
    val anemiaRiskLevel: AnemiaRisk = AnemiaRisk.LOW,
    val meals: List<Meal> = emptyList(),
    val ironContent: Double = 0.0, // mg de hierro total
    val calories: Double = 0.0
)