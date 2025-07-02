package com.upao.nutrialarm.domain.usecase.auth

import com.upao.nutrialarm.data.remote.firebase.AuthService
import com.upao.nutrialarm.domain.model.User
import com.upao.nutrialarm.domain.repository.UserRepository
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val authService: AuthService,
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(email: String, password: String): Result<User> {
        return try {
            val firebaseResult = authService.signInWithEmailAndPassword(email, password)

            firebaseResult.fold(
                onSuccess = { firebaseUser ->
                    // Buscar usuario en la base de datos local
                    val localUser = userRepository.getUserById(firebaseUser.uid)

                    localUser?.let {
                        Result.success(it)
                    } ?: Result.failure(Exception("Usuario no encontrado localmente"))
                },
                onFailure = { exception ->
                    Result.failure(exception)
                }
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}