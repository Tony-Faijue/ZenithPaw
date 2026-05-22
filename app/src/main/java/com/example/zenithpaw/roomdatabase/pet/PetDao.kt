package com.example.zenithpaw.roomdatabase.pet

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.OnConflictStrategy.Companion.ABORT
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface PetDao {
    //Create new pet
    @Insert(onConflict = ABORT)
    suspend fun insertPet(pet: Pet)

    //Create new pet/ Update an existing pet if it exists
    //Cloud Sync
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertPet(pet: Pet)

    // Bulk Pet Updates
    @Upsert
    suspend fun upsertPets(pets: List<Pet>)

    @Delete
    suspend fun deletePet(pet: Pet)

    //Normal Update 1: True, 0: False
    @Update(onConflict = ABORT)
    suspend fun updatePet(pet: Pet): Int

    @Query("SELECT * FROM pets WHERE petId = :petId")
    suspend fun getPetById(petId: String): Pet?

    @Query("SELECT * FROM pets WHERE userId = :userId AND petState =:state")
    suspend fun getPetsByUserIdAndState(userId: String, state: PetState): List<Pet>

    @Query("SELECT * FROM pets WHERE userId = :userId")
    fun getPetsForUser(userId: String): Flow<List<Pet>>

    //Get all pets that are not downloaded yet
    @Query("SELECT * FROM pets WHERE isDownloaded = 0")
    suspend fun getPetsPendingDownload(): List<Pet>

}