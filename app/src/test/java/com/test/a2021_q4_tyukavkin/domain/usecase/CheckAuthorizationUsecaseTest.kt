package com.test.a2021_q4_tyukavkin.domain.usecase

import com.test.a2021_q4_tyukavkin.domain.repository.Repository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.Mockito

class CheckAuthorizationUsecaseTest {

    @ExperimentalCoroutinesApi
    @Test
    fun `WHEN invoke() EXPECT true`() {
        runTest {
            val mockRepository = Mockito.mock(Repository::class.java)

            val checkAuthorizationUsecase = CheckAuthorizationUsecase(mockRepository)

            Mockito.`when`(mockRepository.checkAuthorization()).thenReturn(true)

            val actual = checkAuthorizationUsecase()

            val expected = true

            assertEquals(expected, actual)
        }
    }

}