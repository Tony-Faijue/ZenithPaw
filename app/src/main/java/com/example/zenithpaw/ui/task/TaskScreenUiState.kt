package com.example.zenithpaw.ui.task

data class TaskScreenUiState(
    val tasks: List<TaskUiState> = emptyList(),
    val isLoading: Boolean = false,
    val selectedTask: TaskUiState? = null,
    val isTaskSelectionDialogVisible: Boolean = false,
    val isDeleteTaskDialogVisible: Boolean = false,
    val isAddingTask: Boolean = false,
    val errorMessage: String? = null
)
