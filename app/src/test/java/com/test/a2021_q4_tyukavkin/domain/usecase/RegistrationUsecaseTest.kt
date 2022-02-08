package com.test.a2021_q4_tyukavkin.domain.usecase

import com.test.a2021_q4_tyukavkin.domain.entity.Auth
import com.test.a2021_q4_tyukavkin.domain.entity.User
import com.test.a2021_q4_tyukavkin.domain.entity.UserRole
import com.test.a2021_q4_tyukavkin.domain.repository.Repository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.Mockito

class RegistrationUsecaseTest {

    private val testAuth =
        Auth(
            username = "User",
            password = "qwe123"
        )

    private val testUser =
        User(
            name = "User",
            role = UserRole.USER
        )

    @ExperimentalCoroutinesApi
    @Test
    fun `WHEN invoke(testAuth) EXPECT testUser`() {
        runTest {
            val mockRepository = Mockito.mock(Repository::class.java)
            Mockito.`when`(mockRepository.register(testAuth)).thenReturn(testUser)
            val registrationUsecase = RegistrationUsecase(mockRepository)

            val actual = registrationUsecase(testAuth)

            val expected = testUser

            assertEquals(expected, actual)
        }
    }
}