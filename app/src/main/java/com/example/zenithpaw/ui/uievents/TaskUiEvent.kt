package com.example.zenithpaw.ui.uievents

import com.example.zenithpaw.roomdatabase.task.TaskState

sealed interface TaskUiEvent {

    // Selection Action
    data class OnTaskSelected(val taskId: String) : TaskUiEvent

    // UI-specific updates
    data class OnTitleChange(val title: String) : TaskUiEvent
    data class OnTimeChange(val time: Long) : TaskUiEvent
    data class OnDescriptionChange(val description: String) : TaskUiEvent
    data class OnTaskStateChange(val taskState: TaskState) : TaskUiEvent

    // Task Creation
    data class OnCreateTaskClicked(val title: String, val description: String, val time: Long) : TaskUiEvent

    // Save and Delete Actions
    object OnSaveTaskClicked : TaskUiEvent
    object OnDeleteTaskClicked : TaskUiEvent

    // Dialog Actions
    object OnShowCreateTaskDialogClicked : TaskUiEvent
    object OnHideCreateTaskDialogClicked : TaskUiEvent
    object OnShowDeleteTaskDialogClicked : TaskUiEvent
    object OnHideDeleteTaskDialogClicked : TaskUiEvent
    object OnShowTaskSelectionDialogClicked : TaskUiEvent
    object OnHideTaskSelectionDialogClicked : TaskUiEvent
}
