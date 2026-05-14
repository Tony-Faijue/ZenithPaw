package com.example.zenithpaw.ui.user

import com.example.zenithpaw.ui.userinventoryitem.UserInventoryItemUiState

data class UserUiState(
    val userId: String = "",
    val name: String = "",
    val email: String = "",
    val imageUrl: String = "",
    val lastLogin: Long = 0,
    val gold: Int = 0,
    val inventory: List<UserInventoryItemUiState> = emptyList(),
    val isLoading: Boolean = false,
)