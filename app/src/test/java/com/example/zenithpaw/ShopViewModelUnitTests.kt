package com.example.zenithpaw

import app.cash.turbine.test
import com.example.zenithpaw.roomdatabase.DefaultDispatcher
import com.example.zenithpaw.roomdatabase.shop.Shop
import com.example.zenithpaw.roomdatabase.shop.ShopRepository
import com.example.zenithpaw.roomdatabase.shopitem.ShopItem
import com.example.zenithpaw.roomdatabase.shopitem.ShopItemRepository
import com.example.zenithpaw.roomdatabase.user.UserRepository
import com.example.zenithpaw.roomdatabase.userinventoryitem.UserInventoryItemRepository
import com.example.zenithpaw.ui.viewmodels.ShopViewModel
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals

class ShopViewModelUnitTests {
    private val shopRepository = mockk<ShopRepository>()
    private val shopItemRepository = mockk<ShopItemRepository>()
    private val userRepository = mockk<UserRepository>()
    private val userInventoryItemRepository = mockk<UserInventoryItemRepository>()

    private val testDispatcher = StandardTestDispatcher()

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun teardown(){
        Dispatchers.resetMain()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `when user repository is empty, the UI state updates errorMessage to 'No user found'`() = runTest(testDispatcher) {
        // Arrange
        val shop = Shop("shop_id_1", "shop_name_1")
        val shopItem = ShopItem("Carrot", "carrot.png", 10, "Carrot", "shop_item_id_1", "shop_id_1")
        val shopItem2 = ShopItem("Apple", "apple.png", 5, "Apple", "shop_item_id_2", "shop_id_1")
        val shopItems = listOf(shopItem, shopItem2)

        every { userRepository.getUsers() } returns flowOf(emptyList())
        every { shopRepository.getShops() } returns flowOf(listOf(shop))
        every { shopItemRepository.getShopItemsByShopId(shop.shopId) } returns flowOf(shopItems)

        // Initialize viewmodel
        val viewModel = ShopViewModel(
            shopRepository,
            shopItemRepository,
            userRepository,
            userInventoryItemRepository,
            testDispatcher
        )

        // Act
        viewModel.uiState.test {
            val initialState = awaitItem()
            assert(initialState.isLoading)

            // Assert the error state
            val errorState = awaitItem()
            assertEquals("No User Found", errorState.errorMessage)
            assertEquals(false, errorState.isLoading)

            // Assert the shop state
            val shopState = awaitItem()
            assertEquals(shop.shopId, shopState.shopId)
            assertEquals(shop.name, shopState.name)

            // Assert the final state with updated available items
            val finalState = awaitItem()
            assertEquals(shopItems.size,finalState.availableItems.size)
            assertEquals(false, finalState.isLoading)
        }
    }
}