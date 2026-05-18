package com.example.zenithpaw.ui.shop

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
    val selectedItem: ShopItem? = null, // For showing preview dialog
    val isLoading: Boolean = false,
    val isPreviewVisible: Boolean = false, // For preview dialog
    val isBuyingVisible: Boolean = false, // For buying confirmation dialog
    val errorMessage: String? = null
)