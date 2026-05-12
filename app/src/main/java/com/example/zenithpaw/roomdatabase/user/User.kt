package com.example.zenithpaw.roomdatabase.user

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "users")
data class User (
    val name: String,
    val email: String,
    val imageUrl: String,
    val lastLogin: Long,
    val gold: Int,
    @PrimaryKey(autoGenerate = false)
    val userId: String = UUID.randomUUID().toString()
)