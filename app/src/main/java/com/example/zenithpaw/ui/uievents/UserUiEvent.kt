package com.example.zenithpaw.ui.uievents

sealed interface UserUiEvent {
    // UI-specific updates
    data class OnNameChange(val name: String) : UserUiEvent
    data class OnEmailChange(val email: String) : UserUiEvent
    data class OnChangeProfileImage(val imageUrl: String) : UserUiEvent
    //Explicit user actions / intent
    object OnSaveProfileClicked : UserUiEvent
    object OnSyncCloudClicked : UserUiEvent
    object OnDeleteAccountClicked : UserUiEvent
    data class OnCreateAccountClicked(val name: String, val email: String) : UserUiEvent
}