package com.example.zenithpaw.roomdatabase.task

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.OnConflictStrategy.Companion.ABORT
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {
    @Insert(onConflict = ABORT)
    suspend fun insertTask(task: Task)

    //Cloud Sync
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertTask(task: Task)

    @Delete
    suspend fun deleteTask(task: Task)

    @Update(onConflict = ABORT)
    suspend fun updateTask(task: Task): Int

    @Query("SELECT * FROM tasks WHERE taskId = :taskId")
    suspend fun getTaskById(taskId: String): Task?

    @Query("SELECT * FROM tasks WHERE userId = :userId")
    fun getTasksForUser(userId: String): Flow<List<Task>>
}
