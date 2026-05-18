package com.example.zenithpaw.roomdatabase.userinventoryitem

import kotlinx.coroutines.flow.Flow

interface UserInventoryItemRepository {
    suspend fun insertUserInventoryItem(userInventoryItem: UserInventoryItem)
    suspend fun upsertUserInventoryItem(userInventoryItem: UserInventoryItem)
    suspend fun deleteUserInventoryItem(userInventoryItem: UserInventoryItem)
    suspend fun updateUserInventoryItem(userInventoryItem: UserInventoryItem): Int
    suspend fun getUserInventoryItemById(inventoryItemId: String): UserInventoryItem
    suspend fun getUserInventoryItemByShopItemId(userId: String, shopItemId: String): UserInventoryItem?
    fun getUserInventoryItemsByUserId(userId: String): Flow<List<UserInventoryItem>>
}
