package com.test.a2021_q4_tyukavkin.domain.usecase

import com.test.a2021_q4_tyukavkin.domain.repository.Repository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.Mockito

class UpdateLoansListUsecaseTest {

    @ExperimentalCoroutinesApi
    @Test
    fun `WHEN invoke() EXPECT true`() {
        runTest {
            val mockRepository = Mockito.mock(Repository::class.java)
            Mockito.`when`(mockRepository.updateLoansList()).thenReturn(true)
            val updateLoansListUsecase = UpdateLoansListUsecase(mockRepository)

            val actual = updateLoansListUsecase()

            val expected = true

            assertEquals(expected, actual)
        }
    }
}