package com.upao.nutrialarm.domain.usecase.user

import com.upao.nutrialarm.domain.model.User
import com.upao.nutrialarm.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCurrentUserUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(): User? {
        return userRepository.getCurrentUser()
    }

    fun getFlow(): Flow<User?> {
        return userRepository.getCurrentUserFlow()
    }
}