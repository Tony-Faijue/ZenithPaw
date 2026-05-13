package com.example.zenithpaw.roomdatabase.task

import kotlinx.coroutines.flow.Flow

class TaskRepositoryImpl(private val taskDao: TaskDao) : TaskRepository {
    override suspend fun insertTask(task: Task) {
        taskDao.insertTask(task)
    }
    override suspend fun upsertTask(task: Task) {
        taskDao.upsertTask(task)
    }
    override suspend fun deleteTask(task: Task) {
        taskDao.deleteTask(task)
    }
    override suspend fun updateTask(task: Task): Int {
        return taskDao.updateTask(task)
    }
    override suspend fun getTaskById(taskId: String): Task {
        return taskDao.getTaskById(taskId)
    }
    override fun getTasksForUser(userId: String): Flow<List<Task>> {
        return taskDao.getTasksForUser(userId)
    }
}