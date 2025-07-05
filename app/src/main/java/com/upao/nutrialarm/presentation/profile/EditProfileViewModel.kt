package com.upao.nutrialarm.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.upao.nutrialarm.domain.model.ActivityLevel
import com.upao.nutrialarm.domain.model.AnemiaRisk
import com.upao.nutrialarm.domain.model.User
import com.upao.nutrialarm.domain.repository.UserRepository
import com.upao.nutrialarm.util.ValidationUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(EditProfileUiState())
    val uiState: StateFlow<EditProfileUiState> = _uiState.asStateFlow()

    init {
        loadCurrentUser()
    }

    private fun loadCurrentUser() {
        viewModelScope.launch {
            try {
                val user = userRepository.getCurrentUser()
                _uiState.value = _uiState.value.copy(user = user)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = "Error al cargar datos del usuario"
                )
            }
        }
    }

    fun updateUserProfile(
        name: String,
        age: String,
        weight: String,
        height: String,
        activityLevel: ActivityLevel,
        anemiaRisk: AnemiaRisk
    ) {
        // Validaciones
        val nameValidation = ValidationUtils.validateName(name)
        if (!nameValidation.isValid) {
            _uiState.value = _uiState.value.copy(errorMessage = nameValidation.errorMessage)
            return
        }

        val ageValidation = ValidationUtils.validateAge(age)
        if (!ageValidation.isValid) {
            _uiState.value = _uiState.value.copy(errorMessage = ageValidation.errorMessage)
            return
        }

        val weightValidation = ValidationUtils.validateWeight(weight)
        if (!weightValidation.isValid) {
            _uiState.value = _uiState.value.copy(errorMessage = weightValidation.errorMessage)
            return
        }

        val heightValidation = ValidationUtils.validateHeight(height)
        if (!heightValidation.isValid) {
            _uiState.value = _uiState.value.copy(errorMessage = heightValidation.errorMessage)
            return
        }

        val ageInt = age.toIntOrNull()
        val weightDouble = weight.toDoubleOrNull()
        val heightDouble = height.toDoubleOrNull()

        if (ageInt == null || weightDouble == null || heightDouble == null) {
            _uiState.value = _uiState.value.copy(
                errorMessage = "Por favor, verifica que todos los campos numéricos sean válidos"
            )
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                errorMessage = null
            )

            try {
                val currentUser = _uiState.value.user
                if (currentUser != null) {
                    val updatedUser = currentUser.copy(
                        name = name.trim(),
                        age = ageInt,
                        weight = weightDouble,
                        height = heightDouble,
                        activityLevel = activityLevel,
                        anemiaRisk = anemiaRisk
                    )

                    val result = userRepository.updateUser(updatedUser)

                    result.fold(
                        onSuccess = { savedUser ->
                            _uiState.value = _uiState.value.copy(
                                isLoading = false,
                                user = savedUser,
                                isUpdateSuccessful = true,
                                errorMessage = null
                            )
                        },
                        onFailure = { exception ->
                            _uiState.value = _uiState.value.copy(
                                isLoading = false,
                                errorMessage = "Error al guardar cambios: ${exception.message}"
                            )
                        }
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
                    errorMessage = "Error inesperado: ${e.message}"
                )
            }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }

    fun resetUpdateState() {
        _uiState.value = _uiState.value.copy(isUpdateSuccessful = false)
    }
}

data class EditProfileUiState(
    val user: User? = null,
    val isLoading: Boolean = false,
    val isUpdateSuccessful: Boolean = false,
    val errorMessage: String? = null
)