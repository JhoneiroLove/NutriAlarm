package com.upao.nutrialarm.domain.usecase.auth

import com.upao.nutrialarm.data.remote.firebase.AuthService
import com.upao.nutrialarm.domain.model.User
import com.upao.nutrialarm.domain.model.ActivityLevel
import com.upao.nutrialarm.domain.model.AnemiaRisk
import com.upao.nutrialarm.domain.repository.UserRepository
import javax.inject.Inject

class RegisterUseCase @Inject constructor(
    private val authService: AuthService,
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(
        email: String,
        password: String,
        name: String,
        age: Int,
        weight: Double,
        height: Double,
        activityLevel: ActivityLevel,
        anemiaRisk: AnemiaRisk
    ): Result<User> {
        return try {
            val firebaseResult = authService.createUserWithEmailAndPassword(email, password, name)

            firebaseResult.fold(
                onSuccess = { firebaseUser ->
                    // Crear usuario en la base de datos
                    val user = User(
                        id = firebaseUser.uid,
                        email = email,
                        name = name,
                        age = age,
                        weight = weight,
                        height = height,
                        activityLevel = activityLevel,
                        anemiaRisk = anemiaRisk
                    )

                    userRepository.saveUser(user)
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