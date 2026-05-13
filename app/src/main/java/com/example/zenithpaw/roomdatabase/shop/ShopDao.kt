package com.example.zenithpaw.roomdatabase.shop

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
interface ShopDao {
    @Insert(onConflict = ABORT)
    suspend fun insertShop(shop: Shop)

    //Cloud Sync
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertShop(shop: Shop)

    @Delete
    suspend fun deleteShop(shop: Shop)

    @Update(onConflict = ABORT)
    suspend fun updateShop(shop: Shop): Int

    @Query("SELECT * FROM shops WHERE shopId = :shopId")
    suspend fun getShopById(shopId: String): Shop?

    @Query("SELECT * FROM shops")
    fun getShops(): Flow<List<Shop>>
}

