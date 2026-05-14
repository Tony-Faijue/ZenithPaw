package com.example.zenithpaw.roomdatabase.pet

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PetRepositoryImpl @Inject constructor(private val petDao: PetDao) : PetRepository {
    override suspend fun insertPet(pet: Pet) {
        petDao.insertPet(pet)
    }
    override suspend fun upsertPet(pet: Pet) {
        petDao.upsertPet(pet)
    }
    override suspend fun deletePet(pet: Pet) {
        petDao.deletePet(pet)
    }
    override suspend fun updatePet(pet: Pet): Int {
        return petDao.updatePet(pet)
    }
    override suspend fun getPetById(petId: String): Pet? {
        return petDao.getPetById(petId)
    }
    override suspend fun getPetsByUserIdAndState(userId: String, state: PetState): List<Pet> {
        return petDao.getPetsByUserIdAndState(userId, state)
    }
    override fun getPetsForUser(userId: String): Flow<List<Pet>> {
        return petDao.getPetsForUser(userId)
    }
    override suspend fun getPetsPendingDownload(): List<Pet> {
        return petDao.getPetsPendingDownload()
    }
}