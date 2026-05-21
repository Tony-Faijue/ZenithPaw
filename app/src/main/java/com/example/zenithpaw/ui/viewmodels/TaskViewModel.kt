package com.example.zenithpaw.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.zenithpaw.roomdatabase.task.Task
import com.example.zenithpaw.roomdatabase.task.TaskRepository
import com.example.zenithpaw.roomdatabase.task.TaskState
import com.example.zenithpaw.roomdatabase.user.UserRepository
import com.example.zenithpaw.ui.task.TaskScreenUiState
import com.example.zenithpaw.ui.task.toEntity
import com.example.zenithpaw.ui.task.toTaskUiState
import com.example.zenithpaw.ui.uievents.TaskUiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor(
    private val taskRepository: TaskRepository,
    private val userRepository: UserRepository
): ViewModel(){

    private val _uiState = MutableStateFlow(TaskScreenUiState(isLoading = true))
    val uiState: StateFlow<TaskScreenUiState> = _uiState.asStateFlow()

    /**
     * Initialize the ViewModel by observing task data
     */
    init {
        observeTaskData()
    }

    fun observeTaskData(){
        viewModelScope.launch{
            // Observe the user data
            userRepository.getUsers()
                .distinctUntilChanged { old, new ->
                    old.firstOrNull()?.userId == new.firstOrNull()?.userId
                }.flatMapLatest { users ->
                    val currentUser = users.firstOrNull()
                    if (currentUser == null){
                        flowOf(TaskScreenUiState(isLoading = false))
                    } else {
                        taskRepository.getTasksForUser(currentUser.userId).map { tasks ->

                        // Get the current selected task
                        val currentSelection = _uiState.value.selectedTask
                        val newTaskList = tasks.map { it.toTaskUiState() } //Convert Task entities to TaskUiState

                            // ---Logic to shield from stubborn overwrites---
                            val existingDatabaseTask = newTaskList.find { it.taskId == currentSelection?.taskId}

                            // If already editing this task keep the current selection task fields
                            val updateSelection = if (currentSelection != null && existingDatabaseTask != null){
                                existingDatabaseTask.copy(
                                    // Keep the current selection task fields from the UI not the database ones
                                    title = currentSelection.title,
                                    description = currentSelection.description,
                                    time = currentSelection.time,
                                    taskState = currentSelection.taskState
                                )
                            } else {
                                // use the existing task else the first task in the list
                                existingDatabaseTask ?: newTaskList.firstOrNull()
                            }

                        TaskScreenUiState(
                            tasks = newTaskList,
                            isLoading = false,
                            selectedTask = updateSelection
                        )
                    } }
                }.collect { newState ->
                    _uiState.value = newState
                }
        }
    }

    /**
     * Handle the task screen interface events
     */
    fun onEvent(event: TaskUiEvent){
        when (event){
            //Selection Events
            is TaskUiEvent.OnTaskSelected -> onTaskSelected(event.taskId)

            // Task Creation
            is TaskUiEvent.OnCreateTaskClicked -> onCreateTaskClicked(event.title, event.description, event.time)

            // Allow input fields and task state changes
            is TaskUiEvent.OnTitleChange -> _uiState.update { it.copy(selectedTask = it.selectedTask?.copy(title = event.title))}
            is TaskUiEvent.OnDescriptionChange -> _uiState.update { it.copy(selectedTask = it.selectedTask?.copy(description = event.description))}
            is TaskUiEvent.OnTimeChange -> _uiState.update { it.copy(selectedTask = it.selectedTask?.copy(time = event.time))}
            is TaskUiEvent.OnTaskStateChange -> _uiState.update { it.copy(selectedTask = it.selectedTask?.copy(taskState = event.taskState))}

            // Save and Deletion actions
            TaskUiEvent.OnSaveTaskClicked -> onSaveTaskClicked()
            TaskUiEvent.OnDeleteTaskClicked -> onDeleteTaskClicked()

            // Dialog actions
            TaskUiEvent.OnShowCreateTaskDialogClicked -> _uiState.update { it.copy(isAddingTask = true)}
            TaskUiEvent.OnHideCreateTaskDialogClicked -> _uiState.update { it.copy(isAddingTask = false)}
            TaskUiEvent.OnShowDeleteTaskDialogClicked -> _uiState.update { it.copy(isDeleteTaskDialogVisible = true)}
            TaskUiEvent.OnHideDeleteTaskDialogClicked -> _uiState.update { it.copy(isDeleteTaskDialogVisible = false)}
            TaskUiEvent.OnShowTaskSelectionDialogClicked -> _uiState.update { it.copy(isTaskSelectionDialogVisible = true)}
            TaskUiEvent.OnHideTaskSelectionDialogClicked -> _uiState.update { it.copy(isTaskSelectionDialogVisible = false)}

        }
    }

    /**
     * Save the selected task
     */
    private fun onSaveTaskClicked(){
        // Get the selected task
        val taskToUpdate = _uiState.value.selectedTask
        if (taskToUpdate == null) return
        else {
            viewModelScope.launch {
                // Update the selected task
                taskRepository.upsertTask(taskToUpdate.toEntity())
                _uiState.update { it.copy(isTaskSelectionDialogVisible = false, selectedTask = null, errorMessage = null) }
            }
        }
    }

    /**
     * Delete the selected task
     */
    private fun onDeleteTaskClicked(){
        // Get the selected task
        val taskToDelete = _uiState.value.selectedTask
        if (taskToDelete == null ) return
        else {
            viewModelScope.launch {
                // Delete the selected task
                taskRepository.deleteTask(taskToDelete.toEntity())
                _uiState.update { it.copy(isDeleteTaskDialogVisible = false, selectedTask = null, errorMessage = null) }
            }
        }
    }

    /**
     * Create a new task
     */
    private fun onCreateTaskClicked(title: String, description: String, time: Long ){
        viewModelScope.launch {
            // Get the current user
            val users = userRepository.getUsers().first()
            val currentUser = users.firstOrNull()

            if (currentUser == null){
                _uiState.update { it.copy(errorMessage = "No User Found: When Creating task")}
                return@launch
            }

            val newTask = Task(
                taskId = UUID.randomUUID().toString(),
                title = title,
                time = time,
                description = description,
                taskState = TaskState.Pending,
                userId = currentUser.userId
            )
            // Save the new task to the database
            taskRepository.upsertTask(newTask)
            // Close the adding task dialog
            _uiState.update { it.copy(isAddingTask = false) }
        }
    }

    /**
     * Preview the selected task in the UI state
     */
    private fun onTaskSelected(taskId: String){
       viewModelScope.launch {
           if (selectTask(taskId)){
               _uiState.update { it.copy(isTaskSelectionDialogVisible = true)}
           }
       }
   }

    /**
     * Update the selected task in the UI state
     */
    private fun selectTask(taskId: String): Boolean{
        val task = _uiState.value.tasks.find {it.taskId == taskId}
        if (task != null){
            _uiState.update { it.copy(selectedTask = task, errorMessage = null)}
            return true
        }
        return false
    }

}