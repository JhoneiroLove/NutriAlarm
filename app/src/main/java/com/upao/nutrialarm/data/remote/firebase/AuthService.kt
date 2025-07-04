package com.upao.nutrialarm.data.remote.firebase

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthService @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) {

    companion object {
        private const val TAG = "AuthService"
    }

    val currentUser: FirebaseUser?
        get() = firebaseAuth.currentUser

    val currentUserId: String?
        get() = firebaseAuth.currentUser?.uid

    val isUserSignedIn: Boolean
        get() = firebaseAuth.currentUser != null

    suspend fun signInWithEmailAndPassword(email: String, password: String): Result<FirebaseUser> =
        withContext(Dispatchers.IO) {
            try {
                Log.d(TAG, "Intentando iniciar sesión con email: $email")

                val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()

                result.user?.let { user ->
                    Log.d(TAG, "Inicio de sesión exitoso para usuario: ${user.uid}")
                    Result.success(user)
                } ?: Result.failure(Exception("Error de autenticación: Usuario nulo"))

            } catch (e: Exception) {
                Log.e(TAG, "Error en inicio de sesión: ${e.message}", e)
                val errorMessage = when {
                    e.message?.contains("password is invalid") == true ->
                        "Contraseña incorrecta"
                    e.message?.contains("no user record") == true ->
                        "No existe una cuenta con este correo electrónico"
                    e.message?.contains("email address is badly formatted") == true ->
                        "Formato de correo electrónico inválido"
                    e.message?.contains("network error") == true ->
                        "Error de conexión. Verifica tu internet"
                    else -> "Error de autenticación: ${e.localizedMessage ?: "Error desconocido"}"
                }
                Result.failure(Exception(errorMessage))
            }
        }

    suspend fun createUserWithEmailAndPassword(
        email: String,
        password: String,
        displayName: String
    ): Result<FirebaseUser> = withContext(Dispatchers.IO) {
        try {
            Log.d(TAG, "Creando usuario con email: $email")

            val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()

            result.user?.let { user ->
                Log.d(TAG, "Usuario creado exitosamente: ${user.uid}")

                // Actualizar perfil con nombre
                val profileUpdates = UserProfileChangeRequest.Builder()
                    .setDisplayName(displayName)
                    .build()

                user.updateProfile(profileUpdates).await()
                Log.d(TAG, "Perfil actualizado con nombre: $displayName")

                Result.success(user)
            } ?: Result.failure(Exception("Error al crear usuario: Usuario nulo"))

        } catch (e: Exception) {
            Log.e(TAG, "Error al crear usuario: ${e.message}", e)
            val errorMessage = when {
                e.message?.contains("email address is already in use") == true ->
                    "Ya existe una cuenta con este correo electrónico"
                e.message?.contains("password should be at least 6 characters") == true ->
                    "La contraseña debe tener al menos 6 caracteres"
                e.message?.contains("email address is badly formatted") == true ->
                    "Formato de correo electrónico inválido"
                e.message?.contains("network error") == true ->
                    "Error de conexión. Verifica tu internet"
                else -> "Error al crear la cuenta: ${e.localizedMessage ?: "Error desconocido"}"
            }
            Result.failure(Exception(errorMessage))
        }
    }

    suspend fun sendPasswordResetEmail(email: String): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            firebaseAuth.sendPasswordResetEmail(email).await()
            Log.d(TAG, "Email de recuperación enviado a: $email")
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e(TAG, "Error al enviar email de recuperación: ${e.message}", e)
            val errorMessage = when {
                e.message?.contains("no user record") == true ->
                    "No existe una cuenta con este correo electrónico"
                e.message?.contains("email address is badly formatted") == true ->
                    "Formato de correo electrónico inválido"
                else -> "Error al enviar email de recuperación: ${e.localizedMessage}"
            }
            Result.failure(Exception(errorMessage))
        }
    }

    suspend fun updateProfile(displayName: String): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val user = firebaseAuth.currentUser
            user?.let {
                val profileUpdates = UserProfileChangeRequest.Builder()
                    .setDisplayName(displayName)
                    .build()
                it.updateProfile(profileUpdates).await()
                Log.d(TAG, "Perfil actualizado exitosamente")
                Result.success(Unit)
            } ?: Result.failure(Exception("Usuario no autenticado"))
        } catch (e: Exception) {
            Log.e(TAG, "Error al actualizar perfil: ${e.message}", e)
            Result.failure(Exception("Error al actualizar perfil: ${e.localizedMessage}"))
        }
    }

    fun signOut() {
        try {
            val userId = currentUserId
            firebaseAuth.signOut()
            Log.d(TAG, "Sesión cerrada para usuario: $userId")
        } catch (e: Exception) {
            Log.e(TAG, "Error al cerrar sesión: ${e.message}", e)
        }
    }

    // Función para verificar si hay una sesión activa
    fun hasActiveSession(): Boolean {
        val isActive = firebaseAuth.currentUser != null
        Log.d(TAG, "Verificando sesión activa: $isActive")
        return isActive
    }

    // Función para reautenticar usuario actual
    suspend fun reloadCurrentUser(): Result<FirebaseUser> = withContext(Dispatchers.IO) {
        try {
            val user = firebaseAuth.currentUser
            user?.let {
                it.reload().await()
                Result.success(it)
            } ?: Result.failure(Exception("No hay usuario autenticado"))
        } catch (e: Exception) {
            Log.e(TAG, "Error al recargar usuario: ${e.message}", e)
            Result.failure(e)
        }
    }
}