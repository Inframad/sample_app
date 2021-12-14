package com.test.a2021_q4_tyukavkin.domain.usecase

import com.test.a2021_q4_tyukavkin.domain.entity.Loan
import com.test.a2021_q4_tyukavkin.domain.entity.LoanState
import com.test.a2021_q4_tyukavkin.domain.repository.Repository
import kotlinx.coroutines.flow.flow
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.Mockito
import java.time.OffsetDateTime

class GetAllLoansUsecaseTest {

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

    @Test
    fun `WHEN invoke() EXPECT fakeSource`() {
        val mockRepository = Mockito.mock(Repository::class.java)
        val testLoanList = listOf(testLoan)
        val fakeSource = flow { emit(testLoanList) }
        Mockito.`when`(mockRepository.getAllLoans()).thenReturn(fakeSource)
        val getAllLoansUsecase = GetAllLoansUsecase(mockRepository)

        val actual = getAllLoansUsecase()

        val expected = fakeSource

        assertEquals(expected, actual)
    }
}