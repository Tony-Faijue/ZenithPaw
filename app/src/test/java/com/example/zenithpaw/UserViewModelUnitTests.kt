package com.example.zenithpaw

import com.example.zenithpaw.roomdatabase.shopitem.ShopItemRepository
import com.example.zenithpaw.roomdatabase.user.UserRepository
import com.example.zenithpaw.roomdatabase.userinventoryitem.UserInventoryItemRepository
import com.example.zenithpaw.ui.viewmodels.UserViewModel
import io.mockk.mockk
import kotlinx.coroutines.test.StandardTestDispatcher
import org.junit.Before

class UserViewModelUnitTests {
    // mock repository dependencies
    private val userRepository = mockk<UserRepository>()
    private val userInventoryItemRepository = mockk<UserInventoryItemRepository>()
    private val shopItemRepository = mockk<ShopItemRepository>()
    private val testDispatcher = StandardTestDispatcher()
    private lateinit var viewModel: UserViewModel

    /**
     * Initialize the user view model with mock repository dependencies
     */
    @Before
    fun setup(){
        viewModel = UserViewModel(userRepository, userInventoryItemRepository, shopItemRepository, testDispatcher)
    }

}