package com.upao.nutrialarm.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.upao.nutrialarm.domain.usecase.meal.GetNextMealUseCase
import com.upao.nutrialarm.domain.usecase.meal.GetDailyProgressUseCase
import com.upao.nutrialarm.domain.usecase.meal.NextMealInfo
import com.upao.nutrialarm.domain.usecase.meal.DailyProgress
import com.upao.nutrialarm.presentation.profile.UserProfileViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getNextMealUseCase: GetNextMealUseCase,
    private val getDailyProgressUseCase: GetDailyProgressUseCase,
    private val userProfileViewModel: UserProfileViewModel
) : ViewModel() {

    private val _nextMealInfo = MutableStateFlow<NextMealInfo?>(null)
    val nextMealInfo: StateFlow<NextMealInfo?> = _nextMealInfo.asStateFlow()

    private val _dailyProgress = MutableStateFlow<DailyProgress?>(null)
    val dailyProgress: StateFlow<DailyProgress?> = _dailyProgress.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        loadHomeData()
        // Actualizar cada 5 minutos
        startPeriodicUpdates()
    }

    private fun loadHomeData() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val currentUser = userProfileViewModel.currentUser.value
                if (currentUser != null) {
                    // Cargar próxima comida
                    val nextMeal = getNextMealUseCase(currentUser.id)
                    _nextMealInfo.value = nextMeal

                    // Cargar progreso diario
                    val progress = getDailyProgressUseCase(currentUser.id)
                    _dailyProgress.value = progress
                }
            } catch (e: Exception) {
                // Handle error silently for now
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
        loadHomeData()
    }

    fun markMealAsConsumed(mealId: String) {
        viewModelScope.launch {
            try {
                val currentUser = userProfileViewModel.currentUser.value
                if (currentUser != null) {
                    // Aquí marcaríamos la comida como consumida
                    // TODO: Implementar markMealAsConsumed en repository

                    // Recargar datos después de marcar como consumida
                    loadHomeData()
                }
            } catch (e: Exception) {
                // Handle error
            }
        }
    }
}