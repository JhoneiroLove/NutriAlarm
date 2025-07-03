package com.upao.nutrialarm.domain.usecase.alarm

import com.upao.nutrialarm.domain.model.Alarm
import com.upao.nutrialarm.domain.repository.AlarmRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAlarmsUseCase @Inject constructor(
    private val alarmRepository: AlarmRepository
) {
    suspend operator fun invoke(userId: String): List<Alarm> {
        return alarmRepository.getAlarmsByUser(userId)
    }

    fun getAlarmsFlow(userId: String): Flow<List<Alarm>> {
        return alarmRepository.getAlarmsByUserFlow(userId)
    }
}