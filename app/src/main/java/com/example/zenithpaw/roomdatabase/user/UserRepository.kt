package com.example.zenithpaw.roomdatabase.user

import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun insertUser(user: User)
    suspend fun upsertUser(user: User)
    suspend fun deleteUser(user: User)
    suspend fun updateUser(user: User): Int
    suspend fun getUserById(userId: String): User?
    fun getUsers(): Flow<List<User>>
}

