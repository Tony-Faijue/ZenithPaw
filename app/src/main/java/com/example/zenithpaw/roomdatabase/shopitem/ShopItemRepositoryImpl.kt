package com.example.zenithpaw.roomdatabase.shopitem

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ShopItemRepositoryImpl @Inject constructor(private val shopItemDao: ShopItemDao) : ShopItemRepository {
    override suspend fun insertShopItem(shopItem: ShopItem) {
        shopItemDao.insertShopItem(shopItem)
    }
    override suspend fun upsertShopItem(shopItem: ShopItem) {
        shopItemDao.upsertShopItem(shopItem)
    }
    override suspend fun deleteShopItem(shopItem: ShopItem) {
        shopItemDao.deleteShopItem(shopItem)
    }
    override suspend fun updateShopItem(shopItem: ShopItem): Int {
        return shopItemDao.updateShopItem(shopItem)
        }
    override suspend fun getShopItemById(shopItemId: String): ShopItem {
        return shopItemDao.getShopItemById(shopItemId)
    }
    override fun getShopItems(): Flow<List<ShopItem>> {
        return shopItemDao.getShopItems()
    }
    override fun getShopItemsByShopId(shopId: String): Flow<List<ShopItem>> {
        return shopItemDao.getShopItemsByShopId(shopId)
    }
}
