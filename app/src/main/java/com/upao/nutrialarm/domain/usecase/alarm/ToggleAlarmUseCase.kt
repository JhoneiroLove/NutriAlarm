package com.upao.nutrialarm.domain.usecase.alarm

import com.upao.nutrialarm.domain.model.Alarm
import com.upao.nutrialarm.domain.repository.AlarmRepository
import com.upao.nutrialarm.util.AlarmManagerUtil
import javax.inject.Inject

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