package com.example.zenithpaw.roomdatabase

import com.example.zenithpaw.roomdatabase.pet.PetRepository
import com.example.zenithpaw.roomdatabase.pet.PetRepositoryImpl
import com.example.zenithpaw.roomdatabase.shop.ShopRepository
import com.example.zenithpaw.roomdatabase.shop.ShopRepositoryImpl
import com.example.zenithpaw.roomdatabase.shopitem.ShopItemRepository
import com.example.zenithpaw.roomdatabase.shopitem.ShopItemRepositoryImpl
import com.example.zenithpaw.roomdatabase.task.TaskRepository
import com.example.zenithpaw.roomdatabase.task.TaskRepositoryImpl
import com.example.zenithpaw.roomdatabase.user.UserRepository
import com.example.zenithpaw.roomdatabase.user.UserRepositoryImpl
import com.example.zenithpaw.roomdatabase.userinventoryitem.UserInventoryItemRepository
import com.example.zenithpaw.roomdatabase.userinventoryitem.UserInventoryItemRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
abstract class RepositoryModule {

    //Binding functions to the interface data repositories
    //When the data repositories are called the implementation classes will be used

    @Binds
    abstract fun bindUserRepository(impl: UserRepositoryImpl): UserRepository

    @Binds
    abstract fun bindPetRepository(impl: PetRepositoryImpl): PetRepository

    @Binds
    abstract fun bindTaskRepository(impl: TaskRepositoryImpl): TaskRepository

    @Binds
    abstract fun bindShopRepository(impl: ShopRepositoryImpl): ShopRepository

    @Binds
    abstract fun bindShopItemRepository(impl: ShopItemRepositoryImpl): ShopItemRepository

    @Binds
    abstract fun bindUserInventoryItemRepository(impl: UserInventoryItemRepositoryImpl): UserInventoryItemRepository
}
