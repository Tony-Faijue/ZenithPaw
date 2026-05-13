package com.example.zenithpaw.roomdatabase

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.zenithpaw.roomdatabase.pet.Pet
import com.example.zenithpaw.roomdatabase.pet.PetDao
import com.example.zenithpaw.roomdatabase.shop.Shop
import com.example.zenithpaw.roomdatabase.shop.ShopDao
import com.example.zenithpaw.roomdatabase.shopitem.ShopItem
import com.example.zenithpaw.roomdatabase.shopitem.ShopItemDao
import com.example.zenithpaw.roomdatabase.task.Task
import com.example.zenithpaw.roomdatabase.task.TaskDao
import com.example.zenithpaw.roomdatabase.user.User
import com.example.zenithpaw.roomdatabase.user.UserDao
import com.example.zenithpaw.roomdatabase.userinventoryitem.UserInventoryItem
import com.example.zenithpaw.roomdatabase.userinventoryitem.UserInventoryItemDao

@Database(
    entities = [User::class, Pet::class, Task::class, Shop::class, ShopItem::class, UserInventoryItem::class],
    version = 1,
    exportSchema = true
)

abstract class ZenithPawDatabase: RoomDatabase() {
    abstract val userDao: UserDao
    abstract val petDao: PetDao
    abstract val taskDao: TaskDao
    abstract val shopDao: ShopDao
    abstract val shopItemDao: ShopItemDao
    abstract val userInventoryItemDao: UserInventoryItemDao
}
