package com.example.zenithpaw

import app.cash.turbine.test
import com.example.zenithpaw.roomdatabase.pet.Pet
import com.example.zenithpaw.roomdatabase.pet.PetRepository
import com.example.zenithpaw.roomdatabase.pet.PetState
import com.example.zenithpaw.roomdatabase.pet.PetType
import com.example.zenithpaw.roomdatabase.shopitem.ShopItem
import com.example.zenithpaw.roomdatabase.shopitem.ShopItemRepository
import com.example.zenithpaw.roomdatabase.user.User
import com.example.zenithpaw.roomdatabase.user.UserRepository
import com.example.zenithpaw.roomdatabase.userinventoryitem.UserInventoryItem
import com.example.zenithpaw.roomdatabase.userinventoryitem.UserInventoryItemRepository
import com.example.zenithpaw.ui.viewmodels.PetViewModel
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

class PetViewModelUnitTests {
    private val petRepository = mockk<PetRepository>()
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

    @Test
    fun `when user repository is empty, the UI state updates errorMessage to 'No User Found'`() = runTest(testDispatcher) {
        //Arrange
        every { userRepository.getUsers() } returns flowOf(emptyList())
        every { petRepository.getPetsForUser(any()) } returns flowOf(emptyList())
        every { userInventoryItemRepository.getUserInventoryItemsByUserId(any()) } returns flowOf(
            emptyList()
        )
        every { shopItemRepository.getShopItems() } returns flowOf(emptyList())

        val viewModel = PetViewModel(
            petRepository,
            shopItemRepository,
            userRepository,
            userInventoryItemRepository,
            testDispatcher
        )

        // Act
        viewModel.uiState.test {
            // initial state
            awaitItem()
            // Assert
            // no user found error state
            val noUserErrorState = awaitItem()
            assertEquals("No User Found", noUserErrorState.errorMessage)

        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `when pet repository is empty, the UI state updates errorMessage to 'No Pets Found`() = runTest(testDispatcher){
        // Arrange
        val testUser = User("JohnDoe", "johndoe@example.com", "imageurl.com", 500L, 50, "1")

        every { userRepository.getUsers() } returns flowOf(listOf(testUser))
        every { petRepository.getPetsForUser(any()) } returns flowOf(emptyList())
        every { userInventoryItemRepository.getUserInventoryItemsByUserId(any()) } returns flowOf(emptyList())
        every { shopItemRepository.getShopItems() } returns flowOf(emptyList())

        val viewModel = PetViewModel(
            petRepository,
            shopItemRepository,
            userRepository,
            userInventoryItemRepository,
            testDispatcher
        )
        // Act
        viewModel.uiState.test {
            // initial state
            awaitItem()
            // Assert
            // pet error state
            val petErrorState = awaitItem()
            assertEquals("No Pets Found", petErrorState.errorMessage)

        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `when user repository contains a user with pets and inventory items, the user and other corresponding data is seeded in the UI state`() = runTest(testDispatcher){
        // Arrange
        val testUser = User("JohnDoe", "johndoe@example.com", "imageurl.com", 500L, 50, "1")

        val myPet1 = Pet("Rover", PetType.CAT, isDownloaded = false, petState = PetState.Idle, userId = testUser.userId, petId = "pet_id_1", imageUrl = "image_url_1", animationUrl = "animation_url_1", zen = 100)
        val myPet2 = Pet("Buddy", PetType.RABBIT, isDownloaded = false, petState = PetState.Idle, userId = testUser.userId, petId = "pet_id_2", imageUrl = "image_url_2", animationUrl = "animation_url_2", zen = 80)

        val shopItem1 = ShopItem("Carrot", "carrot.png", 10, "Carrot", "shop_item_id_1", "shop_id_1")
        val shopItem2 = ShopItem("Fish", "fish.png", 10, "Fish", "shop_item_id_1", "shop_id_1")

        val myInventoryItem1 = UserInventoryItem("user_item_1", testUser.userId, shopItem1.shopItemId, 3)
        val myInventoryItem2 = UserInventoryItem("user_item_2", testUser.userId, shopItem2.shopItemId, 1)

        every { userRepository.getUsers() } returns flowOf(listOf(testUser))
        every { petRepository.getPetsForUser(any()) } returns flowOf(listOf(myPet1, myPet2))
        every { userInventoryItemRepository.getUserInventoryItemsByUserId(any()) } returns flowOf(listOf(myInventoryItem1, myInventoryItem2))
        every { shopItemRepository.getShopItems() } returns flowOf(listOf(shopItem1, shopItem2))

        val viewModel = PetViewModel(
            petRepository,
            shopItemRepository,
            userRepository,
            userInventoryItemRepository,
            testDispatcher
        )

        // Act
        viewModel.uiState.test {
            // initial state
            awaitItem()
            // Assert
            // pet screen state
            val petScreenState = awaitItem()
            assertEquals("Rover", petScreenState.selectedPet?.name)
            assertEquals(2, petScreenState.pets.size)
            assertEquals(2, petScreenState.items.size)
            assertEquals(3, petScreenState.items[0].quantity)
        }
    }
}