package com.upao.nutrialarm.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.upao.nutrialarm.domain.usecase.meal.GetNextMealUseCase
import com.upao.nutrialarm.domain.usecase.meal.GetDailyProgressUseCase
import com.upao.nutrialarm.domain.usecase.meal.NextMealInfo
import com.upao.nutrialarm.domain.usecase.meal.DailyProgress
import com.upao.nutrialarm.domain.repository.UserRepository
import com.upao.nutrialarm.domain.repository.MealConsumptionRepository
import com.upao.nutrialarm.domain.repository.DietRepository
import com.upao.nutrialarm.domain.model.User
import com.upao.nutrialarm.domain.model.MealConsumption
import com.upao.nutrialarm.domain.model.MealType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getNextMealUseCase: GetNextMealUseCase,
    private val getDailyProgressUseCase: GetDailyProgressUseCase,
    private val userRepository: UserRepository,
    private val mealConsumptionRepository: MealConsumptionRepository,
    private val dietRepository: DietRepository
) : ViewModel() {

    private val _nextMealInfo = MutableStateFlow<NextMealInfo?>(null)
    val nextMealInfo: StateFlow<NextMealInfo?> = _nextMealInfo.asStateFlow()

    private val _dailyProgress = MutableStateFlow<DailyProgress?>(null)
    val dailyProgress: StateFlow<DailyProgress?> = _dailyProgress.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _message = MutableStateFlow<String?>(null)
    val message: StateFlow<String?> = _message.asStateFlow()

    // Exponer el usuario actual como StateFlow
    val currentUser: StateFlow<User?> = userRepository.getCurrentUserFlow()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    init {
        loadHomeData()
        startPeriodicUpdates()
    }

    private fun loadHomeData() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val currentUserValue = userRepository.getCurrentUser()
                if (currentUserValue != null) {
                    // Cargar próxima comida
                    val nextMeal = getNextMealUseCase(currentUserValue.id)
                    _nextMealInfo.value = nextMeal

                    // Cargar progreso diario
                    val progress = getDailyProgressUseCase(currentUserValue.id)
                    _dailyProgress.value = progress
                }
            } catch (e: Exception) {
                _message.value = "Error al cargar datos: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun startPeriodicUpdates() {
        viewModelScope.launch {
            while (true) {
                kotlinx.coroutines.delay(5 * 60 * 1000L) // 5 minutos
                loadHomeData()
            }
        }
    }

    fun refreshData() {
        _message.value = "Actualizando datos..."
        loadHomeData()
    }

    fun markMealAsConsumed() {
        viewModelScope.launch {
            try {
                val currentUserValue = userRepository.getCurrentUser()
                val nextMeal = _nextMealInfo.value

                if (currentUserValue != null && nextMeal != null) {
                    // Buscar una comida ejemplo para el tipo de comida actual
                    val availableMeals = dietRepository.getMealsByType(nextMeal.mealType)
                    val mealToConsume = availableMeals.firstOrNull()

                    if (mealToConsume != null) {
                        // Crear registro de consumo
                        val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
                        val consumption = MealConsumption(
                            id = "${currentUserValue.id}_${mealToConsume.id}_${System.currentTimeMillis()}",
                            userId = currentUserValue.id,
                            mealId = mealToConsume.id,
                            mealType = nextMeal.mealType,
                            date = today,
                            ironContent = mealToConsume.ironContent,
                            calories = mealToConsume.calories,
                            vitaminC = mealToConsume.vitaminC,
                            folate = mealToConsume.folate,
                            consumedAt = System.currentTimeMillis()
                        )

                        // Guardar consumo
                        val result = mealConsumptionRepository.insertMealConsumption(consumption)

                        result.fold(
                            onSuccess = {
                                _message.value = "¡${getMealDisplayName(nextMeal.mealType)} marcada como consumida! ✅"

                                // Recargar datos para actualizar progreso y próxima comida
                                loadHomeData()
                            },
                            onFailure = { exception ->
                                _message.value = "Error al marcar como consumida: ${exception.message}"
                            }
                        )
                    } else {
                        _message.value = "No se encontró una comida para ${getMealDisplayName(nextMeal.mealType)}"
                    }
                } else {
                    _message.value = "No se puede marcar como consumida en este momento"
                }
            } catch (e: Exception) {
                _message.value = "Error inesperado: ${e.message}"
            }
        }
    }

    fun clearMessage() {
        _message.value = null
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
}