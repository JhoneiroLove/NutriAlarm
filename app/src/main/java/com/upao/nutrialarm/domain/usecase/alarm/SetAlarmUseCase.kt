package com.upao.nutrialarm.domain.usecase.alarm

import com.upao.nutrialarm.domain.model.Alarm
import com.upao.nutrialarm.domain.model.DayOfWeek
import com.upao.nutrialarm.domain.model.MealType
import com.upao.nutrialarm.domain.repository.AlarmRepository
import com.upao.nutrialarm.util.AlarmManagerUtil
import javax.inject.Inject

class SetAlarmUseCase @Inject constructor(
    private val alarmRepository: AlarmRepository,
    private val alarmManagerUtil: AlarmManagerUtil
) {
    suspend operator fun invoke(
        userId: String,
        mealType: MealType,
        time: String,
        days: List<DayOfWeek> = emptyList(),
        reminderMessage: String = ""
    ): Result<Alarm> {
        return try {
            val alarm = Alarm(
                id = "${userId}_${mealType.name}_${time.replace(":", "")}",
                userId = userId,
                mealType = mealType,
                time = time,
                isEnabled = true,
                days = days,
                reminderMessage = reminderMessage.ifEmpty { getDefaultMessage(mealType) }
            )

            // Guardar en base de datos
            val saveResult = alarmRepository.saveAlarm(alarm)

            saveResult.fold(
                onSuccess = { savedAlarm ->
                    // Programar la alarma en el sistema
                    alarmManagerUtil.scheduleAlarm(savedAlarm)
                    Result.success(savedAlarm)
                },
                onFailure = { exception ->
                    Result.failure(exception)
                }
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun getDefaultMessage(mealType: MealType): String {
        return when (mealType) {
            MealType.BREAKFAST -> "¡Es hora del desayuno! 🌅"
            MealType.SCHOOL_SNACK -> "Tiempo de tu refrigerio escolar 🍎"
            MealType.LUNCH -> "¡Hora del almuerzo! 🍽️"
            MealType.AFTERNOON_SNACK -> "Merienda de tarde 🥜"
            MealType.DINNER -> "¡Hora de cenar! 🌙"
            MealType.OPTIONAL_SNACK -> "Snack opcional ☕"
        }
    }
}

class CancelAlarmUseCase @Inject constructor(
    private val alarmRepository: AlarmRepository,
    private val alarmManagerUtil: AlarmManagerUtil
) {
    suspend operator fun invoke(alarm: Alarm): Result<Unit> {
        return try {
            // Cancelar la alarma del sistema
            alarmManagerUtil.cancelAlarm(alarm)

            // Eliminar de la base de datos
            alarmRepository.deleteAlarm(alarm)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

class GetAlarmsUseCase @Inject constructor(
    private val alarmRepository: AlarmRepository
) {
    suspend operator fun invoke(userId: String): List<Alarm> {
        return alarmRepository.getAlarmsByUser(userId)
    }
}

class ToggleAlarmUseCase @Inject constructor(
    private val alarmRepository: AlarmRepository,
    private val alarmManagerUtil: AlarmManagerUtil
) {
    suspend operator fun invoke(alarm: Alarm, enabled: Boolean): Result<Unit> {
        return try {
            if (enabled) {
                // Activar alarma
                alarmManagerUtil.scheduleAlarm(alarm.copy(isEnabled = true))
            } else {
                // Desactivar alarma
                alarmManagerUtil.cancelAlarm(alarm)
            }

            // Actualizar en base de datos
            alarmRepository.toggleAlarm(alarm.id, enabled)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}