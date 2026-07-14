package com.example.zenithpaw

import com.example.zenithpaw.roomdatabase.user.User
import com.example.zenithpaw.roomdatabase.user.UserDao
import com.example.zenithpaw.roomdatabase.user.UserRepositoryImpl
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Test

import org.junit.Assert.*

//See [testing documentation](http://d.android.com/tools/testing).
/**
 *Quick Example of Unit Test in ZenithPaw
 */
class UserRepositoryImplUnitTest {
    // Mock the dependencies
    private val mockDao: UserDao = mockk()
    private val testDispatcher = StandardTestDispatcher()

    // Instantiate the repository
    private val repository = UserRepositoryImpl(mockDao, testDispatcher)

    @Test
    fun `insertUser should call insert on the dao and add the user`() = runTest(testDispatcher){
        // Arrange
        val user = User("JohnDoe", "johndoe@example.com", "imageurl.com", 500L, 0, "1")
        coEvery { mockDao.insertUser(user) } returns Unit
        // Act
        repository.insertUser(user)
        // Assert
        coVerify(exactly = 1) { mockDao.insertUser(user) }
    }
}