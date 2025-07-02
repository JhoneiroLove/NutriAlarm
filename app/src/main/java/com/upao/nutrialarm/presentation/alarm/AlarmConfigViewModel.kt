package com.upao.nutrialarm.presentation.alarm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.upao.nutrialarm.domain.model.DayOfWeek
import com.upao.nutrialarm.domain.model.MealType
import com.upao.nutrialarm.domain.usecase.alarm.GetAlarmsUseCase
import com.upao.nutrialarm.domain.usecase.alarm.SetAlarmUseCase
import com.upao.nutrialarm.domain.usecase.alarm.ToggleAlarmUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AlarmConfigViewModel @Inject constructor(
    private val getAlarmsUseCase: GetAlarmsUseCase,
    private val setAlarmUseCase: SetAlarmUseCase,
    private val toggleAlarmUseCase: ToggleAlarmUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(AlarmConfigUiState())
    val uiState: StateFlow<AlarmConfigUiState> = _uiState.asStateFlow()

    fun loadAlarms(userId: String) {
        viewModelScope.launch {
            try {
                val alarms = getAlarmsUseCase(userId)
                val alarmStates = mutableMapOf<MealType, AlarmState>()

                // Inicializar con horarios por defecto
                MealType.values().forEach { mealType ->
                    val defaultTime = getDefaultTime(mealType)
                    alarmStates[mealType] = AlarmState(defaultTime, false)
                }

                // Actualizar con alarmas existentes
                alarms.forEach { alarm ->
                    alarmStates[alarm.mealType] = AlarmState(alarm.time, alarm.isEnabled)
                }

                _uiState.value = _uiState.value.copy(
                    alarmStates = alarmStates,
                    isLoading = false
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    message = "Error al cargar alarmas: ${e.message}",
                    isError = true,
                    isLoading = false
                )
            }
        }
    }

    fun updateAlarmTime(mealType: MealType, time: String) {
        val currentStates = _uiState.value.alarmStates.toMutableMap()
        val currentState = currentStates[mealType] ?: AlarmState("06:00", false)
        currentStates[mealType] = currentState.copy(time = time)

        _uiState.value = _uiState.value.copy(
            alarmStates = currentStates,
            message = ""
        )
    }

    fun toggleAlarm(mealType: MealType, enabled: Boolean) {
        val currentStates = _uiState.value.alarmStates.toMutableMap()
        val currentState = currentStates[mealType] ?: AlarmState("06:00", false)
        currentStates[mealType] = currentState.copy(enabled = enabled)

        _uiState.value = _uiState.value.copy(
            alarmStates = currentStates,
            message = ""
        )
    }

    fun saveAlarm(mealType: MealType) {
        viewModelScope.launch {
            try {
                val alarmState = _uiState.value.alarmStates[mealType]
                if (alarmState != null && alarmState.enabled) {
                    _uiState.value = _uiState.value.copy(isLoading = true)

                    val result = setAlarmUseCase(
                        userId = "current_user", // TODO: Get real user ID
                        mealType = mealType,
                        time = alarmState.time,
                        days = listOf(
                            DayOfWeek.MONDAY,
                            DayOfWeek.TUESDAY,
                            DayOfWeek.WEDNESDAY,
                            DayOfWeek.THURSDAY,
                            DayOfWeek.FRIDAY,
                            DayOfWeek.SATURDAY,
                            DayOfWeek.SUNDAY
                        ),
                        reminderMessage = getDefaultMessage(mealType)
                    )

                    result.fold(
                        onSuccess = {
                            _uiState.value = _uiState.value.copy(
                                message = "Alarma guardada exitosamente para ${getMealDisplayName(mealType)}",
                                isError = false,
                                isLoading = false
                            )
                        },
                        onFailure = { exception ->
                            _uiState.value = _uiState.value.copy(
                                message = "Error al guardar alarma: ${exception.message}",
                                isError = true,
                                isLoading = false
                            )
                        }
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    message = "Error inesperado: ${e.message}",
                    isError = true,
                    isLoading = false
                )
            }
        }
    }

    private fun getDefaultTime(mealType: MealType): String {
        return when (mealType) {
            MealType.BREAKFAST -> "06:30"
            MealType.SCHOOL_SNACK -> "09:30"
            MealType.LUNCH -> "13:30"
            MealType.AFTERNOON_SNACK -> "17:00"
            MealType.DINNER -> "19:30"
            MealType.OPTIONAL_SNACK -> "21:00"
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

    private fun getMealDisplayName(mealType: MealType): String {
        return when (mealType) {
            MealType.BREAKFAST -> "Desayuno"
            MealType.SCHOOL_SNACK -> "Refrigerio Escolar"
            MealType.LUNCH -> "Almuerzo"
            MealType.AFTERNOON_SNACK -> "Merienda de Tarde"
            MealType.DINNER -> "Cena"
            MealType.OPTIONAL_SNACK -> "Snack Opcional"
        }
    }

    fun clearMessage() {
        _uiState.value = _uiState.value.copy(message = "")
    }
}

data class AlarmConfigUiState(
    val alarmStates: Map<MealType, AlarmState> = emptyMap(),
    val isLoading: Boolean = false,
    val message: String = "",
    val isError: Boolean = false
)