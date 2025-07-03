package com.upao.nutrialarm.presentation.meal

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.upao.nutrialarm.domain.model.*
import com.upao.nutrialarm.domain.repository.DietRepository
import com.upao.nutrialarm.domain.usecase.meal.GetUserMealPreferencesUseCase
import com.upao.nutrialarm.domain.usecase.meal.SaveUserMealPreferenceUseCase
import com.upao.nutrialarm.domain.usecase.alarm.SetAlarmUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MealSelectionViewModel @Inject constructor(
    private val dietRepository: DietRepository,
    private val getUserMealPreferencesUseCase: GetUserMealPreferencesUseCase,
    private val saveUserMealPreferenceUseCase: SaveUserMealPreferenceUseCase,
    private val setAlarmUseCase: SetAlarmUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(MealSelectionUiState())
    val uiState: StateFlow<MealSelectionUiState> = _uiState.asStateFlow()

    private val _selectedMealType = MutableStateFlow<MealType?>(null)
    val selectedMealType: StateFlow<MealType?> = _selectedMealType.asStateFlow()

    private var currentUserId: String = ""

    fun loadUserPreferences(userId: String) {
        currentUserId = userId
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isLoading = true)

                // Cargar preferencias del usuario
                val preferences = getUserMealPreferencesUseCase(userId)
                val preferencesMap = preferences.associateBy { it.mealType }

                // Cargar todas las comidas por tipo
                val mealsByType = mutableMapOf<MealType, List<Meal>>()
                MealType.values().forEach { mealType ->
                    val meals = dietRepository.getMealsByType(mealType)
                    mealsByType[mealType] = meals
                }

                _uiState.value = _uiState.value.copy(
                    userPreferences = preferencesMap,
                    mealsByType = mealsByType,
                    isLoading = false
                )

                // Seleccionar el primer tipo de comida por defecto
                if (_selectedMealType.value == null) {
                    _selectedMealType.value = MealType.BREAKFAST
                }

            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "Error al cargar preferencias: ${e.message}"
                )
            }
        }
    }

    fun selectMealType(mealType: MealType) {
        _selectedMealType.value = mealType
        clearMessage()
    }

    fun selectMeal(mealType: MealType, mealId: String) {
        viewModelScope.launch {
            try {
                // Crear o actualizar preferencia
                val currentPreference = _uiState.value.userPreferences[mealType]
                val newPreference = UserMealPreference(
                    id = currentPreference?.id ?: "${currentUserId}_${mealType.name}",
                    userId = currentUserId,
                    mealType = mealType,
                    selectedMealId = mealId,
                    timeSlot = currentPreference?.timeSlot ?: mealType.getDefaultTime(),
                    reminderEnabled = currentPreference?.reminderEnabled ?: true,
                    updatedAt = System.currentTimeMillis()
                )

                // Guardar preferencia
                val result = saveUserMealPreferenceUseCase(newPreference)

                result.fold(
                    onSuccess = {
                        // Actualizar estado local
                        val updatedPreferences = _uiState.value.userPreferences.toMutableMap()
                        updatedPreferences[mealType] = newPreference

                        _uiState.value = _uiState.value.copy(
                            userPreferences = updatedPreferences,
                            message = "Menú seleccionado: ${getMealName(mealId)}",
                            isError = false
                        )

                        // Programar/actualizar alarma si el recordatorio está activo
                        if (newPreference.reminderEnabled) {
                            scheduleAlarmForPreference(newPreference)
                        }
                    },
                    onFailure = { exception ->
                        _uiState.value = _uiState.value.copy(
                            message = "Error al guardar: ${exception.message}",
                            isError = true
                        )
                    }
                )

            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    message = "Error inesperado: ${e.message}",
                    isError = true
                )
            }
        }
    }

    fun updateMealTime(mealType: MealType, newTime: String) {
        viewModelScope.launch {
            try {
                val currentPreference = _uiState.value.userPreferences[mealType]

                if (currentPreference != null) {
                    val updatedPreference = currentPreference.copy(
                        timeSlot = newTime,
                        updatedAt = System.currentTimeMillis()
                    )

                    val result = saveUserMealPreferenceUseCase(updatedPreference)

                    result.fold(
                        onSuccess = {
                            // Actualizar estado local
                            val updatedPreferences = _uiState.value.userPreferences.toMutableMap()
                            updatedPreferences[mealType] = updatedPreference

                            _uiState.value = _uiState.value.copy(
                                userPreferences = updatedPreferences,
                                message = "Horario actualizado a $newTime",
                                isError = false
                            )

                            // Reprogramar alarma
                            if (updatedPreference.reminderEnabled) {
                                scheduleAlarmForPreference(updatedPreference)
                            }
                        },
                        onFailure = { exception ->
                            _uiState.value = _uiState.value.copy(
                                message = "Error al actualizar horario: ${exception.message}",
                                isError = true
                            )
                        }
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    message = "Error inesperado: ${e.message}",
                    isError = true
                )
            }
        }
    }

    fun toggleReminder(mealType: MealType, enabled: Boolean) {
        viewModelScope.launch {
            try {
                val currentPreference = _uiState.value.userPreferences[mealType]

                if (currentPreference != null) {
                    val updatedPreference = currentPreference.copy(
                        reminderEnabled = enabled,
                        updatedAt = System.currentTimeMillis()
                    )

                    val result = saveUserMealPreferenceUseCase(updatedPreference)

                    result.fold(
                        onSuccess = {
                            // Actualizar estado local
                            val updatedPreferences = _uiState.value.userPreferences.toMutableMap()
                            updatedPreferences[mealType] = updatedPreference

                            _uiState.value = _uiState.value.copy(
                                userPreferences = updatedPreferences,
                                message = if (enabled) "Recordatorio activado" else "Recordatorio desactivado",
                                isError = false
                            )

                            // Programar o cancelar alarma
                            if (enabled) {
                                scheduleAlarmForPreference(updatedPreference)
                            } else {
                                // TODO: Cancelar alarma específica
                            }
                        },
                        onFailure = { exception ->
                            _uiState.value = _uiState.value.copy(
                                message = "Error al cambiar recordatorio: ${exception.message}",
                                isError = true
                            )
                        }
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    message = "Error inesperado: ${e.message}",
                    isError = true
                )
            }
        }
    }

    private suspend fun scheduleAlarmForPreference(preference: UserMealPreference) {
        try {
            val selectedMeal = getMealById(preference.selectedMealId)
            val reminderMessage = selectedMeal?.let {
                "¡Es hora de: ${it.name}! ${getMealTypeIcon(preference.mealType)}"
            } ?: getDefaultReminderMessage(preference.mealType)

            setAlarmUseCase(
                userId = preference.userId,
                mealType = preference.mealType,
                time = preference.timeSlot,
                days = listOf(
                    DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY,
                    DayOfWeek.THURSDAY, DayOfWeek.FRIDAY, DayOfWeek.SATURDAY, DayOfWeek.SUNDAY
                ),
                reminderMessage = reminderMessage
            )
        } catch (e: Exception) {
            // Log error pero no interrumpir el flujo
        }
    }

    private fun getMealById(mealId: String): Meal? {
        return _uiState.value.mealsByType.values.flatten().find { it.id == mealId }
    }

    private fun getMealName(mealId: String): String {
        return getMealById(mealId)?.name ?: "Comida seleccionada"
    }

    private fun getDefaultReminderMessage(mealType: MealType): String {
        return when (mealType) {
            MealType.BREAKFAST -> "¡Es hora del desayuno! 🌅"
            MealType.SCHOOL_SNACK -> "Tiempo de tu refrigerio escolar 🍎"
            MealType.LUNCH -> "¡Hora del almuerzo! 🍽️"
            MealType.AFTERNOON_SNACK -> "Merienda de tarde 🥜"
            MealType.DINNER -> "¡Hora de cenar! 🌙"
            MealType.OPTIONAL_SNACK -> "Snack opcional ☕"
        }
    }

    private fun getMealTypeIcon(mealType: MealType): String {
        return when (mealType) {
            MealType.BREAKFAST -> "🌅"
            MealType.SCHOOL_SNACK -> "🍎"
            MealType.LUNCH -> "🍽️"
            MealType.AFTERNOON_SNACK -> "🥜"
            MealType.DINNER -> "🌙"
            MealType.OPTIONAL_SNACK -> "☕"
        }
    }

    fun clearMessage() {
        _uiState.value = _uiState.value.copy(message = "", isError = false)
    }
}

data class MealSelectionUiState(
    val userPreferences: Map<MealType, UserMealPreference> = emptyMap(),
    val mealsByType: Map<MealType, List<Meal>> = emptyMap(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val message: String = "",
    val isError: Boolean = false
)