package com.example.zenithpaw.ui.user

import com.example.zenithpaw.roomdatabase.user.User
import com.example.zenithpaw.ui.userinventoryitem.UserInventoryItemUiState

data class UserUiState(
    val userId: String = "",
    val lastLogin: Long = 0,
    val gold: Int = 0,
    val inventory: List<UserInventoryItemUiState> = emptyList(),
    val isLoading: Boolean = false,
    val isRegisteringDialogVisible: Boolean = false, //state of registration with dialog
    val errorMessage: String? = null,
    // Text field states that are editable
    val name: String = "",
    val email: String = "",
    val imageUrl: String = "",
){
    //isRegistered depends on the userId property for registration check
    val isRegistered: Boolean get() = userId.isNotEmpty()
}

/**
 * Mapper/Extension function to convert UserUiState to UserEntity
 */
fun UserUiState.toEntity(): User {
    return User(
        userId = this.userId,
        name = this.name,
        email = this.email,
        imageUrl = this.imageUrl,
        lastLogin = this.lastLogin,
        gold = this.gold,
    )
}