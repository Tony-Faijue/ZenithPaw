package com.example.zenithpaw

import com.example.zenithpaw.roomdatabase.user.User
import com.example.zenithpaw.roomdatabase.user.UserDao
import com.example.zenithpaw.roomdatabase.user.UserRepositoryImpl
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test

import org.junit.Assert.*

//See [testing documentation](http://d.android.com/tools/testing).
/**
 *
 */
class UserRepositoryImplUnitTest {
    // Mock the dependencies
    private val mockDao: UserDao = mockk()

    // Instantiate the repository
    private val repository = UserRepositoryImpl(mockDao)

    @Test
    fun `insertUser should call insert on the dao and add the user`() = runTest{
        // Arrange
        val user = User("JohnDoe", "johndoe@example.com", "imageurl.com", 500L, 0, "1")
        coEvery { mockDao.insertUser(user) } returns Unit
        // Act
        repository.insertUser(user)
        // Assert
        coVerify(exactly = 1) { mockDao.insertUser(user) }
    }
}