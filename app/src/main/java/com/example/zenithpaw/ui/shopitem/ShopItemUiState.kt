package com.example.zenithpaw.ui.shopitem

import com.example.zenithpaw.roomdatabase.shopitem.ShopItem

data class ShopItemUiState(
    val shopItemId: String = "",
    val name: String = "",
    val imageUrl: String = "",
    val price: Int = 0,
    val description: String = "",
    val shopId: String = "",
)

/**
 * Convert the ShopItemEntity to ShopItemUiState
 */
fun ShopItem.toShopItemUiState(): ShopItemUiState {
    return ShopItemUiState(
        shopItemId = this.shopItemId,
        name = this.name,
        imageUrl = this.imageUrl,
        price = this.price,
        description = this.description,
        shopId = this.shopId,
    )
}

/**
 * Convert the ShopItemUiState to ShopItemEntity
 */
fun ShopItemUiState.toEntity(): ShopItem {
    return ShopItem(
        shopItemId = this.shopItemId,
        name = this.name,
        imageUrl = this.imageUrl,
        price = this.price,
        description = this.description,
        shopId = this.shopId,
    )
}