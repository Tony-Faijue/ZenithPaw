package com.example.zenithpaw.roomdatabase.userinventoryitem

import kotlinx.coroutines.flow.Flow

class UserInventoryItemRepositoryImpl(private val userInventoryItemDao: UserInventoryItemDao) :
    UserInventoryItemRepository {
        override suspend fun insertUserInventoryItem(userInventoryItem: UserInventoryItem) {
        userInventoryItemDao.insertUserInventoryItem(userInventoryItem)
    }
    override suspend fun upsertUserInventoryItem(userInventoryItem: UserInventoryItem) {
        userInventoryItemDao.upsertUserInventoryItem(userInventoryItem)
    }
    override suspend fun deleteUserInventoryItem(userInventoryItem: UserInventoryItem) {
        userInventoryItemDao.deleteUserInventoryItem(userInventoryItem)
    }
    override suspend fun updateUserInventoryItem(userInventoryItem: UserInventoryItem): Int {
        return userInventoryItemDao.updateUserInventoryItem(userInventoryItem)
    }
    override suspend fun getUserInventoryItemById(inventoryItemId: String): UserInventoryItem {
        return userInventoryItemDao.getUserInventoryItemById(inventoryItemId)
    }
    override fun getUserInventoryItemsByUserId(userId: String): Flow<List<UserInventoryItem>> {
        return userInventoryItemDao.getUserInventoryItemsByUserId(userId)
    }
}