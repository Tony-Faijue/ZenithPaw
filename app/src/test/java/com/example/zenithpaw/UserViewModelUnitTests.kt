package com.example.zenithpaw

import app.cash.turbine.test
import com.example.zenithpaw.roomdatabase.shopitem.ShopItemRepository
import com.example.zenithpaw.roomdatabase.user.User
import com.example.zenithpaw.roomdatabase.user.UserRepository
import com.example.zenithpaw.roomdatabase.userinventoryitem.UserInventoryItemRepository
import com.example.zenithpaw.ui.viewmodels.UserViewModel
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
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

        // Act
        // Initialize the view model
        val viewModel = UserViewModel(userRepository, userInventoryItemRepository, shopItemRepository, testDispatcher)

        // Assert
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

        // Act
        // Initialize the view model
        val viewModel = UserViewModel(userRepository, userInventoryItemRepository, shopItemRepository, testDispatcher)

        // Assert
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

}