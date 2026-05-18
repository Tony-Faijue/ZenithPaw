package com.example.zenithpaw.roomdatabase.userinventoryitem

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.OnConflictStrategy.Companion.ABORT
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface UserInventoryItemDao {
    @Insert(onConflict = ABORT)
    suspend fun insertUserInventoryItem(userInventoryItem: UserInventoryItem)

    //Cloud Sync
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertUserInventoryItem(userInventoryItem: UserInventoryItem)

    @Delete
    suspend fun deleteUserInventoryItem(userInventoryItem: UserInventoryItem)

    @Update(onConflict = ABORT)
    suspend fun updateUserInventoryItem(userInventoryItem: UserInventoryItem): Int

    @Query("SELECT * FROM user_inventory_item WHERE inventoryItemId = :inventoryItemId")
    suspend fun getUserInventoryItemById(inventoryItemId: String): UserInventoryItem

    @Query("SELECT * FROM user_inventory_item WHERE userId = :userId AND shopItemId = :shopItemId")
    suspend fun getUserInventoryItemByShopItemId(userId: String, shopItemId: String): UserInventoryItem?

    @Query("SELECT * FROM user_inventory_item WHERE userId = :userId")
    fun getUserInventoryItemsByUserId(userId: String): Flow<List<UserInventoryItem>>
}

