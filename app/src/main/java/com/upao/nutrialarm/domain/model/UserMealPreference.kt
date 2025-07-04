package com.upao.nutrialarm.domain.model

data class UserMealPreference(
    val id: String,
    val userId: String,
    val mealType: MealType,
    val selectedMealId: String,
    val timeSlot: String, // "06:30"
    val isActive: Boolean = true,
    val reminderEnabled: Boolean = true,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

// Horarios recomendados para cada tipo de comida
object RecommendedMealTimes {
    val MEAL_TIME_RANGES = mapOf(
        MealType.BREAKFAST to TimeRange("06:30", "07:00"),
        MealType.SCHOOL_SNACK to TimeRange("09:30", "10:00"),
        MealType.LUNCH to TimeRange("13:30", "14:00"),
        MealType.AFTERNOON_SNACK to TimeRange("17:00", "17:30"),
        MealType.DINNER to TimeRange("19:30", "20:00"),
        MealType.OPTIONAL_SNACK to TimeRange("21:00", "21:30")
    )

    fun getRecommendedTime(mealType: MealType): String {
        return MEAL_TIME_RANGES[mealType]?.start ?: "12:00"
    }

    fun getTimeRangeText(mealType: MealType): String {
        val range = MEAL_TIME_RANGES[mealType]
        return if (range != null) {
            "${range.start} - ${range.end}"
        } else {
            "Hora flexible"
        }
    }
}

data class TimeRange(
    val start: String, // "06:30"
    val end: String    // "07:00"
)

// Extensión para facilitar el trabajo con horarios
fun MealType.getRecommendedTimeRange(): String = RecommendedMealTimes.getTimeRangeText(this)
fun MealType.getDefaultTime(): String = RecommendedMealTimes.getRecommendedTime(this)