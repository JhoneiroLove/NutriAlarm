package com.upao.nutrialarm.presentation.navigation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.upao.nutrialarm.data.remote.firebase.AuthService
import com.upao.nutrialarm.data.local.preferences.PreferencesManager
import com.upao.nutrialarm.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SessionViewModel @Inject constructor(
    private val authService: AuthService,
    private val preferencesManager: PreferencesManager,
    private val userRepository: UserRepository
) : ViewModel() {

    companion object {
        private const val TAG = "SessionViewModel"
    }

    private val _authState = MutableStateFlow<AuthState>(AuthState.Loading)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    fun checkAuthState() {
        viewModelScope.launch {
            try {
                Log.d(TAG, "Verificando estado de autenticación...")

                // 1. Verificar si hay sesión en Firebase
                val hasFirebaseSession = authService.hasActiveSession()
                Log.d(TAG, "Sesión Firebase activa: $hasFirebaseSession")

                // 2. Verificar si hay sesión local
                val hasLocalSession = preferencesManager.hasValidSession()
                Log.d(TAG, "Sesión local válida: $hasLocalSession")

                // 3. Log de información de sesión para debug
                Log.d(TAG, "Info de sesión: ${preferencesManager.getSessionInfo()}")

                when {
                    // Caso ideal: ambas sesiones activas
                    hasFirebaseSession && hasLocalSession -> {
                        val userId = preferencesManager.currentUserId
                        Log.d(TAG, "Ambas sesiones activas, verificando usuario: $userId")

                        if (userId != null) {
                            // Verificar que el usuario existe en BD local
                            val user = userRepository.getUserById(userId)
                            if (user != null) {
                                Log.d(TAG, "Usuario autenticado encontrado: ${user.email}")
                                _authState.value = AuthState.Authenticated(user)
                            } else {
                                Log.w(TAG, "Usuario no encontrado en BD local, cerrando sesión")
                                logout()
                            }
                        } else {
                            Log.w(TAG, "ID de usuario nulo, cerrando sesión")
                            logout()
                        }
                    }

                    // Solo sesión Firebase, intentar recuperar sesión local
                    hasFirebaseSession && !hasLocalSession -> {
                        Log.d(TAG, "Solo sesión Firebase, intentando recuperar datos...")

                        val firebaseUser = authService.currentUser
                        if (firebaseUser != null) {
                            // Buscar usuario en BD local
                            val localUser = userRepository.getUserById(firebaseUser.uid)

                            if (localUser != null) {
                                // Restaurar sesión local
                                preferencesManager.saveUserSession(
                                    userId = localUser.id,
                                    email = localUser.email,
                                    name = localUser.name
                                )
                                Log.d(TAG, "Sesión local restaurada para: ${localUser.email}")
                                _authState.value = AuthState.Authenticated(localUser)
                            } else {
                                Log.w(TAG, "Usuario Firebase no encontrado localmente")
                                logout()
                            }
                        } else {
                            Log.w(TAG, "Usuario Firebase nulo")
                            _authState.value = AuthState.Unauthenticated
                        }
                    }

                    // Solo sesión local, verificar Firebase
                    !hasFirebaseSession && hasLocalSession -> {
                        Log.w(TAG, "Solo sesión local sin Firebase, cerrando sesión")
                        logout()
                    }

                    // Sin sesiones
                    else -> {
                        Log.d(TAG, "Sin sesiones activas")
                        _authState.value = AuthState.Unauthenticated
                    }
                }

            } catch (e: Exception) {
                Log.e(TAG, "Error al verificar estado de autenticación", e)
                _authState.value = AuthState.Unauthenticated
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            try {
                Log.d(TAG, "Iniciando proceso de logout...")

                // 1. Cerrar sesión en Firebase
                authService.signOut()
                Log.d(TAG, "Sesión Firebase cerrada")

                // 2. Limpiar sesión local
                preferencesManager.clearUserSession()
                Log.d(TAG, "Sesión local limpiada")

                // 3. Actualizar estado
                _authState.value = AuthState.Unauthenticated
                Log.d(TAG, "Logout completado")

            } catch (e: Exception) {
                Log.e(TAG, "Error durante logout", e)
                // Aunque haya error, marcar como no autenticado
                _authState.value = AuthState.Unauthenticated
            }
        }
    }

    fun refreshAuthState() {
        checkAuthState()
    }

    // Función para cuando se complete login/registro exitoso
    fun onAuthenticationSuccess(user: com.upao.nutrialarm.domain.model.User) {
        Log.d(TAG, "Autenticación exitosa para: ${user.email}")
        _authState.value = AuthState.Authenticated(user)
    }
}

// Estados de autenticación
sealed class AuthState {
    object Loading : AuthState()
    object Unauthenticated : AuthState()
    data class Authenticated(val user: com.upao.nutrialarm.domain.model.User) : AuthState()
}