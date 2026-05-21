package com.example.zenithpaw.ui.task

import com.example.zenithpaw.roomdatabase.task.Task
import com.example.zenithpaw.roomdatabase.task.TaskState

data class TaskUiState(
    val taskId: String = "",
    val title: String = "",
    val time: Long = 0,
    val description: String = "",
    val taskState: TaskState = TaskState.Pending,
    val userId: String = "",
)

/**
 * Convert the TaskEntity to TaskUiState
 */
fun Task.toTaskUiState(): TaskUiState{
    return TaskUiState(
        taskId = this.taskId,
        title = this.title,
        time = this.time,
        description = this.description,
        taskState = this.taskState,
        userId = this.userId,
    )
}

/**
 * Convert the TaskUiState to TaskEntity
 */
fun TaskUiState.toEntity(): Task {
    return Task (
        taskId = this.taskId,
        title = this.title,
        time = this.time,
        description = this.description,
        taskState = this.taskState,
        userId = this.userId,
    )
}

