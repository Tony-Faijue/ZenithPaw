package com.example.zenithpaw.roomdatabase.shop

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
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
    val shopItemId: String = UUID.randomUUID().toString(),
    val shopId: String // Foreign key to the Shop entity
)
