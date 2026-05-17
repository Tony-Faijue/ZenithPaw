package com.example.zenithpaw.roomdatabase.user

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(private val userDao: UserDao): UserRepository {
    override suspend fun insertUser(user: User) {
        userDao.insertUser(user)
    }
    override suspend fun upsertUser(user: User) {
        userDao.upsertUser(user)
    }
    override suspend fun deleteUser(user: User) {
        userDao.deleteUser(user)
    }
    override suspend fun updateUser(user: User): Int {
        return userDao.updateUser(user)
    }
    override suspend fun getUserById(userId: String): User? {
        return userDao.getUserById(userId)
    }
    override fun getUsers(): Flow<List<User>> {
        return userDao.getUsers()
    }
    override suspend fun deleteAllUsers(){
        return userDao.deleteAllUsers()
    }
}