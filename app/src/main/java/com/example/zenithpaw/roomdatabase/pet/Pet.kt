package com.example.zenithpaw.roomdatabase.pet

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.zenithpaw.roomdatabase.user.User
import java.util.UUID

@Entity(tableName = "pets", foreignKeys = [
    ForeignKey(
        entity = User::class,
        parentColumns = ["userId"],
        childColumns = ["userId"],
        onDelete = ForeignKey.CASCADE
    )
])
data class Pet (
    val name: String,
    val species: PetType,
    val isDownloaded: Boolean,
    val animationUrl: String,
    val imageUrl: String,
    val zen: Int,
    val petState: PetState,
    @PrimaryKey(autoGenerate = false)
    val petId: String,
    val userId: String // Foreign key to the User entity
)
