package com.example.zenithpaw.ui.pet

import com.example.zenithpaw.roomdatabase.pet.Pet
import com.example.zenithpaw.roomdatabase.pet.PetState
import com.example.zenithpaw.roomdatabase.pet.PetType

data class PetUiState(
    val petId: String = "",
    val name: String = "",
    val species: PetType = PetType.CAT,
    val animationUrl: String = "",
    val imageUrl: String = "",
    val zen: Int = 0,
    val petState: PetState = PetState.Idle,
    val userId: String = "",
)

/**
 * Convert the PetEntity to PetUiState
 */
fun Pet.toPetUiState(): PetUiState{
    return PetUiState(
        petId = this.petId,
        name = this.name,
        species = this.species,
        imageUrl = this.imageUrl,
        zen = this.zen,
        petState = this.petState,
        userId = this.userId,
    )
}

/**
 * Convert the PetUiState to PetEntity
 */
fun PetUiState.toEntity(): Pet {
    return Pet(
        name = this.name,
        species = this.species,
        imageUrl = this.imageUrl,
        zen = this.zen,
        petState = this.petState,
        petId = this.petId,
        userId = this.userId,
    )
}