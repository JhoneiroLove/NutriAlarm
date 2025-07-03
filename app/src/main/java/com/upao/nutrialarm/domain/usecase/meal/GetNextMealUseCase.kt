package com.upao.nutrialarm.domain.usecase.meal

import com.upao.nutrialarm.domain.model.MealType
import com.upao.nutrialarm.domain.repository.MealConsumptionRepository
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class GetNextMealUseCase @Inject constructor(
    private val mealConsumptionRepository: MealConsumptionRepository
) {
    suspend operator fun invoke(userId: String): NextMealInfo {
        val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        val currentTime = Calendar.getInstance()
        val currentHour = currentTime.get(Calendar.HOUR_OF_DAY)
        val currentMinute = currentTime.get(Calendar.MINUTE)

        // Obtener tipos de comida ya consumidos hoy
        val consumedMealTypes = mealConsumptionRepository.getConsumedMealTypes(userId, today)
        val consumedTypes = consumedMealTypes.map { MealType.valueOf(it) }.toSet()

        // Horarios de comidas con orden
        val mealSchedule = listOf(
            MealTimeInfo(MealType.BREAKFAST, 6, 30),
            MealTimeInfo(MealType.SCHOOL_SNACK, 9, 30),
            MealTimeInfo(MealType.LUNCH, 13, 30),
            MealTimeInfo(MealType.AFTERNOON_SNACK, 17, 0),
            MealTimeInfo(MealType.DINNER, 19, 30),
            MealTimeInfo(MealType.OPTIONAL_SNACK, 21, 0)
        )

        // Encontrar próxima comida no consumida
        val nextMeal = findNextMeal(mealSchedule, consumedTypes, currentHour, currentMinute)

        return NextMealInfo(
            mealType = nextMeal.mealType,
            timeSlot = "${String.format("%02d", nextMeal.hour)}:${String.format("%02d", nextMeal.minute)}",
            isToday = isToday(nextMeal, currentHour, currentMinute),
            hasBeenConsumed = consumedTypes.contains(nextMeal.mealType)
        )
    }

    private fun findNextMeal(
        schedule: List<MealTimeInfo>,
        consumed: Set<MealType>,
        currentHour: Int,
        currentMinute: Int
    ): MealTimeInfo {
        val currentTimeInMinutes = currentHour * 60 + currentMinute

        // Buscar próxima comida del día que no se haya consumido
        for (meal in schedule) {
            val mealTimeInMinutes = meal.hour * 60 + meal.minute
            if (mealTimeInMinutes >= currentTimeInMinutes && !consumed.contains(meal.mealType)) {
                return meal
            }
        }

        // Si no hay más comidas hoy, buscar la primera del día siguiente
        for (meal in schedule) {
            if (!consumed.contains(meal.mealType)) {
                return meal
            }
        }

        // Si todo fue consumido, volver al desayuno
        return schedule.first()
    }

    private fun isToday(meal: MealTimeInfo, currentHour: Int, currentMinute: Int): Boolean {
        val currentTimeInMinutes = currentHour * 60 + currentMinute
        val mealTimeInMinutes = meal.hour * 60 + meal.minute
        return mealTimeInMinutes >= currentTimeInMinutes
    }
}

data class MealTimeInfo(
    val mealType: MealType,
    val hour: Int,
    val minute: Int
)

data class NextMealInfo(
    val mealType: MealType,
    val timeSlot: String,
    val isToday: Boolean,
    val hasBeenConsumed: Boolean
)