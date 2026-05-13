package com.example.zenithpaw.roomdatabase.pet

import kotlinx.coroutines.flow.Flow

interface PetRepository {
    suspend fun insertPet(pet: Pet)
    suspend fun upsertPet(pet: Pet)
    suspend fun deletePet(pet: Pet)
    suspend fun updatePet(pet: Pet): Int
    suspend fun getPetById(petId: String): Pet?
    suspend fun getPetsByUserIdAndState(userId: String, state: PetState): List<Pet>
    fun getPetsForUser(userId: String): Flow<List<Pet>>
    suspend fun getPetsPendingDownload(): List<Pet>
}
