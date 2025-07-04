package com.upao.nutrialarm.domain.usecase.auth

import android.util.Log
import com.upao.nutrialarm.data.remote.firebase.AuthService
import com.upao.nutrialarm.data.remote.firebase.FirestoreService
import com.upao.nutrialarm.data.local.preferences.PreferencesManager
import com.upao.nutrialarm.domain.model.User
import com.upao.nutrialarm.domain.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val authService: AuthService,
    private val userRepository: UserRepository,
    private val firestoreService: FirestoreService,
    private val preferencesManager: PreferencesManager
) {
    companion object {
        private const val TAG = "LoginUseCase"
    }

    suspend operator fun invoke(email: String, password: String): Result<User> = withContext(Dispatchers.IO) {
        try {
            Log.d(TAG, "Iniciando proceso de login para: $email")

            // 1. Autenticar en Firebase
            val firebaseResult = authService.signInWithEmailAndPassword(email, password)

            firebaseResult.fold(
                onSuccess = { firebaseUser ->
                    Log.d(TAG, "Autenticación exitosa en Firebase: ${firebaseUser.uid}")

                    // 2. Buscar usuario en base de datos local
                    var localUser = userRepository.getUserById(firebaseUser.uid)

                    if (localUser == null) {
                        Log.d(TAG, "Usuario no encontrado localmente, buscando en Firestore...")

                        // 3. Si no está local, buscar en Firestore
                        val firestoreResult = firestoreService.getUser(firebaseUser.uid)

                        firestoreResult.fold(
                            onSuccess = { firestoreUser ->
                                if (firestoreUser != null) {
                                    Log.d(TAG, "Usuario encontrado en Firestore, guardando localmente")

                                    // Guardar en BD local
                                    val saveResult = userRepository.saveUser(firestoreUser)
                                    saveResult.fold(
                                        onSuccess = { savedUser ->
                                            localUser = savedUser
                                            Log.d(TAG, "Usuario sincronizado desde Firestore a BD local")
                                        },
                                        onFailure = { exception ->
                                            Log.w(TAG, "Error al guardar usuario desde Firestore: ${exception.message}")
                                            // Continuar con el usuario de Firestore aunque no se guarde localmente
                                            localUser = firestoreUser
                                        }
                                    )
                                } else {
                                    Log.e(TAG, "Usuario no encontrado en Firestore")
                                    return@withContext Result.failure(Exception("Cuenta no encontrada. Por favor, regístrate nuevamente."))
                                }
                            },
                            onFailure = { exception ->
                                Log.e(TAG, "Error al buscar usuario en Firestore: ${exception.message}")

                                // Como último recurso, crear un usuario básico con la info de Firebase
                                if (firebaseUser.displayName != null && firebaseUser.email != null) {
                                    Log.d(TAG, "Creando usuario básico desde información de Firebase")

                                    localUser = User(
                                        id = firebaseUser.uid,
                                        email = firebaseUser.email!!,
                                        name = firebaseUser.displayName ?: "Usuario",
                                        age = 18, // Valores por defecto
                                        weight = 60.0,
                                        height = 160.0,
                                        activityLevel = com.upao.nutrialarm.domain.model.ActivityLevel.SEDENTARY,
                                        anemiaRisk = com.upao.nutrialarm.domain.model.AnemiaRisk.LOW,
                                        createdAt = System.currentTimeMillis()
                                    )

                                    // Intentar guardar el usuario creado
                                    try {
                                        userRepository.saveUser(localUser!!)
                                        Log.d(TAG, "Usuario básico guardado exitosamente")
                                    } catch (e: Exception) {
                                        Log.w(TAG, "Error al guardar usuario básico: ${e.message}")
                                    }
                                } else {
                                    return@withContext Result.failure(Exception("No se pudo recuperar la información de la cuenta"))
                                }
                            }
                        )
                    } else {
                        Log.d(TAG, "Usuario encontrado en BD local: ${localUser!!.id}")
                    }

                    // 4. Guardar sesión
                    localUser?.let { user ->
                        preferencesManager.saveUserSession(
                            userId = user.id,
                            email = user.email,
                            name = user.name
                        )

                        Log.d(TAG, "Login completado exitosamente para: ${user.email}")
                        Result.success(user)
                    } ?: run {
                        Log.e(TAG, "No se pudo obtener información del usuario")
                        Result.failure(Exception("Error al obtener información del usuario"))
                    }
                },
                onFailure = { exception ->
                    Log.e(TAG, "Error en autenticación Firebase", exception)
                    Result.failure(exception)
                }
            )

        } catch (e: Exception) {
            Log.e(TAG, "Error inesperado durante el login", e)
            Result.failure(Exception("Error inesperado durante el login: ${e.message}"))
        }
    }
}