package com.example.aquaminder.feature_login.domain.use_case

import com.example.aquaminder.core.utils.ResultEvent
import com.example.aquaminder.feature_login.data.repository.FakeUserRepository
import com.example.aquaminder.feature_login.domain.model.UserDomainModel
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class GetUserUseCaseTest {

    private lateinit var getUserUseCase: GetUserUseCase
    private lateinit var fakeUserRepository: FakeUserRepository

    @Before
    fun setUp() {
        fakeUserRepository = FakeUserRepository()
        getUserUseCase = GetUserUseCase(fakeUserRepository)
    }

    @Test
    fun `getUserUseCase returns Success when user is valid`() = runBlocking {
        // When
        fakeUserRepository.userToReturn = UserDomainModel("Test User", "test@mail.com", "password")
        val result = getUserUseCase("Test User", "password", "token")

        // Then
        assertTrue(result is ResultEvent.Success)
        val user = (result as ResultEvent.Success).data
        assertEquals("Test User", user.name)
        assertEquals("test@mail.com", user.mail)
        assertEquals("password", user.password)
    }
}