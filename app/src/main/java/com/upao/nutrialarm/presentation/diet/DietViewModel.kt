package com.upao.nutrialarm.presentation.diet

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.upao.nutrialarm.domain.model.AnemiaRisk
import com.upao.nutrialarm.domain.model.Diet
import com.upao.nutrialarm.domain.repository.DietRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DietViewModel @Inject constructor(
    private val dietRepository: DietRepository
) : ViewModel() {

    private val _diets = MutableStateFlow<List<Diet>>(emptyList())
    val diets: StateFlow<List<Diet>> = _diets.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    init {
        loadDiets()
    }

    fun loadDiets() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            try {
                val result = dietRepository.getAllDiets()
                _diets.value = result
            } catch (e: Exception) {
                _error.value = "Error al cargar las dietas: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun getDietsByRiskLevel(riskLevel: AnemiaRisk) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            try {
                val result = dietRepository.getDietsByRiskLevel(riskLevel)
                _diets.value = result
            } catch (e: Exception) {
                _error.value = "Error al filtrar dietas: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun clearError() {
        _error.value = null
    }
}