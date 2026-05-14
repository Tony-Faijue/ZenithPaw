package com.example.zenithpaw.roomdatabase.task

data class TaskUiState(
    val taskId: String = "",
    val title: String = "",
    val time: Long = 0,
    val description: String = "",
    val taskState: TaskState = TaskState.Pending
)
