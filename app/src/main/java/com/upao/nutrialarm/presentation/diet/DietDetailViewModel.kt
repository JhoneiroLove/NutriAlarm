package com.upao.nutrialarm.presentation.diet

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.upao.nutrialarm.domain.model.Diet
import com.upao.nutrialarm.domain.repository.DietRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DietDetailViewModel @Inject constructor(
    private val dietRepository: DietRepository
) : ViewModel() {

    private val _diet = MutableStateFlow<Diet?>(null)
    val diet: StateFlow<Diet?> = _diet.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    fun loadDiet(dietId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            try {
                val result = dietRepository.getDietWithMeals(dietId)
                if (result != null) {
                    _diet.value = result
                } else {
                    _error.value = "Dieta no encontrada"
                }
            } catch (e: Exception) {
                _error.value = "Error al cargar la dieta: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun clearError() {
        _error.value = null
    }
}