package com.example.zenithpaw.roomdatabase.shop

class ShopItemRepositoryImpl(private val shopItemDao: ShopItemDao) : ShopItemRepository {
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
}