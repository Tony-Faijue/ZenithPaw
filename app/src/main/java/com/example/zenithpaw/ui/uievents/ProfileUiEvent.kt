package com.example.zenithpaw.ui.uievents

sealed interface ProfileUiEvent {
    // UI-specific updates
    data class OnNameChange(val name: String) : ProfileUiEvent
    data class OnEmailChange(val email: String) : ProfileUiEvent
    data class OnChangeProfileImage(val imageUrl: String) : ProfileUiEvent
    //Explicit user actions / intent
    object OnSaveProfileClicked : ProfileUiEvent
    object OnSyncCloudClicked : ProfileUiEvent
    object OnDeleteAccountClicked : ProfileUiEvent
}