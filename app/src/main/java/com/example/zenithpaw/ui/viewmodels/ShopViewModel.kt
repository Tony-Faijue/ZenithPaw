package com.example.zenithpaw.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.zenithpaw.roomdatabase.shop.ShopRepository
import com.example.zenithpaw.roomdatabase.shopitem.ShopItemRepository
import com.example.zenithpaw.roomdatabase.user.UserRepository
import com.example.zenithpaw.ui.shop.ShopUiState
import com.example.zenithpaw.ui.shopitem.ShopItemUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShopViewModel @Inject constructor(
    // Inject the interface repositories
    private val shopRepository: ShopRepository,
    private val shopItemRepository: ShopItemRepository,
    private val userRepository: UserRepository,
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
                    _uiState.update { it.copy(availableItems = displayItems, isLoading = false )}
                }
        }
    }
}