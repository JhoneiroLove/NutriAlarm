package com.upao.nutrialarm.util

import android.util.Patterns
import com.upao.nutrialarm.util.AppConstants

object ValidationUtils {

    data class ValidationResult(
        val isValid: Boolean,
        val errorMessage: String? = null
    )

    fun validateEmail(email: String): ValidationResult {
        return when {
            email.isBlank() -> ValidationResult(false, "El email es requerido")
            !Patterns.EMAIL_ADDRESS.matcher(email).matches() ->
                ValidationResult(false, "Email inválido")
            else -> ValidationResult(true)
        }
    }

    fun validatePassword(password: String): ValidationResult {
        return when {
            password.isBlank() -> ValidationResult(false, "La contraseña es requerida")
            password.length < 6 ->
                ValidationResult(false, "La contraseña debe tener al menos 6 caracteres")
            !password.any { it.isDigit() } ->
                ValidationResult(false, "La contraseña debe contener al menos un número")
            else -> ValidationResult(true)
        }
    }

    fun validateName(name: String): ValidationResult {
        return when {
            name.isBlank() -> ValidationResult(false, "El nombre es requerido")
            name.length < 2 -> ValidationResult(false, "El nombre debe tener al menos 2 caracteres")
            name.length > 50 -> ValidationResult(false, "El nombre es demasiado largo")
            !name.all { it.isLetter() || it.isWhitespace() } ->
                ValidationResult(false, "El nombre solo puede contener letras y espacios")
            else -> ValidationResult(true)
        }
    }

    fun validateAge(age: String): ValidationResult {
        val ageInt = age.toIntOrNull()
        return when {
            age.isBlank() -> ValidationResult(false, "La edad es requerida")
            ageInt == null -> ValidationResult(false, "Edad inválida")
            ageInt < AppConstants.MIN_AGE ->
                ValidationResult(false, "La edad mínima es ${AppConstants.MIN_AGE} años")
            ageInt > AppConstants.MAX_AGE ->
                ValidationResult(false, "La edad máxima es ${AppConstants.MAX_AGE} años")
            else -> ValidationResult(true)
        }
    }

    fun validateWeight(weight: String): ValidationResult {
        val weightDouble = weight.toDoubleOrNull()
        return when {
            weight.isBlank() -> ValidationResult(false, "El peso es requerido")
            weightDouble == null -> ValidationResult(false, "Peso inválido")
            weightDouble < AppConstants.MIN_WEIGHT ->
                ValidationResult(false, "El peso mínimo es ${AppConstants.MIN_WEIGHT} kg")
            weightDouble > AppConstants.MAX_WEIGHT ->
                ValidationResult(false, "El peso máximo es ${AppConstants.MAX_WEIGHT} kg")
            else -> ValidationResult(true)
        }
    }

    fun validateHeight(height: String): ValidationResult {
        val heightDouble = height.toDoubleOrNull()
        return when {
            height.isBlank() -> ValidationResult(false, "La altura es requerida")
            heightDouble == null -> ValidationResult(false, "Altura inválida")
            heightDouble < AppConstants.MIN_HEIGHT ->
                ValidationResult(false, "La altura mínima es ${AppConstants.MIN_HEIGHT} cm")
            heightDouble > AppConstants.MAX_HEIGHT ->
                ValidationResult(false, "La altura máxima es ${AppConstants.MAX_HEIGHT} cm")
            else -> ValidationResult(true)
        }
    }

    fun validatePasswordConfirmation(password: String, confirmPassword: String): ValidationResult {
        return when {
            confirmPassword.isBlank() ->
                ValidationResult(false, "Confirma tu contraseña")
            password != confirmPassword ->
                ValidationResult(false, "Las contraseñas no coinciden")
            else -> ValidationResult(true)
        }
    }
}