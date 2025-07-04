package com.upao.nutrialarm.domain.usecase.auth

import android.util.Log
import com.upao.nutrialarm.data.remote.firebase.AuthService
import com.upao.nutrialarm.data.local.preferences.PreferencesManager
import com.upao.nutrialarm.domain.model.User
import com.upao.nutrialarm.domain.model.ActivityLevel
import com.upao.nutrialarm.domain.model.AnemiaRisk
import com.upao.nutrialarm.domain.repository.UserRepository
import com.upao.nutrialarm.domain.usecase.meal.InitializeDefaultPreferencesUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RegisterUseCase @Inject constructor(
    private val authService: AuthService,
    private val userRepository: UserRepository,
    private val preferencesManager: PreferencesManager,
    private val initializeDefaultPreferencesUseCase: InitializeDefaultPreferencesUseCase
) {
    companion object {
        private const val TAG = "RegisterUseCase"
    }

    suspend operator fun invoke(
        email: String,
        password: String,
        name: String,
        age: Int,
        weight: Double,
        height: Double,
        activityLevel: ActivityLevel,
        anemiaRisk: AnemiaRisk
    ): Result<User> = withContext(Dispatchers.IO) {
        try {
            Log.d(TAG, "Iniciando proceso de registro para: $email")

            // 1. Crear usuario en Firebase
            val firebaseResult = authService.createUserWithEmailAndPassword(email, password, name)

            firebaseResult.fold(
                onSuccess = { firebaseUser ->
                    Log.d(TAG, "Usuario creado en Firebase: ${firebaseUser.uid}")

                    // 2. Crear objeto User para la base de datos local
                    val user = User(
                        id = firebaseUser.uid,
                        email = email,
                        name = name,
                        age = age,
                        weight = weight,
                        height = height,
                        activityLevel = activityLevel,
                        anemiaRisk = anemiaRisk,
                        createdAt = System.currentTimeMillis()
                    )

                    // 3. Guardar en base de datos local
                    val localSaveResult = userRepository.saveUser(user)

                    localSaveResult.fold(
                        onSuccess = { savedUser ->
                            Log.d(TAG, "Usuario guardado en BD local: ${savedUser.id}")

                            // 4. Guardar sesión en SharedPreferences
                            preferencesManager.saveUserSession(
                                userId = savedUser.id,
                                email = savedUser.email,
                                name = savedUser.name
                            )

                            // 5. Inicializar preferencias por defecto
                            try {
                                initializeDefaultPreferencesUseCase(savedUser.id)
                                Log.d(TAG, "Preferencias por defecto inicializadas")
                            } catch (e: Exception) {
                                Log.w(TAG, "Error al inicializar preferencias por defecto: ${e.message}")
                            }

                            // 6. Marcar onboarding como completado
                            preferencesManager.isUserOnboarded = true

                            Log.d(TAG, "Registro completado exitosamente para: ${savedUser.email}")
                            Result.success(savedUser)
                        },
                        onFailure = { exception ->
                            Log.e(TAG, "Error al guardar usuario en BD local", exception)

                            // Si falla el guardado local, eliminar de Firebase
                            try {
                                authService.signOut()
                                Log.d(TAG, "Usuario eliminado de Firebase debido a error local")
                            } catch (e: Exception) {
                                Log.e(TAG, "Error al limpiar Firebase después de fallo local", e)
                            }

                            Result.failure(Exception("Error al guardar datos localmente: ${exception.message}"))
                        }
                    )
                },
                onFailure = { exception ->
                    Log.e(TAG, "Error al crear usuario en Firebase", exception)
                    Result.failure(exception)
                }
            )

        } catch (e: Exception) {
            Log.e(TAG, "Error inesperado durante el registro", e)
            Result.failure(Exception("Error inesperado durante el registro: ${e.message}"))
        }
    }
}