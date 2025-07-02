package com.upao.nutrialarm.data.remote.firebase.dto

data class MealDto(
    val id: String = "",
    val name: String = "",
    val description: String = "",
    val ingredients: List<String> = emptyList(),
    val mealType: String = "",
    val ironContent: Double = 0.0,
    val calories: Double = 0.0,
    val preparationTime: Int = 0,
    val imageUrl: String = "",
    val vitaminC: Double = 0.0,
    val folate: Double = 0.0
) {
    fun toDomain(): Meal {
        return Meal(
            id = id,
            name = name,
            description = description,
            ingredients = ingredients,
            mealType = MealType.valueOf(mealType),
            ironContent = ironContent,
            calories = calories,
            preparationTime = preparationTime,
            imageUrl = imageUrl,
            vitaminC = vitaminC,
            folate = folate
        )
    }
}