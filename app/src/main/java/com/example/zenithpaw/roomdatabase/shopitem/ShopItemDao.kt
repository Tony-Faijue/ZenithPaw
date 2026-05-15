package com.example.zenithpaw.roomdatabase.shopitem

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.OnConflictStrategy.Companion.ABORT
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ShopItemDao {
    @Insert(onConflict = ABORT)
    suspend fun insertShopItem(shopItem: ShopItem)

    //Cloud Sync
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertShopItem(shopItem: ShopItem)

    @Delete
    suspend fun deleteShopItem(shopItem: ShopItem)

    @Update(onConflict = ABORT)
    suspend fun updateShopItem(shopItem: ShopItem): Int

    @Query("SELECT * FROM shop_items WHERE shopItemId = :shopItemId")
    suspend fun getShopItemById(shopItemId: String): ShopItem

    @Query("SELECT * FROM shop_items")
    fun getShopItems(): Flow<List<ShopItem>>
}
