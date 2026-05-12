package com.example.zenithpaw.roomdatabase.task

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.zenithpaw.roomdatabase.user.User
import java.util.UUID

@Entity(tableName = "tasks", foreignKeys = [
    ForeignKey(
        entity = User::class,
        parentColumns = ["userId"],
        childColumns = ["userId"],
        onDelete = ForeignKey.CASCADE
    )
])
data class Task (
    val title: String,
    val time: Long,
    val description: String,
    val taskState: TaskState,
    val userId: String, //Foreign key to the User entity
    @PrimaryKey(autoGenerate = false)
    val taskId: String = UUID.randomUUID().toString()
)
