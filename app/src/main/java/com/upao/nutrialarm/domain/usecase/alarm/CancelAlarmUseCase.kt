package com.upao.nutrialarm.domain.usecase.alarm

import com.upao.nutrialarm.domain.model.Alarm
import com.upao.nutrialarm.domain.repository.AlarmRepository
import com.upao.nutrialarm.util.AlarmManagerUtil
import javax.inject.Inject

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