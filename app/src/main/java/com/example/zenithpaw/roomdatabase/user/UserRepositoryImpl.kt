package com.example.zenithpaw.roomdatabase.user

import com.example.zenithpaw.roomdatabase.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Repository class for handling user data operations
 * @param userDao The data access object for user data
 * @param dispatcher The coroutine dispatcher for performing database operations
 */
class UserRepositoryImpl @Inject constructor(
    private val userDao: UserDao,
   @IoDispatcher private val dispatcher: CoroutineDispatcher
): UserRepository {
    override suspend fun insertUser(user: User) = withContext(dispatcher) {
        userDao.insertUser(user)
    }
    override suspend fun upsertUser(user: User) = withContext(dispatcher) {
        userDao.upsertUser(user)
    }
    override suspend fun deleteUser(user: User) = withContext(dispatcher) {
        userDao.deleteUser(user)
    }
    override suspend fun updateUser(user: User): Int = withContext(dispatcher) {
        return@withContext userDao.updateUser(user)
    }
    override suspend fun getUserById(userId: String): User? = withContext(dispatcher) {
        return@withContext userDao.getUserById(userId)
    }
    override fun getUsers(): Flow<List<User>> {
        return userDao.getUsers()
    }
    override suspend fun deleteAllUsers() = withContext(dispatcher) {
        return@withContext userDao.deleteAllUsers()
    }
}