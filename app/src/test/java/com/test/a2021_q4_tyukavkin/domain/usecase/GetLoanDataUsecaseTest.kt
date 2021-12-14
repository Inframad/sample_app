package com.test.a2021_q4_tyukavkin.domain.usecase

import com.test.a2021_q4_tyukavkin.domain.entity.Loan
import com.test.a2021_q4_tyukavkin.domain.entity.LoanState
import com.test.a2021_q4_tyukavkin.domain.repository.Repository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.Mockito
import java.time.OffsetDateTime

class GetLoanDataUsecaseTest {

    private val testId = 1L

    private val testLoan =
        Loan(
            amount = 10000F,
            offsetDateTime = OffsetDateTime.MAX,
            firstName = "John",
            lastName = "Johnson",
            id = 1L,
            percent = 10.5,
            period = 30,
            phoneNumber = "(123) 456-7890",
            state = LoanState.REGISTERED
        )

    @ExperimentalCoroutinesApi
    @Test
    fun `WHEN invoke(testId) EXPECT testLoan corresponds testId`() {
        runTest {
            val mockRepository = Mockito.mock(Repository::class.java)
            Mockito.`when`(mockRepository.getLoanData(testId)).thenReturn(testLoan)
            val getLoanDataUsecase = GetLoanDataUsecase(mockRepository)

            val actual = getLoanDataUsecase(testId).id

            val expected = testId

            assertEquals(expected, actual)
        }
    }
}