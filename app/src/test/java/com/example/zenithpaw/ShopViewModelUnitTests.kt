package com.example.zenithpaw

import app.cash.turbine.test
import com.example.zenithpaw.roomdatabase.DefaultDispatcher
import com.example.zenithpaw.roomdatabase.shop.Shop
import com.example.zenithpaw.roomdatabase.shop.ShopRepository
import com.example.zenithpaw.roomdatabase.shopitem.ShopItem
import com.example.zenithpaw.roomdatabase.shopitem.ShopItemRepository
import com.example.zenithpaw.roomdatabase.user.User
import com.example.zenithpaw.roomdatabase.user.UserRepository
import com.example.zenithpaw.roomdatabase.userinventoryitem.UserInventoryItem
import com.example.zenithpaw.roomdatabase.userinventoryitem.UserInventoryItemRepository
import com.example.zenithpaw.ui.shop.ShopUiState
import com.example.zenithpaw.ui.shopitem.toShopItemUiState
import com.example.zenithpaw.ui.uievents.ShopUiEvent
import com.example.zenithpaw.ui.viewmodels.ShopViewModel
import io.mockk.coEvery
import io.mockk.coVerify
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

        every { userRepository.getUsers() } returns flowOf(emptyList())
        every { shopRepository.getShops() } returns flowOf(listOf(shop))
        every { shopItemRepository.getShopItemsByShopId(shop.shopId) } returns flowOf(shopItems)

        // Stub the suspended function results for the dependencies
        // return the shopItem object when getShopItemById is called internally
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

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `when buying a shop item and the user repository contains a user, the user gold is updated and new purchased item appears in user inventory item repository`() = runTest(testDispatcher){
        // Arrange
        val testUser = User("JohnDoe", "johndoe@example.com", "imageurl.com", 500L, 50, "1")
        // MutableStateFlow to simulate database changes
        val userFlow = MutableStateFlow(listOf(testUser))

        every { userRepository.getUsers() } returns userFlow

        // Define the behavior of the suspended functions for database changes

        // Simulate the database reaction to item purchase
        coEvery { userRepository.upsertUser(any()) } answers {
            val updatedUser = it.invocation.args[0] as User
            userFlow.value = listOf(updatedUser)
        }

        // Since adding new userInventoryItem start with user inventory item repository as null
        coEvery {
            userInventoryItemRepository.getUserInventoryItemByShopItemId(any(), any())
        } returns null

        coEvery { userInventoryItemRepository.insertUserInventoryItem(any()) } returns Unit

        val shop = Shop("shop_id_1", "shop_name_1")
        val shopItem = ShopItem("Carrot", "carrot.png", 10, "Carrot", "shop_item_id_1", "shop_id_1")
        val shopItem2 = ShopItem("Apple", "apple.png", 5, "Apple", "shop_item_id_2", "shop_id_1")
        val shopItems = listOf(shopItem, shopItem2)


        every { shopRepository.getShops() } returns flowOf(listOf(shop))
        every { shopItemRepository.getShopItemsByShopId(shop.shopId)} returns flowOf(shopItems)

        // Stub the suspended function results for the dependencies
        // return the shopItem object when getShopItemById is called internally
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
            // initial state
            awaitItem()

            // initial the gold state
            awaitItem()

            // initial shop state
            awaitItem()

            // Assert loaded ShopItems State
            val itemsLoadedState = awaitItem()
            assertEquals(shopItems.size, itemsLoadedState.availableItems.size)

            // Act : Select the item
            viewModel.onEvent(ShopUiEvent.OnPreviewItemClicked(shopItem.shopItemId))

            // Await event emission to update the selected item
            val selectedItemState = awaitItem()
            assertEquals(shopItem.shopItemId, selectedItemState.selectedItem?.shopItemId)

            // Act: Buy the item
            viewModel.onEvent(ShopUiEvent.OnConfirmPurchase)

            // Event emission to update the userInventoryItemRepository with new item
            awaitItem()

            // Assert the final gold balance
            val purchaseState = awaitItem()
            assertEquals(testUser.gold - shopItem.price, purchaseState.userBalance)

            // Assert the shopItem was added in user inventory item repository
            coVerify(exactly = 1){ userInventoryItemRepository.insertUserInventoryItem(any()) }
        }
    }


    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `when buying a shop item and the user repository contains a user, the user gold is updated and existing purchased item increases count in user inventory item repository`() = runTest(testDispatcher){
        // Arrange
        val testUser = User("JohnDoe", "johndoe@example.com", "imageurl.com", 500L, 50, "1")
        // MutableStateFlow to simulate database changes
        val userFlow = MutableStateFlow(listOf(testUser))

        val shop = Shop("shop_id_1", "shop_name_1")
        val shopItem = ShopItem("Carrot", "carrot.png", 10, "Carrot", "shop_item_id_1", "shop_id_1")
        val shopItem2 = ShopItem("Apple", "apple.png", 5, "Apple", "shop_item_id_2", "shop_id_1")
        val shopItems = listOf(shopItem, shopItem2)

        val myItem = UserInventoryItem("user_item_id_1",testUser.userId, shopItem.shopItemId, 1)

        every { userRepository.getUsers() } returns userFlow

        // Define the behavior of the suspended functions for database changes

        // Simulate the database reaction to item purchase
        coEvery { userRepository.upsertUser(any()) } answers {
            val updatedUser = it.invocation.args[0] as User
            userFlow.value = listOf(updatedUser)
        }

        // Since adding existing userInventoryItem start with user inventory item repository with an item
        coEvery {
            userInventoryItemRepository.getUserInventoryItemByShopItemId(any(), any())
        } returns myItem

        coEvery { userInventoryItemRepository.upsertUserInventoryItem(any()) } returns Unit

        every { shopRepository.getShops() } returns flowOf(listOf(shop))
        every { shopItemRepository.getShopItemsByShopId(shop.shopId)} returns flowOf(shopItems)

        // Stub the suspended function results for the dependencies
        // return the shopItem object when getShopItemById is called internally
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
            // initial state
            awaitItem()

            // initial the gold state
            awaitItem()

            // initial shop state
            awaitItem()

            // Assert loaded ShopItems State
            val itemsLoadedState = awaitItem()
            assertEquals(shopItems.size, itemsLoadedState.availableItems.size)

            // Act : Select the item
            viewModel.onEvent(ShopUiEvent.OnPreviewItemClicked(shopItem.shopItemId))

            // Await event emission to update the selected item
            val selectedItemState = awaitItem()
            assertEquals(shopItem.shopItemId, selectedItemState.selectedItem?.shopItemId)

            // Act: Buy the item
            viewModel.onEvent(ShopUiEvent.OnConfirmPurchase)

            // Event emission to update the userInventoryItemRepository with new item
            awaitItem()

            // Assert the final state gold balance & qty of item
            val purchaseState = awaitItem()
            assertEquals(testUser.gold - shopItem.price, purchaseState.userBalance)

            // Assert the shopItem was updated in user inventory item repository with increase in quantity by 1
            coVerify(exactly = 1){ userInventoryItemRepository.upsertUserInventoryItem(myItem.copy(quantity = myItem.quantity + 1)) }
        }
    }


    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `when buying a shop item and the user repository contains a user, with the user gold not enough updates the error message`() = runTest(testDispatcher){
        // Arrange
        val testUser = User("JohnDoe", "johndoe@example.com", "imageurl.com", 500L, 50, "1")

        every { userRepository.getUsers() } returns flowOf(listOf(testUser))

        // Simulate the database reaction to item purchase

        coEvery {
            userInventoryItemRepository.getUserInventoryItemByShopItemId(any(), any())
        } returns null

        val shop = Shop("shop_id_1", "shop_name_1")
        val shopItem = ShopItem("Carrot", "carrot.png", 100, "Carrot", "shop_item_id_1", "shop_id_1")
        val shopItem2 = ShopItem("Apple", "apple.png", 5, "Apple", "shop_item_id_2", "shop_id_1")
        val shopItems = listOf(shopItem, shopItem2)

        every { shopRepository.getShops() } returns flowOf(listOf(shop))
        every { shopItemRepository.getShopItemsByShopId(shop.shopId)} returns flowOf(shopItems)

        // Stub the suspended function results for the dependencies

        coEvery {userInventoryItemRepository.upsertUserInventoryItem(any())} returns Unit

        // return the shopItem object when getShopItemById is called internally
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
            // initial state
            awaitItem()

            // initial the gold state
            awaitItem()

            // initial shop state
            awaitItem()

            // Assert loaded ShopItems State
            val itemsLoadedState = awaitItem()
            assertEquals(shopItems.size, itemsLoadedState.availableItems.size)

            // Act : Select the item
            viewModel.onEvent(ShopUiEvent.OnPreviewItemClicked(shopItem.shopItemId))

            // Await event emission to update the selected item
            val selectedItemState = awaitItem()
            assertEquals(shopItem.shopItemId, selectedItemState.selectedItem?.shopItemId)

            // Act: Buy the item
            viewModel.onEvent(ShopUiEvent.OnConfirmPurchase)

            // Assert the error state
            val errorState = awaitItem()
            assertEquals("Not Enough Gold: When Buying: ${shopItem.name}, with Balance: $${testUser.gold}", errorState.errorMessage)
        }
    }

}