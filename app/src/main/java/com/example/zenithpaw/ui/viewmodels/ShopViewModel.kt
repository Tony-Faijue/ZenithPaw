package com.example.zenithpaw.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.zenithpaw.roomdatabase.shop.ShopRepository
import com.example.zenithpaw.roomdatabase.shopitem.ShopItemRepository
import com.example.zenithpaw.roomdatabase.user.UserRepository
import com.example.zenithpaw.roomdatabase.userinventoryitem.UserInventoryItem
import com.example.zenithpaw.roomdatabase.userinventoryitem.UserInventoryItemRepository
import com.example.zenithpaw.ui.shop.ShopUiState
import com.example.zenithpaw.ui.shopitem.ShopItemUiState
import com.example.zenithpaw.ui.uievents.ShopUiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class ShopViewModel @Inject constructor(
    // Inject the interface repositories
    private val shopRepository: ShopRepository,
    private val shopItemRepository: ShopItemRepository,
    private val userRepository: UserRepository,
    private val userInventoryItemRepository: UserInventoryItemRepository
    ): ViewModel() {
    private val _uiState =
        MutableStateFlow(ShopUiState(isLoading = true)) //loading shop state first)
    val uiState: StateFlow<ShopUiState> = _uiState.asStateFlow()

    /**
     * Initialize the ViewModel by observing shop data
     */
    init {
        observeShopData()
    }

    /**
     * Observe shop data and update UI state
     */
    private fun observeShopData() {
        //  Observe the user data
        viewModelScope.launch {
            userRepository.getUsers()
                // Only trigger if the user/gold has changed
                .distinctUntilChanged { old, new ->
                    old.firstOrNull()?.userId == new.firstOrNull()?.userId && old.firstOrNull()?.gold == new.firstOrNull()?.gold
                }.collect { users ->
                    val currentUser = users.firstOrNull()
                    if (currentUser == null) {
                        //No user found
                        _uiState.update { it.copy(errorMessage = "No User Found" , isLoading = false) }
                    } else {
                        _uiState.update { currentState ->
                            currentState.copy(
                                userBalance = currentUser.gold
                            )
                        }
                    }
                }
        }
        //Observe the Shop Items
        viewModelScope.launch {
            shopRepository.getShops()
                // Only trigger if the shopId has changed
                .distinctUntilChanged { old, new ->
                    old.firstOrNull()?.shopId == new.firstOrNull()?.shopId
                }
                .flatMapLatest { shops ->
                    val shop = shops.firstOrNull()
                    if (shop == null ){
                        flowOf(emptyList<ShopItemUiState>())
                    } else {
                        // Update the state with the shopId
                        _uiState.update { it.copy(
                            shopId = shop.shopId,
                            name = shop.name
                        )}
                        shopItemRepository.getShopItemsByShopId(shop.shopId)
                            .map{ shopItems ->
                                shopItems.map { item ->
                                    ShopItemUiState(
                                        shopItemId = item.shopItemId,
                                        name = item.name,
                                        imageUrl = item.imageUrl,
                                        price = item.price,
                                        description = item.description,
                                    )
                                }
                            }
                    }
                }.collect { displayItems ->
                    _uiState.update { it.copy(
                        availableItems = displayItems,
                        isLoading = false,
                    )}
                }
        }
    }

    private fun onEvent(event: ShopUiEvent){
        if (_uiState.value.shopId.isEmpty()) return //If the shopId is not loaded yet, stop events
        when (event){
            //User buys the shop item
            is ShopUiEvent.OnBuyItemClicked -> onBuyItemClicked(event.shopItemId)
            //User previews the shop item
            is ShopUiEvent.OnPreviewItemClicked -> onPreviewItemClicked(event.shopItemId)
            //Dialog Actions
            ShopUiEvent.OnShowPreviewDialogClicked -> _uiState.update { it.copy(isPreviewVisible = true) }
            ShopUiEvent.OnDismissPreviewDialogClicked -> _uiState.update { it.copy(isPreviewVisible = false) }
            ShopUiEvent.OnShowConfirmBuyDialogClicked -> _uiState.update { it.copy(isBuyingVisible = true) }
            ShopUiEvent.OnDismissConfirmBuyDialogClicked -> _uiState.update { it.copy(isBuyingVisible = false) }
        }
    }

    /**
     * Use buys the shop item reducing their gold and adding the shop item to their inventory
     */
    private fun onBuyItemClicked(shopItemId: String){
        viewModelScope.launch{
            //Shop item price
            val price = shopItemRepository.getShopItemById(shopItemId).price

            // Get the current user snapshot
            val users = userRepository.getUsers().first()
            val currentUser = users.firstOrNull()

            //If no user is found, return
            if (currentUser == null) {
                _uiState.update { it.copy(errorMessage = "No User Found: When Buying ShopItem") }
                return@launch
            }

            val balance = currentUser.gold

            //User does not enough have gold
            if (price > balance) {
                _uiState.update { it.copy(errorMessage = "Not Enough Gold: When Buying ShopItem") }
            }
            // User has enough gold to buy the item; Perform the Transaction
            else {
                val remainingGold = balance - price
                //Update the user gold and copy the current user
                val updatedUser = currentUser.copy(gold = remainingGold)
                //Save the updated user
                userRepository.upsertUser(updatedUser)

            // Add Item to user inventory
                // Check if the user already has the item in their inventory
                val existingItem = userInventoryItemRepository.getUserInventoryItemByShopItemId(currentUser.userId, shopItemId)
                if (existingItem != null){
                    // Increase the quantity of the item
                    val updatedItem = existingItem.copy(quantity = existingItem.quantity + 1)
                    userInventoryItemRepository.upsertUserInventoryItem(updatedItem)
                } else {
                    // Add the new item to the user's inventory
                    val newItem = UserInventoryItem(
                        inventoryItemId = UUID.randomUUID().toString(),
                        userId = currentUser.userId,
                        shopItemId = shopItemId,
                        quantity = 1
                    )
                    userInventoryItemRepository.insertUserInventoryItem(newItem)
                }
                //Close the confirmation dialog
                _uiState.update { it.copy(isBuyingVisible = false, errorMessage = null) }
            }
        }
    }

    /**
     * Preview the shop item
     */
    private fun onPreviewItemClicked(shopItemId: String){}

}