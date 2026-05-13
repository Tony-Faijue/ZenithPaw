package com.example.zenithpaw.roomdatabase.shop

interface ShopItemRepository {
    suspend fun insertShopItem(shopItem: ShopItem)
    suspend fun upsertShopItem(shopItem: ShopItem)
    suspend fun deleteShopItem(shopItem: ShopItem)
    suspend fun updateShopItem(shopItem: ShopItem): Int
    suspend fun getShopItemById(shopItemId: String): ShopItem
}
