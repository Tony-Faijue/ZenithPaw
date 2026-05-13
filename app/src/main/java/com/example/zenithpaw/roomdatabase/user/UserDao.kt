package com.example.zenithpaw.roomdatabase.user

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.OnConflictStrategy.Companion.ABORT
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Insert(onConflict = ABORT)
    suspend fun insertUser(user: User)

    //Cloud Sync
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertUser(user: User)

    @Delete()
    suspend fun deleteUser(user: User)

    @Update(onConflict = ABORT)
    suspend fun updateUser(user: User): Int

    @Query("SELECT * FROM users WHERE userId = :userId")
    suspend fun getUserById(userId: String): User?

    @Query("SELECT * FROM users")
    fun getUsers(): Flow<List<User>>
}
