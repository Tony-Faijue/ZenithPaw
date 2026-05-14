package com.example.zenithpaw.roomdatabase.shop

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ShopRepositoryImpl @Inject constructor(private val shopDao: ShopDao) : ShopRepository {
    override suspend fun insertShop(shop: Shop) {
        shopDao.insertShop(shop)
    }
    override suspend fun upsertShop(shop: Shop) {
        shopDao.upsertShop(shop)
    }
    override suspend fun deleteShop(shop: Shop) {
        shopDao.deleteShop(shop)
    }
    override suspend fun updateShop(shop: Shop): Int {
        return shopDao.updateShop(shop)
    }
    override suspend fun getShopById(shopId: String): Shop {
        return shopDao.getShopById(shopId)
    }
    override fun getShops(): Flow<List<Shop>> {
        return shopDao.getShops()
    }
}