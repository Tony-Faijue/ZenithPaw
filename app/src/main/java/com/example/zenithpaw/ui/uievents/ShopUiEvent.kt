package com.example.zenithpaw.ui.uievents

sealed interface ShopUiEvent {
    // User Actions data persistence
    data class OnPreviewItemClicked(val shopItemId: String) : ShopUiEvent
    data class OnShowConfirmBuyDialogClicked(val shopItemId: String): ShopUiEvent
    object OnConfirmPurchase : ShopUiEvent
    //Dialog Dismiss Actions
    object OnDismissPreviewDialogClicked: ShopUiEvent
    object OnDismissConfirmBuyDialogClicked: ShopUiEvent
}