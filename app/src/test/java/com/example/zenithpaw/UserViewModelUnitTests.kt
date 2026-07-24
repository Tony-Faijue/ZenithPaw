package com.example.zenithpaw

import app.cash.turbine.test
import com.example.zenithpaw.roomdatabase.shopitem.ShopItem
import com.example.zenithpaw.roomdatabase.shopitem.ShopItemRepository
import com.example.zenithpaw.roomdatabase.user.User
import com.example.zenithpaw.roomdatabase.user.UserRepository
import com.example.zenithpaw.roomdatabase.userinventoryitem.UserInventoryItem
import com.example.zenithpaw.roomdatabase.userinventoryitem.UserInventoryItemRepository
import com.example.zenithpaw.ui.uievents.UserUiEvent
import com.example.zenithpaw.ui.viewmodels.UserViewModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals

class UserViewModelUnitTests {
    // mock repository dependencies
    private val userRepository = mockk<UserRepository>()
    private val userInventoryItemRepository = mockk<UserInventoryItemRepository>()
    private val shopItemRepository = mockk<ShopItemRepository>()
    private val testDispatcher = StandardTestDispatcher()

    /**
     * Set the default dispatcher for testing
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setup(){
        Dispatchers.setMain(testDispatcher)
    }

    /**
     * Reset the test dispatcher after each test
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun teardown(){
        Dispatchers.resetMain()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `when user repository is empty, the UI state updates errorMessage to 'No user found'`() = runTest(testDispatcher){
        // Arrange
        // Make sure the list of users is empty
        every { userRepository.getUsers() } returns flowOf(emptyList())

        // Initialize the view model
        val viewModel = UserViewModel(userRepository, userInventoryItemRepository, shopItemRepository, testDispatcher)

        // Act
        // test{} handles asserting the emissions from the flow
        viewModel.uiState.test {
            // Assert the initial state event
            val initialState = awaitItem()
            assert(initialState.isLoading)

            // Assert the error state event
            val errorState = awaitItem()
            assertEquals("No user found", errorState.errorMessage)
            assertEquals(false, errorState.isLoading)

            //Assert no more emission events are sent
            expectNoEvents()
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `when user repository contains a user, UI state is seeded with the user data`() = runTest(testDispatcher){
        // Arrange
        val testUser = User("JohnDoe", "johndoe@example.com", "imageurl.com", 500L, 0, "1")
        val testUsers = listOf(testUser)

        // Stub the results for the dependencies
        every { userRepository.getUsers() } returns flowOf(testUsers)
        every { userInventoryItemRepository.getUserInventoryItemsByUserId(testUser.userId) } returns flowOf(emptyList())
        every { shopItemRepository.getShopItems() } returns flowOf(emptyList())

        // Initialize the view model
        val viewModel = UserViewModel(userRepository, userInventoryItemRepository, shopItemRepository, testDispatcher)

        // Act
        // test{} handles asserting the emissions from the flow
        viewModel.uiState.test {
            // Assert the initial state
            val initialState = awaitItem()
            assert(initialState.isLoading)

            // Assert the user error state
            val userUiState = awaitItem()
            assertEquals(null, userUiState.errorMessage)

            // Assert the seeded data from the database
            assertEquals(testUser.name, userUiState.name)
            assertEquals(testUser.email, userUiState.email)
            assertEquals(testUser.imageUrl, userUiState.imageUrl)
            assertEquals(testUser.lastLogin, userUiState.lastLogin)
            assertEquals(testUser.gold, userUiState.gold)
            assertEquals(testUser.userId, userUiState.userId)

            assertEquals(false, userUiState.isLoading)

            // Ignore th rest of the state changes
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `when database updates, existing user typed changes are not overwritten`() = runTest(testDispatcher){
        // Arrange
        val initialUser = User("JohnDoe", "johndoe@example.com", "imageurl.com", 500L, 0, "1")
        val userFlow = MutableStateFlow(listOf(initialUser))

        every { userRepository.getUsers() } returns userFlow
        every { userInventoryItemRepository.getUserInventoryItemsByUserId(initialUser.userId) } returns flowOf(emptyList())
        every { shopItemRepository.getShopItems() } returns flowOf(emptyList())


        val viewModel = UserViewModel(userRepository, userInventoryItemRepository, shopItemRepository, testDispatcher)

        // Act
        viewModel.uiState.test {
            val initialState = awaitItem() // Initial loading state
            assert(initialState.isLoading)

            val loadedState = awaitItem() // Loaded state with initial data
            assertEquals(initialUser.name, loadedState.name)

            // Act -> Update the name (field the user is currently editing)
            viewModel.onEvent(UserUiEvent.OnNameChange("NewName"))
            val typedState = awaitItem()
            assertEquals("NewName", typedState.name)

            // Act -> Update the gold (field updated not directly by the user)
            val updatedDbUser = initialUser.copy(gold = 500)
            userFlow.value = listOf(updatedDbUser)

            // Assert -> The name should have changed AND gold is updated
            val finalState = awaitItem()
            assertEquals(500, finalState.gold)
            assertEquals("NewName", finalState.name)

            assertEquals(false, finalState.isLoading)
        }
    }

    @Test
    fun `when user account is created, new user is added to the database`() = runTest(testDispatcher){
        // Arrange
        every { userRepository.getUsers() } returns flowOf(emptyList())

        coEvery { userRepository.deleteAllUsers() } returns Unit
        coEvery {userRepository.upsertUser(any())} returns Unit

        val viewModel = UserViewModel(userRepository, userInventoryItemRepository, shopItemRepository, testDispatcher)

        // Act
        viewModel.uiState.test {
            val initialState = awaitItem() // Initial loading state
            assert(initialState.isLoading)

            // Act
            viewModel.onEvent(UserUiEvent.OnCreateAccountClicked("JohnDoe", "johndoe@example.com"))

            // Assert
            val finalState = awaitItem()
            assertEquals(false, finalState.isRegisteringDialogVisible)
            assertEquals(false, finalState.isLoading)


            coVerify(exactly = 1) { userRepository.deleteAllUsers() }
            coVerify(exactly = 1) { userRepository.upsertUser(any()) }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `when save profile is clicked, changes are saved to the database`() = runTest(testDispatcher){
        // Arrange
        val testUser = User("JohnDoe", "johndoe@example.com", "imageurl.com", 500L, 0, "1")
        val testUsers = listOf(testUser)

        every { userRepository.getUsers() } returns flowOf(testUsers)
        every { userInventoryItemRepository.getUserInventoryItemsByUserId(testUser.userId) } returns flowOf(emptyList())
        every { shopItemRepository.getShopItems() } returns flowOf(emptyList())

        coEvery {userRepository.upsertUser(any())} returns Unit

        val viewModel = UserViewModel(userRepository, userInventoryItemRepository, shopItemRepository, testDispatcher)

        viewModel.uiState.test {
            val initialState = awaitItem() // Initial loading state
            assert(initialState.isLoading)

            val loadedState = awaitItem() // Loaded state with initial data
            assertEquals(testUser.name, loadedState.name)

            // Act
            viewModel.onEvent(UserUiEvent.OnNameChange("NewName"))
            val typedStateName = awaitItem()
            assertEquals("NewName", typedStateName.name)

            viewModel.onEvent(UserUiEvent.OnEmailChange("newemail@example.com"))
            val typedStateEmail = awaitItem()
            assertEquals("newemail@example.com", typedStateEmail.email)

            viewModel.onEvent(UserUiEvent.OnSaveProfileClicked)
            advanceUntilIdle()

            // Assert
            assertEquals("NewName", typedStateEmail.name)
            assertEquals("newemail@example.com", typedStateEmail.email)
            assertEquals(false, typedStateEmail.isLoading)

            coVerify(exactly = 1) { userRepository.upsertUser(any()) }
        }
    }
}
