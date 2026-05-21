package com.example.zenithpaw.roomdatabase.task

import kotlinx.coroutines.flow.Flow

interface TaskRepository {
    suspend fun insertTask(task: Task)
    suspend fun upsertTask(task: Task)
    suspend fun upsertTasks(tasks: List<Task>)
    suspend fun deleteTask(task: Task)
    suspend fun updateTask(task: Task): Int
    suspend fun getTaskById(taskId: String): Task
    fun getTasksForUser(userId: String): Flow<List<Task>>
    fun getActiveTaskForUser(userId: String): Flow<Task>
}
