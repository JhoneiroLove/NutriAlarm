package com.upao.nutrialarm.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.upao.nutrialarm.domain.model.User
import com.upao.nutrialarm.domain.repository.UserRepository
import com.upao.nutrialarm.domain.usecase.user.HealthStatus
import com.upao.nutrialarm.domain.usecase.user.UserManagementUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HealthStatusViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val userManagementUseCase: UserManagementUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(HealthStatusUiState())
    val uiState: StateFlow<HealthStatusUiState> = _uiState.asStateFlow()

    fun loadHealthStatus() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)

            try {
                val user = userRepository.getCurrentUser()
                if (user != null) {
                    val healthStatus = userManagementUseCase.getUserHealthStatus(user)

                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        user = user,
                        healthStatus = healthStatus,
                        errorMessage = null
                    )
                } else {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = "No se encontró información del usuario"
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "Error al cargar el estado de salud: ${e.message}"
                )
            }
        }
    }

    fun refreshHealthStatus() {
        loadHealthStatus()
    }
}

data class HealthStatusUiState(
    val user: User? = null,
    val healthStatus: HealthStatus? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)