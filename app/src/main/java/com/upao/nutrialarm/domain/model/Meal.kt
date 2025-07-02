package com.upao.nutrialarm.domain.model

data class Meal(
    val id: String = "",
    val name: String = "",
    val description: String = "",
    val ingredients: List<String> = emptyList(),
    val mealType: MealType = MealType.BREAKFAST,
    val ironContent: Double = 0.0, // mg de hierro
    val calories: Double = 0.0,
    val preparationTime: Int = 0, // minutos
    val imageUrl: String = ""
)

enum class MealType { BREAKFAST, LUNCH, DINNER }