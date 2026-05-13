package com.example.zenithpaw.roomdatabase.shop

import kotlinx.coroutines.flow.Flow

interface ShopRepository {
    suspend fun insertShop(shop: Shop)
    suspend fun upsertShop(shop: Shop)
    suspend fun deleteShop(shop: Shop)
    suspend fun updateShop(shop: Shop): Int
    suspend fun getShopById(shopId: String): Shop
    fun getShops(): Flow<List<Shop>>
}