package com.example.zenithpaw.ui.task

import com.example.zenithpaw.roomdatabase.task.TaskState

data class TaskUiState(
    val taskId: String = "",
    val title: String = "",
    val time: Long = 0,
    val description: String = "",
    val taskState: TaskState = TaskState.Pending
)