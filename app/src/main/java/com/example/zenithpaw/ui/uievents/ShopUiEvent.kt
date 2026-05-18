package com.example.zenithpaw.ui.uievents

sealed interface ShopUiEvent {
    // User Actions data persistence
    data class OnBuyItemClicked(val shopItemId: String) : ShopUiEvent
    data class OnPreviewItemClicked(val shopItemId: String) : ShopUiEvent
    //Dialog Actions
    object OnDismissPreviewDialogClicked: ShopUiEvent
    object OnShowPreviewDialogClicked: ShopUiEvent
    object OnShowConfirmBuyDialogClicked: ShopUiEvent
    object OnDismissConfirmBuyDialogClicked: ShopUiEvent
}