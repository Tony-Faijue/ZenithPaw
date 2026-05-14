package com.example.zenithpaw.ui.uievents

sealed interface PetUiEvent {
    data class OnNameChange(val name: String) : PetUiEvent
    object OnFeedPetClicked : PetUiEvent
    object OnPlayPetClicked : PetUiEvent
}