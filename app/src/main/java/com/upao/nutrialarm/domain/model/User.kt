package com.upao.nutrialarm.domain.model

data class User(
    val id: String = "",
    val email: String = "",
    val name: String = "",
    val age: Int = 0,
    val weight: Double = 0.0,
    val height: Double = 0.0,
    val activityLevel: ActivityLevel = ActivityLevel.SEDENTARY,
    val anemiaRisk: AnemiaRisk = AnemiaRisk.LOW,
    val createdAt: Long = System.currentTimeMillis()
)

enum class ActivityLevel { SEDENTARY, LIGHT, MODERATE, ACTIVE, VERY_ACTIVE }
enum class AnemiaRisk { LOW, MEDIUM, HIGH }