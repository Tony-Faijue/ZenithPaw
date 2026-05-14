package com.example.zenithpaw.ui.user

data class UserUiState(
    val userId: String = "",
    val name: String = "",
    val email: String = "",
    val imageUrl: String = "",
    val lastLogin: Long = 0,
    val gold: Int = 0,
)