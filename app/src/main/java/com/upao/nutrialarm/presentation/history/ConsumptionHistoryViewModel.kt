package com.upao.nutrialarm.presentation.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.upao.nutrialarm.domain.model.MealConsumption
import com.upao.nutrialarm.domain.repository.MealConsumptionRepository
import com.upao.nutrialarm.domain.repository.UserRepository
import com.upao.nutrialarm.domain.repository.DietRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class ConsumptionHistoryViewModel @Inject constructor(
    private val mealConsumptionRepository: MealConsumptionRepository,
    private val userRepository: UserRepository,
    private val dietRepository: DietRepository
) : ViewModel() {

    private val _selectedDate = MutableStateFlow(getCurrentDate())
    val selectedDate: StateFlow<String> = _selectedDate.asStateFlow()

    private val _consumptions = MutableStateFlow<List<MealConsumptionWithMeal>>(emptyList())
    val consumptions: StateFlow<List<MealConsumptionWithMeal>> = _consumptions.asStateFlow()

    private val _dailySummary = MutableStateFlow<DailySummary?>(null)
    val dailySummary: StateFlow<DailySummary?> = _dailySummary.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _message = MutableStateFlow<String?>(null)
    val message: StateFlow<String?> = _message.asStateFlow()

    init {
        loadConsumptionsForDate()
    }

    fun selectDate(date: String) {
        _selectedDate.value = date
        loadConsumptionsForDate()
    }

    private fun loadConsumptionsForDate() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val currentUser = userRepository.getCurrentUser()
                if (currentUser != null) {
                    // Cargar consumos del día
                    val consumptions = mealConsumptionRepository.getMealConsumptionForDay(
                        currentUser.id,
                        _selectedDate.value
                    )

                    // Enriquecer con información de las comidas
                    val enrichedConsumptions = consumptions.map { consumption ->
                        val meal = dietRepository.getMealById(consumption.mealId)
                        MealConsumptionWithMeal(
                            consumption = consumption,
                            mealName = meal?.name ?: "Comida desconocida",
                            mealDescription = meal?.description ?: ""
                        )
                    }.sortedByDescending { it.consumption.consumedAt }

                    _consumptions.value = enrichedConsumptions

                    // Calcular resumen diario
                    val summary = mealConsumptionRepository.getDailyNutritionSummary(
                        currentUser.id,
                        _selectedDate.value
                    )

                    _dailySummary.value = summary?.let {
                        DailySummary(
                            totalIron = it.totalIron,
                            totalCalories = it.totalCalories,
                            totalVitaminC = it.totalVitaminC,
                            totalFolate = it.totalFolate,
                            totalMeals = it.totalMeals
                        )
                    }
                }
            } catch (e: Exception) {
                _message.value = "Error al cargar historial: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun deleteConsumption(consumption: MealConsumption) {
        viewModelScope.launch {
            try {
                val result = mealConsumptionRepository.deleteMealConsumption(consumption)
                result.fold(
                    onSuccess = {
                        _message.value = "Consumo eliminado correctamente"
                        loadConsumptionsForDate() // Recargar lista
                    },
                    onFailure = { exception ->
                        _message.value = "Error al eliminar: ${exception.message}"
                    }
                )
            } catch (e: Exception) {
                _message.value = "Error inesperado: ${e.message}"
            }
        }
    }

    fun clearMessage() {
        _message.value = null
    }

    private fun getCurrentDate(): String {
        val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return format.format(Date())
    }
}

// Data class para enriquecer consumos con información de comidas
data class MealConsumptionWithMeal(
    val consumption: MealConsumption,
    val mealName: String,
    val mealDescription: String
)