package com.test.a2021_q4_tyukavkin.domain.usecase

import com.test.a2021_q4_tyukavkin.domain.entity.Loan
import com.test.a2021_q4_tyukavkin.domain.entity.LoanRequest
import com.test.a2021_q4_tyukavkin.domain.entity.LoanState
import com.test.a2021_q4_tyukavkin.domain.repository.Repository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.Mockito
import java.time.OffsetDateTime

class CreateLoanUsecaseTest {

    private val loanRequest =
        LoanRequest(
            amount = 10000,
            firstName = "John",
            lastName = "Johnson",
            percent = 10.5,
            period = 30,
            phoneNumber = "(123) 456-7890"
        )

    private val loan =
        Loan(
            amount = 10000F,
            offsetDateTime = OffsetDateTime.MAX,
            firstName = "John",
            lastName = "Johnson",
            id = 1,
            percent = 10.5,
            period = 30,
            phoneNumber = "(123) 456-7890",
            state = LoanState.REGISTERED
        )

    @ExperimentalCoroutinesApi
    @Test
    fun `WHEN invoke(testLoanRequest) EXPECT testLoan`() {
        runTest {
            val mockRepository = Mockito.mock(Repository::class.java)

            val testLoanRequest = loanRequest
            val testLoan = loan
            Mockito.`when`(mockRepository.createLoan(testLoanRequest)).thenReturn(testLoan)

            val createLoanUsecase = CreateLoanUsecase(mockRepository)

            val actual = createLoanUsecase(testLoanRequest)

            val expected = loan

            assertEquals(expected, actual)
        }
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `WHEN invoke(testLoanRequest) EXPECT testLoanRequest corresponds testLoan`() {
        runTest {
            val mockRepository = Mockito.mock(Repository::class.java)

            val testLoanRequest = loanRequest
            val testLoan = loan
            Mockito.`when`(mockRepository.createLoan(testLoanRequest)).thenReturn(testLoan)

            val createLoanUsecase = CreateLoanUsecase(mockRepository)

            val actual = createLoanUsecase(testLoanRequest)

            assertEquals(testLoanRequest.amount.toFloat(), actual.amount)
            assertEquals(testLoanRequest.firstName, actual.firstName)
            assertEquals(testLoanRequest.lastName, actual.lastName)
            assertEquals(testLoanRequest.lastName, actual.lastName)
            assertEquals(testLoanRequest.percent, actual.percent, 0.0)
            assertEquals(testLoanRequest.period, actual.period)
            assertEquals(testLoanRequest.phoneNumber, actual.phoneNumber)
        }
    }
}