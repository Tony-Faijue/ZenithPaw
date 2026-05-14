package com.example.zenithpaw.roomdatabase

import android.content.Context
import androidx.room.Room
import com.example.zenithpaw.roomdatabase.pet.PetDao
import com.example.zenithpaw.roomdatabase.shop.ShopDao
import com.example.zenithpaw.roomdatabase.shopitem.ShopItemDao
import com.example.zenithpaw.roomdatabase.task.TaskDao
import com.example.zenithpaw.roomdatabase.user.UserDao
import com.example.zenithpaw.roomdatabase.userinventoryitem.UserInventoryItemDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    /**
     * Provides a singleton instance of ZenithPawDatabase.
     */
    @Provides
    @Singleton
    fun provideZenithPawDatabase(@ApplicationContext context: Context): ZenithPawDatabase {
        return Room.databaseBuilder(
            context,
            ZenithPawDatabase::class.java,
            "zenith_paw_database"
        )
            .fallbackToDestructiveMigration() // Early development with schema migrations; remove in production (Look into AutoMigrations for production)
            .build()
    }

    // DAO providers

    @Provides
    fun provideUserDao(database: ZenithPawDatabase): UserDao {
        return database.userDao
    }

    @Provides
    fun providePetDao(database: ZenithPawDatabase): PetDao {
        return database.petDao
    }

    @Provides
    fun provideTaskDao(database: ZenithPawDatabase): TaskDao {
        return database.taskDao
    }

    @Provides
    fun provideShopDao(database: ZenithPawDatabase): ShopDao {
        return database.shopDao
    }

    @Provides
    fun provideShopItemDao(database: ZenithPawDatabase): ShopItemDao {
        return database.shopItemDao
    }

    @Provides
    fun provideUserInventoryItemDao(database: ZenithPawDatabase): UserInventoryItemDao {
        return database.userInventoryItemDao
    }
}
