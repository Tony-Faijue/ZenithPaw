package com.example.zenithpaw.roomdatabase.shop

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "shops")
data class Shop(
    val name: String,
    @PrimaryKey(autoGenerate = false)
    val shopId: String = UUID.randomUUID().toString()
)
