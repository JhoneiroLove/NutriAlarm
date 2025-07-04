package com.upao.nutrialarm.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.upao.nutrialarm.domain.model.ActivityLevel
import com.upao.nutrialarm.domain.model.AnemiaRisk
import com.upao.nutrialarm.domain.usecase.auth.RegisterUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val registerUseCase: RegisterUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState: StateFlow<RegisterUiState> = _uiState.asStateFlow()

    fun register(
        email: String,
        password: String,
        confirmPassword: String,
        name: String,
        age: String,
        weight: String,
        height: String,
        activityLevel: ActivityLevel,
        anemiaRisk: AnemiaRisk
    ) {
        when {
            email.isBlank() -> {
                showError("El correo electrónico es requerido")
                return
            }
            !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                showError("El formato del correo electrónico no es válido")
                return
            }
            password.isBlank() -> {
                showError("La contraseña es requerida")
                return
            }
            password.length < 6 -> {
                showError("La contraseña debe tener al menos 6 caracteres")
                return
            }
            password != confirmPassword -> {
                showError("Las contraseñas no coinciden")
                return
            }
            name.isBlank() -> {
                showError("El nombre completo es requerido")
                return
            }
            name.length < 2 -> {
                showError("El nombre debe tener al menos 2 caracteres")
                return
            }
            age.isBlank() -> {
                showError("La edad es requerida")
                return
            }
            weight.isBlank() -> {
                showError("El peso es requerido")
                return
            }
            height.isBlank() -> {
                showError("La altura es requerida")
                return
            }
        }

        val ageInt = age.toIntOrNull()
        val weightDouble = weight.toDoubleOrNull()
        val heightDouble = height.toDoubleOrNull()

        when {
            ageInt == null || ageInt <= 0 || ageInt > 120 -> {
                showError("La edad debe ser un número válido entre 1 y 120 años")
                return
            }
            weightDouble == null || weightDouble <= 0 || weightDouble > 300 -> {
                showError("El peso debe ser un número válido entre 1 y 300 kg")
                return
            }
            heightDouble == null || heightDouble <= 0 || heightDouble > 250 -> {
                showError("La altura debe ser un número válido entre 1 y 250 cm")
                return
            }
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                errorMessage = null
            )

            try {
                val result = registerUseCase(
                    email = email.trim(),
                    password = password,
                    name = name.trim(),
                    age = ageInt!!,
                    weight = weightDouble!!,
                    height = heightDouble!!,
                    activityLevel = activityLevel,
                    anemiaRisk = anemiaRisk
                )

                result.fold(
                    onSuccess = { user ->
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            isRegistrationSuccessful = true,
                            user = user,
                            errorMessage = null
                        )
                    },
                    onFailure = { exception ->
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            errorMessage = exception.message ?: "Error de registro desconocido"
                        )
                    }
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "Error inesperado: ${e.message}"
                )
            }
        }
    }

    private fun showError(message: String) {
        _uiState.value = _uiState.value.copy(errorMessage = message)
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }

    fun resetRegistrationState() {
        _uiState.value = _uiState.value.copy(
            isRegistrationSuccessful = false,
            errorMessage = null
        )
    }
}

data class RegisterUiState(
    val isLoading: Boolean = false,
    val isRegistrationSuccessful: Boolean = false,
    val errorMessage: String? = null,
    val user: com.upao.nutrialarm.domain.model.User? = null
)