package com.example.zenithpaw

import app.cash.turbine.test
import com.example.zenithpaw.roomdatabase.DefaultDispatcher
import com.example.zenithpaw.roomdatabase.shop.Shop
import com.example.zenithpaw.roomdatabase.shop.ShopRepository
import com.example.zenithpaw.roomdatabase.shopitem.ShopItem
import com.example.zenithpaw.roomdatabase.shopitem.ShopItemRepository
import com.example.zenithpaw.roomdatabase.user.User
import com.example.zenithpaw.roomdatabase.user.UserRepository
import com.example.zenithpaw.roomdatabase.userinventoryitem.UserInventoryItemRepository
import com.example.zenithpaw.ui.shop.ShopUiState
import com.example.zenithpaw.ui.shopitem.toShopItemUiState
import com.example.zenithpaw.ui.uievents.ShopUiEvent
import com.example.zenithpaw.ui.viewmodels.ShopViewModel
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
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

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `when user repository contains a user, userBalance for ShopUiState is seeded with the user gold`() = runTest(testDispatcher) {
        // Arrange
        val testUser = User("JohnDoe", "johndoe@example.com", "imageurl.com", 500L, 50, "1")
        val shop = Shop("shop_id_1", "shop_name_1")
        val shopItem = ShopItem("Carrot", "carrot.png", 10, "Carrot", "shop_item_id_1", "shop_id_1")
        val shopItem2 = ShopItem("Apple", "apple.png", 5, "Apple", "shop_item_id_2", "shop_id_1")
        val shopItems = listOf(shopItem, shopItem2)

        every { userRepository.getUsers() } returns flowOf(listOf(testUser))
        every { shopRepository.getShops() } returns flowOf(listOf(shop))
        every { shopItemRepository.getShopItemsByShopId(shop.shopId) } returns flowOf(shopItems)

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

            val userGoldState = awaitItem()
            assertEquals(testUser.gold, userGoldState.userBalance)

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

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `when shop repository is empty, the UI state updates errorMessage to 'No shop found'`() = runTest(testDispatcher){
        // Arrange
        val testUser = User("JohnDoe", "johndoe@example.com", "imageurl.com", 500L, 50, "1")

        every { userRepository.getUsers() } returns flowOf(listOf(testUser))
        every { shopRepository.getShops() } returns flowOf(emptyList())
        every { shopItemRepository.getShopItemsByShopId(any()) } returns flowOf(emptyList())

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

            val userGoldState = awaitItem()
            assertEquals(testUser.gold, userGoldState.userBalance)

            // Assert the error state
            val errorState = awaitItem()
            assertEquals("No Shop Found", errorState.errorMessage)
            assertEquals(false, errorState.isLoading)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `when buying a shop item and the user repository is empty, the UI state updates errorMessage to 'No User Found When Buying ShopItemName'`() = runTest(testDispatcher){
        // Arrange
        val shop = Shop("shop_id_1", "shop_name_1")
        val shopItem = ShopItem("Carrot", "carrot.png", 10, "Carrot", "shop_item_id_1", "shop_id_1")
        val shopItem2 = ShopItem("Apple", "apple.png", 5, "Apple", "shop_item_id_2", "shop_id_1")
        val shopItems = listOf(shopItem, shopItem2)

        val availableItems = listOf(shopItem.toShopItemUiState(), shopItem2.toShopItemUiState())
        val shopUiStateFlow = MutableStateFlow(ShopUiState(shopId = shop.shopId, name = shop.name, availableItems = availableItems))

        every { userRepository.getUsers() } returns flowOf(emptyList())
        every { shopRepository.getShops() } returns flowOf(listOf(shop))
        every { shopItemRepository.getShopItemsByShopId(shop.shopId) } returns flowOf(shopItems)

        // Stub the suspended function results for the dependencies
        coEvery { shopItemRepository.getShopItemById(any()) } returns shopItem

        val viewModel = ShopViewModel(
            shopRepository,
            shopItemRepository,
            userRepository,
            userInventoryItemRepository,
            testDispatcher
        )

        // Act
        viewModel.uiState.test {
            awaitItem() // Initial loading state

            // Assert the error state
            val errorState = awaitItem()
            assertEquals("No User Found", errorState.errorMessage)

            val shopState = awaitItem()
            assertEquals(shop.shopId, shopState.shopId)
            assertEquals(shop.name, shopState.name)

            val itemsLoadedState = awaitItem()
            assertEquals(shopItems.size,itemsLoadedState.availableItems.size)

            // Act : Select the item
            viewModel.onEvent(ShopUiEvent.OnPreviewItemClicked(shopItem.shopItemId))

            // Await event emission to update the selected item
            val selectedItemState = awaitItem()
            assertEquals(shopItem.shopItemId, selectedItemState.selectedItem?.shopItemId)

            viewModel.onEvent(ShopUiEvent.OnConfirmPurchase)

            // Act: Attempt to buy the item
            val purchaseState = awaitItem()
            assertEquals("No User Found: When Buying: ${shopItem.name}", purchaseState.errorMessage)
        }

    }

}