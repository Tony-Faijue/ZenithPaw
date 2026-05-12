package com.example.zenithpaw.roomdatabase.user

import androidx.room.Entity
import androidx.room.ForeignKey
import com.example.zenithpaw.roomdatabase.shop.ShopItem

@Entity(tableName = "user_inventory_item", foreignKeys = [
    ForeignKey(
        entity = User::class,
        parentColumns = ["userId"],
        childColumns = ["userId"],
        onDelete = ForeignKey.CASCADE
    ),
    ForeignKey(
        entity = ShopItem::class,
        parentColumns = ["shopItemId"],
        childColumns = ["shopItemId"],
        onDelete = ForeignKey.CASCADE
    )
])
data class UserInventoryItem(
    val inventoryItemId: String,
    val userId: String, // Foreign key to the User entity
    val shopItemId: String, // Foreign key to the ShopItem entity
    val quantity: Int // Quantity of the item in the User Inventory
)
