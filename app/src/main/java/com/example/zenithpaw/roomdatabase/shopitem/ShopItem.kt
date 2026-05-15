package com.example.zenithpaw.roomdatabase.shopitem

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.zenithpaw.roomdatabase.shop.Shop
import java.util.UUID

@Entity(tableName = "shop_items", foreignKeys = [
    ForeignKey(
        entity = Shop::class,
        parentColumns = ["shopId"],
        childColumns = ["shopId"],
        onDelete = ForeignKey.CASCADE,
        )]
)
data class ShopItem(
    val name: String,
    val imageUrl: String,
    val price: Int,
    val description: String,
    @PrimaryKey(autoGenerate = false)
    val shopItemId: String,
    val shopId: String // Foreign key to the Shop entity
)
