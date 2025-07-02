package com.upao.nutrialarm.domain.repository

import com.upao.nutrialarm.domain.model.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun saveUser(user: User): Result<User>
    suspend fun getCurrentUser(): User?
    fun getCurrentUserFlow(): Flow<User?>
    suspend fun getUserById(userId: String): User?
    suspend fun updateUser(user: User): Result<User>
    suspend fun deleteUser(user: User): Result<Unit>
}