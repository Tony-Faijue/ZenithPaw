package com.example.zenithpaw.ui.shop

import com.example.zenithpaw.roomdatabase.shop.Shop
import com.example.zenithpaw.roomdatabase.shopitem.ShopItem
import com.example.zenithpaw.ui.shopitem.ShopItemUiState

data class ShopUiState(
    //Shop identifiers
    val shopId: String = "",
    val name: String = "Loading Shop...",

    //Data
    val availableItems: List<ShopItemUiState> = emptyList(),
    val userBalance: Int = 0,

    //UI Logic State
    val selectedItem: ShopItemUiState? = null,
    val isLoading: Boolean = false,
    val isPreviewVisible: Boolean = false, // For preview dialog
    val isBuyingVisible: Boolean = false, // For buying confirmation dialog
    val errorMessage: String? = null
)

/**
 * Convert the ShopEntity to ShopUiState
 */
fun Shop.toShopUiState() = ShopUiState(
    shopId = this.shopId,
    name = this.name,
    availableItems = emptyList(),
    userBalance = 0,
    selectedItem = null,
    isLoading = false,
    isPreviewVisible = false,
    isBuyingVisible = false,
    errorMessage = null
)

/**
 * Convert the ShopUiState to ShopEntity
 */
fun ShopUiState.toShopEntity() = Shop(
    shopId = this.shopId,
    name = this.name
)