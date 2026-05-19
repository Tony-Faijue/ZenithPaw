package com.example.zenithpaw.ui.pet

import com.example.zenithpaw.ui.userinventoryitem.UserInventoryItemUiState

data class PetScreenUiState(
    val pets: List<PetUiState> = emptyList(),
    val items: List<UserInventoryItemUiState> = emptyList(),
    val isLoading: Boolean = false,
    val selectedPet: PetUiState? = null,
    val selectedItem: UserInventoryItemUiState? = null,
    val isPetSelectionDialogVisible: Boolean = false,
    val isItemSelectionDialogVisible: Boolean = false,
    val isNameChangeDialogVisible: Boolean = false,
    val errorMessage: String? = null
)
