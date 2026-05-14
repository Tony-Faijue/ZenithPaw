package com.example.zenithpaw.ui.uievents

import com.example.zenithpaw.roomdatabase.task.TaskState

sealed interface TaskUiEvent {
    // UI-specific updates
    data class OnTitleChange(val title: String) : TaskUiEvent
    data class OnTimeChange(val time: Long) : TaskUiEvent
    data class OnDescriptionChange(val description: String) : TaskUiEvent
    data class OnTaskStateChange(val taskState: TaskState) : TaskUiEvent
    // Explicit user actions / intent
    object OnSaveTaskClicked : TaskUiEvent
    object OnDeleteTaskClicked : TaskUiEvent
}
