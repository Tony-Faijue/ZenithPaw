package com.example.zenithpaw.ui.uievents

sealed interface PetUiEvent {

    // Selection Actions
    data class OnPetSelected(val petId: String) : PetUiEvent
    data class OnInventoryItemSelected(val inventoryItemId: String) : PetUiEvent

    // Interactions
    object OnFeedPetConfirmed: PetUiEvent
    object OnPlayWithPetClicked : PetUiEvent
    object OnStopPlayingWithPetClicked : PetUiEvent

    // Save Actions
    data class OnSavePetNameClicked(val newName: String) : PetUiEvent

    // Dialog actions
    object OnShowNameChangeDialogClicked : PetUiEvent
    object OnDismissNameChangeDialogClicked : PetUiEvent

    // Select inventory item dialog
    object OnShowFeedDialogClicked : PetUiEvent
    object OnDismissFeedDialogClicked : PetUiEvent
}