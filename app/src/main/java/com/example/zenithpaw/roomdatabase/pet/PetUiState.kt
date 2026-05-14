package com.example.zenithpaw.roomdatabase.pet

data class PetUiState(
    val petId: String = "",
    val name: String = "",
    val species: PetType = PetType.CAT,
    val isDownloaded: Boolean = false,
    val animationUrl: String = "",
    val imageUrl: String = "",
    val zen: Int = 0,
    val petState: PetState = PetState.Idle
)
