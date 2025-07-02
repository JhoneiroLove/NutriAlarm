package com.upao.nutrialarm.data.repository

import com.upao.nutrialarm.data.local.database.dao.UserDao
import com.upao.nutrialarm.data.remote.firebase.FirestoreService
import com.upao.nutrialarm.domain.model.User
import com.upao.nutrialarm.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepositoryImpl @Inject constructor(
    private val userDao: UserDao,
    private val firestoreService: FirestoreService
) : UserRepository {

    override suspend fun saveUser(user: User): Result<User> {
        return try {
            userDao.insertUser(user.toEntity())
            firestoreService.saveUser(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getCurrentUser(): User? {
        return userDao.getCurrentUser()?.toDomain()
    }

    override fun getCurrentUserFlow(): Flow<User?> {
        return userDao.getCurrentUserFlow().map { it?.toDomain() }
    }

    override suspend fun getUserById(userId: String): User? {
        return userDao.getUserById(userId)?.toDomain()
    }

    override suspend fun updateUser(user: User): Result<User> {
        return try {
            userDao.updateUser(user.toEntity())
            firestoreService.saveUser(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteUser(user: User): Result<Unit> {
        return try {
            userDao.deleteUser(user.toEntity())
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}