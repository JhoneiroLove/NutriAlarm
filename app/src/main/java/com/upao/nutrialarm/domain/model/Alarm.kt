package com.upao.nutrialarm.domain.model

data class Alarm(
    val id: String = "",
    val userId: String = "",
    val mealType: MealType = MealType.BREAKFAST,
    val time: String = "08:00", // HH:mm format
    val isEnabled: Boolean = true,
    val days: List<DayOfWeek> = listOf(),
    val reminderMessage: String = "",
    val createdAt: Long = System.currentTimeMillis()
)

enum class DayOfWeek { MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY }
