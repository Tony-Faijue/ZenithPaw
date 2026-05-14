package com.example.zenithpaw.ui.pet

data class PetScreenUiState(
    val pets: List<PetUiState> = emptyList(),
    val isLoading: Boolean = false,
    val selectedPet: PetUiState? = null,
    val errorMessage: String? = null
)
