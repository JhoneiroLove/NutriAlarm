package com.upao.nutrialarm.presentation.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.upao.nutrialarm.data.local.preferences.PreferencesManager
import com.upao.nutrialarm.data.local.preferences.getDailyIronBonus
import com.upao.nutrialarm.data.local.preferences.saveDailyIronBonus
import com.upao.nutrialarm.data.local.preferences.cleanupOldIronBonuses
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
    private val dietRepository: DietRepository,
    private val preferencesManager: PreferencesManager
) : ViewModel() {

    companion object {
        private const val TAG = "HomeViewModel"
        private const val REWARDED_AD_IRON_BONUS = 2.0
    }

    private val _nextMealInfo = MutableStateFlow<NextMealInfo?>(null)
    val nextMealInfo: StateFlow<NextMealInfo?> = _nextMealInfo.asStateFlow()

    private val _dailyProgress = MutableStateFlow<DailyProgress?>(null)
    val dailyProgress: StateFlow<DailyProgress?> = _dailyProgress.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _message = MutableStateFlow<String?>(null)
    val message: StateFlow<String?> = _message.asStateFlow()

    private val _dailyIronBonus = MutableStateFlow(0.0)
    val dailyIronBonus: StateFlow<Double> = _dailyIronBonus.asStateFlow()

    // Exponer el usuario actual como StateFlow
    val currentUser: StateFlow<User?> = userRepository.getCurrentUserFlow()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    init {
        loadHomeData()
        loadDailyIronBonus()
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

                    val bonusAdjustedProgress = progress?.let { original ->
                        val ironWithBonus = original.iron.copy(
                            current = original.iron.current + _dailyIronBonus.value
                        )
                        original.copy(iron = ironWithBonus)
                    }
                    _dailyProgress.value = bonusAdjustedProgress ?: progress
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
        loadDailyIronBonus() // AGREGADO SOLO ESTO
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

    // ===== FUNCIONES AGREGADAS SOLO PARA ADMOB =====

    /**
     * Función para agregar bonus de hierro desde anuncios con recompensa
     */
    fun addIronBonus(bonusAmount: Double = REWARDED_AD_IRON_BONUS) {
        viewModelScope.launch {
            try {
                val newBonus = _dailyIronBonus.value + bonusAmount
                _dailyIronBonus.value = newBonus

                // Guardar en preferencias
                preferencesManager.saveDailyIronBonus(newBonus)

                // Actualizar el progreso inmediatamente
                loadHomeData()

                _message.value = "¡Bonus de +${bonusAmount}mg de hierro añadido! 🩸"

                Log.d(TAG, "Iron bonus added: +${bonusAmount}mg, total: ${newBonus}mg")
            } catch (e: Exception) {
                Log.e(TAG, "Error adding iron bonus", e)
                _message.value = "Error al aplicar bonus de hierro"
            }
        }
    }

    private fun loadDailyIronBonus() {
        viewModelScope.launch {
            try {
                val savedBonus = preferencesManager.getDailyIronBonus()
                _dailyIronBonus.value = savedBonus

                Log.d(TAG, "Loaded daily iron bonus: ${savedBonus}mg")

                // Limpiar bonus antiguos periódicamente
                preferencesManager.cleanupOldIronBonuses()
            } catch (e: Exception) {
                Log.e(TAG, "Error loading daily iron bonus", e)
                _dailyIronBonus.value = 0.0
            }
        }
    }

    /**
     * Función para resetear el bonus diario (útil para testing)
     */
    fun resetDailyIronBonus() {
        viewModelScope.launch {
            _dailyIronBonus.value = 0.0
            preferencesManager.saveDailyIronBonus(0.0)

            loadHomeData()
            _message.value = "Bonus de hierro diario reseteado"
            Log.d(TAG, "Daily iron bonus reset to 0.0mg")
        }
    }
}