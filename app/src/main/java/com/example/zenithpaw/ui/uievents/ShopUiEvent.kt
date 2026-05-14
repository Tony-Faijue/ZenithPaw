package com.example.zenithpaw.ui.uievents

sealed interface ShopUiEvent {
    data class OnBuyItemClicked(val shopItemId: String) : ShopUiEvent
    data class OnPreviewItemClicked(val shopItemId: String) : ShopUiEvent
    object OnCloseShopClicked : ShopUiEvent
    object OnDismissDialog: ShopUiEvent
}