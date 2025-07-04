package com.upao.nutrialarm.domain.usecase.auth

import android.util.Log
import com.upao.nutrialarm.data.remote.firebase.AuthService
import com.upao.nutrialarm.data.local.preferences.PreferencesManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LogoutUseCase @Inject constructor(
    private val authService: AuthService,
    private val preferencesManager: PreferencesManager
) {
    companion object {
        private const val TAG = "LogoutUseCase"
    }

    suspend operator fun invoke(): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            Log.d(TAG, "Iniciando proceso de logout...")

            // 1. Obtener información del usuario antes de cerrar sesión
            val currentUserId = preferencesManager.currentUserId
            val currentUserEmail = preferencesManager.userEmail

            Log.d(TAG, "Cerrando sesión para usuario: $currentUserId ($currentUserEmail)")

            // 2. Cerrar sesión en Firebase
            authService.signOut()
            Log.d(TAG, "Sesión de Firebase cerrada exitosamente")

            // 3. Limpiar datos locales de sesión
            preferencesManager.clearUserSession()
            Log.d(TAG, "Datos de sesión local limpiados")

            // 4. Limpiar otros datos sensibles del usuario
            // preferencesManager.clearUserData()

            Log.d(TAG, "Logout completado exitosamente")
            Result.success(Unit)

        } catch (e: Exception) {
            Log.e(TAG, "Error durante el logout", e)

            try {
                preferencesManager.clearUserSession()
                Log.d(TAG, "Sesión local limpiada tras error")
            } catch (cleanupError: Exception) {
                Log.e(TAG, "Error al limpiar sesión local", cleanupError)
            }

            Result.failure(Exception("Error al cerrar sesión: ${e.message}"))
        }
    }
}